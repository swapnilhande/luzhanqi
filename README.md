# README #

[Luzhanqi](http://en.wikipedia.org/wiki/Luzhanqi) implementation in Java. The program parses referee programs commands, maintains board state and evaluates the best possible move to win the game.  
The program works on the referee programs command on standard input. 
***
##### Message by game-playing program to the referee  #####
    - initial configuration  
    - move  
    - resign  
    
    Syntax:
    <mg_p2r>   ::=  <initial>  
                |  <move>  
                |  <resign>  
    <initial>   ::=  ( <inits> )
    <inits>     ::=  
                |  <init> <inits>
    <init>      ::=  ( <position> <piece> )
    <position>  ::=  A1 | A2 | A3 | A4 | A5 | A6 | A7 | A8 | A9 | A10 | A11 | A12
                |  B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B11 | B12
                |  C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8 | C9 | C10 | C11 | C12
                |  D1 | D2 | D3 | D4 | D5 | D6 | D7 | D8 | D9 | D10 | D11 | D12
                |  E1 | E2 | E3 | E4 | E5 | E6 | E7 | E8 | E9 | E10 | E11 | E12
    <piece>     ::=  F | L | B | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
    <move>      ::=  ( <position> <position> )
    <resign>    ::=  (resign)
##### Every kind of message the referee might have to send to a game-playing program #####
    - initial player assignment and time-per-move limit
    - end of game result
    - illegal move
    - result of a move
    - indicate the start of a player's turn
    - location of a player's flag

    Syntax:
    <msg_r2p>   ::=  <msg0>
                |  <game_end>
                |  <illegal>
                |  <turn>
                |  <outcome>
                |  <flag>
    <msg0>      ::=  (init <which> time/move <seconds>)
    <game_end>  ::=  (end <winner>)
    <illegal>   ::=  (illegal <move>)
    <turn>      ::=  (go <which>)
    <outcome>   ::=  (outcome <result>)
    <flag>      ::=  (flag <which> <position>)
    <result>    ::=  <move>
                |  <compare> <move>
     <compare>   ::=  <
                |  >
                |  =
    <winner>    ::=  0
                |  <which>
    <which>     ::=  1
                |  2
    <seconds>   ::=  <digits>
                |  <digits> . <digits>
    <digits>    ::=  <digit>
                |  <digit> <digits>
    <digit>     ::=  0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
    <position>  ::=  A1 | A2 | A3 | A4 | A5 | A6 | A7 | A8 | A9 | A10 | A11 | A12
                |  B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B11 | B12
                |  C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8 | C9 | C10 | C11 | C12
                |  D1 | D2 | D3 | D4 | D5 | D6 | D7 | D8 | D9 | D10 | D11 | D12
                |  E1 | E2 | E3 | E4 | E5 | E6 | E7 | E8 | E9 | E10 | E11 | E12
    <piece>     ::=  F | L | B | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
    <move>      ::=  ( <position> <position> )

##### Help #####

    Symbol      Purpose
    ------      -------
    <initial>   send the player's initial board config to the referee
    <move>      send the player's move to the referee
    <resign>    the player forfeits the game
    <msg0>      assign a player number and the time-per-move limit
    <game_end>  indicate that the game is over, along with who won
    <illegal>   indicate that the last received move is not valid
    <turn>      tell the player it is their turn to move
    <outcome>   indicate the outcome of the last move
    <flag>      reveals the position of a player's flag
