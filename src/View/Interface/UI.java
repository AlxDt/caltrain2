/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Interface;

import Controller.CalTrain2;
import Controller.Listeners;
import Model.Core.Passenger;
import Model.Core.Segment;
import Model.Core.Train;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class UI extends Application {

    public static Stage window;
    public static Scene scene;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    // Create an update queue to listen for updates
    LinkedBlockingQueue updateQueue = new LinkedBlockingQueue();

    // Sound functions
    public static void playSound(String filename) {
        try {
            // Play sound
            Media sound
                    = new Media(UI.class.getResource("/View/Res/" + filename).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(1.0);

            mediaPlayer.play();
        } catch (URISyntaxException ex) {
            System.out.println("Unable to play sound.");
        }
    }

    // Draw functions
    public static synchronized void drawTrains(GraphicsContext gc, HashMap<Train, Segment> trainPositionMap) {
        // Draw the trains corresponding to their position
        for (Map.Entry<Train, Segment> entry : trainPositionMap.entrySet()) {
            Train train = entry.getKey();
            Segment segment = entry.getValue();

            // If it's a null, draw nothing
            if (segment != null) {
                switch (((Segment) segment).getNum()) {
                    case 0:
                        gc.save();
                        gc.rotate(-38);

                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 137.5 - 185, 162.5 + 45);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 137.5 - 185, 162.5 + 45);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 137.5 - 185, 162.5 + 45);
                                break;
                        }

                        gc.restore();

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 120, 150);

                        break;
                    case 1:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 200, 110);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 200, 110);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 200, 110);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 200 + 47, 110 + 70);

                        break;
                    case 2:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 325, 110);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 325, 110);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 325, 110);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 325 + 47, 110 + 70);

                        break;
                    case 3:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 450, 110);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 450, 110);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 450, 110);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 450 + 47, 110 + 70);

                        break;
                    case 4:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 575, 110);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 575, 110);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 575, 110);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 575 + 47, 110 + 70);

                        break;
                    case 5:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 700, 110);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 700, 110);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 700, 110);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 700 + 47, 110 + 70);

                        break;
                    case 6:
                        gc.save();
                        gc.rotate(38);

                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 750 - 10, 500 - 910);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 750 - 10, 500 - 910);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 750 - 10, 500 - 910);
                                break;
                        }

                        gc.restore();

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 875, 150);

                        break;
                    case 7:
                        gc.save();
                        gc.rotate(90);

                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 225, -940);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 225, -940);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 225, -940);
                                break;
                        }

                        gc.restore();

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 960, 275);

                        break;
                    case 8:
                        gc.save();
                        gc.rotate(-38);

                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 400, 810);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 400, 810);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 400, 810);
                                break;
                        }

                        gc.restore();

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 875, 400);

                        break;
                    case 9:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 700, 410);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 700, 410);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 700, 410);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 700 + 47, 410 - 40);

                        break;
                    case 10:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 575, 410);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 575, 410);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 575, 410);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 575 + 47, 410 - 40);

                        break;
                    case 11:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 450, 410);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 450, 410);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 450, 410);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 450 + 47, 410 - 40);

                        break;
                    case 12:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 325, 410);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 325, 410);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 325, 410);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 325 + 47, 410 - 40);

                        break;
                    case 13:
                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 200, 410);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 200, 410);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 200, 410);
                                break;
                        }

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 200 + 47, 410 - 40);

                        break;
                    case 14:
                        gc.save();
                        gc.rotate(38);

                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train" + (train.isFull() ? "Full.png" : ".png")), 300 - 10, 200 - 5);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange" + (train.isFull() ? "Full.png" : ".png")), 300 - 10, 200 - 5);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen" + (train.isFull() ? "Full.png" : ".png")), 300 - 10, 200 - 5);
                                break;
                        }

                        gc.restore();

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 120, 400);

                        break;
                    case 15:
                        gc.save();
                        gc.rotate(-90);

                        switch (train.getColor()) {
                            case "YELLOW":
                                gc.drawImage(new Image("/View/Res/Train.png"), -325, 60);
                                break;
                            case "ORANGE":
                                gc.drawImage(new Image("/View/Res/TrainOrange.png"), -325, 60);
                                break;
                            default:
                                gc.drawImage(new Image("/View/Res/TrainGreen.png"), -325, 60);
                                break;
                        }

                        gc.restore();

                        gc.setFill(Color.BLACK);
                        gc.fillText("" + train.getNum(), 35, 275);

                        break;
                }
            }
        }
    }

    public static synchronized void drawPassengers(GraphicsContext gc, HashMap<Passenger, Segment> passengerPositionMap) {
        // Take note of the station passenger queue offsets
        int[] inOffsets = new int[CalTrain2.NUM_STATIONS];
        int[] outOffsets = new int[CalTrain2.NUM_STATIONS];

        Arrays.fill(inOffsets, 0);
        Arrays.fill(outOffsets, 0);

        // Draw the passengers corresponding to their position
        for (Map.Entry<Passenger, Segment> entry : passengerPositionMap.entrySet()) {
            Passenger passenger = entry.getKey();
            Segment segment = entry.getValue();

            gc.setFill(passenger.getColor());

            // If it's a null, draw nothing
            if (segment != null) {
                switch (((Segment) segment).getNum()) {
                    case 1:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(200 + 40, 110 - 40 - inOffsets[0], 10, 10);
                                inOffsets[0] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(200 + 40, 110 - 30 - inOffsets[0], 10, 10);
                                inOffsets[0] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(200 + 54, 110 - 30 - outOffsets[0], 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(200 + 54, 110 - 40 - outOffsets[0], 10, 10);
                                outOffsets[0] += 3;
                                break;
                        }

                        break;
                    case 3:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(450 + 40, 110 - 40 - inOffsets[1], 10, 10);
                                inOffsets[1] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(450 + 40, 110 - 30 - inOffsets[1], 10, 10);
                                inOffsets[1] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(450 + 54, 110 - 30 - outOffsets[1], 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(450 + 54, 110 - 40 - outOffsets[1], 10, 10);
                                outOffsets[1] += 3;
                                break;
                        }

                        break;
                    case 5:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(700 + 40, 110 - 40 - inOffsets[2], 10, 10);
                                inOffsets[2] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(700 + 40, 110 - 30 - inOffsets[2], 10, 10);
                                inOffsets[2] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(700 + 54, 110 - 30 - outOffsets[2], 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(700 + 54, 110 - 40 - outOffsets[2], 10, 10);
                                outOffsets[2] += 3;
                                break;
                        }

                        break;
                    case 7:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(960 - 90 - inOffsets[3], 275 - 7, 10, 10);
                                inOffsets[3] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(960 - 80 - inOffsets[3], 275 - 7, 10, 10);
                                inOffsets[3] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(960 - 80 - outOffsets[3], 275 + 7, 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(960 - 90 - outOffsets[3], 275 + 7, 10, 10);
                                outOffsets[3] += 3;
                                break;
                        }

                        break;
                    case 9:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(700 + 40, 410 + 60 + inOffsets[4], 10, 10);
                                inOffsets[4] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(700 + 40, 410 + 50 + inOffsets[4], 10, 10);
                                inOffsets[4] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(700 + 54, 410 + 50 + outOffsets[4], 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(700 + 54, 410 + 60 + outOffsets[4], 10, 10);
                                outOffsets[4] += 3;
                                break;
                        }

                        break;
                    case 11:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(450 + 40, 410 + 60 + inOffsets[5], 10, 10);
                                inOffsets[5] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(450 + 40, 410 + 50 + inOffsets[5], 10, 10);
                                inOffsets[5] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(450 + 54, 410 + 50 + outOffsets[5], 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(450 + 54, 410 + 60 + outOffsets[5], 10, 10);
                                outOffsets[5] += 3;
                                break;
                        }

                        break;
                    case 13:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(200 + 40, 410 + 60 + inOffsets[6], 10, 10);
                                inOffsets[6] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(200 + 40, 410 + 50 + inOffsets[6], 10, 10);
                                inOffsets[6] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(200 + 54, 410 + 50 + outOffsets[6], 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(200 + 54, 410 + 60 + outOffsets[6], 10, 10);
                                outOffsets[6] += 3;
                                break;
                        }

                        break;
                    case 15:
                        switch (passenger.getStatus()) {
                            case WAITING:
                                gc.fillOval(35 + 90 + inOffsets[7], 275 - 7, 10, 10);
                                inOffsets[7] += 5;
                                break;
                            case BOARDING:
                                gc.fillOval(35 + 80 + inOffsets[7], 275 - 7, 10, 10);
                                inOffsets[7] -= 5;
                                break;
                            case ALIGHTING:
                                gc.fillOval(35 + 80 + outOffsets[7], 275 + 7, 10, 10);
                                break;
                            case ARRIVED:
                                gc.fillOval(35 + 90 + outOffsets[7], 275 + 7, 10, 10);
                                outOffsets[7] += 3;
                                break;
                        }

                        break;
                }
            }
        }
    }

    public static synchronized void drawBackground(GraphicsContext gc) {
        // Background
        gc.setFill(Color.LIGHTSTEELBLUE);
        gc.fillRect(0, 0, UI.WIDTH, UI.HEIGHT);

        gc.setFill(Color.BLACK);

        // Service saturation
        gc.fillText("Service saturation: " + new DecimalFormat("#0.000").format(CalTrain2.getServiceSaturation()), 10, 20);

        // Segments
        gc.setStroke(Color.DARKGOLDENROD);
        gc.setLineWidth(5);

        // Segment 0 (entry point)
        gc.strokeLine(75, 225, 200, 125);

        // Segment 2
        gc.strokeLine(300, 125, 450, 125);

        // Segment 4
        gc.strokeLine(550, 125, 700, 125);

        // Segment 6
        gc.strokeLine(800, 125, 925, 225);

        // Segment 8
        gc.strokeLine(925, 325, 800, 425);

        // Segment 10
        gc.strokeLine(700, 425, 550, 425);

        // Segment 12
        gc.strokeLine(450, 425, 300, 425);

        // Segment 14
        gc.strokeLine(200, 425, 75, 325);

        // Stations
        gc.setFill(Color.ORANGE);

        // Station 1
        gc.fillRoundRect(200, 100, 100, 50, 25, 25);

        // Station 2
        gc.fillRoundRect(450, 100, 100, 50, 25, 25);

        // Station 3
        gc.fillRoundRect(700, 100, 100, 50, 25, 25);

        // Station 4
        gc.fillRoundRect(900, 225, 50, 100, 25, 25);

        // Station 5
        gc.fillRoundRect(200, 400, 100, 50, 25, 25);

        // Station 6
        gc.fillRoundRect(450, 400, 100, 50, 25, 25);

        // Station 7
        gc.fillRoundRect(700, 400, 100, 50, 25, 25);

        // Station 8
        gc.fillRoundRect(50, 225, 50, 100, 25, 25);

        // Station labels
        gc.setFill(Color.BLACK);

        // Station 1
        gc.fillText("1", 200 + 50, 100 + 25);

        // Station 2
        gc.fillText("2", 450 + 50, 100 + 25);

        // Station 3
        gc.fillText("3", 700 + 50, 100 + 25);

        // Station 4
        gc.fillText("4", 900 + 25, 225 + 50);

        // Station 5
        gc.fillText("7", 200 + 50, 400 + 25);

        // Station 6
        gc.fillText("6", 450 + 50, 400 + 25);

        // Station 7
        gc.fillText("5", 700 + 50, 400 + 25);

        // Station 8
        gc.fillText("8", 50 + 25, 225 + 50);
    }

    public static synchronized void redrawUI(GraphicsContext gc,
            HashMap<Train, Segment> trainPositionMap,
            HashMap<Passenger, Segment> passengerPositionMap) {
        // Clear canvas
        gc.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);

        // General exception catcher, just in case
        try {
            // Draw the background (including the stations)
            drawBackground(gc);

            // Draw the trains
            drawTrains(gc, trainPositionMap);

            // Draw the passengers
            drawPassengers(gc, passengerPositionMap);
        } catch (Exception ex) {
            System.out.println("Non-fatal error.");
        }
    }

    public static void passengerTrackerMessage(String msg) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION, msg);
        dialog.show();

        playSound("chime.wav");
    }

    // Launchers
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        // Border pane
        BorderPane root = new BorderPane();

        // Canvas for drawing
        Canvas canvas = new Canvas(UI.WIDTH, UI.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initially redraw the UI
        redrawUI(gc, CalTrain2.trainPositionMap, CalTrain2.passengerPositionMap);

        // Window configuration
        scene = new Scene(root);

        root.setCenter(canvas);

        // Toolbar configuration
        HBox toolbar = new HBox(50);

        toolbar.setAlignment(Pos.CENTER);
        toolbar.setStyle("-fx-background-color: orange;");
        toolbar.setPadding(new Insets(5, 5, 5, 5));

        Button newPassenger = new Button("Create new passenger");
        Button newTrain = new Button("Manually add a train");
        Button runSim = new Button("Start a self-running simulation");

        toolbar.getChildren().addAll(newPassenger, newTrain, runSim);

        root.setTop(toolbar);

        window.setScene(scene);
        window.setTitle("CalTrain II");
        window.setHeight(UI.HEIGHT);
        window.setWidth(UI.WIDTH);
        window.setResizable(false);
        window.show();

        window.setOnCloseRequest(e -> {
            System.exit(0);
        });

        // MAIN ENTRY POINT //
        // Start the controller in a separate thread
        // to avoid choking the UI thread
        CalTrain2 dispatcher = new CalTrain2(updateQueue);

        // Set the capacity of the trains
        SetTrains.display(this);

        new Thread(() -> {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    System.out.println("Services interrupted.");
                }

                dispatcher.start();
            }
        }).start();

        // UPDATE LISTENERS //
        // Update listener
        Listeners.updateListener(this, gc, updateQueue);

        // New passengers button listener
        newPassenger.setOnAction(e -> {
            Listeners.newPassengersListener(dispatcher);
        });

        // Manually add a train button listener
        newTrain.setOnAction(e -> {
            if (CalTrain2.trainsDispatched >= CalTrain2.MAX_TRAINS) {
                newTrain.setDisable(true);
            } else {
                Listeners.addTrain(dispatcher);
            }
        });

        // Simulation button listener
        runSim.setOnAction(e -> {
            Listeners.simListener(dispatcher);

            runSim.setDisable(true);
        });

        // Saturation polling listener
        Listeners.satListener(dispatcher);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
