import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    FSALoader loader = new FSALoader();

    @Override
    public void start(Stage primaryStage) throws Exception{

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Folder");
        File file = chooser.showOpenDialog(primaryStage);
        loader.setFileName("foo.txt");
        loader.load();

        //Drawing
        Group root = new Group();
        root.getChildren().add(new Circle(200,200,25));


        primaryStage.show();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 800));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
