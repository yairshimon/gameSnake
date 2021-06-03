import javax.swing.*;
public class DisplaysGameScreen extends JFrame {
    DisplaysGameScreen() {
        this.add(new Snake());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}