package luzhanqi.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import luzhanqi.game.Utils;
import luzhanqi.grammar.SyntaxParser;
import luzhanqi.grammar.SyntaxValidator;
import luzhanqi.player.Player;

public class Play5500 {
    public static String logFile = "logFile.txt";
    public static boolean isLogEnabled = false;
    static Player player = null;

    public static void main(String[] args) {

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
                System.in));
        while (true) {
            try {
                String inputCommand = bufferRead.readLine();
                if (SyntaxValidator.isMsg0SyntaxValid(inputCommand)) {
                    player = makeInitialConfigMove(inputCommand);
                } else if (SyntaxValidator.isTurn(inputCommand)) {
                    // Send our move
                    makeOurMove(player, inputCommand);
                } else if (SyntaxValidator.isOutcome(inputCommand)) {
                    storeOutcomesToGameState(player, inputCommand);
                } else if (SyntaxValidator.isIllegal(inputCommand)) {
                    System.exit(-1);
                } else if (SyntaxValidator.isGameEnd(inputCommand)) {
                    System.exit(0);
                } else if (SyntaxValidator.isFlag(inputCommand)) {
                    storeOpponentFlagPosition(player, inputCommand);
                }
            } catch (Exception e) {
                System.exit(-1);
            }
        }
    }

    /**
     * Stores opponents flag position
     * 
     * @param player
     * @param inputCommand
     * @throws Exception
     */
    private static void storeOpponentFlagPosition(Player player,
            String inputCommand) throws Exception {
        String flagPosition = SyntaxParser.getFlagPosition(inputCommand);
        int flagPlayer = SyntaxParser.getFlagPlayerType(inputCommand);
        if (player.getMyPlayerNumber() != flagPlayer) {
            // save opponents flag position
            int position = Utils.getIndexFromPositionName(flagPosition);
            player.storeOpponentFlag(position);
        }
    }

    /**
     * Stores the outcomes from referee to our state, depending on the player
     * 
     * @param player
     * @param inputCommand
     * @throws IOException
     * @throws Exception
     */
    private static void storeOutcomesToGameState(Player player,
            String inputCommand) throws IOException, Exception {
        int outcome = Utils
                .getResultOutcomeFromOutcomeCommand(inputCommand);
        int[] outcomeMove = Utils
                .getMoveFromOutcomeCommand(inputCommand);
        player.submitMoveResult(outcomeMove[0], outcomeMove[1], outcome);
    }

    /**
     * Gets our move based on the current board configuration
     * 
     * @param player
     * @param inputCommand
     */
    private static void makeOurMove(Player player, String inputCommand) {
        int whoseTurn = Integer.parseInt(SyntaxParser.getWhoseTurn(inputCommand));
        if (whoseTurn == player.getMyPlayerNumber()) {
            int[] movegame = player.getMove();
            if (movegame == null) {
                System.out.println("( )");
            } else {
                System.out.println(Utils.getMoveStringFromMove(movegame));
            }
        }
    }

    /**
     * Initialized the board and pieces and prints the initial configuration
     * to standard output
     * 
     * @param inputCommand
     * @return
     */
    private static Player makeInitialConfigMove(String inputCommand) {
        // initialize game and send out initial config
        String playerNumber = SyntaxParser.getFirstTurn(inputCommand);
        player = new Player(Integer.parseInt(playerNumber));
        System.out.println(Utils.getInitialConfig(player.getBoard()));
        return player;
    }

}
