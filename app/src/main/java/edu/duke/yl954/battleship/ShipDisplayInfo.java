package edu.duke.yl954.battleship;

/**
 * This interface allows for flexible implementation of how ships are
 * represented.
 */
public interface ShipDisplayInfo<T> {
    /**
     * Returns the display information for a ship at a given coordinate.
     *
     * @param where The coordinate of the part of the ship for which display
     *              information is requested.
     * @param hit   A boolean indicating whether the part of the ship at the given
     *              coordinate has been hit.
     * @return The display information of type T for the specified part of
     *         the ship.
     */
    public T getInfo(Coordinate where, boolean hit);
}
