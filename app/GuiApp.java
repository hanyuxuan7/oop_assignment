package app;

import javax.swing.*;
import view.LoginFrame;

public class GuiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
