package love.lock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoveLock {

    static int poskusi = 3;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Love Lock");
        frame.setSize(420, 260);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel title = new JLabel("Odkleni :3");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel instruction = new JLabel("Vpiši geslo");

        JPasswordField passwordField = new JPasswordField(15);
        JButton unlockButton = new JButton("Odkleni");

        JLabel message = new JLabel(" ");

        String correctPassword = "21.5.";

        //big boss
        unlockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String enteredPassword = new String(passwordField.getPassword());

                if (enteredPassword.equals(correctPassword)) {
                    title.setText("Odklenjeno ^_^");
                    message.setText("Pol leta pikica pol leta aah message");
                    unlockButton.setEnabled(false);
                    passwordField.setEnabled(false);
                } else {
                    poskusi--;

                    if (poskusi > 0) {
                        message.setText(getNamig(poskusi));
                    }
                }
            }
        });

        frame.add(title);
        frame.add(instruction);
        frame.add(passwordField);
        frame.add(unlockButton);
        frame.add(message);
        frame.setVisible(true);
    }

    //namigi
    static String getNamig(int poskusi) {
        if (poskusi == 2) {
            return "Namig: pomemben datum";
        } else if (poskusi == 1) {
            return "Zadnji namig: tik pred maturo";
        }
        return "";
    }

}