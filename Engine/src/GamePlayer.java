import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Objects;

public class GamePlayer implements Serializable {
    public enum Type{
        Human,
        Computer
    }

    private String name;

    public Type getPlayerType() {
        return playerType;
    }

    public short getId() {
        return id;
    }

    private short id;
    private int howManyTurnsPlayed = 0;
    private Color playerColor;
    private char pieceShape;
    private Type playerType;

    public void setPlayer(String i_name, char i_pieceShape, Type i_isComputer) {
        name = i_name;
        pieceShape = i_pieceShape;
        playerType = i_isComputer;

    }

    public GamePlayer(short id, String name, Type type, Color color)
    {
        this.id = id;
        this.name = name;
        this.playerType = type;
        this.playerColor = color;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
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
        GamePlayer gamePlayer = (GamePlayer) o;
        return pieceShape == gamePlayer.pieceShape &&
                Objects.equals(name, gamePlayer.name) &&
                Objects.equals(id, gamePlayer.id);
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

