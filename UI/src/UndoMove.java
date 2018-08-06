public class UndoMove implements Commandable {

    @Override
    public String toString() {

        return "9. Undo Move";
    }

    public void Invoke(Menu menu) {

        try {
            menu.getGame().undoMove();
        }
        catch (NoMovesMadeException e) {
            System.out.println(e.toString());
        }
    }
}
