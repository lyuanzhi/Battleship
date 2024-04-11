package edu.duke.yl954.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class AIPlayerTest {
    /**
     * Helper function used to create player.
     */
    private AIPlayer createAIPlayer(int w, int h, String inputData, OutputStream bytes) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V2ShipFactory shipFactory = new V2ShipFactory();
        return new AIPlayer("A", board, input, output, shipFactory);
    }

    /**
     * Test the checkLost and doOnePlacement function.
     */
    @Test
    void test_checkLost_doOnePlacement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        AIPlayer player1 = createAIPlayer(10, 20, "", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        assertTrue(player1.checkLost("Player B Win!"));
        assertEquals("Player B Win!\n", bytes.toString());
        bytes.reset();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        bytes.reset();
        assertFalse(player1.checkLost("Player B Win!"));
        player1.theBoard.fireAt(new Coordinate("A0"));
        player1.theBoard.fireAt(new Coordinate("B0"));
        player1.theBoard.fireAt(new Coordinate("C0"));
        assertTrue(player1.checkLost("Player B Win!"));
        assertEquals("Player B Win!\n", bytes.toString());
        bytes.reset();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        bytes.reset();
        assertFalse(player1.checkLost("Player B Win!"));
        player1.theBoard.fireAt(new Coordinate("A2"));
        player1.theBoard.fireAt(new Coordinate("B2"));
        player1.theBoard.fireAt(new Coordinate("C2"));
        player1.theBoard.fireAt(new Coordinate("B3"));
        assertTrue(player1.checkLost("Player B Win!"));
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        bytes.reset();
        assertFalse(player1.checkLost("Player B Win!"));
        player1.theBoard.fireAt(new Coordinate("A4"));
        player1.theBoard.fireAt(new Coordinate("B4"));
        player1.theBoard.fireAt(new Coordinate("C4"));
        player1.theBoard.fireAt(new Coordinate("B5"));
        assertTrue(player1.checkLost("Player B Win!"));
        player1.placeIndex = 1000;
        assertThrows(IllegalArgumentException.class,
                () -> player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p)));
    }

    /**
     * Test the playOneTurn function.
     */
    @Test
    void test_playOneTurn() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        AIPlayer player1 = createAIPlayer(10, 20, "", bytes);
        AIPlayer player2 = createAIPlayer(10, 20, "", bytes);
        AbstractShipFactory<Character> shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        bytes.reset();
        player2.playOneTurn(player1);
        assertEquals("Player A hit a Destroyer!\n", bytes.toString());
        bytes.reset();
        player2.playOneTurn(player1);
        assertEquals("Player A missed!\n", bytes.toString());
        bytes.reset();
        player2.playOneTurn(player1);
        assertEquals("Player A hit a Battleship!\n", bytes.toString());
        bytes.reset();
        player2.playOneTurn(player1);
        assertEquals("Player A missed!\n", bytes.toString());
        bytes.reset();
    }
}
