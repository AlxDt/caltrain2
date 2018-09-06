/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Core.FreeSegment;
import Model.Core.Passenger;
import Model.Core.Passenger.Status;
import Model.Core.Segment;
import Model.Core.Station;
import Model.Core.Train;
import Model.Sync.PassengerMoveSignal;
import Model.Sync.PassengerTrackSignal;
import Model.Sync.TrainMoveSignal;
import View.Interface.SetTrains;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author user
 */
public class CalTrain2 {

    // Number of stations
    public static final int NUM_STATIONS = 8;

    // Number of maximum trains
    public static final int MAX_TRAINS = 15;

    // Number of (initial) passengers
    public static final int INITIAL_PASSENGERS = 10;

    // Default train capacity
    public final static int DEFAULT_CAPACITY = 15;

    // Number of trains dispatched
    public static int trainsDispatched = 0;

    // Number of passengers in the system
    public static int numPassengers = 0;

    // The system segments
    private Segment[] segments;

    // The train fleet
    private Train[] fleet;

    // The total train capacities
    public static int[] capacities = new int[MAX_TRAINS];

    // The effective capacity
    public static int effectiveCapacity = 0;

    // Automatic dispatch on?
    public static boolean isAutomaticDispatch = true;

    // The runnable train fleet
    private Thread[] trains;

    // Update queue
    private final LinkedBlockingQueue updateQueue;

    // The train position map
    public static HashMap<Train, Segment> trainPositionMap = new HashMap<>();

    // The train position map
    public static HashMap<Passenger, Segment> passengerPositionMap = new HashMap<>();

    // Passenger removal lock
    public static Object removalLock = new Object();

    // Constructor
    public CalTrain2(LinkedBlockingQueue updateQueue) {
        this.updateQueue = updateQueue;
    }

    /**
     * @return the segments
     */
    public Segment[] getSegments() {
        return segments;
    }

    /**
     * @return the capacities
     */
    public int[] getCapacities() {
        return capacities;
    }

    public void start() {
        // Set the infrastructure up
        segments = setInfrastructure();

        // Set the train fleet up
        fleet = setTrains();

        // Make the fleet runnable
        trains = prepareTrains();

        // Operation loop
        System.out.println("Starting services...");

        // Add an initial train
        //activateTrain();
    }

    // Set the train stations up
    public Segment[] setInfrastructure() {
        // Create the segments
        Segment[] newSegments = new Segment[NUM_STATIONS * 2 + 1];

        for (int i = 0; i < newSegments.length - 1; i++) {
            if (i % 2 != 0) {
                newSegments[i] = new Station("Station " + (i / 2 + 1), i);
            } else {
                newSegments[i] = new FreeSegment(i);
            }
        }

        // Link the segments to form a circular linked list
        for (int i = 0; i < newSegments.length - 1; i++) {
            if (i > 0) {
                newSegments[i].setPrevSegment(newSegments[(i - 1) % (newSegments.length - 1)]);
            } else {
                newSegments[i].setPrevSegment(newSegments[newSegments.length - 2]);
            }

            newSegments[i].setNextSegment(newSegments[(i + 1) % (newSegments.length - 1)]);
        }

        // Configure the entry point branch (segment[16])
        newSegments[newSegments.length - 1] = new FreeSegment(newSegments.length - 1);
        Segment entryPoint = newSegments[newSegments.length - 1];
        entryPoint.setPrevSegment(null);
        entryPoint.setNextSegment(newSegments[0]);

        return newSegments;
    }

    // Set the train fleet up
    public Train[] setTrains() {
        // Create the trains
        Train[] newTrains = new Train[CalTrain2.MAX_TRAINS];

        for (int i = 0; i < newTrains.length; i++) {
            String color;

            switch (i % 3) {
                case 0:
                    color = "YELLOW";

                    break;
                case 1:
                    color = "ORANGE";

                    break;
                default:
                    color = "GREEN";

                    break;
            }

            newTrains[i] = new Train(i + 1, capacities[i], (FreeSegment) getSegments()[getSegments().length - 1], color, this);
            trainPositionMap.put(newTrains[i], null);
        }

        return newTrains;
    }

    // Make the fleet runnable
    public Thread[] prepareTrains() {
        Thread[] newTrains = new Thread[fleet.length];

        for (int i = 0; i < newTrains.length; i++) {
            newTrains[i] = new Thread(fleet[i]);
        }

        return newTrains;
    }

    // Activate a train for dispatch
    public synchronized void activateTrain() {
        // Only do this if there are still trains available to be dispatched
        try {
            // Start that train
            trains[trainsDispatched].start();

            // Add it to the effective capacity
            effectiveCapacity += fleet[trainsDispatched].getCapacity();

            // One more train recorded
            trainsDispatched++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("No more trains!");
        }
    }

    // One more passenger!
    public synchronized void addPassenger() {
        CalTrain2.numPassengers++;
    }

    // Remove a passenger!
    public synchronized void removePassenger() {
        CalTrain2.numPassengers--;
    }

    // Visualize the system
    public synchronized void visualize(boolean showMsgs) {
        if (!showMsgs) {
            // Header
            for (int i = 0; i < getSegments().length - 1; i++) {
                System.out.print((getSegments()[i] instanceof Station)
                        ? "S" : "F");
            }

            System.out.println();

            for (int i = 1; i < getSegments().length - 1; i += 2) {
                System.out.print(" " + (i / 2 + 1));
            }

            System.out.println();

            for (int i = 0; i < getSegments().length - 1; i++) {
                System.out.print("=");
            }

            System.out.println();

            // Content
            for (int i = 0; i < getSegments().length - 1; i++) {
                // Free segment
                if (i % 2 == 0) {
                    System.out.print((getSegments()[i].getTrainInside() != null)
                            ? Integer.toHexString(getSegments()[i].getTrainInside().getNum()) : " ");
                } else {
                    // Station
                    System.out.print((getSegments()[i].getTrainInside() != null)
                            ? Integer.toHexString(getSegments()[i].getTrainInside().getNum()) : " ");
                }
            }

            System.out.println();

            for (int i = 0; i < getSegments().length * 2 - 1; i++) {
                System.out.print("=");
            }

            System.out.println();
        }
    }

    // Print a signal message
    public synchronized void print(String msg, boolean showMsgs) {
        if (showMsgs) {
            System.out.println(msg);
        }
    }

    // Update the train visualization
    public void updateTrainPosition(Train train, Segment newLocation) {
        updateQueue.add(new TrainMoveSignal(train, newLocation));
    }

    // Update the passenger visualization
    public void updatePassengerPosition(Passenger passenger, Segment newLocation) {
        updateQueue.add(new PassengerMoveSignal(passenger, newLocation));
    }

    // Update the tracking visualization
    public void updatePassengerTrack(Passenger passenger, Segment newLocation, Status status) {
        updateQueue.add(new PassengerTrackSignal(passenger, newLocation, status));
    }

    // Request train heuristic
    public synchronized void requestTrain() {
        // Only do this if trains are automatically
        if (isAutomaticDispatch) {
            double serviceSaturation = getServiceSaturation();

            // If our services are 75% saturated or greater
            if (serviceSaturation >= 0.75 && CalTrain2.trainsDispatched < CalTrain2.MAX_TRAINS) {
                activateTrain();
            }
        }
    }

    public static synchronized double getServiceSaturation() {
        return ((double) CalTrain2.numPassengers) / CalTrain2.effectiveCapacity;
    }

    // Pause the thread for n seconds
    public void sleep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException ex) {
            System.out.println("Services interrupted.");
        }
    }
}
