package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class drawFSM {
    private final static int CANVIS_WIDTH = 600;
    private final static int CANVIS_HIEGHT = 410;


    public void draw(ArrayList<DrawState> drawStates, GraphicsContext g,
                     HashMap<Integer, Hashtable<String, Integer>> conditionsMap) {

//need to call this to update the y value when switching to two row layout
        if(drawStates.size() > 26)
        {
            for(DrawState state: drawStates)
                state.updateY();


        }
        //clears the canvis for new drawing
        g.clearRect(0, 0, CANVIS_WIDTH, CANVIS_HIEGHT);
        for (DrawState state : drawStates) {
            for (int nextState : conditionsMap.get(state.getStateNum()).values()) {

                //if the next state has been defined yet, dont draw it
                if (nextState > drawStates.size()-1) continue;
                // if both the states are on the same line
                if ((state.getStateNum() < 26 && drawStates.get(nextState).getStateNum()< 26)
                        ||(state.getStateNum() > 25 && drawStates.get(nextState).getStateNum() > 25)) {
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
            g.strokeLine(state1.getX()+15,state1.getY()-50,state1.getX()+10,state1.getY()-53);
            g.strokeLine(state1.getX()+15,state1.getY()-50,state1.getX()+10,state1.getY()-47);
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
        boolean isLowToHigh;
        double[] function = getLinearFunction(state1,state2);
        double centerX;
        double centerY =  (state2.getY()+state1.getY())/2;
        if (state1.getStateNum() > state2.getStateNum()) {
            double staringX = (state1.getX() > state2.getX())? state2.getX():state1.getX();
            g.setStroke(Color.RED);
            centerX = staringX + Math.abs(state1.getX()-state2.getX())/2;
            isLowToHigh = false;

        }
        else {
            double staringX = (state1.getX() > state2.getX())? state2.getX():state1.getX();
            centerX = staringX + Math.abs(state1.getX()-state2.getX())/2;
            g.setStroke(Color.BLACK);
            isLowToHigh = true;
        }
        g.strokeLine(state1.getX(), state1.getY(), state2.getX(), state2.getY());
        drawDirectionalArrows(g,function,isLowToHigh,centerX,centerY);


    }

    public static int getY(DrawState state1, DrawState state2) {
        // equation to make the max point of arc 10 pixels higher per state jumped
        int y = -10 * (int) (Math.abs(state1.getStateNum() - state2.getStateNum()) - 1)
                + state1.getY() - DrawState.getRaduis();
        if (y < 0)
            return 0;
        return y;
    }
// needed to draw the arrows when the states are between 2 rows
    public static double[] getLinearFunction(DrawState state1, DrawState state2)
    {
        // this is for whichever state is on top
        //if the two states are more less 26 appart we know we want the slope to be neg
        double[] function = new double[2];
        int truNum;

        if(state1.getStateNum() < state2.getStateNum())
            truNum = (state1.getStateNum()+26 < state2.getStateNum())?-1:1;
        else
            truNum = (state2.getStateNum()+26 < state1.getStateNum())?-1:1;

        double xDiff = Math.abs(state1.getX()-state2.getX());
        double yDIff = (state2.getY() > state1.getY())?state2.getY()-state1.getY()
                :state1.getY()-state2.getY();
        function[0] = truNum * yDIff/xDiff;

        //getting the y intercept, also needing to adjust because (0,0) is in TOP LEFT not BOTTOM LEFT

        int uupdatedY = CANVIS_HIEGHT-state1.getY();
        function[1] = uupdatedY-function[0]*state1.getX();


        return function;

    }

// purpose is to draw the directional arrows for next state transitions that occur from states
    //between 2 rows
    public static void drawDirectionalArrows(GraphicsContext g, double[] function, boolean lowToHigh
    ,double xCenter,double yCenter)
    {
        double xNew = (lowToHigh)?xCenter-5:xCenter+5;
        double updatedY = function[0] * (xNew) + function[1];
        updatedY = CANVIS_HIEGHT-updatedY;
        double runGain = xNew-xCenter;
        double riseGain = yCenter-updatedY;
        double radius = Math.sqrt(runGain*runGain+riseGain*riseGain);
        double theta = Math.atan(riseGain/runGain);
        theta = (runGain > 0)?Math.PI*2+theta:Math.PI+theta;


        double updatedX0 = radius * Math.cos(theta+Math.PI/6);
        double updatedY0 = radius * Math.sin(theta+Math.PI/6);

        double updatedX1 = radius * Math.cos(theta-Math.PI/6);
        double updatedY1 = radius * Math.sin(theta-Math.PI/6);

        g.strokeLine(xCenter,yCenter,xCenter+updatedX0,yCenter-updatedY0);
        g.strokeLine(xCenter,yCenter,xCenter+updatedX1,yCenter-updatedY1);

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
