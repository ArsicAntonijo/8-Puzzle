package puzzle;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGUI{
    JButton button0;
    JTextField displayField;
    JFrame frame;
    JComboBox comboBox;
    ImageIcon image;


    StartGUI() {
        SwingUtilities.invokeLater(() -> {

                // button 0
                button0 = new JButton("Start animation");


                // ..text field
                displayField = new JTextField(30);
                displayField.setBorder(BorderFactory.createEmptyBorder(20,15,20,15));
                int fontSize = 18;
                Font f = new Font("Comic Sans MS", Font.BOLD, fontSize);
                displayField.setFont(f);
                displayField.setHorizontalAlignment(JTextField.CENTER);
                displayField.setText("Welcome, select type and press start animation!");

                //combo button
                String[] typeToChoose = { "select type", "3x3", "4x4", "Try yourself"};
                comboBox = new JComboBox(typeToChoose);
                comboBox.setSelectedIndex(0);

                // picture
                image = new ImageIcon(getClass().getResource("logo.png"));
                JLabel label = new JLabel(image);

                JPanel p1 = new JPanel();
                GridLayout gl = new GridLayout(1,2);
                p1.setLayout(gl);

                //p1.add(displayField);
                p1.add(comboBox);
                p1.add(button0);

                //button0  listener
                button0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String s = (String) comboBox.getSelectedItem();
                        assert s != null;

                        switch (s) {
                            case "2x2":
                                frame.setVisible(false);
                                new PuzzleGUI(2);
                                break;
                            case "3x3":
                                frame.setVisible(false);
                                new PuzzleGUI(3);
                                break;
                            case "4x4":
                                frame.setVisible(false);
                                new PuzzleGUI(4);
                                break;
                            case "Try yourself":
                                frame.setVisible(false);
                                new PuzzleGUI(5);
                                break;
                        }
                    }
                });


                // setting up the frame
                frame = new JFrame();

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Game of Fifteen");
                frame.setResizable(false);

                frame.add("North", label);
                frame.add(p1);
                frame.add("South", displayField);

                frame.setSize(300, 500);
                frame.pack();

                // center on the screen
                frame.setLocationRelativeTo(null);

                // enable the window's close button
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        new StartGUI();
    }
}

