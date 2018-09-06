/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Sync;

import Model.Core.Passenger;
import Model.Core.Passenger.Status;
import Model.Core.Segment;

/**
 *
 * @author user
 */
public class PassengerTrackSignal extends Signal {

    private final Passenger passenger;
    private final Segment newSegment;
    private final Status status;

    public PassengerTrackSignal(Passenger passenger, Segment newSegment, Status status) {
        this.passenger = passenger;
        this.newSegment = newSegment;
        this.status = status;
    }

    /**
     * @return the passenger
     */
    public Passenger getPassenger() {
        return passenger;
    }

    /**
     * @return the newSegment
     */
    public Segment getNewSegment() {
        return newSegment;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }
}
