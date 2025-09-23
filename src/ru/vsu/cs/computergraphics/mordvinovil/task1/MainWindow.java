package ru.vsu.cs.computergraphics.mordvinovil.task1;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final DrawPanel panel;

    public MainWindow() throws HeadlessException {
        setSize(800, 600);
        panel = new DrawPanel(100);
        this.add(panel);
    }
}
