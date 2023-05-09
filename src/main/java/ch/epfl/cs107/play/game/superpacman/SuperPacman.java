package ch.epfl.cs107.play.game.superpacman;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.SuperPacmanPlayerStatusGUI;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.LevelX;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

/**
 * Represents the game that takes place in the window.
 * @author H. REMMAL - M.ZIAZI
 *
 */
public class SuperPacman extends RPG {
	
	private SuperPacmanPlayer player;
	private static boolean isPaused;
	private static boolean[] canBegin = new boolean[] {false,true};
	private Window canvas;
	
	/**
	 * returns the title of an instance.
	 * @return (String): the title of the game
	 */
	@Override
	public String getTitle() {
		return "Super Pac-Man";
	    }
	
	/**
	 * Returns true if the game is paused and false otherwise.
	 * @return isPaused(boolean) : true, if the game is paused
	 */
	public static boolean isPaused() {
		return  isPaused;
	    }
	
	/**
	 * Updates the game each deltaTime.
	 * stays on the welcome screen if the player hasn't moved yet
	 * pauses the game if the player presses Space
	 * stops when the player dies
	 * @param deltaTime(float): time between two updates.
	 */
	@Override
	public void update(float deltaTime) {
		Keyboard keyboard =this.getCurrentArea().getKeyboard();
		if(keyboard.get(Keyboard.LEFT).isDown()
		   ||keyboard.get(Keyboard.RIGHT).isDown()
		   ||keyboard.get(Keyboard.UP).isDown() 
		   ||keyboard.get(Keyboard.DOWN).isDown()) { 
			isPaused=false;
			canBegin[0]= true;
		}
	    else if(keyboard.get(Keyboard.SPACE).isDown()) {isPaused=true; }
		
		if(canBegin[1])	{super.update(deltaTime);
		                 canBegin[1]=false;}
	
		if(this.player.isDead()) this.end();
			           
		else  if(!canBegin[0]) this.welcome();
	  
	    else if(!isPaused) super.update(deltaTime);
		
		else this.pause();}
			
	
	/**
	 * Generates the areas of the game.
	 */
	private void createAreas(){
		addArea(new Level0());
		addArea(new Level1());
		addArea(new Level2());
		addArea(new LevelX());
	}
	
	/**
	 * Launches the game.
	 * @return (boolean): true if the game started well.
	 */
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (super.begin(window, fileSystem)) {
			this.canvas=window;
			createAreas();
			Area area = setCurrentArea("superpacman/Level0", true);
			player = new SuperPacmanPlayer(area, Level0.getSpawnPositon());
			initPlayer(player);
			return true;
		    }
	   return false;
	   }
	
	/**
	 * Ends the game.
	 */
	@Override
	public void end() {	
		
		ImageGraphics gameOver =new ImageGraphics(ResourcePath.getSprite("gameOver"), this.getCurrentArea().getCameraScaleFactor()-2,
				this.getCurrentArea().getCameraScaleFactor()-2,new RegionOfInterest(0, 0, 1400, 815),
				canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth()/2, canvas.getScaledHeight()/2)));

	   TextGraphics score= new TextGraphics("SCORE "+player.getScore(),
			  			  1f, Color.YELLOW,Color.BLACK,0.01f,true,true, 
			  			  canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth()/2,canvas.getScaledHeight()/2))
			  			  .add(5, canvas.getScaledHeight( )- 1f)) ;
		
		gameOver.draw(canvas);            
		score.draw(canvas);
		}

	/**
	 * Starts the game.
	 */
	private void welcome() {
		
		ImageGraphics	ecranStart =new ImageGraphics(ResourcePath.getSprite("Welcome"), this.getCurrentArea().getCameraScaleFactor(),
		this.getCurrentArea().getCameraScaleFactor(),new RegionOfInterest(0, 0, 224, 288),	
		canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth()/2, canvas.getScaledHeight()/2)));
		ecranStart.draw(canvas);
		}

	/**
	 * Pauses the game.
	 */
	private void pause() {
		ImageGraphics	ecranPause =new ImageGraphics(ResourcePath.getSprite("isPaused"), this.getCurrentArea().getCameraScaleFactor(), 
				this.getCurrentArea().getCameraScaleFactor()-2,
				new RegionOfInterest(0, 0, 880, 440),
		canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth()/2, canvas.getScaledHeight()/2))		
		);
	
 
		ecranPause.draw(canvas);
		
		SuperPacmanPlayerStatusGUI status =new SuperPacmanPlayerStatusGUI(player,this.getCurrentArea());
		status.draw(canvas);}
	
		}

