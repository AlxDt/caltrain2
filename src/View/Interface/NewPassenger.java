/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Interface;

import Controller.CalTrain2;
import Controller.Listeners;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class NewPassenger {

    public static Stage window;
    public static Scene scene;

    public static final int WIDTH = 300;

    // Launchers
    public static void display(CalTrain2 dispatcher) {
        window = new Stage();

        // Border pane
        GridPane root = new GridPane();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setVgap(10.0);
        root.setHgap(5.0);

        // Window configuration
        scene = new Scene(root);

        // Fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Name this passenger");

        Label originLabel = new Label("Origin:");
        TextField originField = new TextField();
        originField.setPromptText("1 to 8");

        Label destinationLabel = new Label("Destination:");
        TextField destinationField = new TextField();
        destinationField.setPromptText("1 to 8");

        CheckBox isTracked = new CheckBox("Track this passenger");

        Button createButton = new Button("Add passenger");

        root.add(nameLabel, 0, 0);
        root.add(nameField, 1, 0);
        root.add(originLabel, 0, 1);
        root.add(originField, 1, 1);
        root.add(destinationLabel, 0, 2);
        root.add(destinationField, 1, 2);
        root.add(isTracked, 0, 3);
        root.add(createButton, 0, 4);

        window.setScene(scene);
        window.setTitle("New");
        window.setResizable(false);
        window.show();

        // Handlers
        createButton.setOnAction(e -> {
            Listeners.addPassenger(nameField.getText(),
                    originField.getText(),
                    destinationField.getText(),
                    isTracked.isSelected(),
                    dispatcher);
        });
    }
}
