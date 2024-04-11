package edu.duke.yl954.battleship;

/**
 * A factory class for creating version 1 ships for a Battleship game. This
 * factory produces ships with character-based display information, including
 * submarines, battleships, carriers, and destroyers.
 */
public class V1ShipFactory implements AbstractShipFactory<Character> {

    /**
     * Creates a ship with the specified parameters. This method is a helper used to
     * create different types of ships based on their size and orientation.
     *
     * @param where  The placement of the ship, including its starting coordinate
     *               and orientation.
     * @param w      The width of the ship.
     * @param h      The height of the ship.
     * @param letter The character used to represent the ship.
     * @param name   The name of the ship (e.g., "Submarine").
     * @return A Ship<Character> instance with the specified characteristics.
     * @throws IllegalArgumentException If the provided orientation is not 'H'
     *                                  (horizontal) or 'V' (vertical).
     */
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
        if (where.getOrientation() != 'H' && where.getOrientation() != 'V') {
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }
        if (where.getOrientation() == 'H') {
            return new RectangleShip<>(name, where.getWhere(), h, w, letter, '*', null, where.getOrientation());
        }
        return new RectangleShip<>(name, where.getWhere(), w, h, letter, '*', null, where.getOrientation());
    }

    /**
     * Creates a submarine with the specified placement. Submarines are represented
     * with a length of 2.
     *
     * @param where The placement of the submarine.
     * @return A submarine ship instance.
     */
    @Override
    public Ship<Character> makeSubmarine(Placement where) {
        return createShip(where, 1, 2, 's', "Submarine");
    }

    /**
     * Creates a battleship with the specified placement. Battleships are
     * represented with a length of 4.
     *
     * @param where The placement of the battleship.
     * @return A battleship ship instance.
     */
    @Override
    public Ship<Character> makeBattleship(Placement where) {
        return createShip(where, 1, 4, 'b', "Battleship");
    }

    /**
     * Creates a carrier with the specified placement. Carriers are represented with
     * a length of 6.
     *
     * @param where The placement of the carrier.
     * @return A carrier ship instance.
     */
    @Override
    public Ship<Character> makeCarrier(Placement where) {
        return createShip(where, 1, 6, 'c', "Carrier");
    }

    /**
     * Creates a destroyer with the specified placement. Destroyers are represented
     * with a length of 3.
     *
     * @param where The placement of the destroyer.
     * @return A destroyer ship instance.
     */
    @Override
    public Ship<Character> makeDestroyer(Placement where) {
        return createShip(where, 1, 3, 'd', "Destroyer");
    }

    /**
     * Copy a submarine to the specified placement.
     *
     * @param where The placement of the new submarine.
     * @return The new submarine instance.
     */
    @Override
    public Ship<Character> copySubmarine(Placement where, Ship<Character> oldShip) {
        return null;
    }

    /**
     * Copy a battleship to the specified placement.
     *
     * @param where The placement of the new battleship.
     * @return The new battleship instance.
     */
    @Override
    public Ship<Character> copyBattleship(Placement where, Ship<Character> oldShip) {
        return null;
    }

    /**
     * Copy a carrier to the specified placement.
     *
     * @param where The placement of the new carrier.
     * @return The new carrier instance.
     */
    @Override
    public Ship<Character> copyCarrier(Placement where, Ship<Character> oldShip) {
        return null;
    }

    /**
     * Copy a destroyer to the specified placement.
     *
     * @param where The placement of the new destroyer.
     * @return The new destroyer instance.
     */
    @Override
    public Ship<Character> copyDestroyer(Placement where, Ship<Character> oldShip) {
        return null;
    }

}
