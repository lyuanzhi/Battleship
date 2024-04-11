package edu.duke.yl954.battleship;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import edu.duke.yl954.battleship.vo.MoveRespEventData;

import java.util.UUID;

public class WebPlayer extends BasicPlayer {
    public UUID myId;
    public Integer submarineNum;
    public Integer destroyerNum;
    public Integer battleshipNum;
    public Integer carrierNum;
    public Integer moveRemain;
    public Integer scanRemain;
    public Boolean isReady;
    public Boolean isYourTurn;
    public WebPlayer enermy;

    public WebPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out,
            AbstractShipFactory<Character> shipFactory, int s, int d, int b, int c, int mr, int sr, UUID id) {
        super(name, theBoard, inputReader, out, shipFactory, s, d, b, c);
        myId = id;
        this.moveRemain = mr;
        this.scanRemain = sr;
        this.submarineNum = s;
        this.destroyerNum = d;
        this.battleshipNum = b;
        this.carrierNum = c;
        this.isReady = false;
        this.isYourTurn = false;
        this.enermy = null;
    }

    @Override
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {

    }

    public void place(String shipName, int row, int column, char orientation) {
        Placement loc = new Placement(new Coordinate(row, column), orientation);
        Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
        Ship<Character> s = createFn.apply(loc);
        String error = theBoard.tryAddShip(s);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
    }

    public String fire(int row, int column) throws IOException {
        Coordinate fireCoord = new Coordinate(row, column);
        // You can fire at the same position again.
        Ship<Character> ship = theBoard.fireAt(fireCoord);
        return ship == null ? "" : ship.getName();
    }

    public int[] scan(int row, int column) throws IOException {
        int shipNum[] = { 0, 0, 0, 0 };
        Coordinate centerCoord = new Coordinate(row, column);
        for (int i = centerCoord.getRow() - 3; i <= centerCoord.getRow() + 3; i++) {
            for (int j = centerCoord.getColumn() - 3; j <= centerCoord.getColumn() + 3; j++) {
                if (j < 0 || j >= theBoard.getWidth() || i < 0 || i >= theBoard.getHeight()) {
                    continue;
                }
                if (Math.abs(i - centerCoord.getRow()) + Math.abs(j - centerCoord.getColumn()) <= 3) {
                    Ship<Character> ship = theBoard.whichShip(new Coordinate(i, j));
                    if (ship == null) {
                        continue;
                    }
                    if (ship.getName() == "Submarine") {
                        shipNum[0] += 1;
                    }
                    if (ship.getName() == "Destroyer") {
                        shipNum[1] += 1;
                    }
                    if (ship.getName() == "Battleship") {
                        shipNum[2] += 1;
                    }
                    if (ship.getName() == "Carrier") {
                        shipNum[3] += 1;
                    }
                }
            }
        }
        return shipNum;
    }

    public MoveRespEventData move(int oldRow, int oldColomn, int newRow, int newColomn, char newOrientation, int index)
            throws IOException {
        MoveRespEventData moveRespEventData = new MoveRespEventData();
        Coordinate c = new Coordinate(oldRow, oldColomn);
        Ship<Character> ship = theBoard.whichShip(c);
        Placement loc = new Placement(new Coordinate(newRow, newColomn), newOrientation);
        BiFunction<Placement, Ship<Character>, Ship<Character>> copyFn = shipCopyFns.get(ship.getName());
        Ship<Character> s = copyFn.apply(loc, ship);
        theBoard.rmShip(ship);
        String error = theBoard.tryAddShip(s);
        if (error != null) {
            theBoard.tryAddShip(ship);
            throw new IllegalArgumentException(error);
        }
        moveRemain -= 1;
        moveRespEventData.setMoveRemain(moveRemain);
        moveRespEventData.setIndex(index);
        for (Coordinate oldShipC : ship.getCoordinates()) {
            moveRespEventData.oldShipCoords.add(oldShipC);
        }
        for (Coordinate newShipC : s.getCoordinates()) {
            moveRespEventData.newShipCoords.add(newShipC);
        }
        for (Boolean h : s.getHits()) {
            moveRespEventData.newShipHits.add(h);
        }
        return moveRespEventData;
    }

    @Override
    public void doPlacementPhase() throws IOException {

    }

    @Override
    public void playOneTurn(BasicPlayer enemy) throws IOException {

    }

    @Override
    public boolean checkLost(String msg) {
        return theBoard.hasLost();
    }
}
