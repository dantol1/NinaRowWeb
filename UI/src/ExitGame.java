public class ExitGame implements Commandable {

    @Override
    public String toString() {

        return "6. Exit Game";
    }

    public void Invoke(Menu menu) {

        menu.setExitGame(true);
    }
}
