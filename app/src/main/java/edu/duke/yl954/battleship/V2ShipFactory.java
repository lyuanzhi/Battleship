package edu.duke.yl954.battleship;

import java.util.HashMap;

/**
 * A factory class for creating version 2 ships for a Battleship game. This
 * factory produces ships with character-based display information, including
 * submarines, battleships, carriers, and destroyers. (different shape)
 */
public class V2ShipFactory implements AbstractShipFactory<Character> {

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
     * @param mask   The mask used to determine the shape of the ship
     * @return A Ship<Character> instance with the specified characteristics.
     * @throws IllegalArgumentException If the provided orientation is not 'H'
     *                                  (horizontal) or 'V' (vertical).
     */
    protected Ship<Character> createShipVH(Placement where, int w, int h, char letter, String name, boolean[][] mask) {
        if (where.getOrientation() != 'H' && where.getOrientation() != 'V') {
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }
        if (where.getOrientation() == 'H') {
            return new RectangleShip<>(name, where.getWhere(), h, w, letter, '*', rotate90(mask),
                    where.getOrientation());
        }
        return new RectangleShip<>(name, where.getWhere(), w, h, letter, '*', mask, where.getOrientation());
    }

    /**
     * Creates a ship with the specified parameters. This method is a helper used to
     * create different types of ships based on their size and orientation.
     *
     * @param where  The placement of the ship, including its starting coordinate
     *               and orientation.
     * @param w      The width of the ship.
     * @param h      The height of the ship.
     * @param letter The character used to represent the ship.
     * @param name   The name of the ship
     * @param mask   The mask used to determine the shape of the ship
     * @return A Ship<Character> instance with the specified characteristics.
     * @throws IllegalArgumentException If the provided orientation is not 'U'
     *                                  (up) or 'R' (right) or 'D' (down) or 'L'
     *                                  (left).
     */
    protected Ship<Character> createShipURDL(Placement where, int w, int h, char letter, String name,
            boolean[][] mask) {
        if (where.getOrientation() != 'U' && where.getOrientation() != 'R' && where.getOrientation() != 'D'
                && where.getOrientation() != 'L') {
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }
        if (where.getOrientation() == 'R') {
            return new RectangleShip<>(name, where.getWhere(), h, w, letter, '*', rotate90(mask),
                    where.getOrientation());
        }
        if (where.getOrientation() == 'D') {
            return new RectangleShip<>(name, where.getWhere(), w, h, letter, '*', rotate90(rotate90(mask)),
                    where.getOrientation());
        }
        if (where.getOrientation() == 'L') {
            return new RectangleShip<>(name, where.getWhere(), h, w, letter, '*', rotate90(rotate90(rotate90(mask))),
                    where.getOrientation());
        }
        return new RectangleShip<>(name, where.getWhere(), w, h, letter, '*', mask, where.getOrientation());
    }

    /**
     * rotate the matrix by 90 degree
     *
     * @param mask The the matrix will be rotated
     * @return The rotated matrix
     */
    protected boolean[][] rotate90(boolean[][] mask) {
        boolean[][] ans = new boolean[mask[0].length][mask.length];
        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                ans[j][mask.length - 1 - i] = mask[i][j];
            }
        }
        return ans;
    }

    /**
     * Copy the exist ship to a new placement.
     *
     * @param where   The placement of the new ship, including its starting
     *                coordinate and orientation.
     * @param oldShip The old ship ship.
     * @param newShip The new ship ship which will copy the hit situation.
     * @return The copied ship.
     */
    protected Ship<Character> copyShipVH(Placement where, Ship<Character> oldShip, Ship<Character> newShip) {
        HashMap<Character, Integer> mapVH = new HashMap<>();
        mapVH.put('H', 0);
        mapVH.put('V', 1);
        boolean[][] fireMask = new boolean[oldShip.getMask().length][oldShip.getMask()[0].length];
        for (int i = 0; i < fireMask.length; i++) {
            for (int j = 0; j < fireMask[0].length; j++) {
                Coordinate c = new Coordinate(oldShip.getUpperLeft().getRow() + i,
                        oldShip.getUpperLeft().getColumn() + j);
                fireMask[i][j] = oldShip.wasHitAt(c);
            }
        }
        int rotationTimes = (mapVH.get(where.getOrientation()) - mapVH.get(oldShip.getOrientation()) + 4) % 4;
        if (rotationTimes == 0)
            return copyShipHit(where, newShip, fireMask);
        if (rotationTimes == 1)
            return copyShipHit(where, newShip, rotate90(fireMask));
        return copyShipHit(where, newShip, rotate90(rotate90(rotate90(fireMask))));
    }

    /**
     * Copy the exist ship to a new placement.
     *
     * @param where   The placement of the new ship, including its starting
     *                coordinate and orientation.
     * @param oldShip The old ship ship.
     * @param newShip The new ship ship which will copy the hit situation.
     * @return The copied ship.
     */
    protected Ship<Character> copyShipURDL(Placement where, Ship<Character> oldShip, Ship<Character> newShip) {
        HashMap<Character, Integer> mapURDL = new HashMap<>();
        mapURDL.put('U', 0);
        mapURDL.put('R', 1);
        mapURDL.put('D', 2);
        mapURDL.put('L', 3);
        boolean[][] fireMask = new boolean[oldShip.getMask().length][oldShip.getMask()[0].length];
        for (int i = 0; i < fireMask.length; i++) {
            for (int j = 0; j < fireMask[0].length; j++) {
                Coordinate c = new Coordinate(oldShip.getUpperLeft().getRow() + i,
                        oldShip.getUpperLeft().getColumn() + j);
                if (oldShip.occupiesCoordinates(c)) {
                    fireMask[i][j] = oldShip.wasHitAt(c);
                }
            }
        }
        int rotationTimes = (mapURDL.get(where.getOrientation()) - mapURDL.get(oldShip.getOrientation()) + 4) % 4;
        if (rotationTimes == 0)
            return copyShipHit(where, newShip, fireMask);
        if (rotationTimes == 1)
            return copyShipHit(where, newShip, rotate90(fireMask));
        if (rotationTimes == 2)
            return copyShipHit(where, newShip, rotate90(rotate90(fireMask)));
        return copyShipHit(where, newShip, rotate90(rotate90(rotate90(fireMask))));
    }

    /**
     * Copy the hit situation.
     *
     * @param where   The placement of the new ship, including its starting
     *                coordinate and orientation.
     * @param newShip The new ship.
     * @param mask    The mask used to copy the hit situation.
     * @return The copied ship.
     */
    protected Ship<Character> copyShipHit(Placement where, Ship<Character> newShip, boolean[][] mask) {
        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                if (mask[i][j]) {
                    newShip.recordHitAt(
                            new Coordinate(where.getWhere().getRow() + i, where.getWhere().getColumn() + j));
                }
            }
        }
        return newShip;
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
        boolean[][] mask = new boolean[][] {
                { true },
                { true }
        };
        return createShipVH(where, 1, 2, 's', "Submarine", mask);
    }

    /**
     * Creates a battleship with the specified placement.
     *
     * @param where The placement of the battleship.
     * @return A battleship ship instance.
     */
    @Override
    public Ship<Character> makeBattleship(Placement where) {
        boolean[][] mask = new boolean[][] {
                { false, true, false },
                { true, true, true }
        };
        return createShipURDL(where, 3, 2, 'b', "Battleship", mask);
    }

    /**
     * Creates a carrier with the specified placement.
     *
     * @param where The placement of the carrier.
     * @return A carrier ship instance.
     */
    @Override
    public Ship<Character> makeCarrier(Placement where) {
        boolean[][] mask = new boolean[][] {
                { true, false },
                { true, false },
                { true, true },
                { true, true },
                { false, true }
        };
        return createShipURDL(where, 2, 5, 'c', "Carrier", mask);
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
        boolean[][] mask = new boolean[][] {
                { true },
                { true },
                { true }
        };
        return createShipVH(where, 1, 3, 'd', "Destroyer", mask);
    }

    /**
     * Copy a submarine to the specified placement.
     *
     * @param where The placement of the new submarine.
     * @return The new submarine instance.
     */
    @Override
    public Ship<Character> copySubmarine(Placement where, Ship<Character> oldShip) {
        return copyShipVH(where, oldShip, makeSubmarine(where));
    }

    /**
     * Copy a battleship to the specified placement.
     *
     * @param where The placement of the new battleship.
     * @return The new battleship instance.
     */
    @Override
    public Ship<Character> copyBattleship(Placement where, Ship<Character> oldShip) {
        return copyShipURDL(where, oldShip, makeBattleship(where));
    }

    /**
     * Copy a carrier to the specified placement.
     *
     * @param where The placement of the new carrier.
     * @return The new carrier instance.
     */
    @Override
    public Ship<Character> copyCarrier(Placement where, Ship<Character> oldShip) {
        return copyShipURDL(where, oldShip, makeCarrier(where));
    }

    /**
     * Copy a destroyer to the specified placement.
     *
     * @param where The placement of the new destroyer.
     * @return The new destroyer instance.
     */
    @Override
    public Ship<Character> copyDestroyer(Placement where, Ship<Character> oldShip) {
        return copyShipVH(where, oldShip, makeDestroyer(where));
    }

}
