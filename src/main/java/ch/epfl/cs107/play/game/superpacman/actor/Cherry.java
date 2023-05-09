package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class represents the  cherry's within the area. 
 * @author M. ZIAZI
 *
 */
public class Cherry extends CollectableAreaEntity {
	
	private static final  double value =200;
	private Sprite sprite;
	
	/**
	 * Creates an instance of Cherry 
	 * @param area(Area): the area that contains the instance.
	 * @param orientation(Orientation): orientation of the instance.
	 * @param position(DiscreteCorrdinates): position of the instance within the area.
	 */
	public Cherry(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.sprite= new Sprite("superpacman/cherry",1.f, 1.f,this);
	}
	
	/**
	 * returns the points scored when eating a cherry
	 * @return Cherry.points(double);
	 */
	public static double getPoints() {
		return Cherry.value;
	}

	/**
	 * A method to draw the sprite of the cherry.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	
	/**
	 * Handles the interactions of the cherry.
	 * @param v(AreaInteractionVisitor): handles the interactions.
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor)v).interactWith(this);
	}

}
