public class GameSettings {
    private enum GameType{
        Basic,
        MultiPlayer,
        DynamicMultiPlayer
    }
    private enum Variant{
        Regular,
        Popout,
        Circular
    }

    GameType gameType;
    Variant variant;
    int rows;
    int columns;
    int target;
    private Player players[];
}
