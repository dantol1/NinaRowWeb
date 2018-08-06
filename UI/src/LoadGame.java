import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadGame implements Commandable {

    @Override
    public String toString() {

        return "8. Load Game";
    }

    public void Invoke(Menu menu) {

        try {
            menu.setTheGame(GameFactory.LoadGame(SaveGame.FILE_NAME));
            System.out.println("Loaded Successfully!");
            menu.setGameLoaded(true);
        } catch (FileNotFoundException e) {

            System.out.println("No Saved Game Found");
        } catch (IOException e) {

            System.out.println("Error: Load Unsuccessful");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}


