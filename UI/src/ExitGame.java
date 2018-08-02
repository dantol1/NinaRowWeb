public class ExitGame implements Commandable{

    public void Invoke(Menu menu) {

        menu.setExitGame(true);
    }
}
