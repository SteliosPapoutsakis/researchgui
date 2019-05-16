package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class DrawState {
    private int x;

    private static boolean twoRows = false;

    public int getX() {
        return x + raduis/2;
    }

    public int getY() {
        return y+ raduis/2;}

    public static int getRaduis() {
        return raduis/2; }



    private int y;
    private final static int raduis = 50;


    private final static javafx.scene.paint.Paint color = Color.BLUE;

    public int getStateNum() {
        return stateNum;
    }

    public void setStateNum(int stateNum) {
        this.stateNum = stateNum;
    }

    private int stateNum;


    public DrawState(int x, int statNum) {
        this.x = x;
        this.y = (statNum > 25)?250:150;
        if(statNum > 25) twoRows = true;
        this.stateNum = statNum;

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


//this is necessary to adjust the y component if two rows are need for states
    public void updateY(){
        this.y = (this.stateNum < 26)?65:this.y;
    }

}
