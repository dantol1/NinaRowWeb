package GameLogic;

import Exceptions.PlayersAmountException;
import javafx.beans.property.SimpleIntegerProperty;

public class GameInfo {
    private String title;
    private SimpleIntegerProperty totalMoves = new SimpleIntegerProperty();
    private int numberOfPlayers;
    private int numberOfRegisteredPlayers = 0;
    private int rows = 0;
    private int columns = 0;
    private String uploadedBy;
    private int target;
    private String variant;

    public void setDisplay(boolean display) {
        this.display = display;
    }

    private boolean display = true;

    public GameInfo(Game game, String uploadedBy) {
        this.title = game.getGameTitle();
        this.numberOfPlayers = game.getTotalPlayers();
        this.uploadedBy = uploadedBy;
        this.columns = game.getSettings().getColumns();
        this.rows = game.getSettings().getRows();
        this.target = game.getSettings().getTarget();
        this.variant = game.getSettings().getVariant().name();
    }

    public synchronized void registerPlayer() throws PlayersAmountException {
        if (numberOfRegisteredPlayers >= numberOfPlayers) {
            throw new PlayersAmountException("Game already have its maximum number of players allowed");
        }
        numberOfRegisteredPlayers++;
    }

    public synchronized void unregisterPlayer() throws PlayersAmountException {
        if (numberOfRegisteredPlayers <= 0) {
            throw new PlayersAmountException("Game number of players can't be negative");
        }
        --numberOfRegisteredPlayers;
    }
}
