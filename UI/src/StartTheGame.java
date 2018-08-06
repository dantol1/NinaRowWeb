import java.util.Scanner;

public class StartTheGame implements Commandable {

    @Override
    public String toString(){

        return "2. Start Game";
    }

    public void Invoke(Menu menu) {

        if (menu.isLoadedXML() == true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("is Player 2 a computer? (Y/N)");

            String input = scanner.nextLine();

            if (input.matches("Y") || input.matches("y")) {

                menu.getGame().getPlayerByIndex(1).setPlayer("Player 2", 'O', Player.Type.Computer);
                menu.setStartGame(true);
            } else if (input.matches("N") || input.matches("n")) {

                menu.getGame().getPlayerByIndex(1).setPlayer("Player 2", 'O', Player.Type.Human);
                menu.setStartGame(true);
            } else {

                System.out.println("Incorrect Input -> Game Not Started!");
            }
        }
    }
}
