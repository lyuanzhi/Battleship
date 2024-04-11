package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlacementTest {
    /**
     * Test the Placement Constructor.
     */
    @Test
    public void test_where_and_orientation() {
        Coordinate c1 = new Coordinate(2, 3);
        Placement p1 = new Placement(c1, 'V');
        assertEquals(c1, p1.getWhere());
        assertEquals('V', p1.getOrientation());
    }

    /**
     * Test the equals function.
     */
    @Test
    public void test_equals() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        Coordinate c3 = new Coordinate(3, 2);
        Placement p1 = new Placement(c1, 'V');
        Placement p2 = new Placement(c2, 'v');
        Placement p3 = new Placement(c1, 'H');
        Placement p4 = new Placement(c2, 'H');
        Placement p5 = new Placement(c3, 'V');
        assertEquals(p1, p1); // equals should be reflexsive
        assertEquals(p1, p2); // different objects but same contents
        assertNotEquals(p1, p5); // different contents
        assertNotEquals(p1, p3);
        assertNotEquals(p4, p5);
        assertNotEquals(p1, "(1, 2)"); // different types
    }

    /**
     * Test the toString function.
     */
    @Test
    public void test_toString() {
        Coordinate c1 = new Coordinate(1, 2);
        Placement p1 = new Placement(c1, 'V');
        Placement p2 = new Placement(c1, 'H');
        assertEquals("(1, 2) | V", p1.toString());
        assertEquals("(1, 2) | H", p2.toString());
    }

    /**
     * Test the hashCode function.
     */
    @Test
    public void test_hashCode() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(0, 3);
        Placement p1 = new Placement(c1, 'V');
        Placement p2 = new Placement(c1, 'V');
        Placement p3 = new Placement(c1, 'v');
        Placement p4 = new Placement(c1, 'H');
        Placement p5 = new Placement(c2, 'V');
        assertEquals(p1.hashCode(), p1.hashCode()); // equals should be reflexsive
        assertEquals(p1.hashCode(), p2.hashCode()); // different objects but same contents
        assertEquals(p1.hashCode(), p3.hashCode()); // checking case insensitivity
        assertNotEquals(p1.hashCode(), p4.hashCode());
        assertNotEquals(p1.hashCode(), p5.hashCode());
    }

    /**
     * Test the valid cases of the string constructor.
     */
    @Test
    void test_string_constructor_valid_cases() {
        Placement p1 = new Placement("B3H");
        assertEquals('H', p1.getOrientation());
        assertEquals(new Coordinate("B3"), p1.getWhere());
        Placement p2 = new Placement("D5h");
        assertEquals('H', p2.getOrientation());
        assertEquals(new Coordinate("D5"), p2.getWhere());
        Placement p3 = new Placement("A9V");
        assertEquals('V', p3.getOrientation());
        assertEquals(new Coordinate("A9"), p3.getWhere());
        Placement p4 = new Placement("Z0v");
        assertEquals('V', p4.getOrientation());
        assertEquals(new Coordinate("Z0"), p4.getWhere());

    }

    /**
     * Test the invalid cases of the string constructor.
     */
    @Test
    public void test_string_constructor_error_cases() {
        assertThrows(IllegalArgumentException.class, () -> new Placement(""));
        assertThrows(IllegalArgumentException.class, () -> new Placement("A"));
        assertThrows(IllegalArgumentException.class, () -> new Placement("A1"));
        assertThrows(IllegalArgumentException.class, () -> new Placement("A1V2"));
    }

}
