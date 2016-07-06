import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Alex on 3/6/2016.
 */
public class FSAProcessor {
    private String buffer;
    private Scanner file;
    private int currentState;
    private boolean accepting;
    private FSALoader loader;
    private String currentSymbol;

    /**
     *
     * @param file
     */
    public FSAProcessor(File file){
        try {
            this.file = new Scanner(file);
            this.buffer = new String();
            this.buffer = this.file.nextLine();
            this.accepting = false;
            this.currentState = -1;
            this.loader = null;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }

    }
    public FSAProcessor(){
        this.file = null;
        this.buffer = new String();
        this.buffer = new String();
        this.accepting = false;
        this.currentState = -1;
        this.loader = null;
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
    public String getBuffer() {
        return buffer;
    }

    /**
     *
     * @param buffer
     */
    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    /**
     *
     * @return
     */
    public int getCurrentState() {
        return currentState;
    }

    /**
     *
     * @param currentState
     */
    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    /**
     *
     * @return
     */
    public boolean isAccepting() {
        return accepting;
    }

    /**
     *
     * @param accepting
     */
    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
    }

    /**
     *
     * @return
     */
    public FSALoader getLoader() {
        return loader;
    }

    /**
     *
     * @param loader
     */
    public void setLoader(FSALoader loader) {
        this.loader = loader;
    }

    /** main method that is called
     *
     * @return
     */
    public boolean run(){
        this.currentState = this.getLoader().getInitialState();
        for(int i = 0; i < this.buffer.length(); i++){
            this.currentSymbol = this.getBuffer().substring(i, i+1);
            boolean good = false;
            for(int j = 0; j < this.getLoader().getAlphabet().length; j++){
                if(this.getLoader().getAlphabet()[j].toString().contains(this.currentSymbol)){
                    good = true;
                    break;
                }
            }
            if(!good){
                System.out.println("Input string contains character not in alphabet!");
                return false;
            }
            String transitionsOfThisState = new String(this.getLoader().getTransitionsOfState(this.currentState));

            if(!transitionsOfThisState.contains(currentSymbol)){
                System.out.println("Illegal transitions on this input!");
                return false;
            }

            LinkedList<State.Transition> transitions = new LinkedList<>();
            transitions = this.getLoader().getState(currentState).getTransitions();
            State.Transition tmp = null;
            for(int j = 0; j < transitions.size(); j++){
                tmp = transitions.get(j);
                if(tmp.toString().contains(currentSymbol)){
                    break;
                }
            }
            this.currentState = tmp.getIndex();

        }

        for(int i = 0; i < this.getLoader().getAcceptStates().length; i++){
            if(this.currentState == this.getLoader().getAcceptStates()[i]){
                return true;
            }
        }
        return false;
    }
}
