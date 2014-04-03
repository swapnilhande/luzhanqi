package luzhanqi.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import luzhanqi.game.Constants;
import luzhanqi.game.Utils;
import luzhanqi.grammar.SyntaxParser;
import luzhanqi.grammar.SyntaxValidator;
import luzhanqi.play.MoveGenerator;
import luzhanqi.player.Player;

public class Play5500 {
    public static String logFile = "logFile.txt";
    static Player player = null;
    static MoveGenerator moveGenerator = null;

    public static void main(String[] args) {

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
                System.in));
        while (true) {
            try {
                String inputCommand = bufferRead.readLine();
                if (SyntaxValidator.isMsg0SyntaxValid(inputCommand)) {
                    player = makeInitialConfigMove(inputCommand);
                    moveGenerator = new MoveGenerator(player);
                } else if (SyntaxValidator.isTurn(inputCommand)) {
                    // Send our move
                    makeOurMove(inputCommand);
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
        if (Constants.LOGGING_ENABLED) {
            try {
                FileWriter fw = new FileWriter("log.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("\nBoard:\n" + player.getBoardString() + "\n");
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets our move based on the current board configuration
     * 
     * @param inputCommand
     */
    private static void makeOurMove(String inputCommand) {
        int whoseTurn = Integer.parseInt(SyntaxParser
                .getWhoseTurn(inputCommand));
        if (whoseTurn == player.getMyPlayerNumber()) {
            long startTime = System.currentTimeMillis();
            int[] movegame = moveGenerator.getMoveInTime(startTime);
            if (Constants.LOGGING_ENABLED) {
                try {
                    FileWriter fw = new FileWriter("log.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    if(movegame == null) {
                        bw.write("\nMove: resign");
                    }else{
                        bw.write("\nMove: "+movegame[0] + " -> "+movegame[1]);
                    }
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (movegame == null) {
                System.out.println("(resign)");
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
        String time = SyntaxParser.getFirstTimePerMove(inputCommand);
        player = new Player(Integer.parseInt(playerNumber),
                Double.parseDouble(time)*1000);
        System.out.println(Utils.getInitialConfig(player.getBoard()));
        return player;
    }

}
