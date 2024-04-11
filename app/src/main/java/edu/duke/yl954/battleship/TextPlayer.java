package edu.duke.yl954.battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class represents a player in a text-based Battleship game.
 */
public class TextPlayer extends BasicPlayer {
    /**
     * How many uses of move action remained.
     */
    public Integer moveRemain;
    /**
     * How many uses of scan action remained.
     */
    public Integer scanRemain;
    /**
     * last fire coordinate.
     */
    private Coordinate lastFireCoord;
    /**
     * last mark.
     */
    private Character lastMark;

    /**
     * Constructs a TextPlayer with specified attributes.
     *
     * @param name        The name of the player.
     * @param theBoard    The game board associated with the player.
     * @param inputReader A BufferedReader for reading user input.
     * @param out         A PrintStream for outputting text to the user.
     * @param shipFactory A factory for creating ships.
     */
    public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out,
            AbstractShipFactory<Character> shipFactory) {
        super(name, theBoard, inputReader, out, shipFactory, 2, 3, 3, 2);
        this.moveRemain = 3;
        this.scanRemain = 3;
        this.lastFireCoord = null;
        this.lastMark = null;
    }

    /**
     * Prompts the user for a placement on the board, reading their input and
     * creating a Placement object.
     *
     * @param prompt The message to display to the user prompting for input.
     * @return A Placement object representing the user's desired placement.
     * @throws IOException If an I/O error occurs while reading input.
     */
    public Placement readPlacement(String prompt) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("That placement is invalid: it does not have the correct format.");
        }
        return new Placement(s);
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
        Placement loc = readPlacement("Player " + name + " Where do you want to place a " + shipName + "?");
        Ship<Character> s = createFn.apply(loc);
        String error = theBoard.tryAddShip(s);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        out.println(view.displayMyOwnBoard());
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
        out.println(view.displayMyOwnBoard());
        out.println("---------------------------------------------------------------------------\n" +
                "Player " + name + ": you are going to place the following ships.\n" +
                "For each ship, type the coordinate of the upper left side of the\n" +
                "ship, followed by H (for horizontal) or V (for vertical) or U (up)\n" +
                "or R (right) or D (down) or L (left). For example M4H would place a\n" +
                "ship horizontally starting at M4 and going to the right. You have\n" +
                "\n" +
                "2 \"Submarines\" that are 1x2\n" +
                "3 \"Destroyers\" that are 1x3\n" +
                "3 \"Battleships\" that are not rectangular\n" +
                "2 \"Carriers\" that are not rectangular\n" +
                "---------------------------------------------------------------------------");
        for (String s : shipsToPlace) {
            boolean success = false;
            while (!success) {
                try {
                    doOnePlacement(s, shipCreationFns.get(s));
                    success = true;
                } catch (Exception e) {
                    out.println(e.getMessage());
                }
            }
        }
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
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("That coordinate is invalid: it does not have the correct format.");
        }
        Coordinate res = new Coordinate(s);
        if (res.getColumn() < 0 || res.getColumn() >= enemy.theBoard.getWidth() || res.getRow() < 0
                || res.getRow() >= enemy.theBoard.getHeight()) {
            throw new IllegalArgumentException(
                    "That coordinate is invalid: the point goes off the bounds of the board.");
        }
        return res;
    }

    /**
     * Executes a single turn for the player.
     *
     * @param enemy the opposing player.
     * @throws IOException if an error occurs while reading input.
     */
    @Override
    public void playOneTurn(BasicPlayer enemy) throws IOException {
        String header1 = "Player " + name + "'s turn:";
        String myHeader2 = "Your ocean";
        String enemyHeader2 = "Player " + enemy.name + "'s ocean";
        out.println(view.displayMyBoardWithEnemyNextToIt(enemy.view, header1, myHeader2, enemyHeader2, lastFireCoord,
                lastMark));
        // choose actions
        boolean success2 = false;
        while (!success2) {
            char action = 'F';
            if (moveRemain != 0 || scanRemain != 0) {
                out.println("---------------------------------------------------------------------------\n" +
                        "Possible actions for Player " + name + ":\n\n" +
                        "F Fire at a square\n" +
                        (moveRemain == 0 ? "" : "M Move a ship to another square (" + moveRemain + " remaining)\n") +
                        (scanRemain == 0 ? "" : "S Sonar scan (" + scanRemain + " remaining)\n") +
                        "---------------------------------------------------------------------------");
                String actionStr = "Player " + name + ", what would you like to do?";
                boolean success = false;
                while (!success) {
                    try {
                        action = readAction(actionStr);
                        success = true;
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                }
            }
            if (action == 'F') {
                success2 = fire(enemy);
            }
            if (action == 'M') {
                success2 = move();
            }
            if (action == 'S') {
                success2 = scan(enemy);
            }
        }
    }

    /**
     * Reads a single character ('F', 'M', or 'S') from the input provided by the
     * user, representing an action to be performed.
     * 
     * @param prompt The message displayed to the user asking for input.
     * @return The action character entered by the user, converted to uppercase.
     * @throws IOException              if an I/O error occurs while reading input.
     * @throws EOFException             if an EOF error occurs while reading input.
     * @throws IllegalArgumentException If the input does not consist of exactly one
     *                                  character or if the character is not one of
     *                                  'F', 'M', or 'S', or if the action cannot be
     *                                  performed due to restrictions (e.g., no
     *                                  remaining moves or scans).
     */
    public char readAction(String prompt) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("That action is invalid: it does not have the correct format.");
        }
        if (s.length() != 1) {
            throw new IllegalArgumentException("That action is invalid: it does not have the correct format.");
        }
        // to realize character insensitivity
        s = s.toUpperCase();
        char ans = s.charAt(0);
        if (ans != 'F' && ans != 'M' && ans != 'S') {
            throw new IllegalArgumentException("That action is invalid: it does not have the correct format.");
        }
        if (ans == 'M' && moveRemain == 0) {
            throw new IllegalArgumentException("That action is invalid: the use of move action has run out.");
        }
        if (ans == 'S' && scanRemain == 0) {
            throw new IllegalArgumentException("That action is invalid: the use of scan action has run out.");
        }
        return s.charAt(0);
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
                fireCoord = readCoordinate("Player " + name + " Please choose a position to fire at:", enemy);
                success = true;
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }
        // You can fire at the same position again.
        Ship<Character> ship = enemy.theBoard.fireAt(fireCoord);
        out.println(ship == null ? "You missed!" : "You hit a " + ship.getName() + "!");
        lastFireCoord = fireCoord;
        lastMark = enemy.view.toDisplay.whatIsAtForEnemy(lastFireCoord);
        return true;
    }

    /**
     * Move a ship to another square.
     * 
     * @return Whether this action is successful.
     * @throws IOException if an error occurs while reading input.
     */
    protected boolean move() throws IOException {
        Coordinate c = null;
        try {
            c = readCoordinate(
                    "Which ship do you want to move?\n(Enter any coordinate which is a part of the ship)", this);
        } catch (Exception e) {
            out.println(e.getMessage());
            return false;
        }
        Ship<Character> ship = theBoard.whichShip(c);
        try {
            Placement loc = readPlacement("Which position do you want to move to? (Enter placement)");
            BiFunction<Placement, Ship<Character>, Ship<Character>> copyFn = shipCopyFns.get(ship.getName());
            Ship<Character> s = copyFn.apply(loc, ship);
            theBoard.rmShip(ship);
            String error = theBoard.tryAddShip(s);
            if (error != null) {
                theBoard.tryAddShip(ship);
                throw new IllegalArgumentException(error);
            }
        } catch (Exception e) {
            out.println(e.getMessage());
            return false;
        }
        moveRemain -= 1;
        return true;
    }

    /**
     * Sonar scan.
     * 
     * @param enemy the opposing player.
     * @return Whether this action is successful.
     * @throws IOException if an error occurs while reading input.
     */
    protected boolean scan(BasicPlayer enemy) throws IOException {
        int shipNum[] = { 0, 0, 0, 0 };
        boolean success = false;
        Coordinate centerCoord = null;
        while (!success) {
            try {
                centerCoord = readCoordinate("Player " + name + " Please choose a center coordinate of a sonar scan:",
                        enemy);
                success = true;
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }
        for (int i = centerCoord.getRow() - 3; i <= centerCoord.getRow() + 3; i++) {
            for (int j = centerCoord.getColumn() - 3; j <= centerCoord.getColumn() + 3; j++) {
                if (j < 0 || j >= enemy.theBoard.getWidth() || i < 0 || i >= enemy.theBoard.getHeight()) {
                    continue;
                }
                if (Math.abs(i - centerCoord.getRow()) + Math.abs(j - centerCoord.getColumn()) <= 3) {
                    Ship<Character> ship = enemy.theBoard.whichShip(new Coordinate(i, j));
                    if (ship == null) {
                        continue;
                    }
                    if (ship.getName() == "Submarine") {
                        shipNum[0] += 1;
                    }
                    if (ship.getName() == "Destroyer") {
                        shipNum[1] += 1;
                    }
                    if (ship.getName() == "Battleship") {
                        shipNum[2] += 1;
                    }
                    if (ship.getName() == "Carrier") {
                        shipNum[3] += 1;
                    }
                }
            }
        }
        out.println("---------------------------------------------------------------------------");
        out.println("Submarines occupy " + shipNum[0] + " squares");
        out.println("Destroyers occupy " + shipNum[1] + " squares");
        out.println("Battleships occupy " + shipNum[2] + " squares");
        out.println("Carriers occupy " + shipNum[3] + " squares");
        out.println("---------------------------------------------------------------------------");
        scanRemain -= 1;
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
