package main;
import core.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
        });
    }
}