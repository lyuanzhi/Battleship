package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V2ShipFactoryTest {
    /**
     * Helper function for test_make.
     */
    private void checkShip(Ship<Character> testShip, String expectedName,
            char expectedLetter, Coordinate... expectedLocs) {
        assertEquals(expectedName, testShip.getName());
        for (Coordinate c : expectedLocs) {
            assertEquals(true, testShip.occupiesCoordinates(c));
            if (testShip.getDisplayInfoAt(c, true) == '*')
                continue;
            assertEquals(expectedLetter, testShip.getDisplayInfoAt(c, true));
        }
    }

    /**
     * Test the makeship functions.
     */
    @Test
    public void test_make() {
        AbstractShipFactory<Character> f = new V2ShipFactory();
        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        Placement h1_2 = new Placement(new Coordinate(1, 2), 'H');
        Ship<Character> dst1 = f.makeDestroyer(h1_2);
        checkShip(dst1, "Destroyer", 'd', new Coordinate(1, 2),
                new Coordinate(1, 3), new Coordinate(1, 4));
        Ship<Character> dst2 = f.makeSubmarine(v1_2);
        checkShip(dst2, "Submarine", 's', new Coordinate(1, 2),
                new Coordinate(2, 2));
        Placement p1 = new Placement(new Coordinate(1, 2), 'U');
        Placement p2 = new Placement(new Coordinate(1, 2), 'R');
        Placement p3 = new Placement(new Coordinate(1, 2), 'D');
        Placement p4 = new Placement(new Coordinate(1, 2), 'L');
        Ship<Character> dst3_1 = f.makeBattleship(p1);
        Ship<Character> dst3_2 = f.makeBattleship(p2);
        Ship<Character> dst3_3 = f.makeBattleship(p3);
        Ship<Character> dst3_4 = f.makeBattleship(p4);
        checkShip(dst3_1, "Battleship", 'b',
                new Coordinate(1, 3), new Coordinate(2, 2),
                new Coordinate(2, 3), new Coordinate(2, 4));
        checkShip(dst3_2, "Battleship", 'b',
                new Coordinate(1, 2), new Coordinate(2, 2),
                new Coordinate(3, 2), new Coordinate(2, 3));
        checkShip(dst3_3, "Battleship", 'b',
                new Coordinate(1, 2), new Coordinate(1, 3),
                new Coordinate(1, 4), new Coordinate(2, 3));
        checkShip(dst3_4, "Battleship", 'b',
                new Coordinate(2, 2), new Coordinate(1, 3),
                new Coordinate(2, 3), new Coordinate(3, 3));
        Ship<Character> dst4_1 = f.makeCarrier(p1);
        Ship<Character> dst4_2 = f.makeCarrier(p2);
        Ship<Character> dst4_3 = f.makeCarrier(p3);
        Ship<Character> dst4_4 = f.makeCarrier(p4);
        checkShip(dst4_1, "Carrier", 'c',
                new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2),
                new Coordinate(4, 2), new Coordinate(3, 3),
                new Coordinate(4, 3), new Coordinate(5, 3));
        checkShip(dst4_2, "Carrier", 'c',
                new Coordinate(1, 3),
                new Coordinate(1, 4), new Coordinate(1, 5),
                new Coordinate(1, 6), new Coordinate(2, 2),
                new Coordinate(2, 3), new Coordinate(2, 4));
        checkShip(dst4_3, "Carrier", 'c',
                new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2),
                new Coordinate(2, 3), new Coordinate(3, 3),
                new Coordinate(4, 3), new Coordinate(5, 3));
        checkShip(dst4_4, "Carrier", 'c',
                new Coordinate(2, 5),
                new Coordinate(1, 4), new Coordinate(1, 5),
                new Coordinate(1, 6), new Coordinate(2, 2),
                new Coordinate(2, 3), new Coordinate(2, 4));
    }

    /**
     * Test the invalid cases of makeship functions.
     */
    @Test
    public void test_make_invalid() {
        AbstractShipFactory<Character> f = new V2ShipFactory();
        Placement p1 = new Placement(new Coordinate(1, 2), 'd');
        Placement p2 = new Placement(new Coordinate(1, 2), 'h');
        assertThrows(IllegalArgumentException.class, () -> f.makeDestroyer(p1));
        assertThrows(IllegalArgumentException.class, () -> f.makeSubmarine(p1));
        assertThrows(IllegalArgumentException.class, () -> f.makeBattleship(p2));
        assertThrows(IllegalArgumentException.class, () -> f.makeCarrier(p2));
    }

    /**
     * Test the copyship functions.
     */
    @Test
    public void test_copy() {
        AbstractShipFactory<Character> f = new V2ShipFactory();
        Placement p1 = new Placement(new Coordinate(5, 6), 'V');
        Placement p21 = new Placement(new Coordinate(1, 2), 'V');
        Placement p22 = new Placement(new Coordinate(1, 2), 'H');
        Placement p3 = new Placement(new Coordinate(5, 6), 'U');
        Placement p41 = new Placement(new Coordinate(1, 2), 'U');
        Placement p42 = new Placement(new Coordinate(1, 2), 'R');
        Placement p43 = new Placement(new Coordinate(1, 2), 'D');
        Placement p44 = new Placement(new Coordinate(1, 2), 'L');
        checkShip(f.copyDestroyer(p21, f.makeDestroyer(p1)), "Destroyer", 'd', new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2));
        checkShip(f.copySubmarine(p21, f.makeSubmarine(p1)), "Submarine", 's', new Coordinate(1, 2),
                new Coordinate(2, 2));
        checkShip(f.copySubmarine(p22, f.makeSubmarine(p1)), "Submarine", 's', new Coordinate(1, 2),
                new Coordinate(1, 3));
        checkShip(f.copySubmarine(p1, f.makeSubmarine(p22)), "Submarine", 's', new Coordinate(5, 6),
                new Coordinate(6, 6));

        checkShip(f.copyBattleship(p41, f.makeBattleship(p3)), "Battleship", 'b', new Coordinate(1, 3),
                new Coordinate(2, 2), new Coordinate(2, 3), new Coordinate(2, 4));
        checkShip(f.copyBattleship(p42, f.makeBattleship(p3)), "Battleship", 'b', new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(2, 3));
        checkShip(f.copyBattleship(p43, f.makeBattleship(p3)), "Battleship", 'b', new Coordinate(1, 2),
                new Coordinate(1, 3), new Coordinate(1, 4), new Coordinate(2, 3));
        Ship<Character> ship = f.makeBattleship(p3);
        ship.recordHitAt(new Coordinate(5, 7));
        ship.recordHitAt(new Coordinate(6, 8));
        Ship<Character> newShip = f.copyBattleship(p44, ship);
        checkShip(newShip, "Battleship", 'b', new Coordinate(2, 2),
                new Coordinate(1, 3), new Coordinate(2, 3), new Coordinate(3, 3));
        assertTrue(newShip.wasHitAt(new Coordinate(1, 3)));
        assertTrue(newShip.wasHitAt(new Coordinate(2, 2)));
        assertFalse(newShip.wasHitAt(new Coordinate(2, 3)));
        assertFalse(newShip.wasHitAt(new Coordinate(3, 3)));

        checkShip(f.copyCarrier(p41, f.makeCarrier(p3)), "Carrier", 'c', new Coordinate(1, 2),
                new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(4, 2), new Coordinate(3, 3),
                new Coordinate(4, 3), new Coordinate(5, 3));

    }
}
