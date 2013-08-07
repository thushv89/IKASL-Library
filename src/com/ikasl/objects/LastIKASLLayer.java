/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

import java.io.Serializable;

/**
 *
 * @author Ruchira
 */
public class LastIKASLLayer implements Serializable{
    // Neuronlayer object to be serialized
    private NeuronLayer neuronLayer = null;
    private int lastLearnCycle = 0;

    /**
     * @return the neuronLayer
     */
    public NeuronLayer getNeuronLayer() {
        return neuronLayer;
    }

    public LastIKASLLayer(NeuronLayer gLayer, int learnCycle){
        this.neuronLayer=gLayer;
        this.lastLearnCycle=learnCycle;
    }
    /**
     * @param neuronLayer the neuronLayer to set
     */
    public void setNeuronLayer(NeuronLayer neuronLayer) {
        this.neuronLayer = neuronLayer;
    }

    /**
     * @return the currentLearningCycleNo
     */
    public int getLastLearningCycle() {
        return lastLearnCycle;
    }

    /**
     * @param currentLearningCycleNo the currentLearningCycleNo to set
     */
    public void setLastLearningCycle(int currentLearningCycleNo) {
        this.lastLearnCycle = currentLearningCycleNo;
    }
    
    
}
