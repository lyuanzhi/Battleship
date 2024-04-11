package edu.duke.yl954.battleship.vo;

import java.util.ArrayList;

import edu.duke.yl954.battleship.Coordinate;

public class MoveRespEventData {
    private Boolean isYourTurn;
    private int moveRemain;
    private int index;
    public ArrayList<Coordinate> oldShipCoords;
    public ArrayList<Coordinate> newShipCoords;
    public ArrayList<Boolean> newShipHits;

    public MoveRespEventData() {
        this.oldShipCoords = new ArrayList<>();
        this.newShipCoords = new ArrayList<>();
        this.newShipHits = new ArrayList<>();
    }

    public Boolean getIsYourTurn() {
        return isYourTurn;
    }

    public void setIsYourTurn(Boolean isYourTurn) {
        this.isYourTurn = isYourTurn;
    }

    public int getMoveRemain() {
        return moveRemain;
    }

    public void setMoveRemain(int moveRemain) {
        this.moveRemain = moveRemain;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
