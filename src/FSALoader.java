/**
 * Created by Alex on 3/6/2016.
 */
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**THIS DOES TOKENIZING
 *
 */
public class FSALoader {


    private String fileName;
    private char[][] matrix;
    private Scanner file;
    private String stringOfTheFile;
    private int[] acceptStates;
    private int initialState;

    private int numberOfStates;
    private String[] alphabet;
    private String[] stateTransitions;
    private State[] arrayList;


    /**
     * Default constructor
     */
    public FSALoader(){
        this.file = null;
        this.fileName = null;
        this.matrix = null;
        this.stringOfTheFile = null;
    }

    /**
     *
     * @return
     */
    public char[][] getMatrix() {
        return matrix;
    }

    /**
     *
     * @param matrix
     */
    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }

    /**
     *
     * @return
     */
    public Scanner getFile() {
        return file;
    }

    /**
     *
     * @param file
     */
    public void setFile(Scanner file) {
        this.file = file;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /** main method that is ran
     *
     * @return
     */
    public boolean run(){

        try {
            this.file = new Scanner(new File(this.fileName));
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }
        this.stringOfTheFile = new String();
        while(file.hasNext()){
            this.stringOfTheFile += file.next();
        }


        tokenize(stringOfTheFile);

        makeMatrix();
        makeArrayList();
        return true;
    }

    /**
     *
     */
    private void makeArrayList(){
        this.arrayList = new State[this.numberOfStates];
        for(int i = 0; i < this.numberOfStates; i++){
            this.arrayList[i] = new State(i);
        }
        for(int i = 0; i < this.numberOfStates; i++){
            this.arrayList[i].transitions = new LinkedList<>();
            for(int j = 0; j < this.matrix[i].length; j++){
                if(this.matrix[i][j] == '\0');
                else {
                    this.arrayList[i].addTransition(j, this.matrix[i][j]);
                }
            }
        }
    }

    /**
     *
     * @param index
     * @return
     */
    public State getState(int index){
        return this.arrayList[index];
    }


    /**
     *
     * @param index
     * @return
     */
    public String getTransitionsOfState(int index){

        return this.arrayList[index].toString();

    }

    /**
     *
     * @return
     */
    public int getNumberOfStates() {
        return numberOfStates;
    }

    /**
     *
     * @param numberOfStates
     */
    public void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }


    /**Splits up the input string into its pieces for the matrix.
     *
     * @param buffer the whole file
     */
    private void tokenize(String buffer){
        String[] tokens = stringOfTheFile.split(";");
        for(int i = 0; i < tokens.length; i++){
            if(tokens[i].isEmpty()){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("ERROR");
                error.setTitle("ERROR");
                error.setContentText("Input FSA file contains errors. Exiting!");
                error.showAndWait();
                System.exit(0);
            }
        }
        this.numberOfStates = Integer.parseInt(tokens[0]);
        this.alphabet = tokens[1].split(",");


        this.stateTransitions = tokens[2].split(",");

        this.initialState = Integer.parseInt(tokens[3]);

        String[] acceptStates = tokens[4].split(",");
        this.acceptStates = new int[acceptStates.length];
        for(int i= 0; i < acceptStates.length; i++){
            this.acceptStates[i] = Integer.parseInt(acceptStates[i]);
        }

    }

    /**
     * Makes a matrix out of the input file
     * @return boolean whether or not the method was successfull in making the matrix. As of right now, it should always return true.
     */
    private boolean makeMatrix(){
        this.matrix = new char[this.numberOfStates][this.numberOfStates];

        for(int i = 0; i < this.stateTransitions.length; i++){
            matrix[Character.getNumericValue(this.stateTransitions[i].charAt(1))][Character.getNumericValue(this.stateTransitions[i].charAt(3))] = this.stateTransitions[i].charAt(5);
        }


        return true;
    }

    /**
     *
     * @return
     */
    public int[] getAcceptStates(){
        return this.acceptStates;
    }

    /**
     *
     * @return
     */
    public String[] getAlphabet(){
        return this.alphabet;
    }

    /**
     *
     * @return
     */
    public int getInitialState(){
        return this.initialState;
    }

    public String getAlphabetToString(){
        String tmp = new String();
        for(int i = 0; i < this.alphabet.length; i++){
            tmp += this.alphabet[i] + " ";
        }
        return tmp;
    }

    public String getAcceptStatesToString(){
        String tmp = new String();
        for(int i = 0; i < this.getAcceptStates().length; i++){
            tmp += this.getAcceptStates()[i] + " ";
        }
        return tmp;
    }

    public String getTransitionsToString(){
        String tmp = new String();
        tmp = "(";
        for(int i = 0; i < this.stateTransitions.length; i++){
            tmp += this.stateTransitions[i].replace(":", " ") + " ";
        }
        tmp += ")";
        return tmp;
    }





}
