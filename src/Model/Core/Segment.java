/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Core;

import Controller.CalTrain2;
import java.util.concurrent.Semaphore;

/**
 *
 * @author user
 */
public abstract class Segment {

    // Core attributes
    private int num;

    // Synchronization attributes
    private boolean isOccupied;
    private Train trainInside;
    private boolean isReserved;

    private Semaphore semaphore;

    // Link attributes
    private Segment nextSegment;
    private Segment prevSegment;

    // Constructor
    public Segment(int num) {
        this.num = num;

        this.isOccupied = false;
        this.trainInside = null;
        this.isReserved = false;

        this.semaphore = new Semaphore(1, true);

        this.nextSegment = null;
        this.prevSegment = null;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return the isOccupied
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * @param isOccupied the isOccupied to set
     */
    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    /**
     * @return the trainInside
     */
    public Train getTrainInside() {
        return trainInside;
    }

    /**
     * @param trainInside the trainInside to set
     */
    public void setTrainInside(Train trainInside) {
        this.trainInside = trainInside;
    }

    /**
     * @return the isReserved
     */
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * @param isReserved the isReserved to set
     */
    public void setIsReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }

    /**
     * @return the semaphore
     */
    public Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     * @param semaphore the semaphore to set
     */
    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
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
     * @return the prevSegment
     */
    public Segment getPrevSegment() {
        return prevSegment;
    }

    /**
     * @param prevSegment the prevSegment to set
     */
    public void setPrevSegment(Segment prevSegment) {
        this.prevSegment = prevSegment;
    }
}
