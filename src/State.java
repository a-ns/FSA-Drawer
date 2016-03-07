import java.util.LinkedList;

public class State{

    LinkedList<Transition> transitions;
    int index;
    public State(){
        this.transitions = new LinkedList<Transition>();
    }
    public State(int index){
        this.transitions = new LinkedList<>();
        this.index = index;
    }
    public void addTransition(int index, char transitioner){
        Transition tmp = new Transition(index, transitioner);
        this.transitions.add(tmp);
        return;
    }


    public class Transition{

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

    public LinkedList<Transition> getTransitions(){
        return this.transitions;
    }
}