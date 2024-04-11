package edu.duke.yl954.battleship;

import java.util.HashMap;

/**
 * This abstract class represents the basic ship in our Battleship game.
 */
public abstract class BasicShip<T> implements Ship<T> {

    /**
     * if myPieces.get(c) is null, c is not part of this Ship
     * if myPieces.get(c) is false, c is part of this ship and has not been hit
     * if myPieces.get(c) is true, c is part of this ship and has been hit
     */
    protected HashMap<Coordinate, Boolean> myPieces;
    /**
     * Holds display information for my ship
     */
    protected ShipDisplayInfo<T> myDisplayInfo;
    /**
     * Holds display information for enemy's ship
     */
    protected ShipDisplayInfo<T> enemyDisplayInfo;

    /**
     * Constructs a BasicShip with specified coordinates and display information.
     *
     * @param where            Iterable of Coordinates where the ship is located.
     * @param myDisplayInfo    Display information handler for my ship.
     * @param enemyDisplayInfo Display information handler for enemy's ship.
     */
    public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo,
            ShipDisplayInfo<T> enemyDisplayInfo) {
        this.myPieces = new HashMap<Coordinate, Boolean>();
        for (Coordinate c : where) {
            this.myPieces.put(c, false);
        }
        this.myDisplayInfo = myDisplayInfo;
        this.enemyDisplayInfo = enemyDisplayInfo;
    }

    /**
     * Ensures a given coordinate is part of this ship.
     *
     * @param c The Coordinate to check.
     * @throws IllegalArgumentException If the coordinate is not part of the ship.
     */
    protected void checkCoordinateInThisShip(Coordinate c) {
        if (!occupiesCoordinates(c)) {
            throw new IllegalArgumentException("The coordinate is not in this ship!");
        }
    }

    /**
     * Check if this ship occupies the given coordinate.
     * 
     * @param where is the Coordinate to check if this Ship occupies
     * @return true if where is inside this ship, false if not.
     */
    @Override
    public boolean occupiesCoordinates(Coordinate where) {
        return myPieces.containsKey(where);
    }

    /**
     * Checks if the ship has been sunk, which occurs when all parts of the ship
     * have been hit.
     *
     * @return true if all parts of the ship have been hit, false otherwise.
     */
    @Override
    public boolean isSunk() {
        for (Boolean b : myPieces.values()) {
            if (b == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Records a hit at a given coordinate.
     *
     * @param where The coordinate where the ship has been hit.
     */
    @Override
    public void recordHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        myPieces.put(where, true);
    }

    /**
     * Checks if a part of the ship at a given coordinate was hit.
     *
     * @param where The coordinate to check.
     * @return true if the part at the coordinate was hit, false otherwise.
     */
    @Override
    public boolean wasHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        return myPieces.get(where);
    }

    /**
     * Return indicator at the given coordinate. This coordinate
     * must be part of the ship.
     * 
     * @param where  is the coordinate to return information for
     * @param myShip determine to display my ship or enemy's ship
     * @return indicator
     */
    @Override
    public T getDisplayInfoAt(Coordinate where, boolean myShip) {
        checkCoordinateInThisShip(where);
        if (myShip) {
            return myDisplayInfo.getInfo(where, wasHitAt(where));
        }
        return enemyDisplayInfo.getInfo(where, wasHitAt(where));
    }

    /**
     * Get all of the Coordinates that this Ship occupies.
     * 
     * @return An Iterable with the coordinates that this Ship occupies
     */
    @Override
    public Iterable<Coordinate> getCoordinates() {
        return myPieces.keySet();
    }

    @Override
    public Iterable<Boolean> getHits() {
        return myPieces.values();
    }

}
