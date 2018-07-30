import java.util.LinkedList;

public class History {

    private LinkedList<Move> moveList = new LinkedList<>();

    public void AddMoveToHistory(int i_PlayerIndex, int i_ColumnIndex) {

        moveList.add(new Move(i_PlayerIndex,i_ColumnIndex));

    }

    public LinkedList<Move> showHistory(){

        return moveList;
    }

}
