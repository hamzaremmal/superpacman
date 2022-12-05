package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class represents the keys within the area.
 * @author M.ZIAZI
 *
 */
public class Key extends CollectableAreaEntity {
	
	private Sprite sprite; 
	
	/**
	 * Creates an instance of Key
	 * @param area(Area): the area that contains the instance.
	 * @param orientation(Orientation): orientation of the instance.
	 * @param position(DiscreteCorrdinates): position of the instance within the area.
	 */
	public Key(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		this.sprite= new Sprite("superpacman/key",1.f,1.f,this);
	}

	/**
	 * A method to draw the sprite of the Key.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		this.sprite.draw(canvas);

	}
	
	/**
	 * Call directly the interaction on this if accepted
	 * @param v (AreaInteractionVisitor): The visitor.
	 */
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor)v).interactWith(this);
	}

}
