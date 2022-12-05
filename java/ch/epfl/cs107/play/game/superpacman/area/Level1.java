package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * The abstraction of a new level, The level 2.
 * In this Level you will have to collect all the keys...
 * @author H. REMMAL - M. ZIAZI
 */
public class Level1 extends SuperPacManArea {
	
	private final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);
	
	/**
	 * Return the name of the Level
	 * @return (String): the title of the level
	 */
	@Override
	public String getTitle() {
		return "superpacman/Level1";
	}
	
	/**
	 * Return the position the player have to spawn in
	 * @return (DiscreteCoordinates): The position where the player have to spawn in.
	 */
	protected final static DiscreteCoordinates getSpawnPositon() {
		return PLAYER_SPAWN_POSITION;
	}
	
	/**
	 * A method that initialise the area.
	 */
	@Override
	public void createArea() {
		super.createArea();
		this.registerActor(new Door("superpacman/Level2", Level2.getSpawnPositon(), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(14,0), new DiscreteCoordinates(15,0)));
		this.registerActor( new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(14,3),this.getSignal()));
		this.registerActor( new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(15,3),this.getSignal()));
	}

}
