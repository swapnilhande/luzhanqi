package luzhanqi.game;

public class Square {

    // Rank of the piece on this square
    private int piece;
    // Owner of this piece
    private int owner;

    // Should only be called for initializing empty pieces
    public Square() {
        this.piece = Constants.PIECE_EMPTY;
        // Empty place has no owner
        this.owner = Constants.NO_ONE;
    }

    public Square(int piece, int owner) {
        this.piece = piece;
        this.owner = owner;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

}
