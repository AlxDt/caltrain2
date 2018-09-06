/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Observer;

import Controller.CalTrain2;

/**
 *
 * @author user
 */
public class PassengerObserver extends Observer {

    public PassengerObserver(CalTrain2 dispatcher) {
        super(dispatcher);
    }

    public synchronized void addPassenger() {
        dispatcher.addPassenger();
    }

    public synchronized void removePassenger() {
        dispatcher.removePassenger();
    }
}
