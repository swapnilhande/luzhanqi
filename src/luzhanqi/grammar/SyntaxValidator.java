package luzhanqi.grammar;

/**
 * Contains methods that just check if syntax is valid as per grammar
 *
 */
public class SyntaxValidator {

    public static boolean isMsg0SyntaxValid(String input) {
	return input.matches(SyntaxGrammar.MSG0);
    }

    public static boolean isWhitespace(String input) {
	return input.matches(SyntaxGrammar.WHITESPACE);
    }

    public static boolean isDigit(String input) {
	return input.matches(SyntaxGrammar.DIGIT);
    }

    public static boolean isDigits(String input) {
	return input.matches(SyntaxGrammar.DIGITS);
    }

    public static boolean isPosition(String input) {
	return input.matches(SyntaxGrammar.POSITION);
    }

    public static boolean isPiece(String input) {
	return input.matches(SyntaxGrammar.PIECE);
    }

    public static boolean isMove(String input) {
	return input.matches(SyntaxGrammar.MOVE);
    }

    public static boolean isSeconds(String input) {
	return input.matches(SyntaxGrammar.SECONDS);
    }

    public static boolean isWhich(String input) {
	return input.matches(SyntaxGrammar.WHICH);
    }

    public static boolean isWinner(String input) {
	return input.matches(SyntaxGrammar.WINNER);
    }

    public static boolean isCompare(String input) {
	return input.matches(SyntaxGrammar.COMPARE);
    }

    public static boolean isResult(String input) {
	return input.matches(SyntaxGrammar.RESULT);
    }

    public static boolean isFlag(String input) {
	return input.matches(SyntaxGrammar.FLAG);
    }

    public static boolean isOutcome(String input) {
	return input.matches(SyntaxGrammar.OUTCOME);
    }

    public static boolean isTurn(String input) {
	return input.matches(SyntaxGrammar.TURN);
    }

    public static boolean isIllegal(String input) {
	return input.matches(SyntaxGrammar.ILLEGAL);
    }

    public static boolean isGameEnd(String input) {
	return input.matches(SyntaxGrammar.GAMEEND);
    }

    public static boolean isMsgR2p(String input) {
	return input.matches(SyntaxGrammar.MSGR2P);
    }

}
