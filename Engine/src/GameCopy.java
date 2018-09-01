public class GameCopy{

    public Game getGameCopy() {
        return gameCopy;
    }

    public void setGameCopy(Game gameCopy) {
        this.gameCopy = gameCopy;
    }

    Game gameCopy;

    public History getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(History moveHistory) {
        this.moveHistory = moveHistory;
    }

    History moveHistory;

    int lastMoveSeenIndex = 0;

    public void PlayMoveFromHistory()
    {
        Move moveToPlay = moveHistory.showHistory().get(lastMoveSeenIndex);

        if(moveToPlay.getType() == Move.moveType.POPIN)
        {
            gameCopy.dropDisc(moveToPlay.getColumnIndex());
        }
        else if(moveToPlay.getType() == Move.moveType.POPOUT)
        {
            gameCopy.popoutDisc(moveToPlay.getColumnIndex());
        }

        lastMoveSeenIndex++;
    }

    public void UndoMoveFromHistory()
    {
        try
        {
            gameCopy.undoMove();
        }
        catch(NoMovesMadeException e)
        {

        }

        lastMoveSeenIndex--;
    }
}
