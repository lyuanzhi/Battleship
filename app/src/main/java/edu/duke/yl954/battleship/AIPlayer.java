package edu.duke.yl954.battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Function;

/**
 * This class represents a player in a text-based Battleship game.
 */
public class AIPlayer extends BasicPlayer {
    /**
     * Autoplay readCoordinate.
     */
    int coordIndex;
    /**
     * Autoplay readPlacement.
     */
    int placeIndex;

    /**
     * Constructs a AIPlayer with specified attributes.
     *
     * @param name        The name of the player.
     * @param theBoard    The game board associated with the player.
     * @param inputReader A BufferedReader for reading user input.
     * @param out         A PrintStream for outputting text to the user.
     * @param shipFactory A factory for creating ships.
     */
    public AIPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out,
            AbstractShipFactory<Character> shipFactory) {
        super(name, theBoard, inputReader, out, shipFactory, 2, 3, 3, 2);
        this.coordIndex = 0;
        this.placeIndex = 0;
    }

    /**
     * Prompts the user to choose a coordinate to fire at in the enemy's board,
     * ensuring the coordinate is valid within the game board dimensions.
     *
     * @param prompt the prompt to display to the user.
     * @param enemy  the opposing player, used to validate coordinates.
     * @return a Coordinate object representing the user's choice.
     * @throws IOException              if an I/O error occurs while reading input.
     * @throws EOFException             if an EOF error occurs while reading input.
     * @throws IllegalArgumentException if the coordinate goes off the bounds of the
     *                                  enemy's board.
     */
    public Coordinate readCoordinate(String prompt, BasicPlayer enemy) throws IOException {
        Coordinate ans = new Coordinate(coordIndex / enemy.theBoard.getWidth(), coordIndex % enemy.theBoard.getWidth());
        coordIndex += 1;
        return ans;
    }

    /**
     * Prompts the user for a placement on the board, reading their input and
     * creating a Placement object.
     *
     * @param prompt The message to display to the user prompting for input.
     * @return A Placement object representing the user's desired placement.
     * @throws IOException If an I/O error occurs while reading input.
     */
    public Placement readPlacement(String prompt, String shipName) throws IOException {
        Coordinate ans = new Coordinate(placeIndex / theBoard.getWidth(), placeIndex % theBoard.getWidth());
        placeIndex += 2;
        if (shipName == "Submarine" || shipName == "Destroyer") {
            return new Placement(ans, 'V');
        }
        return new Placement(ans, 'R');
    }

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
    @Override
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        Placement loc = readPlacement(null, shipName);
        Ship<Character> s = createFn.apply(loc);
        String error = theBoard.tryAddShip(s);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
    }

    /**
     * Manages the entire ship placement phase for the player. This method
     * iteratively prompts the player to place each ship specified in shipsToPlace.
     * This phase continues until all ships have been placed on the board.
     *
     * @throws IOException If an error occurs while reading input from the player.
     */
    @Override
    public void doPlacementPhase() throws IOException {
        for (String s : shipsToPlace) {
            boolean success = false;
            while (!success) {
                try {
                    doOnePlacement(s, shipCreationFns.get(s));
                    success = true;
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * Executes a single turn for the player.
     *
     * @param enemy the opposing player.
     * @throws IOException if an error occurs while reading input.
     */
    @Override
    public void playOneTurn(BasicPlayer enemy) throws IOException {
        fire(enemy);
    }

    /**
     * Fire at a square.
     *
     * @param enemy the opposing player.
     * @return Whether this action is successful.
     * @throws IOException if an error occurs while reading input.
     */
    protected boolean fire(BasicPlayer enemy) throws IOException {
        boolean success = false;
        Coordinate fireCoord = null;
        while (!success) {
            try {
                fireCoord = readCoordinate(null, enemy);
                success = true;
            } catch (Exception e) {

            }
        }
        Ship<Character> ship = enemy.theBoard.fireAt(fireCoord);
        out.println(ship == null ? "Player " + name + " missed!" : "Player " + name + " hit a " + ship.getName() + "!");
        return true;
    }

    /**
     * Checks if the player has lost the game, which occurs when all their ships
     * have been sunk.
     *
     * @param msg the message to display if the player has lost.
     * @return true if the player has lost, false otherwise.
     */
    @Override
    public boolean checkLost(String msg) {
        if (theBoard.hasLost()) {
            out.println(msg);
            return true;
        }
        return false;
    }

}
