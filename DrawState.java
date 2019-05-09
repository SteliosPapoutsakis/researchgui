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
        return raduis/2; }

    public ArrayList<Integer> getNextStates() {
        return this.nextStates;
    }

    private int y;
    private final static int raduis = 50;


    private final static javafx.scene.paint.Paint color = Color.BLUE;

    public int getStateNum() {
        return stateNum;
    }

    private int stateNum;
    private ArrayList<Integer> nextStates;

    public DrawState(int x, int statNum, ArrayList<Integer> nextStates) {
        this.x = x;
        this.y = (nextStates.size() > 26 && statNum < 26)?65:
                (nextStates.size() > 25 && statNum > 26)?250:150;
        this.stateNum = statNum;
        this.nextStates = nextStates;

    }

    public void DrawStatecircle(GraphicsContext g) {
        g.setFill(this.color);
        g.fillOval(this.x, y, raduis, raduis);


        g.setFill(Color.RED);
        g.fillText("S" + this.stateNum, this.x + 7, y + 25);

    }

    //method too see if an (x,y) is located within the state
    public boolean Contains(int x, int y)
    {
        if(x >= this.x && x<= this.x + raduis)
        {
            if(y >= this.y && y <= this.y + raduis)
                return true;
        }
        return false;
    }

}
