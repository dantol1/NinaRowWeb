import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("N In a Row");
        Scene theScene = new Scene(root, 1024, 800);
        primaryStage.setScene(theScene);
        Controller myController = new Controller();
        myController.setTheStage(primaryStage);
        myController.setMainScene(theScene);
        primaryStage.show();

    }
}
