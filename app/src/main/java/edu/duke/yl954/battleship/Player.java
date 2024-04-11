package edu.duke.yl954.battleship;

import java.io.IOException;
import java.util.function.Function;

/**
 * The Player interface represents a generic player.
 */
public interface Player {
    /**
     * Performs the actions for one placement of a ship on the board, including
     * reading user input, placing the ship, and updating the display of the board.
     *
     * @param shipName The name of the ship to be placed.
     * @param createFn A function that creates a ship of the specified type.
     * @throws IOException              If an error occurs while reading input from
     *                                  the player.
     * @throws IllegalArgumentException If an error occurs while adding a ship.
     */
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException;

    /**
     * Manages the entire ship placement phase for the player. This method
     * iteratively prompts the player to place each ship specified in shipsToPlace.
     * This phase continues until all ships have been placed on the board.
     *
     * @throws IOException If an error occurs while reading input from the player.
     */
    public void doPlacementPhase() throws IOException;

    /**
     * Executes a single turn for the player.
     *
     * @param enemy the opposing player.
     * @throws IOException if an error occurs while reading input.
     */
    public void playOneTurn(BasicPlayer enemy) throws IOException;

    /**
     * Checks if the player has lost the game, which occurs when all their ships
     * have been sunk.
     *
     * @param msg the message to display if the player has lost.
     * @return true if the player has lost, false otherwise.
     */
    public boolean checkLost(String msg);
}
