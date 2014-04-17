package luzhanqi.game;

import luzhanqi.grammar.SyntaxParser;

public class Utils {

    /**
     * Returns the left position of the given position
     * @param position
     * @return
     */
    public static int getLeftOfPosition(int position) {
        return position - 1;
    }

    /**
     * Returns the right position of the given position
     * @param position
     * @return
     */
    public static int getRightOfPosition(int position) {
        return position + 1;
    }

    /**
     * Returns the top position of the given position
     * @param position
     * @return
     */
    public static int getTopOfPosition(int position) {
        return position - 5;
    }

    /**
     * Returns the bottom position of the given position
     * @param position
     * @return
     */
    public static int getBottomOfPosition(int position) {
        return position + 5;
    }

    /**
     * Returns the top right position of the given position
     * @param position
     * @return
     */
    public static int getTopRightOfPosition(int position) {
        return position - 4;
    }

    /**
     * Returns the bottom right position of the given position
     * @param position
     * @return
     */
    public static int getBottomRightOfPosition(int position) {
        return position + 6;
    }

    /**
     * Returns the top left position of the given position
     * @param position
     * @return
     */
    public static int getTopLeftOfPosition(int position) {
        return position - 6;
    }

    /**
     * Returns the bottom left position of the given position
     * @param position
     * @return
     */
    public static int getBottomLeftOfPosition(int position) {
        return position + 4;
    }
    

    /**
     * Is the given position a camp position?
     * @param position
     * @return
     */
    public static boolean isCampPosition(int position) {
        return (position == 11 || position == 13 || position == 17
                || position == 21 || position == 23 || position == 36
                || position == 38 || position == 42 || position == 46
                || position == 48);
    }

    /**
     * Is the given position the headquarter position?
     * @param position
     * @return
     */
    public static boolean isHeadquarterPosition(int position) {
        return (position == 1 || position == 3 || position == 58
        || position == 56);
    }

    /**
     * Is the given position the enemy headquarter position?
     * @param position
     * @return
     */
    public static boolean isEnemyHeadquarterPosition(int position) {
        return (position == 58 || position == 56);
    }

    /**
     * Is the piece immovable?
     * @param piece
     * @return
     */
    public static boolean isNonMovablePiece(int piece) {
        return (Constants.PIECE_FLAG == piece
                || Constants.PIECE_LANDMINE == piece
                || Constants.PIECE_EMPTY == piece);
    }

    /**
     * Is top direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isTopAvailable(int xPos, int yPos) {
        int position = getIndexFromCoordinates(xPos, yPos);
        return (yPos > 0) && (position != 31 && position != 33);
    }

    /**
     * Is bottom direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isBottomAvailable(int xPos, int yPos) {
        int position = getIndexFromCoordinates(xPos, yPos);
        return (yPos < 11) && (position != 26 && position != 28);
    }

    /**
     * Get board index from the given xposition and yposition
     * @param xPos
     * @param yPos
     * @return
     */
    public static int getIndexFromCoordinates(int xPos, int yPos) {
        return xPos + 5 * yPos;
    }

    /**
     * Is right direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isRightAvailable(int xPos) {
        return (xPos < 4);
    }

    /**
     * Is left direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isLeftAvailable(int xPos) {
        return (xPos > 0);
    }

    /**
     * Is top right direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isTopRightAvailable(int xPos, int yPos) {
        int position = getIndexFromCoordinates(xPos, yPos);
        return (xPos < 4 && yPos > 0)
                && (position != 5 && position != 6 && position != 7
                        && position != 8 && position != 10 && position != 12
                        && position != 16 && position != 18 && position != 20
                        && position != 22 && position != 26 && position != 28
                        && position != 30 && position != 31 && position != 32
                        && position != 33 && position != 35 && position != 37
                        && position != 41 && position != 43 && position != 45
                        && position != 47 && position != 51 && position != 53
                        && position != 55 && position != 56 && position != 57
                        && position != 58);
    }

    /**
     * Is bottom left direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isBottomLeftAvailable(int xPos, int yPos) {
        int position = getIndexFromCoordinates(xPos, yPos);
        return (xPos > 0 && yPos < 11)
                && (position != 1 && position != 2 && position != 3
                        && position != 4 && position != 6 && position != 8
                        && position != 12 && position != 14 && position != 16
                        && position != 18 && position != 22 && position != 24
                        && position != 29 && position != 26 && position != 27
                        && position != 28 && position != 31 && position != 33
                        && position != 39 && position != 37 && position != 41
                        && position != 43 && position != 49 && position != 47
                        && position != 54 && position != 51 && position != 52
                        && position != 53);
    }

    /**
     * Is bottom right direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isBottomRightAvailable(int xPos, int yPos) {
        int position = getIndexFromCoordinates(xPos, yPos);
        return (xPos < 4 && yPos < 11)
                && (position != 0 && position != 1 && position != 2
                        && position != 3 && position != 6 && position != 8
                        && position != 10 && position != 12 && position != 16
                        && position != 18 && position != 20 && position != 22
                        && position != 25 && position != 26 && position != 27
                        && position != 28 && position != 31 && position != 33
                        && position != 35 && position != 37 && position != 41
                        && position != 43 && position != 45 && position != 47
                        && position != 50 && position != 51 && position != 52
                        && position != 53);
    }

    /**
     * Is top left direction available?
     * @param xPos
     * @param yPos
     * @return
     */
    public static boolean isTopLeftAvailable(int xPos, int yPos) {
        int position = getIndexFromCoordinates(xPos, yPos);
        return (xPos > 0 && yPos > 0)
                && (position != 6 && position != 7 && position != 8
                        && position != 9 && position != 12 && position != 14
                        && position != 16 && position != 18 && position != 22
                        && position != 24 && position != 26 && position != 28
                        && position != 31 && position != 32 && position != 33
                        && position != 34 && position != 37 && position != 39
                        && position != 41 && position != 43 && position != 47
                        && position != 49 && position != 51 && position != 53
                        && position != 56 && position != 57 && position != 58
                        && position != 59);
    }

    /**
     * Returns the xposition from the given index of board
     * @param squareIndex
     * @return
     */
    public static int getXPosition(int squareIndex) {
        return squareIndex % 5;
    }

    /**
     * Returns the yposition from the given index of board
     * @param squareIndex
     * @return
     */
    public static int getYPosition(int squareIndex) {
        return squareIndex / 5;
    }

    /**
     * Returns true if we can move in the given direction from the given
     * position, else returns false
     * 
     * @param direction
     * @param position
     * @return
     */
    public static boolean isDirectionAvailable(int direction, int position) {
        int xPos = Utils.getXPosition(position);
        int yPos = Utils.getYPosition(position);
        if (Constants.TOP == direction) {
            return isTopAvailable(xPos, yPos);
        }
        if (Constants.DOWN == direction) {
            return isBottomAvailable(xPos, yPos);
        }
        if (Constants.LEFT == direction) {
            return isLeftAvailable(xPos);
        }
        if (Constants.RIGHT == direction) {
            return isRightAvailable(xPos);
        }
        if (Constants.TOPLEFT == direction) {
            return isTopLeftAvailable(xPos, yPos);
        }
        if (Constants.TOPRIGHT == direction) {
            return isTopRightAvailable(xPos, yPos);
        }
        if (Constants.DOWNLEFT == direction) {
            return isBottomLeftAvailable(xPos, yPos);
        }
        if (Constants.DOWNRIGHT == direction) {
            return isBottomRightAvailable(xPos, yPos);
        }
        return false;
    }

    /**
     * Returns the next position in the given direction from the given
     * position
     */
    public static int getNextPosition(int direction, int position) {
        switch (direction) {
        case Constants.TOP:
            return getTopOfPosition(position);
        case Constants.DOWN:
            return getBottomOfPosition(position);
        case Constants.LEFT:
            return getLeftOfPosition(position);
        case Constants.RIGHT:
            return getRightOfPosition(position);
        case Constants.TOPLEFT:
            return getTopLeftOfPosition(position);
        case Constants.TOPRIGHT:
            return getTopRightOfPosition(position);
        case Constants.DOWNLEFT:
            return getBottomLeftOfPosition(position);
        case Constants.DOWNRIGHT:
            return getBottomRightOfPosition(position);
        default:
            return -1;
        }
    }

    /**
     * Returns the next position in rail, in the given direction and position,
     * returns -1 if move not possible
     */
    public static int getNextPositionOnRail(int direction, int fromPosition,
            int myPlayerNumber, int hisPlayerNumber, Square[] board) {
        switch (direction) {
        case Constants.TOP:
            return getNextTopPositionOnRail(fromPosition, myPlayerNumber,
                    hisPlayerNumber, board);
        case Constants.DOWN:
            return getNextBottomPositionOnRail(fromPosition, myPlayerNumber,
                    hisPlayerNumber, board);
        case Constants.LEFT:
            return getNextLeftPositionOnRail(fromPosition, myPlayerNumber,
                    hisPlayerNumber, board);
        case Constants.RIGHT:
            return getNextRightPositionOnRail(fromPosition, myPlayerNumber,
                    hisPlayerNumber, board);
        default:
            return -1;
        }
    }

    /**
     * Returns the opposite direction of the given direction
     */
    public static int getInverseDirection(int direction) {
        switch (direction) {
        case Constants.TOP:
            return Constants.DOWN;
        case Constants.DOWN:
            return Constants.TOP;
        case Constants.LEFT:
            return Constants.RIGHT;
        case Constants.RIGHT:
            return Constants.LEFT;
        case Constants.TOPLEFT:
            return Constants.DOWNRIGHT;
        case Constants.TOPRIGHT:
            return Constants.DOWNLEFT;
        case Constants.DOWNLEFT:
            return Constants.TOPRIGHT;
        case Constants.DOWNRIGHT:
            return Constants.TOPLEFT;
        default:
            return -1;
        }
    }

    /**
     * Returns the minimum distance from the given location to a rail road
     * @param fromIndex
     * @return
     */
    public static int getMinDistanceFromRail(int fromIndex) {
        int minDistance = Integer.MAX_VALUE;
        int[] railIndexes = { 5, 6, 7, 8, 9, 14, 19, 24, 29, 34, 39, 44, 49,
                54, 53, 52, 51, 50, 45, 40, 35, 25, 20, 15, 10, 26, 27, 28,
                31, 32, 33 };
        for (int railIndex : railIndexes) {
            int distance = Utils.getDistanceBetweenPositions(fromIndex,
                    railIndex);
            minDistance = Math.min(distance, minDistance);
        }
        return minDistance;
    }
    
    /**
     * Returns the index on the board based on the position name
     * 
     * @param index
     * @return
     */
    public static String getPositionNameFromIndex(int index) {
        String[] xPositions = { "E", "D", "C", "B", "A" };
        Integer xPos = index % 5;
        Integer yPos = index / 5 + 1;
        return xPositions[xPos] + yPos.toString();
    }

    /**
     * Get the result outcome from the outcome command given by referee
     * 
     * @param command
     * @return
     */
    public static int getResultOutcomeFromOutcomeCommand(String command) {
        String compare = SyntaxParser.getOutcomeCompare(command);
        if (compare.equals("=")) {
            return Constants.RESULT_DRAW;
        }
        if (compare.equals(">")) {
            return Constants.RESULT_WON;
        }
        if (compare.equals("<")) {
            return Constants.RESULT_DEFEATED;
        }
        return Constants.RESULT_NONE;
    }

    /**
     * Get the move from the outcome command given by the referee
     * 
     * @param command
     * @return
     * @throws Exception
     */
    public static int[] getMoveFromOutcomeCommand(String command)
            throws Exception {
        String moveString = SyntaxParser.getOutcomeMove(command);
        String firstPosition = SyntaxParser.getFirstPosition(moveString);
        String secondPosition = SyntaxParser.getSecondPosition(moveString);
        int fromMove = getIndexFromPositionName(firstPosition);
        int toMove = getIndexFromPositionName(secondPosition);
        int[] move = { fromMove, toMove };
        return move;
    }

    /**
     * Based on the position name string, gets the index on board
     * 
     * @param positionName
     * @return
     */
    public static int getIndexFromPositionName(String positionName) {
        int xPos = ('E' - (char) positionName.charAt(0));
        int yPos = Integer.parseInt(positionName.substring(1)) - 1;
        int move = getIndexFromCoordinates(xPos, yPos);
        return move;
    }

    /**
     * Get the move string e.g (A1 B1) from move
     * 
     * @param move (from, to)
     * @return
     */
    public static String getMoveStringFromMove(int[] move) {
        int initialSquare = move[0];
        int targetSquare = move[1];
        return "( " + getPositionNameFromIndex(initialSquare) + " "
                + getPositionNameFromIndex(targetSquare) + " )";
    }

    /**
     * Returns the initial configuration string based on the saved config
     * 
     * @return
     */
    public static String getInitialConfig(Square[] board) {
        StringBuilder initialMove = new StringBuilder();
        initialMove.append("( ");
        for (int i = Constants.PLAYER_A_START; 
                i <= Constants.PLAYER_A_END; i++) {
            int piece = board[i].getPiece();
            if (piece > 1) {
                initialMove.append("( ");
                initialMove.append(getPositionNameFromIndex(i));
                initialMove.append(" ");
                initialMove.append(getPieceFromRank(piece));
                initialMove.append(" )");
            }
        }
        initialMove.append(" )");
        return initialMove.toString();
    }

    /**
     * Returns true if the given position is on rail, else returns false
     */
    public static boolean isOnRail(int position) {
        int xPos = Utils.getXPosition(position);
        int yPos = Utils.getYPosition(position);
        if (!(xPos >= 0 && xPos <= 4 && yPos >= 0 && yPos <= 11)) {
            return false;
        }
        return (yPos == 1 || yPos == 5 || yPos == 6 || yPos == 10
                || (xPos == 0 && yPos > 0 && yPos < 11)
                || (xPos == 4 && yPos > 0 && yPos < 11)
                || (xPos == 2 && yPos > 4 && yPos < 7));
    }

    /**
     * Returns the distance between two given positions.
     */
    public static int getDistanceBetweenPositions(int from, int to) {
        int distance = 0;
        int fromX = getXPosition(from);
        int fromY = getYPosition(from);
        int toX = getXPosition(to);
        int toY = getYPosition(to);
        distance = Math.abs(fromX - toX) + Math.abs(fromY - toY);
        return distance;
    }

    /**
     * Returns true if the given position is enemy camp, else returns false
     */
    public static boolean isEnemyCampPosition(int position) {
        return (position == 36 || position == 38 || position == 42
                || position == 46 || position == 48);
    }

    /**
     * Returns true if the given position is our camp position else returns
     * false
     */
    public static boolean isOurCampPosition(int position) {
        return (position == 11 || position == 13 || position == 17
                || position == 21 || position == 23);
    }

    /**
     * Returns the next left position on rail, returns -1 if move is not
     * possible
     */
    public static int getNextLeftPositionOnRail(int fromPosition,
            int myPlayerNumber, int hisPlayerNumber, Square[] board) {
        int nextLeftPosition = -1;
        int xPos = getXPosition(fromPosition);
        int yPos = getYPosition(fromPosition);
        if (xPos > 0) {
            xPos--;
            int position = getIndexFromCoordinates(xPos, yPos);
            if (isOnRail(position)
                    && (Constants.PIECE_EMPTY == board[position].getPiece() ||
                    board[position].getOwner() == hisPlayerNumber)) {
                nextLeftPosition = position;
            }
        }
        return nextLeftPosition;
    }

    /**
     * Returns the next right position on the rail returns -1 if move is not
     * possible
     */
    public static int getNextRightPositionOnRail(int fromPosition,
            int myPlayerNumber, int hisPlayerNumber, Square[] board) {
        int nextRightPosition = -1;
        int xPos = getXPosition(fromPosition);
        int yPos = getYPosition(fromPosition);
        if (xPos < 4) {
            xPos++;
            int position = getIndexFromCoordinates(xPos, yPos);
            if (isOnRail(position)
                    && (Constants.PIECE_EMPTY == board[position].getPiece() ||
                    board[position].getOwner() == hisPlayerNumber)) {
                nextRightPosition = position;
            }
        }
        return nextRightPosition;
    }

    /**
     * Returns the next top position on rail, returns -1 is no move is
     * possible
     */
    public static int getNextTopPositionOnRail(int fromPosition,
            int myPlayerNumber, int hisPlayerNumber, Square[] board) {
        int nextTopPosition = -1;
        int xPos = getXPosition(fromPosition);
        int yPos = getYPosition(fromPosition);
        if (yPos > 0) {
            yPos--;
            int position = getIndexFromCoordinates(xPos, yPos);
            if (isOnRail(position)
                    && (Constants.PIECE_EMPTY == board[position].getPiece() ||
                    board[position].getOwner() == hisPlayerNumber)) {
                nextTopPosition = position;
            }
        }
        return nextTopPosition;
    }

    /**
     * Returns the next bottom position on rail, returns -1 if move is not
     * possible
     */
    public static int getNextBottomPositionOnRail(int fromPosition,
            int myPlayerNumber, int hisPlayerNumber, Square[] board) {
        int nextBottomPosition = -1;
        int xPos = getXPosition(fromPosition);
        int yPos = getYPosition(fromPosition);
        if (yPos < 11) {
            yPos++;
            int position = getIndexFromCoordinates(xPos, yPos);
            if (isOnRail(position)
                    && (Constants.PIECE_EMPTY == board[position].getPiece() ||
                    board[position].getOwner() == hisPlayerNumber)) {
                nextBottomPosition = position;
            }
        }
        return nextBottomPosition;
    }

    /**
     * Returns the next right position on the rail, returns -1 if move is not
     * possible
     */
    public static int getRightmostPositionOnRail(int fromPosition,
            int myPlayerNumber, int hisPlayerNumber, Square[] board) {
        int rightMostPosition = -1;
        int xPos = getXPosition(fromPosition);
        int yPos = getYPosition(fromPosition);
        for (int i = xPos + 1; i <= 4; i++) {
            if (Constants.PIECE_EMPTY == board[i].getPiece()) {
                rightMostPosition = i;
                continue;
            } else if (board[i].getOwner() == myPlayerNumber) {
                break;
            } else if (board[i].getOwner() == hisPlayerNumber) {
                rightMostPosition = i;
                break;
            }
        }
        if (-1 == rightMostPosition) {
            return -1;
        }
        return getIndexFromCoordinates(rightMostPosition, yPos);
    }

    public static boolean isTargetAtIntersection(int target) {
        switch (target) {
        case 9:
        case 5:
        case 54:
        case 50:
        case 29:
        case 34:
        case 25:
        case 30:
        case 27:
        case 32:
            return true;
        default:
            return false;
        }
    }

    /**
     * Initial configuration
     * 
     * @return
     */
    public static int[] getInitialSetup() {
        int[] pieces = {
                Constants.PIECE_LIEUTENANT, // E1
                Constants.PIECE_FLAG, // D1
                Constants.PIECE_LIEUTENANT, // C1
                Constants.PIECE_LIEUTENANT, // B1
                Constants.PIECE_CAPTAIN, // A1
                Constants.PIECE_LANDMINE, // E2
                Constants.PIECE_LANDMINE, // D2
                Constants.PIECE_LANDMINE, // C2
                Constants.PIECE_COLONEL, // B2
                Constants.PIECE_ENGINEER, // A2
                Constants.PIECE_MAJORGENERAL, // E3
                Constants.PIECE_EMPTY, // D3
                Constants.PIECE_FIELDMARSHAL, // C3
                Constants.PIECE_EMPTY, // B3
                Constants.PIECE_MAJOR, // A3
                Constants.PIECE_BRIGADIERGENERAL, // E4
                Constants.PIECE_CAPTAIN, // D4
                Constants.PIECE_EMPTY, // C4
                Constants.PIECE_CAPTAIN, // B4
                Constants.PIECE_BOMB, // A4
                Constants.PIECE_BOMB,// E5
                Constants.PIECE_EMPTY, // D5
                Constants.PIECE_MAJORGENERAL, // C5
                Constants.PIECE_EMPTY, // B5
                Constants.PIECE_COLONEL, // A5
                Constants.PIECE_GENERAL, // E6
                Constants.PIECE_ENGINEER,// D6
                Constants.PIECE_BRIGADIERGENERAL, // C6
                Constants.PIECE_ENGINEER, // B6
                Constants.PIECE_MAJOR // A6
        };
        return pieces;
    }

    /**
     * Get the rank that class decided, based on the rank we chose
     * 
     * @param rank
     * @return
     */
    public static String getPieceFromRank(int rank) {
        switch (rank) {
        case 0:
            return " ";
        case 1:
            return "U";
        case 2:
            return "F";
        case 3:
            return "L";
        case 4:
            return "B";
        case 5:
            return "1";
        case 6:
            return "2";
        case 7:
            return "3";
        case 8:
            return "4";
        case 9:
            return "5";
        case 10:
            return "6";
        case 11:
            return "7";
        case 12:
            return "8";
        case 13:
            return "9";
        default:
            return "U";
        }
    }

    /**
     * Returns true if both source and target are on rail, else returns false
     */
    public static boolean moveOnRailPossible(int source, int target) {
        return isOnRail(source)
                && isOnRail(target);
    }

    /**
     * Returns true if the given index position on board is a corner position
     * on rail road. Else returns false.
     * 
     * @param index
     * @return boolean
     */
    public static boolean isCornerOnRail(int index) {
        return (index == 5 || index == 9 || index == 50 || index == 54);
    }

    /**
     * Returns true if the given index position on board is a intersection
     * position on rail road. Else returns false.
     * 
     * @param index
     * @return boolean
     */
    public static boolean isIntersectionOnRail(int index) {
        return (index == 25 || index == 27 || index == 29 || index == 30
                || index == 32 || index == 34);
    }
}
