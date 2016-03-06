import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    FSALoader loader = new FSALoader();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Folder");
        File file = chooser.showOpenDialog(primaryStage);
        loader.setFileName("foo.txt");
        loader.load();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
