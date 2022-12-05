package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * This class represents the abstraction of a collectable object within the area.
 * @author 	M. ZIAZI
 *
 */
public abstract class CollectableAreaEntity extends AreaEntity {
	
	protected static final int  FRAMES = 8; 
     
	/**
     * Creates an instance of the entity.
     * @param area(Area): the owner area.
     * @param orientation(Orientation): it's orientation within the area.
     * @param position(DiscreteCoordinates): it's coordinates within the area.
     */
	public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
	}

	/**
	 * Returns a list of the coordinates of the entity
	 * @return (List of Discrete Coordinates): may be empty but not null.
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> liste = new ArrayList<>();
		liste.add(this.getCurrentMainCellCoordinates());
		return liste;
	}

	/**
	 * Returns whether or not the instance is traversable.
	 * @return(boolean false)
	 */
	@Override
	public boolean takeCellSpace() {
		return false;
	}

	/**
	 * Returns true if the instance is cell interactable and false otherwise.
	 * @return(boolean true)
	 */
	@Override
	public boolean isCellInteractable() {
		return true;
	}

	/**
	 * Returns true if the instance is view interactable and false otherwise.
	 * @return (boolean false)
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
}
