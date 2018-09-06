/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Observer;

import Controller.CalTrain2;
import Model.Core.Passenger;
import Model.Core.Passenger.Status;
import Model.Core.Segment;
import Model.Core.Train;

/**
 *
 * @author user
 */
public abstract class Observer {

    protected final CalTrain2 dispatcher;

    public Observer(CalTrain2 dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * @return the dispatcher
     */
    public CalTrain2 getDispatcher() {
        return dispatcher;
    }

    public void update(boolean showMsgs) {
        getDispatcher().visualize(showMsgs);
    }

    public void updateTrainPosition(Train train, Segment newLocation) {
        getDispatcher().updateTrainPosition(train, newLocation);
    }

    public void updatePassengerPosition(Passenger passenger, Segment newLocation) {
        getDispatcher().updatePassengerPosition(passenger, newLocation);
    }

    public void updatePassengerTrack(Passenger passenger, Segment newLocation, Status status) {
        getDispatcher().updatePassengerTrack(passenger, newLocation, status);
    }

    public void print(String msg, boolean showMsgs) {
        getDispatcher().print(msg, showMsgs);
    }
}
