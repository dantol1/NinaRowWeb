package WebLogic;

import GameLogic.GameInfo;

import java.util.List;

public class Games {
    List<GameInfo> games;

    public Games(List<GameInfo> games) {
        this.games = games;
    }

    public List<GameInfo> getGames() {
        return this.games;
    }

    public void setGames(List<GameInfo> games) {
        this.games = games;
    }
}