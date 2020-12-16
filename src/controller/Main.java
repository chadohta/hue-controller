package controller;

import controller.gui.MainWindow;
/*
 The entry point for the hue controller. Run this file to start the application.

 This application launches a GUI that the user can interact with to change the
 color(s) of their Philips Hue lights. Based on user input
 this program will generate an API call directed at the user's
 Philips Hue bridge and execute it on the local machine using curl.
 */
public class Main {
    public static void main(String[] args) {
        MainWindow.launch();
    }
}
