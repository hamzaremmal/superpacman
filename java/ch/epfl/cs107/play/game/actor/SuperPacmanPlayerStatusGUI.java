package ch.epfl.cs107.play.game.actor;

import java.awt.Color;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents the status of the SuperPacmanPlayer .
 * Allows us to represent the player's information on the window.
 * @author M. ZIAZI
 *
 */
public class SuperPacmanPlayerStatusGUI implements Graphics {
	
	private ImageGraphics[] life= new ImageGraphics[5];
	private TextGraphics score;
	private SuperPacmanPlayer player;
	private Area area;
	
	/**
	 * Creates an instance of SuperPacmanPlayerStatusGUI .
	 * @param player(SuperPacmanPlayer): player which information are represented.
	 */
	public SuperPacmanPlayerStatusGUI(SuperPacmanPlayer player ,Area area) {
		this.player=player;
		this.area= area;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void draw(Canvas canvas) {
		 float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));
		for(int i=0;i<5;i++) {
			if(this.player.getPV()>=i+1) {
				this.life[i]= new
						ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
								area.getCameraScaleFactor()/15, area.getCameraScaleFactor()/15, new RegionOfInterest(0, 0, 64, 64),
								anchor.add(new Vector(i+1, height - 1.375f)), 1, 1);
											//i+1
			}
			else {
				this.life[i]= new
						ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
								area.getCameraScaleFactor()/15, area.getCameraScaleFactor()/15, new RegionOfInterest(64, 0, 64, 64),
								anchor.add(new Vector(i+1, height - 1.375f)), 1, 1);
			}
		}
		for(int i=0;i<life.length;i++) {
			this.life[i].draw(canvas);
		}
		this.score= new TextGraphics("SCORE "+player.getScore(), 0.5f, Color.YELLOW,Color.BLACK,0.1f,true,true,anchor.add(7, height - 1.375f)) ;
		this.score.draw(canvas);
	}
}