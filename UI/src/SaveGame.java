import java.io.IOException;

public class SaveGame implements Commandable {

    public static final String FILE_NAME = "SavedGame.dat";

    @Override
    public String toString() {

        return "7. Save Game";
    }

    public void Invoke(Menu menu) {

        try {
            menu.getGame().saveGame(FILE_NAME);
            System.out.println("Saved Successfully!");
        }
        catch (IOException e) {

            System.out.println("Error: Save Unsuccessful");
        }
    }
}
