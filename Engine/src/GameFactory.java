import jaxb.schema.generated.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;


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
        checkGameValidity(rows,columns,target);


        GameSettings gs = new GameSettings(gameType, variant, rows, columns, target);
        Game game = new Game(gs);

        return game;
    }

    private static void checkGameValidity(int rows, int columns, int target) throws FileDataException{

        String message = "";
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
}
