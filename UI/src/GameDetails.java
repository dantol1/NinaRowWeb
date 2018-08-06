public class GameDetails implements Commandable {

    @Override
    public String toString(){

        return "3. Show Game Details";
    }

    public void Invoke(Menu menu) {

        menu.showBoard();
        System.out.println(String.format("Target: %d",menu.getGame().getSettings().getTarget()));
        if (menu.isStartGame()) {

            System.out.println("Game Status: Started");
            System.out.println(String.format("Current Turn: %s",menu.getGame()
            .getPlayerByIndex(menu.getGame().getActivePlayerIndex()).getName()));
            System.out.println(String.format("Player 1 Piece Shape: %s",menu.getGame()
            .getPlayerByIndex(0).getPieceShape()));
            System.out.println(String.format("Player 2 Piece Shape: %s",menu.getGame()
                    .getPlayerByIndex(1).getPieceShape()));
            System.out.println(String.format("Player 1 Turns Played: %d",menu.getGame().getPlayerByIndex(0)
                    .getHowManyTurnsPlayed()));
            System.out.println(String.format("Player 1 Turns Played: %d",menu.getGame().getPlayerByIndex(1)
                    .getHowManyTurnsPlayed()));
            System.out.println(String.format("Time Passed Since Game Started: %s",menu.showTime()));


        }
        else {

            System.out.println("Game Status: Not Started");
        }
    }
}
