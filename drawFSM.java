package sample;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class drawFSM {


    public void draw(ArrayList<DrawState> drawStates, GraphicsContext g)
    {
        for(DrawState state:drawStates)
        {
            state.DrawStatecircle(g);
        }



    }


    public void drawArc(DrawState state1, DrawState state2,GraphicsContext g, boolean state2IsGreater)
    {
        if(state2IsGreater)
        {

        }

    }
}
