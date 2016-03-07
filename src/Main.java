import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    FSALoader loader = new FSALoader();
    FSAProcessor processor;

    final double LINE_LENGTH = 100.0;
    final double CIRCLE_RADIUS = 25.0;


    @Override
    public void start(Stage primaryStage) throws Exception {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Folder");
        File file = chooser.showOpenDialog(primaryStage);
        loader.setFileName("foo.txt");
        loader.run();
        processor = new FSAProcessor(new File("fooTest.txt"));
        processor.setLoader(loader);
        processor.run();
        //Drawing
        draw(primaryStage);


    }


    public static void main(String[] args) {
        launch(args);
    }


    public void draw(Stage primaryStage) {
        Group root = new Group();
       // root.getChildren().add(new Circle(200, 200, CIRCLE_RADIUS));
        //draw line
        double y = primaryStage.getWidth()/2;
        Line line = new Line();
        line.setStartX(50);
        line.setEndX(50);
        line.setStartY(y);
        line.setEndY(y*this.loader.getNumberOfStates());
        root.getChildren().add(line);
        primaryStage.show();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 800));
    }
}
