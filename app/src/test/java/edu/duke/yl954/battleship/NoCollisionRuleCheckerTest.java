package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NoCollisionRuleCheckerTest {
    /**
     * Test the NoCollisionRuleChecker.
     */
    @Test
    public void test_checkPlacement() {
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Ship<Character> s1 = f.makeSubmarine(new Placement(new Coordinate(1, 2), 'V'));
        Ship<Character> s2 = f.makeSubmarine(new Placement(new Coordinate(2, 2), 'V'));
        PlacementRuleChecker<Character> checker = new NoCollisionRuleChecker<>(null);
        Board<Character> b = new BattleShipBoard<>(10, 20, checker, 'X');
        assertEquals(null, checker.checkPlacement(s2, b));
        b.tryAddShip(s1);
        assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(s2, b));
    }

    /**
     * Test both the NoCollisionRuleChecker and InBoundsRuleChecker together.
     */
    @Test
    public void test_combine() {
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Ship<Character> s1 = f.makeSubmarine(new Placement(new Coordinate(1, 2), 'V'));
        Ship<Character> s2 = f.makeSubmarine(new Placement(new Coordinate(2, 2), 'V'));
        Ship<Character> s3 = f.makeSubmarine(new Placement(new Coordinate(2, 9), 'H'));
        Ship<Character> s4 = f.makeSubmarine(new Placement(new Coordinate(10, 5), 'V'));
        PlacementRuleChecker<Character> checker = new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null));
        Board<Character> b = new BattleShipBoard<>(10, 20, checker, 'X');
        assertEquals(null, checker.checkPlacement(s1, b));
        assertEquals(null, checker.checkPlacement(s2, b));
        assertEquals("That placement is invalid: the ship goes off the right of the board.",
                checker.checkPlacement(s3, b));
        b.tryAddShip(s1);
        assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(s1, b));
        assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(s2, b));
        assertEquals("That placement is invalid: the ship goes off the right of the board.",
                checker.checkPlacement(s3, b));
        assertEquals(null, checker.checkPlacement(s4, b));
    }
}
