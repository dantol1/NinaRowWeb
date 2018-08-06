import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LoadGame implements Commandable {

    @Override
    public String toString() {

        return "8. Load Game";
    }

    public void Invoke(Menu menu) {

        Scanner sc = new Scanner(System.in);
        String saveFileName;
        try {
            System.out.println("Please enter a save file name to load from:");
            saveFileName = sc.nextLine();
            menu.setTheGame(GameFactory.LoadGame(saveFileName));
            menu.showBoard();
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


