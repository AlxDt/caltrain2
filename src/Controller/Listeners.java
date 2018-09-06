/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Core.Passenger;
import Model.Core.Passenger.Status;
import Model.Core.Station;
import Model.Sync.PassengerMoveSignal;
import Model.Sync.PassengerTrackSignal;
import Model.Sync.Signal;
import Model.Sync.TrainMoveSignal;
import View.Interface.NewPassenger;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.scene.canvas.GraphicsContext;
import View.Interface.UI;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 *
 * @author user
 */
public class Listeners {

    public static String msg;

    // Update listener
    public synchronized static void updateListener(UI dispatcher, GraphicsContext gc, LinkedBlockingQueue updateQueue) {
        new Thread(() -> {
            // The trains' positions
            HashMap trainPositionMap = CalTrain2.trainPositionMap;

            // The passengers' positions
            HashMap passengerPositionMap = CalTrain2.passengerPositionMap;

            // Listen indefinitely
            while (true) {
                // Wait for a signal that a train has moved
                Signal signal = (Signal) updateQueue.poll();

                // What kind of signal?
                if (signal instanceof TrainMoveSignal) {
                    // Train has moved
                    // Update that train's position
                    trainPositionMap.put(((TrainMoveSignal) signal).getTrain(), ((TrainMoveSignal) signal).getNewLocation());

                    // Don't choke the JavaFX thread!
                    Platform.runLater(() -> {
                        // Redraw UI canvas
                        UI.redrawUI(gc, trainPositionMap, passengerPositionMap);
                    });
                } else if (signal instanceof PassengerMoveSignal) {
                    // Update the passenger's position
                    passengerPositionMap.put(((PassengerMoveSignal) signal).getPassenger(), ((PassengerMoveSignal) signal).getNewSegment());

                    // Don't choke the JavaFX thread!
                    Platform.runLater(() -> {
                        // Redraw UI canvas
                        UI.redrawUI(gc, trainPositionMap, passengerPositionMap);
                    });
                } else if (signal instanceof PassengerTrackSignal) {
                    // Give the location of the passenger
                    Passenger passenger = ((PassengerTrackSignal) signal).getPassenger();
                    Station station = (Station) ((PassengerTrackSignal) signal).getNewSegment();
                    Status status = ((PassengerTrackSignal) signal).getStatus();

                    msg = "";

                    switch (status) {
                        case WAITING:
                            msg = "Passenger " + passenger.getName() + " is waiting at " + station.getName() + ".";
                            break;
                        case BOARDING:
                            msg = "Passenger " + passenger.getName() + " is boarding Train " + passenger.getCurrTrain().getNum() + " at "
                                    + station.getName() + ".";
                            break;
                        case SEATED:
                            msg = "Passenger " + passenger.getName() + " is now seated inside Train " + passenger.getCurrTrain().getNum() + ".";
                            break;
                        case ALIGHTING:
                            msg = "Passenger " + passenger.getName() + " now alighting at "
                                    + station.getName() + ".";
                            break;
                        case ARRIVED:
                            msg = "Passenger " + passenger.getName() + " has arrived at " + station.getName() + ".";
                            break;
                    }

                    // Don't choke the JavaFX thread!
                    Platform.runLater(() -> {
                        // Display the notification
                        UI.passengerTrackerMessage(msg);
                    });
                }
            }
        }).start();
    }

    // Set trains listener
    public static void addCapacities(TextField[] trainFields) {
        for (int i = 0; i < trainFields.length; i++) {
            try {
                CalTrain2.capacities[i] = Integer.parseInt(trainFields[i].getText());
            } catch (NumberFormatException ex) {
                CalTrain2.capacities[i] = CalTrain2.DEFAULT_CAPACITY;
            }
        }
    }

    // Will there be automatic dispatch?
    public static void setAutomaticDispatch(boolean isDispatch) {
        CalTrain2.isAutomaticDispatch = isDispatch;
    }

    // New passenger button listener
    public static void newPassengersListener(CalTrain2 dispatcher) {
        NewPassenger.display(dispatcher);
    }

    // Start a simulation listener
    public static void simListener(CalTrain2 dispatcher) {
        new Thread(() -> {
            for (int i = 1;; i++) {
                int originNo = new Random().nextInt(8) + 1;
                int destinationNo;

                do {
                    destinationNo = new Random().nextInt(8) + 1;
                } while (destinationNo == originNo);

                new Thread(new Passenger(Integer.toString(i), originNo, destinationNo, false, dispatcher)).start();

                try {
                    // Wait for at most 3 secs before generating another passenger
                    Thread.sleep(new Random().nextInt(3000));
                } catch (InterruptedException ex) {
                    System.out.println("Services interrupted.");
                }
            }
        }).start();
    }

    // Start a saturation listener
    public static void satListener(CalTrain2 dispatcher) {
        new Thread(() -> {
            for (;;) {
                try {
                    // Wait for at most 3 secs before polling
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.out.println("Services interrupted.");
                }

                // Poll for saturation
                dispatcher.requestTrain();
            }
        }).start();
    }

    // Add a passenger listener
    public static void addPassenger(String name, String origin, String destination, boolean isTracked, CalTrain2 dispatcher) {
        int originNo;
        int destinationNo;

        try {
            originNo = Integer.parseInt(origin);
            destinationNo = Integer.parseInt(destination);
        } catch (NumberFormatException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, "You messed up the inputs.");
            error.show();

            return;
        }

        // If the user enters incorrect parameters, inform them
        if ((originNo <= 0 || originNo > CalTrain2.NUM_STATIONS)
                || (destinationNo <= 0 || destinationNo > CalTrain2.NUM_STATIONS)
                || (originNo == destinationNo)) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Invalid parameters.");
            error.show();

            return;
        }

        // Create a new passenger thread
        new Thread(new Passenger(name, originNo, destinationNo, isTracked, dispatcher)).start();
    }
    
    // Manually add a new train
    public static void addTrain(CalTrain2 dispatcher) {
        dispatcher.activateTrain();
    }
}
