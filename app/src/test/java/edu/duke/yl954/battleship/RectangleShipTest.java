package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {
    /**
     * Test the makeCoords function.
     */
    @Test
    public void test_makeCoords() {
        Coordinate c1 = new Coordinate(0, 0);
        HashSet<Coordinate> coords1 = RectangleShip.makeCoords(c1, 1, 1, null);
        assertTrue(coords1.contains(new Coordinate(0, 0)));
        assertEquals(1, coords1.size());
        HashSet<Coordinate> coords2 = RectangleShip.makeCoords(c1, 2, 3, null);
        assertTrue(coords2.contains(new Coordinate(0, 0)));
        assertTrue(coords2.contains(new Coordinate(1, 0)));
        assertTrue(coords2.contains(new Coordinate(2, 0)));
        assertTrue(coords2.contains(new Coordinate(0, 1)));
        assertTrue(coords2.contains(new Coordinate(1, 1)));
        assertTrue(coords2.contains(new Coordinate(2, 1)));
        assertEquals(6, coords2.size());
        HashSet<Coordinate> coords3 = RectangleShip.makeCoords(c1, 0, 0, null);
        assertEquals(0, coords3.size());
        boolean mask[][] = { { true, false }, { false, false }, { true, true } };
        HashSet<Coordinate> coords4 = RectangleShip.makeCoords(c1, 2, 3, mask);
        assertTrue(coords4.contains(new Coordinate(0, 0)));
        assertFalse(coords4.contains(new Coordinate(1, 0)));
        assertTrue(coords4.contains(new Coordinate(2, 0)));
        assertFalse(coords4.contains(new Coordinate(0, 1)));
        assertFalse(coords4.contains(new Coordinate(1, 1)));
        assertTrue(coords4.contains(new Coordinate(2, 1)));
        assertEquals(3, coords4.size());
    }

    /**
     * Test the RectangleShip Constructors.
     */
    @Test
    public void test_RectangleShip() {
        RectangleShip<Character> r1 = new RectangleShip<>(new Coordinate(0, 0), 's', '*');
        assertTrue(r1.occupiesCoordinates(new Coordinate(0, 0)));
        RectangleShip<Character> r2 = new RectangleShip<>("submarine", new Coordinate(1, 1), 1, 2, 's', '*', null, 'v');
        assertEquals("submarine", r2.getName()); // check that getName worked correctly
        assertTrue(r2.occupiesCoordinates(new Coordinate(1, 1)));
        assertTrue(r2.occupiesCoordinates(new Coordinate(2, 1)));
        assertFalse(r2.occupiesCoordinates(new Coordinate(3, 1)));
        assertFalse(r2.occupiesCoordinates(new Coordinate(2, 2)));
    }

    /**
     * Test the recordHitAt and wasHitAt function.
     */
    @Test
    public void test_recordHitAt_wasHitAt() {
        RectangleShip<Character> r1 = new RectangleShip<>("submarine", new Coordinate(0, 0), 1, 2, 's', '*', null, 'v');
        r1.recordHitAt(new Coordinate(0, 0));
        assertTrue(r1.wasHitAt(new Coordinate(0, 0)));
        assertFalse(r1.wasHitAt(new Coordinate(1, 0)));
        r1.recordHitAt(new Coordinate(1, 0));
        assertTrue(r1.wasHitAt(new Coordinate(0, 0)));
        assertTrue(r1.wasHitAt(new Coordinate(1, 0)));
    }

    /**
     * Test the invalid cases of recordHitAt, wasHitAt, and getDisplayInfoAt
     * function.
     */
    @Test
    public void test_invalid() {
        RectangleShip<Character> r1 = new RectangleShip<>("submarine", new Coordinate(0, 0), 1, 2, 's', '*', null, 'v');
        assertThrows(IllegalArgumentException.class, () -> r1.recordHitAt(new Coordinate(5, 0)));
        assertThrows(IllegalArgumentException.class, () -> r1.recordHitAt(new Coordinate(0, 5)));
        assertThrows(IllegalArgumentException.class, () -> r1.wasHitAt(new Coordinate(5, 0)));
        assertThrows(IllegalArgumentException.class, () -> r1.wasHitAt(new Coordinate(0, 5)));
        assertThrows(IllegalArgumentException.class, () -> r1.getDisplayInfoAt(new Coordinate(5, 0), true));
        assertThrows(IllegalArgumentException.class, () -> r1.getDisplayInfoAt(new Coordinate(0, 5), true));
    }

    /**
     * Test the isSunk function.
     */
    @Test
    public void test_isSunk() {
        RectangleShip<Character> r1 = new RectangleShip<>("submarine", new Coordinate(0, 0), 2, 2, 's', '*', null, 'v');
        assertFalse(r1.isSunk());
        r1.recordHitAt(new Coordinate(0, 0));
        assertFalse(r1.isSunk());
        r1.recordHitAt(new Coordinate(1, 0));
        assertFalse(r1.isSunk());
        r1.recordHitAt(new Coordinate(0, 1));
        assertFalse(r1.isSunk());
        r1.recordHitAt(new Coordinate(1, 1));
        assertTrue(r1.isSunk());
        assertEquals(4, r1.myPieces.size());
    }

    /**
     * Test the getDisplayInfoAt function.
     */
    @Test
    public void test_getDisplayInfoAt() {
        RectangleShip<Character> r1 = new RectangleShip<>("submarine", new Coordinate(0, 0), 1, 2, 's', '*', null, 'v');
        assertEquals('s', r1.getDisplayInfoAt(new Coordinate(0, 0), true));
        assertEquals('s', r1.getDisplayInfoAt(new Coordinate(1, 0), true));
        assertEquals(null, r1.getDisplayInfoAt(new Coordinate(1, 0), false));
        r1.recordHitAt(new Coordinate(0, 0));
        r1.recordHitAt(new Coordinate(1, 0));
        assertEquals('*', r1.getDisplayInfoAt(new Coordinate(0, 0), true));
        assertEquals('*', r1.getDisplayInfoAt(new Coordinate(1, 0), true));
        assertEquals('s', r1.getDisplayInfoAt(new Coordinate(1, 0), false));
        assertEquals(2, r1.myPieces.size());
    }

    /**
     * Test the get functions.
     */
    @Test
    public void test_getCoordinates() {
        RectangleShip<Character> r1 = new RectangleShip<>("submarine", new Coordinate(0, 0), 1, 2, 's', '*', null, 'v');
        for (Coordinate c : r1.getCoordinates()) {
            assertTrue(r1.occupiesCoordinates(c));
        }
        r1.getMask();
        assertEquals(null, r1.getMask());
        assertEquals('v', r1.getOrientation());
        assertEquals(new Coordinate(0, 0), r1.getUpperLeft());
    }
}
