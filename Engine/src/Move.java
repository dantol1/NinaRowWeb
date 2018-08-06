import java.io.Serializable;

public class Move implements Serializable {

    private int playerIndex;
    private int specificMoveNum;
    private int columnIndex;
    private static int moveNum = 0;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getSpecificMoveNum() {
        return specificMoveNum;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public Move(int i_playerIndex, int i_columnIndex) {

        playerIndex = i_playerIndex;
        columnIndex = i_columnIndex;
        moveNum++;
        specificMoveNum = moveNum;
    }

}

