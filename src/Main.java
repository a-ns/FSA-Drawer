import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Main extends Application {
    FSALoader loader;
    FSAProcessor processor;
    Drawer canvas;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.loader = new FSALoader();
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("."));
        chooser.setTitle("Open FSA");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FSA files (*.fsa)", "*.fsa"));
        File file = chooser.showOpenDialog(primaryStage);

        chooser.getExtensionFilters().remove(0);
        chooser.setTitle("Open FSA Input Text File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FSA text files (*.txt)", "*.txt"));
        File file1 = chooser.showOpenDialog(primaryStage);
        if(file1 == null || file == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setTitle("ERROR");
            alert.setContentText("No file input! File input is required! Program will exit.");
            alert.showAndWait();
            System.exit(0);
        }
        this.loader.setFileName(file.getName());
        this.loader.run();
        chooser.setTitle("Open String to Process");
        this.processor = new FSAProcessor(file1);
        this.processor.setLoader(loader);
        showAlert(processor.run());


        //Drawing
        this.canvas = new Drawer(this.processor, this.loader);
        this.canvas.draw(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }


    /**
     *
     * @param bool
     */
    public void showAlert(boolean bool){
        if(bool){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("ACCEPTED");
            alert.setTitle("FSA String Input");
            alert.setContentText("The string was ACCEPTED by the FSA.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("REJECTED");
            alert.setTitle("FSA String Input");
            alert.setContentText("The string was REJECTED by the FSA.");
            alert.showAndWait();
        }
    }



}
