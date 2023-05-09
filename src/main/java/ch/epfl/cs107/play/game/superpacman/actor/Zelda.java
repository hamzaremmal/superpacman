package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanBehavior;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Player that comes to pacman when he wins the level 2 and welcomes him to the LevelX
 * @author H. REMMAL
 */
public final class Zelda extends Player{
	
	private final Sprite [][] sprites;
	private final Animation[] animations;
	private boolean cc = true;
	private final Level2 level = (Level2)this.getOwnerArea();
	private SuperPacmanPlayer player;
	private final AreaGraph graph = ((SuperPacmanBehavior)level.getBehavior()).getAreaGraph();
	private Queue<Orientation> shortestPath;
	private boolean vv = true;
	
	/**
	 * This is the constructor of the Zelda object.
	 * @param area (Area): The designate area to spawn in.
	 * @param orientation (Orientation): The orientation it starts with.
     * @param coordinates (DiscreteCoordinates): The coordinates of the spawn.
	 */
	public Zelda(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		sprites 	= RPGSprite.extractSprites("zelda/darkLord.spell",3, 1, 1, this , 32, 32, new Orientation[] {Orientation.UP ,Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		animations 	= Animation.createAnimations(SuperPacmanPlayer.getSpeed()/2, sprites);
		this.resetMotion();
	}

	/**
	 * Simulates a single time step.
	 * @param deltaTime (float): Elapsed time since last update, in seconds, non-negative.
	 */
	@Override
	public void update(float deltaTime){
		if(this.isDisplacementOccurs())animations[this.getOrientation().ordinal()].update(deltaTime);
		else {
			if(level.getSignal().equals(Logic.TRUE) && vv && player!= null) {
			shortestPath = graph.shortestPath(getCurrentMainCellCoordinates(), new DiscreteCoordinates((int)player.getPosition().x,(int) player.getPosition().y));
			}
			if(shortestPath != null && !shortestPath.isEmpty()) {
				this.orientate(shortestPath.poll());
				this.move(SuperPacmanPlayer.getSpeed());
				if(shortestPath.size()==1) vv = false;
			}
		}
		super.update(deltaTime);
	}

	/**
	 * Get  Zelda's current occupying cells coordinates.
	 * @return (List of DiscreteCoordinates). May be empty but not null.
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
		list.add(this.getCurrentMainCellCoordinates());
		return list;
	}

	/**
	 * Get this Ghost's current field of view cells coordinates
	 * @return (List of DiscreteCoordinates). May be empty but not null.
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
			for(int x = -50 ; x <= 50 ;x++) {
				for(int y = -50 ; y <= 50 ; y++){
					list.add(this.getCurrentMainCellCoordinates().jump(x, y));
				}
			}
		return list;
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

	
	@Override
	public void interactWith(Interactable other) {
		if(other instanceof SuperPacmanPlayer) {
			player = (SuperPacmanPlayer)other;
			if(!vv && cc) {
			this.orientate(((SuperPacmanPlayer)other).getOrientation());
			System.out.println("Welcome to LevelX");
			cc = false;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
			shortestPath = graph.shortestPath(getCurrentMainCellCoordinates(), new DiscreteCoordinates(14, 1));
		}
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public final void acceptInteraction(AreaInteractionVisitor v) {}

	/**
	 * Draw the sprites of the player in the canavas.
	 * @param canvas (Canvas): Canvas where to draw the sprites.
	 */
	@Override
	public void draw(Canvas canvas) {
		animations[this.getOrientation().ordinal()].draw(canvas);
	}

}
