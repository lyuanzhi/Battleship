package edu.duke.yl954.battleship;

/**
 * A specific rule checker that verifies whether a ship's placement falls within
 * the bounds of the game board.
 */
public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

    /**
     * Constructs an InBoundsRuleChecker with a reference to the next rule checker
     * in the chain.
     *
     * @param next The next PlacementRuleChecker in the chain.
     */
    public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * Checks if the ship's placement is within the boundaries of the board. This
     * method iterates over all coordinates occupied by the ship and verifies that
     * each one is within the valid range defined by the board's dimensions.
     *
     * @param theShip  The ship whose placement is being checked.
     * @param theBoard The board on which the ship is being placed.
     * @return the error message.
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        for (Coordinate c : theShip.getCoordinates()) {
            if (c.getRow() < 0) {
                return "That placement is invalid: the ship goes off the top of the board.";
            }
            if (c.getRow() >= theBoard.getHeight()) {
                return "That placement is invalid: the ship goes off the bottom of the board.";
            }
            if (c.getColumn() < 0) {
                return "That placement is invalid: the ship goes off the left of the board.";
            }
            if (c.getColumn() >= theBoard.getWidth()) {
                return "That placement is invalid: the ship goes off the right of the board.";
            }
        }
        return null;
    }

}
