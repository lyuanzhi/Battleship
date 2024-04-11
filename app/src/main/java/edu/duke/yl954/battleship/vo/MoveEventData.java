package edu.duke.yl954.battleship.vo;

import java.util.UUID;

public class MoveEventData {
    private UUID hostId;
    private UUID myId;
    private int oldRow;
    private int oldColomn;
    private int newRow;
    private int newColomn;
    private char newOrientation;
    private int index;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public UUID getMyId() {
        return myId;
    }

    public void setMyId(UUID myId) {
        this.myId = myId;
    }

    public int getOldRow() {
        return oldRow;
    }

    public void setOldRow(int oldRow) {
        this.oldRow = oldRow;
    }

    public int getOldColomn() {
        return oldColomn;
    }

    public void setOldColomn(int oldColomn) {
        this.oldColomn = oldColomn;
    }

    public int getNewRow() {
        return newRow;
    }

    public void setNewRow(int newRow) {
        this.newRow = newRow;
    }

    public int getNewColomn() {
        return newColomn;
    }

    public void setNewColomn(int newColomn) {
        this.newColomn = newColomn;
    }

    public char getNewOrientation() {
        return newOrientation;
    }

    public void setNewOrientation(char newOrientation) {
        this.newOrientation = newOrientation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
