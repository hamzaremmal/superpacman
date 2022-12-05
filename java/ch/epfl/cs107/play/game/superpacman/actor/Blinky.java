package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

/**
 * This class represents an abstraction of the Blinky ghost.
 * @author H. REMMAL
 *
 */
public final class Blinky extends Ghost {
	
	/**
	 * Constructor of the Blinky.
	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
	 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	public Blinky(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		this.resetMotion();
	}

	/**
	 * This method manage the movement for this ghost.
	 */
	@Override
	protected final void getNextOrientation() {
		int randomInt = RandomGenerator.getInstance().nextInt(4);
		this.orientate(Orientation.fromInt(randomInt));
		this.move(Ghost.getSpeed());
	}

	/**
	 * Returns the title of the Ghost
	 * @return (String): the title if the ghost
	 */
	@Override
	protected String getTitle() {
		return "superpacman/ghost.blinky";
	}
}
