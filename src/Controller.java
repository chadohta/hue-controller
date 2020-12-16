import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Controller implements ActionListener {
    private static final String IP_ADDRESS = "YOUR_BRIDGE_IP_ADDRESS";
    private static final String USER_ID = "YOUR_AUTHORIZED_USER_ID";

    // GUI Components
    private static JButton nightModeOn;
    private static JTextField redText;
    private static JTextField greenText;
    private static JTextField blueText;
    private static JTextField briText;
    private static JButton buttonOn;
    private static JButton buttonRandom;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        initGUI(frame, panel);
        frame.setVisible(true);
    }

    // Initializes GUI
    public static void initGUI(JFrame frame, JPanel panel) {
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

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
                String colorXY = "[0.3582077650776395, 0.23040678214652616]";
                String param = "{ \"on\": true, \"bri\": 255, \"xy\": " + colorXY +  "}";
                String[] cmd = {
                        "curl",
                        "-X",
                        "PUT",
                        "-d",
                        param,
                        "http://" + IP_ADDRESS + "/api/" + USER_ID + "/lights/1/state"
                };
                executeCommand(cmd);

                String colorXY2 = "[0.16531321842220142, 0.28687412031689224]";
                String param2 = "{ \"on\": true, \"bri\": 255, \"xy\": " + colorXY2 +  "}";
                cmd[4] = param2;
                cmd[5] = "http://" + IP_ADDRESS + "/api/" + USER_ID + "/lights/2/state";
                executeCommand(cmd);
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
                int[] rgb = getRandomRGB();
                int bri = 255;
                Double[] aXY = getColorValues(rgb[0], rgb[1], rgb[2]);

                String param = getJSONParameters(aXY, bri);
                String[] cmd = {
                        "curl",
                        "-X",
                        "PUT",
                        "-d",
                        param,
                        "http://" + IP_ADDRESS + "/api/" + USER_ID + "/lights/1/state"
                };
                executeCommand(cmd);

                int[] rgb2 = getRandomRGB();
                Double[] aXY2 = getColorValues(rgb2[0], rgb2[1], rgb2[2]);
                String param2 = getJSONParameters(aXY2, bri);
                cmd[4] = param2;
                cmd[5] = "http://" + IP_ADDRESS + "/api/" + USER_ID + "/lights/2/state";
                executeCommand(cmd);
            }
        });
        buttonRandom2.setBounds(250, 320, 200, 25);
        panel.add(buttonRandom2);
    }

    // Creates curl command
    @Override
    public void actionPerformed(ActionEvent e) {
        String toggle;
        if (e.getSource() == buttonOn) {
            int r = tryParse(redText.getText());
            int g = tryParse(greenText.getText());
            int b = tryParse(blueText.getText());
            int bri = tryParse(briText.getText());

            Double[] aXY = getColorValues(r, g, b);
            toggle = getJSONParameters(aXY, bri);
        } else if (e.getSource() == buttonRandom) {
            int[] rgb = getRandomRGB();
            int bri = 255;

            Double[] aXY = getColorValues(rgb[0], rgb[1], rgb[2]);
            toggle = getJSONParameters(aXY, bri);
        } else if (e.getSource() == nightModeOn) {
            String colorXY = "[0.5690582185605182, 0.3952092253488186]";
            toggle = "{ \"on\": true, \"bri\": 75, \"xy\": " + colorXY +  "}";
        } else {
            toggle = "{\"on\": false}";
        }
        String[] cmd = {
                "curl",
                "-X",
                "PUT",
                "-d",
                toggle,
                "http://" + IP_ADDRESS + "/api/" + USER_ID + "/groups/0/action"
        };
        executeCommand(cmd);
    }

    // Ensures user input is valid, defaults to 100 if invalid
    public static int tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 100;
        }
    }

    // Returns an array containing a random RGB color code
    public static int[] getRandomRGB() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return new int[]{r, g, b};
    }

    // Returns parameters for curl command based on input
    public static String getJSONParameters(Double[] aXY, int bri) {
        String x = String.valueOf(aXY[0]);
        String y = String.valueOf(aXY[1]);
        return "{ \"on\": true, \"bri\": " + bri + ", \"xy\": [" + x + ", " + y + "] }";
    }

    // Executes curl command on local machine
    public static void executeCommand(String[] cmd) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cmd);
        builder.directory(new File(System.getProperty("user.home")));
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        assert exitCode == 0;
    }

    // code from: https://www.reddit.com/r/tasker/comments/4mzd01/using_rgb_colours_with_philips_hue_bulbs/
    // This function creates an xy pair that produces colors equivalent to RGB colors.
    public static Double[] getColorValues(int r, int g, int b) {
        double red = r / 255.0;
        double green = g / 255.0;
        double blue = b / 255.0;

        if (red > 0.04045) {
            red = Math.pow((red + 0.055) / (1.0 + 0.055), 2.4);
        } else red = (red / 12.92);

        if (green > 0.04045) {
            green = Math.pow((green + 0.055) / (1.0 + 0.055), 2.4);
        } else green = (green / 12.92);

        if (blue > 0.04045) {
            blue = Math.pow((blue + 0.055) / (1.0 + 0.055), 2.4);
        } else blue = (blue / 12.92);

        double X = red * 0.664511 + green * 0.154324 + blue * 0.162028;
        double Y = red * 0.283881 + green * 0.668433 + blue * 0.047685;
        double Z = red * 0.000088 + green * 0.072310 + blue * 0.986039;
        double x = X / (X + Y + Z);
        double y = Y / (X + Y + Z);
        return new Double[]{x, y};
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}