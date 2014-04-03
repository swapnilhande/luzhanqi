package luzhanqi.player;

import luzhanqi.game.Constants;
import luzhanqi.game.Square;
import luzhanqi.game.Utils;

public class Player {

    /**
     * Number assigned by referee to game playing program
     */
    private int myNumber;
    /**
     * Number assigned by referee to opponent
     */
    private int hisNumber;
    /**
     * Array of 60 square position to represent
     * Luzhanqi's board configuration.
     * Index represents the position,
     * and value represent the piece
     */
    private Square[] board;
    /**
     * Array of 60 positions with 8 directions which takes either
     * true/false. True indicates piece has moved. False
     * indicates piece has moved from that position
     */
    private boolean[][] canMove;
    /**
     * Array of 60 positions which states if the piece on
     * that position has ever moved
     */
    private boolean[] hasMoved;
    /**
     * Time alloted to return the Move
     */
    private double time;
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
    public static final int SLIDE_LEFT = 13;
    public static final int SLIDE_RIGHT = 14;
    public static final int SLIDE_TOP = 15;
    public static final int SLIDE_DOWN = 16;
    public static final int CAPTURE_FLAG = 17;
    public static final int OPTION_PARAM_MULTIPLIER = 4;

    private double[][] pieceParams;

    public Player(int playerNumber, Double time) {
        this.myNumber = playerNumber;
        this.hisNumber = getOtherPlayerNumber(playerNumber);
        this.board = new Square[Constants.BOARD_SIZE];
        this.hasMoved = new boolean[Constants.BOARD_SIZE];
        this.canMove = new boolean[Constants.BOARD_SIZE]
                [Constants.POSSIBLE_DIRECTIONS];
        this.time = time;
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
            pieceParams[i] = new double[CAPTURE_FLAG + 1];
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
            // TODO: CHeck adding for rail moves
            pieceParams[i][SLIDE_DOWN] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][SLIDE_LEFT] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][SLIDE_RIGHT] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][SLIDE_TOP] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
            pieceParams[i][CAPTURE_FLAG] = (Math.random() - 0.5)
                    * OPTION_PARAM_MULTIPLIER;
        }
    }


    public String getBoardString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append("|-----|-----|-----|-----|-----|\n");
        for (int i = Constants.PLAYER_A_START; i <= Constants.PLAYER_B_END; i++) {
            if (Utils.isCampPosition(i)) {
                boardString.append("(");
            } else if (Utils.isCampPosition(i - 1)) {
                boardString.append(")");
            } else {
                boardString.append("|");
            }
            boardString.append(board[i].getOwner() == 1 ? " A " : board[i]
                    .getOwner() == 2 ? " B " : "   ");
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

//    public int[][] getRailMoves(int fromPosition) {
//        int[][] railMoves = null;
//        if (Utils.isOnRail(fromPosition)) {
//            // Position is on rail, so calculate rail moves
//            // Get moves going above
//            // Get moves going below
//            // Get moves going right
//            // Get moves going left
//        }
//        return railMoves;
//    }

    private void changeCanMoveInDirection(int thisPosition, int direction,
            int nextPosition, int oppDirection) {
        if (board[thisPosition].getOwner() == myNumber
                && board[nextPosition].getOwner() == myNumber) {
            // Care only about my pieces
            canMove[thisPosition][direction] = false;
            canMove[nextPosition][oppDirection] = false;
        } else if (board[thisPosition].getOwner() == myNumber) {
            modifyMyPieceMoveStatus(thisPosition, direction, nextPosition);
        } else if (board[nextPosition].getOwner() == myNumber) {
            modifyMyPieceMoveStatus(nextPosition, oppDirection, thisPosition);
        }
    }

    private void modifyMyPieceMoveStatus(int thisPosition, int direction,
            int nextPosition) {
        if (board[nextPosition].getPiece() == Constants.PIECE_EMPTY) {
            // Next piece is empty, check if our piece can move
            if (!Utils.isNonMovablePiece(board[thisPosition].getPiece())
                    && !Utils.isHeadquarterPosition(thisPosition)) {
                canMove[thisPosition][direction] = true;
            } else {
                canMove[thisPosition][direction] = false;
            }
        } else {
            // next piece is opponent, check if it is camp and our piece
            // can move
            if (!Utils.isCampPosition(nextPosition)
                    && !Utils.isNonMovablePiece(board[thisPosition]
                            .getPiece())
                    && !Utils.isHeadquarterPosition(thisPosition)) {
                canMove[thisPosition][direction] = true;
            } else {
                canMove[thisPosition][direction] = false;
            }
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

    public int getMyNumber() {
        return myNumber;
    }

    public void setMyNumber(int myNumber) {
        this.myNumber = myNumber;
    }

    public int getHisNumber() {
        return hisNumber;
    }

    public void setHisNumber(int hisNumber) {
        this.hisNumber = hisNumber;
    }

    public boolean[][] getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean[][] canMove) {
        this.canMove = canMove;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getThreeTurnRuleTurns() {
        return threeTurnRuleTurns;
    }

    public void setThreeTurnRuleTurns(int threeTurnRuleTurns) {
        this.threeTurnRuleTurns = threeTurnRuleTurns;
    }

    public int getThreeTurnRuleLow() {
        return threeTurnRuleLow;
    }

    public void setThreeTurnRuleLow(int threeTurnRuleLow) {
        this.threeTurnRuleLow = threeTurnRuleLow;
    }

    public int getThreeTurnRuleHigh() {
        return threeTurnRuleHigh;
    }

    public void setThreeTurnRuleHigh(int threeTurnRuleHigh) {
        this.threeTurnRuleHigh = threeTurnRuleHigh;
    }

    public double[][] getPieceParams() {
        return pieceParams;
    }

    public void setPieceParams(double[][] pieceParams) {
        this.pieceParams = pieceParams;
    }
}
