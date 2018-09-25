package GameLogic;

import Exceptions.PlayersAmountException;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Iterator;

public class GameController {

    public enum GameStatus {
        WaitingForPlayers,
        Started
    }

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
    private ArrayList<GamePlayer> players;

    public void setDisplay(boolean display) {
        this.display = display;
    }

    private boolean display = true;

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
    }

    public GameStatus getStatus(){
        return status;
    }
    public synchronized void registerPlayer(String name, boolean isComputer){
        //TODO need to Randomize and give a color to each player which i haven't done yet
        GamePlayer player = new GamePlayer(name,isComputer);
        this.players.add(player);
        this.numberOfRegisteredPlayers++;

        if (this.numberOfRegisteredPlayers == this.numberOfPlayers)
        {
            this.status = GameStatus.Started;
            this.theGame.setPlayers(this.players);
        }

    }

    public synchronized void unregisterPlayer() throws PlayersAmountException {
        if (numberOfRegisteredPlayers <= 0) {
            throw new PlayersAmountException("Game number of players can't be negative");
        }
        --numberOfRegisteredPlayers;
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
}
