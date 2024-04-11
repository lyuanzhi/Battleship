package edu.duke.yl954.battleship.vo;

public class FireRespEventData {
    private int row;
    private int colomn;
    private String shipName;
    private Boolean isYourTurn;

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

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Boolean getIsYourTurn() {
        return isYourTurn;
    }

    public void setIsYourTurn(Boolean isYourTurn) {
        this.isYourTurn = isYourTurn;
    }

}
