import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadGame implements Commandable {

    @Override
    public String toString() {

        return "8. Load Game";
    }

    public void Invoke(Menu menu) {

        if (menu.isStartGame()) {
            try {
                menu.getGame().loadGame(SaveGame.FILE_NAME);
                System.out.println("Loaded Successfully!");
            } catch (FileNotFoundException e) {

                System.out.println("No Saved Game Found");
            } catch (IOException e) {

                System.out.println("Error: Load Unsuccessfully");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {

            System.out.println("Game has not been started!\n");
        }
    }
}


