package GameLogic;

import Exceptions.PlayersAmountException;
import WebLogic.User;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class GameController {

    public enum GameStatus {
        WaitingForPlayers,
        Started,
        Finished
    }

    private Game.GameState state = Game.GameState.GameNotEnded;
    private GameStatus status;
    private String title;
    private SimpleIntegerProperty totalMoves = new SimpleIntegerProperty();
    private int numberOfPlayers;
    private int numberOfRegisteredPlayers = 0;
    private int rows = 0;
    private int columns = 0;
    private String uploadedBy;
    private int target;
    private String variant;
    private Game theGame;
    private String active = "No";

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    private ArrayList<GamePlayer> players;
    private String[][] Discs;

    public void setDisplay(boolean display) {
        this.display = display;
    }

    private boolean display = true;

    public void setStatus (GameStatus status) {

        this.status = status;
    }
    public void setState (Game.GameState state) {

        this.state = state;
    }
    public GameController(Game game, String uploadedBy) {
        this.theGame = game;
        this.title = game.getGameTitle();
        this.numberOfPlayers = game.getTotalPlayers();
        this.uploadedBy = uploadedBy;
        this.columns = game.getSettings().getColumns();
        this.rows = game.getSettings().getRows();
        this.target = game.getSettings().getTarget();
        this.variant = game.getSettings().getVariant().name();
        this.players = new ArrayList<>();
        this.status = GameStatus.WaitingForPlayers;
        this.Discs = new String[rows][columns];
    }

    public void Restart() {

        cleanBoard();
        setState(Game.GameState.GameNotEnded);
        setStatus(GameStatus.WaitingForPlayers);
        theGame.Restart();
        this.active = "No";
    }

    private void cleanBoard(){

        for (int i = 0; i<Discs.length; i++){

            for(int j=0; j<Discs[i].length; j++) {

                Discs[i][j] = null;

            }
        }
    }

    public GameStatus getStatus(){
        return status;
    }

    public synchronized void registerPlayer(User userToAdd) throws PlayersAmountException{
        if(theGame.addPlayer(userToAdd) == true) {
            players.add(theGame.getPlayers().get(theGame.getPlayersCreated()-1));
            this.numberOfRegisteredPlayers++;
        }
        else
        {
            throw new PlayersAmountException("too many players");
        }

        if (this.numberOfRegisteredPlayers == this.numberOfPlayers)
        {
            this.active = "Yes";
            this.status = GameStatus.Started;
        }
    }

    public String getTitle() {

        return title;
    }

    public synchronized void unregisterPlayer(User userToRemove) {

        theGame.removePlayer(userToRemove.getId());

        GamePlayer toRemove = null;
        for (GamePlayer player : players) {

            if (player.getName().equals(userToRemove.getName())) {

                toRemove = player;
            }
        }

        players.remove(toRemove);

        --numberOfRegisteredPlayers;

    }

    public DropDiscComplex dropComputerDisc() {

        DropDiscComplex result;

        result = theGame.playDropTurn();

        return result;
    }

    public DropDiscComplex dropDisc(int column) {

        DropDiscComplex result;

        result = theGame.playDropTurn(column);

        return result;
    }

    public DropDiscComplex popDisc(int column) {

        DropDiscComplex result = new DropDiscComplex(theGame.playPopTurn(column)
                ,0,0);

        return result;
    }

    public DropDiscComplex popComputerDisc() {

        int randomizedColumn = 0;
        Random rand = new Random();
        boolean succeeded;


        randomizedColumn = rand.nextInt(theGame.getSettings().getColumns());
        succeeded = theGame.playPopComputerTurn(randomizedColumn);


        DropDiscComplex result = new DropDiscComplex(succeeded,randomizedColumn,0);

        return result;
    }

    public Game.GameState playComputerTurn() {

        Game.GameState state = null;
        DropDiscComplex checkForWinners = null;
        Random rand = new Random();
        Move.moveType randomMove = null;

        if(theGame.getSettings().getVariant() == GameSettings.Variant.Circular ||
                theGame.getSettings().getVariant() == GameSettings.Variant.Regular)
        {
            randomMove = Move.moveType.POPIN;
            checkForWinners = dropComputerDisc();
        }
        else if (theGame.getSettings().getVariant() == GameSettings.Variant.Popout) {

            do {
                randomMove = Move.moveType.values()[rand.nextInt(
                        Move.moveType.values().length
                )];

                if (randomMove == Move.moveType.POPOUT) {

                    checkForWinners = popComputerDisc();

                } else if (randomMove == Move.moveType.POPIN) {

                    checkForWinners = dropComputerDisc();

                } else if (randomMove == Move.moveType.RETIRE) {

                    checkForWinners = new DropDiscComplex(false,0,0);

                }
            }while(!checkForWinners.isDropSucceded());
        }


        if (checkForWinners.isDropSucceded()) {

            if (randomMove == Move.moveType.POPIN)
            {
                int row = checkForWinners.getRow();
                int col = checkForWinners.getColumn();
                String color = players.get(theGame.getActivePlayerIndex()).getColorName().toLowerCase();
                Discs[row][col] = color;
            }
            else if (randomMove == Move.moveType.POPOUT) {

                Discs[theGame.getSettings().getRows()-1][checkForWinners.getColumn()] = null;

                for (int i = theGame.getSettings().getRows()-1; i>=0; i--)
                {
                    if (Discs[i][checkForWinners.getColumn()] != null) {
                        String save = Discs[i][checkForWinners.getColumn()];
                        Discs[i][checkForWinners.getColumn()] = Discs[i+1][checkForWinners.getColumn()];
                        Discs[i+1][checkForWinners.getColumn()] = save;
                    }
                }
            }

            state = theGame.isGameEnded(checkForWinners.getColumn());
        }

        this.state = state;

        if (state == Game.GameState.GameWin ||
                state == Game.GameState.GameTie ||
                state == Game.GameState.SeveralPlayersWonTie)
        {
            this.status = GameStatus.Finished;
        }

        return state;

    }
    public Game.GameState playTurn(int column, String moveType) {

        DropDiscComplex checkForWinners = null;
        Game.GameState state = null;

        if (moveType.equals("Drop!")) {
            checkForWinners = dropDisc(column);
        }
        else if (moveType.equals("Pop!")){

            checkForWinners = popDisc(column);
        }

        if (checkForWinners.isDropSucceded())
        {
            if (moveType.equals("Drop!")) {
                int row = checkForWinners.getRow();
                int col = checkForWinners.getColumn();
                String color = players.get(theGame.getActivePlayerIndex()).getColorName().toLowerCase();
                Discs[row][col] = color;
            }
            else if (moveType.equals("Pop!")) {

                Discs[theGame.getSettings().getRows()-1][column] = null;

                for (int i = theGame.getSettings().getRows()-1; i>=0; i--)
                {
                    if (Discs[i][column] != null) {
                        String save = Discs[i][column];
                        Discs[i][column] = Discs[i+1][column];
                        Discs[i+1][column] = save;
                    }
                }

            }
            state = theGame.isGameEnded(column);
        }

        this.state = state;

        if (state == Game.GameState.GameWin ||
                state == Game.GameState.GameTie ||
                state == Game.GameState.SeveralPlayersWonTie)
        {
            this.status = GameStatus.Finished;
        }

        return state;
    }
    public boolean hasPlayerWithName(String userName) {
        Iterator var2 = this.players.iterator();

        GamePlayer player;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            player = (GamePlayer) var2.next();
        } while(!player.getName().equals(userName));

        return true;

    }

    public String getTheCurrentPlayer() {

        return players.get(theGame.getActivePlayerIndex()).getName();
    }

    public Game.GameState getState() {

        return this.state;
    }

    public HashSet<GamePlayer> getWinningPlayers() {

        return theGame.getWinningPlayers();
    }
}
