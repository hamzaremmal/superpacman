package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.GhostArrow.Arrow;
import ch.epfl.cs107.play.game.superpacman.actor.Heart;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.game.superpacman.actor.Orbe;
import ch.epfl.cs107.play.game.superpacman.actor.PacmanHelp;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;

/**
 * Interface for all the interaction.
 * By default all the method are empty. If an interaction is needed, the specific method should be overridden.
 */
public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor{
	
	/**
	 * The method to interact with a SuperPacmanPlayer object. Should be overridden.
	 * @param pacman (SuperPacmanPlayer): The pacman we interact with.
	 */
	default void interactWith(SuperPacmanPlayer pacman){
        // by default the interaction is empty
    }
	
	/**
	 * The method to interact with a Ghost object. Should be overridden.
	 * @param ghost (Ghost): The ghost we interact with.
	 */
	default void interactWith(Ghost ghost){
        // by default the interaction is empty
    }
	
	/**
	 * The method to interact with an Arrow object. Should be overridden.
	 * @param arrow (Arrow): The arrow we interact with.
	 */
	default void interactWith(Arrow arrow) {
		 // by default the interaction is empty
	}
	
	/**
	 * The method to interact with a Diamond object. Should be overridden.
	 * @param diamond (Diamond): The diamond we interact with.
	 */
	default void interactWith(Diamond diamond){
        // by default the interaction is empty
    }
	
	/**
	 * The method to interact with a Bonus object. Should be overridden.
	 * @param bonus (Bonus): The bonus we interact with.
	 */
	default void interactWith(Bonus bonus){
        // by default the interaction is empty
    }
	
	/**
	 * The method to interact with a Cherry object. Should be overridden.
	 * @param cherry (Cherry): The cherry we interact with.
	 */
	default void interactWith(Cherry cherry){
        // by default the interaction is empty
    }
	
	/**
	 * The method to interact with a Gate object. Should be overridden.
	 * @param gate (Gate): The gate we interact with.
	 */
	default void interactWith(Gate gate){
        // by default the interaction is empty
    }
	
	/**
	 * The method to interact with a Key object. Should be overridden.
	 * @param key (Key): The key we interact with.
	 */
	default void interactWith(Key key){
        // by default the interaction is empty
    }

	/**
	 * The method to interact with an Orb object. Should be overridden.
	 * @param orb (Orb): The orb we interact with.
	 */
	default void interactWith(Orbe orb) {
		// by default the interaction is empty
	}

	/**
	 * The method to interact with a Heart object. Should be overridden.
	 * @param heart (Heart): The heart we interact with.
	 */
	default void interactWith(Heart heart) {
		// by default the interaction is empty
	}
	
	/**
	 * The method to interact with a PacmanHelp object. Should be overridden.
	 * @param pacman (PacmanHelp): The pacman we interact with.
	 */
	default void interactWith(PacmanHelp pacman) {
		// by default the interaction is empty
	}
}
