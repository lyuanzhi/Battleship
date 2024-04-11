package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V1ShipFactoryTest {
    /**
     * Helper function for test_make.
     */
    private void checkShip(Ship<Character> testShip, String expectedName,
            char expectedLetter, Coordinate... expectedLocs) {
        assertEquals(expectedName, testShip.getName());
        for (Coordinate c : expectedLocs) {
            assertEquals(true, testShip.occupiesCoordinates(c));
            assertEquals(expectedLetter, testShip.getDisplayInfoAt(c, true));
        }
    }

    /**
     * Test the makeship functions.
     */
    @Test
    public void test_make() {
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        Placement h1_2 = new Placement(new Coordinate(1, 2), 'H');
        Ship<Character> dst1 = f.makeDestroyer(h1_2);
        checkShip(dst1, "Destroyer", 'd', new Coordinate(1, 2),
                new Coordinate(1, 3), new Coordinate(1, 4));
        Ship<Character> dst2 = f.makeSubmarine(h1_2);
        checkShip(dst2, "Submarine", 's', new Coordinate(1, 2),
                new Coordinate(1, 3));
        Ship<Character> dst3 = f.makeBattleship(v1_2);
        checkShip(dst3, "Battleship", 'b', new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(4, 2));
        Ship<Character> dst4 = f.makeCarrier(v1_2);
        checkShip(dst4, "Carrier", 'c', new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(4, 2),
                new Coordinate(5, 2), new Coordinate(6, 2));
    }

    /**
     * Test the invalid cases of makeship functions.
     */
    @Test
    public void test_make_invalid() {
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Placement v1_2 = new Placement(new Coordinate(1, 2), 'g');
        assertThrows(IllegalArgumentException.class, () -> f.makeDestroyer(v1_2));
        assertThrows(IllegalArgumentException.class, () -> f.makeSubmarine(v1_2));
        assertThrows(IllegalArgumentException.class, () -> f.makeBattleship(v1_2));
        assertThrows(IllegalArgumentException.class, () -> f.makeCarrier(v1_2));
    }

    /**
     * Test the copyship functions.
     */
    @Test
    public void test_copy() {
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Placement p = new Placement(new Coordinate(1, 2), 'V');
        Placement p2 = new Placement(new Coordinate(5, 6), 'V');
        assertEquals(null, f.copyDestroyer(p, f.makeDestroyer(p2)));
        assertEquals(null, f.copySubmarine(p, f.makeSubmarine(p2)));
        assertEquals(null, f.copyBattleship(p, f.makeBattleship(p2)));
        assertEquals(null, f.copyCarrier(p, f.makeCarrier(p2)));
    }
}
