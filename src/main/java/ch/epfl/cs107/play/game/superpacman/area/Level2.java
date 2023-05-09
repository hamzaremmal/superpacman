package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.game.superpacman.actor.Zelda;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * The abstraction of a new level, The level 2.
 * In this Level you will have to collect all the keys...
 * @author H. REMMAL - M. ZIAZI
 */
public class Level2 extends SuperPacManArea {
	
	private final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);
	private final  Key[] key = new Key[4] ;
	private final Signal[] keySignal = new Signal[] {Logic.FALSE,Logic.FALSE,Logic.FALSE,Logic.FALSE};
	private final Gate[] gates = new Gate[14];
	
	/**
	 * Return the name of the Level
	 * @return (String): The title of the level
	 */
	@Override
	public String getTitle() {
		return "superpacman/Level2";
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
		key[0]= new Key(this,Orientation.DOWN,new DiscreteCoordinates(3,16));
		key[1]= new Key(this,Orientation.DOWN,new DiscreteCoordinates(26,16));
		key[2]= new Key(this,Orientation.DOWN,new DiscreteCoordinates(2,8));
		key[3]= new Key(this,Orientation.DOWN,new DiscreteCoordinates(27,8));
		for(int i=0;i<4;i++) {
			this.registerActor(key[i]);
		}
		//**************************************************************************************
		gates[0] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(8,14),keySignal[0]);
		gates[1] = new Gate(this,Orientation.DOWN,new DiscreteCoordinates(5,12),keySignal[0]);
		gates[2] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(8,10),keySignal[0]);
		gates[3] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(8,8),keySignal[0]);
		//***************************************************************************************
		gates[4] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(21,14),keySignal[1]);
		gates[5] = new Gate(this,Orientation.DOWN,new DiscreteCoordinates(24,12),keySignal[1]);
		gates[6] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(21,10),keySignal[1]);
		gates[7] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(21,8),keySignal[1]);
		//***************************************************************************************
		Signal signal = this.mergeSignals(keySignal[2], keySignal[3]);
		gates[8] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(10,2),signal);
		gates[9] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(19,2),signal);
		gates[10] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(12,8),signal);
		gates[11] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(17,8),signal);
		//***************************************************************************************
		gates[12] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(14,3),this.getSignal());
		gates[13] = new Gate(this,Orientation.RIGHT,new DiscreteCoordinates(15,3),this.getSignal());
		//***************************************************************************************
		for(int i=0;i<gates.length;i++) {
			this.registerActor(gates[i]);
		}
		this.registerActor(new Door("superpacman/LevelX", new DiscreteCoordinates(1, 1), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(14,0), new DiscreteCoordinates(15,0)));
		this.registerActor(new Zelda(Level2.this, Orientation.DOWN, new DiscreteCoordinates(14,1)));
	}
	
	/**
	 * returns a chosen gate from the gate's area.
	 * @param i (int): index of the chosen gate.
	 * @return (Gate)
	 */
	public Gate getGate(int i) {
		return gates[i];
	}
	
	/**
	 * returns a chosen key from the gate's area.
	 * @param i (int): index of the chosen gate.
	 * @return (Key)
	 */
	public Key getKey(int i) {
		return this.key[i];
	}
	
	/**
	 * Sets a key signal of the area.
	 * @param i (int):index of the changed signal.
	 * @param signal (Signal): new signal.
	 */
	public void setKeySignal(int i,Signal signal) {
		this.keySignal[i]= signal;
	}
	
	/**
	 * returns a chosen key signal of the area.
	 * @param i(int): index of the chosen signal.
	 * @return (Signal): signal returned.
	 */
	public Signal getKeySignal(int i ) {
		return this.keySignal[i];
	}
	

}
