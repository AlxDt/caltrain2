/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Interface;

import Controller.CalTrain2;
import Controller.Listeners;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class SetTrains {

    public static Stage window;
    public static Scene scene;

    public static final int WIDTH = 300;

    // Launchers
    public static void display(UI uiLock) {
        window = new Stage();

        // Border pane
        GridPane root = new GridPane();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setVgap(10.0);
        root.setHgap(5.0);

        // Window configuration
        scene = new Scene(root);

        // Fields
        Label[] trainLabels = new Label[CalTrain2.MAX_TRAINS];
        TextField[] trainFields = new TextField[CalTrain2.MAX_TRAINS];

        for (int i = 0; i < CalTrain2.MAX_TRAINS; i++) {
            trainLabels[i] = new Label("Train " + (i + 1));
            trainFields[i] = new TextField();
            trainFields[i].setPromptText("Set this train's capacity");

            root.add(trainLabels[i], 0, i);
            root.add(trainFields[i], 1, i);
        }

        CheckBox isAutomatic = new CheckBox("Automatic train dispatch");

        Button startButton = new Button("Choo choo!");

        root.add(isAutomatic, 0, 16);
        root.add(startButton, 0, 17);

        window.setScene(scene);
        window.setTitle("Start");
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();

        // Handlers
        window.setOnCloseRequest(e -> e.consume());

        startButton.setOnAction(e -> {
            // Add capacities
            Listeners.addCapacities(trainFields);

            // Automatic dispatch?
            Listeners.setAutomaticDispatch(isAutomatic.isSelected());

            // Close this window
            window.close();

            // Start the UI
            synchronized (uiLock) {
                uiLock.notify();
            }
        });
    }
}
