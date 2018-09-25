package WebLogic;

import GameLogic.GameController;

import java.util.List;

public class Games {
    List<GameController> games;

    public Games(List<GameController> games) {
        this.games = games;
    }

    public List<GameController> getGames() {
        return this.games;
    }

    public void setGames(List<GameController> games) {
        this.games = games;
    }
}