public class Player {

    private String name;
    private char pieceShape;
    private boolean isComputer = false;

    public Player(String i_name, char i_pieceShape, boolean i_isComputer) {
        name = i_name;
        pieceShape = i_pieceShape;
        isComputer = i_isComputer;
    }

    public String getName(){

        return name;

    }
}

