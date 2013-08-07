/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

import com.ikasl.enums.LayerType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class IKASLLayer {

    private Map<String,ArrayList<NeuronLayer>> learningLayer;
    private NeuronLayer genLayer;

    public IKASLLayer() {
        learningLayer = new HashMap<String, ArrayList<NeuronLayer>> ();
        genLayer = new NeuronLayer();
    }

    /**
     * @return the learningLayer
     */
    public Map<String,ArrayList<NeuronLayer>> getLearningLayer() {
        return learningLayer;
    }

    /**
     * @param learningLayer the learningLayer to set
     */
    public void setLearningLayer(Map<String,ArrayList<NeuronLayer>> learningLayer) {
        this.learningLayer = new HashMap<String, ArrayList<NeuronLayer>>(learningLayer);
    }

    /**
     * @return a clone of genLayer
     */
    public NeuronLayer getGenLayer() {
        return genLayer;
    }

    public NeuronLayer getCloneGenLayer() {
        NeuronLayer genLayerCopy = new NeuronLayer(genLayer.getNeurons(),LayerType.GENERALIZED);
        return genLayerCopy;
    }

    /**
     * @param genLayer the genLayer to set
     */
    public void setGenLayer(NeuronLayer genLayer) {
        this.genLayer.setlType(LayerType.GENERALIZED);
        this.genLayer.setNeurons(genLayer.getNeurons());
    }
}
