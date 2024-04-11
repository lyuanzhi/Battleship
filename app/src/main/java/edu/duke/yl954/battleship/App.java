/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.yl954.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main application class for a Battleship game. It handles game initialization,
 * user interactions, and displays the game board.
 */
public class App {

    /**
     * The first player
     */
    private final BasicPlayer player1;
    /**
     * The second player
     */
    private final BasicPlayer player2;

    /**
     * Constructs an instance of the App with player1 and player2.
     *
     * @param player1 The first player.
     * @param player1 The second player.
     */
    public App(BasicPlayer player1, BasicPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Manages the entire ship placement phase for both player1 and player2.
     *
     * @throws IOException If an error occurs while reading input from the players.
     */
    public void doPlacementPhase() throws IOException {
        player1.doPlacementPhase();
        player2.doPlacementPhase();
    }

    /**
     * Manages the attacking phase of the Battleship game. During this phase,
     * players take turns attacking the opponent's board. The phase continues until
     * one of the players successfully sinks all ships of the opponent, thereby
     * winning the game.
     * 
     * @throws IOException if an error occurs while reading input.
     */
    public void doAttackingPhase() throws IOException {
        while (true) {
            player1.playOneTurn(player2);
            if (player2.checkLost("Player " + player1.name + " Win!")) {
                return;
            }
            player2.playOneTurn(player1);
            if (player1.checkLost("Player " + player2.name + " Win!")) {
                return;
            }
        }
    }

    /**
     * Main method to start the application. Initializes the board, app, and starts
     * the game.
     *
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
        Board<Character> b2 = new BattleShipBoard<Character>(10, 20, 'X');
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        V2ShipFactory factory = new V2ShipFactory();
        BasicPlayer p1 = null;
        BasicPlayer p2 = null;
        System.out.println("Game mode selection:\n");
        System.out.println("1. human (A) vs human (B)");
        System.out.println("2. human (A) vs computer (B)");
        System.out.println("3. computer (A) vs human (B)");
        System.out.println("4. computer (A) vs computer (B)");
        while (true) {
            System.out.println("Which one do you want to choose? (Enter 1-4)");
            String s = input.readLine();
            if (s == null || s.length() != 1) {
                System.out.println("That action is invalid: it does not have the correct format.");
                continue;
            }
            char ans = s.charAt(0);
            if (ans != '1' && ans != '2' && ans != '3' && ans != '4') {
                System.out.println("That action is invalid: it does not have the correct format.");
                continue;
            }
            if (ans == '1') {
                p1 = new TextPlayer("A", b1, input, System.out, factory);
                p2 = new TextPlayer("B", b2, input, System.out, factory);
            }
            if (ans == '2') {
                p1 = new TextPlayer("A", b1, input, System.out, factory);
                p2 = new AIPlayer("B", b2, input, System.out, factory);
            }
            if (ans == '3') {
                p1 = new AIPlayer("A", b1, input, System.out, factory);
                p2 = new TextPlayer("B", b2, input, System.out, factory);
            }
            if (ans == '4') {
                p1 = new AIPlayer("A", b1, input, System.out, factory);
                p2 = new AIPlayer("B", b2, input, System.out, factory);
            }
            break;
        }
        App app = new App(p1, p2);
        app.doPlacementPhase();
        app.doAttackingPhase();
    }
}
