package edu.duke.yl954.battleship;

/**
 * The Board interface represents a generic game board with operations to manage
 * its contents. It is parameterized by type T, allowing flexibility in what
 * type of information can be associated with each position on the board.
 */
public interface Board<T> {
    /**
     * Returns the width of the board.
     * 
     * @return the width of the board.
     */
    public int getWidth();

    /**
     * Returns the height of the board.
     * 
     * @return the height of the board.
     */
    public int getHeight();

    /**
     * Attempts to add a ship to the board.
     *
     * @param toAdd the ship to be added to the board. It is a generic type allowing
     *              for flexibility.
     * @return the error message.
     */
    public String tryAddShip(Ship<T> toAdd);

    /**
     * Determines what is present at a given coordinate on the board from the
     * player's perspective.
     *
     * @param where The coordinate to inspect.
     * @return The display information at the given coordinate, as seen by the
     *         player.
     */
    public T whatIsAtForSelf(Coordinate where);

    /**
     * Determines what is present at a given coordinate on the board from the
     * enemy's perspective.
     *
     * @param where The coordinate to inspect.
     * @return The display information at the given coordinate, as seen by the
     *         enemy.
     */
    public T whatIsAtForEnemy(Coordinate where);

    /**
     * Processes a shot fire at the given coordinate.
     *
     * @param c The coordinate at which the shot is fired.
     * @return The ship that was hit. Returns null if the shot was a miss.
     */
    public Ship<T> fireAt(Coordinate c);

    /**
     * Find which ship occupies the given coordinate.
     *
     * @param c The coordinate as a part of which ship.
     * @return The ship that was chosen.
     */
    public Ship<T> whichShip(Coordinate c);

    /**
     * Remove ship.
     *
     * @param ship The ship that will be removed.
     */
    public void rmShip(Ship<T> ship);

    /**
     * Checks if the player has lost the game by determining if all of their ships
     * have been sunk.
     * 
     * @return true if all of the player's ships have been sunk, false otherwise.
     */
    public boolean hasLost();
}
