/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Sync;

import Model.Core.Segment;
import Model.Core.Train;

/**
 *
 * @author user
 */
public class TrainMoveSignal extends Signal {

    private final Train train;
    private final Segment newLocation;

    public TrainMoveSignal(Train train, Segment newLocation) {
        this.train = train;
        this.newLocation = newLocation;
    }

    /**
     * @return the train
     */
    public Train getTrain() {
        return train;
    }

    /**
     * @return the newLocation
     */
    public Segment getNewLocation() {
        return newLocation;
    }
}
