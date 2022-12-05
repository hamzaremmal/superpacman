package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class represents an abstraction of the character : Ghost with arrows.
 * @author H. REMMAL
 */
public final class GhostArrow extends Pinky {
	
	private Arrow arrow;
	
	/**
	 * The constructor of the Ghost who can throw arrows.
	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
	 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	public GhostArrow(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		this.resetMotion();
	}
	
	/**
	 * Return the given name of the Actor.
	 */
	@Override
	protected String getTitle() {
		return "superpacman/ghost.clyde";
	}
	
	 /**
	  * Arrow interact with the given Interactable.
	  * @param other (Interactable): Not null.
	  */
	@Override
	public void interactWith(Interactable other) {
		super.interactWith(other);
		if(other instanceof SuperPacmanPlayer || other instanceof PacmanHelp) {
			Player player =  ((Player)other);
			Vector sub =player.getPosition().sub(this.getPosition());
			if(sub.x == 0 || sub.y == 0) {
				if(!GHOST_AFRAID) {
					this.throwArraow(this.getOrientation());	
				}	
			}
		}
	}

	/**
	 * This method gives to the ghost the faculty to throw arrows.
	 * @param orientation (Orientation): The orientation the arrow will have.
	 */
	private void throwArraow(Orientation orientation) {
		if(arrow == null || !this.getOwnerArea().exists(arrow)) {
			arrow = new Arrow(this.getOwnerArea(),orientation,this.getCurrentMainCellCoordinates());
			this.getOwnerArea().registerActor(arrow);
		}
	}

	/**
	 * This class represents the abstraction for the arrows thrown by the ghost.
	 * @author H. REMMAL
	 */
	public final class Arrow extends MovableAreaEntity implements Interactor {

		private final Sprite [][] spritesArrow;
		private final SoundAcoustics music = new SoundAcoustics(ResourcePath.getSounds("Bonus Count"), 0.5f, false, false, false, false);
		private boolean bip = false;
		
		/**
		 * This is the constructor of the Arrow object.
		 * @param area (Area): The designate area to spawn in.
		 * @param orientation (Orientation): The orientation it starts with.
	     * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
		 */
		private Arrow(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
			super(area, orientation, coordinates);
			music.shouldBeStarted();
			spritesArrow = Sprite.extractSprites("zelda/arrow",4, 1, 1, this ,32, 32, new Orientation[] {Orientation.UP ,Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT});
			this.resetMotion();
		}

		/**
		 * Get this Arrow's current occupying cells coordinates.
		 * @return (List of DiscreteCoordinates). May be empty but not null.
		 */
		@Override
		public List<DiscreteCoordinates> getCurrentCells() {
			List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
			list.add(this.getCurrentMainCellCoordinates());
			return list;
		}

		/**
		 * Simulates a single time step.
		 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
		 */
		@Override
		public void update(float deltaTime) {
			if(!this.isDisplacementOccurs()) this.move((int)(getSpeed()*0.5));
			if(!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells())) deleteArrow();
			super.update(deltaTime);
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
		 *  @return (boolean) false.
		 */
		@Override
		public boolean isViewInteractable() {
			return false;
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
		 * A method to draw the sprites of the player.
		 * @param canvas (Canavs): The canavs to draw on.
		 */
		@Override
		public void draw(Canvas canvas) {
			this.spritesArrow[0][this.getOrientation().ordinal()].draw(canvas);
		}
		
		/**
		 * A method to manage the sound of the player.
		 * @param audio (Audio): The audio canal to play in.
		 */
		@Override
		public void bip(Audio audio) {
			if(bip) music.bip(audio);
			bip = false;
		}

		/**
		 * A method to delete the arrow object from the game in case it hits a player or a wall.
		 */
		private final void deleteArrow() {
			this.resetMotion();
			this.getOwnerArea().unregisterActor(this);
			GhostArrow.this.arrow = null;
		}

		@Override
		public List<DiscreteCoordinates> getFieldOfViewCells() {
			return null;
		}

		@Override
		public boolean wantsCellInteraction() {
			return true;
		}

		@Override
		public boolean wantsViewInteraction() {
			return false;
		}
		
		@Override
		public final void interactWith(Interactable other) {
			if(other instanceof SuperPacmanPlayer) {
				this.deleteArrow();
				((SuperPacmanPlayer)other).respawn();
				this.bip = true;
				((SuperPacmanPlayer)other).hit();
			}else if(other instanceof PacmanHelp) {
				this.deleteArrow();
				this.bip = true;
			}
			
		}
	}
}
