public class ShowHistory implements Commandable {


    @Override
    public String toString() {

        return "5. Show History";
    }
    public void Invoke(Menu menu) {


        for (Move move : menu.getGame().getMoveHistory().showHistory()) {

            System.out.println(String.format("%s: %d",menu.getGame().getPlayerByIndex(move.getPlayerIndex()).getName()
            , move.getColumnIndex()));
        }
    }
}
