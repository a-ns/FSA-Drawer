/**
 * Created by Alex on 3/6/2016.
 */
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

    private class State{

        LinkedList<Transition> transitions;

        public State(){
            this.transitions = new LinkedList<Transition>();
        }
        public void addTransition(int index, char transitioner){
            Transition tmp = new Transition(index, transitioner);
            this.transitions.add(tmp);
            return;
        }


        private class Transition{

            private int index;
            private char transitioner;

            public Transition(int index, char transitioner){
                this.index = index;
                this.transitioner = transitioner;
            }
            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public char getTransitioner() {
                return transitioner;
            }

            public void setTransitioner(char transitioner) {
                this.transitioner = transitioner;
            }
            @Override
            public String toString(){
                return this.getIndex() + ":" + this.getTransitioner();
            }
        }
    }


    public FSALoader(){
        this.file = null;
        this.fileName = null;
        this.matrix = null;
        this.stringOfTheFile = null;
    }


    public char[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }

    public Scanner getFile() {
        return file;
    }

    public void setFile(Scanner file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public boolean load(){

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
        System.out.println("Stuff");
        getTransitionsOfState(2);

        return true;
    }


    private void makeArrayList(){
        this.arrayList = new State[this.numberOfStates];
        for(int i = 0; i < this.numberOfStates; i++){
            this.arrayList[i] = new State();
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




    public String getTransitionsOfState(int index){
        String transitions = new String();
        for(int i = 0; i < this.arrayList[index].transitions.size(); i++){
            transitions += "(";
            transitions += index+":";
            transitions += this.arrayList[index].transitions.get(i).toString();
            transitions += "), ";
        }
        return transitions;
    }








    private void tokenize(String buffer){
        String[] tokens = stringOfTheFile.split(";");

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

    private boolean makeMatrix(){
        this.matrix = new char[this.numberOfStates][this.numberOfStates];

        for(int i = 0; i < this.stateTransitions.length; i++){
            System.out.println(this.stateTransitions[i].charAt(1) + " " + this.stateTransitions[i].charAt(3));
            matrix[Character.getNumericValue(this.stateTransitions[i].charAt(1))][Character.getNumericValue(this.stateTransitions[i].charAt(3))] = this.stateTransitions[i].charAt(5);
        }


        return true;
    }






}
