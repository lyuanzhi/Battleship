package edu.duke.yl954.battleship.vo;

public class ScanRespEventData {
    private int row;
    private int colomn;
    private int scanSN;
    private int scanDN;
    private int scanBN;
    private int scanCN;
    private int scanRemain;
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

    public int getScanSN() {
        return scanSN;
    }

    public void setScanSN(int scanSN) {
        this.scanSN = scanSN;
    }

    public int getScanDN() {
        return scanDN;
    }

    public void setScanDN(int scanDN) {
        this.scanDN = scanDN;
    }

    public int getScanBN() {
        return scanBN;
    }

    public void setScanBN(int scanBN) {
        this.scanBN = scanBN;
    }

    public int getScanCN() {
        return scanCN;
    }

    public void setScanCN(int scanCN) {
        this.scanCN = scanCN;
    }

    public int getScanRemain() {
        return scanRemain;
    }

    public void setScanRemain(int scanRemain) {
        this.scanRemain = scanRemain;
    }

    public Boolean getIsYourTurn() {
        return isYourTurn;
    }

    public void setIsYourTurn(Boolean isYourTurn) {
        this.isYourTurn = isYourTurn;
    }

}
