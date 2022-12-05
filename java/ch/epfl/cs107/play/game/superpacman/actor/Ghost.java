package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * An abstraction of the Ghosts.
 * @author H. REMMAL
 *
 */
public abstract class Ghost extends Player {
	
	private final SuperPacmanInteractionVisitor handler = new GhostHandler();
	private Player pacman;
	private static final int SPEED = SuperPacmanPlayer.getSpeed()*2;
	private static final int RAYON_VIEW = 5;
	private static final int GHOST_SCORE = 500;
	private final Sprite [][] spritesAfraid = RPGSprite.extractSprites("superpacman/ghost.afraid",2, 1, 1, this ,16, 16, new Orientation[] {Orientation.DOWN ,Orientation.LEFT , Orientation.UP, Orientation.RIGHT});
	private final Animation[] animationsAfraid = Animation.createAnimations(SuperPacmanPlayer.getSpeed()/2, spritesAfraid);
	private final DiscreteCoordinates SPAWN_POSITION;
	/** The field to know if the Ghost is afraid or not. False means his not afraid and true means he is.*/
	protected static boolean GHOST_AFRAID = false;
	/** The sprites of the ghost character.*/
	private Sprite [][] sprites;
	/** The animations of the ghost character.*/
	private Animation[] animations;
	
	/**
	 * Constructor of Ghosts.
	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
	 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	protected Ghost(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		this.SPAWN_POSITION = coordinates;
		sprites = RPGSprite.extractSprites(this.getTitle(),2, 1, 1, this ,16, 16, new Orientation[] {Orientation.UP ,Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
		animations = Animation.createAnimations(getSpeed()/2, sprites);
		this.resetMotion();
	}
	
	/**
	 * The pacman or the pacmanHelp if detected, otherwise null.
	 * @return The pacman or the PacmanHelp if one of them is detected, otherwise null.
	 */
	protected final Player getPacman() {
		return pacman;
	}

	protected final DiscreteCoordinates getSpawnPosition() {
		if(this instanceof Ghost) return this.SPAWN_POSITION;
		else return null;
	}
	
	/**
	 * Return the points we win every time we eat a ghost.
	 * @return The points we win every time we eat a ghost.
	 */
	public static final int getPoints() {
		return GHOST_SCORE;
	}
	
	/**
	 * Return the speed ghosts will have in the game.
	 * @return The speed ghosts will have in the game.
	 */
	public static final int getSpeed() {
		return SPEED;
	}
	
	/**
	 * This method will manage the movement for every ghost.
	 */
	protected abstract void getNextOrientation();
	
	/**
	 * 
	 * @return (String):
	 */
	protected abstract String getTitle() ;
	
	/**
	 * Method that manage when a ghost got eaten by a SuperPacmanPlayer instance.
	 * @param pacman (SuperPacmanPlayer): the pacman who eats the ghost
	 */
	protected void gotEatenByPacman(SuperPacmanPlayer pacman) {
		this.respawnByPacman();
	}
	
	/**
	 * A method that make the ghost respawn when pacman asked for it to happen.
	 */
	protected void respawnByPacman() {
			this.getOwnerArea().leaveAreaCells(this, getEnteredCells());
			this.setCurrentPosition(SPAWN_POSITION.toVector());
			this.getOwnerArea().enterAreaCells(this, getCurrentCells());
			this.resetMotion();
	}
	
	/**
	 * This method manage the animations of the sprites in the game.
	 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
	 */
	private void manageAnimations(float deltaTime) {
		if(GHOST_AFRAID) animationsAfraid[Orientation.DOWN.ordinal()].update(deltaTime);
		else 			 animations[this.getOrientation().ordinal()].update(deltaTime);
	}
	
	/**
	 * Simulates a single time step.
	 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
	 */
	@Override
	public void update(float deltaTime) {
		if(this.isDisplacementOccurs()) manageAnimations(deltaTime);
		else 							this.getNextOrientation();
		pacman = null;		// reset so every time the ghost will see if pacman is in his view.
		super.update(deltaTime);
	}
	
	/**
	 * A method to draw the sprites of the player.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public final void draw(Canvas canvas) {
		if(GHOST_AFRAID) animationsAfraid[Orientation.DOWN.ordinal()].draw(canvas);
		else 			 animations[this.getOrientation().ordinal()].draw(canvas);
	}
	
	/**
	 * Get this Ghost's current occupying cells coordinates.
	 * @return (List of DiscreteCoordinates). May be empty but not null.
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
		list.add(this.getCurrentMainCellCoordinates());
		return list;
	}
	
	/**
	 * Get this Ghost's current field of view cells coordinates
	 * @return (List of DiscreteCoordinates). May be empty but not null.
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
			for(int x = -RAYON_VIEW ; x <= RAYON_VIEW ;x++) {
				for(int y = -RAYON_VIEW ; y <= RAYON_VIEW ; y++){
					list.add(this.getCurrentMainCellCoordinates().jump(x, y));
				}
			}
		return list;
	}

	/**
	 * Indicate if the current Interactable take the whole cell space or not.
	 * @return (boolean) false.
	 */
	@Override
	public boolean takeCellSpace() { 
		return false;
	}
	
	/**
	 *  Returns if this object is able to have cell interactions.
	 *  @return (boolean) true.
	 */
	@Override
	public boolean isCellInteractable() {
		return  true;
	}
	
	/**
	 *  Returns if this object is able to have view interactions.
	 *  @return (boolean) false.
	 */
	@Override
	public boolean isViewInteractable() {
		return false;
	}
	
	/**
	 *  Returns if this object require cell interaction.
	 *  @return (boolean): false.
	 */
	@Override
	public boolean wantsCellInteraction() {
		return false ;
	}
	
	/**
	 *  Returns if this object require view interaction.
	 *  @return (boolean): true.
	 */
	@Override
	public boolean wantsViewInteraction() {
		return true;
	}
	
	/**
	 * Call directly the interaction on this if accepted
	 * @param v (AreaInteractionVisitor): The visitor.
	 */
	 @Override
	 public void acceptInteraction(AreaInteractionVisitor v) {
		 ((SuperPacmanInteractionVisitor)v).interactWith(this);
	 }
	 
	 /**
	  * Ghost interact with the given Interactable.
	  * @param other (Interactable): Not null.
	  */
	 @Override
	public void interactWith(Interactable other) {
		 other.acceptInteraction(handler);
	}
	 
	 /**
	  * This class is the handler of this player.
	  * @author H. REMMAL
	  *
	  */
	 private final class GhostHandler implements SuperPacmanInteractionVisitor{
		 
		 /**
		  * Manage the view interaction with the pacman.
		  * @param pacman (SuperPacmanPlayer): the detected SuperPacmanPlayer object.
		  */
		 @Override
		 public void interactWith(SuperPacmanPlayer pacman) {
			 Ghost.this.pacman = pacman;
			}
		 
		 /**
		  * Manage the view interaction with the pacman.
		  * @param pacman (PacmanHelp): the detected PacmanHelp object.
		  */
		 @Override
		 public void interactWith(PacmanHelp pacman) {
			 Ghost.this.pacman = pacman;
			}
	 }
}
