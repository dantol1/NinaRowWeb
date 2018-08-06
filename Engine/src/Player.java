public class Player {
    public enum Type{
        Human,
        Computer
    }

    private String name;
    private String id;
    private int howManyTurnsPlayed = 0;

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

    public boolean isComputer() {

        if (playerType == Type.Computer) {

            return true;
        }

        return false;
    }

    public String getName(){

        return name;

    }

    public int getHowManyTurnsPlayed() {
        return howManyTurnsPlayed;
    }

    public void setHowManyTurnsPlayed(int howManyTurnsPlayed) {
        this.howManyTurnsPlayed = howManyTurnsPlayed;
    }

    public void playedTurn(){ howManyTurnsPlayed++; }
    public void undidTurn(){ howManyTurnsPlayed--; }
}

