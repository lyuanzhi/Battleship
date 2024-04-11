package edu.duke.yl954.battleship.vo;

import java.util.UUID;

public class BasicEventData {
    private UUID myId;

    public BasicEventData() {
    }

    public BasicEventData(UUID myId) {
        this.myId = myId;
    }

    public UUID getMyId() {
        return myId;
    }

    public void setMyId(UUID myId) {
        this.myId = myId;
    }
}
