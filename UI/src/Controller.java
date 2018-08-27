import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    private final static int TILE_SIZE = 80;
    public Game getTheGame() {
        return theGame;
    }

    private enum GameMove {

        PopDisc,
        DropDisc
    }

    private Disc[][] theDiscs;
    public void setTheGame(Game theGame) {
        this.theGame = theGame;
    }
    private boolean isGameStarted = false;
    private Game theGame;
    private Scene mainScene;

    private Pane discPane = new Pane();

    public void setTheStage(Stage theStage) {
        this.theStage = theStage;
    }

    private Stage theStage;

    private Pane scrollPaneContent = new Pane();
    public Scene getMainScene() {
        return mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    private class Disc extends Circle {

        public Disc(Color colorOfDisc) {
            super(TILE_SIZE/2, colorOfDisc);

            setCenterX(TILE_SIZE / 2);
            setCenterY(TILE_SIZE / 2);

        }
    }
    @FXML
    private ScrollPane scrollPaneSystem;

    @FXML
    private CheckBox animationCheckBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane GridPanePlayers;

    @FXML
    private ScrollPane gamePane;

    @FXML
    private Pane gridPanePlayer1;

    @FXML
    private Label IdLabel1;

    @FXML
    private Label NameLabel1;

    @FXML
    private Label HowMuchTurnsLabel1;

    @FXML
    private Label KindOfLabel1;

    @FXML
    private Pane gridPanePlayer2;

    @FXML
    private Label IdLabel2;

    @FXML
    private Label NameLabel2;

    @FXML
    private Label HowMuchTurnsLabel2;

    @FXML
    private Label KindOfLabel2;

    @FXML
    private Pane gridPanePlayer3;

    @FXML
    private Label IdLabel3;

    @FXML
    private Label NameLabel3;

    @FXML
    private Label HowMuchTurnsLabel3;

    @FXML
    private Label KindOfLabel3;

    @FXML
    private Pane gridPanePlayer4;

    @FXML
    private Label IdLabel4;

    @FXML
    private Label NameLabel4;

    @FXML
    private Label HowMuchTurnsLabel4;

    @FXML
    private Label KindOfLabel4;

    @FXML
    private Pane gridPanePlayer5;

    @FXML
    private Label IdLabel5;

    @FXML
    private Label NameLabel5;

    @FXML
    private Label HowMuchTurnsLabel5;

    @FXML
    private Label KindOfLabel5;

    @FXML
    private Pane gridPanePlayer6;

    @FXML
    private Label IdLabel6;

    @FXML
    private Label NameLabel6;

    @FXML
    private Label HowMuchTurnsLabel6;

    @FXML
    private Label KindOfLabel6;

    @FXML
    private Button StartGameButton;

    @FXML
    private Button StopGameButton;

    @FXML
    private Button ButtonXMLLoad;

    @FXML
    private VBox VboxLeft;

    @FXML
    private Button ReplayButton;

    @FXML
    private MenuButton ChooseStyleMenuButton;


    @FXML
    private Pane PanePane;

    @FXML
    private HBox HboxTop;

    @FXML
    private Button saveGameButton;

    @FXML
    private Button loadGameButton;

    private String[] chosenStyle = {"playerStyle1", "activePlayerStyle1", "buttonStyle1"};

    @FXML
    void chooseXML(ActionEvent event) {

        InputStream inputstream = null;

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load XML File");
        fileChooser.getExtensionFilters().addAll(extFilter);
        File XMLfile = fileChooser.showOpenDialog(theStage);

        if (XMLfile != null) {
            try {
                inputstream = new FileInputStream(XMLfile);
            } catch (FileNotFoundException e) {

            }

            createXMLwithProgressBar(inputstream);
        }


    }

    private void createXMLwithProgressBar(InputStream inputstream) {

        Stage window = new Stage();
        Label label = new Label();
        label.setText("");
        ProgressBarTask task = new ProgressBarTask();
        task.SetWindow(window);
        StackPane stackpane = new StackPane();
        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(300);

        stackpane.getChildren().add(bar);
        stackpane.getChildren().addAll(label);
        label.setPadding(new Insets(50,0,0,0));
        task.SetInputStream(inputstream);
        label.textProperty().bind(task.messageProperty());
        bar.progressProperty().bind(task.progressProperty());

        Scene loadScene = new Scene(stackpane,600,200);
        window.setScene(loadScene);
        window.show();

       Thread g = new Thread(task);
       g.start();


    }

    private void fillPlayerData() {

        Label[] playerLabels = {NameLabel1, IdLabel1, HowMuchTurnsLabel1, KindOfLabel1,
        NameLabel2, IdLabel2, HowMuchTurnsLabel2, KindOfLabel2,
        NameLabel3, IdLabel3, HowMuchTurnsLabel3, KindOfLabel3,
        NameLabel4, IdLabel4, HowMuchTurnsLabel4, KindOfLabel4,
        NameLabel5, IdLabel5, HowMuchTurnsLabel5, KindOfLabel5,
        NameLabel6, IdLabel6, HowMuchTurnsLabel6, KindOfLabel6};


        for(int currentPlayerIndex = 0, currentWorkedOnLabel = 0; currentPlayerIndex < theGame.getPlayers().length; currentPlayerIndex++)
        {
            playerLabels[currentPlayerIndex + currentWorkedOnLabel].setText(theGame.getPlayers()[currentPlayerIndex].getName());
            currentWorkedOnLabel++;
            playerLabels[currentPlayerIndex + currentWorkedOnLabel].setText(((Short)theGame.getPlayers()[currentPlayerIndex].getId()).toString());
            currentWorkedOnLabel++;
            playerLabels[currentPlayerIndex + currentWorkedOnLabel].textProperty().bind(new SimpleIntegerProperty(((Integer)theGame.getPlayers()[currentPlayerIndex].getHowManyTurnsPlayed())).asString());
            currentWorkedOnLabel++;
            playerLabels[currentPlayerIndex + currentWorkedOnLabel].setText(theGame.getPlayers()[currentPlayerIndex].getPlayerType().toString());
        }

        for(int unusedPlayerIndex = theGame.getPlayers().length, unusedLabel = unusedPlayerIndex * 4; unusedPlayerIndex < 6; unusedPlayerIndex++)
        {
            playerLabels[unusedLabel].setVisible(false);
            unusedLabel++;
            playerLabels[unusedLabel].setVisible(false);
            unusedLabel++;
            playerLabels[unusedLabel].setVisible(false);
            unusedLabel++;
            playerLabels[unusedLabel].setVisible(false);
            unusedLabel++;
        }

    }

    private void paintBoard(double columns, double rows) {

        double widthOfGrid = ((columns) * (TILE_SIZE + 5) + TILE_SIZE / 4)+8, heightOfGrid = ((rows) * (TILE_SIZE + 5) + TILE_SIZE / 4)+8;


        if (columns * TILE_SIZE < gamePane.getWidth())
        {
            gamePane.setPrefWidth(columns * TILE_SIZE);
        }
        if (rows * TILE_SIZE < gamePane.getHeight())
        {
            gamePane.setPrefHeight(rows * TILE_SIZE);
        }

        Shape gridShape = new Rectangle(widthOfGrid,heightOfGrid);

        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<columns; j++) {
                Circle circle = new Circle(TILE_SIZE / 2);
                circle.setCenterX(TILE_SIZE / 2);
                circle.setCenterY(TILE_SIZE / 2);
                circle.setTranslateX(j * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(i * (TILE_SIZE + 5) + TILE_SIZE / 4);

                gridShape = Shape.subtract(gridShape,circle);
            }
        }

        gridShape.setFill(Color.MEDIUMPURPLE);


        scrollPaneContent.setPrefWidth(widthOfGrid);
        scrollPaneContent.setPrefHeight(heightOfGrid);
        scrollPaneContent.getChildren().remove(discPane);
        scrollPaneContent.getChildren().add(discPane);
        scrollPaneContent.getChildren().add(gridShape);
        scrollPaneContent.getChildren().addAll(setOverlayAndMouseClickOnOverlay(columns, rows));

        gamePane.setContent(scrollPaneContent);
    }

    private List<Rectangle> setOverlayAndMouseClickOnOverlay(double columns, double rows) {

        List<Rectangle> overlayList = new ArrayList<>();

        for (int i = 0; i<columns; i++){

            Rectangle rectangleUp = new Rectangle(TILE_SIZE,TILE_SIZE+TILE_SIZE/4 + 1);
            rectangleUp.setTranslateX(i * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rectangleUp.setFill(Color.TRANSPARENT);

            rectangleUp.setOnMouseEntered(e -> rectangleUp.setFill(Color.rgb(0,0,0,0.4)));
            rectangleUp.setOnMouseExited(e -> rectangleUp.setFill(Color.TRANSPARENT));
            rectangleUp.setOnMouseClicked(e -> playTurnDropDisc(rectangleUp));

            overlayList.add(rectangleUp);

            if (theGame.getSettings().getVariant() == GameSettings.Variant.Popout)
            {
                Rectangle rectangleDown = new Rectangle(TILE_SIZE, TILE_SIZE + TILE_SIZE / 4 + 1);
                rectangleDown.setTranslateY((rows - 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
                rectangleDown.setTranslateX(i * (TILE_SIZE + 5) + TILE_SIZE / 4);
                rectangleDown.setFill(Color.TRANSPARENT);

                rectangleDown.setOnMouseEntered(e -> rectangleDown.setFill(Color.rgb(0, 0, 0, 0.4)));
                rectangleDown.setOnMouseExited(e -> rectangleDown.setFill(Color.TRANSPARENT));
                rectangleDown.setOnMouseClicked(e -> playTurnPopDisc(rectangleDown));

                overlayList.add(rectangleDown);
            }

        }
        return overlayList;
    }

    private void playTurnPopDisc(Rectangle rect) {

        boolean turnSucceeded = false;
        int column = translateColumnFromXposition((int)rect.getTranslateX());
        int row = theGame.getSettings().getRows() - 1;
        if(isGameStarted) {

            turnSucceeded = popDisc(column,row);

            if (turnSucceeded) {
                endOfTurnActions(column);
            }
        }

    }

    private void playTurnDropDisc(Rectangle rect) {

        boolean turnSucceeded =false;
        int column = translateColumnFromXposition((int)rect.getTranslateX());
        int row = theGame.getNextPlaceInColumn(column);
        if (isGameStarted == true) {

            turnSucceeded = dropDisc(column,row);

            if (turnSucceeded)
            {
                endOfTurnActions(column);
            }

        }
    }

    private void endOfTurnActions(int column){

        theGame.endOfTurnActions(column);
        Game.GameState gs = theGame.isGameEnded(column);
        if(gs == Game.GameState.GameWin)
        {
            String winMessage = "";

            for(GamePlayer p : theGame.getWinningPlayers())
            {
                winMessage += (p.getName());
            }
            winMessage += " Won!";
            gameEndedMessage(winMessage);
            gameStopActions();
            return;
        }
        else if(gs == Game.GameState.SeveralPlayersWonTie)
        {
            String winMessage = "";

            for(GamePlayer p : theGame.getWinningPlayers())
            {
                winMessage += (p.getName() + ", ");
            }
            winMessage += " all reached the target.\n Game Tied!";
            gameEndedMessage(winMessage);
            gameStopActions();
            return;
        }

        changeActivePlayerPane();

        if (theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).isComputer())
        {
            try {
                wait(500);
            }
            catch(Exception e)
            {

            }
            executeComputerTurn();
        }
    }

    private void gameEndedMessage(String playersWon)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Ended!");
        alert.setHeaderText("Game Ended!");
        alert.setContentText(playersWon);
        alert.showAndWait();
    }

    private void changeActivePlayerPane() {
        Pane[] playersPane = {gridPanePlayer1, gridPanePlayer2, gridPanePlayer3, gridPanePlayer4, gridPanePlayer5, gridPanePlayer6};
        int lastPlayerIndex = theGame.getActivePlayerIndex() - 1 < 0 ? theGame.getPlayers().length - 1 : theGame.getActivePlayerIndex() - 1;


        playersPane[lastPlayerIndex].getStyleClass().remove(chosenStyle[1]);
        playersPane[lastPlayerIndex].getStyleClass().add(chosenStyle[0]);
        playersPane[theGame.getActivePlayerIndex()].getStyleClass().remove(chosenStyle[0]);
        playersPane[theGame.getActivePlayerIndex()].getStyleClass().add(chosenStyle[1]);
    }

    private int translateColumnFromXposition(int translateX) {

        return ((translateX - (TILE_SIZE/4))/(TILE_SIZE+5));
    }

    private void executeComputerTurn()
    {
        Random rand = new Random();
        boolean turnSucceeded = false;
        int randomizedColumn;

        do {
            randomizedColumn = rand.nextInt(theGame.getSettings().getColumns());

            if (theGame.getSettings().getVariant() == GameSettings.Variant.Circular ||
                    theGame.getSettings().getVariant() == GameSettings.Variant.Regular) {

                int row = theGame.getNextPlaceInColumn(randomizedColumn);

                turnSucceeded = dropDisc(randomizedColumn, row);
            }
            else if (theGame.getSettings().getVariant() == GameSettings.Variant.Popout) {

               GameMove randomGameMove = GameMove.values()[rand.nextInt(GameMove.values().length)];

               if (randomGameMove == GameMove.DropDisc) {

                  int row = theGame.getNextPlaceInColumn(randomizedColumn);

                  turnSucceeded = dropDisc(randomizedColumn, row);

               }
               else if (randomGameMove == GameMove.PopDisc) {

                   int row = theGame.getSettings().getRows() - 1;

                   turnSucceeded = popDisc(randomizedColumn, row);

               }
            }
        } while (!turnSucceeded);

        endOfTurnActions(randomizedColumn);

        if (theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).isComputer())
        {
            executeComputerTurn();
        }
    }

    private boolean popDisc(int column, int row) {

        boolean result = false;

        if (theGame.popoutDisc(column)) {

            result = true;

            theDiscs[row][column].setFill(Color.TRANSPARENT);
            theDiscs[row][column] = null;

            row--;
            while (theDiscs[row][column] != null) {

                theDiscs[row + 1][column] = theDiscs[row][column];
                theDiscs[row][column] = null;

                if (animationCheckBox.isSelected()) {

                    TranslateTransition animation = new TranslateTransition(Duration.seconds(0.2), theDiscs[row + 1][column]);
                    animation.setToY((row + 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
                    animation.play();
                }
                else {
                    theDiscs[row+1][column].setTranslateY((row + 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
                }

                row--;
            }
        }

        return result;
    }
    private boolean dropDisc(int column, int row) {

        boolean result = false;

        if (theGame.dropDisc(column)) {

            result = true;

            Disc disc = new Disc(theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).getPlayerColor());
            theDiscs[row][column] = disc;
            discPane.getChildren().add(disc);

            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

            if (animationCheckBox.isSelected()) {
                TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
                animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
                animation.play();
            }
            else {
                disc.setTranslateY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            }
        }

        return result;
    }

    @FXML
    void choosedStyle1(ActionEvent event) {
        changeStyleClass("1");
    }

    @FXML
    void choosedStyle2(ActionEvent event) {
        changeStyleClass("2");
    }

    @FXML
    void choosedStyle3(ActionEvent event) {
        changeStyleClass("3");
    }

    private void changeStyleClass(String chosenStyleNum)
    {
        Pane[] playersPane = {gridPanePlayer1, gridPanePlayer2, gridPanePlayer3, gridPanePlayer4, gridPanePlayer5, gridPanePlayer6};
        Button[] systemButtons = {
                loadGameButton,
                saveGameButton,
                ReplayButton,
                StopGameButton,
                StartGameButton,
                ButtonXMLLoad};
        int activePlayerIndex = 0;

        ChooseStyleMenuButton.setText("Style" + chosenStyleNum);

        for(Pane p : playersPane)
        {
            p.getStyleClass().remove(chosenStyle[0]);
        }

        chosenStyle[0] = "playerStyle" + chosenStyleNum;

        for(Pane p : playersPane)
        {
            p.getStyleClass().add(chosenStyle[0]);
        }

        if(theGame != null) {
            activePlayerIndex = theGame.getActivePlayerIndex();
        }

        playersPane[activePlayerIndex].getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
        chosenStyle[1] = "activePlayerStyle" + chosenStyleNum;
        playersPane[activePlayerIndex].getStyleClass().add(chosenStyle[1]);
        ChooseStyleMenuButton.getStyleClass().remove(chosenStyle[2]);

        for(Button b : systemButtons)
        {
            b.getStyleClass().remove(chosenStyle[2]);
        }

        chosenStyle[2] = "buttonStyle" + chosenStyleNum;

        for(Button b : systemButtons)
        {
            b.getStyleClass().add(chosenStyle[2]);
        }

        ChooseStyleMenuButton.getStyleClass().add(chosenStyle[2]);
    }

    @FXML
    void showReplay(ActionEvent event) {

    }

    @FXML
    void startTheGame(ActionEvent event) {

        isGameStarted = true;
        ButtonXMLLoad.setDisable(true);
        StopGameButton.setDisable(false);
        StartGameButton.setDisable(true);

        if (theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).isComputer())
        {
            executeComputerTurn();
        }
    }

    @FXML
    void stopTheGame(ActionEvent event) {

        gameStopActions();

        for (int i = 0; i<theDiscs.length; i++)
        {
            for (int j = 0; j<theDiscs[i].length; j++)
            {
                if (theDiscs[i][j] != null) {
                    theDiscs[i][j].setFill(Color.TRANSPARENT);
                    theDiscs[i][j] = null;
                }
            }
        }

        theGame = null;
        StartGameButton.setDisable(true);
        JOptionPane.showMessageDialog(null, "The Game has stopped\nNo Winners");

    }

    private void gameStopActions()
    {
        StopGameButton.setDisable(true);
        StartGameButton.setDisable(false);
        ButtonXMLLoad.setDisable(false);
        isGameStarted = false;
    }
    @FXML
    public void initialize() {
        choosedStyle1(new ActionEvent());
    }

    @FXML
    void loadTheGame(ActionEvent event) {

    }

    @FXML
    void saveTheGame(ActionEvent event) {

    }

    public class ProgressBarTask extends Task<Integer> {

        InputStream inputstream = null;
        Stage window = null;

        public void SetWindow(Stage window)
        {
            this.window = window;
        }

        public void SetInputStream(InputStream input) {

            this.inputstream = input;
        }


        @Override
        protected Integer call() throws InterruptedException {
            try {
                updateProgress(0.5,10);
                Thread.sleep(200);
                updateProgress(1,10);
                theGame = (GameFactory.CreateGame(inputstream));
                updateProgress(2,10);
                updateMessage("Game Engine Loaded Successfully");
                Thread.sleep(500);
                theDiscs = new Controller.Disc[theGame.getSettings().getRows()][theGame.getSettings().getColumns()];
                StartGameButton.setDisable(false);
                updateProgress(5,10);
                updateMessage("Discs set");
                Thread.sleep(200);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        paintBoard(theGame.getSettings().getColumns(),theGame.getSettings().getRows());
                    }
                });
                Thread.sleep(500);
                updateProgress(7,10);
                updateMessage("Board set");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fillPlayerData();
                    }
                });
                updateProgress(10,10);
                updateMessage("Loaded XML File Successfully");
            } catch (FileDataException e) {

                updateMessage("Error: " + e.getMessage());
            } catch (JAXBException e) {

                throw new RuntimeException();
            }


            return 10;

        }
    }
}
