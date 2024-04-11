package edu.duke.yl954.battleship;

import java.util.function.Function;

/**
 * This class handles textual display of a Board (i.e., converting it to a
 * string to show to the user). It supports two ways to display the Board: one
 * for the player's own board, and one for the enemy's board.
 */
public class BoardTextView {

    /**
     * The Board to display
     */
    final Board<Character> toDisplay;
    /**
     * last enemy board string.
     */
    String lastEnemyBoard;

    /**
     * Constructs a BoardView, given the board it will display.
     * 
     * @param toDisplay is the Board to display
     * @throws IllegalArgumentException if the board is larger than 10x26.
     */
    public BoardTextView(Board<Character> toDisplay) {
        this.toDisplay = toDisplay;
        this.lastEnemyBoard = displayAnyBoard((c) -> toDisplay.whatIsAtForEnemy(c));
        if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
            throw new IllegalArgumentException(
                    "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
        }
    }

    /**
     * This makes the header line, e.g. 0|1|2|3|4\n
     * 
     * @return the String that is the header line for the given board
     */
    String makeHeader() {
        StringBuilder ans = new StringBuilder("  "); // README shows two spaces at beginning
        String sep = ""; // start with nothing to separate, then switch to | to separate
        for (int i = 0; i < toDisplay.getWidth(); i++) {
            ans.append(sep);
            ans.append(i);
            sep = "|";
        }
        ans.append("\n");
        return ans.toString();
    }

    /**
     * Generates a string representation of the game board using a function to
     * determine whether to display the player's own board or the enemy's board.
     *
     * @param getSquareFn A function to display own or enemy's square.
     * @return A string representation of the board, with rows labeled by letters
     *         and columns by numbers.
     */
    protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
        StringBuilder body = new StringBuilder("");
        for (int row = 0; row < toDisplay.getHeight(); row++) {
            body.append((char) (65 + row));
            body.append(" ");
            for (int col = 0; col < toDisplay.getWidth() - 1; col++) {
                Character mark = getSquareFn.apply(new Coordinate(row, col));
                body.append(mark == null ? " " : mark); // If null, append a space; otherwise, append the character.
                body.append("|");
            }
            // Process the last column of the row separately to avoid appending "|" at the
            // end of the row.
            Character mark = getSquareFn.apply(new Coordinate(row, toDisplay.getWidth() - 1));
            body.append(mark == null ? " " : mark);
            body.append(" ");
            body.append((char) (65 + row));
            body.append("\n");
        }
        // Combines the header, body, and footer (same as header) into the complete
        // board representation.
        return makeHeader() + body.toString() + makeHeader();
    }

    /**
     * Displays the player's own board.
     *
     * @return A string representation of the player's own board.
     */
    public String displayMyOwnBoard() {
        return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
    }

    /**
     * Displays the board from the enemy's perspective. This board only changes when
     * a fire action is taken.
     *
     * @param c    Last fire coordinate.
     * @param mark Last mark.
     * @return A string representation of the board from the enemy's perspective.
     */
    public String displayEnemyBoard(Coordinate c, Character mark) {
        if (c == null) {
            return lastEnemyBoard;
        }
        int index = makeHeader().length();
        boolean flag = false;
        for (int row = 0; row < toDisplay.getHeight(); row++) {
            index += 2;
            for (int col = 0; col < toDisplay.getWidth() - 1; col++) {
                if (row == c.getRow() && col == c.getColumn()) {
                    flag = true;
                    break;
                }
                index += 2;
            }
            if (flag == true) {
                break;
            }
            if (row == c.getRow() && toDisplay.getWidth() - 1 == c.getColumn()) {
                flag = true;
                break;
            }
            index += 4;
        }
        if (flag == false) {
            return lastEnemyBoard;
        }
        return lastEnemyBoard.substring(0, index) + mark + lastEnemyBoard.substring(index + 1);
    }

    /**
     * Combines the player's board and the enemy's board into a single string
     * representation. It includes headers for each board to differentiate between
     * the player's view and the enemy's view.
     *
     * @param enemyView     The view of the enemy's board.
     * @param header1       The main header for the combined boards display.
     * @param myHeader2     The sub-header for the player's board.
     * @param enemyHeader2  The sub-header for the enemy's board.
     * @param lastFireCoord Last fire coordinate.
     * @param lastMark      Last mark.
     * @return A string representation that combines both the player's and the
     *         enemy's boards.
     */
    public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String header1, String myHeader2,
            String enemyHeader2, Coordinate lastFireCoord, Character lastMark) {
        String[] myBoard = this.displayMyOwnBoard().split("\n");
        enemyView.lastEnemyBoard = enemyView.displayEnemyBoard(lastFireCoord, lastMark);
        String[] enemyBoard = enemyView.lastEnemyBoard.split("\n");
        StringBuilder sb = new StringBuilder(header1 + "\n");
        sb.append(new String(" ").repeat(5) + myHeader2);
        sb.append(new String(" ").repeat((2 * toDisplay.getWidth() + 22 - 5 - myHeader2.length())));
        sb.append(enemyHeader2 + "\n");
        // header
        sb.append(myBoard[0] + new String(" ").repeat(16 + 2) + enemyBoard[0] + "\n");
        for (int i = 1; i < myBoard.length - 1; i++) {
            sb.append(myBoard[i] + new String(" ").repeat(16) + enemyBoard[i] + "\n");
        }
        // header
        sb.append(myBoard[myBoard.length - 1] + new String(" ").repeat(16 + 2) + enemyBoard[myBoard.length - 1] + "\n");

        return sb.toString();
    }

}
