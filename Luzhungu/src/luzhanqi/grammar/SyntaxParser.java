package luzhanqi.grammar;

public class SyntaxParser {

   /**
    * Gets which turn is assigned to us, based on the init command
    * @param command
    * @return
    */
   public static String getFirstTurn(String command) {
      String trimmed = command.replaceAll(SyntaxGrammar.WHITESPACE, "");
      int firstT = trimmed.indexOf("t");
      int secondT = trimmed.indexOf("t", firstT + 1);
      String turnIndicator = trimmed.substring(firstT + 1, secondT);
      return turnIndicator;
   }

   /**
    * Gets the time/move assigned to us, based on the init command
    * @param command
    * @return
    */
   public static String getFirstTimePerMove(String command) {
      String timePerMove = command.substring(
	    command.indexOf("time/move") + 9, command.lastIndexOf(")"));
      timePerMove = timePerMove.replaceAll(SyntaxGrammar.WHITESPACE, "");
      return timePerMove;
   }

   /**
    * Gets who won, from the end command
    * @param command
    * @return
    */
   public static String getWhoWon(String command) {
      String whoWon = command.substring(command.indexOf("end") + 3,
	    command.lastIndexOf(")"));
      whoWon = whoWon.replaceAll(SyntaxGrammar.WHITESPACE, "");
      return whoWon;
   }

   /**
    * Gets the move done, which was illegal, from the illegal command
    * @param command
    * @return
    */
   public static String getIllegalMove(String command) {
      String illegalMove = command.substring(command.indexOf("illegal") + 7,
	    command.lastIndexOf(")"));
      illegalMove = illegalMove.replaceAll(SyntaxGrammar.WHITESPACE, "");
      return illegalMove;
   }

   /**
    * Gets whose turn is it from the go command
    * @param command
    * @return
    */
   public static String getWhoseTurn(String command) {
      String whoseTurn = command.substring(command.indexOf("go") + 2,
	    command.lastIndexOf(")"));
      whoseTurn = whoseTurn.replaceAll(SyntaxGrammar.WHITESPACE, "");
      return whoseTurn;
   }

   /**
    * Gets initial position from a move command
    * @param move
    * @return
    */
   public static String getFirstPosition(String move) {
      String trimmed = move.replaceAll(SyntaxGrammar.WHITESPACE, "");

      if (Character.isDigit(trimmed.charAt(3))) {
	 return trimmed.substring(1, 4);
      } else {
	 return trimmed.substring(1, 3);
      }
   }

   /**
    * Gets the target position from a move command
    * @param move
    * @return
    * @throws Exception
    */
   public static String getSecondPosition(String move) throws Exception {
      String trimmed = move.replaceAll(SyntaxGrammar.WHITESPACE, "");
      if (Character.isDigit(trimmed.charAt(3))) {
	 if (!Character.isLetter(trimmed.charAt(4))) {
	    throw new Exception("Outcome move positions are invalid");
	 } else {
	    return trimmed.substring(4, trimmed.indexOf(")"));
	 }
      } else {
	 return trimmed.substring(3, trimmed.indexOf(")"));
      }
   }

   /**
    * Gets the comparing string from the outcome command
    * e.g >, <, =
    * @param command
    * @return
    */
   public static String getOutcomeCompare(String command) {
      String trimmed = command.replaceAll(SyntaxGrammar.WHITESPACE, "");
      if ('(' == trimmed.charAt(8)) {
	 return "";
      } else {
	 return String.valueOf(trimmed.charAt(8));
      }
   }

   /**
    * Gets the move done from the outcome command
    * @param command
    * @return
    */
   public static String getOutcomeMove(String command) {
      int firstParenthesis = command.indexOf("(");
      int secondParenthesis = command.indexOf("(", firstParenthesis + 1);
      String move = command
	    .substring(secondParenthesis, command.indexOf(")") + 1);
      return move;
   }

   /**
    * Gets the player whose flag position is revealed in flag command
    * @param command
    * @return
    * @throws Exception
    */
   public static int getFlagPlayerType(String command) throws Exception {
      String trimmed = command.replaceAll(SyntaxGrammar.WHITESPACE, "");
      int which;
      which = Integer.parseInt(trimmed.substring(5, 6));
      return which;
   }

   /**
    * Gets the flag position from the flag command
    * @param command
    * @return
    */
   public static String getFlagPosition(String command) {
      int firstSpace = command.indexOf(" ");
      int secondSpace = command.indexOf(" ", firstSpace + 1);
      int firstParanthesis = command.indexOf(")");
      String position = command.substring(secondSpace + 1, firstParanthesis);
      return position;
   }
}
