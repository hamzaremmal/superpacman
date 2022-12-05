package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.superpacman.mazegenerator.MazeGenerator;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

/**
 * Represents the abdtraction of a new Level that generate evrytime from scratch.
 * @author H. REMMAL
 */
public class LevelX extends SuperPacManArea{

	/**
	 * Return the name of the Level
	 * @return (String): the title of the level
	 */
	@Override
	public String getTitle() {
		return "superpacman/LevelX";
	}
	
	/**
	 *  Method that creates the area
	 *  @return (boolean): true, if the level is intialised well.
	 */
	@Override
	 public boolean begin(Window window, FileSystem fileSystem) {
		MazeGenerator.generateMaze();
		if(super.begin(window, fileSystem)) {
			return true;
		}
		return false;
	}

}