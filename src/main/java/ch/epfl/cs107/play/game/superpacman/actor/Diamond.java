package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class represents the diamonds within the area.
 * @author M. ZIAZI
 *
 */
public class Diamond extends CollectableAreaEntity {
	
	private final static double value = 10;
	private Sprite sprite; 

	/**
	 * creates an instance of Diamond.
	 * @param area(Area): owner area.
	 * @param orientation(Orientation).
	 * @param position(DiscreteCoordinates).
	 */
	public Diamond(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.sprite= new Sprite("superpacman/diamond",1.f,1.f,this);
		
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
	 * Returns the gotten points when we eat a diamond.
	 * @return (double): The score we get when we eat a diamond.
	 */
	public double getPoints() {
		return Diamond.value;
	}
	
	/**
	 * A method to draw the sprite of the diamond.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		this.sprite.draw(canvas);
	}
}
