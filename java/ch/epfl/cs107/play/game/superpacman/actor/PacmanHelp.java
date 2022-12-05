package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
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
import ch.epfl.cs107.play.game.superpacman.actor.GhostArrow.Arrow;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer.SuperPacmanPlayerHandler;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacManArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
	
/**
 * This class is a representation of a pacman player who helps the SuperPacmanPlayer. This player has the same shape of a SuperPacman Player but we don't control it.
 * This class follows the design pattern : Singleton, so we only can create one instance of this class.
 * @author H. REMMAL
 */
public final class PacmanHelp extends Player {
	
		private final SuperPacmanInteractionVisitor HANDLER;
		private static PacmanHelp instance;
		private DiscreteCoordinates cible;
		private Queue<Orientation> orientationPath;
		private final Sprite [][] sprites = RPGSprite.extractSprites("superpacman/pacman",4, 1, 1, this , 64, 64, new Orientation[] {Orientation.DOWN ,Orientation.LEFT , Orientation.UP, Orientation.RIGHT});;
		private final Animation[] animations = Animation.createAnimations(SuperPacmanPlayer.getSpeed()/2, sprites);
		private double pendingScore = 0;
		
		/**
		 * Creates a instance of the PacmanHelp.
		 * @param area (Area): The designate area to spawn in.
		 * @param pacman (SuperPacmanPlayer): The player.
		 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
		 */
		private PacmanHelp(Area area, DiscreteCoordinates coordinates , SuperPacmanPlayer pacman) {
			super(area, Orientation.RIGHT ,coordinates);
			HANDLER = new HelperHandler(pacman);
			this.resetMotion();
		}
		
		/**
		 * Returns the instance.
		 * @param area (Area): The area where to spawn the pacman.
		 * @param coordinates (DiscreteCoordinates): The coordinates where to spawn the pacman.
		 * @return instance (PacmanHelp): The instance we deal with in the game.
		 */
		public static final PacmanHelp getInstance(Area area, DiscreteCoordinates coordinates , SuperPacmanPlayer pacman) {
			if(instance == null) instance = new PacmanHelp(area,coordinates,pacman);
			return instance;
		}
		
		/**
		 * To get the pending points our player collected to transfer them to the master player (SuperPacmanPlayer)
		 * @return pendingScore
		 */
		protected double getPendingScore() {
			return pendingScore ;
		}

		/**
		 * This method manage the movement for this player.
		 */
		private void getNextOrientation() {
			if(orientationPath != null && !orientationPath.isEmpty()) {
				this.orientate(orientationPath.poll());
				this.move(SuperPacmanPlayer.getSpeed()*2);
			}
			else {
				specialMovement();
			}
		}
		
		/**
		 * A intern method that helps with managing the movement. It makes us have a random position and a path to follow.
		 */
		private void specialMovement() {
			do {
				randomPosition();
			}while(orientationPath == null || orientationPath.isEmpty());
		}
		
		/**
		 * Gets a random achievable position without any restriction
		 */
		private void randomPosition() {
			int randX = new Random().nextInt(this.getOwnerArea().getWidth());
			int randY = new Random().nextInt(this.getOwnerArea().getHeight());
			cible = new DiscreteCoordinates(randX, randY);
			orientationPath = ((SuperPacManArea) this.getOwnerArea()).getBehavior(). getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), cible);
		}
		
		/**
		 * Update the animation every single time step
		 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative
		 */
		private void manageAnimations(float deltaTime) {
			animations[this.getOrientation().ordinal()].update(deltaTime);
		}
		
		/**
		 * To unregister the arrow when needed(hit the player or a wall)
		 */
		private void leave() {
			this.resetMotion();
			this.getOwnerArea().unregisterActor(this);
		}
		
		/**
		 * Simulates a single time step.
		 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative
		 */
		@Override
		public void update(float deltaTime) {
			if(this.isDisplacementOccurs()) manageAnimations(deltaTime);
			else 							this.getNextOrientation();																		
			super.update(deltaTime);
		}
		
		/**
		 * Draw the sprites of the player in the canavas.
		 * @param canvas (Canvas): Canvas where to draw the sprites.
		 */
		@Override
		public void draw(Canvas canvas) {
			animations[this.getOrientation().ordinal()].draw(canvas);
			Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth()/2, canvas.getScaledHeight()/2));
			
			 new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
					1.f, 1.f, new RegionOfInterest(0, 0, 64, 64),
					anchor.add(new Vector(1,1)), 1, 1).draw(canvas);
		
		}
	
		/**
		 * Get this Pacman's current occupying cells coordinates
		 * @return (List of DiscreteCoordinates). May be empty but not null
		 */
		@Override
		public List<DiscreteCoordinates> getCurrentCells() {
			List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
			list.add(this.getCurrentMainCellCoordinates());
			return list;
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
		public boolean takeCellSpace() {
			return false;
		}

		
		@Override
		public boolean isCellInteractable() {
			return false;
		}
		
		@Override
		public void interactWith(Interactable other) {
			other.acceptInteraction(HANDLER);
			
		}

		@Override
		public boolean isViewInteractable() {
			return true;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
			((SuperPacmanInteractionVisitor)v).interactWith(this);
		}
		 
		 /**
		  * The PacmanHelp handler
		  * @author H. REMMAL
		  *
		  */
		private final class HelperHandler extends SuperPacmanPlayerHandler{
			 
			private final SuperPacmanPlayer pacman ;
			
			/**
			 * Constructor of the Arrow
			 * @param pacman (SuperPacmanPlayer): The pacman we help in the game.
			 */
			private HelperHandler(SuperPacmanPlayer pacman) {
				pacman.super();
				this.pacman = pacman;
			}
			
			/**
			 * An override of the method so we can create one instance at every level. Do the same thing as the original method.
			 * @param door (Door): The door we interact with.
			 */
			@Override
			public void interactWith(Door door) {
				super.interactWith(door);
				PacmanHelp.instance = null;
			}
			
			/**
			 * Manage the cell contact with the Ghost
			 * @param ghost (Ghost): The ghost we interact with.
			 */
			@Override
		    public void interactWith(Ghost ghost) {
				if(!Ghost.GHOST_AFRAID) {
					ghost.respawnByPacman();
					PacmanHelp.this.leave();
				}
				else {
					ghost.respawnByPacman();
					PacmanHelp.this.pendingScore = Ghost.getPoints();
					pacman.setScore(instance);
					pendingScore = 0;
				}
			}
			 
			/**
			 * Manage the cell contact with the Arrow
			 * @param arrow (Arrow): The arrow we interact with.
			 */
			@Override
			public void interactWith(Arrow arrow) {
				if(!Ghost.GHOST_AFRAID) {
					// The class Arrow manages too the interaction so that she could delete the arrow
					PacmanHelp.this.leave();
				}
			}
			 
		 }

		


	

		

}
