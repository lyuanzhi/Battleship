package edu.duke.yl954.battleship;

import java.util.HashSet;

/**
 * Represents a ship with a rectangular shape on the game board. This class
 * extends BasicShip to provide functionality for creating and managing ships
 * defined by a rectangular area.
 */
public class RectangleShip<T> extends BasicShip<T> {

    /**
     * The name of the ship.
     */
    final String name;
    /**
     * The mask used to determine the shape of the ship.
     */
    final boolean[][] mask;
    /**
     * The upper left coordinate of the rectangle.
     */
    final Coordinate upperLeft;
    /**
     * The upper left coordinate of the rectangle.
     */
    final char orientation;

    /**
     * Constructs a RectangleShip with specified parameters.
     *
     * @param name             The name of the ship.
     * @param upperLeft        The upper left coordinate of the rectangle.
     * @param width            The width of the ship (number of columns).
     * @param height           The height of the ship (number of rows).
     * @param myDisplayInfo    The display information for my ship.
     * @param enemyDisplayInfo The display information for enemy's ship.
     * @param mask             The mask used to determine the shape of the ship.
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo,
            ShipDisplayInfo<T> enemyDisplayInfo, boolean[][] mask, char orientation) {
        super(makeCoords(upperLeft, width, height, mask), myDisplayInfo, enemyDisplayInfo);
        this.name = name;
        this.mask = mask;
        this.upperLeft = upperLeft;
        this.orientation = orientation;
    }

    /**
     * Helper method to generate a set of coordinates that define the rectangular
     * shape of the ship.
     *
     * @param upperLeft The upper left coordinate of the rectangle.
     * @param width     The width of the rectangle.
     * @param height    The height of the rectangle.
     * @param mask      The mask used to determine the shape of the ship.
     * @return A set of coordinates representing the area occupied by the ship.
     */
    static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height, boolean[][] mask) {
        HashSet<Coordinate> ans = new HashSet<>();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (mask == null) {
                    ans.add(new Coordinate(upperLeft.getRow() + r, upperLeft.getColumn() + c));
                    continue;
                }
                if (mask[r][c]) {
                    ans.add(new Coordinate(upperLeft.getRow() + r, upperLeft.getColumn() + c));
                }
            }
        }
        return ans;
    }

    /**
     * Constructs a RectangleShip with specified parameters and display info.
     *
     * @param name      The name of the ship.
     * @param upperLeft The upper left coordinate of the ship's rectangular area.
     * @param width     The width of the ship.
     * @param height    The height of the ship.
     * @param data      The display data for unhit parts of the ship.
     * @param onHit     The display data for hit parts of the ship.
     * @param mask      The mask used to determine the shape of the ship.
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit, boolean[][] mask,
            char orientation) {
        this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit),
                new SimpleShipDisplayInfo<T>(null, data), mask, orientation);
    }

    /**
     * Constructs a single-cell RectangleShip for testing purposes.
     *
     * @param upperLeft The coordinate of the single cell occupied by the ship.
     * @param data      The display data for unhit parts of the ship.
     * @param onHit     The display data for hit parts of the ship.
     */
    public RectangleShip(Coordinate upperLeft, T data, T onHit) {
        this("testship", upperLeft, 1, 1, data, onHit, null, 'v');
    }

    /**
     * Returns the name of the ship.
     *
     * @return The name of the ship.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the mask of the ship.
     *
     * @return The mask of the ship.
     */
    @Override
    public boolean[][] getMask() {
        return mask;
    }

    /**
     * Returns the upperLeft coordinate of the ship.
     *
     * @return The upperLeft coordinate of the ship.
     */
    @Override
    public Coordinate getUpperLeft() {
        return upperLeft;
    }

    /**
     * Returns the orientation of the ship.
     *
     * @return The orientation of the ship.
     */
    @Override
    public char getOrientation() {
        return orientation;
    }
    
}
