package edu.duke.yl954.battleship;

/**
 * This abstract class forms the basis for a chain of responsibility pattern to
 * check ship placement rules on the board.
 */
public abstract class PlacementRuleChecker<T> {
    /**
     * The next rule checker in the chain.
     */
    private final PlacementRuleChecker<T> next;

    /**
     * Constructs a PlacementRuleChecker with the next checker in the chain.
     *
     * @param next The next PlacementRuleChecker in the chain, or null if this is
     *             the last checker.
     */
    public PlacementRuleChecker(PlacementRuleChecker<T> next) {
        this.next = next;
    }

    /**
     * Checks the specific rule implemented by this checker against the given ship
     * and board.
     *
     * @param theShip  The ship whose placement is being checked.
     * @param theBoard The board on which the ship is being placed.
     * @return the error message.
     */
    protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

    /**
     * Initiates the placement checking process for the given ship and board.
     *
     * @param theShip  The ship whose placement is being checked.
     * @param theBoard The board on which the ship is being placed.
     * @return the error message.
     */
    public String checkPlacement(Ship<T> theShip, Board<T> theBoard) {
        // if we fail our own rule: stop the placement is not legal
        if (checkMyRule(theShip, theBoard) != null) {
            return checkMyRule(theShip, theBoard);
        }
        // other wise, ask the rest of the chain.
        if (next != null) {
            return next.checkPlacement(theShip, theBoard);
        }
        // if there are no more rules, then the placement is legal
        return null;
    }
}
