package edu.duke.yl954.battleship;

/**
 * A rule checker that ensures a ship's placement does not collide with any
 * other existing ships on the board.
 */
public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {

    /**
     * Constructs a NoCollisionRuleChecker with a reference to the next rule checker
     * in the chain.
     *
     * @param next The next PlacementRuleChecker in the chain.
     */
    public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * Checks whether the specified ship's placement would result in a collision
     * with any existing ships on the board. A collision occurs if any part of the
     * ship would occupy a board position that is already taken by another ship.
     *
     * @param theShip  The ship whose placement is being checked.
     * @param theBoard The board on which the ship is being placed.
     * @return the error message.
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        for (Coordinate c : theShip.getCoordinates()) {
            if (theBoard.whatIsAtForSelf(c) != null) {
                return "That placement is invalid: the ship overlaps another ship.";
            }
        }
        return null;
    }

}
