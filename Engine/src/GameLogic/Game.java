package GameLogic;

import Exceptions.NoMovesMadeException;
import WebLogic.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import jaxb.schema.generated.DynamicPlayers;
import jaxb.schema.generated.Player;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Game implements Serializable {

    public HashSet<GamePlayer> getWinningPlayers() {
        return winningPlayers;
    }

    private int totalPlayers;

    public int getTotalPlayers(){
        return totalPlayers;
    }

    HashSet<GamePlayer> winningPlayers = new HashSet<>();

    private ArrayList<Color> playerColors = new ArrayList<Color> () {{

        add(Color.BLUE);
        add(Color.RED);
        add(Color.GREEN);
        add(Color.PURPLE);
        add(Color.ORANGE);
        add(Color.BLACK);

    }};

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
    private ColorGenerator colorGenerator = new ColorGenerator();
    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }
    private ArrayList<GamePlayer> players = new ArrayList<>();

    public void setPlayers(ArrayList<GamePlayer> thePlayers) {
        players = thePlayers;
    }
    private int numOfActivePlayers;
    private History moveHistory;
    private HashSet<Point> winningPieces = new HashSet<>();
    private boolean[] retiredPlayersIndexes = new boolean[6];

    public int getTotalMoves() {
        return totalMoves.get();
    }

    public SimpleIntegerProperty totalMovesProperty() {
        return totalMoves;
    }

    private SimpleIntegerProperty totalMoves = new SimpleIntegerProperty(0); //TODO: bind this property to the sum of all the player turns properties.

    public String getGameTitle() {
        return gameTitle;
    }

    private String gameTitle;

    public HashSet<Point> getWinningPieces() {
        return winningPieces;
    }

    public GamePlayer getPlayerByIndex(int i) throws ArrayIndexOutOfBoundsException {

        if (i>players.size()) {

            throw new ArrayIndexOutOfBoundsException();
        }
        else {

            return players.get(i);

        }
    }

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public void setActivePlayerIndex(int activePlayerIndex) {
        this.activePlayerIndex = activePlayerIndex % numOfActivePlayers;
    }

    private int activePlayerIndex = 0;
    private int playersCreated = 0;

    public int getPlayersCreated(){

        return playersCreated;
    }

    public void Restart() {

        gameBoard = new Board(settings.getColumns(), settings.getRows());
        activePlayerIndex = 0;
        playersCreated = 0;
        winningPlayers = new HashSet<>();
        restartRetired();
        colorGenerator = new ColorGenerator();
    }

    private void restartRetired() {
        for(boolean r : retiredPlayersIndexes)
        {
            r = false;
        }
    }

    public Game(GameSettings gs, List<Player> thePlayers)
    {
        gameBoard = new Board(gs.getColumns(), gs.getRows());
        settings = gs;

        if(gs.getGameType() == GameSettings.GameType.Basic)
        {
            numOfActivePlayers = 2;
        }
        else if (gs.getGameType() == GameSettings.GameType.MultiPlayer)
        {
            numOfActivePlayers = thePlayers.size();
        }

        moveHistory = new History();

        for (int i = 0; i<numOfActivePlayers; i++) {
            players.add(new GamePlayer(thePlayers.get(i).getId(), thePlayers.get(i).getName(),
                    GamePlayer.Type.valueOf(thePlayers.get(i).getType()), playerColors.get(i)));

        }

        for(boolean i : retiredPlayersIndexes)
        {
            i = false;
        }
    }

    public Game(GameSettings gs, DynamicPlayers dm)
    {
        gameBoard = new Board(gs.getColumns(), gs.getRows());
        settings = gs;
        totalPlayers = dm.getTotalPlayers();
        moveHistory = new History();
        gameTitle = dm.getGameTitle();

        for(boolean i : retiredPlayersIndexes)
        {
            i = false;
        }
    }

    public Game(GameSettings gs, List<GamePlayer> thePlayers, History copyMoveHistory)
    {
        gameBoard = new Board(gs.getColumns(), gs.getRows());
        settings = gs;

        if(gs.getGameType() == GameSettings.GameType.Basic)
        {
            numOfActivePlayers = 2;
        }
        else if (gs.getGameType() == GameSettings.GameType.MultiPlayer)
        {
            numOfActivePlayers = thePlayers.size();
        }

        moveHistory = new History();

        for(boolean i : retiredPlayersIndexes)
        {
            i = false;
        }
    }

    public char[][] returnBoard()
    {
        return gameBoard.getBoard();
    }

    public DropDiscComplex dropDisc (int column){

        return gameBoard.dropDisc(column,players.get(activePlayerIndex).getPieceShape());
    }

    public boolean popoutDisc(int column)
    {
        PopoutMoveComplex popoutMove;
        if (gameBoard.getBoard()[settings.getRows()-1][column] !=
                players.get(activePlayerIndex).getPieceShape())
        {
            popoutMove = new PopoutMoveComplex('7',false);
        }
        else {
            popoutMove = gameBoard.popoutDisc(column);
        }

        if (popoutMove.isSucceeded())
        {
            int playerIndex = -1;
            Move move;
            for (int i = 0 ; i < players.size(); i++) {

                if (players.get(i).getPieceShape() == popoutMove.getPieceShape()){

                    playerIndex = i;
                }
            }

            move = new Move(activePlayerIndex,settings.getRows()-1,column,
                    Move.moveType.POPOUT, playerIndex);
            moveHistory.showHistory().add(move);
        }

        return popoutMove.isSucceeded();
    }

    public GameState isGameEnded(int column){

        boolean gameEnded = false;
        GameState state = null;

        if(settings.getVariant() == GameSettings.Variant.Regular)
        {
            gameWinChecker gameWinCheck = new gameWinChecker() {
                @Override
                public void run() {
                    this.setFoundSequence(
                            checkAllDirectionsForWinRegular(this.getColumn()));
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
        else if(settings.getVariant() == GameSettings.Variant.Popout)
        {
            gameWinChecker gameWinCheck = new gameWinChecker() {
                @Override
                public void run() {
                    this.setFoundSequence(
                            checkAllDirectionsForWinPopout(this.getColumn(), this.getRow()));
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
            if(winningPlayers.size() == 1) {
                state = GameState.GameWin;
            }
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

            winConditionsChecker.setFoundSequence(false);
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
        for(Point d : this.winningPieces)
        {
            for(GamePlayer p : this.players)
            {
                if(p.getPieceShape() == this.gameBoard.getCellSymbol(d.y, d.x))
                {
                    winningPlayers.add(p);
                }
            }
        }
    }

    private boolean checkAllDirectionsForWinRegular(int column)
    {
        boolean gameEnded = false;

        if (checkConsecutiveDirection(DiscDirection.Down,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Down,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.Left,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Left,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.Up,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Up,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.Right,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Right,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalLeft,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.LowerDiagonalLeft,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalLeft,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.UpperDiagonalLeft,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalRight,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.LowerDiagonalRight,column);
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalRight,column) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.UpperDiagonalRight,column);
        }

        return gameEnded;
    }

    private boolean checkAllDirectionsForWinPopout(int column, int row)
    {
        boolean gameEnded = false;

        if (checkConsecutiveDirectionPopout(DiscDirection.Down,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.Down,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.Left,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.Left,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.Up,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.Up,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.Right,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.Right,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.LowerDiagonalLeft,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.LowerDiagonalLeft,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.UpperDiagonalLeft,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.UpperDiagonalLeft,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.LowerDiagonalRight,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.LowerDiagonalRight,column, row);
        }
        else if (checkConsecutiveDirectionPopout(DiscDirection.UpperDiagonalRight,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSetPopout(DiscDirection.UpperDiagonalRight,column, row);
        }

        return gameEnded;
    }

    private void addWinningPiecesToSet(DiscDirection dir, int column) {
        int target = this.settings.getTarget();
        movement mv = getMovementByDirection(dir);
        int winPieceCol = column, winPieceRow = (gameBoard.getNextPlaceInColumn())[column]+1;

        for(int i = 0; i < target; i++)
        {
            winningPieces.add(new Point(winPieceCol, winPieceRow));
            winPieceCol += mv.colMovement;
            winPieceRow += mv.rowMovement;
        }
    }

    private void addWinningPiecesToSetPopout(DiscDirection dir, int column, int row) {
        int target = this.settings.getTarget();
        movement mv = getMovementByDirection(dir);
        int winPieceCol = column, winPieceRow = row;

        for(int i = 0; i < target; i++)
        {
            winningPieces.add(new Point(winPieceCol, winPieceRow));
            winPieceCol += mv.colMovement;
            winPieceRow += mv.rowMovement;
        }
    }

    private boolean checkAllDirectionsForWinCircular(int column, int row)
    {
        boolean gameEnded = false;

        if (checkConsecutiveDirection(DiscDirection.Down,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Down,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.Left,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Left,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.Up,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Up,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.Right,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.Right,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalLeft,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.LowerDiagonalLeft,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalLeft,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.UpperDiagonalLeft,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.LowerDiagonalRight,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.LowerDiagonalRight,column, row);
        }
        else if (checkConsecutiveDirection(DiscDirection.UpperDiagonalRight,column, row) == true)
        {
            gameEnded = true;
            addWinningPiecesToSet(DiscDirection.UpperDiagonalRight,column, row);
        }

        return gameEnded;
    }

    private void addWinningPiecesToSet(DiscDirection dir, int column, int row) {
        int target = this.settings.getTarget();
        movement mv = getMovementByDirection(dir);
        int winPieceCol = column, winPieceRow = row;

        for(int i = 0; i < target; i++)
        {
            winningPieces.add(new Point(winPieceCol, winPieceRow));
            winPieceCol += mv.colMovement;
            winPieceRow += mv.rowMovement;
            winPieceCol = winPieceCol % settings.getColumns();
            winPieceRow = winPieceRow % settings.getRows();
            if(winPieceRow < 0)
                winPieceRow = settings.getRows() - 1;
            if(winPieceCol < 0)
                winPieceCol = settings.getColumns() - 1;
        }
    }

    public GameSettings getSettings() {
        return settings;
    }


    private boolean checkConsecutiveDirection(DiscDirection dir, int column) {
        boolean result = false;
        int row = (gameBoard.getNextPlaceInColumn())[column]+1;
        int rowMovement = 0, colMovement = 0;
        int i;
        char symbolToCheck;
        movement moveChange = getMovementByDirection(dir);
        try {
            symbolToCheck = gameBoard.getCellSymbol(row, column);
        }
        catch(IndexOutOfBoundsException e)
        {
            return false;
        }

        rowMovement = moveChange.rowMovement;
        colMovement = moveChange.colMovement;

        for (i = 0; i < settings.getTarget(); i++) {
            try {
                if (gameBoard.getCellSymbol(row, column) == symbolToCheck) {
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

    private boolean checkConsecutiveDirectionPopout(DiscDirection dir, int column, int rowToCheck) {
        boolean result = false;
        int row = rowToCheck;
        int rowMovement = 0, colMovement = 0;
        int i;
        char symbolToCheck;
        movement moveChange = getMovementByDirection(dir);
        try {
            symbolToCheck = gameBoard.getCellSymbol(row, column);
        }
        catch(IndexOutOfBoundsException e)
        {
            return false;
        }

        rowMovement = moveChange.rowMovement;
        colMovement = moveChange.colMovement;

        for (i = 0; i < settings.getTarget(); i++) {
            try {
                if (gameBoard.getCellSymbol(row, column) == symbolToCheck && symbolToCheck != Board.EMPTY_CELL) {
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
                if (gameBoard.getCellSymbol(row, column) == players.get(activePlayerIndex).getPieceShape()) {
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

    public DropDiscComplex playDropTurn(int columnToPlaceDisc)
    {
        DropDiscComplex succeeded;

        succeeded = gameBoard.dropDisc(columnToPlaceDisc, players.get(activePlayerIndex).getPieceShape());
        if(succeeded.isDropSucceded())
        {
            endOfTurnActions(getNextPlaceInColumn(columnToPlaceDisc),columnToPlaceDisc, Move.moveType.POPIN);
        }

        return succeeded;
    }

    public boolean playPopTurn(int columnToPlaceDisc)
    {
        boolean succeeded = true;

        succeeded = popoutDisc(columnToPlaceDisc);
        if (succeeded == true) {
             players.get(activePlayerIndex).playedTurn();
         }

        return succeeded;
    }

    public boolean playPopComputerTurn(int column)
    {
        boolean succeeded = popoutDisc(column);
        if (succeeded) {

            players.get(activePlayerIndex).playedTurn();
        }
        return succeeded;
    }

    public DropDiscComplex playDropTurn() {

        DropDiscComplex succeeded;
        Random rand = new Random();
        int randomizedColumn = 0;

        do {

            randomizedColumn = rand.nextInt(settings.getColumns());
            succeeded = gameBoard.dropDisc(randomizedColumn,players.get(activePlayerIndex).getPieceShape());
        } while(!succeeded.isDropSucceded());

        players.get(activePlayerIndex).playedTurn();

        return succeeded;
    }

    public boolean playComputerTurn()
    {
        boolean isDrop = false;
        DropDiscComplex succeeded = null;
        Random randomCol = new Random();
        int columnToPlaceDisc = 0;

        if (settings.getVariant() == GameSettings.Variant.Circular ||
                settings.getVariant() == GameSettings.Variant.Regular  ) {
            do {
                columnToPlaceDisc = randomCol.nextInt(settings.getColumns());
                succeeded = gameBoard.dropDisc(columnToPlaceDisc, players.get(activePlayerIndex).getPieceShape());
            } while (!succeeded.isDropSucceded());
            isDrop = true;
        }
        else if (settings.getVariant() == GameSettings.Variant.Popout) {

            do {

                Move.moveType randomMoveType = Move.moveType.values()[randomCol.nextInt(Move.moveType.values().length)];
                columnToPlaceDisc = randomCol.nextInt(settings.getColumns());

                if (randomMoveType == Move.moveType.POPOUT) {

                    succeeded = new DropDiscComplex(playPopTurn(columnToPlaceDisc), 0, 0);
                    isDrop = true;
                } else if (randomMoveType == Move.moveType.POPIN) {

                    succeeded = playDropTurn();
                    isDrop = false;
                }
                else if (randomMoveType == Move.moveType.RETIRE) {

                    succeeded = new DropDiscComplex(false,0,0);
                    isDrop = false;
                }
            }while(!succeeded.isDropSucceded());
        }

        endOfTurnActions(getNextPlaceInColumn(columnToPlaceDisc), columnToPlaceDisc, Move.moveType.POPIN);

        return isDrop;
    }

    public void endOfTurnActions(int columnInWhichDiscWasPut,int rowInWhichDiscWasPut, Move.moveType i_MoveType)
    {
        if (i_MoveType != Move.moveType.POPOUT) {
            moveHistory.AddMoveToHistory(activePlayerIndex, rowInWhichDiscWasPut, columnInWhichDiscWasPut, i_MoveType);
        }
        players.get(activePlayerIndex).playedTurn();
        totalMoves.set(totalMoves.get() + 1);
    }
    public void addComputerTurnToHistory(int columnInWhichDiscWasPut,int rowInWhichDiscWasPut, Move.moveType i_MoveType)
    {
        moveHistory.AddMoveToHistory(activePlayerIndex, rowInWhichDiscWasPut, columnInWhichDiscWasPut, i_MoveType);
    }

    public void changeToNextActivePlayer()
    {
        activePlayerIndex = (activePlayerIndex + 1) % totalPlayers;
        if(retiredPlayersIndexes[activePlayerIndex] == true)
        {
            changeToNextActivePlayer();
        }
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
            columnToRemoveFrom = moveToUndo.getColumnIndex();
            rowToRemoveFrom = moveToUndo.getRowIndex();
            if(moveToUndo.getType() == Move.moveType.POPIN) {
                gameBoard.removeDisc(columnToRemoveFrom, rowToRemoveFrom, players.get(activePlayerIndex).getPieceShape());
            }
            else if(moveToUndo.getType() == Move.moveType.POPOUT)
            {
                gameBoard.insertDiscAtBottom(columnToRemoveFrom, players.get(moveToUndo.getPlayerIndex()).getPieceShape());
            }
            players.get(moveToUndo.getPlayerIndex()).undidTurn();
        }
        else
        {
            throw new NoMovesMadeException();
        }
    }

    public History getMoveHistory() {
        return moveHistory;
    }

    public int getNextPlaceInColumn(int column)
    {
        return (gameBoard.getNextPlaceInColumn())[column];
    }

    public GameState retireFromGame()
    {
        removeAllPlayerPiecesFromBoard();
        removePlayerFromGame();
        collapseRemainingPieces();

        return checkIfLastPlayer();
    }

    public GameState retireFromGame(GamePlayer playerToRemove)
    {
        removeAllPlayerPiecesFromBoard(playerToRemove);
        removePlayerFromGame(playerToRemove);
        collapseRemainingPieces();

        return checkIfLastPlayer();
    }

    private GameState checkIfLastPlayer()
    {
        int stillPlaying = 0;
        int lastPlayerIndex = 0;

        for(int i = 0; i < players.size(); i++)
        {
            if(!retiredPlayersIndexes[i])
            {
                stillPlaying++;
                lastPlayerIndex = i;
            }
        }

        if(stillPlaying == 1)
        {
            winningPlayers.add(players.get(lastPlayerIndex));

            return GameState.GameWin;
        }
        else
        {
            return null;
        }
    }

    public boolean[] getRetiredPlayersIndexes() {
        return retiredPlayersIndexes;
    }

    private void collapseRemainingPieces() {
        gameBoard.collapseSpaces();
    }

    private void removePlayerFromGame() {
        retiredPlayersIndexes[activePlayerIndex] = true;
        changeToNextActivePlayer();
    }

    private void removePlayerFromGame(GamePlayer retiree) {
        retiredPlayersIndexes[players.indexOf(retiree)] = true;
        if(players.indexOf(retiree) == activePlayerIndex) {
            changeToNextActivePlayer();
        }
    }

    private void removeAllPlayerPiecesFromBoard()
    {
        for(int row = 0; row < settings.getRows(); row++)
        {
            for(int column = 0; column < settings.getColumns(); column++)
            {
                if(gameBoard.getCellSymbol(row, column) == players.get(activePlayerIndex).getPieceShape())
                {
                    gameBoard.getBoard()[row][column] = Board.EMPTY_CELL;
                    moveHistory.AddMoveToHistory(activePlayerIndex,-1, column, Move.moveType.RETIRE);
                }
            }
        }
    }

    private void removeAllPlayerPiecesFromBoard(GamePlayer retiree)
    {
        for(int row = 0; row < settings.getRows(); row++)
        {
            for(int column = 0; column < settings.getColumns(); column++)
            {
                if(gameBoard.getCellSymbol(row, column) == retiree.getPieceShape())
                {
                    gameBoard.getBoard()[row][column] = Board.EMPTY_CELL;
                    moveHistory.AddMoveToHistory(activePlayerIndex,-1, column, Move.moveType.RETIRE);
                }
            }
        }
    }

    public synchronized boolean addPlayer(User user)
    {
        boolean added = false;
        if(playersCreated < totalPlayers)
        {
            Color playerColor = colorGenerator.getColor(playersCreated);
            players.add(new GamePlayer(user.getId(),
                    user.getName(),
                    user.isComputer() ? GamePlayer.Type.Computer : GamePlayer.Type.Human,
                    playerColor, colorGenerator.getColorName(playerColor)));
            playersCreated++;
            numOfActivePlayers++;
            added = true;
        }

        return added;
    }

    public synchronized boolean removePlayer(short playerID)
    {
        boolean removed = false;

        if(playersCreated > 0)
        {
            ArrayList<GamePlayer> remainingPlayers = new ArrayList<>(playersCreated - 1);
            for(int i = 0, playerToCopyIndex = 0; i < playersCreated; i++)
            {
                if(players.get(i).getId() != playerID)
                {
                    remainingPlayers.add(players.get(i));
                    playerToCopyIndex++;
                }
                else
                {
                    colorGenerator.freeColor(players.get(i).getPlayerColor());
                }
            }

            players = remainingPlayers;
            playersCreated--;
            removed = true;
        }

        return removed;
    }


    private class ColorGenerator {
        private LinkedList<Color> colors = new LinkedList<>();
        private boolean[] colorUsed = {false, false, false, false, false, false};
        ColorGenerator() {
            colors.add(Color.RED);
            colors.add(Color.BLUE);
            colors.add(Color.GREEN);
            colors.add(Color.ORANGE);
            colors.add(Color.BLACK);
            colors.add(Color.PURPLE);
        }

        public Color getColor(int index)
        {
            while(colorUsed[index])
            {
                index = (index + 1) % 6;
            }

            colorUsed[index] = true;
            return colors.get(index);
        }

        public String getColorName(Color color)
        {

            if(color == Color.RED)
            {
                return "RED";
            }
            else if(color == Color.BLUE)
            {
                return "BLUE";
            }
            else if(color == Color.BLACK)
            {
                return "BLACK";
            }
            else if(color == Color.GREEN)
            {
                return "GREEN";
            }
            else if(color == Color.PURPLE)
            {
                return "PURPLE";
            }
            else if(color == Color.ORANGE)
            {
                return "ORANGE";
            }


            return null;
        }

        public void freeColor(Color colorToFree)
        {
            int index = 0;

            while(colors.get(index) != colorToFree)
            {
                index++;
            }

            colorUsed[index] = false;
        }
    }

}
