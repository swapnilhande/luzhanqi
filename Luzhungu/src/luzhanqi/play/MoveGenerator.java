package luzhanqi.play;

import luzhanqi.game.Constants;
import luzhanqi.game.Utils;
import luzhanqi.player.Player;

public class MoveGenerator {

    Player player;
    Move bestMove;

    public static final double MARGIN = 0.1;

    public MoveGenerator(Player player) {
        super();
        this.player = player;
    }

    /**
     * This method returns a move in alloted time.
     * 
     * @param start
     * @return returns array of integer of length 2. first is source index and
     *         second is target
     */
    public int[] getMoveInTime(long start) {
        // from, to
        int[] move = new int[2];
        int bestFrom = -1;
        int bestTo = -1;
        double bestValuation = Double.NEGATIVE_INFINITY;
        bestMove = new Move(bestFrom, bestTo, bestValuation);
        while (isTimeLeft(start)) {
            getBestMove(start);
            break;
        }

        // if no moves are left
        if (noMove())
            return null;

        move[0] = bestMove.getFromIndex();
        move[1] = bestMove.getToIndex();
        return move;
    }

    /**
     * This method evaluates all possible moves and updates bestMove with best
     * evaluation
     */
    public void getBestMove(long startTime) {
        double valuation = 0.0;
        for (int i = 0; i < player.getCanMove().length; i++) {
            if (player.getBoard()[i].getOwner() == player.getMyNumber()) {
                for (int direction = Constants.TOP; direction <= Constants.DOWNRIGHT; direction++) {
                    updateBestMoveInDirection(i, direction, valuation);
                }
                if (!isTimeLeft(startTime))
                    return;
            }
        }
        if (noMove())
            return;
        adjustThreeTurnParams(bestMove.getFromIndex(), bestMove.getToIndex());
    }

    /**
     * This method determines if time to determine is left or not
     * 
     * @param startTime
     * @return true if time left false otherwise
     */
    private boolean isTimeLeft(long startTime) {
        long currentTime = System.currentTimeMillis();
        return ((currentTime - startTime) < player.getTime());
    }

    /**
     * This method determines if the bestMove is updated after all possible
     * moves
     * 
     * @return true if bestMove is updated false otherwise
     */
    public boolean noMove() {
        return bestMove.getFromIndex() == -1;
    }

    /**
     * This method determines the possible moves in a direction
     * 
     * @param source
     * @param direction
     * @param valuation
     */
    public void updateBestMoveInDirection(int source, int direction,
            double valuation) {
        if (player.getCanMove()[source][direction]) {
            int target =
                    Utils.getNextPosition(direction, source);
            // Start - Railroad- Vivek
            if (Utils.moveOnRailPossible(source, target)) {
                updateBestMoveOnRail(source, direction, target);
            }
            else {
                updateBestMove(source, target);
            }
        }
    }

    /**
     * This method evaluates the moves along the rails
     * 
     * @param source
     * @param direction
     * @param target
     */
    private void updateBestMoveOnRail(int source, int direction, int target) {
        while (Utils.isOnRail(target)) {
            updateBestMove(source, target);
            if (player.getHisNumber() == player.getBoard()[target].getOwner()
                    || !Utils.isDirectionAvailable
                            (direction,
                                    target)) {
                break;
            }
            target =
                    Utils.getNextPosition(direction,
                            target);
            if (isNextOnRailValid(target)) {
                break;
            }
        }
    }

    /**
     * This method evaluates a move against bestMove
     * 
     * @param source
     * @param target
     */
    private void updateBestMove(int source, int target) {
        double valuation;
        valuation = evaluateMove(source,
                target);
        if (isBetterValuation(valuation,
                bestMove.getEvalution(),
                source,
                target)) {
            bestMove.setFromIndex(source);
            bestMove.setToIndex(target);
            bestMove.setEvalution(valuation);
        }
    }

    /**
     * This method update the three turn parameters for the move
     * 
     * @param from
     *            : from position
     * @param to
     *            : to position
     */
    private void adjustThreeTurnParams(int from, int to) {
        if (player.getThreeTurnRuleHigh() == Math.max(from, to)
                && player.getThreeTurnRuleLow() == Math.min(from, to)) {
            player.setThreeTurnRuleTurns(player.getThreeTurnRuleTurns() + 1);
        } else {
            player.setThreeTurnRuleHigh(Math.max(from, to));
            player.setThreeTurnRuleLow(Math.min(from, to));
            player.setThreeTurnRuleTurns(1);
        }
    }

    private boolean isNextOnRailValid(int pos)
    {
        return (-1 == pos
        || player.getBoard()[pos].getOwner() == player.getMyNumber());

    }

    /**
     * Check is current evaluation is better than earlier valuations
     * 
     * @param valuation
     * @param bestValuation
     * @param threeTurnRuleHighValue
     * @param threeTurnRuleLowValue
     * @return
     */
    public boolean isBetterValuation(double valuation, double bestValuation,
            int threeTurnRuleHighValue, int threeTurnRuleLowValue) {
        return (valuation > bestValuation
        && !(player.getThreeTurnRuleTurns() >= 3
                && player.getThreeTurnRuleHigh() == threeTurnRuleHighValue
                && player.getThreeTurnRuleLow() == threeTurnRuleLowValue));
    }

    /**
     * Evaluate the move, add weights accordingly and then see which move is
     * the best
     * 
     * @param from
     * @param to
     * @return
     */
    public double evaluateMove(int from, int to) {

        double valuation = 0;
        // Add a penalty if your piece has moved (gives away that it is not a
        // bomb)
        int myPiece = player.getBoard()[from].getPiece();
        int hisPiece = player.getBoard()[to].getPiece();

        // 1. Pieces that have not moved should get chance to move.
        if (!player.getHasMoved()[from]) {
            // TODO Increase this value when there are fewer pieces left that
            // have not moved yet
            valuation += player.getPieceParams()[myPiece][Constants.FIRST_MOVE];
        }

        // 2. Dare to take down unknown opponent
        if (hisPiece == Constants.PIECE_UNKNOWN && !player.getHasMoved()[to]) {
            // TODO Incorporate the chance that you can beat this opponent
            valuation += player.getPieceParams()[myPiece][Constants.STILL_OPPONENT];
        }

        // 3. Take down lower ranking opponent
        // Add a penalty/bonus if there's a piece at our target we can beat
        if (hisPiece > Constants.PIECE_UNKNOWN && myPiece > hisPiece) {
            // The parameter weighs in more when the opponent's piece is
            // relatively weak and ours relatively strong
            int totalValue = (hisPiece + myPiece);
            valuation += totalValue
                    * player.getPieceParams()[myPiece][Constants.BEAT_OPPONENT];
        }

        // 4. Take down enemy flag
        // CAPTURE_FLAG
        if (Constants.PIECE_FLAG == hisPiece) {
            valuation += player.getPieceParams()[myPiece][Constants.CAPTURE_FLAG];
        }

        // 5. Check how close are we from enemy flag
        // APPROACH_ENEMY_FLAG
        if (player.getBoard()[58].getPiece() == Constants.PIECE_FLAG) {
            int distance = Utils.getDistanceBetweenPositions(to, 58);
            valuation += (15 - distance)
                    * player.getPieceParams()[myPiece][Constants.APPROACH_ENEMY_FLAG];
        } else if (player.getBoard()[56].getPiece() == Constants.PIECE_FLAG) {
            int distance = Utils.getDistanceBetweenPositions(to, 56);
            valuation += (15 - distance)
                    * player.getPieceParams()[myPiece][Constants.APPROACH_ENEMY_FLAG];
        } else {
            int flagPosition1 = Utils.getDistanceBetweenPositions(to, 58);
            int flagPosition2 = Utils.getDistanceBetweenPositions(to, 56);
            valuation += (15 - Math.min(flagPosition1, flagPosition2))
                    * player.getPieceParams()[myPiece][Constants.APPROACH_ENEMY_FLAG];
        }

        // 6. Check how close are we from enemy safe zones
        // Give more weights if it is an enemy safe position
        // more weights as it gets closer to headquarters
        // APPROACH_ENEMY_SAFE_ZONES
        if (Utils.isEnemyCampPosition(to)) {
            if (player.getBoard()[57].getPiece() == Constants.PIECE_FLAG) {
                int distance = Utils.getDistanceBetweenPositions(to, 57);
                valuation += (15 - distance)
                        * player.getPieceParams()[myPiece][Constants.APPROACH_ENEMY_SAFE_ZONES];
            } else if (player.getBoard()[55].getPiece() == Constants.PIECE_FLAG) {
                int distance = Utils.getDistanceBetweenPositions(to, 55);
                valuation += (15 - distance)
                        * player.getPieceParams()[myPiece][Constants.APPROACH_ENEMY_SAFE_ZONES];
            } else {
                int flagPosition1 = Utils.getDistanceBetweenPositions(to, 57);
                int flagPosition2 = Utils.getDistanceBetweenPositions(to, 55);
                valuation += (15 - Math.min(flagPosition1, flagPosition2))
                        * player.getPieceParams()[myPiece][Constants.APPROACH_ENEMY_SAFE_ZONES];
            }
        }

        // 7. Check how close are we to our safe zones
        // APPROACH_OUR_SAFE_ZONES
        if (Utils.isOurCampPosition(to)) {
            valuation += player.getPieceParams()[myPiece][Constants.APPROACH_OUR_SAFE_ZONES];
        }

        // 8. Our engineer takes out the land mines
        // DEFUSE_MINE
        if (player.getBoard()[from].getPiece() == Constants.PIECE_ENGINEER
                &&
                !player.getHasMoved()[to]
                && player.getBoard()[to].getPiece() == Constants.PIECE_UNKNOWN
                && to > 49) {
            valuation += player.getPieceParams()[myPiece][Constants.DEFUSE_MINE];
        }

        // 9. How close are we to rail roads
        // TODO More effective for engineer

        // 10. Is enemy near our flag position?
        // PROTECT_BASE
        for (int i = 0; i < 15; i++) {
            if (player.getBoard()[i].getOwner() == player.getHisNumber()) {
                if (to < 15) {
                    // give more weights to pieces that are going to defend
                    valuation += player.getPieceParams()[myPiece][Constants.PROTECT_BASE];
                }
                if (to == i
                        && player.getBoard()[from].getPiece() > player
                                .getBoard()[i].getPiece()) {
                    // give more weights if our piece can kill intruder
                    // KILL_INTRUDER
                    valuation += player.getPieceParams()[myPiece][Constants.KILL_INTRUDER];
                }
            }
        }

        // 11. If going against mines, least rank player should get more
        // weight
        // CHEAP_PATRIOT
        if (!player.getHasMoved()[to]
                && player.getBoard()[to].getPiece() == Constants.PIECE_UNKNOWN
                && to > 49) {
            valuation += (Constants.PIECE_FIELDMARSHAL - player.getBoard()[from]
                    .getPiece())
                    * player.getPieceParams()[myPiece][Constants.CHEAP_PATRIOT];
        }

        // 12. Lower rank player should capture headquarters if position is
        // unknown
        // BRAVE_PATRIOT
        if (Utils.isEnemyHeadquarterPosition(to)) {
            valuation += (Constants.PIECE_FIELDMARSHAL - myPiece)
                    * player.getPieceParams()[myPiece][Constants.BRAVE_PATRIOT];
        }

        // If we can attack an unknown piece that has moved,
        // see if our piece is brave (read: weak) enough to do that
        // TODO Incorporate whether we're a miner
        if (player.getHasMoved()[to] && hisPiece == Constants.PIECE_UNKNOWN) {
            valuation += player.getPieceParams()[myPiece][Constants.EXPLORATION_RATE];
        }

        int topPosition = Utils.getTopOfPosition(from);
        int topRightPosition = Utils.getTopRightOfPosition(from);
        int topLeftPosition = Utils.getTopLeftOfPosition(from);
        int bottomPosition = Utils.getBottomOfPosition(from);
        int bottomLeftPosition = Utils.getBottomLeftOfPosition(from);
        int bottomRightPosition = Utils.getBottomRightOfPosition(from);
        int rightPosition = Utils.getRightOfPosition(from);
        int leftPosition = Utils.getLeftOfPosition(from);

        // If we can move to the opponent's side
        if (to == bottomPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_FORWARD];
        }

        // If we can move to the right
        if (to == rightPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_RIGHT];
        }

        // If we can move to our own side
        if (to == topPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_BACKWARD];
        }

        // If we can move to the left
        if (to == leftPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_LEFT];
        }

        // Top left
        if (to == bottomRightPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_TOP_LEFT];
        }

        // Top right
        if (to == bottomLeftPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_TOP_RIGHT];
        }

        // Bottom left
        if (to == topRightPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_BOTTOM_LEFT];
        }

        // Bottom right
        if (to == topLeftPosition) {
            valuation += player.getPieceParams()[myPiece][Constants.MOVE_BOTTOM_RIGHT];
        }

        // TODO: Check, adding for rail moves
        int fromXPos = Utils.getXPosition(from);
        int toXPos = Utils.getXPosition(to);
        int fromYPos = Utils.getYPosition(from);
        int toYPos = Utils.getYPosition(to);

        if (toXPos < fromXPos - 1) {
            valuation += player.getPieceParams()[myPiece][Constants.SLIDE_LEFT];
        }

        if (toXPos > fromXPos + 1) {
            valuation += player.getPieceParams()[myPiece][Constants.SLIDE_RIGHT];
        }

        if (toYPos < fromYPos - 1) {
            valuation += player.getPieceParams()[myPiece][Constants.SLIDE_TOP];
        }

        if (toYPos > fromYPos + 1) {
            valuation += player.getPieceParams()[myPiece][Constants.SLIDE_DOWN];
        }

        // Add the random influence
        valuation += Math.random() * Constants.RANDOM_INFLUENCE;
        return valuation;
    }
}
