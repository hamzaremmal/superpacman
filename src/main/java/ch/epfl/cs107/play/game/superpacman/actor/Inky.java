package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Queue;
import java.util.Random;

import ch.epfl.cs107.play.game.superpacman.area.SuperPacManArea;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * This class represents an abstraction of the Inky ghost.
 * @author H. REMMAL
 */
public final class Inky extends Ghost {
	
	private DiscreteCoordinates cible;
	private static final int MAX_DISTANCE_WHEN_SCARED = 5;
	private static final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
	private Queue<Orientation> orientationPath;
	
	/**
	 * Returns the title of the ghost
	 * @return (String): the title of the Ghost
	 */
	protected String getTitle() {
		return "superpacman/ghost.inky";
	}
	/**
	 * Constructor of the Inky.
 	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
	 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	public Inky(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		this.resetMotion();
	}
	
	/**
	 * This method manage the movement for this ghost.
	 */
	@Override
	protected void getNextOrientation() {
			// if the ghost doesn't know where is pacman and didn't arrive to the cible position
		if((super.getPacman() == null || GHOST_AFRAID) && orientationPath != null && !orientationPath.isEmpty()) { 
			if(this.orientationPath!= null) {
				this.orientate(orientationPath.poll());
				this.move(getSpeed());
			}
		}
			// if the ghost know where is pacman or arrived at the cible position
		else {
			if (GHOST_AFRAID)  specialMovement(MAX_DISTANCE_WHEN_SCARED);				// SCARED
			else 			   specialMovement(MAX_DISTANCE_WHEN_NOT_SCARED); 			// NOT SCARED
			if(orientationPath != null && !orientationPath.isEmpty()) {
				this.orientate(orientationPath.poll());
				this.move(getSpeed());
			}else {
				this.resetMotion();
			}
		}	
	}
	
	/**
	 * A intern method that helps with managing the movement. It makes us having a random position and a path to go to this position.
	 * @param maxDistance (int): The maximum distance from the current position.
	 */
	private void specialMovement(int maxDistance) {
		if(GHOST_AFRAID)	randomPosition(maxDistance);
		else {
			if(super.getPacman() == null) randomPosition(maxDistance);
			else {
				cible = super.getPacman().getCurrentCells().get(0);
				orientationPath = ((SuperPacManArea) this.getOwnerArea()).getBehavior(). getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), cible);
			}	
		}
	}
	
	/**
	 * Gets a random achievable position in a certain distance.
	 * @param maxDistance (int): The maximum distance from the current position.
	 */
	private void randomPosition(int maxDistance) {
		do {
			int randX = new Random().nextInt(this.getOwnerArea().getWidth());
			int randY = new Random().nextInt(this.getOwnerArea().getHeight());
			cible = new DiscreteCoordinates(randX, randY);
			orientationPath = ((SuperPacManArea) this.getOwnerArea()).getBehavior(). getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), cible);
		}while( orientationPath == null|| orientationPath.isEmpty()|| DiscreteCoordinates.distanceBetween(cible, this.getSpawnPosition()) >= maxDistance); // Check if the cell is free
	}

}
