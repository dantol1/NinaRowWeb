import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Game implements Serializable {

    public enum GameState {
        GameWin,
        GameTie,
        SeveralPlayersWonTie,
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
    private History moveHistory;

    public HashSet<Player> getWinningPlayers() {
        return winningPlayers;
    }

    HashSet<Player> winningPlayers = new HashSet<>();

    public Player getPlayerByIndex(int i) throws ArrayIndexOutOfBoundsException {

        if (i>players.length) {

            throw new ArrayIndexOutOfBoundsException();
        }
        else {

            return players[i];

        }
    }

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public void setActivePlayerIndex(int activePlayerIndex) {
        this.activePlayerIndex = activePlayerIndex % numOfActivePlayers;
    }

    private int activePlayerIndex = 0;
    private boolean ended = false;

    public Game(GameSettings gs)
    {
        gameBoard = new Board(gs.getColumns(), gs.getRows());
        settings = gs;
        if(gs.getGameType() == GameSettings.GameType.Basic)
        {
            numOfActivePlayers = 2;
        }

        moveHistory = new History();
        players = new Player[numOfActivePlayers];

        for (int i = 0; i<numOfActivePlayers; i++) {
            players[i] = new Player();
        }


    }

    public void saveGame(String fileName) throws IOException
    {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(this);
            out.flush();
        }
        catch (IOException e) {

            throw e;
        }
    }


    public void loadGame(String fileName) throws ClassNotFoundException, IOException {

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));

        this.gameBoard = ((Game)in.readObject()).gameBoard;
        this.moveHistory = ((Game)in.readObject()).moveHistory;
        this.settings = ((Game)in.readObject()).settings;
        this.players = ((Game)in.readObject()).players;
        this.numOfActivePlayers = ((Game)in.readObject()).numOfActivePlayers;
    }

    public char[][] returnBoard()
    {
        return gameBoard.getBoard();
    }

    public boolean dropDisc (int column){

        return gameBoard.dropDisc(column,players[activePlayerIndex].getPieceShape());
    }

    public boolean popoutDisc(int column){
        return gameBoard.popoutDisc(column);
    }

    public GameState isGameEnded(int column){

        boolean gameEnded = false;
        GameState state = null;

        if(settings.getVariant() == GameSettings.Variant.Regular
                || settings.getVariant() == GameSettings.Variant.Popout)
        {
            gameWinChecker gameWinCheck = new gameWinChecker() {
                @Override
                public void run() {
                    this.setFoundSequence(
                            checkAllDirectionsForWinRegularOrPopout(this.getColumn()));
                }
            };
            gameEnded = checkWin(gameWinCheck, column);
        }
        else if(settings.getVariant() == GameSettings.Variant.Circular)
        {
            gameWinChecker gameWinCheck = new gameWinChecker() {
                @Override
                public void run() {
                    this.setFoundSequence(
                            checkAllDirectionsForWinCircular(this.getColumn(), this.getRow()));
                }
            };

            gameEnded = checkWin(gameWinCheck, column);
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
            if(winningPlayers.size() == 1)
                state = GameState.GameWin;
            else
                state = GameState.SeveralPlayersWonTie;

        }

        changeToNextActivePlayer();
        return state;
    }

    private boolean checkWin(gameWinChecker winConditionsChecker, int column)
    {
        boolean gameEnded = false;

        //check all the column for created sequence
        winConditionsChecker.setColumn(column);
        for(int i = 0; i < settings.getRows(); i++)
        {
            winConditionsChecker.setRow(i);
            winConditionsChecker.run();
            if(winConditionsChecker.isFoundSequence())
            {
                gameEnded = true;
                findWinningPlayers(i, column);
            }
        }

        //check all the row for created sequence
        winConditionsChecker.setRow(gameBoard.getNextPlaceInColumn()[column] + 1);
        for(int i = 0; i < settings.getColumns(); i++)
        {
            winConditionsChecker.setColumn(i);
            winConditionsChecker.run();
            if(winConditionsChecker.isFoundSequence())
            {
                gameEnded = true;
                findWinningPlayers(gameBoard.getNextPlaceInColumn()[column] + 1, i);
            }
        }

        return gameEnded;
    }

    private void findWinningPlayers(int row, int column)
    {
        for(Player p : this.players)
        {
            if(p.getPieceShape() == this.gameBoard.getCellSymbol(row, column))
            {
                winningPlayers.add(p);
                break;
            }
        }
    }

    private boolean checkAllDirectionsForWinRegularOrPopout(int column)
    {
        boolean gameEnded = false;

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

        return gameEnded;
    }

    private boolean checkAllDirectionsForWinCircular(int column, int row)
    {
        boolean gameEnded = false;

        if (checkConsecutiveDirection(DiscDirection.Down,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.Left,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.Up,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.Right,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalLeft,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalLeft,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalRight,column, row) == true)
        {
            gameEnded = true;
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalRight,column, row) == true)
        {
            gameEnded = true;
        }

        return gameEnded;
    }

    public GameSettings getSettings() {
        return settings;
    }

    private boolean checkConsecutiveDirection(DiscDirection dir, int column) {
        boolean result = false;
        int row = (gameBoard.getNextPlaceInColumn())[column]+1;
        int rowMovement = 0, colMovement = 0;
        int i;
        movement moveChange = getMovementByDirection(dir);

        rowMovement = moveChange.rowMovement;
        colMovement = moveChange.colMovement;

        for (i = 0; i < settings.getTarget(); i++) {
            try {
                if (gameBoard.getCellSymbol(row, column) == players[activePlayerIndex].getPieceShape()) {
                    row += rowMovement;
                    column += colMovement;
                }
                else {
                    break;
                }
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                break;
            }
        }
        if(i == settings.getTarget())
        {
            result = true;
        }

        return result;
    }

    private boolean checkConsecutiveDirection(DiscDirection dir, int column, int row) {
        boolean result = false;
        int rowMovement = 0, colMovement = 0;
        int i;
        movement moveChange = getMovementByDirection(dir);

        rowMovement = moveChange.rowMovement;
        colMovement = moveChange.colMovement;

        for (i = 0; i < settings.getTarget(); i++) {
            try {
                if (gameBoard.getCellSymbol(row, column) == players[activePlayerIndex].getPieceShape()) {
                    row += rowMovement;
                    column += colMovement;
                    row = row % settings.getRows();
                    column = column % settings.getColumns();
                    if(row < 0)
                        row = settings.getRows() - 1;
                    if(column < 0)
                        column = settings.getColumns() - 1;
                }
                else {
                    break;
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                break;
            }
        }
        if(i == settings.getTarget())
        {
            result = true;
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

    public boolean playTurn(int columnToPlaceDisc)
    {
        boolean succeeded = true;

        succeeded = gameBoard.dropDisc(columnToPlaceDisc, players[activePlayerIndex].getPieceShape());
        if(succeeded)
        {
            endOfTurnActions(columnToPlaceDisc);
        }

        return succeeded;
    }

    public boolean playComputerTurn()
    {
        boolean succeeded = false;
        Random randomCol = new Random();
        int columnToPlaceDisc;

        do {
            columnToPlaceDisc = randomCol.nextInt(settings.getColumns());
            succeeded = gameBoard.dropDisc(columnToPlaceDisc, players[activePlayerIndex].getPieceShape());
        }while(!succeeded);

        endOfTurnActions(columnToPlaceDisc);

        return succeeded;
    }

    private void endOfTurnActions(int columnInWhichDiscWasPut)
    {
        moveHistory.AddMoveToHistory(activePlayerIndex, columnInWhichDiscWasPut);
        players[activePlayerIndex].playedTurn();
    }

    private void changeToNextActivePlayer()
    {
        activePlayerIndex = (activePlayerIndex + 1) % numOfActivePlayers;
    }

    public void undoMove() throws NoMovesMadeException
    {
        Move moveToUndo;
        int columnToRemoveFrom;
        int rowToRemoveFrom;

        if(!moveHistory.showHistory().isEmpty())
        {
            activePlayerIndex--;
            if (activePlayerIndex < 0) {//it was the first player's turn so we give the turn to the last player.
                activePlayerIndex = numOfActivePlayers - 1;
            }

            moveToUndo = moveHistory.showHistory().getLast();
            moveHistory.removeFromHistory();
            columnToRemoveFrom = moveToUndo.getColumnIndex();
            rowToRemoveFrom = gameBoard.getNextPlaceInColumn()[columnToRemoveFrom] + 1;
            gameBoard.removeDisc(columnToRemoveFrom, rowToRemoveFrom, players[activePlayerIndex].getPieceShape());
            players[activePlayerIndex].undidTurn();
        }
        else
        {
            throw new NoMovesMadeException();
        }
    }

    public History getMoveHistory() {
        return moveHistory;
    }
}
