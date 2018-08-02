import com.sun.xml.internal.ws.api.pipe.Engine;

public class Menu {

    private boolean exitGame = false;
    private boolean PreGame = true;
    private boolean StartGame = false;
    private boolean LoadedXML = false;
    private StopWatch gameTime = new StopWatch();
    private Game theGame;

    public void setTheGame(Game theGame) {

        this.theGame = theGame;
    }
    public Game getGame() {

        return theGame;
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
