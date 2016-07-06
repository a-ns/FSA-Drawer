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
        if(file == null){
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
        this.processor = new FSAProcessor();
        this.processor.setLoader(loader);
        createPROLOG();
        System.exit(0);
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

    private void createPROLOG(){
        PrintWriter output = null;
        try {
            output = new PrintWriter(new File("JavaGenerated-FSA-Prolog.pl"));
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        if(null == output);
        else{
            output.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                          "%%%THIS IS A JAVA GENERATED FILE%%\n" +
                          "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
            for(int i = 0; i < this.loader.getStateTransitions().length; i++){
                String tmp = this.loader.getStateTransitions()[i];
                tmp = tmp.replace(":", ",");
                output.append("transition" + tmp +".\n");
            }
            for(int i = 0; i < this.loader.getAcceptStates().length; i++){
                String tmp = String.valueOf(this.loader.getAcceptStates()[i]);
                output.append("acceptState(" + tmp + ").\n");
            }
            String legal = createInput();
            this.processor.setBuffer(legal);
            while(!processor.run()){
                legal = createInput();
                this.processor.setBuffer(legal);
            }



            String illegal = createInput();
            this.processor.setBuffer(illegal);
            while(processor.run()){
                illegal = createInput();
                this.processor.setBuffer(illegal);
            }



            String legalToOutput = "[";
            for(int i = 0; i < legal.length(); i++){
                legalToOutput += legal.charAt(i) + ",";
            }
            legalToOutput = legalToOutput.substring(0, legalToOutput.length() - 1) + "]";

            String illegalToOutput = "[";
            for(int i = 0; i < illegal.length(); i++){
                illegalToOutput += illegal.charAt(i) + ",";
            }
            illegalToOutput = illegalToOutput.substring(0, illegalToOutput.length() - 1) + "]";




            output.append("checkTransition(CurrentChar,[CurrentChar|NewString],NewString). \n\n");
            output.append("" +
                    "run(CurrentState, []) :- acceptState(CurrentState).\n" +
                    "run(CurrentState,CurrentString) :- \n" +
                    "    transition(CurrentState, DestinationState, CurrentChar),\n" +
                    "    checkTransition(CurrentChar, CurrentString , NewString),\n" +
                    "    run(DestinationState, NewString).\n" +
                    "demo(X) :- run(" + this.loader.getInitialState() + ", X).\n" +
                    "bad :- demo(" + illegalToOutput + ").\n" +
                    "good :- demo(" + legalToOutput + ").\n");
            output.close();

        }
    }



    private void createLISP(){
        PrintWriter output = null;
        try {
            output = new PrintWriter(new File("JavaGenerated-FSA-Lisp.lsp"));
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
                    "(defun demo ()\n" +
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
        PrintWriter legal;
        String stringToOutput = createInput();
        this.processor.setBuffer(stringToOutput);
        while(!processor.run()){
            stringToOutput = createInput();
            this.processor.setBuffer(stringToOutput);
        }
        try {
            legal = new PrintWriter(new File("thestring.txt"));
            String lispLegalInput = new String();
            lispLegalInput = "(";
            for(int i = 0; i < stringToOutput.length(); i++){
                lispLegalInput += stringToOutput.charAt(i) + " ";
            }
            lispLegalInput += ")";
            legal.write(lispLegalInput);
            legal.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }
    int p = 0, q = 0, r = 0, s = 0, t = 0;

        private String createInput(){
            String tmp = new String();
            tmp += this.loader.getAlphabet()[p];
            tmp += this.loader.getAlphabet()[q];
            tmp += this.loader.getAlphabet()[r];
            tmp += this.loader.getAlphabet()[s];
            tmp += this.loader.getAlphabet()[t];
            t++;
            if(t == this.loader.getAlphabet().length){
                t = 0;
                s++;
            }
            if(s == this.loader.getAlphabet().length){
                s = 0;
                r++;
            }
            if(r == this.loader.getAlphabet().length){
                r = 0;
                q++;
            }
            if(q == this.loader.getAlphabet().length){
                q = 0;
                p++;
            }
            return tmp;
        }

}
