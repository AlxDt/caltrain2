/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Core;

import Controller.CalTrain2;
import Model.Observer.PassengerObserver;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class Passenger implements Runnable {

    // Core attributes
    private final String name;
    private Station origin;
    private Station destination;
    private Status status;
    private Train currTrain;
    private Color color;
    private final boolean isTracked;

    // Observers
    private final PassengerObserver passengerObserver;

    public enum Status {
        WAITING, BOARDING, SEATED, ALIGHTING, ARRIVED
    };

    // Constructor
    public Passenger(String name, int origin, int destination, boolean isTracked, CalTrain2 dispatcher) {
        this.name = name;
        this.origin = (Station) dispatcher.getSegments()[origin * 2 - 1];
        this.destination = (Station) dispatcher.getSegments()[destination * 2 - 1];
        this.status = Status.WAITING;
        this.currTrain = null;
        this.color = Color.color(Math.random(), Math.random(), Math.random());
        this.isTracked = isTracked;

        this.passengerObserver = new PassengerObserver(dispatcher);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the origin
     */
    public Station getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Station origin) {
        this.origin = origin;
    }

    /**
     * @return the destination
     */
    public Station getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Station destination) {
        this.destination = destination;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the currTrain
     */
    public Train getCurrTrain() {
        return currTrain;
    }

    /**
     * @param currTrain the currTrain to set
     */
    public void setCurrTrain(Train currTrain) {
        this.currTrain = currTrain;
    }

    /**
     * @return the isTracked
     */
    public boolean isTracked() {
        return isTracked;
    }

    @Override
    public void run() {
        // Debug variable to use monitors or not
        boolean useMonitors = true;

        // Debug variable to show print statements (or not)
        boolean showMsgs = true;

        // Use monitors or not?
        if (useMonitors) {
            runWithMonitors(showMsgs);
        } else {
            runWithSemaphores(showMsgs);
        }
    }

    // Run with monitors
    public void runWithMonitors(boolean showMsgs) {
        // Is this passenger in his/her destination station?
        boolean isArrived = false;

        // Run this code indefinitely
        while (!isArrived) {
            // What the passenger does depends on his/her status
            switch (status) {
                case WAITING:
                    // Hey look, a passenger!
                    passengerObserver.addPassenger();

                    // Wait for the train
                    // This is the origin station monitor
                    Station originStationMonitor = origin.getStationBoardLock();

                    // station_wait_for_train(struct station *station)
                    synchronized (originStationMonitor) {
                        try {
                            // Console position update
                            passengerObserver.print("Passenger " + name + " is waiting at " + origin.getName(), showMsgs);

                            // GUI position update (waiting)
                            passengerObserver.updatePassengerPosition(this, origin);

                            // Tracker position update
                            if (isTracked) {
                                passengerObserver.updatePassengerTrack(this, origin, status);
                            }

                            // Wait until the train is ready to pick the
                            // passengers up or until a train with free
                            // seats is available
                            // This loop guards against spurious wakeups as recommended
                            // by the official Java documentation
                            while (!origin.isOccupied() || !origin.getTrainInside().reserveSeat()) {
                                originStationMonitor.wait();
                            }
                        } catch (InterruptedException ex) {
                            passengerObserver.print("Services interrupted.", showMsgs);
                        }
                    }

                    // Get ready to board!
                    status = Status.BOARDING;

                    break;
                case BOARDING:
                    // This is the time between the passengers waiting for the
                    // train and actually sitting down
                    // i.e., the passengers are in the process of boarding

                    // This passenger is now getting in the train
                    currTrain = origin.getTrainInside();

                    // Console position update
                    passengerObserver.print("Passenger " + name + " is boarding Train " + currTrain.getNum(), showMsgs);

                    // GUI position update (boarding)
                    passengerObserver.updatePassengerPosition(this, origin);

                    // Tracker position update
                    if (isTracked) {
                        passengerObserver.updatePassengerTrack(this, origin, status);
                    }

                    // Just sit down
                    status = Status.SEATED;

                    break;
                case SEATED:
                    // Wait for the destination station
                    // This is the destination monitor
                    Station destinationStationMonitor = destination.getStationAlightLock();

                    // Tracker position update
                    if (isTracked) {
                        passengerObserver.updatePassengerTrack(this, origin, status);
                    }

                    synchronized (destinationStationMonitor) {
                        try {
                            // Console position update
                            passengerObserver.print("Passenger " + name + " is waiting for "
                                    + destination.getName() + " at Train " + currTrain.getNum(), showMsgs);

                            // Wait until the destination station is occupied
                            // and the train occupying it is the very train this
                            // passenger is on
                            // This loop guards against spurious wakeups as recommended
                            // by the official Java documentation
                            while (!destination.isOccupied() || !destination.getTrainInside().equals(currTrain)) {
                                destinationStationMonitor.wait();
                            }
                        } catch (InterruptedException ex) {
                            passengerObserver.print("Services interrupted.", showMsgs);
                        }
                    }

                    // If it is this station, get off!
                    status = Status.ALIGHTING;

                    break;
                case ALIGHTING:
                    // Console position update
                    passengerObserver.print("Passenger " + name + " is alighting.", showMsgs);

                    // GUI position update (alighting)
                    passengerObserver.updatePassengerPosition(this, destination);

                    // Tracker position update
                    if (isTracked) {
                        passengerObserver.updatePassengerTrack(this, destination, status);
                    }

                    // Leave the seat
                    currTrain.leaveSeat();

                    // This passenger is now getting off the train
                    currTrain = null;

                    // We've arrived!
                    status = Status.ARRIVED;

                    break;
                case ARRIVED:
                    // Console position update
                    passengerObserver.print("Passenger " + name + " is done!", showMsgs);

                    // GUI position update (arrived)
                    passengerObserver.updatePassengerPosition(this, destination);

                    // Tracker position update
                    if (isTracked) {
                        passengerObserver.updatePassengerTrack(this, destination, status);
                    }

                    // Bye passenger!
                    passengerObserver.removePassenger();

                    // Get out because we're here and we're done!
                    isArrived = true;

                    break;
            }
        }
    }

    // Run with semaphores
    public void runWithSemaphores(boolean showMsgs) {
        try {
            // Is this passenger in his/her destination station?
            boolean isArrived = false;

            // This is the origin station semaphore
            Semaphore originStationBoardSemaphore = origin.getStationBoardSemaphore();

            // This is the destination station semaphore
            Semaphore destinationStationAlightSemaphore = destination.getStationAlightSemaphore();

            // This is the origin station done semaphore
            Semaphore originStationDoneSemaphore = origin.getIsDoneBoardingSemaphore();

            // This is the destination station done semaphore
            Semaphore destinationStationDoneSemaphore = destination.getIsDoneAlightingSemaphore();

            // Run this code indefinitely
            while (!isArrived) {
                // What the passenger does depends on his/her status
                switch (status) {
                    case WAITING:
                        // Hey look, a passenger!
                        passengerObserver.addPassenger();

                        // One more passenger is now waiting
                        origin.addWaitingPassenger();

                        // Wait for the train
                        // station_wait_for_train(struct station *station)
                        // Console position update
                        passengerObserver.print("Passenger " + name + " is waiting at " + origin.getName(), showMsgs);

                        // GUI position update (waiting)
                        passengerObserver.updatePassengerPosition(this, origin);

                        // Tracker position update
                        if (isTracked) {
                            passengerObserver.updatePassengerTrack(this, origin, status);
                        }

                        // Wait until the train is ready to pick the
                        // passengers up or until a train with free
                        // seats is available
                        originStationBoardSemaphore.acquire();

                        // Remove a waiting passenger
                        origin.removeWaitingPassenger();

                        // Reserve a seat inside
                        origin.getTrainInside().reserveSeat();

                        // Get ready to board!
                        status = Status.BOARDING;

                        break;
                    case BOARDING:
                        // This is the time between the passengers waiting for the
                        // train and actually sitting down
                        // i.e., the passengers are in the process of boarding
                        // Now boarding
                        origin.addBoardingPassenger();

                        // This passenger is now getting in the train
                        currTrain = origin.getTrainInside();

                        // Regsiter one's destination
                        currTrain.registerDestination(destination);

                        // Console position update
                        passengerObserver.print("Passenger " + name + " is boarding Train " + currTrain.getNum(), showMsgs);

                        // GUI position update (boarding)
                        passengerObserver.updatePassengerPosition(this, origin);

                        // Tracker position update
                        if (isTracked) {
                            passengerObserver.updatePassengerTrack(this, origin, status);
                        }

                        // Remove a boarding passenger
                        origin.removeBoardingPassenger();

                        // Just sit down
                        status = Status.SEATED;

                        break;
                    case SEATED:
                        // If everyone is now seated, the train can go
                        if (origin.getBoarding() == 0) {
                            originStationDoneSemaphore.release();
                        }

                        // Tracker position update
                        if (isTracked) {
                            passengerObserver.updatePassengerTrack(this, origin, status);
                        }

                        // Console position update
                        passengerObserver.print("Passenger " + name + " is waiting for "
                                + destination.getName() + " at Train " + currTrain.getNum(), showMsgs);

                        // Wait for the destination station
                        // Set your eyes at the destination
                        currTrain.getDestinationSemaphores()[destination.getNum() / 2].acquire();

                        /*int notifyCounter = 0;
                        
                        do {
                            // False alarm!
                            if (notifyCounter > 0) {
                                destinationStationAlightSemaphore.release();
                            }

                            destinationStationAlightSemaphore.acquire();
                        } while (!destination.isOccupied() || !destination.getTrainInside().equals(currTrain));*/
                        // If it is this station, get off!
                        status = Status.ALIGHTING;

                        break;
                    case ALIGHTING:
                        // Now alighting
                        destination.addAlightingPassenger();

                        // Console position update
                        passengerObserver.print("Passenger " + name + " is alighting.", showMsgs);

                        // GUI position update (alighting)
                        passengerObserver.updatePassengerPosition(this, destination);

                        // Tracker position update
                        if (isTracked) {
                            passengerObserver.updatePassengerTrack(this, destination, status);
                        }

                        // Leave the seat
                        currTrain.leaveSeat();

                        // Unregister one's destination
                        currTrain.unregisterDestination(destination);

                        // This passenger is now getting off the train
                        currTrain = null;

                        // We've arrived!
                        status = Status.ARRIVED;

                        // Remove an alighting passenger
                        destination.removeAlightingPassenger();

                        break;
                    case ARRIVED:
                        // If everyone has now alighted, the train can board
                        if (destination.getAlighting() == 0) {
                            destinationStationDoneSemaphore.release();
                        }

                        // Console position update
                        passengerObserver.print("Passenger " + name + " is done!", showMsgs);

                        // GUI position update (arrived)
                        passengerObserver.updatePassengerPosition(this, destination);

                        // Tracker position update
                        if (isTracked) {
                            passengerObserver.updatePassengerTrack(this, destination, status);
                        }

                        // Bye passenger!
                        passengerObserver.removePassenger();

                        // Get out because we're here and we're done!
                        isArrived = true;

                        break;
                }
            }
        } catch (InterruptedException ex) {
            passengerObserver.print("Services interrupted.", showMsgs);
        }
    }
}
