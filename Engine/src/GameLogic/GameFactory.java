package GameLogic;

import Exceptions.FileDataException;
import jaxb.schema.generated.GameDescriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameFactory {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public static Game CreateGame(String xmlFile) throws JAXBException, FileDataException
    {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        StringReader sr = new StringReader(xmlFile);
        GameDescriptor gd = (GameDescriptor)u.unmarshal(sr);

        if(gd.getDynamicPlayers() == null) {
            int rows = gd.getGame().getBoard().getRows();
            int columns = gd.getGame().getBoard().getColumns().intValue();
            String variant = gd.getGame().getVariant();
            int target = gd.getGame().getTarget().intValue();
            String gameType = gd.getGameType();

            GameSettings gs = new GameSettings(gameType, variant, rows, columns, target);
            Game game = new Game(gs,gd.getPlayers().getPlayer());

            return game;
        }
        else
        {
            return CreateDynamicGame(gd);
        }
    }

    public static Game CreateDynamicGame(GameDescriptor gd) throws FileDataException
    {
        int rows = gd.getGame().getBoard().getRows();
        int columns = gd.getGame().getBoard().getColumns().intValue();
        String variant = gd.getGame().getVariant();
        int target = gd.getGame().getTarget().intValue();
        String gameType = gd.getGameType();
        checkGameValidity(rows, columns, target, gd.getDynamicPlayers().getTotalPlayers());

        GameSettings gs = new GameSettings(gameType, variant, rows, columns, target);
        Game game = new Game(gs, gd.getDynamicPlayers());

        return game;
    }

    private static void checkGameValidity(int rows, int columns, int target, byte thePlayers) throws FileDataException{

        String message = "";
        boolean idIsDuplicated = false;
        
        if (thePlayers > 6 || thePlayers < 2)
        {
            message += "Num of players is incorrect, must be within 2-6";
        }

        if (rows > 50 || rows < 5) {
            message += "Rows is not within boundries [5,50]";
        }
        if (columns > 30 || columns < 3) {
            message += "Columns is not within boundries [3,30]";
        }
        if (target >= Math.min(rows,columns)) {
            message += "Target is bigger than Columns and/or Rows";
        }

        if (message != "") {
            throw new FileDataException(message);
        }

    }

    public static Game LoadGame(String location) throws IOException, ClassNotFoundException
    {
        Game g;

        try(ObjectInputStream load =
                    new ObjectInputStream(
                            new FileInputStream(location)))
        {
            g = (Game)load.readObject();
        }

        return g;
    }

    public static GameCopy CreateGameCopy(Game gameToCopy)
    {
        Game gameCopy;
        GameCopy copy = new GameCopy();

        List<GamePlayer> players = createPlayerCopies(gameToCopy.getPlayers());
        gameCopy = new Game(gameToCopy.getSettings(), players, gameToCopy.getMoveHistory());
        copy.setGameCopy(gameCopy);
        copy.setMoveHistory(History.CopyHistory(gameToCopy.getMoveHistory()));

        return copy;
    }

    private static List<GamePlayer> createPlayerCopies(GamePlayer[] players) {
        List<GamePlayer> playerCopies = new ArrayList<>(players.length);

        for(GamePlayer pl : players)
        {
            GamePlayer player = new GamePlayer(pl.getId(), pl.getName(), pl.getPlayerType(), pl.getPlayerColor());
            playerCopies.add(player);
        }

        return playerCopies;
    }
}
