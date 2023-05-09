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
 * This class is a representation of a heart that heals the SuperPacman. 
 * This hearts appears when a at least 3 cherry are eaten in one of their positions(randomly)
 * Changes position (randomly) each 5 seconds.
 * Because this class follows the singleton pattern , only one instance is created.
 * @author M. ZIAZI
 */
public class Heart extends CollectableAreaEntity implements Interactor{

	private Animation[] animations ;
	private Animation courante;
	private Sprite[][] sprites;
	private static Heart myHeart;
	
	/**
	 * Creates an instance of Heart 
	 * @param area(Area): the area that contains the instance.
	 * @param orientation(Orientation): orientation of the instance.
	 * @param position(DiscreteCorrdinates): position of the instance within the area.
	 */
	private Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sprites=  RPGSprite.extractSprites("zelda/heart",
				4, 1, 1,
				this , 16, 16,new Orientation[] {Orientation.DOWN ,
						Orientation.LEFT , Orientation.UP, Orientation.RIGHT});
		
		animations= Animation.createAnimations(FRAMES/2, sprites);
		courante= animations[orientation.ordinal()];
	}
	
	/**
	 * A method to draw the sprite of the heart and it's animation.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		courante= animations[this.getOrientation().ordinal()];
		courante.update(FRAMES);
		courante.draw(canvas);
	}
	
	/**
	 * Handles the interactions of the heart.
	 * @param v(AreaInteractionVisitor): handles the interactions.
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanPlayerHandler)v).interactWith(this);
		
	}
	
	/**
	 * Spawns a unique Heart in an area.
	 * @param area(SuperPacManArea): owner area of the heart.
	 * @param coord(DiscreteCoordinates ): coordinates of the heart within the area.
	 */
	public static void spawn(DiscreteCoordinates coord, SuperPacManArea area) {
		if(myHeart==null)  myHeart= new Heart(area, Orientation.DOWN,coord);	
		else  myHeart.updateHeart(coord,area);
		myHeart.getOwnerArea().registerActor(myHeart);
	}
	
	/**
	 * Changes the position of the heart.
	 * @param area(SuperPacManArea): owner area of the heart.
	 * @param coord(DiscreteCoordinates ): coordinates of the heart within the area.
	 */
	private void updateHeart(DiscreteCoordinates coord,Area area) {
		if(myHeart!=null) {
			myHeart.getOwnerArea().unregisterActor(myHeart);
			myHeart.setOwnerArea(area);
			myHeart.setCurrentPosition(coord.toVector());
			
		   }
	}
	
	/**
	 * Returns null.
	 * @return null
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}
	
	/**
	 * Returns true if the instance wants a cell interaction
	 * @return (boolean): true.
	 */
	@Override
	public boolean wantsCellInteraction() {
		return true;
	}
	
	/**
	 * Returns true if the instance wants a view interaction
	 * @return (boolean): false.
	 */
	@Override
	public boolean wantsViewInteraction() {
		return false;
	}
	
	/**
	 * The heart handles it's own interactions.
	 * if the interactable is the player, heals him .
	 * the heart doesn't respawn after this.
	 * @param other (interactable)
	 */
	@Override
	public void interactWith(Interactable other) {
		if(other instanceof SuperPacmanPlayer) {
			SuperPacmanPlayer player= (SuperPacmanPlayer) other; 
			player.heals();
				((SuperPacManArea)myHeart.getOwnerArea()).setHeartSignal(Logic.FALSE);
				myHeart.getOwnerArea().unregisterActor(myHeart);
				
		}		
	}
}