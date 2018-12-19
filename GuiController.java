package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;

public class GuiController {
/***
 * BUG: if you add a new output it deletes all the vlaues of the state assign
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
    private HashMap<String, String> variableType = new HashMap<>();
    private ArrayList<String> inputs = new ArrayList<>();
    private ArrayList<String> outputs = new ArrayList<>();
    private ArrayList<String> registers = new ArrayList<>();
    private ArrayList<String> assigns = new ArrayList<>();
    protected Hashtable<String, String> assigmentState;
    protected Hashtable<String, Integer> conditionsState;


    private int indexInput = 0;
    private int indexOutput = 0;
    private int indexRegister = 0;
    private int indexAssign = 0;
    private int indexConditions = 0;
    private int indexStates = 0;

    /**
     * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     */

    @FXML
    protected Label conditionLabel;
    @FXML
    protected TextField conditiontext2;

    @FXML
    private Tab stateInputsTab;

    @FXML
    protected ComboBox<String> inputOutputMenu;



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
    private Button inputInOutButton;

    @FXML
    private Button clearInOutButton;

    @FXML
    private Button nextInOutButton;

    @FXML
    private TextField title;

    @FXML
    private Button clearallInOutButton;

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
    private Button inputAssignButton;

    @FXML
    private Button nextAssignButton;

    @FXML
    private Button holdButton;

    @FXML
    private Button holdStateButton;

    @FXML
    private Button clearAssginButton;

    @FXML
    protected Spinner<Integer> stateNumSpinner;

    @FXML
    private RadioButton conditionRadio;

    @FXML
    protected TextField conditionTextbox;

    @FXML
    protected ComboBox<String> conditionMenu;

    @FXML
    private Button conditionInputButton;

    @FXML
    private Button conditionNextButton;

    @FXML
    private Button conditionClearButton;

    @FXML
    protected Spinner<Integer> conditionSpinner;

    @FXML
    protected Label fLabel;

    @FXML
    private Button generateButton;

    @FXML
    private Button deletStateButton;

    @FXML
    private CheckBox generateTestBenchCheckBox;

    @FXML
    private Button saevFSMButton;

    @FXML
    private TextField loadTextbox;


    @FXML
    private Button loadFSMButton;

    @FXML
    private Button loadStateButton;



    @FXML
    private Canvas canvas;




    @FXML
        //inputing values
    void InputButton(ActionEvent event) {
        //this.canvas.getGraphicsContext2D().fillOval(200,400,890,200);
        //checking to see if stuff is input
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

                //puts values of an input in tables and arrayList
                if (!this.inputs.contains(this.nameVarRegTextBox.getText()))
                    this.inputs.add(this.nameVarRegTextBox.getText());
                this.variableType.put(this.nameVarRegTextBox.getText(), "Var");
                this.variableSize.put(this.nameVarRegTextBox.getText(), this.sizeInOutSpinner.getValue());
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
                }
                this.variableSize.put(this.nameVarRegTextBox.getText(), this.sizeInOutSpinner.getValue());
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


        if (!this.assigns.isEmpty())
            this.NameAssignmentsTextBox.setText(this.assigns.get(0));
    }


    //printing title of FSM
    @FXML
    void InputtitleButton(ActionEvent event) {
        if (event.getSource().equals(this.InputTitleButton) && this.title.getText().length() > 0)
            this.fsmTitle = this.title.getText();

    }

    @FXML
    void buttonClick(ActionEvent event) {
        String condition1 = "";
        String condition2 = "";
        //if radio button is selected
        if (this.conditionRadio.isSelected()) {
            condition1 = this.conditionTextbox.getText();
            condition1 = this.variableType.get(condition1) + " " + condition1;
            condition2 = this.conditiontext2.getText();
        } else {
            this.conditionsState.put("NOCON", this.conditionSpinner.getValue());
            return;
        }

        this.conditionsState.put(condition1 + " " + this.conditionLabel.getText()
                + " " + condition2, this.conditionSpinner.getValue());

        this.conditionTextbox.clear();
        this.conditiontext2.clear();

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


    }

    @FXML
    void clearAssign(ActionEvent event) {
        //just clears the value from the hash table
        String str = this.NameAssignmentsTextBox.getText();
        this.assigmentState.put(str, "");
        this.Assign1TextBox.clear();
        this.Operator.setVisible(false);
        this.Assign2TextBox.setVisible(false);

    }

    @FXML
    void clearCondition(ActionEvent event) {
        //if radio button is selected
        if (this.conditionRadio.isSelected()) {
            String condition1 = "";
            String condition2 = "";
            condition1 = this.conditionTextbox.getText();

            condition2 = this.conditiontext2.getText();
            this.conditionsState.remove(condition1 + " " + this.conditionLabel.getText()
                    + " " + condition2);
        } else {
            this.conditionsState.remove("NOCON");
        }


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

    }

    @FXML
    void deleteStateButton(ActionEvent event) {

    }

    @FXML
    void generate(ActionEvent event) {
        //putting in everything in format



//        System.out.println(this.inputs);
//        System.out.println(this.outputs);
//        System.out.println(this.registers);
//        System.out.println(this.variableSize);
//        System.out.println(this.variableType);
//        System.out.println(this.assigmentState);
//        System.out.println(this.conditionsState);

        StringBuilder string = new StringBuilder("start FSM\n");
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
            for (String condition : this.conditions.get(state).keySet()) {

                if (condition.equals("NOCON")) {
                    temp = "Next State State:" + this.conditions.get(state).get(condition) + "\n";
                } else {
                    string.append("Next State if " + condition +
                            " State:" + this.conditions.get(state).get(condition) + "\n");
                }
            }
            string.append(temp);
            string.append("End\n");
        }
        string.append("End FSM");


        System.out.println(string);
    }

    @FXML
    void holdAssign(ActionEvent event) {
        this.assigmentState.put(this.NameAssignmentsTextBox.getText(), this.NameAssignmentsTextBox.getText());
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
                        && this.Assign2TextBox.isVisible())
                         {
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
            if(this.variableType.containsKey(assign1))
                type = this.variableType.get(assign1) + " ";
            if(this.variableType.containsKey(assign2))
                type2 = this.variableType.get(assign2) + " ";

            this.assigmentState.put(reg, type + assign1 + " " + op + " " + type2 + " " + assign2);
            this.Assign1TextBox.clear();
            this.Assign2TextBox.clear();

            assignPutData();

        }
    }

    @FXML
    void inputCondition(MouseEvent event) {

    }

    @FXML
    void loadButton(ActionEvent event) {
        String file = loadTextbox.getText();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String str = br.readLine();
            //deletes begining inputs
            str = str.substring(str.indexOf('S'),str.length());
            while(str.charAt(0) == 'S') {
                str = str.replaceAll(" ", "");
                String size = str.substring(4,5);
                String type = str.substring(5,8);
                str = str.substring(4,str.length());
                String name ="";
                if(str.contains("Size"))
                {
                    int index = str.indexOf("Size");
                    name = str.substring(4,index);
                }
                else{
                    name = str.substring(4,str.length());
                }
                this.inputs.add(name);
                this.variableType.put(name,type);
                this.variableSize.put(name,Integer.parseInt(size));
                str = str.substring(str.indexOf(name.charAt(name.length()-1)),str.length());

            }
            //read outputs
            str = br.readLine();
            str = str.substring(str.indexOf('S'),str.length());
            while(str.charAt(0) == 'S') {
                str = str.replaceAll(" ", "");
                String size = str.substring(4,5);
                String type = str.substring(5,8);
                str = str.substring(4,str.length());
                String name ="";
                if(str.contains("Size"))
                {
                    int index = str.indexOf("Size");
                    name = str.substring(4,index);
                }
                else{
                    name = str.substring(4,str.length());
                }
                this.outputs.add(name);
                this.variableType.put(name,type);
                this.variableSize.put(name,Integer.parseInt(size));
                str = str.substring(str.indexOf(name.charAt(name.length()-1)),str.length());

            }
            str = br.readLine();
            while(!(str.equals("End FSM")))
            {
                this.stateNumbers.add(Integer.parseInt(str.substring(6,str.length())));
                this.assignments.put(Integer.parseInt(str.substring(6,str.length())), new Hashtable<>());
                this.assigmentState = this.assignments.get(Integer.parseInt(str.substring(6,str.length())));

                str = br.readLine();
                while(!str.contains("Next State"))
                {
                    str.replace(" ","");
                  if(str.charAt(0) == 'S')
                  {
                      String size = str.substring(4,5);
                      String type = str.substring(5,8);
                      String name = str.substring(8,str.indexOf("="));
                      if(str.contains("+"))
                      {

                      }
                      else if(str.contains("-"))
                      {

                      }
                      else if(str.contains("/"))
                      {

                      }
                      else if(str.contains("*"))
                      {

                      }




                  }

                }
            }
br.close();


        } catch(FileNotFoundException e)
        {
            Warnings fail = new Warnings("File not found","error");
            Thread t = new Thread(fail);
            t.run();
            return;
        } catch(IOException e)
        {
            Warnings fail = new Warnings("Error reading","error");
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
        // this method does all the work
        assignPutData();

    }

    @FXML
    void nextCondition(ActionEvent event) {
        this.indexConditions++;
        if (this.indexConditions == this.conditionsState.keySet().size()) {
            this.indexConditions = 0;
        }
        String str = (String) (this.conditionsState.keySet().toArray())[this.indexConditions];
        if (str.equals("NOCON")) {
            // if NOCON make all the text boxes disapper

            this.conditionRadio.setSelected(false);
            this.fLabel.setVisible(false);
            this.conditionTextbox.setVisible(false);
            this.conditionMenu.setVisible(false);
            this.conditiontext2.setVisible(false);
            this.conditionLabel.setVisible(false);

            this.conditionSpinner.getValueFactory().setValue(this.conditionsState.get("NOCON"));

        } else {
            if (!this.conditionRadio.isSelected()) {
                this.conditionRadio.setSelected(true);
                this.fLabel.setVisible(true);
                this.conditionTextbox.setVisible(true);
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
                        label = "=";
                        break;

                }
            }
            //break the substrings
            condition1 = str.substring(0, str.indexOf(label.charAt(0)));
            condition2 = str.substring(str.indexOf(label.charAt(label.length()-1)) + 1, str.length());
            this.conditiontext2.setText(condition2);
            this.conditionLabel.setText(label);
            this.conditionTextbox.setText(condition1);
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
            this.fLabel.setVisible(true);
            this.conditionTextbox.setVisible(true);
            this.conditionMenu.setVisible(true);
            this.conditiontext2.setVisible(true);
            this.conditionLabel.setVisible(true);

        } else {
            this.fLabel.setVisible(false);
            this.conditionTextbox.setVisible(false);
            this.conditionMenu.setVisible(false);
            this.conditiontext2.setVisible(false);
            this.conditionLabel.setVisible(false);

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
                this.conditionLabel.setText("=");
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
    void saveButton(ActionEvent event) {

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
            for (String str : this.assigns) {
                this.assignments.get(state).put(str, "");
            }
            this.stateNumbers.add(state);
            this.Assign1TextBox.clear();
            this.Assign2TextBox.setVisible(false);
            this.Operator.setVisible(false);
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
        for (int i = 0; i < 4; i++) {
            if (str.contains(inQuestion + "")) {
                containAny = true;
                String str1 = str.substring(0, str.indexOf(inQuestion));
                String str2 = str.substring(str.indexOf(inQuestion) + 1, str.length());

                this.Assign1TextBox.setText(str1);
                this.Operator.setText(inQuestion + "");
                this.Assign2TextBox.setText(str2);
                this.Operator.setVisible(true);
                this.Assign2TextBox.setVisible(true);
                break;
            }

            // do for all chars

            switch (i) {
                case 1:
                    inQuestion = '-';
                    break;
                case 2:
                    inQuestion = '*';
                    break;
                case 3:
                    inQuestion = '/';
                    break;
            }
        }

        if (!containAny) {
            this.Assign1TextBox.setText(str);
            this.Operator.setVisible(false);
            this.Assign2TextBox.setVisible(false);

        }

    }

}


