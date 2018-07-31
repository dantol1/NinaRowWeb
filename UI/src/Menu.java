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
                if ((i>1) && (j>1 && j<cols)) {

                    System.out.print(" ___ |");
                }
            }
            System.out.print('\n');

        }
    }
}
