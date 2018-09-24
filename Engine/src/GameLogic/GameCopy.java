package GameLogic;

import Exceptions.NoMovesMadeException;

public class GameCopy{

    public Game getGameCopy() {
        return gameCopy;
    }

    public void setGameCopy(Game gameCopy) {
        this.gameCopy = gameCopy;
    }

    private Game gameCopy;

    public History getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(History moveHistory) {
        this.moveHistory = moveHistory;
    }

    private History moveHistory;

    public char[][] getBoard() {

        return gameCopy.returnBoard();
    }
    private int lastMoveSeenIndex = 0;

    public int getLastMoveSeenIndex(){
        return lastMoveSeenIndex;
    }

    public Move PlayMoveFromHistory()
    {
        Move moveToPlay = moveHistory.showHistory().get(lastMoveSeenIndex);

        if(moveToPlay.getType() == Move.moveType.POPIN)
        {
            gameCopy.playTurn(moveToPlay.getColumnIndex());
        }
        else if(moveToPlay.getType() == Move.moveType.POPOUT)
        {
            gameCopy.popoutDisc(moveToPlay.getColumnIndex());
            gameCopy.getPlayers().get(moveToPlay.getPlayerIndex()).playedTurn();
        }

        gameCopy.changeToNextActivePlayer();
        lastMoveSeenIndex++;

        return moveToPlay;
    }

    public Move UndoMoveFromHistory()
    {

        try
        {
            gameCopy.undoMove();
        }
        catch(NoMovesMadeException e)
        {

        }

        lastMoveSeenIndex--;
        Move undoMove = moveHistory.showHistory().get(lastMoveSeenIndex);

        return undoMove;
    }
}
