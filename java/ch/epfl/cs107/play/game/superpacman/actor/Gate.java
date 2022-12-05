package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacManArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class represents the gates within the area that can't be traversed unless their signal is on.
 * @author M. ZIAZI
 */
public class Gate extends AreaEntity {
	
	private Sprite sprite;
	private Signal signal;
	
	/**
	 * The constructor of the Gate.
	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
	 * @param position (DiscreteCoordinates): The coordinates of the spawn.
	 * @param signal (): the signal of the gate
	 */
	public Gate(Area area, Orientation orientation, DiscreteCoordinates position,Signal signal) {
		super(area, orientation, position);
		this.signal=signal;
		if(orientation.equals(Orientation.DOWN)||orientation.equals(Orientation.DOWN)) {
			this.sprite = new RPGSprite("superpacman/gate",1,1,this, new RegionOfInterest(0, 0, 32,32));
		}else{	
			this.sprite = new RPGSprite("superpacman/gate",1,1,this, new RegionOfInterest(0, 64, 32,32));
		}
		if(!((Logic)signal).isOn()) {
			((SuperPacManArea)(area)).setSignal(position,Logic.FALSE);
		}
	}
	
	/**
	 * Get this Gate current occupying cells coordinates.
	 * @return (List of DiscreteCoordinates). May be empty but not null.
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
		list.add(this.getCurrentMainCellCoordinates());
		return list;
	}
	
	/**
	 * Returns whether or not the instance is traversable.
	 * Returns true if the signal is on and false otherwise.
	 * @return(boolean)
	 */
	@Override
	public boolean takeCellSpace() {
		if(((Logic) this.signal).isOff()) return true;
		else return false;
	}

	/**
	 * @return(boolean false);
	 */
	@Override
	public boolean isCellInteractable() {
		return false;
	}

	/**
	 * @return(boolean false);
	 */
	@Override
	public boolean isViewInteractable() {
		return false;
	}

	/**
	 * the instance doesn't interact.
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// No interaction
	}
	
	/**
	 * Allows to set the Signal of the instance.
	 * @param logic(Signal).
	 */
	public void setSignal(Signal logic) {
		this.signal = logic;
	}
	
	/**
	 * A method to draw the sprite of the gate.
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		if(this.signal.getIntensity(0)==0.0f) {
		this.sprite.draw(canvas);}
	}
	
	/**
	 * Simulates a single time step.
	 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
	 */
	@Override
	public void update(float deltatime) {
		Area area =this.getOwnerArea();
		if(area.getTitle().equals("superpacman/Level2")) {
			Level2 level = (Level2)area;
			for(int i=0;i<=3;i++) {
				if(this.equals(level.getGate(i))) this.setSignal(level.getKeySignal(0));
			}
			for(int i=4;i<=7;i++) {
				if(this.equals(level.getGate(i))) { this.setSignal( level.getKeySignal(1));}
			}
			for(int i=8;i<=11;i++) {
				if(this.equals(level.getGate(i))) { this.setSignal( level.mergeSignals(level.getKeySignal(2),level.getKeySignal(3)));}
			}
			for(int i=12;i<=13;i++) {
				if(this.equals(level.getGate(i))) { this.setSignal( level.getSignal());}
			}
		} else{
			this.setSignal(((SuperPacManArea)area).getSignal());
		}
	}
		
}	


