import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Player;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class GameFactory {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public static Game CreateGame(InputStream xmlFile) throws JAXBException, FileDataException
    {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        GameDescriptor gd = (GameDescriptor)u.unmarshal(xmlFile);

        int rows = gd.getGame().getBoard().getRows();
        int columns = gd.getGame().getBoard().getColumns().intValue();
        String variant = gd.getGame().getVariant();
        int target = gd.getGame().getTarget().intValue();
        String gameType = gd.getGameType();
        checkGameValidity(rows,columns,target,gd.getPlayers().getPlayer());

        GameSettings gs = new GameSettings(gameType, variant, rows, columns, target);
        Game game = new Game(gs,gd.getPlayers().getPlayer());

        return game;
    }

    private static void checkGameValidity(int rows, int columns, int target, List<Player> thePlayers) throws FileDataException{

        String message = "";
        boolean idIsDuplicated = false;
        
        if (thePlayers.size() > 6 || thePlayers.size() < 2)
        {
            message += "Num of players is incorrect, must be within 2-6";
        }

        for (int i = 0; i<thePlayers.size(); i++)
        {
            for (int j = i+1; j<thePlayers.size(); j++)
            {
                if (thePlayers.get(i).getId() == thePlayers.get(j).getId())
                {
                    idIsDuplicated = true;
                    break;
                }
            }
        }
        if (idIsDuplicated)
        {
            message += "Two or more players have the same id, each player must have a different id";
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

        List<Player> players = createPlayerCopies(gameToCopy.getPlayers());
        gameCopy = new Game(gameToCopy.getSettings(), players, gameToCopy.getMoveHistory());
        copy.setGameCopy(gameCopy);
        copy.setMoveHistory(gameCopy.getMoveHistory());

        return copy;
    }

    private static List<Player> createPlayerCopies(GamePlayer[] players) {
        List<Player> playerCopies = new ArrayList<>(players.length);

        for(GamePlayer pl : players)
        {
            playerCopies.add(new Player());
        }

        return playerCopies;
    }
}
