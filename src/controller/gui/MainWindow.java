package controller.gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainWindow {
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 400;

    public static void launch() {
        MainWindow window = new MainWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        window.construct();
    }

    private int width;
    private int height;

    // GUI Components
    public static JButton nightModeOn;
    public static JTextField redText;
    public static JTextField greenText;
    public static JTextField blueText;
    public static JTextField briText;
    public static JButton buttonOn;
    public static JButton buttonRandom;

    public MainWindow(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void construct() {
        this.setupMainWindow();
    }

    private void setupMainWindow() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setSize(this.width, this.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        addGUI(panel);
        frame.setVisible(true);
    }

    // Initializes GUI
    private static void addGUI(JPanel panel) {
        JLabel titleLabel = new JLabel("Phillips Hue Controller");
        titleLabel.setBounds(175, 20, 150, 25);
        panel.add(titleLabel);

        // ----- Preset Buttons Section -----
        JLabel presetsLabel = new JLabel("Presets");
        presetsLabel.setBounds(225, 50, 50, 25);
        panel.add(presetsLabel);

        JButton bubbleOn = new JButton(new AbstractAction("Bubble") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.bubbleColors();
            }
        });
        bubbleOn.setBounds(170, 80, 80, 25);
        panel.add(bubbleOn);

        nightModeOn = new JButton("Night");
        nightModeOn.setBounds(250, 80, 80, 25);
        nightModeOn.addActionListener(new Controller());
        panel.add(nightModeOn);

        JButton buttonOff = new JButton("Off");
        buttonOff.setBounds(210, 110, 80, 25);
        buttonOff.addActionListener(new Controller());
        panel.add(buttonOff);

        // ----- Custom Color Section -----
        JLabel customLabel = new JLabel("Custom");
        customLabel.setBounds(225, 170, 50, 25);
        panel.add(customLabel);

        JLabel redLabel = new JLabel("r:");
        redLabel.setBounds(60, 200, 30, 25);
        panel.add(redLabel);

        redText = new JTextField();
        redText.setBounds(90, 200, 50, 25);
        panel.add(redText);

        JLabel greenLabel = new JLabel("g:");
        greenLabel.setBounds(160, 200, 30, 25);
        panel.add(greenLabel);

        greenText = new JTextField();
        greenText.setBounds(190, 200, 50, 25);
        panel.add(greenText);

        JLabel blueLabel = new JLabel("b:");
        blueLabel.setBounds(260, 200, 30, 25);
        panel.add(blueLabel);

        blueText = new JTextField();
        blueText.setBounds(290, 200, 50, 25);
        panel.add(blueText);

        JLabel briLabel = new JLabel("bri:");
        briLabel.setBounds(360, 200, 30, 25);
        panel.add(briLabel);

        briText = new JTextField();
        briText.setBounds(390, 200, 50, 25);
        panel.add(briText);

        buttonOn = new JButton("Custom On");
        buttonOn.setBounds(200, 240, 100, 25);
        buttonOn.addActionListener(new Controller());
        panel.add(buttonOn);

        // ----- Random Color(s) Section -----
        buttonRandom = new JButton("Random (One Color)");
        buttonRandom.setBounds(50, 320, 200, 25);
        buttonRandom.addActionListener(new Controller());
        panel.add(buttonRandom);

        JButton buttonRandom2 = new JButton(new AbstractAction("Random (Two Colors)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.randomColors();
            }
        });
        buttonRandom2.setBounds(250, 320, 200, 25);
        panel.add(buttonRandom2);
    }
}