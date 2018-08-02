public class Player {
    public enum Type{
        Human,
        Computer
    }

    private String name;
    private String id;

    public char getPieceShape() {
        return pieceShape;
    }

    private char pieceShape;
    private Type playerType;

    public void setPlayer(String i_name, char i_pieceShape, Type i_isComputer) {
        name = i_name;
        pieceShape = i_pieceShape;
        playerType = i_isComputer;

    }

    public String getName(){

        return name;

    }
}

