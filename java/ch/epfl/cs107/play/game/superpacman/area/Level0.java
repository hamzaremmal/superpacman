package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Represents the abstraction of the first level of the game.
 * To win this level, you will have to collect the key, so the gates will open and the player can then move to the next level.
 * @author H. REMMAL - M. ZIAZI
 */
public class Level0 extends SuperPacManArea{

	private final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);
	private Key key ;
	private Signal keySignal= Logic.FALSE;
	
	/**
	 * Return the name of the Level
	 * @return (String): the title of the level
	 */
	@Override
	public String getTitle() {
		return "superpacman/Level0";
	}
	
	/**
	 * Return the position the player have to spawn in
	 * @return (DiscreteCoordinates): The position where the player have to spawn in.
	 */
	public final static DiscreteCoordinates getSpawnPositon() {
		return PLAYER_SPAWN_POSITION;
	}
	
	/**
	 * A method that initialise the area.
	 */
	@Override
	public void createArea() {
		this.key=new Key(this,Orientation.DOWN,new DiscreteCoordinates(3,4));
		super.createArea();
		this.registerActor(key);
		this.registerActor(new Door("superpacman/Level1", Level1.getSpawnPositon(), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(5,9), new DiscreteCoordinates(6,9)));
		this.registerActor( new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(5,8),this.getSignal()));
	    this.registerActor(new Gate(this,Orientation.LEFT,new DiscreteCoordinates(6,8),this.getSignal()));
	}
	
	/**
	 * Sets the key Signal of the area.
	 * @param signal(Logic): new signal of the key.
	 */
	public  void setKeySignal(Logic signal) {
		this.keySignal=signal;
	}
	
	/**
	 * returns the signal of this area.
	 * this method is  override to make the area signal it's key signal. 
	 * @return (Signal): signal returned.
	 */
	@Override
	public   Signal getSignal() {
		return this.keySignal;
	}
	
}
