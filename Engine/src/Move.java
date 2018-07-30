public class Move {

    private int playerIndex;
    private int specificMoveNum;
    private int columnIndex;
    private static int moveNum = 0;

    public Move(int i_playerIndex, int i_columnIndex) {

        playerIndex = i_playerIndex;
        columnIndex = i_columnIndex;
        moveNum++;
        specificMoveNum = moveNum;
    }

}

