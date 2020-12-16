# Philips Hue Desktop Controller

A Graphical User Interface (GUI) that allows you to control Philips Hue 
light bulbs from your Laptop/Desktop.

The primary way to control Philips Hue light bulbs is through their mobile app.
I often leave my phone charging away from my desk, and so I built this application 
to control my lights without having to get up 😄

![](demo.gif)

## Usage

In order for this application to work your Laptop/Desktop must be connected to the same
network as your Philips Hue bridge.

1. Obtain your bridge IP address and create an authorized user. Instructions on how to do 
both can be found here [Philips Hue API](https://developers.meethue.com/develop/get-started-2/).
   
2. Clone this repo to your local machine using
    ```
    git clone https://github.com/chadohta/philips-hue-gui.git
    ```
   
3. In the Controller class change the final variables to your bridge's IP address and user ID obtained in step one
    ```
    public class Controller implements ActionListener {
        private static final String IP_ADDRESS = "YOUR_BRIDGE_IP_ADDRESS";
        private static final String USER_ID = "YOUR_AUTHORIZED_USER_ID";
        
        ...
    }
    ```
4. Compile and run the program.

### Make a Desktop Shortcut

Instead of opening the program in an IDE everytime you wanted to change your lights you can create 
a desktop shortcut that will run the java program with a simple double-click.

1. After compiling the program, navigate to the directory that contains the Controller.class file. Note that
   this is different from the Controller.java file. Create a manifest file called controller.mf that contains 
   the following:
   ```
   Manifest-Version: 1.0
   Main-Class: Controller
   ```

2. In the command line navigate to the directory that contains the Controller.class and controller.mf file.
Create a .jar file by executing the following command:
   ```
   jar cfm controller.jar controller.mf *.class
   
   ```

3. Use Windows Explorer or macOS Finder to locate your project repo. Find the controller.jar file. 
   
4. Right-click the controller.jar file and select "Create Shortcut" (Windows) or "Make Alias" (macOS).

5. Drag the shortcut/alias to your Desktop.

## Technologies Used

* Java + Swing
* Phillips Hue API
