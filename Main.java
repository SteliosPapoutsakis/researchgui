package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Hashtable;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent p = loader.load();

        //getting the controller
        GuiController controller = loader.getController();



        //setting up all the intial values of GUI objects, starting with conditional menu
        controller.conditionMenu.getItems().add("Greater than");
        controller.conditionMenu.getItems().add("Equal to");
        controller.conditionMenu.getItems().add("Not Equal to");
        controller.conditionMenu.getItems().add("Less than");

        //controller.canvas.setWidth(3000);

        controller.tabFSM.setOnSelectionChanged(new TabListener(controller.tabFSM,
                controller.canvas.getGraphicsContext2D(),controller.states));


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
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999,0);
        SpinnerValueFactory<Integer>  nextStateNum =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0);
        controller.sizeInOutSpinner.setValueFactory(sizeInOut);
        controller.stateNumSpinner.setValueFactory(stateNum);
        controller.conditionSpinner.setValueFactory(nextStateNum);

        //set visiablity
        controller.Operator.setVisible(false);
        controller.Assign2TextBox.setVisible(false);
        controller.conditionTextbox.setVisible(false);
        controller.conditionMenu.setVisible(false);
        controller.fLabel.setVisible(false);
        controller.conditionLabel.setVisible(false);
        controller.conditiontext2.setVisible(false);

        //intiatting hastablles for state 0
        controller.conditions.put(0,new Hashtable<>());
        controller.assignments.put(0,new Hashtable<>());
        controller.states.add(new DrawState(50,0,new ArrayList<Integer>()));
        controller.assigmentState = controller.assignments.get(0);
        controller.conditionsState = controller.conditions.get(0);

        //making name assigments not editable
        controller.NameAssignmentsTextBox.setEditable(false);
        controller.stateNumbers.add(0);




        primaryStage.setTitle("FSM Generator");
        primaryStage.setScene(new Scene(p, 600, 410));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
