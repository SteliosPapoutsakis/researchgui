package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import org.omg.CORBA.MARSHAL;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class drawFSM {
    private final static int CANVIS_WIDTH = 600;
    private final static int CANVIS_HIEGHT = 410;


    public void draw(ArrayList<DrawState> drawStates, GraphicsContext g,
                     HashMap<Integer, Hashtable<String, Integer>> conditionsMap) {


        //clears the canvis for new drawing
        // g.clearRect(0, 0, CANVIS_WIDTH, CANVIS_HIEGHT);
        for (DrawState state : drawStates) {
            for (int nextState : conditionsMap.get(state.getStateNum()).values()) {
                //if the next state has been defined yet, dont draw it
                if (nextState > drawStates.size()) continue;
                // if both the states are on the same line
                if (state.getStateNum() < 26) {
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
            g.setStroke(Color.BLACK);
            g.strokeArc(state1.getX(), state1.getY() - 50, 30, 45, 0, 360, ArcType.OPEN);
            return;
        }
        //else we have to draw an arc

        int y = getY(state1, state2);
        int x = state1.getX();
        int height = (state1.getY() - y) * 2;
        int width = (state2.getX()) - (
                state1.getX());
        int startAngle = 0;
        int arcExtend = 180;
        //if we are branching backwards, draw arc upsidedown
        if (state1.getStateNum() > state2.getStateNum()) {
            x = state2.getX();
            startAngle = 180;
            width = (state1.getX() - (
                    state2.getX()));
            g.setStroke(Color.RED);
            g.strokeArc(x, y, width, height, startAngle, arcExtend, ArcType.OPEN);
            g.strokeLine(width / 2 + x, y+height, x + width / 2 + 5, y+height - 3);
            g.strokeLine(width / 2 + x, y+height, x + width / 2 + 5, y+height + 3);


        } else {
            g.setStroke(Color.BLACK);
            g.strokeArc(x, y, width, height, startAngle, arcExtend, ArcType.OPEN);
            g.strokeLine(width / 2 + x, y, x + width / 2 - 5, y - 3);
            g.strokeLine(width / 2 + x, y, x + width / 2 - 5, y + 3);

        }


    }

    //to draw a line between states on differet y axis
    public static void drawLine(DrawState state1, DrawState state2, GraphicsContext g) {
        if (state1.getStateNum() > state2.getStateNum())
            g.setStroke(Color.RED);
        else
            g.setStroke(Color.BLACK);
        g.strokeLine(state1.getX(), state1.getY(), state2.getX(), state2.getY());

    }

    public static int getY(DrawState state1, DrawState state2) {
        // equation to make the max point of arc 10 pixels higher per state jumped
        int y = -10 * (int) (Math.abs(state1.getStateNum() - state2.getStateNum()) - 1)
                + state1.getY() - DrawState.getRaduis();
        if (y < 0)
            return 0;
        return y;
    }

}















/*
old code
 */




//    //purpose is to draw the tiny arrow to signify direction
//    public static void drawTinyArrowArc(DrawState state1, DrawState state2, int width, int y, int height,
//                                        GraphicsContext g) {
//        g.setStroke(Color.RED);
//        if (state1.getStateNum() > state2.getStateNum()) {
//            int mainx = width / 2 + state2.getX();
//            g.strokeLine(mainx, y + height, mainx + 5, y + height - 6);
//            g.strokeLine(mainx, y + height, mainx + 5, y + height + 6);
//        } else if (state1.getStateNum() < state2.getStateNum()) {
//            int mainx = width / 2 + state1.getX();
//            g.strokeLine(mainx, y, mainx - 5, y - 6);
//            g.strokeLine(mainx, y, mainx - 5, y + 6);
//        }
//        g.setStroke(Color.BLACK);
//    }
//
//    public void drawLegend(GraphicsContext g) {
//        g.setLineWidth(.8);
//        g.setStroke(Color.BLACK);
//        g.setFont(new Font(10));
//        g.strokeText("Legend", 25, CANVIS_HIEGHT - 76);
//        g.strokeLine(5, CANVIS_HIEGHT - 63, 12, CANVIS_HIEGHT - 63);
//        g.strokeText("= Lower State to Greater State", 15, CANVIS_HIEGHT - 60);
//        g.setStroke(Color.RED);
//        g.strokeLine(5, CANVIS_HIEGHT - 53, 12, CANVIS_HIEGHT - 53);
//        g.strokeText("= Greater State to Lower State", 15, CANVIS_HIEGHT - 50);
//    }
//
//}
