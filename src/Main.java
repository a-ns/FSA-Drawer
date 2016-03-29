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
import java.io.*;

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


        createLISP();
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

    private void createLISP(){
        PrintWriter output = null;
        try {
            output = new PrintWriter(new File("program.lsp"));
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        if(null == output);
        else {
            output.write("" +
                    "(defun setlist()\n" +
                    "\t(set 'l '" + "(" + this.loader.getNumberOfStates() + " (" + this.loader.getAlphabetToString() + ") " + this.loader.getTransitionsToString() + " " + this.loader.getInitialState() + " (" + this.loader.getAcceptStatesToString() + "))) \n" +
                    "\t(set 'transitions (caddr l))\n" +
                    "\t(set 'startState (cadddr l))\n" +
                    "\t(set 'endStates (car(cddddr l)))\n" +
                    "\t(set 'alphabet (cadr l))\n" +
                    "\t(set 'numOfStates (car l))\n" +
                    "\t(set 'currentState startState)\n" +
                    ")\n");

            output.append("" +
                    "(defun demoLegal ()\n" +
                    "\t(setlist)\n" +
                    "\t(setq fp (open \"thestring.txt\" :direction :input))\n" +
                    "\t(setq inputBuffer (read fp))\n" +
                    "\t(close fp)\n" +
                    "\t(set 'currentSymbol (car inputBuffer))\n" +
                    "\t(run inputBuffer)\n" +
                    "\t(cond\n" +
                    "\t\t((null (memb currentState endStates)) nil)\n" +
                    "\t\t(T T)\n" +
                    "\t)\n" +
                    ")\n" +
                    "\n" +
                    "(defun run (l)\n" +
                    "\t(cond \n" +
                    "\t\t((null l) nil)\n" +
                    "\t\t(T \n" +
                    "\t\t\t(set 'theTransitionToDo (findTransitionToDo transitions))\n" +
                    "\t\t\t(cond\n" +
                    "\t\t\t\t((null theTransitionToDo)\n" +
                    "\t\t\t\tnil)\n" +
                    "\t\t\t)\n" +
                    "\t\t\t(setq currentState (cadr theTransitionToDo))\n" +
                    "\t\t\t(setq currentSymbol (cadr l))\n" +
                    "\t\t\t(run (cdr l))\n" +
                    "\t\t)\n" +
                    "\t)\n" +
                    ")\n" +
                    "\n" +
                    "(DEFUN MEMB (X L)\n" +
                    "    (COND \n" +
                    "\t\t((NULL L)\t  NIL)\n" +
                    "        ((ATOM L)\t  NIL)\n" +
                    "        ((EQUAL X (CAR L)) T)\n" +
                    "        (T  (MEMB X (CDR L)))\n" +
                    "    )\n" +
                    ")\n" +
                    "\n" +
                    "(defun findTransitionToDo (l)\n" +
                    "\t(cond\n" +
                    "\t\t\t((null l) nil)\n" +
                    "\t\t\t((and (equal currentState (caar l)) (equal currentSymbol (caddar l)))\n" +
                    "\t\t\t\t(car l)\n" +
                    "\t\t\t)\n" +
                    "\t\t\t(T (findTransitionToDo (cdr l)))\n" +
                    "\t)\n" +
                    ")");
            output.close();
        }

        String stringToOutput = createRandomInput();
        this.processor.setBuffer(stringToOutput);
        while(!processor.run()){
            stringToOutput = createRandomInput();
            this.processor.setBuffer(stringToOutput);
        }
        try {
            output = new PrintWriter(new File("theString.txt"));
            String lispLegalInput = new String();
            lispLegalInput = "(";
            for(int i = 0; i < stringToOutput.length(); i++){
                lispLegalInput += stringToOutput.charAt(i) + " ";
            }
            lispLegalInput += ")";
            output.write(lispLegalInput);
            output.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }






    }
        private String createRandomInput(){
            String tmp = new String();
            for(int i = 0; i < 5; i++){
                int random = 0 + (int)(Math.random() * ((this.loader.getAlphabet().length - 1 - 0) + 1));
                tmp += this.loader.getAlphabet()[random];
            }
            return tmp;
        }

}
