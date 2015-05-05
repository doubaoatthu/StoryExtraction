package storyextraction;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vera on 15-3-17.
 */
public class MainWindow extends JComponent {
    public static void main(String [] argc){
     /*   JFrame frame = new JFrame("blbl");
        frame.add(new Canvas(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0,300,500);
        //frame.setMaximumSize(new Dimension(1024,756));
        //frame.setMinimumSize(new Dimension(1024,756));
        //frame.setPreferredSize(new Dimension(1300,800));
        frame.pack();
        frame.setVisible(true);*/
        String query = argc[0];
        Parse parse = new Parse();
        System.out.print("Answer: " + parse.AnswerWithString(query));
    }
}
