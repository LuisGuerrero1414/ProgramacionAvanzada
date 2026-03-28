package saeae.view;

import java.nio.file.Path;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        UiTheme.install();
        try {
            UIManager.put("Component.focusWidth", 1);
        } catch (Exception ignored) {
        }
        Path base = Path.of(System.getProperty("user.dir"));
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            saeae.controller.AppController controller =
                    new saeae.controller.AppController(frame, base);
            frame.bind(controller);
            frame.setVisible(true);
        });
    }
}
