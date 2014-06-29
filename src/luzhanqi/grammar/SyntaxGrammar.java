package luzhanqi.grammar;

public class SyntaxGrammar {
   public static final String WHITESPACE_CHARS = "" + "\\u0009" // CHARACTER
	 // TABULATION
	 + "\\u000A" // LINE FEED (LF)
	 + "\\u000B" // LINE TABULATION
	 + "\\u000C" // FORM FEED (FF)
	 + "\\u000D" // CARRIAGE RETURN (CR)
	 + "\\u0020" // SPACE
	 + "\\u0085" // NEXT LINE (NEL)
	 + "\\u00A0" // NO-BREAK SPACE
	 + "\\u1680" // OGHAM SPACE MARK
	 + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
	 + "\\u2000" // EN QUAD
	 + "\\u2001" // EM QUAD
	 + "\\u2002" // EN SPACE
	 + "\\u2003" // EM SPACE
	 + "\\u2004" // THREE-PER-EM SPACE
	 + "\\u2005" // FOUR-PER-EM SPACE
	 + "\\u2006" // SIX-PER-EM SPACE
	 + "\\u2007" // FIGURE SPACE
	 + "\\u2008" // PUNCTUATION SPACE
	 + "\\u2009" // THIN SPACE
	 + "\\u200A" // HAIR SPACE
	 + "\\u2028" // LINE SEPARATOR
	 + "\\u2029" // PARAGRAPH SEPARATOR
	 + "\\u202F" // NARROW NO-BREAK SPACE
	 + "\\u205F" // MEDIUM MATHEMATICAL SPACE
	 + "\\u3000"; // IDEOGRAPHIC SPACE
   public static final String WHITESPACE = "[" + WHITESPACE_CHARS + "]*";
   public static final String OR = "|";
   public static final String DIGIT = "[0-9]";
   public static final String DIGITS = "(" + DIGIT + ")+";
   public static final String POSITION = "[A-E](1[0-2]" + OR + "[1-9])";
   public static final String PIECE = "(F" + OR + "L" + OR + "B" + OR
	 + "[1-9])";
   public static final String MOVE = "(\\(" + WHITESPACE + POSITION
	 + WHITESPACE + POSITION + WHITESPACE + "\\))";
   public static final String SECONDS = "((" + DIGITS + ")+(" + WHITESPACE
	 + "[/.]" + WHITESPACE + "(" + DIGITS + ")+)?)";
   public static final String WHICH = "[1" + OR + "2]";
   public static final String WINNER = "([0]" + OR + WHICH + ")";
   public static final String COMPARE = "(<" + OR + ">" + OR + "=)";
   public static final String RESULT = "((" + MOVE + ")" + OR + "("
	 + WHITESPACE + COMPARE + WHITESPACE + MOVE + "))";
   public static final String FLAG = "(\\(" + WHITESPACE + "flag"
	 + WHITESPACE + WHICH + WHITESPACE + POSITION + WHITESPACE + "\\))";
   public static final String OUTCOME = "(\\(" + WHITESPACE + "outcome"
	 + WHITESPACE + RESULT + WHITESPACE + "\\))";
   public static final String TURN = "(\\(" + WHITESPACE + "go" + WHITESPACE
	 + WHICH + WHITESPACE + "\\))";
   public static final String ILLEGAL = "(\\(" + WHITESPACE + "illegal"
	 + WHITESPACE + MOVE + WHITESPACE + "\\))";
   public static final String GAMEEND = "(\\(" + WHITESPACE + "end"
	 + WHITESPACE + WINNER + WHITESPACE + "\\))";
   public static final String MSG0 = "\\(" + WHITESPACE + "init" + WHITESPACE
	 + WHICH + WHITESPACE + "time/move" + WHITESPACE + SECONDS
	 + WHITESPACE + "\\)";
   public static final String MSGR2P = "(" + MSG0 + OR + GAMEEND + OR
	 + ILLEGAL + OR + TURN + OR + OUTCOME + OR + FLAG + ")";
}
