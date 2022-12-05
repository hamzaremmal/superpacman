package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

/**
 * This class represents the bonus that frightens the ghosts when collected.
 * @author M. ZIAZI
 *
 */
public class Bonus extends CollectableAreaEntity {

	private Animation[] animations ;
	private Animation courante;
	private Sprite[][] sprites;
	private SoundAcoustics music = new SoundAcoustics(ResourcePath.getSounds("transactionOk"), 1f, false, false, false, false);
	protected boolean bip = false;
	
	/**
	 * creates an instance of Bonus.
	 * @param area(Area): owner area.
	 * @param orientation(Orientation).
	 * @param position(DiscreteCoordinates).
	 */
	public Bonus(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		music.shouldBeStarted();
		sprites=  RPGSprite.extractSprites("superpacman/coin",4, 1, 1,this , 16, 16,new Orientation[] {Orientation.DOWN ,Orientation.LEFT , Orientation.UP, Orientation.RIGHT});
		animations= Animation.createAnimations(FRAMES/2, sprites);
		courante= animations[orientation.ordinal()];
		
	}
	
	/**
	 * A method to draw the sprite of the bonus
	 * @param canvas (Canvas): The canvas to draw on.
	 */
	@Override
	public void draw(Canvas canvas) {
		courante= animations[this.getOrientation().ordinal()];
		courante.update(FRAMES);
		courante.draw(canvas);
	}
	
	/**
	 * Call directly the interaction on this if accepted
	 * @param v (AreaInteractionVisitor): The visitor.
	 */
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor)v).interactWith(this);
	}
	
	/**
	 * plays a music when the coin is collected
	 * @param audio(Audio).: music played
	 */
	@Override
	public void bip(Audio audio) {
		if(bip) music.bip(audio);
		bip = false;
	}

}
