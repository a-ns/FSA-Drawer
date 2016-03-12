import java.util.LinkedList;

public class State{

    LinkedList<Transition> transitions;
    int index;
    public State(){
        this.transitions = new LinkedList<Transition>();
    }

    /**
     *
     * @param index If the state is state 0, then index is 0.
     */
    public State(int index){
        this.transitions = new LinkedList<>();
        this.index = index;
    }

    /**
     *
     * @param index
     * @param transitioner
     */
    public void addTransition(int index, char transitioner){
        Transition tmp = new Transition(index, transitioner);
        this.transitions.add(tmp);
        return;
    }

    /**
     * Inner class for State. Transition is public because I couldn't figure out how to get it to work if it was private.
     */
    public class Transition{

        private int index;
        private char transitioner;

        /**
         *
         * @param index
         * @param transitioner
         */
        public Transition(int index, char transitioner){
            this.index = index;
            this.transitioner = transitioner;
        }

        /**
         *
         * @return
         */
        public int getIndex() {
            return index;
        }

        /**
         *
         * @param index
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         *
         * @return
         */
        public char getTransitioner() {
            return transitioner;
        }

        /**
         *
         * @param transitioner
         */
        public void setTransitioner(char transitioner) {
            this.transitioner = transitioner;
        }
        @Override
        public String toString(){
            return this.getIndex() + ":" + this.getTransitioner();
        }

    }

    /**
     *
     * @return int index of where the current node is
     */
    public int getIndex(){
        return this.index;
    }

    @Override
    public String toString(){
        String transitions = new String();
        for(int i = 0; i < this.transitions.size(); i++){
            transitions += "(";
            transitions += this.transitions.get(i);
            transitions += "), ";
        }
        transitions = transitions.substring(0, transitions.length() - 2);
        return transitions;
    }

    /**
     *
     * @return LinkedList<Transition> of the current state
     */
    public LinkedList<Transition> getTransitions(){
        return this.transitions;
    }
}