import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final static int TILE_SIZE = 80;
    public Game getTheGame() {
        return theGame;
    }
    private Disc[][] theDiscs;
    public void setTheGame(Game theGame) {
        this.theGame = theGame;
    }

    private Game theGame;
    private Pane discPane = new Pane();

    public Stage getTheStage() {
        return theStage;
    }

    public void setTheStage(Stage theStage) {
        this.theStage = theStage;
    }

    private Stage theStage;

    private Pane scrollPaneContent = new Pane();

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

            try {

                theGame = (GameFactory.CreateGame(inputstream));
                theDiscs = new Disc[theGame.getSettings().getColumns()][theGame.getSettings().getRows()];
                StartGameButton.setDisable(false);
                paintBoard(theGame.getSettings().getColumns(),theGame.getSettings().getRows());
            } catch (FileDataException e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (JAXBException e) {

                throw new RuntimeException();
            }
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
            rectangleUp.setOnMouseClicked(e -> dropDisc(rectangleUp));

            overlayList.add(rectangleUp);
            
            Rectangle rectangleDown = new Rectangle(TILE_SIZE,TILE_SIZE+TILE_SIZE/4 + 1);
            rectangleDown.setTranslateY((rows-1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rectangleDown.setTranslateX(i * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rectangleDown.setFill(Color.TRANSPARENT);

            rectangleDown.setOnMouseEntered(e -> rectangleDown.setFill(Color.rgb(0,0,0,0.4)));
            rectangleDown.setOnMouseExited(e -> rectangleDown.setFill(Color.TRANSPARENT));
            rectangleDown.setOnMouseClicked(e -> popDisc());

            overlayList.add(rectangleDown);

        }
        return overlayList;
    }

    private void popDisc() {

    }

    private void dropDisc(Rectangle rect) {

        int column = translateColumnFromXposition((int)rect.getTranslateX());
        //need to be replaced with next position in column from theGame
        int row = translateRowFromYposition((int)rect.getTranslateY());
        //ToDelete
        theDiscs = new Disc[7][6];
        //
        if (true) { //need to be changed to theGame.DropDisc

            Disc disc = new Disc(Color.RED);
            theDiscs[row][column] = disc;
            discPane.getChildren().add(disc);

            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5),disc);
            animation.setToY(row  * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation.play();

        }
    }

    private int translateRowFromYposition(int translateY) {

        return ((translateY - (TILE_SIZE/4))/(TILE_SIZE+5));
    }

    private int translateColumnFromXposition(int translateX) {

        return ((translateX - (TILE_SIZE/4))/(TILE_SIZE+5));
    }

    @FXML
    void choosedStyle1(ActionEvent event) {

    }

    @FXML
    void choosedStyle2(ActionEvent event) {

    }

    @FXML
    void choosedStyle3(ActionEvent event) {

    }

    @FXML
    void showReplay(ActionEvent event) {

        paintBoard(6,7);
    }

    @FXML
    void startTheGame(ActionEvent event) {

    }

    @FXML
    void stopTheGame(ActionEvent event) {

    }
    @FXML
    public void initialize() {


    }

    @FXML
    void loadTheGame(ActionEvent event) {

    }

    @FXML
    void saveTheGame(ActionEvent event) {

    }
}
