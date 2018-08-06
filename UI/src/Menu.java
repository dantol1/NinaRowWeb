import com.sun.xml.internal.ws.api.pipe.Engine;

import java.util.Scanner;

public class Menu {

    private boolean exitGame = false;
    private boolean StartGame = false;
    private boolean LoadedXML = false;
    private StopWatch gameTime = new StopWatch();
    private Commandable[] commands = new Commandable[] {new ReadXML(), new StartTheGame()
    ,new GameDetails(), new PlayTurn(), new ShowHistory(), new ExitGame(), new SaveGame(),
    new LoadGame(),new UndoMove() };
    private Game theGame;

    public void setExitGame(boolean exitGame) {
        this.exitGame = exitGame;
    }

    public void setTheGame(Game theGame) {

        this.theGame = theGame;
    }
    public Game getGame() {

        return theGame;
    }

    public void printCommands() {

        for (Commandable commandsItr : commands) {

            System.out.println(commandsItr.toString());
        }
    }

    public boolean isStartGame() {
        return StartGame;
    }

    public void setStartGame(boolean startGame) {
        StartGame = startGame;
    }


    public boolean isLoadedXML() {
        return LoadedXML;
    }

    public void setLoadedXML(boolean loadedXML) {
        LoadedXML = loadedXML;
    }

    public String showTime()
    {
        return gameTime.timeRunning();
    }

    public void Run() {

        int userInput;

        while (!exitGame) {

            printCommands();
            userInput = getUserInput();
            commands[userInput-1].Invoke(this);
        }
    }

    public int getUserInput() {

        int userInput = 0;
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);

        do {
            try {
                userInput = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {

                System.out.println("Invalid Input, please choose a number of the available options");
            }

            if (userInput > 0 && userInput<commands.length) {

                validInput = true;
            }
            else {

                System.out.println("Invalid Input, please choose a number of the available option");
            }
        } while (!validInput);

        return userInput;
    }

    public void setNewGame(){

        this.StartGame = false;
        this.LoadedXML = false;
    }

    public void showBoard()
    {
        int rows = theGame.getSettings().getRows() + 2;
        int cols = theGame.getSettings().getColumns() + 2;

        for (int i = 0; i<rows; i++) {

            for (int j = 0; j<cols; j++) {

                if ( (i == 0 || i == 1) && (j == 0)) {

                    System.out.print(' ');
                }
                if (i == 0 && j == 1) {
                    System.out.print(' ');
                }
                if ( i== 1 && j == 1) {

                    System.out.print(" X ");
                }

                if (i == 0 && (j>1 && j<cols)) {
                    if (j>10) {
                        System.out.print("  ");
                    }
                    else {
                        System.out.print("   ");
                    }
                    System.out.print(j-1);
                    if (j>10) {
                        System.out.print("  ");
                    }
                    else {
                        System.out.print("  ");
                    }
                }

                if (i == 1 && (j>1 && j<cols)) {
                    System.out.print("--- X ");
                }

                if ((i>1) && (j == 0)) {
                    if (i<11) {
                        System.out.print(' ');
                    }
                    System.out.print(i-1);
                }

                if ((i>1) && (j == 1)) {

                    System.out.print('|');
                }
                if ((i>1) && (j>1 && j<cols)) { //Printing the columns
                    System.out.print(" _");
                    if(theGame.returnBoard()[i-2][j-2] != ' ') {
                        System.out.print(theGame.returnBoard()[i-2][j-2]);
                    }
                    else{
                        System.out.print("_");
                    }
                    System.out.print("_ |");
                    //System.out.print(" ___ |");
                }
            }
            System.out.print('\n');

        }
    }
}
