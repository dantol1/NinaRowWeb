package GameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.List;

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
    private IntegerProperty howManyTurnsPlayed = new SimpleIntegerProperty(0);
    private Color playerColor;
    private char pieceShape;
    private Type playerType;
    private static char pieceShapeChar = 'a';

    public void setPlayer(String i_name, char i_pieceShape, Type i_isComputer) {
        name = i_name;
        pieceShape = i_pieceShape;
        playerType = i_isComputer;
    }
    public GamePlayer(String name, boolean isComputer) {
        this.playerType = Type.Human;
        if (isComputer == true)
        {
           this.playerType = Type.Computer;
        }
        this.name = name;
        this.pieceShape = GamePlayer.pieceShapeChar;
        GamePlayer.pieceShapeChar++;
    }


    public GamePlayer(short id, String name, Type type, Color color)
    {
        this.id = id;
        this.name = name;
        this.playerType = type;
        this.playerColor = color;
        this.pieceShape = GamePlayer.pieceShapeChar;
        GamePlayer.pieceShapeChar++;
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

    public String getName(){

        return name;

    }

    public int getHowManyTurnsPlayed() {
        return howManyTurnsPlayed.intValue();
    }
    public IntegerProperty getHowManyTurnsPlayedProperty() {
        return howManyTurnsPlayed;
    }

    public void setHowManyTurnsPlayed(int howManyTurnsPlayed) {
        this.howManyTurnsPlayed.setValue(howManyTurnsPlayed);
    }

    public void playedTurn(){ howManyTurnsPlayed.setValue(howManyTurnsPlayed.intValue() + 1); }
    public void undidTurn(){ howManyTurnsPlayed.setValue(howManyTurnsPlayed.intValue() - 1); }


    public static GamePlayer[] Copy(List<GamePlayer> thePlayers) {
        GamePlayer[] copiedPlayers = new GamePlayer[thePlayers.size()];


        for(int i = 0; i < thePlayers.size(); i++)
        {
            copiedPlayers[i] = thePlayers.get(i);
        }

        return copiedPlayers;
    }
}

