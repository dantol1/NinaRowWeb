import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


public class ReplayWindowController {

    private Shape theGridShape;
    private int col;
    private int row;
    private Pane pane;
    private Pane discPane = new Pane();
    private Controller.Disc[][] theDiscs;
    public GameCopy getGame() {
        return game;

    }

    public void setGameCopy(GameCopy game) {
        this.game = game;
    }

    private GameCopy game;

    @FXML
    private Pane thePane;

    @FXML
    private Button replayPrev;

    @FXML
    private Button replayClose;

    @FXML
    private Button replayNext;

    @FXML
    private ScrollPane replayScrollPane;

    @FXML
    private Label replayPlayerName;

    @FXML
    private Label replayPlayerID;

    @FXML
    private Label replayPlayerTurns;

    public Shape getGridShape() {
        return theGridShape;
    }

    public void setGridShape(Shape gridShape) {
        this.theGridShape = gridShape;
    }


    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }


    public void initialize() {


        setAllLabelsToNone();
        replayPrev.setDisable(true);
        if (game.getMoveHistory().showHistory().size() == 0)
        {
            replayNext.setDisable(true);
        }
        makeGrid(col,row);
        theDiscs = new Controller.Disc[row][col];
        pane = new Pane();
        pane.getChildren().add(discPane);
        pane.getChildren().addAll(theGridShape);
        replayScrollPane.setContent(pane);
//        replayScrollPane.minHeightProperty().bind(thePane.heightProperty());
//        replayScrollPane.minWidthProperty().bind(thePane.widthProperty());
//        replayScrollPane.maxHeightProperty().bind(thePane.maxHeightProperty());
//        replayScrollPane.maxWidthProperty().bind(thePane.maxWidthProperty());
//        replayScrollPane.prefHeightProperty().bind(thePane.prefHeightProperty());
//        replayScrollPane.prefWidthProperty().bind(thePane.prefWidthProperty());
    }

    private void setAllLabelsToNone() {
        replayPlayerTurns.setText("");
        replayPlayerID.setText("");
        replayPlayerName.setText("");
    }

    private void makeGrid(int col, int row) {

        double widthOfGrid = ((col) * (80 + 5) + 80 / 4)+8, heightOfGrid = ((row) * (80 + 5) + 80 / 4)+8;

        Shape gridShape = new Rectangle(widthOfGrid,heightOfGrid);

        for (int i = 0; i<row; i++) {
            for (int j = 0; j<col; j++) {
                Circle circle = new Circle(80 / 2);
                circle.setCenterX(80 / 2);
                circle.setCenterY(80 / 2);
                circle.setTranslateX(j * (80 + 5) + 80 / 4);
                circle.setTranslateY(i * (80 + 5) + 80 / 4);

                gridShape = Shape.subtract(gridShape,circle);
            }
        }

        gridShape.setFill(Color.MEDIUMPURPLE);
        theGridShape = gridShape;
    }

    @FXML
    public void closeButton()
    {
        Stage window = (Stage) replayClose.getScene().getWindow();

        window.close();
    }
    @FXML
    public void prevButton() {

        Move move = game.UndoMoveFromHistory();
        int col = move.getColumnIndex();
        int row = move.getRowIndex();


        int playerIndex = move.getPlayerIndex();

        if (playerIndex == 0)
        {
            playerIndex = game.getGameCopy().getPlayers().length - 1;
        }
        else
        {
            playerIndex--;
        }
        setPlayerInfo(new Move(playerIndex,move.getRowIndex(),move.getColumnIndex(),
                move.getType()));

        if (move.getType() == Move.moveType.POPIN) {

            theDiscs[row][col].setFill(Color.TRANSPARENT);
            theDiscs[row][col] = null;
        }

        else if (move.getType() == Move.moveType.POPOUT) {

            while (theDiscs[row][col] != null)
            {
                row--;
                System.out.println("in row-- loop");
            }
            while (row < game.getGameCopy().getSettings().getRows() - 1) {
                System.out.println("in row++ loop");
                Controller.Disc disc = theDiscs[row+1][col];
                disc.setTranslateY(row * (80 + 5) + 80 / 4);
                theDiscs[row][col] = theDiscs[row + 1][col];
                row++;
            }
            Controller.Disc disc = new Controller.Disc(
                    game.getGameCopy().getPlayerByIndex(move.getPlayerIndexDiscThatWasPopped()).getPlayerColor()
            );
            disc.setTranslateX(col * (80 + 5) + 80 / 4);
            disc.setTranslateY(row * (80 + 5) + 80 / 4);
            theDiscs[row][col] = disc;
            discPane.getChildren().add(disc);

        }

        if (game.getLastMoveSeenIndex() == 0)
        {
            replayPrev.setDisable(true);
        }
        if (game.getLastMoveSeenIndex() < game.getMoveHistory().showHistory().size())
        {
            replayNext.setDisable(false);
        }

    }

    @FXML
    public void nextButton(){

        Move move = game.PlayMoveFromHistory();
        int col = move.getColumnIndex();
        int row = move.getRowIndex();

        setPlayerInfo(move);

        if (move.getType() == Move.moveType.POPIN) {
            Controller.Disc disc = new Controller.Disc
                    (game.getGameCopy().getPlayerByIndex(move.getPlayerIndex()).getPlayerColor());

            theDiscs[row][col] = disc;
            discPane.getChildren().add(disc);

            disc.setTranslateX(col * (80 + 5) + 80 / 4);
            disc.setTranslateY(row * (80 + 5) + 80 / 4);
        }
        else if (move.getType() == Move.moveType.POPOUT) {

            theDiscs[row][col].setFill(Color.TRANSPARENT);
            theDiscs[row][col] = null;

            row--;
            while (theDiscs[row][col] != null)
            {
                theDiscs[row + 1][col] = theDiscs[row][col];
                theDiscs[row][col] = null;

                theDiscs[row+1][col].setTranslateY((row + 1) * (80 + 5) + 80 / 4);

                row--;
            }
        }

        if (game.getLastMoveSeenIndex() >= game.getMoveHistory().showHistory().size())
        {
            replayNext.setDisable(true);
        }
        if (game.getLastMoveSeenIndex() > 0)
        {
            replayPrev.setDisable(false);
        }

    }

    private void setPlayerInfo(Move move) {

        if (game.getLastMoveSeenIndex() != 0) {
            replayPlayerID.setText(String.format("%d", game.getGameCopy().getPlayerByIndex(
                    move.getPlayerIndex()).getId()));
            replayPlayerName.setText(game.getGameCopy().getPlayerByIndex(
                    move.getPlayerIndex()).getName());
            replayPlayerTurns.setText(String.format("%d", game.getGameCopy().getPlayerByIndex
                    (move.getPlayerIndex()).getHowManyTurnsPlayed()));

        }
        else
        {
            setAllLabelsToNone();
        }
    }

}
