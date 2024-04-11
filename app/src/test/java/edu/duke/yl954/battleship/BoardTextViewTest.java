package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {
    /**
     * Helper function to check that the representation of the player's own board is
     * correct.
     */
    private void myBoardHelper(Board<Character> b, String expectedHeader, String body) {
        BoardTextView view = new BoardTextView(b);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + body + expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());
    }

    /**
     * Helper function to check that the representation of the player's enemy board
     * is correct.
     */
    private void enemyBoardHelper(Board<Character> b, String expectedHeader, String body, Coordinate c) {
        BoardTextView view = new BoardTextView(b);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + body + expectedHeader;
        assertEquals(expected, view.displayEnemyBoard(c, view.toDisplay.whatIsAtForEnemy(c)));
    }

    /**
     * Test the 2*2 empty board.
     */
    @Test
    public void test_display_empty_2by2() {
        Board<Character> b = new BattleShipBoard<Character>(2, 2, 'X');
        String body = "A  |  A\n" +
                "B  |  B\n";
        myBoardHelper(b, "  0|1\n", body);
    }

    /**
     * Test the 3*2 empty board.
     */
    @Test
    public void test_display_empty_3by2() {
        Board<Character> b = new BattleShipBoard<Character>(3, 2, 'X');
        String body = "A  | |  A\n" +
                "B  | |  B\n";
        myBoardHelper(b, "  0|1|2\n", body);
    }

    /**
     * Test the 3*5 empty board.
     */
    @Test
    public void test_display_empty_3by5() {
        Board<Character> b = new BattleShipBoard<Character>(3, 5, 'X');
        String body = "A  | |  A\n" +
                "B  | |  B\n" +
                "C  | |  C\n" +
                "D  | |  D\n" +
                "E  | |  E\n";
        myBoardHelper(b, "  0|1|2\n", body);
    }

    /**
     * Fully test the 4*3 board.
     */
    @Test
    public void test_display_4by3() {
        Board<Character> b = new BattleShipBoard<Character>(4, 3, 'X');
        String body = "A  | | |  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
        myBoardHelper(b, "  0|1|2|3\n", body);

        b.tryAddShip(new RectangleShip<Character>(new Coordinate(0, 0), 's', '*'));
        body = "A s| | |  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
        myBoardHelper(b, "  0|1|2|3\n", body);

        b.tryAddShip(new RectangleShip<Character>(new Coordinate(2, 3), 's', '*'));
        body = "A s| | |  A\n" + "B  | | |  B\n" + "C  | | |s C\n";
        myBoardHelper(b, "  0|1|2|3\n", body);

        b.tryAddShip(new RectangleShip<Character>(new Coordinate(0, 3), 's', '*'));
        body = "A s| | |s A\n" + "B  | | |  B\n" + "C  | | |s C\n";
        myBoardHelper(b, "  0|1|2|3\n", body);

        b.tryAddShip(new RectangleShip<Character>(new Coordinate(2, 0), 's', '*'));
        body = "A s| | |s A\n" + "B  | | |  B\n" + "C s| | |s C\n";
        myBoardHelper(b, "  0|1|2|3\n", body);

        // test enemy board
        body = "A  | | |  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
        enemyBoardHelper(b, "  0|1|2|3\n", body, null);
        b.fireAt(new Coordinate(0, 10));
        body = "A  | | |  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
        enemyBoardHelper(b, "  0|1|2|3\n", body, new Coordinate(0, 10));
        b.fireAt(new Coordinate(1, 1));
        body = "A  | | |  A\n" + "B  |X| |  B\n" + "C  | | |  C\n";
        enemyBoardHelper(b, "  0|1|2|3\n", body, new Coordinate(1, 1));
        b.fireAt(new Coordinate(0, 0));
        body = "A s| | |  A\n" + "B  |X| |  B\n" + "C  | | |  C\n";
        enemyBoardHelper(b, "  0|1|2|3\n", body, new Coordinate(0, 0));
        b.fireAt(new Coordinate(1, 3));
        body = "A s| | |  A\n" + "B  |X| |X B\n" + "C  | | |  C\n";
        enemyBoardHelper(b, "  0|1|2|3\n", body, new Coordinate(1, 3));
        b.fireAt(new Coordinate(2, 3));
        body = "A s| | |  A\n" + "B  |X| |X B\n" + "C  | | |s C\n";
        enemyBoardHelper(b, "  0|1|2|3\n", body, new Coordinate(2, 3));
    }

    /**
     * Test the invalid board size.
     */
    @Test
    public void test_invalid_board_size() {
        Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20, 'X');
        Board<Character> tallBoard = new BattleShipBoard<Character>(10, 27, 'X');
        assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
        assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));
    }

    /**
     * Test the displayMyBoardWithEnemyNextToIt function.
     */
    @Test
    public void test_displayMyBoardWithEnemyNextToIt() {
        Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
        Board<Character> b2 = new BattleShipBoard<Character>(10, 20, 'X');
        BoardTextView view1 = new BoardTextView(b1);
        BoardTextView view2 = new BoardTextView(b2);
        String res = view1.displayMyBoardWithEnemyNextToIt(view2, "Player A's turn:", "Your ocean", "Player B's ocean",
                null, null);
        String expected = "Player A's turn:\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "K  | | | | | | | | |  K                K  | | | | | | | | |  K\n" +
                "L  | | | | | | | | |  L                L  | | | | | | | | |  L\n" +
                "M  | | | | | | | | |  M                M  | | | | | | | | |  M\n" +
                "N  | | | | | | | | |  N                N  | | | | | | | | |  N\n" +
                "O  | | | | | | | | |  O                O  | | | | | | | | |  O\n" +
                "P  | | | | | | | | |  P                P  | | | | | | | | |  P\n" +
                "Q  | | | | | | | | |  Q                Q  | | | | | | | | |  Q\n" +
                "R  | | | | | | | | |  R                R  | | | | | | | | |  R\n" +
                "S  | | | | | | | | |  S                S  | | | | | | | | |  S\n" +
                "T  | | | | | | | | |  T                T  | | | | | | | | |  T\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        assertEquals(expected, res);
    }

}
