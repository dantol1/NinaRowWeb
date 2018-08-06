import java.util.Scanner;

public class PlayTurn implements Commandable {

    @Override
    public String toString(){

        return "4. Play Turn";
    }

    public void Invoke(Menu menu) {

        ExitGame exit = new ExitGame();
        boolean validAnswer = false;
        boolean validMove = false;
        int column = 0;

        if (menu.isStartGame()) {
            Game.GameState gameState = null;
            String possibleWinnerName = menu.getGame().getPlayerByIndex(menu.getGame().getActivePlayerIndex())
                    .getName();

            if (!menu.getGame().getPlayerByIndex(menu.getGame().getActivePlayerIndex()).isComputer()) {
                //A Human Turn
                menu.showBoard();

                do {
                    do {

                        System.out.println("Please specify the column number to drop the disc: ");
                        Scanner scanner = new Scanner(System.in);

                        try {
                            column = Integer.parseInt(scanner.nextLine());
                            validAnswer = true;
                        } catch (NumberFormatException e) {

                            System.out.println("Please specify a valid column number");
                        }

                    } while (!validAnswer);

                    if (menu.getGame().playTurn(column - 1) == false) {
                        System.out.println("Invalid column selection, Please select a different column.");
                        validAnswer = false;
                    }
                    else {

                        validMove = true;
                    }
                } while (!validMove);
            } else { //A Computer Turn

                menu.getGame().playComputerTurn();
            }

            menu.showBoard();
            gameState = menu.getGame().isGameEnded(menu.getGame().getMoveHistory().showHistory().getLast().getColumnIndex());

            if (gameState == Game.GameState.GameTie) {

                System.out.println("Game Ended With Tie");
                menu.setNewGame();
            } else if (gameState == Game.GameState.GameWin) {

                System.out.println(String.format("Game Ended, The Winner is: %s", possibleWinnerName));
                menu.setNewGame();
            }
        }
        else {

            System.out.println("Game has not been started!\n");
        }

    }
}
