package luzhanqi.play;

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

    /**
     * Margin to keep before the time runs out.
     */
    public static final double MARGIN = 0.1;

    /**
     * Initialize move generator for the given player
     * @param player
     */
    public MoveGenerator(Player player) {
        super();
        this.player = player;
    }

    /**
     * This method returns a move in alloted time.
     * 
     * @param start Start time to get move
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
        if (noMove()) {
            return null;
        }
        move[0] = bestMove.getFromIndex();
        move[1] = bestMove.getToIndex();
        return move;
    }

    /**
     * This method evaluates all possible moves and updates bestMove with best
     * evaluation, within the given time.
     */
    public void getBestMove(long startTime) {
        double valuation = 0.0;
        for (int i = 0; i < player.getCanMove().length; i++) {
            if (player.getBoard()[i].getOwner() == player.getMyNumber()) {
                for (int direction = Constants.TOP; 
                        direction <= Constants.DOWNRIGHT; direction++) {
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
     * @return true if bestMove is updated false otherwise
     */
    public boolean noMove() {
        return bestMove.getFromIndex() == -1;
    }

    /**
     * This method determines the possible moves in a direction
     * @param source
     * @param direction
     * @param valuation
     */
    public void updateBestMoveInDirection(int source, int direction,
            double valuation) {
        if (player.getCanMove()[source][direction]) {
            int target = Utils.getNextPosition(direction, source);
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
     * @param index index of piece on the board
     * @return true if the piece is engineer otherwise false
     */
    private boolean isPieceEngineer(int index) {
        return (player.getBoard()[index].getPiece()
                == Constants.PIECE_ENGINEER);
    }

    /**
     * This method evaluates the moves along the rails
     * @param source
     * @param direction
     * @param target
     */
    private void updateBestMoveOnRail(int source, int direction, int target) {
        if (isPieceEngineer(source)) {
            boolean[][] isDirectionCovered = new boolean[60][8];
            isDirectionCovered[source][direction] = true;
            updateBestMoveOnRailForEngineer(source,
                    direction, target, isDirectionCovered);
        } else {
            while (Utils.isOnRail(target)) {
                updateBestMove(source, target);
                if (player.getHisNumber() == player.getBoard()[target]
                        .getOwner()) {
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
    }

    /**
     * Updates the best possible move for an Engineer based on the given source
     * direction, target and covered directions
     * @param source
     * @param direction
     * @param target
     * @param isDirectionCovered
     */
    private void updateBestMoveOnRailForEngineer(int source
            , int direction, int target, boolean[][] isDirectionCovered) {
        while (Utils.isOnRail(target)) {
            if (isDirectionCovered[target][direction]) {
                break;
            }

            updateBestMove(source, target);
            isDirectionCovered[target][direction] = true;
            // check for the target if the target is
            // opponent player; if yes, break
            if (player.getHisNumber()
                == player.getBoard()[target].getOwner()) {
                break;
            }

            // Special cases: intersections
            if (Utils.isTargetAtIntersection(target)) {
                for (int i = 0; i < 4; i++) {
                    int newDirection = i;
                    if(!Utils.isDirectionAvailable(newDirection, target)){
                        continue;
                    }
                    int newTarget = Utils.getNextPosition(newDirection,
                            target);
                    if (isNextOnRailInValid(newTarget)) {
                        continue;
                    }
                    if (Utils.isDirectionAvailable(newDirection, newTarget)) {
                        updateBestMoveOnRailForEngineer(source,
                                newDirection, newTarget, isDirectionCovered);
                    }
                }
            } else {
                if (!Utils.isDirectionAvailable(direction, target)) {
                    break;
                }
                target = Utils.getNextPosition(direction, target);
                if (isNextOnRailInValid(target)) {
                    break;
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
     * @param from from position
     * @param to to position
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
     * @param pos
     * @return
     */
    private boolean isNextOnRailInValid(int pos)
    {
        return (-1 == pos
        || player.getBoard()[pos].getOwner() == player.getMyNumber());

    }

    /**
     * Check is current evaluation is better than earlier valuations
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
     * @return valuation
     */
    public double evaluateMove(int from, int to) {

        double valuation = 0;
        double[] pieceWeights = player.getPieceParams();
        int fromYPos = Utils.getYPosition(from);
        int toYPos = Utils.getYPosition(to);
        int myPiece = player.getBoard()[from].getPiece();
        int hisPiece = player.getBoard()[to].getPiece();

        // SPARTA ABYSS: Do not move to our head quarter position
        if (to == 3 || to == 1) {
            return valuation;
        }

        // NO SUICIDES: Do not attack the piece we know is of higher rank
        if (myPiece < hisPiece && myPiece != Constants.PIECE_BOMB) {
            return valuation;
        }

        // SMART ENGINEER: Do not move engineer in places that are not near
        // to enemy headquarters
        if (myPiece == Constants.PIECE_ENGINEER && to < 50) {
            return valuation;
        }

        // DONT LOOK BACK: If we are in enemy safe zone, do not move back to 
        // non attacking position in empty position
        if (Utils.isEnemyCampPosition(from) && toYPos < fromYPos
                /*&& hisPiece == Constants.PIECE_EMPTY*/) {
            return valuation;
        }

        // DANGEROUS GRENADES: Do not move grenades to our camp position
        if (myPiece == Constants.PIECE_BOMB && Utils.isOurCampPosition(to)) {
            return valuation;
        }

        // MEDAL OF HONOR: Capture the enemy flag if you get the chance
        if (player.getBoard()[56].getPiece() == Constants.PIECE_FLAG
                && to == 56) {
            return Double.POSITIVE_INFINITY;
        } else if (player.getBoard()[58].getPiece() == Constants.PIECE_FLAG
                && to == 58) {
            return Double.POSITIVE_INFINITY;
        } else if (to == 56 || to == 58) {
            return Double.POSITIVE_INFINITY;
        }

        // NUKE MARSHALL: Kill enemy marshal with bomb when possible.
        if (myPiece == Constants.PIECE_BOMB
                && hisPiece == Constants.PIECE_FIELDMARSHAL) {
            return Double.MAX_VALUE / 2;
        }
        
        // DETONATOR: Engineer should defuse the mines, or move towards the 
        // flag position
        if (player.getBoard()[from].getPiece() == Constants.PIECE_ENGINEER
                && to > 49) {
            if (player.getBoard()[56].getPiece() == Constants.PIECE_FLAG
                    && to == 51) {
                return Double.MAX_VALUE / 2;
            } else if (player.getBoard()[58].getPiece() 
                    == Constants.PIECE_FLAG && to == 53) {
                return Double.MAX_VALUE / 2;
            } else if (to == 51 || to == 53) {
                return Double.MAX_VALUE / 3;
            } else if (hisPiece != Constants.PIECE_EMPTY 
                    && myPiece >= hisPiece){
                return Double.MAX_VALUE / 4;
            }
        }
        
        // SWEET TARGET: Move in front of the flag when possible
        if (player.getBoard()[56].getPiece() == Constants.PIECE_FLAG
                && to == 51) {
            return Double.MAX_VALUE / 5;
        } else if (player.getBoard()[58].getPiece() == Constants.PIECE_FLAG
                && to == 53) {
            return Double.MAX_VALUE / 5;
        } else if (to == 51 || to == 53) {
            return Double.MAX_VALUE / 6;
        }

        // DARE TO KILL: Dare to take down unknown opponent
        // Bomb should get better valuation, and more valuation for pieces 
        // closer to enemy headquarters
        if (hisPiece == Constants.PIECE_UNKNOWN) {
            if (myPiece == Constants.PIECE_BOMB) {
                valuation += (myPiece * 
                        pieceWeights[Constants.ATTACK_UNKNOWN_OPPONENT]);
            }
            // Add weight depending on how its closer to enemy flag
            int distanceFromFlag = Utils.getDistanceBetweenPositions(
                    fromYPos, 12);
            valuation += (13 - distanceFromFlag) *
                    pieceWeights[Constants.ATTACK_UNKNOWN_OPPONENT];
        }

        // EXPRESS TRAVEL: Moving towards rail roads
        valuation += (Utils.getMinDistanceFromRail(to) *
                pieceWeights[Constants.MOVE_TOWARDS_RAIL]);

        // ENGINEER EXPRESS TRAVEL: Add more weight for engineer
        if (myPiece == Constants.PIECE_ENGINEER) {
            valuation += (Utils.getMinDistanceFromRail(to) *
                    pieceWeights[Constants.MOVE_TOWARDS_RAIL]);
        }

        // EASY KILL: Take down lower ranking opponent
        if ((hisPiece > Constants.PIECE_UNKNOWN && myPiece > hisPiece)
                || myPiece == Constants.PIECE_BOMB) {
            valuation += pieceWeights[Constants.BEAT_OPPONENT];
        }

        // CAPTURE MARCH: Give weights depending on how close are we from the
        // enemy flag.
        if (player.getBoard()[58].getPiece() == Constants.PIECE_FLAG) {
            int distance = Utils.getDistanceBetweenPositions(to, 58);
            valuation += (15 - distance)
                    * pieceWeights[Constants.APPROACH_ENEMY_FLAG];
        } else if (player.getBoard()[56].getPiece() == Constants.PIECE_FLAG) {
            int distance = Utils.getDistanceBetweenPositions(to, 56);
            valuation += (15 - distance)
                    * pieceWeights[Constants.APPROACH_ENEMY_FLAG];
        } else {
            int flagPosition1 = Utils.getDistanceBetweenPositions(to, 58);
            int flagPosition2 = Utils.getDistanceBetweenPositions(to, 56);
            valuation += (15 - Math.min(flagPosition1, flagPosition2))
                    * pieceWeights[Constants.APPROACH_ENEMY_FLAG];
        }

        // SILENT ASSASIN: Check how close are we from enemy safe zones,
        // more weights as it gets closer to headquarters
        if (Utils.isEnemyCampPosition(to)) {
            if (player.getBoard()[57].getPiece() == Constants.PIECE_FLAG) {
                int distance = Utils.getDistanceBetweenPositions(to, 57);
                valuation += (15 - distance)
                        * pieceWeights[Constants.APPROACH_ENEMY_SAFE_ZONES];
            } else if (player.getBoard()[55].getPiece()
                == Constants.PIECE_FLAG) {
                int distance = Utils.getDistanceBetweenPositions(to, 55);
                valuation += (15 - distance)
                        * pieceWeights[Constants.APPROACH_ENEMY_SAFE_ZONES];
            } else {
                int flagPosition1 = Utils.getDistanceBetweenPositions(to, 57);
                int flagPosition2 = Utils.getDistanceBetweenPositions(to, 55);
                valuation += (15 - Math.min(flagPosition1, flagPosition2))
                        * pieceWeights[Constants.APPROACH_ENEMY_SAFE_ZONES];
            }
        }

        // HIDE IN BUNKERS: Give weights for pieces to move to our safe zones
        // when we enemy is in our territory
        for (int j = 0; j < 30; j++) {
            if (player.getBoard()[j].getOwner() == player.getHisNumber()) {
                if (Utils.isOurCampPosition(to)
                        && myPiece != Constants.PIECE_BOMB) {
                    valuation += ((15 - Utils.getDistanceBetweenPositions(to,
                            1)) + myPiece + 
                            pieceWeights[Constants.APPROACH_OUR_SAFE_ZONES]);
                }
                break;
            }
        }

        // GENOCIDE GENERAL: Kill enemy front line
        if (myPiece == Constants.PIECE_GENERAL
                && fromYPos == 7
                && toYPos == 7
                && hisPiece != Constants.PIECE_EMPTY) {
            valuation += Constants.PIECE_GENERAL
                    * pieceWeights[Constants.ATTACK_UNKNOWN_OPPONENT];
        }

        // SAVE OUR SOULS: If enemy in our territory, move pieces towards our
        // headquarters. More weights to kill the intruder.
        for (int i = 0; i < 15; i++) {
            if (player.getBoard()[i].getOwner() == player.getHisNumber()) {
                if (to < 15) {
                    // give more weights to pieces that are going to defend
                    valuation += ((15 - Utils.getDistanceBetweenPositions(to,
                            1)) + myPiece)
                            * pieceWeights[Constants.PROTECT_BASE];
                }
                if (to == i
                        && (player.getBoard()[from].getPiece() > player
                                .getBoard()[i].getPiece()
                        || myPiece == Constants.PIECE_BOMB)) {
                    // give more weights if our piece can kill intruder
                    valuation += (myPiece * 
                            pieceWeights[Constants.KILL_INTRUDER]);
                }
            }
        }

        // BRAVE_PATRIOT: Lower rank player should capture headquarters 
        // if position is unknown
        if (Utils.isEnemyHeadquarterPosition(to)) {
            valuation += (Constants.PIECE_FIELDMARSHAL - myPiece)
                    * pieceWeights[Constants.BRAVE_PATRIOT];
        }

        // THIS IS SPARTA: When our pieces are low, full attack, more weights
        // to go forward
        int mypiecesCount = 0;
        for (int i = 0; i < 60; i++) {
            if (player.getBoard()[i].getOwner() == player.getMyNumber()) {
                mypiecesCount++;
            }
        }
        if (mypiecesCount < 13) {
            boolean isOpponentPresent = false;
            for (int i = 0; i < 15; i++) {
                if (player.getBoard()[i].getOwner() == player.getHisNumber()) {
                    isOpponentPresent = true;
                    break;
                }
            }
            if(!isOpponentPresent) {
                boolean isMyPieceInEnemy = false;
                for (int i = 45; i < 60; i++) {
                    if (player.getBoard()[i].getOwner() 
                            == player.getMyNumber()) { 
                        isMyPieceInEnemy = true;
                        break;
                    }
                }
                if(!isMyPieceInEnemy) {
                    if (toYPos > fromYPos) {
                       return pieceWeights[Constants.SLIDE_FORWARD];
                    }
                }
            }
        }

        return valuation;
    }
}
