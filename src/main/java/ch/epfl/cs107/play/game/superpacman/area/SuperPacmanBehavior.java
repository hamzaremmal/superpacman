package ch.epfl.cs107.play.game.superpacman.area;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.GhostArrow;
import ch.epfl.cs107.play.game.superpacman.actor.Blinky;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Inky;
import ch.epfl.cs107.play.game.superpacman.actor.Pinky;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

/**
 * This class represents the Behavior of a SuperPacmanArea
 * @author H. REMMAL - M.ZIAZI
 */
public final class SuperPacmanBehavior extends AreaBehavior{
	
	private AreaGraph graph;
	private final List<Diamond> diamonds = new ArrayList<>();	
	private final  List<Cherry> eatenCherry= new ArrayList<>();
	private final List<Bonus> eatenBonus= new ArrayList<>();

	/**
	 * Creates an instance of SuperPacmanBehavior.
	 * @param window (Window):
	 * @param name (String):
	 */
	protected SuperPacmanBehavior(Window window, String name) {
		super(window, name);
		graph = new AreaGraph();
		SuperPacmanCellType cellType;
		for(int x = 0;x<super.getWidth();x++) {
			for(int y = 0 ; y<super.getHeight() ; y++) {
				cellType = SuperPacmanCellType.toType(getRGB(getHeight() -1-y, x));
				setCell(x,y,new SuperPacmanCell( x, y, cellType));
			}
		}
	}
	
	/**
	 * Returns the AreaGraph of the behavior
	 * @return graph (AreaGraph): the AreaGraph object of this behavior.
	 */
	public AreaGraph getAreaGraph() {
		return graph;
	}
	
	/**
	 * 
	 * @param area (Area): The area to register in.
	 */
	protected void registerActors(Area area) {
		DiscreteCoordinates coordinates;
		for(int x = 0 ; x<this.getWidth();x++) {
			for(int y = 0 ;y<this.getHeight();y++) {
				coordinates = new DiscreteCoordinates(x,y);
				if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.WALL) area.registerActor(new Wall(area,coordinates, isNeighborhoodWall(coordinates)));
				else {
					if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_BLINKY) area.registerActor(new Blinky(area, Orientation.DOWN,coordinates));
					else if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_ARROW) area.registerActor(new GhostArrow(area, Orientation.DOWN,coordinates));
					else if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_INKY) area.registerActor(new Inky(area, Orientation.DOWN,coordinates));
					else if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_PINKY) area.registerActor(new Pinky(area, Orientation.DOWN,coordinates));
					else if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_BONUS) area.registerActor(new Bonus(area,Orientation.DOWN,new DiscreteCoordinates(x,y)));
					else if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_CHERRY) area.registerActor(new Cherry(area,Orientation.DOWN,new DiscreteCoordinates(x,y)));
					else if(((SuperPacmanCell) getCell(x,y)).type == SuperPacmanCellType.FREE_WITH_DIAMOND) {
						Diamond diamond =new Diamond(area,Orientation.DOWN,new DiscreteCoordinates(x,y));
						area.registerActor(diamond);
						this.diamonds.add(diamond);
					}
						graph.addNode(coordinates, hasLeftEdge(coordinates), hasUpEdge(coordinates), hasRightEdge(coordinates), hasDownEdge(coordinates));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param coordinates (DiscreteCoordinates):
	 * @return (boolean[][]):
	 */
	private final boolean[][] isNeighborhoodWall(DiscreteCoordinates coordinates) {
		boolean[][] neighborhood = new boolean[3][3];
		for(int x = 0 ; x <3 ; x++) {
			for(int y = 0 ; y<3 ; y++) {
				try {
					if(((SuperPacmanCell) getCell(coordinates.x+x-1,coordinates.y-y+1)).type == SuperPacmanCellType.WALL) neighborhood[x][y] = true;
					else neighborhood[x][y] = false;
				}catch(ArrayIndexOutOfBoundsException e) {
					neighborhood[x][y] = false;
				}
			}
		}
		return neighborhood;
	}
	
	/**
	 * Check if the given coordinates has a left edge
	 * @param coordinates (DiscreteCoordinates): the coordinates the method will deal with
	 * @return (boolean) : false, if has a left wall
	 */
	private boolean hasLeftEdge(DiscreteCoordinates coordinates) {
		return coordinates.x > 0 &&((SuperPacmanCell)getCell(coordinates.left().x,coordinates.left().y)).type != (SuperPacmanCellType.WALL);
	}
	
	/**
	 * Check if the given coordinates has a down edge
	 * @param coordinates (DiscreteCoordinates): the coordinates the method will deal with
	 * @return (boolean) : false, if has a down wall
	 */
	private boolean hasDownEdge(DiscreteCoordinates coordinates) {
		return coordinates.y > 0 && ((SuperPacmanCell)getCell(coordinates.down().x,coordinates.down().y)).type != (SuperPacmanCellType.WALL);
	}
	
	/**
	 * Check if the given coordinates has an upper edge
	 * @param coordinates (DiscreteCoordinates): the coordinates the method will deal with
	 * @return (boolean) : false, if has an upper wall
	 */
	private boolean hasUpEdge(DiscreteCoordinates coordinates) {
		return coordinates.y+1 <this.getHeight() &&((SuperPacmanCell)getCell(coordinates.up().x,coordinates.up().y)).type != (SuperPacmanCellType.WALL);
	}
	
	/**
	 * Check if the given coordinates has a right edge
	 * @param coordinates (DiscreteCoordinates): the coordinates the method will deal with
	 * @return (boolean) :false, if has a right wall
	 */
	private boolean hasRightEdge(DiscreteCoordinates coordinates) {
		return coordinates.x+1 <this.getWidth() &&((SuperPacmanCell)getCell(coordinates.right().x,coordinates.right().y)).type != (SuperPacmanCellType.WALL);
	}
	
	/**
	 * returns a random location of the eaten cherry's .
	 * @return (DiscreteCoordinates)
	 */
	protected DiscreteCoordinates nextHeart() {
		DiscreteCoordinates coord;
		Random rand = new Random();
		int index = rand.nextInt(eatenCherry.size());
		coord = eatenCherry.get(index).getCurrentCells().get(0);
		return coord;
	}
	
	/**
	 * returns a random location of the eaten bonuses .
	 * @return (DiscreteCoordinates).
	 */
	protected DiscreteCoordinates nextOrb() {
		DiscreteCoordinates coord;
		Random rand = new Random();
		int index = rand.nextInt(eatenBonus.size());
		coord = eatenBonus.get(index).getCurrentCells().get(0);
		return coord;
	}
	
	/**
	 * Returns true if at least 3 cherry's are eaten and false otherwise.
	 * @return (boolean)
	 */
	protected boolean heartPlace() {
		if(eatenCherry.size()<3) return false;
		return !eatenCherry.isEmpty();
	}
	
	/**
	 * Returns true if at least 3 bonuses are eaten and false otherwise.
	 * @return (boolean):
	 */
	protected boolean OrbPlace() {
		if(eatenBonus.size()<3) return false;
		return !eatenBonus.isEmpty();
	}
	
	/**
	 * returns true if all diamonds of the associated area are eaten
	 * @return (boolean):
	 */
	protected boolean hasDiamonds() {
		return this.diamonds.isEmpty();
	}
	
	/**
	 * removes the eaten diamonds from the behavior's diamonds list.
	 * @param diamond (Diamond).
	 */
	protected void removeCollectable(Diamond diamond) {
		this.diamonds.remove(diamond);
	}
	
	/**
	 * adds the eaten cherry to an eaten cherry list.
	 * @param cherry (Cherry):
	 */
	protected void removeCollectable(Cherry cherry) {
		this.eatenCherry.add(cherry);
	}
	
	/**
	 * adds the eaten bonus to the eaten bonus list.
	 * @param bonus (Bonus):
	 */
	protected void removeCollectable(Bonus bonus) {
		this.eatenBonus.add(bonus);
	}
	
	/**
	 * 
	 * @author H. REMMAL - M. ZIAZI
	 */
	private final class SuperPacmanCell extends Cell {
		private SuperPacmanCellType type;
		
		/**
		 * 
		 * @param x (int):
		 * @param y (int):
		 * @param type (SuperPacmanCellType):
		 */
		private SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
			super(x, y);
			this.type = type;
		}

		/**
		 * 
		 */
		@Override
		public final boolean isCellInteractable() {
			return false;
		}

		/**
		 * 
		 */
		@Override
		public final  boolean isViewInteractable() {
			return false;
		}

		
		@Override
		public final void acceptInteraction(AreaInteractionVisitor v) {}

		/**
		 * 
		 */
		@Override
		protected final boolean canLeave(Interactable entity) {
			return true;
		}

		/**
		 * 
		 */
		@Override
		protected final boolean canEnter(Interactable entity) {
			return !this.hasNonTraversableContent();
		}
			
	}
	
}

