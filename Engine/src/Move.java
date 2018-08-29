import java.io.Serializable;

public class Move implements Serializable {
    public enum moveType{
        POPIN,
        POPOUT,
        RETIRE
    }

    private int playerIndex;
    private int specificMoveNum;
    private int columnIndex;
    private static int moveNum = 0;
    private moveType type;

    public moveType getType() {
        return type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getSpecificMoveNum() {
        return specificMoveNum;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public Move(int i_playerIndex, int i_columnIndex, moveType i_MoveType) {

        playerIndex = i_playerIndex;
        columnIndex = i_columnIndex;
        moveNum++;
        specificMoveNum = moveNum;
        type = i_MoveType;
    }

}

