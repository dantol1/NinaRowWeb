import com.sun.xml.internal.ws.api.pipe.Engine;

public class Menu {

    private boolean exitGame = false;
    private boolean PreGame = true;
    private StopWatch gameTime = new StopWatch();
    private Game theGame;

    public enum MenuOptions {
        SelectGameFile,
        LoadGame,
        History,
        PlayTurn,
        SaveGame,
        Exit,
        Stop,
        StartGame,
        UndoMove,
        ShowGameDetails
    }

    public void showMenu()
    {

    }

    private void showTime()
    {
        System.out.println(gameTime.timeRunning());
    }

    private void showBoard()
    {
        int rows = theGame.getSettings().getRows() + 2;
        int cols = theGame.getSettings().getColumns() + 3;

        for (int i = 0; i<rows; i++) {

            for (int j = 0; i<cols; j++) {

                if ( (i == 0 || i == 1) && (j == 0 || j == 1)) {

                    System.out.println(' ');
                }

                if (i == 0 && (j != 0 && j !=1)) {

                    System.out.println(j-1);
                }

                if (i == 1 && (j != 0 && j !=1)) {

                    System.out.println("__");
                }

                if ((i>1) && (j == 0)) {

                    System.out.println(i-1);
                }

                if ((i>1) && (j == 1 || j == cols-1)) {

                    System.out.println('|');
                }
                if ((i>1) && (j>1 && j<cols-1)) {

                    System.out.println("__");
                }
            }

            System.out.println('\n');
        }
    }
}
