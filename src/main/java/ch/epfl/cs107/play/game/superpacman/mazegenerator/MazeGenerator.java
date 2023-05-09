package ch.epfl.cs107.play.game.superpacman.mazegenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Generate a random maze of (31x31)pixels.
 * @author Hamza REMMAL
 */
public class MazeGenerator {
	
	private static Stack<MazeCoordinates> stack;
	private static Set<MazeCoordinates> visitedCells;
	private static Set<DiscreteCoordinates> walls;
	private static Set<DiscreteCoordinates> freeCells;
	private static Set<MazeCoordinates> cellsNeighbourInvisited;
	private static int width = 30,height = 30;
	private static BufferedImage buffImg;
	private static Graphics2D g2d;
	private static File file;
	private static final String path = MazeGenerator.class.getResource("/images/behaviors/superpacman/LevelX.png").getPath();
	private static final Random rand = new Random();
	
	private MazeGenerator() {}
	
	/**
	 * Create a random maze of (31x31) pixels
	 * @return (BufferedImage): The image of the maze.
	 */
	public static BufferedImage generateMaze() {
		stack = new Stack<>();
		visitedCells = new HashSet<>();
		walls = new HashSet<DiscreteCoordinates>();
		freeCells = new HashSet<DiscreteCoordinates>();
		buffImg = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_RGB);
		g2d = buffImg.createGraphics();
		Thread.yield();
			update();
			paint();
		file = new File(path);
		FileImageOutputStream fileImgOutStrm;
		try {
			fileImgOutStrm = new FileImageOutputStream(file);
			ImageIO.write(buffImg, "png", fileImgOutStrm);
			fileImgOutStrm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffImg; 
	}
	
	/**
	 * Filling the pixels with the good color for the maze.
	 */
	private static final void paint() {
		for(MazeCoordinates coord : visitedCells){
				// DRAW PASSAGE
			g2d.setColor(new Color(-1));
			
			g2d.fillRect(coord.x, coord.y, 1,1);
			if(!(coord.neighboursWall[0])) {
				g2d.fillRect(coord.x, coord.y-1, 1,1);
				freeCells.add(new DiscreteCoordinates(coord.x, coord.y-1));
			}
			if(!(coord.neighboursWall[1])) {
				g2d.fillRect(coord.x, coord.y+1, 1,1);
				freeCells.add(new DiscreteCoordinates(coord.x, coord.y+1));
			}
			if(!(coord.neighboursWall[2])) {
				g2d.fillRect(coord.x+1, coord.y, 1,1);
				freeCells.add(new DiscreteCoordinates(coord.x+1, coord.y));
			}
			if(!(coord.neighboursWall[3])) {
				g2d.fillRect(coord.x-1, coord.y, 1,1);
				freeCells.add(new DiscreteCoordinates(coord.x-1, coord.y));
			}
				// DRAW WALLS
				g2d.setColor(new Color(-16777216));
			if((coord.neighboursWall[0])) {
				g2d.fillRect(coord.x, coord.y-1, 1,1);
				walls.add(new DiscreteCoordinates(coord.x, coord.y-1));
			}
			if((coord.neighboursWall[1])) {
				g2d.fillRect(coord.x, coord.y+1, 1,1);
				walls.add(new DiscreteCoordinates(coord.x, coord.y+1));
			}
			if((coord.neighboursWall[2])) {
				g2d.fillRect(coord.x+1, coord.y, 1,1);
				walls.add(new DiscreteCoordinates(coord.x+1, coord.y));
			}
			if((coord.neighboursWall[3])) {
				g2d.fillRect(coord.x-1, coord.y, 1,1);
				walls.add(new DiscreteCoordinates(coord.x-1, coord.y));
			}
		}
			// REMOVING SOME WALLS SO WE HAVE A GOOD MAZE
		g2d.setColor(new Color(0));
		for(DiscreteCoordinates coord : walls) {
			if(coord.x == 0 || coord.y == 0 || coord.x == width || coord.y == height) {
				continue;
			}
				// If it's not a border wall the we have 1/5 chance to remove the wall
			else if(rand.nextInt(5) == 3) {
				freeCells.add(coord);
				g2d.fillRect(coord.x, coord.y, 1,1);
			}
		}
		
			// FILL THE MAZE WITH GHOSTS AND COLLECTABLE ENTITIES
		for(DiscreteCoordinates coord : freeCells) {
			int randomInt = rand.nextInt(100); // PROPABILITE D'APPARTITION 1/100
				// FREE_WITH_BLINKY (p=1/200)
			if(randomInt == 2 && rand.nextBoolean()) g2d.setColor(new Color(-65536));
				// FREE_WITH_ARROW (p=1/200)
			else if(randomInt == 3  && rand.nextBoolean()) g2d.setColor(new Color(-12829690));
				// FREE_WITH_PINKY (p=1/200)
			else if(randomInt == 4  && rand.nextBoolean()) g2d.setColor(new Color(-157237));
				// FREE_WITH_INKY (p=1/200)
			else if(randomInt == 5  && rand.nextBoolean()) g2d.setColor(new Color( -16724737));
				// FREE_WITH_CHERRY (p=1/10)
			else if(randomInt % 11 == 0) g2d.setColor(new Color(-36752));
				// FREE_WITH_BONUS (p=1/100)
			else if(randomInt == 7) g2d.setColor(new Color( -16478723));
				// FREE_WITH_DIAMOND
			else g2d.setColor(new Color(-1));
				
			g2d.fillRect(coord.x, coord.y, 1,1);
		}
	}
	
	/**
	 * Creating the abstract structure of the maze.
	 */
	private static void update() {
		boolean cc = true;
		stack.push(new MazeCoordinates(1, 1));
		visitedCells.add(new MazeCoordinates(1, 1));
		do{
		if(!stack.isEmpty()) {
			cellsNeighbourInvisited = getNeighboursInvisited();
			if(!cellsNeighbourInvisited.isEmpty()) {
				MazeCoordinates coord = (MazeCoordinates) cellsNeighbourInvisited.toArray()[new Random().nextInt(10) % cellsNeighbourInvisited.size()];
				if(coord.equals(stack.peek().up())) {
					stack.peek().neighboursWall[0] = false ;
					coord.neighboursWall[1] = false ;
				}
				if(coord.equals(stack.peek().down())) {
					stack.peek().neighboursWall[1] = false ;
					coord.neighboursWall[0] = false ;
					if(coord.y == height) {
						stack.peek().neighboursWall[1] = true ;
					}
				}
				if(coord.equals(stack.peek().right())) {
					stack.peek().neighboursWall[2] = false ;
					coord.neighboursWall[3] = false ;
					if(coord.x == width) {
						stack.peek().neighboursWall[2] = true ;
					}
				}
				if(coord.equals(stack.peek().left())) {
					stack.peek().neighboursWall[3] = false ;
					coord.neighboursWall[2] = false ;
				}
				stack.push(coord);
				visitedCells.add(stack.peek());			
			}else {
				stack.pop();
			}
		}
		else cc = false;
		}while(cc);
	}
	
	/**
	 * Gets the top of the stack and return its neighbours that the algorithm didn't visit yet.
	 * @return (Set of MazeCoodinates): The invisited neighbours so we can have choose which path to follow.
	 */
	private static Set<MazeCoordinates> getNeighboursInvisited() {
		Set<MazeCoordinates> set = new HashSet<MazeCoordinates>();
		if(stack.peek().left().x > 0 && !visitedCells.contains(stack.peek().left()))set.add(stack.peek().left());
		if(stack.peek().down().y < height && !visitedCells.contains(stack.peek().down()))set.add(stack.peek().down());
		if(stack.peek().up().y > 0 && !visitedCells.contains(stack.peek().up()))set.add(stack.peek().up());
		if(stack.peek().right().x < width && !visitedCells.contains(stack.peek().right()))set.add(stack.peek().right());
		return set;
	}
	
	/**
	 * This class represents the abstraction of the coordinates we use to generate the maze
	 * @author H. REMMAL
	 */
	private final static class MazeCoordinates implements Serializable {

		private static final long serialVersionUID = 1;
	    private final int x, y;
	    
	    /*
	     * Every Cell has it own MazeCoordinates and this array so we know if there is a wall near it.
	     * 0: UP, 1: DOWN, 2:RIGHT, 3:LEFT;
	     */
	   private final boolean[] neighboursWall = new boolean[] {true,true,true,true}; //true, there is a wall

	    /**
	     * Default coordinate constructor
	     * @param y (int): The row index
	     * @param x (int): The column index
	     */
	    private MazeCoordinates(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }

	    /** 
	     * @return (MazeCoordinates): two coordinate left 
	     */
	    private MazeCoordinates left(){
	        return new MazeCoordinates(x-2, y);
	    }

	    /** 
	     * @return (MazeCoordinates): two coordinate right 
	     */
	    private MazeCoordinates right(){
	        return new MazeCoordinates(x+2, y);
	    }

	    /** 
	     * @return (MazeCoordinates): two coordinate below
	     */
	    private MazeCoordinates up(){
	        return new MazeCoordinates(x, y-2);
	    }

	    /** 
	     * @return (MazeCoordinates): two coordinate above 
	     */
	    private MazeCoordinates down(){
	        return new MazeCoordinates(x, y+2);
	    }

	    /// Implements Serializable

	    @Override
	    public int hashCode() {
	        return Integer.hashCode(y) ^ Integer.hashCode(x);
	    }

	    @Override
	    public boolean equals(Object object) {
	        if (object == null || !(object instanceof MazeCoordinates))
	            return false;
	        else {
	        	MazeCoordinates other = (MazeCoordinates)object;
	            return x == other.x && y == other.y;
	        }
	    }

	}
	
}
