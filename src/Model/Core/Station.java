/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Core;

import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Station extends Segment implements Cloneable {

    // Core attributes
    private final String name;

    private int waiting;
    private int boarding;
    private int alighting;

    // Synchronization attributes
    private Station stationBoardLock;
    private Station stationAlightLock;

    private Semaphore stationBoardSemaphore;
    private Semaphore stationAlightSemaphore;

    private Semaphore isDoneAlightingSemaphore;
    private Semaphore isDoneBoardingSemaphore;

    // Constructor
    public Station(String name, int num) {
        super(num);

        this.name = name;

        this.waiting = 0;
        this.boarding = 0;
        this.alighting = 0;

        try {
            // Create clones of this station for the passengers to wait on
            this.stationBoardLock = (Station) this.clone();
            this.stationAlightLock = (Station) this.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("Cannot create a clone of this station.");

            this.stationBoardLock = null;
            this.stationAlightLock = null;
        }

        this.stationBoardSemaphore = new Semaphore(0, true);
        this.stationAlightSemaphore = new Semaphore(0, true);

        this.isDoneAlightingSemaphore = new Semaphore(0, true);
        this.isDoneBoardingSemaphore = new Semaphore(0, true);

        /*try {
            this.stationBoardSemaphore.acquire();
            this.stationAlightSemaphore.acquire();
            this.doneWaitingSemaphore.acquire();
        } catch (InterruptedException ex) {
            System.out.println("Services interrupted.");
        }*/
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the waiting
     */
    public int getWaiting() {
        return waiting;
    }

    /**
     * @param waiting the waiting to set
     */
    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    /**
     * @return the boarding
     */
    public int getBoarding() {
        return boarding;
    }

    /**
     * @param boarding the boarding to set
     */
    public void setBoarding(int boarding) {
        this.boarding = boarding;
    }

    /**
     * @return the alighting
     */
    public int getAlighting() {
        return alighting;
    }

    /**
     * @param alighting the alighting to set
     */
    public void setAlighting(int alighting) {
        this.alighting = alighting;
    }

    /**
     * @return the stationBoardLock
     */
    public Station getStationBoardLock() {
        return stationBoardLock;
    }

    /**
     * @param stationBoardLock the stationBoardLock to set
     */
    public void setStationBoardLock(Station stationBoardLock) {
        this.stationBoardLock = stationBoardLock;
    }

    /**
     * @return the stationAlightLock
     */
    public Station getStationAlightLock() {
        return stationAlightLock;
    }

    /**
     * @param stationAlightLock the stationAlightLock to set
     */
    public void setStationAlightLock(Station stationAlightLock) {
        this.stationAlightLock = stationAlightLock;
    }

    /**
     * @return the stationBoardSemaphore
     */
    public Semaphore getStationBoardSemaphore() {
        return stationBoardSemaphore;
    }

    /**
     * @param stationBoardSemaphore the stationBoardSemaphore to set
     */
    public void setStationBoardSemaphore(Semaphore stationBoardSemaphore) {
        this.stationBoardSemaphore = stationBoardSemaphore;
    }

    /**
     * @return the stationAlightSemaphore
     */
    public Semaphore getStationAlightSemaphore() {
        return stationAlightSemaphore;
    }

    /**
     * @param stationAlightSemaphore the stationAlightSemaphore to set
     */
    public void setStationAlightSemaphore(Semaphore stationAlightSemaphore) {
        this.stationAlightSemaphore = stationAlightSemaphore;
    }

    /**
     * @return the isDoneAlightingSemaphore
     */
    public Semaphore getIsDoneAlightingSemaphore() {
        return isDoneAlightingSemaphore;
    }

    /**
     * @param isDoneAlightingSemaphore the isDoneAlightingSemaphore to set
     */
    public void setIsDoneAlightingSemaphore(Semaphore isDoneAlightingSemaphore) {
        this.isDoneAlightingSemaphore = isDoneAlightingSemaphore;
    }

    /**
     * @return the isDoneBoardingSemaphore
     */
    public Semaphore getIsDoneBoardingSemaphore() {
        return isDoneBoardingSemaphore;
    }

    /**
     * @param isDoneBoardingSemaphore the isDoneBoardingSemaphore to set
     */
    public void setIsDoneBoardingSemaphore(Semaphore isDoneBoardingSemaphore) {
        this.isDoneBoardingSemaphore = isDoneBoardingSemaphore;
    }

    // Add a waiting passenger
    public synchronized void addWaitingPassenger() {
        waiting++;
    }

    // Remove a waiting passenger
    public synchronized void removeWaitingPassenger() {
        waiting--;
    }

    // Add a boarding passenger
    public synchronized void addBoardingPassenger() {
        boarding++;
    }

    // Remove a boarding passenger
    public synchronized void removeBoardingPassenger() {
        boarding--;
    }

    // Add an alighting passenger
    public synchronized void addAlightingPassenger() {
        setAlighting(getAlighting() + 1);
    }

    // Remove an alighting passenger
    public synchronized void removeAlightingPassenger() {
        setAlighting(getAlighting() - 1);
    }

    // Get the station 
    public Station getStation(int steps) {
        Segment currSegment = this;

        for (int i = 1; i <= steps;) {
            currSegment = currSegment.getNextSegment();

            if (currSegment instanceof Station) {
                i++;
            }
        }

        return (Station) currSegment;
    }

    @Override
    public boolean equals(Object object) {
        return this.name.equals(((Station) object).getName());
    }

    @Override
    public int hashCode() {
        int hash = 5;

        hash = 17 * hash + Objects.hashCode(this.name);

        return hash;
    }
}
