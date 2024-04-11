package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {

    /**
     * Helper function used to create player.
     */
    private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V2ShipFactory shipFactory = new V2ShipFactory();
        return new TextPlayer("A", board, input, output, shipFactory);
    }

    /**
     * Test the readPlacement function.
     */
    @Test
    void test_read_placement() throws IOException {
        // collect the output using ByteArrayOutputStream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);
        String prompt = "Please enter a location for a ship:";
        Placement[] expected = new Placement[3];
        expected[0] = new Placement(new Coordinate(1, 2), 'V');
        expected[1] = new Placement(new Coordinate(2, 8), 'H');
        expected[2] = new Placement(new Coordinate(0, 4), 'V');
        for (int i = 0; i < expected.length; i++) {
            Placement p = player.readPlacement(prompt);
            assertEquals(p, expected[i]); // did we get the right Placement back
            assertEquals(prompt + "\n", bytes.toString()); // should have printed prompt and newline
            bytes.reset(); // clear out bytes for next time around
        }
    }

    /**
     * Test the doOnePlacement function.
     */
    @Test
    void test_doOnePlacement() throws IOException {
        // collect the output using ByteArrayOutputStream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(4, 3, "A0V\nC1H\nb1h\n", bytes);
        String[] expected = new String[3];
        expected[0] = "  0|1|2|3\n" +
                "A d| | |  A\n" +
                "B d| | |  B\n" +
                "C d| | |  C\n" +
                "  0|1|2|3\n";
        expected[1] = "  0|1|2|3\n" +
                "A d| | |  A\n" +
                "B d| | |  B\n" +
                "C d|d|d|d C\n" +
                "  0|1|2|3\n";
        expected[2] = "  0|1|2|3\n" +
                "A d| | |  A\n" +
                "B d|d|d|d B\n" +
                "C d|d|d|d C\n" +
                "  0|1|2|3\n";
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        for (int i = 0; i < expected.length; i++) {
            player.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
            assertEquals("Player A Where do you want to place a Destroyer?\n" + expected[i] + "\n", bytes.toString());
            bytes.reset();
        }
    }

    /**
     * Test the EOF case.
     */
    @Test
    void test_EOF() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer(4, 3, "", bytes);
        TextPlayer player2 = createTextPlayer(4, 3, "", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        assertThrows(EOFException.class,
                () -> player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p)));
        assertThrows(EOFException.class, () -> player1.readCoordinate("", player2));
    }

    /**
     * Test the invalid placements.
     */
    @Test
    void test_invalid_placements() {
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        TextPlayer player1 = createTextPlayer(4, 3, "A0Q\n", new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class,
                () -> player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p)));
        TextPlayer player2 = createTextPlayer(4, 3, "AAV\n", new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class,
                () -> player2.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p)));
        TextPlayer player3 = createTextPlayer(4, 3, "A9H\n", new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class,
                () -> player3.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p)));
    }

    /**
     * Test the readCoordinate function.
     */
    @Test
    void test_readCoordinate() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer(10, 20, "B2\nC8\na4\n", bytes);
        TextPlayer player2 = createTextPlayer(10, 20, "", bytes);
        String prompt = "Please choose a position to fire at:";
        Coordinate[] expected = new Coordinate[3];
        expected[0] = new Coordinate(1, 2);
        expected[1] = new Coordinate(2, 8);
        expected[2] = new Coordinate(0, 4);
        for (int i = 0; i < expected.length; i++) {
            Coordinate c = player1.readCoordinate(prompt, player2);
            assertEquals(c, expected[i]);
            assertEquals(prompt + "\n", bytes.toString());
            bytes.reset();
        }
    }

    /**
     * Test the invalid coordinates.
     */
    @Test
    void test_invalid_coordinates() {
        TextPlayer player1 = createTextPlayer(4, 3, "AA\n", new ByteArrayOutputStream());
        TextPlayer player2 = createTextPlayer(4, 3, "D1\n", new ByteArrayOutputStream());
        TextPlayer player3 = createTextPlayer(4, 3, "A4\n", new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class, () -> player1.readCoordinate("", player2));
        assertThrows(IllegalArgumentException.class, () -> player2.readCoordinate("", player3));
        assertThrows(IllegalArgumentException.class, () -> player3.readCoordinate("", player1));
    }

    /**
     * Test the checkLost function.
     */
    @Test
    void test_checkLost() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer(4, 3, "A0H\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        assertTrue(player1.checkLost("Player B Win!"));
        assertEquals("Player B Win!\n", bytes.toString());
        bytes.reset();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        bytes.reset();
        assertFalse(player1.checkLost("Player B Win!"));
        player1.theBoard.fireAt(new Coordinate("A0"));
        player1.theBoard.fireAt(new Coordinate("A1"));
        player1.theBoard.fireAt(new Coordinate("A2"));
        assertTrue(player1.checkLost("Player B Win!"));
        assertEquals("Player B Win!\n", bytes.toString());
        bytes.reset();
    }

    /**
     * Test the readAction function.
     */
    @Test
    void test_readAction() throws IOException {
        TextPlayer player1 = createTextPlayer(4, 3, "", new ByteArrayOutputStream());
        TextPlayer player2 = createTextPlayer(4, 3, "D1\n", new ByteArrayOutputStream());
        TextPlayer player3 = createTextPlayer(4, 3, "A\n", new ByteArrayOutputStream());
        TextPlayer player4 = createTextPlayer(4, 3, "M\n", new ByteArrayOutputStream());
        player4.moveRemain = 0;
        TextPlayer player5 = createTextPlayer(4, 3, "S\n", new ByteArrayOutputStream());
        player5.scanRemain = 0;
        assertThrows(EOFException.class, () -> player1.readAction(""));
        assertThrows(IllegalArgumentException.class, () -> player2.readAction(""));
        assertThrows(IllegalArgumentException.class, () -> player3.readAction(""));
        assertThrows(IllegalArgumentException.class, () -> player4.readAction(""));
        assertThrows(IllegalArgumentException.class, () -> player5.readAction(""));
    }

    /**
     * Test the playOneTurn function (fire).
     */
    @Test
    void test_playOneTurn_fire() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer(10, 20,
                "A0H\nx\nf\nZ0\n\nB0\nf\nA1\nA1\nf\nA1\ns\nbbb\nb4\n", bytes);
        TextPlayer player2 = createTextPlayer(10, 20, "A0H\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player2.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        bytes.reset();
        String tableTop = "Player A's turn:\n" +
                "     Your ocean                           Player A's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        String tableBottom = "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
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
        String table1 = tableTop +
                "A d|d|d| | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                tableBottom;
        String table2 = tableTop +
                "A d|d|d| | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B X| | | | | | | | |  B\n" +
                tableBottom;
        String table3 = tableTop +
                "A d|d|d| | | | | | |  A                A  |d| | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B X| | | | | | | | |  B\n" +
                tableBottom;
        String prompt = "Player A Please choose a position to fire at:\n";
        String prompt2 = "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "---------------------------------------------------------------------------\n";
        String prompt3 = "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n\n" +
                "F Fire at a square\n" +
                "S Sonar scan (3 remaining)\n" +
                "---------------------------------------------------------------------------\n";
        String prompt4 = "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "---------------------------------------------------------------------------\n";
        String actionStr = "Player A, what would you like to do?\n";
        String err0 = "That action is invalid: it does not have the correct format.\n";
        String err1 = "That coordinate is invalid: the point goes off the bounds of the board.\n";
        String err2 = "That coordinate is invalid: it does not have the correct format.\n";
        player1.playOneTurn(player2);
        assertEquals(table1 + "\n" + prompt2 + actionStr + err0 + actionStr + prompt + err1 + prompt + err2 + prompt
                + "You missed!\n", bytes.toString());
        bytes.reset();
        player1.moveRemain = 0;
        player1.playOneTurn(player2);
        assertEquals(table2 + "\n" + prompt3 + actionStr + prompt + "You hit a Destroyer!\n", bytes.toString());
        bytes.reset();

        player1.scanRemain = 0;
        player1.playOneTurn(player2);
        assertEquals(table3 + "\n" + prompt + "You hit a Destroyer!\n", bytes.toString());
        bytes.reset();

        player1.scanRemain = 0;
        player1.moveRemain = 3;
        player1.playOneTurn(player2);
        assertEquals(table3 + "\n" + prompt4 + actionStr + prompt + "You hit a Destroyer!\n", bytes.toString());
        bytes.reset();
    }

    /**
     * Test the playOneTurn function (scan).
     */
    @Test
    void test_playOneTurn_scan() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer(10, 20,
                "A0H\ns\nbbb\nb4\ns\nb9\n", bytes);
        TextPlayer player2 = createTextPlayer(10, 20, "A0H\nA1l\na7d\nb6h\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player2.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player2.doOnePlacement("Carrier", (p) -> shipFactory.makeCarrier(p));
        player2.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        bytes.reset();
        String tableTop = "Player A's turn:\n" +
                "     Your ocean                           Player A's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        String tableBottom = "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
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
        String table1 = tableTop +
                "A d|d|d| | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                tableBottom;
        String prompt1 = "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "---------------------------------------------------------------------------\n";
        String prompt2 = "---------------------------------------------------------------------------\n" +
                "Submarines occupy 2 squares\n" +
                "Destroyers occupy 1 squares\n" +
                "Battleships occupy 0 squares\n" +
                "Carriers occupy 7 squares\n" +
                "---------------------------------------------------------------------------\n";
        String prompt3 = "Player A Please choose a center coordinate of a sonar scan:\n";
        String prompt4 = "---------------------------------------------------------------------------\n" +
                "Submarines occupy 2 squares\n" +
                "Destroyers occupy 0 squares\n" +
                "Battleships occupy 4 squares\n" +
                "Carriers occupy 0 squares\n" +
                "---------------------------------------------------------------------------\n";
        String actionStr = "Player A, what would you like to do?\n";
        String err = "That coordinate is invalid: it does not have the correct format.\n";
        player1.playOneTurn(player2);
        assertEquals(table1 + "\n" + prompt1 + actionStr + prompt3 + err + prompt3 + prompt2, bytes.toString());
        bytes.reset();

        player1.scanRemain = 3;
        player1.playOneTurn(player2);
        assertEquals(table1 + "\n" + prompt1 + actionStr + prompt3 + prompt4, bytes.toString());
        bytes.reset();
    }

    /**
     * Test the playOneTurn function (move).
     */
    @Test
    void test_playOneTurn_move() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer(10, 20,
                "A0H\nA1l\na7d\nb6h\nm\na100\nm\na3\na0u\nm\na3\nB0R\nm\na0\na0h\n", bytes);
        TextPlayer player2 = createTextPlayer(10, 20, "A0H\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player1.doOnePlacement("Carrier", (p) -> shipFactory.makeCarrier(p));
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player2.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player1.theBoard.fireAt(new Coordinate("a0"));
        player1.theBoard.fireAt(new Coordinate("a3"));
        bytes.reset();
        String tableTop = "Player A's turn:\n" +
                "     Your ocean                           Player A's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        String tableBottom = "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
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
        String table1 = tableTop +
                "A *|d|d|*|c|c| |b|b|b A                A  | | | | | | | | |  A\n" +
                "B  |c|c|c|c| |s|s|b|  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                tableBottom;
        String table2 = tableTop +
                "A *|d|d| | | | |b|b|b A                A  | | | | | | | | |  A\n" +
                "B  |c|c|c|c| |s|s|b|  B                B  | | | | | | | | |  B\n" +
                "C c|c|*| | | | | | |  C                C  | | | | | | | | |  C\n" +
                tableBottom;
        String prompt1 = "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "---------------------------------------------------------------------------\n";
        String prompt2 = "Which ship do you want to move?\n(Enter any coordinate which is a part of the ship)\n";
        String prompt3 = "Which position do you want to move to? (Enter placement)\n";
        String actionStr = "Player A, what would you like to do?\n";
        String err1 = "That coordinate is invalid: it does not have the correct format.\n";
        String err2 = "That placement is invalid: the ship overlaps another ship.\n";
        player1.playOneTurn(player2);
        assertEquals(table1 + "\n" + prompt1 + actionStr + prompt2 + err1 + prompt1 + actionStr + prompt2 + prompt3
                + err2 + prompt1 + actionStr + prompt2 + prompt3, bytes.toString());
        bytes.reset();

        player1.moveRemain = 3;
        player1.playOneTurn(player2);
        assertEquals(table2 + "\n" + prompt1 + actionStr + prompt2 + prompt3, bytes.toString());
        bytes.reset();
    }

}
