import java.util.Scanner;

public class StartTheGame implements Commandable {

    @Override
    public String toString(){

        return "2. Start Game";
    }

    public void Invoke(Menu menu) {

        if (!menu.isStartGame()) {
            if (menu.isLoadedXML() == true) {

                boolean validInput = false;
                Player.Type player2Type = null;

                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter a name for player 1");
                menu.getGame().getPlayerByIndex(0).setPlayer(scanner.nextLine(), 'X', Player.Type.Human);

                System.out.println("is Player 2 a computer? (Y/N)");
                do {
                    String input = scanner.nextLine();

                    if (input.matches("Y") || input.matches("y")) {

                        player2Type = Player.Type.Computer;

                    } else if (input.matches("N") || input.matches("n")) {

                        player2Type = Player.Type.Human;

                    } else {

                        System.out.println("Incorrect Input -> Please choose Y for yes, or N for no");
                    }
                } while (validInput);

                System.out.println("Please specify a name for Player 2:");
                menu.getGame().getPlayerByIndex(1).setPlayer(scanner.nextLine(), 'O', player2Type);
                menu.setStartGame(true);
                menu.getStopWatch().StartTime();
            } else if (menu.isGameLoaded() == true) {
                menu.setStartGame(true);
            } else {

                System.out.println("Cannot start the game without reading the XML file or loading a game.\n");
            }
        }
        else {

            System.out.println("Game already started");
        }
    }
}
