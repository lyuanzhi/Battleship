package edu.duke.yl954.battleship.vo;

import java.util.UUID;

public class CreateRoomEventData extends BasicEventData {
    private int width;
    private int height;
    private String playerName;
    private int submarineNum;
    private int destroyerNum;
    private int battleshipNum;
    private int carrierNum;
    private int moveRemain;
    private int scanRemain;

    public CreateRoomEventData() {
    }

    public CreateRoomEventData(int width, int height, String playerName, int submarineNum, int destroyerNum,
            int battleshipNum, int carrierNum, int moveRemain, int scanRemain, UUID myId) {
        super(myId);
        this.width = width;
        this.height = height;
        this.playerName = playerName;
        this.submarineNum = submarineNum;
        this.destroyerNum = destroyerNum;
        this.battleshipNum = battleshipNum;
        this.carrierNum = carrierNum;
        this.moveRemain = moveRemain;
        this.scanRemain = scanRemain;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getSubmarineNum() {
        return submarineNum;
    }

    public void setSubmarineNum(int submarineNum) {
        this.submarineNum = submarineNum;
    }

    public int getDestroyerNum() {
        return destroyerNum;
    }

    public void setDestroyerNum(int destroyerNum) {
        this.destroyerNum = destroyerNum;
    }

    public int getBattleshipNum() {
        return battleshipNum;
    }

    public void setBattleshipNum(int battleshipNum) {
        this.battleshipNum = battleshipNum;
    }

    public int getCarrierNum() {
        return carrierNum;
    }

    public void setCarrierNum(int carrierNum) {
        this.carrierNum = carrierNum;
    }

    public int getMoveRemain() {
        return moveRemain;
    }

    public void setMoveRemain(int moveRemain) {
        this.moveRemain = moveRemain;
    }

    public int getScanRemain() {
        return scanRemain;
    }

    public void setScanRemain(int scanRemain) {
        this.scanRemain = scanRemain;
    }
}
