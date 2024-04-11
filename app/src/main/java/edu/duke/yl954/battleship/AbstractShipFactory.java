package edu.duke.yl954.battleship;

/**
 * This interface represents an Abstract Factory pattern for Ship creation.
 */
public interface AbstractShipFactory<T> {
    /**
     * Make a submarine.
     * 
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the submarine.
     */
    public Ship<T> makeSubmarine(Placement where);

    /**
     * Make a battleship.
     * 
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the battleship.
     */
    public Ship<T> makeBattleship(Placement where);

    /**
     * Make a carrier.
     * 
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the carrier.
     */
    public Ship<T> makeCarrier(Placement where);

    /**
     * Make a destroyer.
     * 
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the destroyer.
     */
    public Ship<T> makeDestroyer(Placement where);

    /**
     * Copy a battleship to the specified placement.
     *
     * @param where The placement of the new battleship.
     * @return The new battleship instance.
     */
    public Ship<Character> copyBattleship(Placement where, Ship<Character> oldShip);

    /**
     * Copy a carrier to the specified placement.
     *
     * @param where The placement of the new carrier.
     * @return The new carrier instance.
     */
    public Ship<Character> copyCarrier(Placement where, Ship<Character> oldShip);

    /**
     * Copy a destroyer to the specified placement.
     *
     * @param where The placement of the new destroyer.
     * @return The new destroyer instance.
     */
    public Ship<Character> copyDestroyer(Placement where, Ship<Character> oldShip);

    /**
     * Copy a submarine to the specified placement.
     *
     * @param where The placement of the new submarine.
     * @return The new submarine instance.
     */
    public Ship<Character> copySubmarine(Placement where, Ship<Character> oldShip);
}
