import jaxb.schema.generated.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;

public class GameFactory {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public static Game CreateGame(InputStream xmlFile) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        GameDescriptor gd = (GameDescriptor)u.unmarshal(xmlFile);

        int rows = gd.getGame().getBoard().getRows();
        int columns = gd.getGame().getBoard().getColumns().intValue();
        String variant = gd.getGame().getVariant();
        int target = gd.getGame().getTarget().intValue();
        String gameType = gd.getGameType();

        GameSettings gs = new GameSettings(gameType, variant, rows, columns, target);
        Game game = new Game(gs);

        return game;
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
