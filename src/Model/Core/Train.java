/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Core;

import Controller.CalTrain2;
import Model.Observer.TrainObserver;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Train implements Runnable {

    // Core attributes
    private final int num;
    private final int capacity;
    private int taken;
    private boolean isFull;
    private final String color;

    private final int[] destinationCounters;

    // Link attributes
    private final FreeSegment entryPoint;
    private Segment currSegment;
    private Segment nextSegment;

    // Synchronization attributes
    private Semaphore[] destinationSemaphores;

    // Observers
    private final CalTrain2 dispatcher;
    private final TrainObserver trainObserver;

    // Constructor
    public Train(int num, int capacity, FreeSegment entryPoint, String color, CalTrain2 dispatcher) {
        this.num = num;
        this.capacity = capacity;
        this.taken = 0;
        this.isFull = false;
        this.color = color;

        this.destinationCounters = new int[CalTrain2.NUM_STATIONS];

        this.entryPoint = entryPoint;

        this.currSegment = entryPoint;
        this.nextSegment = currSegment.getNextSegment();

        this.destinationSemaphores = new Semaphore[CalTrain2.NUM_STATIONS];

        for (int i = 0; i < destinationSemaphores.length; i++) {
            destinationSemaphores[i] = new Semaphore(0, true);
        }

        this.dispatcher = dispatcher;
        this.trainObserver = new TrainObserver(this.dispatcher);
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @return the taken
     */
    public int getTaken() {
        return taken;
    }

    /**
     * @param taken the taken to set
     */
    public void setTaken(int taken) {
        this.taken = taken;
    }

    /**
     * @return the isFull
     */
    public boolean isFull() {
        return isFull;
    }

    /**
     * @param isFull the isFull to set
     */
    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    /**
     * @return the destinationCounters
     */
    public int[] getDestinationCounters() {
        return destinationCounters;
    }

    /**
     * @param destinationCounters the destinationCounters to set
     */
    public void setDestinationCounters(int[] destinationCounters) {
        this.setDestinationCounters(destinationCounters);
    }

    /**
     * @return the entryPoint
     */
    public FreeSegment getEntryPoint() {
        return entryPoint;
    }

    /**
     * @return the currSegment
     */
    public Segment getCurrSegment() {
        return currSegment;
    }

    /**
     * @param currSegment the currSegment to set
     */
    public void setCurrSegment(Segment currSegment) {
        this.currSegment = currSegment;
    }

    /**
     * @return the nextSegment
     */
    public Segment getNextSegment() {
        return nextSegment;
    }

    /**
     * @param nextSegment the nextSegment to set
     */
    public void setNextSegment(Segment nextSegment) {
        this.nextSegment = nextSegment;
    }

    /**
     * @return the destinationSemaphores
     */
    public Semaphore[] getDestinationSemaphores() {
        return destinationSemaphores;
    }

    /**
     * @param destinationSemaphores the destinationSemaphores to set
     */
    public void setDestinationSemaphores(Semaphore[] destinationSemaphores) {
        this.destinationSemaphores = destinationSemaphores;
    }

    // Move forward
    private void proceed() {
        // We've just moved to the next segment
        currSegment = nextSegment;
        nextSegment = currSegment.getNextSegment();
    }

    /**
     * @return the trainObserver
     */
    public TrainObserver getTrainObserver() {
        return trainObserver;
    }

    /**
     * @return the dispatcher
     */
    public CalTrain2 getDispatcher() {
        return dispatcher;
    }

    // Alight passengers
    private void alightPassengersMonitor(boolean showMsgs) {
        //print("Train #" + num + " is alighting passengers...", showMsgs);
        trainObserver.print("Train #" + num + " is alighting passengers...", showMsgs);

        // This is the current station monitor
        // Notify the passengers that we're ready to take them out
        Station currentStationMonitor = ((Station) currSegment).getStationAlightLock();

        synchronized (currentStationMonitor) {
            currentStationMonitor.notifyAll();
        }

        // A one second delay to make the animation easier to follow
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            trainObserver.print("Services interrupted.", showMsgs);
        }
    }

    // Load passengers
    // station_load_train(struct station *station, int count)
    private void loadPassengersMonitor(boolean showMsgs) {
        //print("Train #" + num + " is boarding passengers...", showMsgs);
        trainObserver.print("Train #" + num + " is boarding passengers...", showMsgs);

        // This is the current station monitor
        // Notify the passengers that we're ready to take them in
        Station currentStationMonitor = ((Station) currSegment).getStationBoardLock();

        synchronized (currentStationMonitor) {
            currentStationMonitor.notifyAll();
        }

        // A one second delay to make the animation easier to follow
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            trainObserver.print("Services interrupted.", showMsgs);
        }
    }

    // Alight passengers
    private void alightPassengersSemaphore(boolean showMsgs) {
        //print("Train #" + num + " is alighting passengers...", showMsgs);
        trainObserver.print("Train #" + num + " is alighting passengers...", showMsgs);

        try {
            // This is the current station semaphore
            // Notify the passengers that we're ready to take them out
            Semaphore isDoneSemaphore = ((Station) currSegment).getIsDoneAlightingSemaphore();

            int alighting = destinationCounters[((Station) currSegment).getNum() / 2];

            Semaphore alightingSemaphore = destinationSemaphores[((Station) currSegment).getNum() / 2];

            // If there are no passengers alighting, just move on!
            // todo, change to >
            if (alighting > 0) {
                while (alighting > 0) {
                    // Passenger by passenger, tell leaving passengers to get off!
                    alightingSemaphore.release();

                    alighting--;
                }

                // Wait until everyone's done
                isDoneSemaphore.acquire();
            }

            // A 1 second delay to make the animation easier to follow
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            trainObserver.print("Services interrupted.", showMsgs);
        }
    }

    // Load passengers
    // station_load_train(struct station *station, int count)
    private void loadPassengersSemaphore(boolean showMsgs) {
        //print("Train #" + num + " is boarding passengers...", showMsgs);
        trainObserver.print("Train #" + num + " is boarding passengers...", showMsgs);

        try {
            // This is the current station semaphore
            // Notify the passengers that we're ready to take them in
            Semaphore currentStationSemaphore = ((Station) currSegment).getStationBoardSemaphore();
            Semaphore isDoneSemaphore = ((Station) currSegment).getIsDoneBoardingSemaphore();

            int waiting = ((Station) currSegment).getWaiting();
            int boarded = taken;

            // If there are no waiting passengers or the train is full, move on!
            if (waiting > 0 && !isFull) {
                // Passenger by passenger, tell them to hop on!
                while (waiting > 0 && !isFull) {
                    currentStationSemaphore.release();

                    // Train-side checking
                    waiting--;
                    boarded++;

                    // If we're full, stop telling passengers to get in!
                    if (boarded == capacity) {
                        break;
                    }
                }

                // Wait until everyone's done
                isDoneSemaphore.acquire();
            }

            // A 1 second delay to make the animation easier to follow
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            trainObserver.print("Services interrupted.", showMsgs);
        }
    }

    // Pause the thread for n seconds
    public void sleep(int n, boolean showMsgs) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException ex) {
            trainObserver.print("Services interrupted.", showMsgs);
        }
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
        // This is the entry monitor
        Segment entryPointMonitor = entryPoint;

        synchronized (entryPointMonitor) {
            try {
                // Wait until the entry point branch is clear
                // This loop guards against spurious wakeups as recommended
                // by the official Java documentation
                while (entryPoint.isOccupied()) {
                    entryPointMonitor.wait();
                }

                // Take this spot
                entryPoint.setIsOccupied(true);
                entryPoint.setTrainInside(this);

                trainObserver.print("Train #" + num + " is waiting for loop entry...", showMsgs);

                // The next segment monitor
                Segment nextSegmentMonitor = nextSegment;

                synchronized (nextSegmentMonitor) {
                    // Wait until the segment AFTER the entry point branch is unreserved
                    // This loop guards against spurious wakeups as recommended
                    // by the official Java documentation
                    while (nextSegment.isReserved()) {
                        nextSegmentMonitor.wait();
                    }

                    // Immediately claim the next segment
                    nextSegment.setIsReserved(true);
                }

                // Leave this spot
                entryPoint.setIsOccupied(false);
                entryPoint.setTrainInside(null);
            } catch (InterruptedException ex) {
                trainObserver.print("Services interrupted.", showMsgs);
            }

            // Enter the line!
            proceed();

            // Notify the trains that are waiting for this segment to be clear
            entryPointMonitor.notify();
        }

        // Run this code indefinitely
        while (true) {
            // This is the current segment monitor
            // Only one train can ever occupy this segment
            // Take note of the current segment
            Segment currSegmentMonitor = currSegment;

            synchronized (currSegmentMonitor) {
                // Take this spot
                currSegment.setIsOccupied(true);
                currSegment.setTrainInside(this);
                currSegment.setIsReserved(false);

                // Notify this train's observer that its position has changed
                // Console position update
                trainObserver.update(showMsgs);

                // GUI position update
                trainObserver.updateTrainPosition(this, currSegment);

                // If this segment is a station, load and unload passengers
                if (currSegment instanceof Station) {
                    trainObserver.print("Train #" + num + " is now at " + ((Station) currSegment).getName() + "...", showMsgs);

                    // Open doors and allow passengers to get off and on
                    alightPassengersMonitor(showMsgs);
                    loadPassengersMonitor(showMsgs);
                } else {
                    // Else, take a second to move
                    sleep(1000, showMsgs);
                }

                // This is the next segment monitor
                Segment nextSegmentMonitor = nextSegment;

                synchronized (nextSegmentMonitor) {
                    // Is it okay to proceed?
                    try {
                        // Wait until the next segment is clear
                        // This loop guards against spurious wakeups as recommended
                        // by the official Java documentation
                        while (nextSegment.isReserved()) {
                            nextSegmentMonitor.wait();
                        }

                        // Immediately claim the next segment
                        nextSegment.setIsReserved(true);
                    } catch (InterruptedException ex) {
                        trainObserver.print("Services interrupted.", showMsgs);
                    }
                }

                // Leave this spot
                currSegment.setIsOccupied(false);
                currSegment.setTrainInside(null);

                // If ready, then proceed
                proceed();

                // Then tell others we're done occupying that spot
                currSegmentMonitor.notify();
            }
        }
    }

    // Run with semaphores
    public void runWithSemaphores(boolean showMsgs) {
        try {
            // This is the entry point semaphore
            Semaphore entryPointSemaphore = entryPoint.getSemaphore();

            // Wait until the entry point branch is clear
            entryPointSemaphore.acquire();

            // Take this spot
            entryPoint.setIsOccupied(true);
            entryPoint.setTrainInside(this);

            trainObserver.print("Train #" + num + " is waiting for loop entry...", showMsgs);

            // This is the entry next segment semaphore
            Semaphore entryNextSegmentSemaphore = nextSegment.getSemaphore();

            // Wait until the segment AFTER the entry point branch is unreserved
            entryNextSegmentSemaphore.acquire();

            // Immediately claim the next segment
            nextSegment.setIsReserved(true);

            // Leave this spot
            entryPoint.setIsOccupied(false);
            entryPoint.setTrainInside(null);

            // Enter the line!
            proceed();

            // Notify the trains that are waiting for this segment to be clear
            entryPointSemaphore.release();

            // Run this code indefinitely
            while (true) {
                // This is the current segment semaphore
                // Only one train can ever occupy this segment
                // Take note of the current segment
                Semaphore currentSegmentSemaphore = currSegment.getSemaphore();

                // Take this spot
                currSegment.setIsOccupied(true);
                currSegment.setTrainInside(this);
                currSegment.setIsReserved(false);

                // Notify this train's observer that its position has changed
                // Console position update
                trainObserver.update(showMsgs);

                // GUI position update
                trainObserver.updateTrainPosition(this, currSegment);

                // If this segment is a station, load and unload passengers
                if (currSegment instanceof Station) {
                    trainObserver.print("Train #" + num + " is now at " + ((Station) currSegment).getName() + "...", showMsgs);

                    // Open doors and allow passengers to get off and on
                    alightPassengersSemaphore(showMsgs);
                    loadPassengersSemaphore(showMsgs);
                } else {
                    // Else, take a second to move
                    sleep(1000, showMsgs);
                }

                // This is the next segment semaphore
                Semaphore nextSegmentSemaphore = nextSegment.getSemaphore();

                // Is it okay to proceed?
                // Wait until the next segment is clear
                nextSegmentSemaphore.acquire();

                // Immediately claim the next segment
                nextSegment.setIsReserved(true);

                // Leave this spot
                currSegment.setIsOccupied(false);
                currSegment.setTrainInside(null);

                // If ready, then proceed
                proceed();

                // Then tell others we're done occupying that spot
                currentSegmentSemaphore.release();
            }
        } catch (InterruptedException ex) {
            System.out.println("Services interrupted.");
        }
    }

    // Reserve a seat
    public synchronized boolean reserveSeat() {
        if (taken < capacity) {
            taken++;

            if (taken == capacity) {
                isFull = true;
            }

            return true;
        } else {
            return false;
        }
    }

    // Leave a seat
    public synchronized void leaveSeat() {
        taken--;

        isFull = false;
    }

    // Register one's destination
    public synchronized void registerDestination(Station destination) {
        for (int i = 0; i < CalTrain2.NUM_STATIONS; i++) {
            if (destination.equals((Station) dispatcher.getSegments()[i * 2 + 1])) {
                destinationCounters[i]++;

                break;
            }
        }
    }

    // Unregister one's destination
    public synchronized void unregisterDestination(Station destination) {
        for (int i = 0; i < CalTrain2.NUM_STATIONS; i++) {
            if (destination.equals((Station) dispatcher.getSegments()[i * 2 + 1])) {
                destinationCounters[i]--;

                break;
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        return ((Train) object).num == this.num;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 67 * hash + this.num;

        return hash;
    }
}
