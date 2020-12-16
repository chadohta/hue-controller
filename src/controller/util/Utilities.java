package controller.util;

import java.util.Random;

public class Utilities {
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
}
