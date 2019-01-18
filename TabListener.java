package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class TabListener implements EventHandler<Event> {
    private Tab fsm;
    private GraphicsContext g;
    private ArrayList<DrawState> drawings;
    private drawFSM fsmDrawing = new drawFSM();
    private HashMap<Integer,Hashtable<String,Integer>> conditionsMap;

    public TabListener(Tab fsm, GraphicsContext g, ArrayList<DrawState> drawings,HashMap<Integer
            ,Hashtable<String,Integer>> conditionsMap) {
        this.g = g;
        this.fsm = fsm;
        this.drawings = drawings;
        this.conditionsMap = conditionsMap;
    }



    @Override
    public void handle(Event event) {

        if (this.fsm.isSelected())
        {

          this.fsmDrawing.draw(this.drawings,this.g,this.conditionsMap);
//
//            DrawState sate1 = this.drawings.get(0);
//            DrawState sate2 = this.drawings.get(7);
//            g.setFill(Color.RED);
//            g.strokeArc(sate1.getX(),10,100,110,0,180,ArcType.OPEN);
//
//
//            for(DrawState state:this.drawings)
//            {
//                state.DrawStatecircle(g);
//            }

        }

    }
}
