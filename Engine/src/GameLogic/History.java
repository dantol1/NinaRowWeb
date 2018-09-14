package GameLogic;

import java.io.Serializable;
import java.util.LinkedList;

public class History implements Serializable {

    private LinkedList<Move> moveList = new LinkedList<>();

    public void AddMoveToHistory(int i_PlayerIndex,int i_RowIndex, int i_ColumnIndex, Move.moveType i_MoveType) {

        moveList.add(new Move(i_PlayerIndex, i_RowIndex, i_ColumnIndex, i_MoveType));
    }
    public void AddMoveToHistory(int i_PlayerIndex,int i_RowIndex, int i_ColumnIndex, Move.moveType i_MoveType, int playerIndexPopped) {

        moveList.add(new Move(i_PlayerIndex, i_RowIndex, i_ColumnIndex, i_MoveType, playerIndexPopped));
    }

    public LinkedList<Move> showHistory(){

        return moveList;
    }

    public void removeFromHistory(Move moveToRemove)
    {
        moveList.remove(moveToRemove);
    }

    public void removeFromHistory()
    {
        //overload that will remove the last move from history - in order to improve performance.
        moveList.removeLast();
    }

    public static History CopyHistory(History copyMoveHistory) {
        History copiedHistory = new History();

        for(Move mv : copyMoveHistory.moveList)
        {
            if(mv.getType() == Move.moveType.POPOUT)
            {
                copiedHistory.AddMoveToHistory(mv.getPlayerIndex(), mv.getRowIndex(), mv.getColumnIndex(), mv.getType(), mv.getPlayerIndexDiscThatWasPopped());
            }
            else
            {
                copiedHistory.AddMoveToHistory(mv.getPlayerIndex(), mv.getRowIndex(), mv.getColumnIndex(), mv.getType());
            }
        }

        return copiedHistory;
    }

}
