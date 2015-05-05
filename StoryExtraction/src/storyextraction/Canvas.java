package storyextraction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vera on 15-3-17.
 */
public class Canvas extends JComponent {
    private JPanel mp;
    private JButton btn;
    private JTextArea query;
    private JTextArea answer;
    public Canvas(){
        mp = new JPanel();
        mp.setSize(new Dimension(300,500));
        btn = new JButton("Submit");
        query = new JTextArea("hello");
        query.setEditable(true);
        query.setSize(new Dimension(250,30));
        answer = new JTextArea();
        //mp.setLayout(new FlowLayout());
        mp.add(query);
        mp.add(btn);
        mp.add(answer);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toquery = query.getText();
                Parse parse = new Parse();
                answer.setText(parse.AnswerWithString(toquery));
            }
        });
        mp.setVisible(true);
        this.add(mp);
    }
}
