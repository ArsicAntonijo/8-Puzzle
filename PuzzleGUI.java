package puzzle;

import javax.swing.*;
import java.awt.*;

public class PuzzleGUI {
    JButton button0;
    JButton button1;
    JTextField displayField;
    JFrame frame;


    PuzzleGUI(int type) {
        SwingUtilities.invokeLater(() -> {

            // button 0
            if (type == 5){
                button0 = new JButton("Get hint");
            } else {
                button0 = new JButton("Shuffle");
            }
            button1 = new JButton("Return to Start menu");
            // ..text field
            displayField = new JTextField(1);
            displayField.setHorizontalAlignment(JTextField.CENTER);
            JPanel p1 = new JPanel();
            GridLayout gl = new GridLayout(1,2);
            p1.setLayout(gl);
            p1.add(button0);
            p1.add(button1);

            // setting up the frame
            frame = new JFrame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("8 Puzzle");
            frame.setResizable(false);
            int dim;
            switch (type) {
                case 2:
                    dim = 300;
                    frame.add(new PuzzleGame(type, dim, 30, this), BorderLayout.CENTER);
                    break;
                case 3:
                    dim = 350;
                    frame.add(new PuzzleGame(type, dim, 30, this), BorderLayout.CENTER);
                    break;
                case 4:
                    dim = 550;
                    frame.add(new PuzzleGame(type, dim, 30, this), BorderLayout.CENTER);
                    break;
                case 5:
                    frame.add(new TryYourself(3, 350, 30, this), BorderLayout.CENTER);
                    break;
            }
         //   frame.add(new GameOfFifteen(type, dim, 30, this), BorderLayout.CENTER);
            frame.add("South", p1);

            frame.pack();
            // center on the screen
            frame.setLocationRelativeTo(null);

            // enable the window's close button
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {

         new PuzzleGUI(3);
    }
}
