import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    public enum Type{
        Human,
        Computer
    }

    private String name;
    private String id;
    private int howManyTurnsPlayed = 0;
    private Color PlayerColor;
    private char pieceShape;
    private Type playerType;

    public void setPlayer(String i_name, char i_pieceShape, Type i_isComputer) {
        name = i_name;
        pieceShape = i_pieceShape;
        playerType = i_isComputer;

    }

    public Color getPlayerColor() {
        return PlayerColor;
    }

    public void setPlayerColor(Color playerColor) {
        PlayerColor = playerColor;
    }

    public char getPieceShape() {
        return pieceShape;
    }

    public boolean isComputer() {

        if (playerType == Type.Computer) {

            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return pieceShape == player.pieceShape &&
                Objects.equals(name, player.name) &&
                Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, pieceShape);
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

