package GameLogic;

public class PopoutMoveComplex {

    private char pieceShape;
    private boolean succeeded;

    public PopoutMoveComplex(char shape, boolean ifsucceeded){
        pieceShape = shape;
        succeeded = ifsucceeded;
    }

    public char getPieceShape() {
        return pieceShape;
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
