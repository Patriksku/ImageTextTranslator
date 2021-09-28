package org.imagetextapp;

import org.imagetextapp.gui.Controller;
import org.imagetextapp.gui.Model;
import org.imagetextapp.gui.View;

import javax.swing.*;

public class App {


    public static void main( String[] args )  {
        // Invoke thread for the program
        SwingUtilities.invokeLater(() -> {
        Model model = new Model();
        View view = new View("ImageTextTranslator");
        Controller controller = new Controller(model, view);

        // Start the program.
        controller.initializeApplication();
        });
//        System.out.println(System.getProperty("java.class.path"));
    }
}
