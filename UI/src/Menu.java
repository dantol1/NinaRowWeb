
public class Menu {

    private boolean exitGame = false;
    private boolean PreGame = true;
    private StopWatch gameTime = new StopWatch();

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

    }
}
