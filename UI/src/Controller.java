import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Light;
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
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.Timer;

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
    private boolean humanPlayerTurn = false;
    private Game theGame;
    private Scene mainScene;
    private ArrayList<FillTransition> winningAnimations = null;

    private Pane discPane = new Pane();

    private Shape theGridShape;

    public Game getGame(){
        return theGame;
    }

    public void setTheStage(Stage theStage) {
        this.theStage = theStage;
    }

    private Stage theStage;

    private Pane scrollPaneContent;
    public Scene getMainScene() {
        return mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    private class Disc extends Circle {

        private Color colorOfDisc;

        public Disc(Color colorOfDisc1) {

            super(TILE_SIZE/2, colorOfDisc1);
            colorOfDisc = colorOfDisc1;
            setCenterX(TILE_SIZE / 2);
            setCenterY(TILE_SIZE / 2);
        }

        public Color getColorOfDisc() {
            return colorOfDisc;
        }
    }

    @FXML
    private ScrollPane replayScrollPane;

    @FXML
    private Button replayNext;

    @FXML
    private Button replayPrev;

    @FXML
    private Button replayClose;

    @FXML
    private Label replayPlayerName;

    @FXML
    private Label replayPlayerID;

    @FXML
    private Label replayPlayerTurns;

    @FXML
    private ScrollPane scrollPaneSystem;

    @FXML
    private Button quitButton;

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
    private AnchorPane gameAnchorPane;

    @FXML
    private Pane PanePane;

    @FXML
    private HBox HboxTop;

    @FXML
    private Button saveGameButton;

    @FXML
    private Button loadGameButton;

    private String[] chosenStyle = {"playerStyle1",
            "activePlayerStyle1",
            "buttonStyle1",
            "mainFormStyle1",
            "retiredPlayerStyle1",
            "labelStyle1"};

    private  Label[] systemLabels;
    private  Button[] systemButtons;
    private  Pane[] systemPlayerPanes;


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
        for(Label l : systemLabels)
        {
            l.setVisible(true);
        }

        for(int currentPlayerIndex = 0, currentWorkedOnLabel = 0; currentPlayerIndex < theGame.getPlayers().length; currentPlayerIndex++)
        {
            systemLabels[currentPlayerIndex + currentWorkedOnLabel].setText(theGame.getPlayers()[currentPlayerIndex].getName());
            currentWorkedOnLabel++;
            systemLabels[currentPlayerIndex + currentWorkedOnLabel].setText(((Short)theGame.getPlayers()[currentPlayerIndex].getId()).toString());
            currentWorkedOnLabel++;
            systemLabels[currentPlayerIndex + currentWorkedOnLabel].textProperty().bind(new SimpleIntegerProperty(((Integer)theGame.getPlayers()[currentPlayerIndex].getHowManyTurnsPlayed())).asString());
            currentWorkedOnLabel++;
            systemLabels[currentPlayerIndex + currentWorkedOnLabel].setText(theGame.getPlayers()[currentPlayerIndex].getPlayerType().toString());
        }

        for(int unusedPlayerIndex = theGame.getPlayers().length, unusedLabel = unusedPlayerIndex * 4; unusedPlayerIndex < 6; unusedPlayerIndex++)
        {
            systemLabels[unusedLabel].setVisible(false);
            unusedLabel++;
            systemLabels[unusedLabel].setVisible(false);
            unusedLabel++;
            systemLabels[unusedLabel].setVisible(false);
            unusedLabel++;
            systemLabels[unusedLabel].setVisible(false);
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

        theGridShape = gridShape;

        scrollPaneContent = new Pane();
        scrollPaneContent.setPrefWidth(widthOfGrid);
        scrollPaneContent.setPrefHeight(heightOfGrid);
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

        if(isGameStarted && humanPlayerTurn) {
            int row = theGame.getSettings().getRows() - 1;
            turnSucceeded = popDisc(column,row);

            if (turnSucceeded) {
                endOfTurnActions(theGame.getSettings().getRows() - 1,column, Move.moveType.POPOUT);
            }
        }

    }

    private void playTurnDropDisc(Rectangle rect) {

        boolean turnSucceeded =false;
        int column = translateColumnFromXposition((int)rect.getTranslateX());

        if (isGameStarted && humanPlayerTurn) {
            int row = theGame.getNextPlaceInColumn(column);
            turnSucceeded = dropDisc(column,row);

            if (turnSucceeded)
            {
                endOfTurnActions(row,column, Move.moveType.POPIN);
            }

        }
    }

    private void endOfTurnActions(int row, int column, Move.moveType i_MoveType){

        theGame.endOfTurnActions(row, column, i_MoveType);
        Game.GameState gs = theGame.isGameEnded(column);
        if(gs == Game.GameState.GameWin)
        {
            String winMessage = "";

            for(GamePlayer p : theGame.getWinningPlayers())
            {
                winMessage += (p.getName());
            }
            if (animationCheckBox.isSelected())
            {
                animateWinningDiscs();
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
            if (animationCheckBox.isSelected())
            {
                animateWinningDiscs();
            }
            winMessage += " all reached the target.\n Game Tied!";
            gameEndedMessage(winMessage);
            gameStopActions();
            return;
        }
        else if (gs == Game.GameState.GameTie)
        {
            String tieMessage = "Game ended with tie, no winners";
            gameEndedMessage(tieMessage);
            gameStopActions();
            return;
        }

        changeActivePlayerPane();


        if (theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).isComputer())
        {
            humanPlayerTurn = false;
            timedComputerTurn();
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

    private void animateWinningDiscs() {

        HashSet<Point> winningPieces = theGame.getWinningPieces();
        winningAnimations = new ArrayList<>();

        for (Point p : winningPieces)
        {
            FillTransition animation = new FillTransition();
            animation.setShape(theDiscs[p.y][p.x]);
            animation.setFromValue(theDiscs[p.y][p.x].getColorOfDisc());
            animation.setToValue(Color.GOLD);
            animation.setDuration(Duration.millis(300));
            animation.setAutoReverse(true);
            animation.setCycleCount(10);
            winningAnimations.add(animation);
            animation.play();
        }
    }

    private void changeActivePlayerPane() {
        Pane[] playersPane = {gridPanePlayer1, gridPanePlayer2, gridPanePlayer3, gridPanePlayer4, gridPanePlayer5, gridPanePlayer6};
        int lastPlayerIndex = theGame.getActivePlayerIndex() - 1 < 0 ? theGame.getPlayers().length - 1 : theGame.getActivePlayerIndex() - 1;

        while(theGame.getRetiredPlayersIndexes()[lastPlayerIndex] == true)
        {
            lastPlayerIndex--;
            if(lastPlayerIndex < 0)
            {
                lastPlayerIndex = theGame.getPlayers().length - 1;
            }
        }

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
        Move.moveType moveType = null;

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
                  moveType = Move.moveType.POPIN;
               }
               else if (randomGameMove == GameMove.PopDisc) {

                   int row = theGame.getSettings().getRows() - 1;

                   turnSucceeded = popDisc(randomizedColumn, row);
                   moveType = Move.moveType.POPOUT;
               }
            }
        } while (!turnSucceeded);

        if (moveType == Move.moveType.POPIN) {
            endOfTurnActions(theGame.getNextPlaceInColumn(randomizedColumn),randomizedColumn, moveType);
        }
        else if (moveType == Move.moveType.POPOUT) {
            endOfTurnActions(theGame.getSettings().getRows() - 1, randomizedColumn, moveType);
        }

        if (theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).isComputer())
        {
            timedComputerTurn();
        }
    }

    private void timedComputerTurn()
    {
        Timer timer = new Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                executeComputerTurn();
                                humanPlayerTurn = true;
                            }
                        });
                    }
                },
                500
        );
    }

    private boolean popDisc(int column, int row) {

        boolean result = false;

        if (theGame.popoutDisc(column)) {

            result = true;

            theDiscs[row][column].setFill(Color.TRANSPARENT);
            theDiscs[row][column] = null;

            row--;
            collapseDiscs(theDiscs,row,column);
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
        int activePlayerIndex = 0;

        changePanesStyle(chosenStyleNum);
        changeStyleName(chosenStyleNum);

        if(theGame != null) {
            activePlayerIndex = theGame.getActivePlayerIndex();
        }

        if(theGame != null)
        {
            changeRetiredPlayerStyle(chosenStyleNum);
        }

        changeActivePlayerStyle(chosenStyleNum, activePlayerIndex);

        ChooseStyleMenuButton.getStyleClass().remove(chosenStyle[2]);

        changeButtonStyle(chosenStyleNum);
        changeAnchorPanesStyle(chosenStyleNum);

        ChooseStyleMenuButton.getStyleClass().add(chosenStyle[2]);

        changeFontStyles(chosenStyleNum);
    }

    private void changeFontStyles(String chosenStyleNum) {

        for(Label l : systemLabels)
        {
            l.getStyleClass().remove(chosenStyle[5]);
        }

        chosenStyle[5] = "labelStyle" + chosenStyleNum;

        for(Label l : systemLabels)
        {
            l.getStyleClass().add(chosenStyle[5]);
        }
    }

    private void changeStyleName(String chosenStyleNum) {
        switch (chosenStyleNum)
        {
            case "1":
                ChooseStyleMenuButton.setText("Marble");
                break;
            case "2":
                ChooseStyleMenuButton.setText("Wood");
                break;
            case "3":
                ChooseStyleMenuButton.setText("Metal");
                break;
        }
    }

    private void changeAnchorPanesStyle(String chosenStyleNum) {
        anchorPane.getStyleClass().remove(chosenStyle[3]);
        gameAnchorPane.getStyleClass().remove(chosenStyle[3]);
        chosenStyle[3] = "mainFormStyle" + chosenStyleNum;
        anchorPane.getStyleClass().add(chosenStyle[3]);
        gameAnchorPane.getStyleClass().add(chosenStyle[3]);
    }

    private void changeActivePlayerStyle(String chosenStyleNum, int activePlayerIndex) {
        systemPlayerPanes[activePlayerIndex].getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
        chosenStyle[1] = "activePlayerStyle" + chosenStyleNum;
        systemPlayerPanes[activePlayerIndex].getStyleClass().add(chosenStyle[1]);
    }

    private void changeRetiredPlayerStyle(String chosenStyleNum) {
        for(int i = 0; i < theGame.getPlayers().length; i++)
        {
            if(theGame.getRetiredPlayersIndexes()[i])
            {
                systemPlayerPanes[i].getStyleClass().removeAll(chosenStyle[0], chosenStyle[4]);
            }
        }

        chosenStyle[4] = "retiredPlayerStyle" + chosenStyleNum;

        for(int i = 0; i < theGame.getPlayers().length; i++)
        {
            if(theGame.getRetiredPlayersIndexes()[i])
            {
                systemPlayerPanes[i].getStyleClass().add(chosenStyle[4]);
            }
        }
    }

    private void changeButtonStyle(String chosenStyleNum) {
        for(Button b : systemButtons)
        {
            b.getStyleClass().remove(chosenStyle[2]);
        }

        chosenStyle[2] = "buttonStyle" + chosenStyleNum;

        for(Button b : systemButtons)
        {
            b.getStyleClass().add(chosenStyle[2]);
        }
    }

    private void changePanesStyle(String chosenStyleNum) {
        for(Pane p : systemPlayerPanes)
        {
            p.getStyleClass().remove(chosenStyle[0]);
        }

        chosenStyle[0] = "playerStyle" + chosenStyleNum;

        for(Pane p : systemPlayerPanes)
        {
            p.getStyleClass().add(chosenStyle[0]);
        }
    }

    @FXML
    void showReplay(ActionEvent event) throws Exception {

        List<ReplayState> replayStates = createReplaySates();
        Parent root = FXMLLoader.load(getClass().getResource("ReplayWindow.fxml"));
        Stage window = new Stage();
        window.setTitle("Replay Mode");
        Scene scene = new Scene(root,572,403);

        Pane contentPane = replayStates.get(0).getFinalizedPane();
        replayScrollPane.setContent(contentPane);

        window.showAndWait();
    }

    private LinkedList<ReplayState> createReplaySates() {

        LinkedList<ReplayState> replayStates = new LinkedList<ReplayState>();

        replayStates.add(new ReplayState(new Disc[theGame.getSettings().getRows()]
                [theGame.getSettings().getColumns()], new Pane(), theGridShape, "None", (short) 0, 0));

        for (Move move : theGame.getMoveHistory().showHistory()) {
            if (move.getType() == Move.moveType.POPIN) {

                Pane currentDiscs = ((LinkedList<ReplayState>) replayStates)
                        .getLast().getDiscPane();
                Disc disc = new Disc(theGame.getPlayerByIndex(move.getPlayerIndex()).getPlayerColor());

                disc.setTranslateX(move.getColumnIndex() * (TILE_SIZE + 5) + TILE_SIZE / 4);
                disc.setTranslateY(move.getRowIndex() * (TILE_SIZE + 5) + TILE_SIZE / 4);

                Disc discMatrix[][] = ((LinkedList<ReplayState>) replayStates).getLast().getDiscMatrix();
                discMatrix[move.getRowIndex()][move.getColumnIndex()] = disc;
                currentDiscs.getChildren().add(disc);

                replayStates.add(new ReplayState(discMatrix, currentDiscs, theGridShape,
                        theGame.getPlayerByIndex(move.getPlayerIndex()).getName(),
                        theGame.getPlayerByIndex(move.getPlayerIndex()).getId(),
                        theGame.getPlayerByIndex(move.getPlayerIndex()).getHowManyTurnsPlayed()));
            } else if (move.getType() == Move.moveType.POPOUT) {
                Pane currentDiscs = ((LinkedList<ReplayState>) replayStates)
                        .getLast().getDiscPane();
                int row = move.getRowIndex();
                Disc discMatrix[][] = ((LinkedList<ReplayState>) replayStates).getLast().getDiscMatrix();

                discMatrix[move.getRowIndex()][move.getColumnIndex()].setFill(Color.TRANSPARENT);
                discMatrix[row][move.getColumnIndex()] = null;

                row--;
                collapseDiscs(discMatrix, row, move.getColumnIndex());

                replayStates.add(new ReplayState(discMatrix, currentDiscs, theGridShape,
                        theGame.getPlayerByIndex(move.getPlayerIndex()).getName(),
                        theGame.getPlayerByIndex(move.getPlayerIndex()).getId(),
                        theGame.getPlayerByIndex(move.getPlayerIndex()).getHowManyTurnsPlayed()));

            }


        }

        return replayStates;
    }


    @FXML
    void startTheGame(ActionEvent event) {

        quitButton.setDisable(false);
        isGameStarted = true;
        ButtonXMLLoad.setDisable(true);
        StopGameButton.setDisable(false);
        StartGameButton.setDisable(true);
        clearExtraStyles();

        if (theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).isComputer())
        {
            timedComputerTurn();
        }
        else {
            humanPlayerTurn = true;
        }
    }

    private void clearExtraStyles() {
        Pane[] playerPanes = {gridPanePlayer1, gridPanePlayer2, gridPanePlayer3, gridPanePlayer4, gridPanePlayer5, gridPanePlayer6};

        for(Pane p : playerPanes)
        {
            p.getStyleClass().remove(chosenStyle[4]);
            p.setDisable(false);
        }
    }

    @FXML
    void stopTheGame(ActionEvent event) {

        gameStopActions();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Ended!");
        alert.setHeaderText("The Game has stopped");
        alert.setContentText("No Winners");
        alert.showAndWait();
    }

    private void gameStopActions()
    {
        clearBoard();

        StopGameButton.setDisable(true);
        StartGameButton.setDisable(true);
        ButtonXMLLoad.setDisable(false);
        quitButton.setDisable(true);
        isGameStarted = false;
    }

    private void clearBoard() {

        if (winningAnimations != null) {
            for (FillTransition animation : winningAnimations) {
                animation.stop();
                ((Disc) animation.getShape()).setFill(Color.TRANSPARENT);

            }
        }

        for (int i = 0; i<theDiscs.length; i++)
        {
            for (int j = 0; j<theDiscs[i].length; j++)
            {
                if (theDiscs[i][j] != null && !theGame.getWinningPieces().contains(new Point(j,i))) {
                    theDiscs[i][j].setFill(Color.TRANSPARENT);
                    theDiscs[i][j] = null;
                }
            }
        }

        winningAnimations = null;
        theGame = null;

    }
    @FXML
    public void quitGame() {

        Color playerColor = theGame.getPlayerByIndex(theGame.getActivePlayerIndex()).getPlayerColor();

        for (int i = 0; i<theDiscs.length; i++)
        {
            for (int j = 0; j<theDiscs[i].length; j++)
            {
                if (theDiscs[i][j] != null && theDiscs[i][j].getColorOfDisc() == playerColor)
                {

                    theDiscs[i][j].setFill(Color.TRANSPARENT);
                    theDiscs[i][j] = null;

                    int row = i;

                    row--;
                    collapseDiscs(theDiscs,row,j);
                }
            }
        }

        disableRetiredPlayerPane();
        if(theGame.retireFromGame() == Game.GameState.GameWin)
        {
            String winMessage = "";

            for(GamePlayer p : theGame.winningPlayers)
            {
                winMessage += p.getName();
            }

            winMessage += " Won by default.";
            gameEndedMessage(winMessage);
            gameStopActions();
            return;
        }
        changeActivePlayerPane();
    }

    private void disableRetiredPlayerPane() {
        Pane[] playerPanes = {gridPanePlayer1, gridPanePlayer2, gridPanePlayer3, gridPanePlayer4, gridPanePlayer5, gridPanePlayer6};

        playerPanes[theGame.getActivePlayerIndex()].getStyleClass().remove(chosenStyle[1]);
        playerPanes[theGame.getActivePlayerIndex()].getStyleClass().add(chosenStyle[4]);
        playerPanes[theGame.getActivePlayerIndex()].setDisable(true);
    }

    private void collapseDiscs(Disc matrix[][], int row, int column){

        while (matrix[row][column] != null)
        {
            matrix[row + 1][column] = matrix[row][column];
            matrix[row][column] = null;

            if (animationCheckBox.isSelected()) {

                TranslateTransition animation = new TranslateTransition(Duration.seconds(0.2), theDiscs[row + 1][column]);
                animation.setToY((row + 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
                animation.play();
            }
            else {
                matrix[row+1][column].setTranslateY((row + 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
            }

            row--;
        }
    }
    @FXML
    public void initialize() {
        createControllersSets();
        choosedStyle1(new ActionEvent());
        bindSize();
    }

    private void createControllersSets() {
        systemButtons = new Button[]{
                loadGameButton,
                saveGameButton,
                ReplayButton,
                StopGameButton,
                StartGameButton,
                ButtonXMLLoad,
                quitButton};
        systemPlayerPanes = new Pane[]{gridPanePlayer1,
                gridPanePlayer2,
                gridPanePlayer3,
                gridPanePlayer4,
                gridPanePlayer5,
                gridPanePlayer6};
        systemLabels = new Label[]{NameLabel1, IdLabel1, HowMuchTurnsLabel1, KindOfLabel1,
                NameLabel2, IdLabel2, HowMuchTurnsLabel2, KindOfLabel2,
                NameLabel3, IdLabel3, HowMuchTurnsLabel3, KindOfLabel3,
                NameLabel4, IdLabel4, HowMuchTurnsLabel4, KindOfLabel4,
                NameLabel5, IdLabel5, HowMuchTurnsLabel5, KindOfLabel5,
                NameLabel6, IdLabel6, HowMuchTurnsLabel6, KindOfLabel6};

    }

    private void bindSize() {
        anchorPane.minHeightProperty().bind(scrollPaneSystem.heightProperty());
        anchorPane.minWidthProperty().bind(scrollPaneSystem.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPaneSystem.prefHeightProperty());
        anchorPane.prefWidthProperty().bind(scrollPaneSystem.prefWidthProperty());
        anchorPane.maxHeightProperty().bind(scrollPaneSystem.maxHeightProperty());
        anchorPane.maxWidthProperty().bind(scrollPaneSystem.maxWidthProperty());
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
                        setFirstActivePlayer();
                    }

                    private void setFirstActivePlayer() {
                        gridPanePlayer1.getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
                        gridPanePlayer2.getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
                        gridPanePlayer3.getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
                        gridPanePlayer4.getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
                        gridPanePlayer5.getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);
                        gridPanePlayer6.getStyleClass().removeAll(chosenStyle[0], chosenStyle[1]);

                        gridPanePlayer1.getStyleClass().add(chosenStyle[1]);
                        gridPanePlayer2.getStyleClass().add(chosenStyle[0]);
                        gridPanePlayer3.getStyleClass().add(chosenStyle[0]);
                        gridPanePlayer4.getStyleClass().add(chosenStyle[0]);
                        gridPanePlayer5.getStyleClass().add(chosenStyle[0]);
                        gridPanePlayer6.getStyleClass().add(chosenStyle[0]);
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

    public class ReplayState {

        private Pane finalizedPane;
        private Pane discPane;
        private Shape gridShape;
        private String PlayerName;
        private short  ID;
        private int howManyTurns;
        private Disc discMatrix[][];

        public Pane getFinalizedPane() {
            return finalizedPane;
        }

        public Disc[][] getDiscMatrix() {
            return discMatrix;
        }

        public Pane getDiscPane(){
            return discPane;
        }

        public String getPlayerName() {
            return PlayerName;
        }

        public short getID() {
            return ID;
        }

        public int getHowManyTurns() {
            return howManyTurns;
        }

        public ReplayState(Disc[][] matrix,Pane discPane, Shape shape, String name, short idnumber, int turnsPlayed)
        {
            this.discPane = discPane;
            discMatrix = matrix;
            finalizedPane = new Pane();
            finalizedPane.getChildren().add(discPane);
            finalizedPane.getChildren().add(shape);

            this.PlayerName = name;
            this.ID = idnumber;
            this.howManyTurns = turnsPlayed;

        }
    }
}
