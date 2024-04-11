package edu.duke.yl954.battleship.vo;

import java.util.UUID;

public class LeaveRoomEventData extends BasicEventData {
    private UUID hostId;
    private UUID enermyId;

    public LeaveRoomEventData() {
    }

    public LeaveRoomEventData(UUID hostId, UUID enermyId) {
        this.hostId = hostId;
        this.enermyId = enermyId;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public UUID getEnermyId() {
        return enermyId;
    }

    public void setEnermyId(UUID enermyId) {
        this.enermyId = enermyId;
    }

}
