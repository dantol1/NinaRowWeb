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
        primaryStage.setScene(new Scene(root, 1024, 800));
        Controller myController = new Controller();
        myController.setTheStage(primaryStage);
        primaryStage.show();

    }
}
