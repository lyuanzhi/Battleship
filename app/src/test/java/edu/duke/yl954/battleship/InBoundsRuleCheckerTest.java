package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {

    /**
     * Test the InBoundsRuleChecker.
     */
    @Test
    public void test_checkPlacement() {
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Ship<Character> s1 = f.makeSubmarine(new Placement(new Coordinate(1, 2), 'V'));
        Ship<Character> s2 = f.makeSubmarine(new Placement(new Coordinate(19, 2), 'V'));
        Ship<Character> s3 = f.makeSubmarine(new Placement(new Coordinate(1, 9), 'H'));
        Ship<Character> s4 = f.makeSubmarine(new Placement(new Coordinate(-1, 2), 'H'));
        Ship<Character> s5 = f.makeSubmarine(new Placement(new Coordinate(1, -2), 'H'));
        PlacementRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
        Board<Character> b = new BattleShipBoard<>(10, 20, checker, 'X');
        assertEquals(null, checker.checkPlacement(s1, b));
        String errMsg = "That placement is invalid: the ship goes off the bottom of the board.";
        assertEquals(errMsg, checker.checkPlacement(s2, b));
        errMsg = "That placement is invalid: the ship goes off the right of the board.";
        assertEquals(errMsg, checker.checkPlacement(s3, b));
        errMsg = "That placement is invalid: the ship goes off the top of the board.";
        assertEquals(errMsg, checker.checkPlacement(s4, b));
        errMsg = "That placement is invalid: the ship goes off the left of the board.";
        assertEquals(errMsg, checker.checkPlacement(s5, b));
    }

}
