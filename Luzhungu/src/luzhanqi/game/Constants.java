package luzhanqi.game;

public class Constants {
   
   // Logistics
   public static final boolean LOGGING_ENABLED = false;
   
   // Players
   public static final int PLAYER_A = 1;
   public static final int PLAYER_B = 2;
   public static final int NO_ONE = 0;
   
   // Pieces
   public static final int PIECE_FIELDMARSHAL = 13;
   public static final int PIECE_GENERAL = 12;
   public static final int PIECE_MAJORGENERAL = 11;
   public static final int PIECE_BRIGADIERGENERAL = 10;
   public static final int PIECE_COLONEL = 9;
   public static final int PIECE_MAJOR = 8;
   public static final int PIECE_CAPTAIN = 7;
   public static final int PIECE_LIEUTENANT = 6;
   public static final int PIECE_ENGINEER = 5;
   public static final int PIECE_BOMB = 4;
   public static final int PIECE_LANDMINE = 3;
   public static final int PIECE_FLAG = 2;
   public static final int PIECE_UNKNOWN = 1;
   public static final int PIECE_EMPTY = 0;
   
   // Outcomes
   public static final int RESULT_NONE = 0;
   public static final int RESULT_WON = 1;
   public static final int RESULT_DEFEATED = 2;
   public static final int RESULT_DRAW = 3;
   
   // Directions
   public static final int POSSIBLE_DIRECTIONS = 8;
   public static final int TOP = 0;
   public static final int RIGHT = 1;
   public static final int DOWN = 2;
   public static final int LEFT = 3;
   public static final int TOPLEFT = 4;
   public static final int TOPRIGHT = 5;
   public static final int DOWNLEFT = 6;
   public static final int DOWNRIGHT = 7;
   
   // Board
   public static final int BOARD_SIZE = 60;
   public static final int PLAYER_A_START = 0;
   public static final int PLAYER_A_END = 29;
   public static final int PLAYER_B_START = 30;
   public static final int PLAYER_B_END = 59;
   
   // Weight params
   public static final int FIRST_MOVE = 0;
   public static final int ATTACK_UNKNOWN_OPPONENT = 1;
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
   public static final int APPROACH_ENEMY_FLAG = 18;
   public static final int APPROACH_ENEMY_SAFE_ZONES = 19;
   public static final int APPROACH_OUR_SAFE_ZONES = 20;
   public static final int DEFUSE_MINE = 20;
   public static final int PROTECT_BASE = 21;
   public static final int CHEAP_PATRIOT = 22;
   public static final int BRAVE_PATRIOT = 23;
   public static final int KILL_INTRUDER = 24;
   public static final int OPTION_PARAM_MULTIPLIER = 4;

}
