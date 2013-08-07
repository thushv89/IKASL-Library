/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

import com.ikasl.enums.LayerType;
import com.ikasl.utils.Utils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class NeuronLayer implements Serializable{

    private Map<String, Node> neurons;
    private LayerType lType;
    
    public NeuronLayer(Map<String, Node> map,LayerType lType) {
        this.neurons = new HashMap<String, Node>(map);
        this.lType = lType;
    }
    public NeuronLayer(){
        this.neurons = new HashMap<String, Node>();
    }
    /**
     * @return the neurons
     */
    public Map<String, Node> getNeurons() {
        return neurons;
    }

    public Map<String, Node> getCloneOfNeurons() {
        return new HashMap<String, Node>(neurons);
    }
    
    /**
     * @param neurons the neurons to set
     */
    public void setNeurons(Map<String, Node> neurons) {
        this.neurons.clear();
        this.neurons.putAll(neurons);
    }

    public void addNeuron(Node node) {
        String key = Utils.generateIndexString(node.getX(), node.getY());
        this.neurons.put(key, node);
    }
    public void addNeuron(Node node,String key) {
        
        this.neurons.put(key, node);
    }

    /**
     * @return the lType
     */
    public LayerType getlType() {
        return lType;
    }

    /**
     * @param lType the lType to set
     */
    public void setlType(LayerType lType) {
        this.lType = lType;
    }
}
