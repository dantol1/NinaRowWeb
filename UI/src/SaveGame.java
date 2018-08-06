import java.io.IOException;
import java.util.Scanner;

public class SaveGame implements Commandable {

    //public static final String FILE_NAME = "SavedGame.dat";

    @Override
    public String toString() {

        return "7. Save Game";
    }

    public void Invoke(Menu menu) {

        Scanner sc = new Scanner(System.in);
        String saveFileName;
        if (menu.isStartGame()) {
            System.out.println("Please enter the name you want to save the game as:");
            saveFileName = sc.nextLine();
            try {
                menu.getGame().saveGame(saveFileName);
                System.out.println("Saved Successfully!");
            } catch (IOException e) {

                System.out.println("Error: Save Unsuccessful");
            }
        }
        else {

            System.out.println("Game has not been started!\n");
        }
    }
}
