import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Game implements Serializable {

    public enum GameState {
        GameWin,
        GameTie,
        GameNotEnded
    }

    public enum DiscDirection {

        UpperDiagonalLeft,
        LowerDiagonalLeft,
        UpperDiagonalRight,
        LowerDiagonalRight,
        Up,
        Down,
        Left,
        Right
    }
    private Board gameBoard;
    private GameSettings settings;
    private Player players[];
    private int numOfActivePlayers;

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public void setActivePlayerIndex(int activePlayerIndex) {
        this.activePlayerIndex = activePlayerIndex % numOfActivePlayers;
    }

    private int activePlayerIndex;
    private boolean ended = false;

    public Game(GameSettings gs)
    {
        gameBoard = new Board(gs.getColumns(), gs.getRows());
        settings = gs;
        if(gs.getGameType() == GameSettings.GameType.Basic)
        {
            numOfActivePlayers = 2;
        }

        players = new Player[numOfActivePlayers];
    }

    public void saveGame(String location) throws IOException
    {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(location))) {
            out.writeObject(this);
            out.flush();
        }
    }

    public char[][] returnBoard()
    {
        return gameBoard.getBoard();
    }

    public boolean dropDisc (int column){

        return gameBoard.dropDisc(column,players[activePlayerIndex].getPieceShape());
    }

    public GameState isGameEnded(int column){

        boolean gameEnded = false;
        GameState state = null;

        if (checkConsecutiveDirection(DiscDirection.Down,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.Left,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.Up,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.Right,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalLeft,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalLeft,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalRight,column) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalRight,column) == true)
        {
            gameEnded = true;
        }

        if (gameEnded == false) {

           if(checkGameTie() == true) {

               state = GameState.GameTie;
           }
           else {

               state = GameState.GameNotEnded;
           }
        }
        else
        {
            state = GameState.GameWin;
        }


        return state;
    }

    private boolean checkConsecutiveDirection(DiscDirection dir, int column) {
        //TODO: check for a general approach
        boolean result = false;
        int startingRow = (gameBoard.getNextPlaceInColumn())[column];
        int row = startingRow;
        int col = column;


        if (settings.getVariant() == GameSettings.Variant.Regular
                || settings.getVariant() == GameSettings.Variant.Popout) {

           for (int i= 0; i<settings.getTarget(); i++) {

               if (dir == DiscDirection.Down) {

                 if (gameBoard.getCellSymbol(row,col) == players[activePlayerIndex].getPieceShape()) {
                     row--;
                 }
                 else {
                     break;
                 }
               }
           }
        }

        return result;
    }

    private boolean checkGameTie() {

        boolean tie = true;

        for (int checkCol : gameBoard.getNextPlaceInColumn()) {

            if (checkCol > -1){

                tie = false;
                break;
            }
        }

        return tie;
    }
}
