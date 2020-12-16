package controller;

import controller.util.Utilities;
import controller.gui.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Controller implements ActionListener {
    private static final String IP_ADDRESS = "YOUR_BRIDGE_IP_ADDRESS";
    private static final String USER_ID = "YOUR_AUTHORIZED_USER_ID";

    // HANDLES SINGLE COLOR LIGHT CHANGES
    // Creates curl command based on user input
    @Override
    public void actionPerformed(ActionEvent e) {
        String params;
        if (e.getSource() == MainWindow.buttonOn) {
            int r = Utilities.tryParse(MainWindow.redText.getText());
            int g = Utilities.tryParse(MainWindow.greenText.getText());
            int b = Utilities.tryParse(MainWindow.blueText.getText());
            int bri = Utilities.tryParse(MainWindow.briText.getText());

            Double[] aXY = Utilities.getColorValues(r, g, b);
            params = Utilities.getJSONParameters(aXY, bri);
        } else if (e.getSource() == MainWindow.buttonRandom) {
            int[] rgb = Utilities.getRandomRGB();
            int bri = 255;

            Double[] aXY = Utilities.getColorValues(rgb[0], rgb[1], rgb[2]);
            params = Utilities.getJSONParameters(aXY, bri);
        } else if (e.getSource() == MainWindow.nightModeOn) {
            String colorXY = "[0.5690582185605182, 0.3952092253488186]";
            params = "{ \"on\": true, \"bri\": 75, \"xy\": " + colorXY +  "}";
        } else {
            params = "{\"on\": false}";
        }
        String[] cmd = {
                "curl",
                "-X",
                "PUT",
                "-d",
                params,
                "http://" + IP_ADDRESS + "/api/" + USER_ID + "/groups/0/action"
        };
        executeCommand(cmd);
    }

    // Switches hue lights to bubble gum colors
    public static void bubbleColors() {
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

    // Switches hue lights to two randomly generated colors
    public static void randomColors() {
        int[] rgb = Utilities.getRandomRGB();
        int bri = 255;
        Double[] aXY = Utilities.getColorValues(rgb[0], rgb[1], rgb[2]);

        String param = Utilities.getJSONParameters(aXY, bri);
        String[] cmd = {
                "curl",
                "-X",
                "PUT",
                "-d",
                param,
                "http://" + IP_ADDRESS + "/api/" + USER_ID + "/lights/1/state"
        };
        executeCommand(cmd);

        int[] rgb2 = Utilities.getRandomRGB();
        Double[] aXY2 = Utilities.getColorValues(rgb2[0], rgb2[1], rgb2[2]);
        String param2 = Utilities.getJSONParameters(aXY2, bri);
        cmd[4] = param2;
        cmd[5] = "http://" + IP_ADDRESS + "/api/" + USER_ID + "/lights/2/state";
        executeCommand(cmd);
    }

    // Executes curl command on local machine
    private static void executeCommand(String[] cmd) {
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