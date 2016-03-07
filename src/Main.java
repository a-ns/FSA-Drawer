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
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    FSALoader loader = new FSALoader();
    FSAProcessor processor;

    final double LINE_LENGTH = 100.0;
    final double CIRCLE_RADIUS = 25.0;
 //   final double CIRCLE_WIDTH = 100;
  //  final double CIRCLE_HEIGHT = 100;

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
        //draw line
        double y = 600/2;
        Line line = new Line(300, 0,300, 100*this.loader.getNumberOfStates());
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(3);
        root.getChildren().add(line);
        double currentHeight = 100;
        double currentWidth = 300;
        for(int i = 0; i < this.loader.getNumberOfStates(); i++) {
            Circle circle = new Circle(currentWidth, currentHeight, CIRCLE_RADIUS);

            circle.setFill(Color.GREY);
            circle.setStroke(Color.BLACK);
            String iToS = String.valueOf(i);
            Text text = new Text(iToS);
            text.setX(currentWidth);
            text.setY(currentHeight);
            text.setBoundsType(TextBoundsType.VISUAL);

            root.getChildren().add(circle);
            root.getChildren().add(text);
            currentHeight += 100;
        }
        primaryStage.show();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 800));
    }
}
