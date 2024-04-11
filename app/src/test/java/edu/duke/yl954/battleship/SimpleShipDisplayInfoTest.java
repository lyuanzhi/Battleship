package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SimpleShipDisplayInfoTest {
    /**
     * Test the getInfo function.
     */
    @Test
    public void test_getInfo() {
        SimpleShipDisplayInfo<Character> s1 = new SimpleShipDisplayInfo<>('s', '*');
        assertEquals('s', s1.getInfo(new Coordinate(0, 0), false));
        assertEquals('*', s1.getInfo(new Coordinate(0, 0), true));
    }
}
