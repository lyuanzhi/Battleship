package edu.duke.yl954.battleship;

/**
 * This class represents the placement of an object on a grid, including its
 * location and orientation.
 */
public class Placement {
    /**
     * The coordinate of the placement on the grid.
     */
    private final Coordinate where;
    /**
     * The orientation of the object at the placement, stored as uppercase
     * regardless of the input case.
     */
    private final char orientation;

    /**
     * Constructs a Placement instance with the specified location and orientation.
     *
     * @param where       The Coordinate specifying the location of the
     *                    placement.
     * @param orientation The orientation character.
     */
    public Placement(Coordinate where, char orientation) {
        this.where = where;
        // to realize character insensitivity
        this.orientation = Character.toUpperCase(orientation);
    }

    /**
     * Constructs a Placement instance from a string description.
     *
     * @param descr A string representation of the placement, e.g., "A1H" or "B2V".
     */
    public Placement(String descr) {
        if (descr.length() != 3) {
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }
        // to realize character insensitivity
        descr = descr.toUpperCase();
        this.where = new Coordinate(descr.substring(0, 2));
        this.orientation = descr.charAt(2);;
    }

    /**
     * Returns the coordinate of this placement.
     *
     * @return The coordinate where this placement is located.
     */
    public Coordinate getWhere() {
        return where;
    }

    /**
     * Returns the orientation of this placement.
     *
     * @return The orientation character.
     */
    public char getOrientation() {
        return orientation;
    }

    /**
     * Checks if this Placement is equal to another object. Equality is defined
     * by both the location and orientation being identical.
     *
     * @param o The object to compare with this Placement.
     * @return True if the given object is a Placement with the same
     *         location and orientation; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Placement c = (Placement) o;
            return where.equals(c.getWhere()) && orientation == c.getOrientation();
        }
        return false;
    }

    /**
     * Returns a string representation of this placement, including its location
     * and orientation.
     *
     * @return A string representation of the placement, e.g., "(1, 2) | V"
     */
    @Override
    public String toString() {
        return where.toString() + " | " + orientation;
    }

    /**
     * Generates a hash code for this Placement.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
