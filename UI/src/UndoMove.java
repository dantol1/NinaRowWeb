public class UndoMove implements Commandable {

    @Override
    public String toString() {

        return "9. Undo Move";
    }

    public void Invoke(Menu menu) {

        if (menu.isStartGame()) {
            try {
                menu.getGame().undoMove();
            } catch (NoMovesMadeException e) {
                System.out.println(e.toString());
            }
        }
        else {

            System.out.println("Game has not been started!\n");
        }
    }
}
