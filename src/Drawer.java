import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

/**
 * Created by Alex on 3/10/2016.
 */
public class Drawer {

    FSAProcessor processor;
    FSALoader loader;
    final double LINE_LENGTH = 100.0;
    final double CIRCLE_RADIUS = 25.0;

    /**
     *
     * @param processor
     * @param loader
     */
    public Drawer(FSAProcessor processor, FSALoader loader) {
        this.processor = processor;
        this.loader = loader;
    }

    /** Main method for drawing
     *
     * @param primaryStage
     */
    public void draw(Stage primaryStage) {
        Group root = new Group();
        //draw line
        double y = 600 / 2;
        Line line = new Line(300, 0, 300, 100 * this.loader.getNumberOfStates());

        line.setStroke(Color.BLACK);
        line.setStrokeWidth(3);
        root.getChildren().add(line);
        double currentHeight = 100;
        double currentWidth = 300;
        double offset = 20;
        for (int i = 0; i < this.loader.getNumberOfStates(); i++) {
            boolean isAcceptState = false;
            for (int j = 0; j < this.processor.getLoader().getAcceptStates().length; j++) {
                if (i == this.processor.getLoader().getAcceptStates()[j])
                    isAcceptState = true;
            }
            Circle circle = new Circle(currentWidth, currentHeight, CIRCLE_RADIUS);

            drawSequentialDownArrow(root, currentWidth, currentHeight - CIRCLE_RADIUS);
            circle.setFill(Color.GREY);
            circle.setStroke(Color.BLACK);
            String iToS = String.valueOf(i);
            Text text = new Text(iToS);
            text.setX(currentWidth);
            text.setY(currentHeight);
            text.setBoundsType(TextBoundsType.VISUAL);
            /**
             * This is for drawing a transition from a state into itself
             */
            if (this.loader.getMatrix()[i][i] != '\0') {
                drawIntoSelf(root, currentWidth, currentHeight, this.loader.getMatrix()[i][i]);
            }

            /**
             * Places the transition symbol
             */
            if (i != this.loader.getNumberOfStates() - 1) {
                String cToS = String.valueOf(this.loader.getMatrix()[i][i + 1]);
                Text tTransitionDown = new Text(cToS);
                tTransitionDown.setX(currentWidth + 5);
                tTransitionDown.setY(currentHeight + 50);
                root.getChildren().add(tTransitionDown);
            }
            /**
             * Draws transitions for n state into n + i where i > 1. ie, States 2 into state 4.
             */
            if (this.loader.getState(i).getTransitions().size() != 0) {
                for (int j = 0; j < this.loader.getState(i).getTransitions().size(); j++) {
                    if (this.loader.getState(i).getTransitions().get(j).getIndex() > i) {
                        drawDown(root, currentWidth, currentHeight, offset, this.loader.getState(i).getTransitions().get(j).getIndex() - i, this.loader.getState(i).getTransitions().get(j).getTransitioner());
                        offset += 20;
                    }
                    else{
                        if(this.loader.getState(i).getTransitions().get(j).getIndex() < i) {
                            drawUp(root, currentWidth, currentHeight, offset, i - this.loader.getState(i).getTransitions().get(j).getIndex(), this.loader.getState(i).getTransitions().get(j).getTransitioner());
                            offset += 20;
                        }
                    }
                }
            }

            root.getChildren().add(circle);
            /**
             * if the current state is an accept state, draw another circle.
             */
            if (isAcceptState) {
                Circle acceptCircle = new Circle(currentWidth, currentHeight, CIRCLE_RADIUS - 3);
                acceptCircle.setFill(Color.GREY);
                acceptCircle.setStroke(Color.BLACK);
                root.getChildren().add(acceptCircle);
            }
            root.getChildren().add(text);
            currentHeight += 100;
        }
        primaryStage.show();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 800));
    }

    /**
     *
     * @param root
     * @param x
     * @param y
     */
    private void drawSequentialDownArrow(Group root, double x, double y) {
        Line line = new Line(x, y, x - 5, y - 5);
        Line line1 = new Line(x, y, x + 5, y - 5);
        root.getChildren().add(line);
        root.getChildren().add(line1);

    }


    /**
     *
     * @param root
     * @param x
     * @param y
     * @param transitioner
     */
    private void drawIntoSelf(Group root, double x, double y, char transitioner) {
        String iToS = String.valueOf(transitioner);
        Circle transitionToSelf = new Circle(x + CIRCLE_RADIUS, y, CIRCLE_RADIUS);
        Text tTransitioner = new Text(iToS);
        transitionToSelf.setFill(Color.WHITE);
        transitionToSelf.setStroke(Color.BLACK);
        tTransitioner.setX(x + 50);
        tTransitioner.setY(y - CIRCLE_RADIUS);
        root.getChildren().add(tTransitioner);
        root.getChildren().add(transitionToSelf);

        x = (x + x + CIRCLE_RADIUS) / 2;
        y = (y + y - CIRCLE_RADIUS - 18) / 2;
        Line up = new Line(x, y, x, y - 8);
        Line right = new Line(x, y, x + 8, y);
        root.getChildren().add(up);
        root.getChildren().add(right);
    }

    /**
     *
     * @param root
     * @param x
     * @param y
     * @param offset
     * @param statesAway
     * @param transitioner
     */
    private void drawDown(Group root, double x, double y, double offset, int statesAway, char transitioner) {
        if (statesAway == 1) return;
        Line left = new Line(x, y, x - offset, y);
        Line right = new Line(x, y + (LINE_LENGTH * statesAway), x - offset, y + (LINE_LENGTH * statesAway));
        Line down = new Line(x - offset, y, x - offset, y + (LINE_LENGTH * statesAway));


        String cToS = String.valueOf(transitioner);
        Text tTransition = new Text(cToS);
        tTransition.setX(x - offset + 3);
        tTransition.setY((2 * (y) + (LINE_LENGTH * statesAway)) / 2 - 15);
        //draw arrow
        y = y + (LINE_LENGTH * statesAway);
        x = x - CIRCLE_RADIUS;
        Line diagonalUp = new Line(x, y, x - 5, y - 5);
        Line diagonalDown = new Line(x, y, x - 5, y + 5);
        root.getChildren().addAll(diagonalDown, diagonalUp, left, right, down, tTransition);
    }

    private void drawUp(Group root, double x, double y, double offset, int statesAway, char transitioner){
        if (statesAway == 1) return;
        Line left = new Line(x, y - 5, x - offset, y - 5);
        Line right = new Line(x - CIRCLE_RADIUS, y - (LINE_LENGTH * statesAway) - 5, x - offset, y - (LINE_LENGTH * statesAway) - 5);
        Line up = new Line(x - offset, y - 5, x - offset, y - (LINE_LENGTH * statesAway) - 5);


        String cToS = String.valueOf(transitioner);
        Text tTransition = new Text(cToS);
        tTransition.setX(x - offset + 3);
        tTransition.setY(((y + y -(LINE_LENGTH*statesAway))/2 - 15));

        //draw arrow
        x = x - CIRCLE_RADIUS;
        y = y - (LINE_LENGTH*statesAway) - 5;
        Line diagonalUp = new Line(x, y , x - 5, y - 5);
        Line diagonalDown = new Line(x, y, x - 5, y + 5);
        root.getChildren().addAll(diagonalDown, diagonalUp, left, right, up, tTransition);

    }
}
