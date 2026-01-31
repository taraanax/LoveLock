package love.lock;

import javax.swing.*;
import java.awt.*;

public class LoveLock {
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

        frame.add(title);
        frame.add(instruction);
        frame.add(passwordField);
        frame.add(unlockButton);
        frame.setVisible(true);
    }


}
