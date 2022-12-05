package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Timer;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.actor.SuperPacmanPlayerStatusGUI;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacManArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * The abstraction of a SuperPacmanPlayer.
 * @author H. REMMAL - M. ZIAZI
 */
public final class SuperPacmanPlayer extends Player {
	
	private final SuperPacmanInteractionVisitor handler = new SuperPacmanPlayerHandler();
	private Orientation desiredOrientation;
	private final DiscreteCoordinates SPAWN_POSITION;
	private static int speed = 6;
	private final Sprite [][] sprites;
	private final Animation[] animations;
	private boolean isAccelerating = false;
	private PacmanHelp helper;
	private final Timer timAcceleration = new Timer(5000,
			e->{ if(!isAccelerating) {
					((Timer) e.getSource()).stop();
					speed=6;
				} else {
					speed=2;
					isAccelerating = false;
				}});
	private SoundAcoustics music = new SoundAcoustics(ResourcePath.getSounds("Road104"), 0.2f, false, false, true, true); // MUSIC DU JEU A VOIR OU PLACER, PAS DANS LE PERSO
	private int pv = 3;
	private double score = 0;
	private Graphics status = new SuperPacmanPlayerStatusGUI(this,this.getOwnerArea());
	private Timer timer = new Timer(10000, e -> {
					Ghost.GHOST_AFRAID = false;
					((Timer)e.getSource()).stop();
					});
	
	/**
	 * The constructor of the SuperPacmanPlayer.
	 * @param area (Area): The designate area to spawn in.
	 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	public SuperPacmanPlayer(Area area, DiscreteCoordinates coordinates) {
		super(area, Orientation.RIGHT, coordinates);
		status = new SuperPacmanPlayerStatusGUI(this,this.getOwnerArea());
		sprites 	= RPGSprite.extractSprites("superpacman/pacman",4, 1, 1, this , 64, 64, new Orientation[] {Orientation.DOWN ,Orientation.LEFT , Orientation.UP, Orientation.RIGHT});
		animations 	= Animation.createAnimations(getSpeed()/2, sprites);
		this.SPAWN_POSITION = coordinates;
		music.shouldBeStarted();
		this.resetMotion();
	}
	
	/**
	 * Return The speed of the SuperPacmanPlayer.
	 * @return speed (int): The speed of the SuperPacmanPlayer
	 */
	public static final int getSpeed() {
		return speed;
	}
	
	/**
	 * Sets isAccelerating to true if the player has collected an Orb
	 * @param b(boolean)
	 */
	protected void accelerate(boolean b) {
		this.isAccelerating=b;
	}
	
	/**
	 * Increases the player's pv by 2 at most.
	 */
	protected void heals() {
		if(this.pv<4) this.pv+=2;
		else if (this.pv<5) this.pv++;
	}
	

	/**
	 * Gives the PacmanHelp object the possibilities to add the points he collected to the player's score.
	 * @param pacman (PacmanHelp): the object who helps pacman in the game
	 */
	protected final void setScore(PacmanHelp pacman) {
		score += pacman.getPendingScore();
	}
	/**
	 *  Called everytime the player is hited.
	 */
	protected void hit() {
		this.pv--;
			if(this.isDead()) {
				if(helper != null)helper.leaveArea();
				SuperPacmanPlayer.this.getOwnerArea().unregisterActor(SuperPacmanPlayer.this);
				SuperPacmanPlayer.this.getOwnerArea().leaveAreaCells(SuperPacmanPlayer.this, SuperPacmanPlayer.this.getCurrentCells());
			}
	}
	
	/**
	 * Manage the case where our pacman got eaten by a ghost
	 * @param ghost (Ghost): the ghost who eated our player
	 */
	private void gotEatenByGhost(Ghost ghost) {
		ghost.respawnByPacman();
		this.respawn();
	}
	
	/**
	 * Manages with the area the position of the player so it can respawn on his SPAWN_POSITION
	 */
	protected void respawn() {
		this.getOwnerArea().leaveAreaCells(this, getEnteredCells());
		this.setCurrentPosition(SPAWN_POSITION.toVector());
		this.getOwnerArea().enterAreaCells(this, getCurrentCells());
		this.resetMotion();
		this.desiredOrientation = null;
		this.animations[0].reset();
	}
	
	/**
	 * Simulates a single time step.
	 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
	 */
	@Override
    public void update(float deltaTime) {
		if(isAccelerating) {timAcceleration.setInitialDelay(1);
		timAcceleration.start();}
		Keyboard keyboard = getOwnerArea().getKeyboard();
		if		(keyboard.get(Keyboard.LEFT).isDown()) 		desiredOrientation = Orientation.LEFT;
		else if(keyboard.get(Keyboard.RIGHT).isDown()) 		desiredOrientation = Orientation.RIGHT;
		else if(keyboard.get(Keyboard.UP).isDown()) 		desiredOrientation = Orientation.UP;
		else if(keyboard.get(Keyboard.DOWN).isDown()) 		desiredOrientation = Orientation.DOWN;
		if	(keyboard.get(Keyboard.P).isDown() && this.getScore() > 1000 && helper == null) {
			helper = PacmanHelp.getInstance(this.getOwnerArea(), this.getCurrentMainCellCoordinates(),this);
			this.getOwnerArea().registerActor(helper);
			score -=1000;
		}
		if(desiredOrientation != null &&!isDisplacementOccurs() && this.getOwnerArea().canEnterAreaCells(this,Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
			this.orientate(desiredOrientation);
			this.move(speed);
		}
		if(desiredOrientation != null) animations[desiredOrientation.ordinal()].update(deltaTime);
		super.update(deltaTime); 
    }
	
	/**
	 * Draw the sprites of the player in the canavas.
	 * @param canvas (Canvas): Canvas where to draw the sprites.
	 */
	@Override
	public void draw(Canvas canvas) {
		if(desiredOrientation != null)	animations[desiredOrientation.ordinal()].draw(canvas);
		else							animations[0].draw(canvas);
		status.draw(canvas);
	}
	
	/**
	 * A method to manage the sound of the player.
	 * @param audio (Audio): The audio canal to play in.
	 */
	@Override
	public void bip(Audio audio){
		if(!Ghost.GHOST_AFRAID) {
			music.bip(audio);
		}
		else {
			SoundAcoustics.stopAllSounds(audio);
			music.shouldBeStarted();
		}
		if(this.pv == 0) {
			SoundAcoustics.stopAllSounds(audio);
			SoundAcoustics sound = new SoundAcoustics(ResourcePath.getSounds("Pac-Man Dies"), 0.5f, false, false, false, false);
			sound.shouldBeStarted();
			sound.bip(audio);
		}
	}
	
	/**
	 * Get this SuperPacmanPlayer's current occupying cells coordinates
	 * @return (List of DiscreteCoordinates). May be empty but not null
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
		list.add(this.getCurrentMainCellCoordinates());
		return list;
	}
	
	/**
	 * Get this SuperPacmanPlayer's current field of view cells coordinates
	 * @return null.
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}

	/**
	 *  Returns if this object require cell interaction.
	 *  @return (boolean): true.
	 */
	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	/**
	 *  Returns if this object require view interaction.
	 *  @return (boolean): false.
	 */
	@Override
	public boolean wantsViewInteraction() {
		return false;
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
		return true;
	}
	
	/**
	 *  Returns if this object is able to have view interactions.
	 *  @return (boolean) true.
	 */
	@Override
	public boolean isViewInteractable() {
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
	  * SuperPacmanPlayer interact with the given Interactable.
	  * @param other (Interactable): Not null.
	  */
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	/**
	 * Return the PV our player have.
	 * @return (int): PV
	 */
	public final int getPV(){
		return this.pv;
	}
	
	/**
	 * Return the score.
	 * @return (double): score.
	 */
	public final double getScore() {
		return score;
	}
	
	/**
	 * Return the status of our player(alive or dead)
	 * @return (boolean): true means dead
	 */
	public final boolean isDead() {
		if(this.pv <= 0f) return true;
		return false;
	}
	
	/**
	 * SuperPacman Handler class.
	 * @author H. REMMAL - M. ZIAZI
	 */
	protected class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {
		
		/**
		 * Manage the cell contact with the Door.
		 * @param door (Door): The door we interact with.
		 */
		@Override
		public void interactWith(Door door) {
			SuperPacmanPlayer.this.setIsPassingADoor(door);
			Ghost.GHOST_AFRAID = false;
			SuperPacmanPlayer.this.helper = null;
		}
		
		/**
		 * Manage the cell contact with the Ghost
		 * @param ghost (Ghost): The ghost we interact with.
		 */
		@Override
		public void interactWith(Ghost ghost) {
			if(!Ghost.GHOST_AFRAID)	{
				SuperPacmanPlayer.this.gotEatenByGhost(ghost);
				SuperPacmanPlayer.this.hit();
			}
			else {
				ghost.gotEatenByPacman(SuperPacmanPlayer.this);
				SuperPacmanPlayer.this.score+= Ghost.getPoints();
			}
		}
		
		/**
		 * Manage the cell contact with the Bonus
		 * @param bonus (Bonus):The bonus we interact with.
		 */
		public void interactWith(Bonus bonus) {
			SuperPacmanPlayer.this.getOwnerArea().unregisterActor(bonus);
			Ghost.GHOST_AFRAID = true;
			bonus.bip = true;
			timer.start();
			((SuperPacManArea) SuperPacmanPlayer.this.getOwnerArea()).removeCollectable(bonus);
		}
		
		/**
		 * Manage the cell contact with the Cherry
		 * @param cherry (Cherry): The cherry we interact with.
		 */
		public void interactWith(Cherry cherry) {
			SuperPacmanPlayer.this.getOwnerArea().unregisterActor(cherry);
			SuperPacmanPlayer.this.score += Cherry.getPoints();
			((SuperPacManArea) SuperPacmanPlayer.this.getOwnerArea()).removeCollectable(cherry);
		}
		
		/**
		 * Manage the cell contact with the Diamond
		 * @param diamond (Diamond): The diamond we interact with.
		 */
		public void interactWith(Diamond diamond) {
			SuperPacmanPlayer.this.score+=diamond.getPoints();
			SuperPacmanPlayer.this.getOwnerArea().unregisterActor(diamond);
			((SuperPacManArea) SuperPacmanPlayer.this.getOwnerArea()).removeCollectable(diamond);
		
		}
		
		/**
		 * Manage the cell contact with the Key
		 * @param key (Key): The key we interact with.
		 */
		@Override
		public void interactWith(Key key) {
			Area area = SuperPacmanPlayer.this.getOwnerArea();
			if(area.getTitle().equals("superpacman/Level0")) {
				((Level0) area).setKeySignal(Logic.TRUE);
			}
			if(area.getTitle().equals("superpacman/Level2")) {
				for(int i = 0 ; i < 4 ; i++) {
					if(key.equals(((Level2) area).getKey(i))) {
						((Level2) area).setKeySignal(i,Logic.TRUE);
					}
				}
			}
			SuperPacmanPlayer.this.getOwnerArea().unregisterActor(key);
		}
	}

}
