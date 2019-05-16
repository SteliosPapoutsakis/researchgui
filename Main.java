package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent p = loader.load();

            //getting the controller
            GuiController controller = loader.getController();

            //set on click listener for canvis
            controller.canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                for (DrawState state : controller.states) {

                //looking to see if the state was clicked
                    if (state.Contains((int) e.getX(), (int) e.getY())) {
                        String stateAssignemts = "";
                        Hashtable<String, String> a = controller.assignments.get(state.getStateNum());
                     //getting all the assigments
                        for (String str : a.keySet()) {
                            stateAssignemts = stateAssignemts + controller.variableType.get(str) + " " +
                                    str + " = " + a.get(str) + "\n";


                        }
                        // help from https://stackoverflow.com/questions/43623278/joptionpane-bigger
                        UIManager.put("OptionPane.minimumSize", new Dimension(500, 200));
                        //help from https://stackoverflow.com/questions/4017042/how-to-enlarge-buttons-on-joptionpane-dialog-boxes
                        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL", Font.PLAIN, 20)));

                        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 25));
                        JOptionPane.showMessageDialog(null, stateAssignemts,
                                "State Assigment: " + state.getStateNum()
                                , JOptionPane.INFORMATION_MESSAGE);

                    }
                }

            });


            //setting up all the intial values of GUI objects, starting with conditional menu
            controller.conditionMenu.getItems().add("Greater than");
            controller.conditionMenu.getItems().add("Equal to");
            controller.conditionMenu.getItems().add("Not Equal to");
            controller.conditionMenu.getItems().add("Less than");

            //controller.canvas.setWidth(3000);

            controller.tabFSM.setOnSelectionChanged(new TabListener(controller.tabFSM,
                    controller.canvas.getGraphicsContext2D(), controller.states, controller.conditions));


            //input output reg
            controller.inputOutputMenu.getItems().add("Input");
            controller.inputOutputMenu.getItems().add("Output");
            controller.inputOutputMenu.getItems().add("Register");

            //operatations
            controller.operatorsMenu.getItems().addAll("None");
            controller.operatorsMenu.getItems().addAll("+");
            controller.operatorsMenu.getItems().addAll("-");
            controller.operatorsMenu.getItems().addAll("*");
            controller.operatorsMenu.getItems().addAll("/");
            controller.operatorsMenu.getItems().addAll("&&");


            // set spinner values help from https://o7planning.org/en/11185/javafx-spinner-tutorial
            SpinnerValueFactory<Integer> sizeInOut =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1
                            , 99, 0);
            SpinnerValueFactory<Integer> stateNum =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0);
            SpinnerValueFactory<Integer> nextStateNum =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0);
            controller.sizeInOutSpinner.setValueFactory(sizeInOut);
            controller.stateNumSpinner.setValueFactory(stateNum);
            controller.conditionSpinner.setValueFactory(nextStateNum);

            //set visiablity
            controller.Operator.setVisible(false);
            controller.Assign2TextBox.setVisible(false);
            controller.conditionRegSelect.setVisible(false);
            controller.conditionMenu.setVisible(false);
            controller.fLabel.setVisible(false);
            controller.conditionLabel.setVisible(false);
            controller.conditiontext2.setVisible(false);

            //intiatting hastablles for state 0
            controller.conditions.put(0, new Hashtable<>());
            controller.assignments.put(0, new Hashtable<>());
            controller.states.add(new DrawState(50, 0));
            controller.assigmentState = controller.assignments.get(0);
            controller.conditionsState = controller.conditions.get(0);

            //making name assigments not editable
            controller.NameAssignmentsTextBox.setEditable(false);
            controller.stateNumbers.add(0);


            primaryStage.setTitle("FSM Generator");
            primaryStage.setScene(new Scene(p, 600, 410));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
