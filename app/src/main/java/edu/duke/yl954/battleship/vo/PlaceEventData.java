package edu.duke.yl954.battleship.vo;

import java.util.UUID;

public class PlaceEventData {
    private UUID hostId;
    private UUID myId;
    private String shipName;
    private int row;
    private int colomn;
    private char orientation;
    private int index;

    public UUID getHostId() {
        return hostId;
    }

    public UUID getMyId() {
        return myId;
    }

    public String getShipName() {
        return shipName;
    }

    public int getRow() {
        return row;
    }

    public int getColomn() {
        return colomn;
    }

    public char getOrientation() {
        return orientation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
