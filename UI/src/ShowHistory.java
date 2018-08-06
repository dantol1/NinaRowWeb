public class ShowHistory implements Commandable {


    @Override
    public String toString() {

        return "5. Show History";
    }
    public void Invoke(Menu menu) {

        if (menu.isStartGame()) {

            System.out.println("Moves History:");
            if (menu.getGame().getMoveHistory().showHistory().size() != 0) {
                for (Move move : menu.getGame().getMoveHistory().showHistory()) {

                    System.out.println(String.format("%s: %d", menu.getGame().getPlayerByIndex(move.getPlayerIndex()).getName()
                            , move.getColumnIndex() + 1));
                }
            }
            else{

                System.out.println("No moves has been made");
            }
        }
        else {

            System.out.println("Game has not been started!\n");
        }
    }
}
