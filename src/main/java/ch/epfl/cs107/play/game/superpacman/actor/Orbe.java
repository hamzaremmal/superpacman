package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.List;

import ch.epfl.cs107.play.game.superpacman.area.SuperPacManArea;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer.SuperPacmanPlayerHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class is a representation of an orb that accelerate the SuperPacman for 5 seconds. 
 * This orb appears when a at least 3 bonuses are eaten, in one of their positions(randomly)
 * Changes position (randomly) each 5 seconds.
 * Only one instance is created.
 * @author M. ZIAZI
 */
public class Orbe extends CollectableAreaEntity implements Interactor{

	private Animation[] animations ;
	private Animation courante;
	private static Orbe myOrb;
	
	/**
	 * Creates an instance of Orbe 
	 * @param area(Area): the area that contains the instance.
	 * @param orientation(Orientation): orientation of the instance.
	 * @param position(DiscreteCorrdinates): position of the instance within the area.
	 */
	private Orbe(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		 Sprite[][] sprites=  RPGSprite.extractSprites("zelda/orb",
				6, 1, 1,
				this , 32, 32,new Orientation[] {Orientation.DOWN ,
						Orientation.LEFT , Orientation.UP, Orientation.RIGHT});
		
		animations= Animation.createAnimations(FRAMES/3, sprites);
		courante= animations[orientation.ordinal()];}
	
	/**
	 * A method to draw the sprite of the orb and it's animation.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		courante= animations[Orientation.UP.ordinal()];
		courante.update(FRAMES);
		courante.draw(canvas);}

	/**
	 * Handles the interactions of the heart.
	 * @param v(AreaInteractionVisitor): handles the interactions.
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanPlayerHandler)v).interactWith(this);
	}
	/**
	 * Spawns a unique Orb in an area.
	 * @param area(SuperPacManArea): owner area of the orb.
	 * @param coord(DiscreteCoordinates ): coordinates of the orb within the area.
	 */
	public static void spawn(DiscreteCoordinates coord, SuperPacManArea area) {
		if(myOrb==null)  myOrb= new Orbe(area, Orientation.DOWN,coord);	
		else  myOrb.updateOrb(coord,area);
		myOrb.getOwnerArea().registerActor(myOrb);
	}
	
	/**
	 * Changes the position of the orb.
	 * @param area(SuperPacManArea): owner area of the orb.
	 * @param coord(DiscreteCoordinates ): coordinates of the orb within the area.
	 */
	private void updateOrb(DiscreteCoordinates coord,Area area) {
		if(!(myOrb==null)) {
			myOrb.getOwnerArea().unregisterActor(myOrb);
			myOrb.setOwnerArea(area);
			myOrb.setCurrentPosition(coord.toVector());
			
		}
	}

	/**
	 * Returns null.
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}
	
	/**
	 * Returns true if the instance wants a cell interaction
	 * @return (boolean true).
	 */
	@Override
	public boolean wantsCellInteraction() {
		return true;
	}
	
	/**
	 * Returns true if the instance wants a view interaction
	 * @return (boolean false).
	 */
	@Override
	public boolean wantsViewInteraction() {
		return false;
	}
	
	/**
	 * The orb handles it's own interactions.
	 * if the interactable is the player, speeds him .
	 * the orb doesn't respawn after this.
	 * @param other (interactable)
	 */
	@Override
	public void interactWith(Interactable other) {
		if(other instanceof SuperPacmanPlayer) {
			SuperPacmanPlayer player= (SuperPacmanPlayer) other; 
			player.accelerate(true);
				((SuperPacManArea) myOrb.getOwnerArea()).setOrbSignal(Logic.FALSE);
				myOrb.getOwnerArea().unregisterActor(myOrb);
				
				
				
			}
		}
		
	}