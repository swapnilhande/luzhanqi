package luzhanqi.player;

import luzhanqi.game.Constants;
import luzhanqi.game.Square;
import luzhanqi.game.Utils;

public class Player {

    private int myNumber;
    private int hisNumber;
    private Square[] board;
    private boolean[][] canMove;
    private boolean[] hasMoved;
    /**
     * How often we've been moving back and forth between lastMoveFrom and
     * lastMoveTo
     */
    private int threeTurnRuleTurns;
    /**
     * The lowest square we've been moving back and forth on
     */
    private int threeTurnRuleLow;
    /**
     * The highest square we've been moving back and forth on
     */
    private int threeTurnRuleHigh;

    public static final int FIRST_MOVE = 0;
    public static final int STILL_OPPONENT = 1;
    public static final int BEAT_OPPONENT = 2;
    public static final int MOVE_FORWARD = 3;
    public static final int MOVE_RIGHT = 4;
    public static final int MOVE_BACKWARD = 5;
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_TOP_RIGHT = 7;
    public static final int MOVE_TOP_LEFT = 8;
    public static final int MOVE_BOTTOM_RIGHT = 9;
    public static final int MOVE_BOTTOM_LEFT = 10;
    public static final int EXPLORATION_RATE = 11;
    public static final int RANDOM_INFLUENCE = 12;
    public static final int OPTION_PARAM_MULTIPLIER = 4;

    private double[][] pieceParams;

    public Player(int playerNumber) {
        this.myNumber = playerNumber;
        this.hisNumber = getOtherPlayerNumber(playerNumber);
        this.board = new Square[Constants.BOARD_SIZE];
        this.hasMoved = new boolean[Constants.BOARD_SIZE];
        this.canMove = new boolean[Constants.BOARD_SIZE]
                [Constants.POSSIBLE_DIRECTIONS];
        initializeBoard();
        setupPiecesOnBoard();
        this.hasMoved = new boolean[Constants.BOARD_SIZE];
        /*
         * this.canMove = new boolean[Constants.BOARD_SIZE]
         * [Constants.POSSIBLE_DIRECTIONS];
         */
        initializeParams();
    }

    /**
     * Stores opponents flag position
     * 
     * @param xPos
     * @param yPos
     */
    public void storeOpponentFlag(int position) {
        board[position].setPiece(Constants.PIECE_FLAG);
    }

    /**
     * Initialize board with all empty pieces
     */
    private void initializeBoard() {
        for (int square = 0; square < Constants.BOARD_SIZE; square++) {
            board[square] = new Square();
        }
    }

    /**
     * Setups our and opponents pieces on the board. Our pieces are set using
     * the initial configuration.
     */
    public void setupPiecesOnBoard() {
        int[] mySetup = Utils.getInitialSetup();
        for (int squareIndex = Constants.PLAYER_A_START; squareIndex <= Constants.PLAYER_B_END; squareIndex++) {
            if (Utils.isCampPosition(squareIndex)) {
                putSquareOnBoard(new Square(), squareIndex);
            } else if (squareIndex <= Constants.PLAYER_A_END) {
                Square mySquare = new Square(mySetup[squareIndex], myNumber);
                putSquareOnBoard(mySquare, squareIndex);
            } else if (squareIndex <= Constants.PLAYER_B_END) {
                Square opponentSquare = new Square(Constants.PIECE_UNKNOWN,
                        hisNumber);
                putSquareOnBoard(opponentSquare, squareIndex);
            }
        }
    }

    /**
     * Add weights to the decisions accordingly. Randomize them. Possible
     * params: - amount of moves one can make afterwards - whether there is an
     * opponent to be hit - move away from dangerous pieces - parameter per
     * piece - incorporate possible negative results of an attack - parameter
     * to hit every moving piece when all threats are far away (>10 squares)
     */
    public void initializeParams() {
        pieceParams = new double[Constants.PIECE_FIELDMARSHAL + 1][];
        for (int i = 0; i < pieceParams.length; i++) {
            pieceParams[i] = new double[RANDOM_INFLUENCE + 1];
            pieceParams[i][FIRST_MOVE] = (Math.random() - 0)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][STILL_OPPONENT] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][BEAT_OPPONENT] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_FORWARD] = (Math.random() - 0)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_RIGHT] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_BACKWARD] = (Math.random() - 0.8)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_LEFT] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_TOP_LEFT] = (Math.random() - 0)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_TOP_RIGHT] = (Math.random() - 0)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_BOTTOM_LEFT] = (Math.random() - 0.8)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][MOVE_BOTTOM_RIGHT] = (Math.random() - 0.8)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][EXPLORATION_RATE] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][RANDOM_INFLUENCE] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
        }
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
        int myPiece = board[from].getPiece();
        if (!hasMoved[from]) {
            // TODO Increase this value when there are fewer pieces left that
            // have not moved yet
            valuation += pieceParams[myPiece][FIRST_MOVE];
        }

        int hisPiece = board[to].getPiece();
        if (hisPiece == Constants.PIECE_UNKNOWN && !hasMoved[to]) {
            // TODO Incorporate the chance that you can beat this opponent
            valuation += pieceParams[myPiece][STILL_OPPONENT];
        }

        // Add a penalty/bonus if there's a piece at our target we can beat
        if (hisPiece > Constants.PIECE_UNKNOWN && myPiece > hisPiece) {
            // The parameter weighs in more when the opponent's piece is
            // relatively weak and ours relatively strong
            int totalValue = (hisPiece + myPiece);
            valuation += totalValue * pieceParams[myPiece][BEAT_OPPONENT];
        }

        // If we can attack an unknown piece that has moved,
        // see if our piece is brave (read: weak) enough to do that
        // TODO Incorporate whether we're a miner
        if (hasMoved[to] && hisPiece == Constants.PIECE_UNKNOWN) {
            valuation += pieceParams[myPiece][EXPLORATION_RATE];
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
            valuation += pieceParams[myPiece][MOVE_FORWARD];
        }

        // If we can move to the right
        if (to == rightPosition) {
            valuation += pieceParams[myPiece][MOVE_RIGHT];
        }

        // If we can move to our own side
        if (to == topPosition) {
            valuation += pieceParams[myPiece][MOVE_BACKWARD];
        }

        // If we can move to the left
        if (to == leftPosition) {
            valuation += pieceParams[myPiece][MOVE_LEFT];
        }

        // Top left
        if (to == bottomRightPosition) {
            valuation += pieceParams[myPiece][MOVE_TOP_LEFT];
        }

        // Top right
        if (to == bottomLeftPosition) {
            valuation += pieceParams[myPiece][MOVE_TOP_RIGHT];
        }

        // Bottom left
        if (to == topRightPosition) {
            valuation += pieceParams[myPiece][MOVE_BOTTOM_LEFT];
        }

        // Bottom right
        if (to == topLeftPosition) {
            valuation += pieceParams[myPiece][MOVE_BOTTOM_RIGHT];
        }

        // Add the random influence
        valuation += Math.random() * RANDOM_INFLUENCE;
        return valuation;
    }

    public int[] getMove() {
        // from, to
        int[] move = new int[2];
        int bestFrom = -1;
        int bestTo = -1;
        double bestValuation = Double.NEGATIVE_INFINITY;
        double valuation;
        for (int i = 0; i < canMove.length; i++) {
            if (board[i].getOwner() == myNumber) {
                for (int direction = Constants.TOP; direction <= Constants.DOWNRIGHT; direction++) {
                    if (canMove[i][direction]) {
                        int positionInDirection =
                                Utils.getNextPosition(direction, i);
                        if ((Utils.isCampPosition(positionInDirection) && board[positionInDirection]
                                .getPiece() != Constants.PIECE_EMPTY)
                                ||
                                Utils.isNonMovablePiece(board[i].getPiece())
                                || Utils.isHeadquarterPosition(i)) {
                            continue;
                        }
                        valuation = evaluateMove(i, positionInDirection);
                        if (isBetterValuation(valuation, bestValuation, i,
                                positionInDirection)) {
                            bestFrom = i;
                            bestTo = positionInDirection;
                            bestValuation = valuation;
                        }
                    }
                }
            }
        }

        if (bestFrom == -1 || bestTo == -1
                || bestValuation == Double.NEGATIVE_INFINITY) {
            return null;
        }

        move[0] = bestFrom;
        move[1] = bestTo;

        if (threeTurnRuleHigh == Math.max(bestFrom, bestTo)
                && threeTurnRuleLow == Math.min(bestFrom, bestTo)) {
            threeTurnRuleTurns++;
        } else {
            threeTurnRuleHigh = Math.max(bestFrom, bestTo);
            threeTurnRuleLow = Math.min(bestFrom, bestTo);
            threeTurnRuleTurns = 1;
        }
        return move;
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
        return (valuation > bestValuation && !(threeTurnRuleTurns >= 3
                && threeTurnRuleHigh == threeTurnRuleHighValue
                && threeTurnRuleLow == threeTurnRuleLowValue));
    }

    public String getBoardString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append("|-----|-----|-----|-----|-----|\n");
        for (int i = Constants.PLAYER_A_START; i <= Constants.PLAYER_B_END; i++) {
            if (Utils.isCampPosition(i)) {
                boardString.append("( ");
            } else if (Utils.isCampPosition(i - 1)) {
                boardString.append(")");
            } else {
                boardString.append("|");
            }
            boardString.append(board[i].getOwner() == 1 ? " A " : board[i]
                    .getOwner() == 2 ? " B " : "  ");
            boardString.append(Utils.getPieceFromRank(board[i].getPiece())
                    + " ");
            if (i == 29) {
                boardString.append("|\n|-----------------------------");
            }
            if ((i + 1) % 5 == 0) {
                boardString.append("|\n");
            }

        }
        boardString.append("|-----|-----|-----|-----|-----|\n");
        return boardString.toString();
    }

    /*
     * public static void main(String[] args) { Player player = new Player(2);
     * System.out.println(player.getBoardString()); int[] move =
     * player.getMove(); System.out.println(move[0] + "->" + move[1]); //
     * System.out.println(player.getBoardString()); }
     */

    /**
     * Stores the move, and updates the board config
     * 
     * @param fromSquare
     *            index of the from square
     * @param toSquare
     *            index of the to square
     * @param outcome
     *            if won, defeated or draw
     */
    public void submitMoveResult(int fromSquare, int toSquare, int outcome) {
        Square movingSquare = board[fromSquare];
        Square fixedSquare = board[toSquare];
        if (Constants.LOGGING_ENABLED) {
            // TODO log the move and outcome
        }
        if (Constants.RESULT_DRAW == outcome) {
            // Make both squares empty
            putSquareOnBoard(new Square(), fromSquare);
            putSquareOnBoard(new Square(), toSquare);
            // Both the positions cannot move in any directions
            canMove[fromSquare] = new boolean[Constants.POSSIBLE_DIRECTIONS];
            canMove[toSquare] = new boolean[Constants.POSSIBLE_DIRECTIONS];
        }
        if (Constants.RESULT_DEFEATED == outcome) {
            // If our player is defeated, we update opponents rank
            // If opponent is defeated, we do nothing
            if (movingSquare.getOwner() == myNumber) {
                // Check rank of opponent
                updateOpponentPiece(movingSquare, fixedSquare);
            }
            // Make from empty, since it lost
            putSquareOnBoard(new Square(), fromSquare);
            canMove[fromSquare] = new boolean[Constants.POSSIBLE_DIRECTIONS];
        }
        if (Constants.RESULT_WON == outcome) {
            if (movingSquare.getOwner() != myNumber) {
                updateOpponentPiece(fixedSquare, movingSquare);
            }
            putSquareOnBoard(new Square(), fromSquare);
            putSquareOnBoard(movingSquare, toSquare);
        }
        if (Constants.RESULT_NONE == outcome) {
            putSquareOnBoard(new Square(), fromSquare);
            putSquareOnBoard(movingSquare, toSquare);
        }
    }

    /**
     * Method updates the opponent piece when our player gets defeated, or
     * opponent wins. We set the opponents piece at least a rank higher than
     * ours. If the rank is already high, we do not change it as it has killed
     * that rank piece sometime.
     * 
     * @param movingSquare
     * @param fixedSquare
     */
    private void updateOpponentPiece(Square movingSquare, Square fixedSquare) {
        if (fixedSquare.getPiece() <= movingSquare.getPiece()) {
            fixedSquare.setPiece(movingSquare.getPiece() + 1);
        }
    }

    public void putSquareOnBoard(Square square, int position) {
        board[position] = square;
        canMove[position] = new boolean[8];
        hasMoved[position] = true;
        // Update neighboring positions
        for (int direction = Constants.TOP; direction <= Constants.DOWNRIGHT; direction++) {
            updatePossibleMovesAfterMove(position, direction);
        }
        if (Utils.isNonMovablePiece(square.getPiece())
                || Utils.isHeadquarterPosition(position)) {
            canMove[position] = new boolean[8];
        }
    }

    public void updatePossibleMovesAfterMove(int thisPosition, int direction) {
        if (Utils.isDirectionAvailable(direction, thisPosition)) {
            int nextPosition = Utils.getNextPosition(direction, thisPosition);
            int oppDirection = Utils.getInverseDirection(direction);
            changeCanMoveInDirection(thisPosition, direction, nextPosition,
                    oppDirection);
        }
    }

    private void changeCanMoveInDirection(int thisPosition, int direction,
            int nextPosition, int oppDirection) {
        if (board[thisPosition].getOwner() != board[nextPosition]
                .getOwner()) {
            if (Utils.isCampPosition(nextPosition)) {
                if (Constants.PIECE_EMPTY != board[nextPosition]
                        .getPiece()) {
                    canMove[thisPosition][direction] = false;
                    canMove[nextPosition][oppDirection] = true;
                } else {
                    canMove[thisPosition][direction] = true;
                    canMove[nextPosition][oppDirection] = false;
                }
            } else if (Utils.isCampPosition(thisPosition)) {
                if (Constants.PIECE_EMPTY != board[thisPosition]
                        .getPiece()) {
                    canMove[thisPosition][direction] = true;
                    canMove[nextPosition][oppDirection] = false;
                } else {
                    canMove[thisPosition][direction] = false;
                    canMove[nextPosition][oppDirection] = true;
                }
            } else {
                canMove[thisPosition][direction] = true;
                canMove[nextPosition][oppDirection] = true;
            }
        } else {
            canMove[thisPosition][direction] = false;
            canMove[nextPosition][oppDirection] = false;
        }
    }

    /**
     * If this player is 0, return 1 else return 0
     * 
     * @param playerNumber
     * @return other player's number
     */
    public int getOtherPlayerNumber(int playerNumber) {
        return Math.abs(3 - playerNumber);
    }

    /* Setter and Getters */
    public int getMyPlayerNumber() {
        return myNumber;
    }

    public void setMyPlayerNumber(int myPlayerNumber) {
        this.myNumber = myPlayerNumber;
    }

    public int getOpponentPlayerNumber() {
        return hisNumber;
    }

    public void setOpponentPlayerNumber(int opponentPlayerNumber) {
        this.hisNumber = opponentPlayerNumber;
    }

    public Square[] getBoard() {
        return board;
    }

    public void setBoard(Square[] board) {
        this.board = board;
    }

    public boolean[][] getPossibleMoves() {
        return canMove;
    }

    public void setPossibleMoves(boolean[][] possibleMoves) {
        this.canMove = possibleMoves;
    }

    public boolean[] getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean[] hasMoved) {
        this.hasMoved = hasMoved;
    }
}
