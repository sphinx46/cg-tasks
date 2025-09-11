import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final DrawPanel panel;

    public MainWindow() throws HeadlessException {
        setSize(800, 600);
        panel = new DrawPanel(this.getWidth(), this.getHeight(), 100);
        this.add(panel);
    }
}
