package sample;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

/**
 * the purpose of this class is to create error pop ups to alert the user of a inccorrect input
 *
 * @author Stelios Papoutsakis
 */

public class Warnings implements Runnable {
    private String str;
    private String str2;

    public Warnings(String str, String str2) {
        this.str = str;
        this.str2 = str2;
    }

    @Override
    public void run() {

        // help from https://stackoverflow.com/questions/43623278/joptionpane-bigger
        UIManager.put("OptionPane.minimumSize",new Dimension(1000,200));
        //help from https://stackoverflow.com/questions/4017042/how-to-enlarge-buttons-on-joptionpane-dialog-boxes
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL",Font.PLAIN,35)));
        JLabel label = new JLabel(this.str);
        label.setFont(new Font("Arial", Font.PLAIN, 50));
        JOptionPane.showMessageDialog(null,label, this.str2, JOptionPane.ERROR_MESSAGE);
    }


}
