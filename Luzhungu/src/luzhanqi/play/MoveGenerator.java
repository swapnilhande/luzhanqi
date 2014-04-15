package luzhanqi.play;

import java.util.ArrayList;
import java.util.List;

import luzhanqi.game.Constants;
import luzhanqi.game.Utils;
import luzhanqi.player.Player;

public class MoveGenerator {

    /**
     * Player whose move is to be calculated
     */
    Player player;
    /**
     * Best move that should be made given the board setup
     */
    Move bestMove;

    public static final double MARGIN = 0.1;

    /**
     * Initialize move generator for the given player
     * 
     * @param player
     */
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
            int target = Utils.getNextPosition(direction, source);
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
     * This method determines if the piece is engineer or not
     * 
     * @param index
     *            : index of piece on the board
     * @return true if the piece is engineer otherwise false
     */
    private boolean isPieceEngineer(int index) {
        return (player.getBoard()[index].getPiece()
                == Constants.PIECE_ENGINEER);
    }

    /**
     * This method evaluates the moves along the rails
     * 
     * @param source
     * @param direction
     * @param target
     */
    private void updateBestMoveOnRail(int source, int direction, int target) {
        if (isPieceEngineer(source)) {
            List<Integer> coveredIndices = new ArrayList<Integer>();
            updateBestMoveOnRailForEngineer(source,
                    direction, target, coveredIndices);
        }
        while (Utils.isOnRail(target)) {
            updateBestMove(source, target);
            if (player.getHisNumber() == player.getBoard()[target].getOwner()) {
                break;
            }

            if (!Utils.isDirectionAvailable(direction, target)) {
                break;
            }
            target = Utils.getNextPosition(direction, target);
            if (isNextOnRailInValid(target)) {
                break;
            }
        }
    }

    private void updateBestMoveOnRailForEngineer(int source
            , int direction, int target, List<Integer> coveredIndices) {
        while (Utils.isOnRail(target)) {
            System.out.println("Source: "
                    + Utils.getPositionNameFromIndex(source) + " Target: "
                    + Utils.getPositionNameFromIndex(target));
            if (coveredIndices.contains(target)) {
                break;
            }
            updateBestMove(source, target);
            coveredIndices.add(target);
            // check for the target if the target is
            // opponent player; if yes, break
            if (player.getHisNumber()
                == player.getBoard()[target].getOwner()) {
                break;
            }

            if (!Utils.isDirectionAvailable (direction, target)) {
                break;
            }
            target = Utils.getNextPosition(direction, target);
            if (isNextOnRailInValid(target)) {
                break;
            }
            // check for the direction
            // if not available
            // update move for rail in
            // every other direction
            if (Utils.isTargetAtIntersection(target)) {
                for (int i = 0; i < 3; i++) {
                    if (i != 1) {
                        direction = direction + 2;
                        if (Utils.isDirectionAvailable(direction, target)) {
                            updateBestMoveOnRailForEngineer(source,
                                    direction, target, coveredIndices);
                        }
                    }
                }

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
        valuation = evaluateMove(source, target);
        if (isBetterValuation(valuation, bestMove.getEvalution(), source,
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

    /**
     * Returns true if next position on rail is -1 or if it is occupied by our
     * player, else false
     */
    private boolean isNextOnRailInValid(int pos)
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
        int fromXPos = Utils.getXPosition(from);
        int toXPos = Utils.getXPosition(to);
        int fromYPos = Utils.getYPosition(from);
        int toYPos = Utils.getYPosition(to);

        // No point attacking our flag position
        if (to == 3 || to == 1) {
            return valuation;
        }
        int myPiece = player.getBoard()[from].getPiece();
        int hisPiece = player.getBoard()[to].getPiece();

        // Genocide General: Kill enemy front line
        if (myPiece == Constants.PIECE_GENERAL && fromYPos == 7
                && toYPos == 7 && hisPiece != Constants.PIECE_EMPTY
                && Player.genocideKillCount < 4) {
            valuation *= Constants.PIECE_GENERAL * player.getPieceParams()
                    [myPiece][Constants.ATTACK_UNKNOWN_OPPONENT];
            Player.genocideKillCount++;
        }

        // 2. Dare to take down unknown opponent
        // The condition when we are going in the last two rows, check if the
        // piece has moved, more chances that its not a bomb
        if (hisPiece == Constants.PIECE_UNKNOWN && player.getHasMoved()[to]
                && to > 49) {
            // TODO Incorporate the chance that you can beat this opponent
            if (myPiece > Constants.PIECE_MAJOR
                    || myPiece == Constants.PIECE_BOMB) {
                valuation += player.getPieceParams()
                        [myPiece][Constants.ATTACK_UNKNOWN_OPPONENT];
            }
        }

        // Try beating opponent in other cases.
        if (hisPiece == Constants.PIECE_UNKNOWN && to < 50) {
            valuation += player.getPieceParams()
                    [myPiece][Constants.ATTACK_UNKNOWN_OPPONENT];
        }

        // 3. Take down lower ranking opponent
        // Add a penalty/bonus if there's a piece at our target we can beat
        if ((hisPiece > Constants.PIECE_UNKNOWN && myPiece > hisPiece)
                || myPiece == Constants.PIECE_BOMB) {
            valuation += player.getPieceParams()[myPiece]
                    [Constants.BEAT_OPPONENT];
        }

        // 4. Take down enemy flag
        // CAPTURE_FLAG
        if (Constants.PIECE_FLAG == hisPiece) {
            valuation += player.getPieceParams()[myPiece]
                    [Constants.CAPTURE_FLAG];
        }

        // 5. Check how close are we from enemy flag
        // APPROACH_ENEMY_FLAG
        if (player.getBoard()[58].getPiece() == Constants.PIECE_FLAG) {
            int distance = Utils.getDistanceBetweenPositions(to, 58);
            valuation += (15 - distance)
                    * player.getPieceParams()[myPiece]
                    [Constants.APPROACH_ENEMY_FLAG];
        } else if (player.getBoard()[56].getPiece() == Constants.PIECE_FLAG) {
            int distance = Utils.getDistanceBetweenPositions(to, 56);
            valuation += (15 - distance)
                    * player.getPieceParams()[myPiece]
                    [Constants.APPROACH_ENEMY_FLAG];
        } else {
            int flagPosition1 = Utils.getDistanceBetweenPositions(to, 58);
            int flagPosition2 = Utils.getDistanceBetweenPositions(to, 56);
            valuation += (15 - Math.min(flagPosition1, flagPosition2))
                    * player.getPieceParams()[myPiece]
                    [Constants.APPROACH_ENEMY_FLAG];
        }

        // 6. Check how close are we from enemy safe zones
        // Give more weights if it is an enemy safe position
        // more weights as it gets closer to headquarters
        // APPROACH_ENEMY_SAFE_ZONES
        if (Utils.isEnemyCampPosition(to)) {
            if (player.getBoard()[57].getPiece() == Constants.PIECE_FLAG) {
                int distance = Utils.getDistanceBetweenPositions(to, 57);
                valuation += (15 - distance)
                        * player.getPieceParams()[myPiece]
                        [Constants.APPROACH_ENEMY_SAFE_ZONES];
            } else if (player.getBoard()[55].getPiece()
                == Constants.PIECE_FLAG) {
                int distance = Utils.getDistanceBetweenPositions(to, 55);
                valuation += (15 - distance)
                        * player.getPieceParams()[myPiece]
                        [Constants.APPROACH_ENEMY_SAFE_ZONES];
            } else {
                int flagPosition1 = Utils.getDistanceBetweenPositions(to, 57);
                int flagPosition2 = Utils.getDistanceBetweenPositions(to, 55);
                valuation += (15 - Math.min(flagPosition1, flagPosition2))
                        * player.getPieceParams()[myPiece]
                        [Constants.APPROACH_ENEMY_SAFE_ZONES];
            }
        }

        // 7. Check how close are we to our safe zones
        // APPROACH_OUR_SAFE_ZONES
        if (Utils.isOurCampPosition(to) && myPiece != Constants.PIECE_BOMB) {
            valuation += ((15 - Utils.getDistanceBetweenPositions(to, 1)) + 
                    myPiece + player.getPieceParams()[myPiece]
                    [Constants.APPROACH_OUR_SAFE_ZONES]);
            /*
             * if (myPiece == Constants.PIECE_FIELDMARSHAL) { valuation +=
             * player.getPieceParams()[myPiece]
             * [Constants.APPROACH_OUR_SAFE_ZONES]; }
             */
        }

        // 8. Our engineer takes out the land mines
        // DEFUSE_MINE
        if (player.getBoard()[from].getPiece() == Constants.PIECE_ENGINEER
                &&
                !player.getHasMoved()[to]
                && player.getBoard()[to].getPiece() == Constants.PIECE_UNKNOWN
                && to > 49) {
            valuation += player.getPieceParams()[myPiece]
                    [Constants.DEFUSE_MINE];
        }

        // 9. How close are we to rail roads
        // TODO More effective for engineer

        // 10. Is enemy near our flag position?
        // PROTECT_BASE
        for (int i = 0; i < 15; i++) {
            if (player.getBoard()[i].getOwner() == player.getHisNumber()) {
                if (to < 15) {
                    // give more weights to pieces that are going to defend
                    valuation += (15 - Utils.getDistanceBetweenPositions(to,
                            1))
                            + myPiece
                            * player.getPieceParams()[myPiece]
                            [Constants.PROTECT_BASE];
                }
                if (to == i
                        && (player.getBoard()[from].getPiece() > player
                                .getBoard()[i].getPiece()
                        || myPiece == Constants.PIECE_BOMB)) {
                    // give more weights if our piece can kill intruder
                    // KILL_INTRUDER
                    valuation += player.getPieceParams()[myPiece]
                            [Constants.KILL_INTRUDER];
                }
            }
        }

        /*
         * // 11. If going against mines, least rank player should get more //
         * weight // CHEAP_PATRIOT if (!player.getHasMoved()[to] &&
         * player.getBoard()[to].getPiece() == Constants.PIECE_UNKNOWN && to >
         * 49) { valuation += (Constants.PIECE_FIELDMARSHAL -
         * player.getBoard()[from] .getPiece())
         * player.getPieceParams()[myPiece][Constants.CHEAP_PATRIOT]; }
         */

        // 12. Lower rank player should capture headquarters if position is
        // unknown
        // BRAVE_PATRIOT
        if (Utils.isEnemyHeadquarterPosition(to)) {
            valuation += (Constants.PIECE_FIELDMARSHAL - myPiece)
                    * player.getPieceParams()[myPiece]
                    [Constants.BRAVE_PATRIOT];
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
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_FORWARD];
        }

        // If we can move to the right
        if (to == rightPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_RIGHT];
        }

        // If we can move to our own side
        if (to == topPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_BACKWARD];
        }

        // If we can move to the left
        if (to == leftPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_LEFT];
        }

        // Top left
        if (to == bottomRightPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_TOP_LEFT];
        }

        // Top right
        if (to == bottomLeftPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_TOP_RIGHT];
        }

        // Bottom left
        if (to == topRightPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_BOTTOM_LEFT];
        }

        // Bottom right
        if (to == topLeftPosition) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.MOVE_BOTTOM_RIGHT];
        }

        // TODO: Check, adding for rail moves

        if (toXPos < fromXPos - 1) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.SLIDE_LEFT];
        }

        if (toXPos > fromXPos + 1) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.SLIDE_RIGHT];
        }

        if (toYPos < fromYPos - 1) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.SLIDE_TOP];
        }

        if (toYPos > fromYPos + 1) {
            valuation *= player.getPieceParams()[myPiece]
                    [Constants.SLIDE_DOWN];
        }

        // No Suicides
        if (myPiece < hisPiece) {
            valuation = 0;
        }

        if (myPiece == Constants.PIECE_ENGINEER && to < 50) {
            valuation = 0;
        }

        // If we are in enemy safe zone, do not move back to non attacking
        // position
        if (Utils.isEnemyCampPosition(from) && toYPos < fromYPos) {
            valuation = 0;
        }
        // Add the random influence
        // valuation += Math.random() * Constants.RANDOM_INFLUENCE;
        return valuation;
    }
}
