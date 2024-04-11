package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
    /**
     * Test the BattleShipBoard constructor.
     */
    @Test
    public void test_width_and_height() {
        Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
        assertEquals(10, b1.getWidth());
        assertEquals(20, b1.getHeight());
    }

    /**
     * Test the invalid dimensions.
     */
    @Test
    public void test_invalid_dimensions() {
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20, 'X'));
    }

    /**
     * Helper function: check what is at my board.
     */
    private <T> void checkWhatIsAtMyBoard(BattleShipBoard<T> b, T[][] expected) {
        for (int i = 0; i < b.getHeight(); i++) {
            for (int j = 0; j < b.getWidth(); j++) {
                assertEquals(expected[i][j], b.whatIsAtForSelf(new Coordinate(i, j)));
            }
        }
    }

    /**
     * Helper function: check what is at enemy board.
     */
    private <T> void checkWhatIsAtEnemyBoard(BattleShipBoard<T> b, T[][] expected) {
        for (int i = 0; i < b.getHeight(); i++) {
            for (int j = 0; j < b.getWidth(); j++) {
                assertEquals(expected[i][j], b.whatIsAtForEnemy(new Coordinate(i, j)));
            }
        }
    }

    /**
     * Test tryAddShip and whatIsAtForSelf function.
     */
    @Test
    public void test_tryAddShip_whatIsAt() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(10, 20, 'X');
        Character[][] expected = new Character[20][10];
        // check that it has no ships anywhere
        checkWhatIsAtMyBoard(b1, expected);
        // check that tryAddShip returns true
        assertEquals(null, b1.tryAddShip(new RectangleShip<Character>(new Coordinate(0, 0), 's', '*')));
        expected[0][0] = 's';
        checkWhatIsAtMyBoard(b1, expected);

        assertEquals(null, b1.tryAddShip(new RectangleShip<Character>(new Coordinate(2, 3), 's', '*')));
        expected[2][3] = 's';
        checkWhatIsAtMyBoard(b1, expected);

        assertEquals(null, b1.tryAddShip(new RectangleShip<Character>(new Coordinate(19, 9), 's', '*')));
        expected[19][9] = 's';
        checkWhatIsAtMyBoard(b1, expected);

        b1.fireAt(new Coordinate(19, 9));
        expected[19][9] = '*';
        checkWhatIsAtMyBoard(b1, expected);

        assertEquals("That placement is invalid: the ship overlaps another ship.",
                b1.tryAddShip(new RectangleShip<Character>(new Coordinate(19, 9), 's', '*')));
    }

    /**
     * Test whatIsAtForEnemy function.
     */
    @Test
    public void test_whatIsAtForEnemy() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(10, 20, 'X');
        Character[][] expected = new Character[20][10];
        // check that it has no ships anywhere
        checkWhatIsAtEnemyBoard(b1, expected);
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(0, 0), 's', '*'));
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(2, 3), 's', '*'));
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(19, 9), 's', '*'));
        checkWhatIsAtEnemyBoard(b1, expected);
        b1.fireAt(new Coordinate(0, 0));
        expected[0][0] = 's';
        checkWhatIsAtEnemyBoard(b1, expected);
        b1.fireAt(new Coordinate(1, 0));
        b1.fireAt(new Coordinate(1, 1));
        b1.fireAt(new Coordinate(2, 3));
        b1.fireAt(new Coordinate(19, 9));
        expected[1][0] = 'X';
        expected[1][1] = 'X';
        expected[2][3] = 's';
        expected[19][9] = 's';
        checkWhatIsAtEnemyBoard(b1, expected);
    }

    /**
     * Test fireAt function.
     */
    @Test
    public void test_fireAt() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(3, 4, 'X');
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        Ship<Character> s1 = shipFactory.makeDestroyer(new Placement("A0H"));
        b1.tryAddShip(s1);
        assertSame(s1, b1.fireAt(new Coordinate("A0")));
        assertFalse(s1.isSunk());
        assertSame(null, b1.fireAt(new Coordinate("B0")));
        assertFalse(s1.isSunk());
        assertSame(s1, b1.fireAt(new Coordinate("A1")));
        assertFalse(s1.isSunk());
        assertSame(s1, b1.fireAt(new Coordinate("A2")));
        assertTrue(s1.isSunk());
    }

    /**
     * Test hasLost function.
     */
    @Test
    public void test_hasLost() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(3, 4, 'X');
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        Ship<Character> s1 = shipFactory.makeDestroyer(new Placement("A0H"));
        Ship<Character> s2 = shipFactory.makeDestroyer(new Placement("B0H"));
        assertTrue(b1.hasLost());
        b1.tryAddShip(s1);
        b1.tryAddShip(s2);
        assertFalse(b1.hasLost());
        assertSame(s1, b1.fireAt(new Coordinate("A0")));
        assertSame(s1, b1.fireAt(new Coordinate("A1")));
        assertSame(s1, b1.fireAt(new Coordinate("A2")));
        assertFalse(b1.hasLost());
        assertSame(s2, b1.fireAt(new Coordinate("B0")));
        assertSame(s2, b1.fireAt(new Coordinate("B1")));
        assertSame(s2, b1.fireAt(new Coordinate("B2")));
        assertTrue(b1.hasLost());
    }

    /**
     * Test rmShip and whichShip function.
     */
    @Test
    public void test_rmShip_whichShip() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(3, 4, 'X');
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        Ship<Character> s1 = shipFactory.makeDestroyer(new Placement("A0H"));
        Ship<Character> s2 = shipFactory.makeDestroyer(new Placement("B0H"));
        b1.tryAddShip(s1);
        b1.tryAddShip(s2);
        assertSame(s1, b1.whichShip(new Coordinate("A0")));
        assertSame(s2, b1.whichShip(new Coordinate("B0")));
        b1.rmShip(s2);
        assertSame(null, b1.whichShip(new Coordinate("B0")));
    }

}
