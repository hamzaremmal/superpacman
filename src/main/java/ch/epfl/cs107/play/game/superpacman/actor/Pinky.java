package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Queue;
import java.util.Random;

import ch.epfl.cs107.play.game.superpacman.area.SuperPacManArea;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * This represents an abstraction of the Pinky ghost.
 * @author H. REMMAL
 *
 */
public class Pinky extends Ghost {
	
	private DiscreteCoordinates cible ;
	private static final int MIN_AFRAID_DISTANCE = 5;
	private static final int MAX_RANDOM_ATTEMPT = 200;
	private Queue<Orientation> orientationPath;
	
	/**
	 * Constructor of Pinky.
	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
	 * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	public Pinky(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		this.resetMotion();
	}

	/**
	 * This method manage the movement for this ghost.
	 */
	@Override
	protected void getNextOrientation() {
		if(!((super.getPacman() == null || GHOST_AFRAID) && orientationPath != null && !orientationPath.isEmpty()))specialMovement(GHOST_AFRAID);
		if(orientationPath!= null) {
			this.orientate(orientationPath.poll());
			this.move(getSpeed());
		}	
	}
	
	/**
	 * A intern method that helps with managing the movement.It makes us have a random position and a path to follow.
	 * @param isAfraid (boolean): the status of the ghost.
	 */
	private void specialMovement(boolean isAfraid) {
		if(!isAfraid) {
			// NOT AFRAID and DON'T KNOW where is pacMan
			if(super.getPacman() == null) {
				do {
					randomPosition();
				}while(orientationPath == null || orientationPath.isEmpty());
			}
			// NOT AFRAID and KNOW where is pacMan
			else cible = super.getPacman().getCurrentCells().get(0);
		}
		else {
			// DON'T KNOW where is pacMan
			if(super.getPacman() == null) 	cible = farAsPossiblePoint(this.getCurrentMainCellCoordinates());
			// KNOW where is PacMan
			else 				cible = farAsPossiblePoint(super.getPacman().getCurrentCells().get(0));	
		}
		orientationPath = ((SuperPacManArea) this.getOwnerArea()).getBehavior(). getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), cible);
	}
	
	/**
	 * Gets a random achievable position.
	 */
	private void randomPosition() {
		int randX = new Random().nextInt(this.getOwnerArea().getWidth());
		int randY = new Random().nextInt(this.getOwnerArea().getHeight());
		cible = new DiscreteCoordinates(randX, randY);
		orientationPath = ((SuperPacManArea) this.getOwnerArea()).getBehavior(). getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), cible);
	}
	
	/**
	 * Gives us a far point from the given location.
	 * @param coordinates (DiscreteCoordinates): The point to be far from.
	 * @return (DiscreteCoordinates): The location of the point.
	 */
	private DiscreteCoordinates farAsPossiblePoint(DiscreteCoordinates coordinates) {
		float distanceMax = 0;
		DiscreteCoordinates maxCoordinates = null;
		for(int i = 0 ;i < MAX_RANDOM_ATTEMPT; i++) {
			do {
				randomPosition();
			}while( this.getCurrentMainCellCoordinates() == cible || orientationPath == null|| DiscreteCoordinates.distanceBetween(cible,coordinates) < MIN_AFRAID_DISTANCE);
			if(DiscreteCoordinates.distanceBetween(cible,coordinates) > distanceMax) {
				distanceMax = DiscreteCoordinates.distanceBetween(cible, coordinates);
				maxCoordinates = cible;
			}	
		}
		return maxCoordinates;
	}

	/**
	 * Return the name of the player
	 */
	@Override
	protected String getTitle() {
		return "superpacman/ghost.pinky";
	}
	
}
