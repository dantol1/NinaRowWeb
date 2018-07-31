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
    private class movement{
        int rowMovement;
        int colMovement;

        public movement(int rowMovement, int colMovement) {
            this.rowMovement = rowMovement;
            this.colMovement = colMovement;
        }
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

    public GameSettings getSettings() {
        return settings;
    }

    private boolean checkConsecutiveDirection(DiscDirection dir, int column) {
        //TODO: Check if its within boundries of the board
        boolean result = false;
        int row = (gameBoard.getNextPlaceInColumn())[column];
        int rowMovement = 0, colMovement = 0;
        int i;
        movement moveChange = getMovementByDirection(dir);

        rowMovement = moveChange.rowMovement;
        colMovement = moveChange.colMovement;

        if (settings.getVariant() == GameSettings.Variant.Regular
                || settings.getVariant() == GameSettings.Variant.Popout) {

           for (i= 0; i<settings.getTarget(); i++) {
             if (gameBoard.getCellSymbol(row,column) == players[activePlayerIndex].getPieceShape()) {
                 row += rowMovement;
                 column += colMovement;
             }
             else {
                 break;
             }
           }
           if(i == settings.getTarget())
           {
               result = true;
           }
        }
        else if(settings.getVariant() == GameSettings.Variant.Circular)
        {
            for (i= 0; i<settings.getTarget(); i++) {
                if (gameBoard.getCellSymbol(row,column) == players[activePlayerIndex].getPieceShape()) {
                    row += rowMovement;
                    column += colMovement;
                    row = row % settings.getRows();
                    column = column % settings.getColumns();
                }
                else {
                    break;
                }
            }
            if(i == settings.getTarget())
            {
                result = true;
            }
        }

        return result;
    }

    private movement getMovementByDirection(DiscDirection dir)
    {
        int rowMovement = 0, colMovement = 0;

        if(dir == DiscDirection.Down)
        {
            rowMovement = 1;
            colMovement = 0;
        }
        else if (dir == DiscDirection.Up)
        {
            rowMovement = -1;
            colMovement = 0;
        }
        else if (dir == DiscDirection.Right)
        {
            rowMovement = 0;
            colMovement = 1;
        }
        else if (dir == DiscDirection.Left)
        {
            rowMovement = 0;
            colMovement = -1;
        }
        else if (dir == DiscDirection.UpperDiagonalLeft)
        {
            rowMovement = -1;
            colMovement = -1;
        }
        else if (dir == DiscDirection.UpperDiagonalRight)
        {
            rowMovement = -1;
            colMovement = 1;
        }
        else if (dir == DiscDirection.LowerDiagonalLeft)
        {
            rowMovement = 1;
            colMovement = -1;
        }
        else if (dir == DiscDirection.LowerDiagonalRight)
        {
            rowMovement = 1;
            colMovement = 1;
        }

        return new movement(rowMovement, colMovement);
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
