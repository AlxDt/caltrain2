/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Sync;

import Model.Core.Passenger;
import Model.Core.Segment;

/**
 *
 * @author user
 */
public class PassengerMoveSignal extends Signal {

    private final Passenger passenger;
    private final Segment newSegment;

    public PassengerMoveSignal(Passenger passenger, Segment newSegment) {
        this.passenger = passenger;
        this.newSegment = newSegment;
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
}
