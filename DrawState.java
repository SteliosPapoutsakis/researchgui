package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class DrawState {
    private int x;

    public int getX() {
        return x + raduis/2;
    }

    public int getY() {
        return y+ raduis/2;}

    public static int getRaduis() {
        return raduis; }

    public ArrayList<Integer> getNextStates() {
        return this.nextStates;
    }

    private int y = 65;
    private final static int raduis = 50;
    private final static javafx.scene.paint.Paint color = Color.BLUE;

    public int getStateNum() {
        return stateNum;
    }

    private int stateNum;
    private ArrayList<Integer> nextStates;

    public DrawState(int x, int statNum, ArrayList<Integer> nextStates) {
        this.x = x;
        this.stateNum = statNum;
        this.nextStates = nextStates;
    }

    public void DrawStatecircle(GraphicsContext g) {
        g.setFill(this.color);
        if (this.stateNum % 2 != 0)
            this.y = 250;
        g.fillOval(this.x, y, raduis, raduis);


        g.setFill(Color.RED);
        g.fillText("S" + this.stateNum, this.x + 7, y + 25);

    }
}
