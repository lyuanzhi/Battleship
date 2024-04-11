package edu.duke.yl954.battleship;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class represents a board for the BattleShip game, which is a rectangular
 * grid that can contain ships.
 */
public class BattleShipBoard<T> implements Board<T> {
    /**
     * The width of the board
     */
    private final int width;
    /**
     * The height of the board
     */
    private final int height;
    /**
     * A list of ships placed on the board
     */
    private final ArrayList<Ship<T>> myShips;
    /**
     * Placement rule check handler
     */
    private final PlacementRuleChecker<T> placementChecker;
    /**
     * Coordinates where enemy shots have missed
     */
    private HashSet<Coordinate> enemyMisses;
    /**
     * Information to display for a miss on the board
     */
    private final T missInfo;

    /**
     * Constructs a BattleShipBoard with the specified width, height,
     * placementChecker, and missInfo.
     * 
     * @param w                is the width of the newly constructed board.
     * @param h                is the height of the newly constructed board.
     * @param placementChecker is the placement rule check handler.
     * @param missInfo         is the information to display for a miss on the
     *                         board.
     * @throws IllegalArgumentException if the width or height are less than or
     *                                  equal to zero.
     */
    public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker, T missInfo) {
        if (w <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
        }
        if (h <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
        }
        this.width = w;
        this.height = h;
        this.myShips = new ArrayList<>();
        this.placementChecker = placementChecker;
        this.enemyMisses = new HashSet<>();
        this.missInfo = missInfo;
    }

    /**
     * Constructs a BattleShipBoard with the specified width, height and missInfo
     * 
     * @param w        is the width of the newly constructed board.
     * @param h        is the height of the newly constructed board.
     * @param missInfo is the information to display for a miss on the board.
     */
    public BattleShipBoard(int w, int h, T missInfo) {
        this(w, h, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), missInfo);
    }

    /**
     * Returns the width of the board.
     *
     * @return the width of the board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the board.
     *
     * @return the height of the board.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Attempts to add a ship to the board.
     *
     * @param toAdd the ship to be added to the board.
     * @return the error message.
     */
    public String tryAddShip(Ship<T> toAdd) {
        if (placementChecker.checkPlacement(toAdd, this) == null) {
            myShips.add(toAdd);
            return null;
        }
        return placementChecker.checkPlacement(toAdd, this);
    }

    /**
     * Determines what is present at a given coordinate on the board from the
     * player's perspective.
     *
     * @param where The coordinate to inspect.
     * @return The display information at the given coordinate, as seen by the
     *         player.
     */
    public T whatIsAtForSelf(Coordinate where) {
        return whatIsAt(where, true);
    }

    /**
     * Determines what is present at a given coordinate on the board from the
     * enemy's perspective.
     *
     * @param where The coordinate to inspect.
     * @return The display information at the given coordinate, as seen by the
     *         enemy.
     */
    public T whatIsAtForEnemy(Coordinate where) {
        return whatIsAt(where, false);
    }

    /**
     * Provides display information for a given coordinate on the board, with the
     * option to differentiate between the player's and the enemy's perspectives.
     *
     * @param where  The coordinate to inspect.
     * @param isSelf Indicates whether the information is for the player's own view
     *               (true) or the enemy's view (false).
     * @return The display information for the specified coordinate.
     */
    protected T whatIsAt(Coordinate where, boolean isSelf) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(where)) {
                return s.getDisplayInfoAt(where, isSelf);
            }
        }
        if (isSelf == false && enemyMisses.contains(where)) {
            return missInfo;
        }
        return null;
    }

    /**
     * Find which ship occupies the given coordinate.
     *
     * @param c The coordinate as a part of which ship.
     * @return The ship that was chosen.
     */
    public Ship<T> whichShip(Coordinate c) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(c)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Remove ship.
     *
     * @param ship The ship that will be removed.
     */
    public void rmShip(Ship<T> ship) {
        myShips.remove(ship);
    }

    /**
     * Processes a shot fire at the given coordinate. If a ship occupies the
     * coordinate, the shot is recorded as a hit; otherwise, it's recorded as a
     * miss.
     *
     * @param c The coordinate at which the shot is fired.
     * @return The ship that was hit. Returns null if the shot was a miss.
     */
    public Ship<T> fireAt(Coordinate c) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(c)) {
                s.recordHitAt(c);
                return s;
            }
        }
        enemyMisses.add(c);
        return null;
    }

    /**
     * Checks if the player has lost the game by determining if all of their ships
     * have been sunk.
     * 
     * @return true if all of the player's ships have been sunk, false otherwise.
     */
    public boolean hasLost() {
        for (Ship<T> s : myShips) {
            if (!s.isSunk()) {
                return false;
            }
        }
        return true;
    }

}
