package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import org.omg.CORBA.MARSHAL;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class drawFSM {
    private final int CANVIS_WIDTH = 600;
    private final int CANVIS_HIEGHT = 410;


    public void draw(ArrayList<DrawState> drawStates, GraphicsContext g,
                     HashMap<Integer, Hashtable<String, Integer>> conditionsMap) {


        //clears the canvis for new drawing
       // g.clearRect(0, 0, CANVIS_WIDTH, CANVIS_HIEGHT);
        for (DrawState state : drawStates) {
            for (int nextState : conditionsMap.get(state.getStateNum()).values()) {
                //if the next state has been defined yet, dont draw it
                if (nextState > drawStates.size()) continue;
                // if both the states are on the same line
                if (state.getStateNum() % 2 == 0 && nextState % 2 == 0 || state.getStateNum() % 2 == 1 &&
                        nextState % 2 == 1) {
                    drawArc(state, drawStates.get(nextState), g);
                } else {
                    drawLine(state, drawStates.get(nextState), g);
                }

            }
        }

        for (DrawState state : drawStates) {
            state.DrawStatecircle(g);
        }



    }


    public static void drawArc(DrawState state1, DrawState state2, GraphicsContext g) {
        //draws a loop if the state1 == state2
        if (state1.getStateNum() == state2.getStateNum()) {
            g.strokeArc(state1.getX(), state1.getY() - 50, 30, 45, 0, 360, ArcType.OPEN);
            return;
        }
        //else we have to draw an arc

        int y = getY(state1, state2);
        int x = state1.getX();
        int height = (state1.getY() - y) * 2;
        int width = (state2.getX() ) - (
                state1.getX());
        int startAngle = 0;
        int arcExtend = 180;
        if (state1.getStateNum() > state2.getStateNum()) {
            x = state2.getX() ;
            startAngle = 180;
            width = (state1.getX()  - (
                    state2.getX()));

        }

        g.strokeArc(x, y, width, height, startAngle, arcExtend, ArcType.OPEN);
        drawTinyArrowArc(state1,state2,width,y,height,g);


        }

    //to draw a line between states on differet y axis
    public static void drawLine(DrawState state1, DrawState state2, GraphicsContext g) {
        g.strokeLine(state1.getX(), state1.getY(), state2.getX(), state2.getY());
    }

    public static int getY(DrawState state1, DrawState state2) {
        // equation to make the max point of arc 10 pixels higher per state jumped
        int y = -10 * (int) (Math.abs(state1.getStateNum() / 2.0 - state2.getStateNum() / 2.0) - 1)
                + state1.getY() - DrawState.getRaduis() / 2;
        if (y < 0)
            return 0;
        return y;
    }

    //purpose is to draw the tiny arrow to signify direction
    public static void drawTinyArrowArc(DrawState state1, DrawState state2,int width, int y,int height,
                                        GraphicsContext g) {
        g.setStroke(Color.RED);
        if(state1.getStateNum() > state2.getStateNum())
        {
            int mainx = width/2 + state2.getX();
            g.strokeLine(mainx,y+height,mainx+5,y+height-6);
            g.strokeLine(mainx,y+height,mainx+5,y+height+6);
        }
        else if(state1.getStateNum()< state2.getStateNum()){
            int mainx = width/2 + state1.getX();
            g.strokeLine(mainx,y,mainx-5,y-6);
            g.strokeLine(mainx,y,mainx-5,y+6);
        }
        g.setStroke(Color.BLACK);
    }

}
