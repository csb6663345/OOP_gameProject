package core;
import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        GamePanel panel = new GamePanel();
        
        this.setTitle("Charismar Apartment");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        panel.startGame();
    }
}