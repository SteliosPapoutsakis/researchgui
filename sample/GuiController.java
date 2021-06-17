package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.*;

import Framework.RunProgram;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static jdk.nashorn.internal.objects.Global.print;


public class GuiController {
/***
 * if you edit a outptut/reg it will cicle and extra time
 */
    /***
     * BELOW ARE THE VARIABLES USED TO HOLD INFO
     */
    private String fsmTitle;
    protected ArrayList<Integer> stateNumbers = new ArrayList();
    protected HashMap<Integer, Hashtable<String, Integer>> conditions = new HashMap<>();
    protected HashMap<Integer, Hashtable<String, String>> assignments = new HashMap<>();
    private HashMap<String, Integer> variableSize = new HashMap<>();
    protected HashMap<String, String> variableType = new HashMap<>();
    private ArrayList<String> inputs = new ArrayList<>();
    private ArrayList<String> outputs = new ArrayList<>();
    private ArrayList<String> registers = new ArrayList<>();
    private ArrayList<String> assigns = new ArrayList<>();
    protected Hashtable<String, String> assigmentState;
    protected Hashtable<String, Integer> conditionsState;
    protected ArrayList<DrawState> states = new ArrayList<>();
    protected ArrayList<ArrayList<String>> conditionsOrder = new ArrayList<>();



    private int indexInput = 0;
    private int indexOutput = 0;
    private int indexRegister = 0;
    private int indexAssign = 0;
    private int indexConditions = 0;
    private boolean didEdit =false;
    private String fsmSTRING;

    /**
     * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     */

    @FXML
    protected Spinner<Integer> stateNumSpinner;
    @FXML
    protected Label conditionLabel;
    @FXML
    protected TextField conditiontext2;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;




    @FXML
    protected ComboBox<String> inputOutputMenu;

    @FXML
    protected ComboBox<String> conditionRegSelect;

   protected FileChooser load = new FileChooser();


    @FXML
    private RadioButton varRadio;

    @FXML
    private RadioButton regRadio;


    @FXML
    protected ComboBox<String> operatorsMenu;

    @FXML
    protected Spinner<Integer> sizeInOutSpinner;

    @FXML
    private TextField nameVarRegTextBox;



    @FXML
    private TextField title;



    @FXML
    private Button InputTitleButton;

    @FXML
    protected TextField NameAssignmentsTextBox;

    @FXML
    private TextField Assign1TextBox;


    @FXML
    protected TextField Assign2TextBox;

    @FXML
    protected Label Operator;




    @FXML
    private RadioButton conditionRadio;



    @FXML
    protected ComboBox<String> conditionMenu;


    @FXML
    protected Spinner<Integer> conditionSpinner;

    @FXML
    protected Label fLabel;






    @FXML
    protected Canvas canvas;

    @FXML
    protected Tab tabFSM;


    @FXML
        //inputing values
    void InputButton(ActionEvent event) {

        if (this.inputOutputMenu.getValue()!=null) {
            //checking to see if input
            if (this.nameVarRegTextBox.getText().length() > 0 &&
                    (this.varRadio.isSelected() || this.regRadio.isSelected())) {

                if (this.inputOutputMenu.getValue().equals("Input")) {
                    //if an output or reg already exists with the same name, do not input it
                    if (this.registers.contains(this.nameVarRegTextBox.getText()) ||
                            this.outputs.contains(this.nameVarRegTextBox.getText())) {
                        Warnings w = new Warnings("You cannot have duplicate names", "Duplicate Name Warning");
                        Thread t = new Thread(w);
                        t.run();
                        this.nameVarRegTextBox.clear();
                        return;
                    }

                    //if a one bit value, add to the list of conditional values
                    if (this.sizeInOutSpinner.getValue() == 1) {
                        this.conditionRegSelect.getItems().addAll("Var " + this.nameVarRegTextBox.getText());
                    }

                    //puts values of an input in tables and arrayList
                    if (!this.inputs.contains(this.nameVarRegTextBox.getText()))
                        this.inputs.add(this.nameVarRegTextBox.getText());
                    this.variableType.put(this.nameVarRegTextBox.getText(), "Var");
                    this.variableSize.put(this.nameVarRegTextBox.getText(), this.sizeInOutSpinner.getValue());
                    //check if output
                } else if (this.inputOutputMenu.getValue().equals("Output")) {

                    //check if a name is a dublicate
                    if (this.registers.contains(this.nameVarRegTextBox.getText()) ||
                            this.inputs.contains(this.nameVarRegTextBox.getText())) {
                        Warnings w = new Warnings("You cannot have duplicate names", "Duplicate Name Warning");
                        Thread t = new Thread(w);
                        t.run();
                        this.nameVarRegTextBox.clear();
                        return;
                    }
                    //if an input or register removes it from list
                    this.inputs.remove(this.nameVarRegTextBox);
                    this.registers.remove(this.nameVarRegTextBox);


                    //puts vales of a output into tables and arrayList
                    if (!this.outputs.contains(this.nameVarRegTextBox.getText()))
                        this.outputs.add(this.nameVarRegTextBox.getText());
                    if (this.varRadio.isSelected()) {
                        this.variableType.put(this.nameVarRegTextBox.getText(), "Var");


                    } else {

                        // go through all reg to see if same size
                        for (String str : this.outputs) {
                            if (str.equals(this.nameVarRegTextBox.getText())) continue;
                            // sees if the output is a reg
                            if (this.variableType.get(str).equals("Reg")) {
                                if (this.variableSize.get(str) != this.sizeInOutSpinner.getValue()) {
                                    Warnings w = new Warnings("You cannot have registers that are different sizes", "Register " +
                                            "Size Warning");
                                    Thread t = new Thread(w);
                                    t.run();

                                    //if we hit this get out of this method
                                    this.outputs.remove(this.nameVarRegTextBox.getText());
                                    this.nameVarRegTextBox.clear();
                                    return;

                                }
                            }


                        }
                        // check in registers too

                        for (String str : this.registers) {
                            if (this.variableSize.get(str) != this.sizeInOutSpinner.getValue()) {
                                Warnings w = new Warnings("You cannot have registers that are different sizes", "Register " +
                                        "Size Warning");
                                Thread t = new Thread(w);
                                t.run();
                                this.outputs.remove(this.nameVarRegTextBox.getText());
                                this.nameVarRegTextBox.clear();
                                return;


                            }
                        }
                        this.variableType.put(this.nameVarRegTextBox.getText(), "Reg");
                        //put the reg in the conditional values
                        this.conditionRegSelect.getItems().addAll("Reg " + this.nameVarRegTextBox.getText());
                    }
                    this.variableSize.put(this.nameVarRegTextBox.getText(), this.sizeInOutSpinner.getValue());

                    //check if reg
                } else if (this.inputOutputMenu.getValue().equals("Register")) {

                    //check if a name is a dublicate
                    if (this.outputs.contains(this.nameVarRegTextBox.getText()) ||
                            this.inputs.contains(this.nameVarRegTextBox.getText())) {
                        Warnings w = new Warnings("You cannot have duplicate names", "Duplicate Name Warning");
                        Thread t = new Thread(w);
                        t.run();
                        this.nameVarRegTextBox.clear();
                        return;
                    }
                    //if an output or input removes it from list
                    this.outputs.remove(this.nameVarRegTextBox);
                    this.inputs.remove(this.nameVarRegTextBox);

                    //puts values of an registers in tables and arrayList
                    if (!this.registers.contains(this.nameVarRegTextBox.getText()))
                        this.registers.add(this.nameVarRegTextBox.getText());


                    // go through all regs to see if same size
                    for (String str : this.registers) {
                        if (str.equals(this.nameVarRegTextBox.getText())) continue;
                        if (this.variableSize.get(str) != this.sizeInOutSpinner.getValue()) {
                            Warnings w = new Warnings("You cannot have registers that are different sizes", "Register " +
                                    "Size Warning");
                            Thread t = new Thread(w);
                            t.run();

                            //if we hit this get out of this method + remve from arrray
                            this.registers.remove(this.nameVarRegTextBox.getText());
                            this.nameVarRegTextBox.clear();
                            return;

                        }
                    }

                    // now check the output registers if inputing a reg
                    for (String str : this.outputs) {
                        // sees if the output is a reg
                        if (this.variableType.get(str).equals("Reg")) {
                            if (this.variableSize.get(str) != this.sizeInOutSpinner.getValue()) {
                                Warnings w = new Warnings("You cannot have registers that are different sizes", "Register " +
                                        "Size Warning");
                                Thread t = new Thread(w);
                                t.run();

                                //if we hit this get out of this method + remove from array
                                this.registers.remove(this.nameVarRegTextBox.getText());
                                this.nameVarRegTextBox.clear();
                                return;

                            }
                        }


                    }

                    this.variableType.put(this.nameVarRegTextBox.getText(), "Reg");
                    this.conditionRegSelect.getItems().addAll("Reg " + this.nameVarRegTextBox.getText());
                    this.variableSize.put(this.nameVarRegTextBox.getText(), this.sizeInOutSpinner.getValue());

                }
                //put the value in assigns if output or reg
                if ((this.outputs.contains(this.nameVarRegTextBox.getText())
                        || this.registers.contains(this.nameVarRegTextBox.getText()))
                        && !(this.assigns.contains(this.nameVarRegTextBox.getText()))) {
                    this.assigns.add(this.nameVarRegTextBox.getText());
                    this.assigmentState.put(this.nameVarRegTextBox.getText(), "");
                }
                this.nameVarRegTextBox.clear();
            }

            //resets the index for viewing inputs
            this.indexInput = 0;
            this.indexOutput = 0;
            this.indexRegister = 0;
            this.didEdit = true;


            if (!this.assigns.isEmpty())
                this.NameAssignmentsTextBox.setText(this.assigns.get(0));
        } else {
            Warnings w = new Warnings("You Must Select a Variable Type", "No Variable Type");
            Thread t = new Thread(w);
            t.run();
        }
    }


    //printing title of FSM
    @FXML
    void InputtitleButton(ActionEvent event) {
        if (event.getSource().equals(this.InputTitleButton) && this.title.getText().length() > 0)
            this.fsmTitle = this.title.getText();
        this.title.clear();
        this.didEdit=true;

    }

    @FXML
    void buttonClick(ActionEvent event) {
        String condition1 = "";
        String condition2 = "";
        //if radio button is selected
        if (this.conditionRadio.isSelected()) {
            condition1 = this.conditionRegSelect.getValue().replaceAll("Var ","Var").replaceAll
                    ("Reg ","Reg");
            condition2 = this.conditiontext2.getText();
            // check to see if condition is a variable/reg
            if (this.variableType.containsKey(condition2))
                condition2 = this.variableType.get(condition2)+condition2;
        } else {
            this.conditionsState.put("NOCON", this.conditionSpinner.getValue());
            this.didEdit = true;
            this.conditionsOrder.get(this.stateNumSpinner.getValue()).add("NOCON");
            // add the next state to the drawing fsm to be drawn
            //  this.states.get(this.stateNumSpinner.getValue()).getNextStates().add(this.conditionSpinner.getValue());
            return;
        }
        int additional = 1;


        //fixes problem, now can have multiple branching with same variable
        if(this.conditionsState.containsKey(condition1 +  this.conditionLabel.getText()
                + condition2))
        {
            while(this.conditionsState.containsKey(condition1 +  this.conditionLabel.getText()
                    + condition2 + additional))
            {
                additional++;
            }
            this.conditionsState.put(condition1 +  this.conditionLabel.getText()
                    +  condition2 + "%"+additional, this.conditionSpinner.getValue());
            this.conditionsOrder.get(this.stateNumSpinner.getValue()).add(
                    condition1 +  this.conditionLabel.getText()
                    + condition2 + "%"+additional);
        }
        else {
            this.conditionsOrder.get(this.stateNumSpinner.getValue()).add(condition1 + this.conditionLabel.getText()
                    +  condition2);
            this.conditionsState.put(condition1 + this.conditionLabel.getText()
                    +  condition2, this.conditionSpinner.getValue());
        }

        this.conditiontext2.clear();
        this.didEdit=true;
        // add the next state to the drawing fsm to be drawn

    }


    @FXML
    void operation(ActionEvent event) {
        //if none, make the labels visable
        if (this.operatorsMenu.getValue().equals("None")) {
            this.Operator.setVisible(false);
            this.Assign2TextBox.setVisible(false);
        }
        // do the same for the other operations
        else if (this.operatorsMenu.getValue().equals("*")) {
            this.Operator.setVisible(true);
            this.Assign2TextBox.setVisible(true);
            this.Operator.setText("*");
        } else if (this.operatorsMenu.getValue().equals("/")) {
            this.Operator.setVisible(true);
            this.Assign2TextBox.setVisible(true);
            this.Operator.setText("/");
        } else if (this.operatorsMenu.getValue().equals("+")) {
            this.Operator.setVisible(true);
            this.Assign2TextBox.setVisible(true);
            this.Operator.setText("+");
        } else if (this.operatorsMenu.getValue().equals("-")) {
            this.Operator.setVisible(true);
            this.Assign2TextBox.setVisible(true);
            this.Operator.setText("-");
        } else if (this.operatorsMenu.getValue().equals("&")) {
            this.Operator.setVisible(true);
            this.Assign2TextBox.setVisible(true);
            this.Operator.setText("&");
        } else if (this.operatorsMenu.getValue().equals("|")) {
            this.Operator.setVisible(true);
            this.Assign2TextBox.setVisible(true);
            this.Operator.setText("|");
        }
    }

    @FXML
    void clearAllinputButton(ActionEvent event) {
        // removes all inputs,outputs, or registers
        if (this.inputOutputMenu.getValue().equals("Input")) {
            for (String str : this.inputs) {
                this.variableType.remove(str);
                this.variableSize.remove(str);
            }
            this.inputs.clear();
            this.indexInput = 0;

        } else if (this.inputOutputMenu.getValue().equals("Output")) {
            for (String str : this.outputs) {
                this.variableType.remove(str);
                this.variableSize.remove(str);
            }
            this.outputs.clear();
            this.indexOutput = 0;


        } else if (this.inputOutputMenu.getValue().equals("Register")) {
            for (String str : this.registers) {
                this.variableType.remove(str);
                this.variableSize.remove(str);
            }
            this.registers.clear();
            this.indexRegister = 0;


        }

        this.nameVarRegTextBox.clear();
        this.didEdit=true;


    }

    @FXML
    void clearAssign(ActionEvent event) {
        //just clears the value from the hash table
        String str = this.NameAssignmentsTextBox.getText();
        this.assigmentState.put(str, "");
        this.Assign1TextBox.clear();
        this.Operator.setVisible(false);
        this.Assign2TextBox.setVisible(false);
        this.didEdit=true;

    }

    @FXML
    void clearCondition(ActionEvent event) {
        //if radio button is selected
        if (this.conditionRadio.isSelected()) {
            String condition1 = "";
            String condition2 = "";
            condition1 = this.conditionRegSelect.getValue().replaceAll("Var ","Var").replaceAll
                    ("Reg ","Reg");

            condition2 = this.conditiontext2.getText();
            this.conditionsState.remove(condition1 + this.conditionLabel.getText()
                    +  condition2);
            this.conditionsOrder.get(this.stateNumSpinner.getValue()).remove(condition1 + this.conditionLabel.getText()
                    +  condition2);
        } else {
            this.conditionsState.remove("NOCON");
            this.conditionsOrder.get(this.stateNumSpinner.getValue()).remove("NOCON");
        }
        this.didEdit=true;



    }

    @FXML
    void clearInputButton(ActionEvent event) {
        if (this.inputOutputMenu.getValue().equals("Input")) {
            //deletes a single input
            String nameOf = this.nameVarRegTextBox.getText();

            // checking to see if the name exists
            if (this.inputs.contains(nameOf)) {
                this.inputs.remove(nameOf);
                this.variableSize.remove(nameOf);
                this.variableType.remove(nameOf);
                this.nameVarRegTextBox.clear();
                this.indexInput = 0;
            } else {
                this.nameVarRegTextBox.setText("ERROR....NAME DOESN't EXIST");
            }

        } else if (this.inputOutputMenu.getValue().equals("Output")) {
            //deletes a single output
            String nameOf = this.nameVarRegTextBox.getText();
// checking to see if the name exists
            if (this.outputs.contains(nameOf)) {
                this.outputs.remove(nameOf);
                this.variableSize.remove(nameOf);
                this.variableType.remove(nameOf);
                this.nameVarRegTextBox.clear();
                this.indexOutput = 0;
            } else {
                this.nameVarRegTextBox.setText("ERROR....NAME DOESN't EXIST");
            }

        } else if (this.inputOutputMenu.getValue().equals("Register")) {
            //deletes a single input
            String nameOf = this.nameVarRegTextBox.getText();
            // checking to see if the name exists
            if (this.registers.contains(nameOf)) {
                this.registers.remove(nameOf);
                this.variableSize.remove(nameOf);
                this.variableType.remove(nameOf);
                this.nameVarRegTextBox.clear();
                this.indexRegister = 0;
            } else {
                this.nameVarRegTextBox.setText("ERROR....NAME DOESN't EXIST");
            }

        }
        this.didEdit=true;

    }

    @FXML
    void deleteStateButton(ActionEvent event) {
        //for every state ahead edit the number to be one state smaller
        int size = states.size();
        if (size > 1) {
            for (int i = stateNumSpinner.getValue() + 1; i < size; i++) {
                this.assignments.put(i - 1, this.assignments.get(i));
                // iterate through
                this.conditions.put(i - 1, this.conditions.get(i));
                this.states.get(i).setStateNum(i - 1);

            }

            // iterate through every condition, and subtract one from each state that was higher or equal to
            // the one begin deleted
            for(int i = 0; i < this.conditions.size(); i++) {
                for (String condition: this.conditions.get(i).keySet()) {
                    int statenum = this.conditions.get(i).get(condition);
                    if (statenum >= stateNumSpinner.getValue())
                        this.conditions.get(i).put(condition, statenum -1);

                }
            }

            //setting everything up after the deletion
            this.states.remove(states.size()-1);
            this.assignments.remove(size - 1);
            this.conditions.remove(size - 1);
            this.assigmentState = this.assignments.get(stateNumSpinner.getValue());
            this.conditionsState = this.conditions.get(stateNumSpinner.getValue());

            this.indexAssign = -1;
            this.indexConditions = -1;
            assignPutData();
            nextCondition(null);
            stateNumbers.remove(stateNumbers.size() - 1);
        }

    }

    @FXML
    void generate(ActionEvent event) {
        if(this.didEdit)
            this.fsmSTRING = generateFunction();
        System.out.println(this.fsmSTRING);
        this.didEdit=false;
         RunProgram run = new RunProgram();
        run.run(this.fsmTitle, this.fsmSTRING);




    }
    //putting in everything in format
    public String generateFunction()
    {

        StringBuilder string = new StringBuilder("Start FSM\n");
        string.append("input ");
        for (String input : this.inputs) {
            string.append("Size " + this.variableSize.get(input) + " " + this.variableType.get(input) + " " + input + " ");
        }
        string.append("\n");
        string.append("output ");
        for (String output : this.outputs) {
            string.append("Size " + this.variableSize.get(output) + " " + this.variableType.get(output) + " " + output + " ");
        }
        string.append("\n");
        for (int state : this.stateNumbers) {
            string.append("State:" + state + " Define\n");
            for (String assgin : this.assignments.get(state).keySet()) {
                String defined = "";
                if (this.registers.contains(assgin) && state == 0) {
                    defined = "Size " + this.variableSize.get(assgin) + " ";
                }
                string.append(defined + this.variableType.get(assgin) + " "
                        + assgin + " = " + this.assignments.get(state).get(assgin) + "\n");
            }
            String temp = "";
            for (String condition : this.conditionsOrder.get(state)) {

                if (condition.equals("NOCON")) {
                    temp = "Next State State:" + this.conditions.get(state).get(condition) + "\n";
                } else if(this.conditions.get(state).containsKey(condition)) {
                    String conditiontemp = condition;
                    if(condition.charAt(condition.length()-2)=='%')
                        conditiontemp =condition.substring(0,condition.length()-2);
                    string.append("Next State if " + conditiontemp.replaceAll("Reg","Reg ").
                            replaceAll("Var","Var ") +
                            " State:" + this.conditions.get(state).get(condition) + "\n");
                }
            }
            string.append(temp);
            string.append("End\n");
        }
        string.append("End FSM");
        return string.toString();

    }

    @FXML
    //assigns the value of the register to itself, a "hold state"
    void holdAssign(ActionEvent event) {
        this.assigmentState.put(this.NameAssignmentsTextBox.getText(), this.variableType.get(this.NameAssignmentsTextBox.getText())+" "+
        this.NameAssignmentsTextBox.getText());
        assignPutData();
    }

    @FXML
    void holdStateAssign(ActionEvent event) {
        // goes through all outputs and assigns themselves
        for (String assign : this.assigns) {
            if (this.variableType.get(assign).equals("Var")) continue;
            this.assigmentState.put(assign, this.variableType.get(assign) + " " + assign);
        }
        this.Assign1TextBox.clear();
        this.Operator.setVisible(false);
        this.Assign2TextBox.setVisible(false);
    }

    @FXML
    void inputAssign(ActionEvent event) {
        if (this.Assign1TextBox.getText().length() > 0) {
            String reg = this.NameAssignmentsTextBox.getText();
            String op = "";
            String assign1 = this.Assign1TextBox.getText();
            String assign2 = "";

            if (this.Operator.isVisible()) {
                op = this.Operator.getText();
            }
            if (this.Assign2TextBox.isVisible()) {
                assign2 = this.Assign2TextBox.getText();
            }
// see if assigment1 is valid, generate warnings
            try {
                Integer.parseInt(assign1);
            } catch (NumberFormatException e) {
                if (!(this.inputs.contains(assign1) || this.registers.contains(assign1) ||
                        this.outputs.contains(assign1))) {
                    Warnings w = new Warnings("This Register/Var doesn't exist", "Input Error");
                    Thread t = new Thread(w);
                    t.run();
                    return;
                } else if (this.outputs.contains(assign1) && this.variableType.get(assign1).equals("Var")) {
                    Warnings w = new Warnings("Cannot use variable Output as a assignmet", "Input Error");
                    Thread t = new Thread(w);
                    t.run();
                    return;
                }


            }


            // see if assigment1 is valid
            try {
                Integer.parseInt(assign2);
            } catch (NumberFormatException e) {
                if (!(this.inputs.contains(assign2) || this.registers.contains(assign2) || this.outputs.contains(assign2))
                        && this.Assign2TextBox.isVisible()) {
                    Warnings w = new Warnings("This Register/Var doesn't exist", "Input Error");
                    Thread t = new Thread(w);
                    t.run();
                    return;
                } else if (this.outputs.contains(assign2) && this.variableType.get(assign2).equals("Var")) {
                    Warnings w = new Warnings("Cannot use variable Output as a assignmet", "Input Error");
                    Thread t = new Thread(w);
                    t.run();
                    return;
                }
            }

            String type = "";
            String type2 = "";
            if (this.variableType.containsKey(assign1))
                type = this.variableType.get(assign1) + " ";
            if (this.variableType.containsKey(assign2))
                type2 = this.variableType.get(assign2) + " ";

            this.assigmentState.put(reg, type + assign1 + " " + op + " " + type2 + " " + assign2);
            this.Assign1TextBox.clear();
            this.Assign2TextBox.clear();

            assignPutData();
            this.didEdit=true;

        }
    }

    @FXML
    void inputCondition(MouseEvent event) {

    }

    @FXML
    void loadButton(ActionEvent event) {
        try {
            File file = load.showOpenDialog(this.stage);
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String str = br.readLine();
            //deletes begining inputs
            str = str.substring(str.indexOf('S'), str.length());
            while (str.length() > 0) {
                str = str.replaceAll(" ", "");
                String size = str.substring(4, 5);
                String type = str.substring(5, 8);
                str = str.substring(4, str.length());
                String name = "";
                if (str.contains("Size")) {
                    int index = str.indexOf("Size");
                    name = str.substring(4, index);
                } else {
                    name = str.substring(4, str.length());
                }
                this.inputs.add(name);
                this.variableType.put(name, type);
                this.variableSize.put(name, Integer.parseInt(size));
                this.conditionRegSelect.getItems().addAll(type+" "+name);
                str = str.substring(str.indexOf(name)+name.length(), str.length());

            }
            //read outputs
            str = br.readLine();
            str = str.substring(str.indexOf('S'), str.length());
            while (str.length() > 0) {
                str = str.replaceAll(" ", "");
                String size = str.substring(4, 5);
                String type = str.substring(5, 8);
                str = str.substring(4, str.length());
                String name = "";
                if (str.contains("Size")) {
                    int index = str.indexOf("Size");
                    name = str.substring(4, index);
                } else {
                    name = str.substring(4, str.length());
                }
                if(type.equals("Reg"))
                    this.conditionRegSelect.getItems().addAll(type+" "+name);
                this.outputs.add(name);
                this.variableType.put(name, type);
                this.variableSize.put(name, Integer.parseInt(size));
                str = str.substring(str.indexOf(name)+name.length(), str.length());
                this.assigns.add(name);

            }
            str = br.readLine();
            while (true) {
                int state = Integer.parseInt(str.substring(6,7));
                if(state!=0) {
                    int statefactor = (state>25)?state-26:state;
                    this.states.add(new DrawState(100 * statefactor + 50, state));
                    this.stateNumbers.add(Integer.parseInt(str.substring(6, 7)));
                    this.assignments.put(Integer.parseInt(str.substring(6, 7)), new Hashtable<>());
                    this.assigmentState = this.assignments.get(Integer.parseInt(str.substring(6, 7)));
                    this.conditions.put(Integer.parseInt(str.substring(6, 7)),new Hashtable<>());
                    this.conditionsState = this.conditions.get(Integer.parseInt(str.substring(6, 7)));
                    this.conditionsOrder.add(new ArrayList<>());
                }

                str = br.readLine();
                while (!str.contains("Next State")) {
                    str = str.replace(" ", "");
                    String name = str.substring(3, str.indexOf("="));
                    if (str.charAt(0) == 'S') {
                        String size = str.substring(4, 5);
                        String type = str.substring(5, 8);
                        name = str.substring(8, str.indexOf("="));
                        this.registers.add(name);
                        this.variableSize.put(name, Integer.parseInt(size));
                        this.variableType.put(name, type);
                        this.assigns.add(name);
                        this.conditionRegSelect.getItems().addAll(type+" "+name);
                    }
                    String assgin0;
                    String assgin1 = "";
                    String op = str.contains("*") ? "*" :
                            (str.contains("+") ? "+" : (str.contains("-") ? "-" :
                                    (str.contains("/") ? "/" :
                                            (str.contains("&") ? "&"
                                                    :  (str.contains("|") ? "|": "")))));
                    if (op.length() == 0) {
                        assgin0 = str.substring(str.indexOf("=") + 1, str.length());
                        assgin0 = assgin0.replaceAll("Reg", "Reg ");
                        assgin0 = assgin0.replaceAll("Var", "Var ");
                        this.assigmentState.put(name, assgin0);

                    } else {
                        assgin0 = str.substring(str.indexOf("=") + 1, str.indexOf(op));
                        assgin1 = str.substring(str.indexOf(op) + op.length(), str.length());
                        assgin0 = assgin0.replaceAll("Reg", "Reg ");
                        assgin0 = assgin0.replaceAll("Var", "Var ");
                        assgin1 = assgin1.replaceAll("Reg", "Reg ");
                        assgin1 = assgin1.replaceAll("Var", "Var ");
                        this.assigmentState.put(name, assgin0 + " " + op + " "+assgin1);
                    }
                    str = br.readLine();
                }

                while(!str.equals("End"))
                {
                    str = str.replaceAll(" ","");

                    //if less than 17, this is a non conditional branch
                    if(str.length() < 17)
                    {
                        this.conditionsOrder.get(state).add("NOCON");
                        this.conditionsState.put("NOCON",Integer.parseInt(str.substring(str.length()-1,str.length())));
                    }
                    //else there is a conditional branch
                    else{
                        str = str.replace("Equals","==");
                        str = str.replace("NotEquals","~=");
                        str = str.replace("GreaterThan",">");
                        str = str.replace("LessThan","<");
                        int startingIndex = (str.contains("Reg"))?str.indexOf("Reg"):str.indexOf("Var");
                        String str2 = str.substring(startingIndex,str.indexOf("State:"));
                        //fixes, now possible to have multiple conditional assigments
                        int offset = 1;
                        if(conditionsOrder.get(state).contains(str.substring(startingIndex,str.indexOf("State:")))) {
                            while (conditionsOrder.get(state).contains(str2 + offset)) offset++;
                            this.conditionsOrder.get(state).add(str2+"%"+offset);
                            this.conditionsState.put(str2+"%"+offset,
                                    Integer.parseInt(str.substring(str.length()-1,str.length())));
                        }
                        else {
                            this.conditionsOrder.get(state).add(str2);
                            this.conditionsState.put(str.substring(startingIndex, str.indexOf("State:")),
                                    Integer.parseInt(str.substring(str.length() - 1, str.length())));
                        }
                    }
                    str = br.readLine();
                }
                str = br.readLine();
                if(str.equals("End FSM")) break;


            }
            br.close();
            this.didEdit=true;

            //dispaly the latest assignmeent
            this.assigmentState = this.assignments.get(0);
            this.conditionsState = this.conditions.get(0);
            this.indexAssign = -1;
            assignPutData();
            this.indexConditions = -1;
            nextCondition(null);



        } catch (FileNotFoundException e) {
            Warnings fail = new Warnings("File not found", "error");
            Thread t = new Thread(fail);
            t.run();
            return;
        } catch (IOException e) {
            Warnings fail = new Warnings("Error reading", "error");
            Thread t = new Thread(fail);
            t.run();
            return;
        }

    }

    @FXML
    void loadStateButton(ActionEvent event) {

    }

    @FXML
    void nextAssign(ActionEvent event) {
        if(this.assigmentState.size()==0)
            return;
        // this method does all the work
        assignPutData();

    }

    @FXML
    void nextCondition(ActionEvent event) {
        this.indexConditions++;
        if (this.indexConditions >= this.conditionsState.keySet().size()) {
            this.indexConditions = 0;
        }
        if(this.conditionsState.size()==0)
            return;

        String str = (String) (this.conditionsState.keySet().toArray())[this.indexConditions];
        if (str.equals("NOCON")) {
            // if NOCON make all the text boxes disapper

            this.conditionRadio.setSelected(false);
            this.fLabel.setVisible(false);
            this.conditionRegSelect.setVisible(false);
            this.conditionMenu.setVisible(false);
            this.conditiontext2.setVisible(false);
            this.conditionLabel.setVisible(false);

            this.conditionSpinner.getValueFactory().setValue(this.conditionsState.get("NOCON"));

        } else {
            if (!this.conditionRadio.isSelected()) {
                this.conditionRadio.setSelected(true);
                this.fLabel.setVisible(true);
                this.conditionRegSelect.setVisible(true);
                this.conditionMenu.setVisible(true);
                this.conditiontext2.setVisible(true);
                this.conditionLabel.setVisible(true);
            }
            String condition1;
            String condition2;
            String label = "~=";
            for (int i = 0; i < 4; i++) {
                if (str.contains(label)) {
                    break;
                }
                switch (i) {
                    case 0:
                        label = "<";
                        break;
                    case 1:
                        label = ">";
                        break;

                    case 2:
                        label = "==";
                        break;

                }
            }
            //only needed if there is a number at the end of condition 2, which only happens
            //if the condition is assigned more than one
            int offfset = 0;
            if(str.charAt(str.length()-2)=='%') offfset=2;
            //break the substrings
            condition1 = str.substring(0, str.indexOf(label.charAt(0))).replaceAll("Reg","Reg ")
                    .replaceAll("Var","Var ");

            condition2 = str.substring(str.indexOf(label.charAt(label.length() - 1)) + 1, str.length()-offfset).replaceAll("Reg","Reg ")
                    .replaceAll("Var","Var ");
            //removes the extra == if label is that
            if(label.equals("=="))
                condition2=condition2.substring(1,condition2.length());

            this.conditionRegSelect.getSelectionModel().select(this.conditionRegSelect.getItems().indexOf(condition1));
            this.conditiontext2.setText(condition2);
            this.conditionLabel.setText(label);
            this.conditionSpinner.getValueFactory().setValue(this.conditionsState.get(str));
        }

    }

    @FXML

        // method purpose is to scroll through already inputed inputs/outpus/registers
    void nextInputButton(ActionEvent event) {
        if (this.inputOutputMenu.getValue().equals("Input") && this.inputs.size() > 0) {
            this.nameVarRegTextBox.setText(this.inputs.get(this.indexInput));
            this.sizeInOutSpinner.getValueFactory().setValue(this.variableSize.get(this.nameVarRegTextBox.getText()));
            this.varRadio.setSelected(true);
            this.regRadio.setSelected(false);

            //increment for next value
            this.indexInput++;
            if (this.indexInput == this.inputs.size())
                this.indexInput = 0;

        }
// now for outputs
        else if (this.inputOutputMenu.getValue().equals("Output") && this.outputs.size() > 0) {
            this.nameVarRegTextBox.setText(this.outputs.get(this.indexOutput));
            this.sizeInOutSpinner.getValueFactory().setValue(this.variableSize.get(this.nameVarRegTextBox.getText()));
            if (this.variableType.get(this.nameVarRegTextBox.getText()).equals("Var")) {
                this.varRadio.setSelected(true);
                this.regRadio.setSelected(false);
            } else {
                this.regRadio.setSelected(true);
                this.varRadio.setSelected(false);
            }

            //increment for next value
            this.indexOutput++;
            if (this.indexOutput == this.outputs.size())
                this.indexOutput = 0;

        }

        //now for registers

        else if (this.inputOutputMenu.getValue().equals("Register") && this.registers.size() > 0) {
            this.nameVarRegTextBox.setText(this.registers.get(this.indexRegister));
            this.sizeInOutSpinner.getValueFactory().setValue(this.variableSize.get(this.nameVarRegTextBox.getText()));
            this.varRadio.setSelected(false);
            this.regRadio.setSelected(true);

            //increment for next value
            this.indexRegister++;
            if (this.indexRegister == this.registers.size())
                this.indexRegister = 0;

        }
        //else clear the textview
        else {
            this.nameVarRegTextBox.clear();
        }
    }

    @FXML
    void radioCondition(ActionEvent event) {
        // if selected make relative stuff visiable
        if (this.conditionRadio.isSelected()) {
            this.conditionLabel.setVisible(true);
            this.fLabel.setVisible(true);
            this.conditionMenu.setVisible(true);
            this.conditiontext2.setVisible(true);
            this.conditionRegSelect.setVisible(true);

        } else {
            this.fLabel.setVisible(false);
            this.conditionMenu.setVisible(false);
            this.conditiontext2.setVisible(false);
            this.conditionLabel.setVisible(false);
            this.conditionRegSelect.setVisible(false);

        }

    }

    @FXML
    void conditionselect(ActionEvent event) {
        // switch statment for conditions
        switch (this.conditionMenu.getValue()) {
            case "Greater than":
                this.conditionLabel.setText(">");
                break;
            case "Equal to":
                this.conditionLabel.setText("==");
                break;
            case "Not Equal to":
                this.conditionLabel.setText("~=");
                break;
            case "Less than":
                this.conditionLabel.setText("<");
                break;
        }
    }

    @FXML
    void radioVarReg(ActionEvent event) {
        // set values if input or reg is selected

        //checks to see if value is selected
        if (this.inputOutputMenu.getValue().equals("Type"))
            return;
        if (this.inputOutputMenu.getValue().equals("Input")) {
            this.regRadio.setSelected(false);
        } else if (this.inputOutputMenu.getValue().equals("Register"))
            this.varRadio.setSelected(false);

            // else an output can be a reg or var
        else if (event.getSource().equals(this.varRadio)) {
            this.regRadio.setSelected(false);
        } else if (event.getSource().equals(this.regRadio)) {
            this.varRadio.setSelected(false);
        }

    }

    @FXML
    //printing the file to a .txt
    void saveButton(ActionEvent event) {
        try{
            //got from here http://java-buddy.blogspot.com/2012/05/save-file-with-javafx-filechooser.html
            //Set extension filter
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            load.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = load.showSaveDialog(stage);
            PrintWriter print = new PrintWriter(file);
            if(this.didEdit)
                this.fsmSTRING = generateFunction();
            print.print(this.fsmSTRING);
            this.didEdit = false;
            print.close();
        }
        catch(FileNotFoundException e)
        {

        }

    }

    @FXML
        //setting certian types for inputs
    void typeSelect(ActionEvent event) {
        if (this.inputOutputMenu.getValue().equals("Input")) {
            this.varRadio.setSelected(true);
            this.regRadio.setSelected(false);
        } else if (this.inputOutputMenu.getValue().equals("Register")) {
            this.varRadio.setSelected(false);
            this.regRadio.setSelected(true);
        }


        this.indexInput = 0;
        this.indexOutput = 0;
        this.indexRegister = 0;
        this.nameVarRegTextBox.clear();
    }

    @FXML
//a listener to make state hastables
    void clickSpin(MouseEvent event) {

        //checks to see if state exists
        if (!this.assignments.containsKey(this.stateNumSpinner.getValue())) {
            int state = this.stateNumSpinner.getValue();
            this.assignments.put(state, new Hashtable<>());
            this.conditions.put(state, new Hashtable<>());
            this.conditionsOrder.add(new ArrayList<>());
            for (String str : this.assigns) {
                this.assignments.get(state).put(str, "");
            }
            this.stateNumbers.add(state);
            this.Assign1TextBox.clear();
            this.Assign2TextBox.setVisible(false);
            this.Operator.setVisible(false);

            //add this state to draw state
            //adjust to use the first row first
            int statefactor = (state>25)?state-26:state;

            this.states.add(new DrawState(100 * statefactor + 50, state));
            //if the state exists and there is a new output,
            //make sure it is in the hashmap


        } else {
            int state = this.stateNumSpinner.getValue();
            for (String str : this.assigns) {
                if (!(this.assignments.get(state).containsKey(str)))
                    this.assignments.get(state).put(str, "");
            }
        }
        // sets varibles with values for easier access
        this.assigmentState = this.assignments.get(this.stateNumSpinner.getValue());
        this.conditionsState = this.conditions.get(this.stateNumSpinner.getValue());

        //checks to see if any inputs where added
        if (this.assigns.size() > 0) {
            this.NameAssignmentsTextBox.setText(this.assigns.get(0));
            //because assignputdata increments
            this.indexAssign--;
            assignPutData();
        }
        indexConditions=0;
    }

    // ppurpose of method is to fill data boxes for values of outputs/regs
    public void assignPutData() {
        this.Operator.setVisible(false);
        this.Assign2TextBox.setVisible(false);
        this.Assign1TextBox.clear();

        //incrment index to see assignemtns
        this.indexAssign++;
        if (this.indexAssign == this.assigns.size())
            this.indexAssign = 0;
        this.NameAssignmentsTextBox.setText(this.assigns.get(this.indexAssign));
        String str = this.assigmentState.get(this.assigns.get(this.indexAssign));
        // if there is no info for this var, stop here
        if (str == null) return;


        char inQuestion = '+';
        boolean containAny = false;
        for (int i = 0; i < 6; i++) {
            if (str.contains(inQuestion + "")) {
                int indexcut = str.indexOf(inQuestion) + 1;
                String additionToOpp = "";


                containAny = true;
                String str1 = str.substring(0, str.indexOf(inQuestion));
                String str2 = str.substring(indexcut, str.length());

                this.Assign1TextBox.setText(str1);


                this.Operator.setText(inQuestion + additionToOpp);
                this.Assign2TextBox.setText(str2);
                this.Operator.setVisible(true);
                this.Assign2TextBox.setVisible(true);
                break;
            }

            // do for all chars

            switch (i) {
                case 0:
                    inQuestion = '-';
                    break;
                case 1:
                    inQuestion = '*';
                    break;
                case 2:
                    inQuestion = '/';
                    break;
                case 3:
                    inQuestion = '&';
                    break;
                case 4:
                    inQuestion = '|';
                    break;
            }
        }

        if (!containAny) {
            this.Assign1TextBox.setText(str);
            this.Operator.setVisible(false);
            this.Assign2TextBox.setVisible(false);

        }

    }

    public boolean isInt(String str)
    {
        try {
            Integer.parseInt(str);
            return true;
        }

        catch(Exception e) {
            return false;
        }
    }

}


