package edu.duke.yl954.battleship;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This abstract class represents the basic palyer in our Battleship game.
 */
public abstract class BasicPlayer implements Player {
    /**
     * The name of the player.
     */
    public final String name;
    /**
     * The game board where ships will be placed.
     */
    public final Board<Character> theBoard;
    /**
     * Textual view for displaying the game board.
     */
    public final BoardTextView view;
    /**
     * Reader for handling user input from the console.
     */
    protected final BufferedReader inputReader;
    /**
     * Output stream for displaying text to the user.
     */
    protected final PrintStream out;
    /**
     * Ship factory for creating ships.
     */
    protected final AbstractShipFactory<Character> shipFactory;
    /**
     * List of ships the player needs to place.
     */
    protected final ArrayList<String> shipsToPlace;
    /**
     * Map associating ship types with creation functions.
     */
    protected final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
    /**
     * Map associating ship types with copy functions.
     */
    protected final HashMap<String, BiFunction<Placement, Ship<Character>, Ship<Character>>> shipCopyFns;

    /**
     * Constructs a BasicPlayer with specified attributes.
     *
     * @param name        The name of the player.
     * @param theBoard    The game board associated with the player.
     * @param inputReader A BufferedReader for reading user input.
     * @param out         A PrintStream for outputting text to the user.
     * @param shipFactory A factory for creating ships.
     */
    public BasicPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out,
            AbstractShipFactory<Character> shipFactory, int s, int d, int b, int c) {
        this.name = name;
        this.theBoard = theBoard;
        this.view = new BoardTextView(theBoard);
        this.inputReader = inputReader;
        this.out = out;
        this.shipFactory = shipFactory;
        this.shipsToPlace = new ArrayList<>();
        this.shipCreationFns = new HashMap<>();
        this.shipCopyFns = new HashMap<>();
        this.setupShipCreationMap();
        this.setupShipCopyMap();
        this.setupShipCreationList(s, d, b, c);
    }

    /**
     * Initializes the ship creation function map. Each ship type is associated with
     * its creation function.
     */
    protected void setupShipCreationMap() {
        shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
        shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
        shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
        shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    }

    /**
     * Initializes the ship copy function map. Each ship type is associated with
     * its copy function.
     */
    protected void setupShipCopyMap() {
        shipCopyFns.put("Submarine", (p, s) -> shipFactory.copySubmarine(p, s));
        shipCopyFns.put("Battleship", (p, s) -> shipFactory.copyBattleship(p, s));
        shipCopyFns.put("Carrier", (p, s) -> shipFactory.copyCarrier(p, s));
        shipCopyFns.put("Destroyer", (p, s) -> shipFactory.copyDestroyer(p, s));
    }

    /**
     * Initializes the list of ships that the player needs to place on the board.
     */
    protected void setupShipCreationList(int s, int d, int b, int c) {
        shipsToPlace.addAll(Collections.nCopies(s, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(d, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(b, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(c, "Carrier"));
    }
}
