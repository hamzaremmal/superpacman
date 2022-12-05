package ch.epfl.cs107.play.game.superpacman.area;

/**
 * Enums of all cell types.
 * @author H. REMMAL - M.ZIAZI
 */
public enum SuperPacmanCellType {
	NONE(0), // never used as real content
	WALL (-16777216), //black
	FREE_WITH_DIAMOND(-1), //white
	FREE_WITH_BLINKY (-65536), //red
	FREE_WITH_ARROW (-12829690),
	FREE_WITH_PINKY (-157237), //pink
	FREE_WITH_INKY ( -16724737), //cyan
	FREE_WITH_CHERRY (-36752), //light red
	FREE_WITH_BONUS ( -16478723), //light blue
	FREE_EMPTY ( -6118750); // sort of gray
	
	final int type;
	
	/**
	 * Constructor of SuperPacmanCellType
	 * @param type (int): The color in int.
	 */
	private SuperPacmanCellType(int type){
		this.type = type;
	}
	
	/**
	 * Return the type of cell for the given integer.
	 * @param type (int): the integer that correspond to the color.
	 * @return (): The type of the cell.
	 */
	public static SuperPacmanCellType toType(int type) {
		switch(type) {
		case -16777216 	: return WALL;
		case -1			: return FREE_WITH_DIAMOND;
		case -65536		: return FREE_WITH_BLINKY;
		case -157237 	: return FREE_WITH_PINKY;
		case -16724737	: return FREE_WITH_INKY;
		case -36752		: return FREE_WITH_CHERRY;
		case -16478723	: return FREE_WITH_BONUS;
		case -6118750	: return FREE_EMPTY;
		case -12829690 	: return FREE_WITH_ARROW;
		default			: return NONE;
		}	
	}

}
