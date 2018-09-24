package WebLogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Exceptions.GameServiceException;
import Exceptions.PlayersAmountException;
import GameLogic.Game;
import GameLogic.GameInfo;

public class GameManager {

    private final Map<String, Game> games;
    private final Map<String, GameInfo> gamesInfo;

    public GameManager() {
        games = new HashMap<>();
        gamesInfo = new HashMap<>();
    }

    public List<GameInfo> getGamesList(){
        return gamesInfo.values().stream().collect(Collectors.toList());
    }

    public void addGame(Game game, String username) throws GameServiceException {
        String title = game.getGameTitle();
        if(isGameExists(title)){
            throw new GameServiceException(String.format("A game With The same Title: %s  already exists",title));
        }
        games.put(title, game);
        gamesInfo.put(title, new GameInfo(game, username));
    }

    public boolean isGameExists(String gameTitle) {
        return gamesInfo.containsKey(gameTitle);
    }

    public void registerUserToAGame(String gameTitle, User user) throws PlayersAmountException, GameServiceException {
        if(!isGameExists(gameTitle)){
            throw new GameServiceException("Provided Game Does not exists");
        }

        GameInfo gameInfo = gamesInfo.get(gameTitle);
        Game game = games.get(gameTitle);

        gameInfo.registerPlayer(user.getName());
        if(game.addPlayer(user) == false)
        {
            throw new PlayersAmountException("The game already has too many players!");
        }
        //gameInfo.setDisplay(game.isShow());
    }

    public Game getGame(String gameTitle) {
        return games.get(gameTitle);
    }
    public GameInfo getGameInfo(String gameTitle) {
        return gamesInfo.get(gameTitle);
    }

    public void unregisterPlayer(String gameTitle, short playerId) throws PlayersAmountException {
        Game game = games.get(gameTitle);
        GameInfo gameInfo = gamesInfo.get(gameTitle);
        if(game.removePlayer(playerId) == false)
        {
            throw new PlayersAmountException("The game has no players!");
        }
        gameInfo.unregisterPlayer();
        //gameInfo.setDisplay(game.isShow());
    }

    public GameInfo getGameByUserName(String userName) {

        GameInfo[] gameResult = new GameInfo[1];

        gamesInfo.forEach((string,game)->{
            if (game.hasPlayerWithName(userName))
            {
                gameResult[0] = game;
            }
        });

        return gameResult[0];
    }
}
