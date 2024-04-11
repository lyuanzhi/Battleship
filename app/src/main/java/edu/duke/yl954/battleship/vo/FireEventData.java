package edu.duke.yl954.battleship.vo;

import java.util.UUID;

public class FireEventData {
    private UUID hostId;
    private UUID myId;
    private int row;
    private int colomn;

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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColomn() {
        return colomn;
    }

    public void setColomn(int colomn) {
        this.colomn = colomn;
    }
}
