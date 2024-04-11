package edu.duke.yl954.battleship;

/**
 * This class rovides a simple implementation of the ShipDisplayInfo interface
 * for returning display information about a ship.
 */
public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
    /**
     * The display information for the ship when it has not been hit.
     */
    T myData;
    /**
     * The display information for the ship when it has been hit.
     */
    T onHit;

    /**
     * Constructs a SimpleShipDisplayInfo instance with specified display
     * information for both hit and not hit states.
     *
     * @param myData The display information for when the ship has not been hit.
     * @param onHit  The display information for when the ship has been hit.
     */
    SimpleShipDisplayInfo(T myData, T onHit) {
        this.myData = myData;
        this.onHit = onHit;
    }

    /**
     * Returns the appropriate display information.
     *
     * @param where The coordinate of the part of the ship for which to get display
     *              information.
     * @param hit   A boolean indicating whether the part of the ship at the given
     *              coordinate has been hit.
     * @return The display information of type T for the specified part of the ship.
     */
    @Override
    public T getInfo(Coordinate where, boolean hit) {
        if (hit) {
            return onHit;
        }
        return myData;
    }

}
