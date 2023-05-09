package ch.epfl.cs107.play.game.superpacman.area;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Heart;
import ch.epfl.cs107.play.game.superpacman.actor.Orbe;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

/**
 * Represnets the abstraction of an area in the SuperPacman game.
 * Every area of the game should be inherited from this class.
 * @author H. REMMAL - M.ZIAZI
 */
public abstract class SuperPacManArea extends Area implements Logic{
	
	private  float CAMERA_SCALE_FACTOR = 15f;
	private SuperPacmanBehavior behavior;
	private Logic[] instablesSignal= new Logic[] {Logic.TRUE,Logic.TRUE};
	private Timer timStabilite;
	
	
	/**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected void createArea() {
    	SuperPacManArea area = this;
   	 	ActionListener listener = arg0 -> {
			if(instablesSignal[0].isOff()&&instablesSignal[1].isOff()) ((Timer)arg0.getSource()).stop();
			if(behavior.heartPlace()) {
				if(instablesSignal[0].isOn()) {
					DiscreteCoordinates coord=	behavior.nextHeart();
					Heart.spawn(coord, area);
				}
			}
			if(behavior.OrbPlace()) {
				if(instablesSignal[1].isOn()) {
					DiscreteCoordinates coord=	behavior.nextOrb();
					Orbe.spawn(coord, area);
				}
			}
		};
   	timStabilite= new Timer(5000,listener) ;
   	this.behavior.registerActors(this);
    }
    
    /**
     * Returns the scale factor of the camera.
     * @return (float): Returns the scale factor of the camera.
     */
	@Override
	public final float getCameraScaleFactor() {
		return CAMERA_SCALE_FACTOR;
	}

	/**
	 *  Method that creates the area
	 */
	 @Override
	 public boolean begin(Window window, FileSystem fileSystem) {
		 if (super.begin(window, fileSystem)) {
			behavior = new SuperPacmanBehavior(window, this.getTitle());
	         setBehavior(behavior);
	         createArea();
	         return true;
	     }
		 return false;
	 }
	 
	 /**
	  * Return the behavior of the area
	  * @return (SuperPacmanBehavior): The beahvior of the area
	  */
	 public SuperPacmanBehavior getBehavior() {
		 return behavior;
	 }
	 
	 /**
		 * Simulates a single time step.
		 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
		 */
	 @Override
	 public void update(float deltaTime) {
		 Keyboard keyboard= this.getKeyboard();
		 if(keyboard.get(Keyboard.S).isDown() && CAMERA_SCALE_FACTOR<80)CAMERA_SCALE_FACTOR++;
		 else if(keyboard.get(Keyboard.D).isDown() && CAMERA_SCALE_FACTOR>5)CAMERA_SCALE_FACTOR--;
		 if(!SuperPacman.isPaused()) super.update(deltaTime);
	 }
	 
	 /**
	  * returns true if all the diamonds of the area are collected.
	  * @return (boolean):
	  */
	 @Override
	public boolean isOn() {
		 return this.behavior.hasDiamonds();
	}

	 /**
	  * returns true if they are still diamonds in area.
	  * @return (boolean):
	  */
	@Override
	public boolean isOff() {
		return !this.isOn();
	}

	/**
	 * returns 1 if is on and 0 otherwise
	 * @return (float)
	 */
	@Override
	public float getIntensity() {
		if(this.isOn()) return 1f;
		else return 0;
	}
		
	/**
	 * returns the signal of the gate.
	 * @return (Signal):
	 */
	public Signal getSignal() {
		if (this.isOn()) return Logic.TRUE;
		else return Logic.FALSE ;	
	} 

	/**
	 * Merges two distinct signals.
	 * @param s1 (Signal):
	 * @param s2 (Signal):
	 * @return (Signal):
	 */
	public Signal mergeSignals(Signal s1,Signal s2) {
		if(s1.getIntensity(1)!=0.0f && s2.getIntensity(1)!=0.0f) return Logic.TRUE;
		else return Logic.FALSE;	
	}
	
	/**
	 * Specifies to the behavior that a cherry is eaten.
	 * if at least 3 cherry's are eaten starts a timer to spawn a heart.
	 * @param cherry (Cherry): eaten cherry
	 */
	public void removeCollectable(Cherry cherry) {
		 this.behavior.removeCollectable(cherry);
		 if(behavior.heartPlace()) timStabilite.start();
		 }
	/**
	 * Specifies to the behavior that a diamond is eaten.
	 * @param diamond (Diamond): eaten diamond.
	 */
	public void removeCollectable(Diamond diamond) {
		 this.behavior.removeCollectable(diamond);
	}
	
	/**
	 * Specifies to the behavior that a bonus is eaten.
	 * if at least 3 bonuses are eaten starts a timer to spawn an orb .
	 * @param bonus (Bonus): eaten bonus.
	 */
	public void removeCollectable(Bonus bonus) {
		 this.behavior.removeCollectable(bonus);
		 if(behavior.OrbPlace())timStabilite.start();
	}	
	
	/**
	 * Sets the heart signal of the area.
	 * if this signal is off, can stop the timer.
	 * @param heartSignal (Logic)
	 */
	public void setHeartSignal(Logic heartSignal) {
		this.instablesSignal[0] = heartSignal;
	}
	/**
	 *  Sets the orb signal of the area.
	 * if this signal is off, can stop the timer.
	 * @param orbSignal (Logic)
	 */
	public void setOrbSignal(Logic orbSignal) {
		this.instablesSignal[1] = orbSignal;
	}

	public void setSignal(DiscreteCoordinates position, Logic signal) {
		this.behavior.getAreaGraph().setSignal(position, signal);
	}
		
}
