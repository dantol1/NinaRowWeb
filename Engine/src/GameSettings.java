import jaxb.schema.generated.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class GameSettings {
    public enum GameType{
        Basic,
        MultiPlayer,
        DynamicMultiPlayer
    }
    public enum Variant{
        Regular,
        Popout,
        Circular
    }

    public GameType getGameType() {
        return gameType;
    }

    public Variant getVariant() {
        return variant;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getTarget() {
        return target;
    }

    private GameType gameType;
    private Variant variant;
    private int rows;
    private int columns;
    private int target;

    public GameSettings(String gameType, String variant, int rows, int columns, int target) {
        this.gameType = GameType.valueOf(gameType);
        this.variant = Variant.valueOf(variant);
        this.rows = rows;
        this.columns = columns;
        this.target = target;
    }


}
