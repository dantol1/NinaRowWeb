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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controller {

    public Game getTheGame() {
        return theGame;
    }

    public void setTheGame(Game theGame) {
        this.theGame = theGame;
    }

    private Game theGame;

    public Stage getTheStage() {
        return theStage;
    }

    public void setTheStage(Stage theStage) {
        this.theStage = theStage;
    }

    private Stage theStage;

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
                StartGameButton.setDisable(false);
            } catch (FileDataException e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (JAXBException e) {

                throw new RuntimeException();
            }
        }


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
}
