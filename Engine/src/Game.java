import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Game implements Serializable {
    private Board gameBoard;
    private GameSettings settings;
    private Player players[];
    private int numOfActivePlayers;
    private boolean ended = false;

    public Game(GameSettings gs)
    {
        gameBoard = new Board(gs.getColumns(), gs.getRows());
        settings = gs;
        if(gs.getGameType() == GameSettings.GameType.Basic)
        {
            numOfActivePlayers = 2;
        }

        players = new Player[numOfActivePlayers];
    }

    public void saveGame(String location) throws IOException
    {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(location))) {
            out.writeObject(this);
            out.flush();
        }
    }
}
