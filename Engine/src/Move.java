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
    private int rowIndex;
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

    public int getRowIndex() { return rowIndex; }

    public Move(int i_playerIndex,int i_rowIndex, int i_columnIndex, moveType i_MoveType) {

        playerIndex = i_playerIndex;
        columnIndex = i_columnIndex;
        rowIndex = i_rowIndex;
        moveNum++;
        specificMoveNum = moveNum;
        type = i_MoveType;
    }

}

