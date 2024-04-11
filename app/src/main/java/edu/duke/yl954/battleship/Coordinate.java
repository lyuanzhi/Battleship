package edu.duke.yl954.battleship;

/**
 * This class represents a two-dimensional coordinate on a grid. This class is
 * used to specify positions on a game board.
 */
public class Coordinate {
    /**
     * The row number of the coordinate.
     */
    private final int row;
    /**
     * The column number of the coordinate.
     */
    private final int column;

    /**
     * Constructs a Coordinate instance with specified row and column.
     *
     * @param row    The row of the coordinate.
     * @param column The column of the coordinate.
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Constructs a Coordinate instance from a string description. The description
     * must consist of two characters: a letter (A-Z) followed by a digit (0-9),
     * representing the row and column, respectively.
     *
     * @param descr A string representation of the coordinate, where the letter
     *              represents the row and the digit represents the column.
     * @throws IllegalArgumentException If the description does not conform to
     *                                  the expected format or contains invalid
     *                                  characters.
     */
    public Coordinate(String descr) {
        if (descr.length() != 2) {
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
        // to realize character insensitivity
        descr = descr.toUpperCase();
        char rowLetter = descr.charAt(0);
        char columnLetter = descr.charAt(1);
        if (rowLetter < 'A' || rowLetter > 'Z' || columnLetter < '0' || columnLetter > '9') {
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
        this.row = rowLetter - 'A';
        this.column = columnLetter - '0';
    }

    /**
     * Returns the row of this coordinate.
     *
     * @return The row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of this coordinate.
     *
     * @return The column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Checks if this Coordinate is equal to another object. Equality is defined
     * by both the row and column numbers being identical.
     *
     * @param o The object to compare with this Coordinate.
     * @return True if the given object is a Coordinate with the same
     *         row and column; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Coordinate c = (Coordinate) o;
            return row == c.getRow() && column == c.getColumn();
        }
        return false;
    }

    /**
     * Returns a string representation of this coordinate in the format "(row,
     * column)".
     *
     * @return A string representation of this coordinate.
     */
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    /**
     * Generates a hash code for this Coordinate.
     * 
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
