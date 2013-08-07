/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

import com.ikasl.id.EntityID;
import java.io.Serializable;
import org.omg.CORBA.TRANSIENT;

/**
 *
 * @author Thush
 */
public class GNode extends Node implements Serializable{
    private EntityID id;
    private String nodeSequence;    //would become obsolete with the introduction of ID
    
    //====== TO BE USED ONLY FOR GEN NODES =========
    private boolean disparityThreshExceeded;
    private int min_X;
    private int min_Y;
    
    public GNode(){        
    }
       
    public GNode(int x, int y,double[] weights,String nodeSeq){
        super(x,y,weights);
        this.nodeSequence=nodeSeq;
        min_X=-1;
        min_Y=-1;
    }
    
    /**
     * @return the nodeSequence
     */
    public String getNodeSequence() {
        return nodeSequence;
    }

    /**
     * @param nodeSequence the nodeSequence to set
     */
    public void setNodeSequence(String nodeSequence) {
        this.nodeSequence = nodeSequence;
    }

    /**
     * @return the disparityThreshExceeded
     */
    public boolean isDisparityThreshExceeded() {
        return disparityThreshExceeded;
    }

    /**
     * @param disparityThreshExceeded the disparityThreshExceeded to set
     */
    public void setDisparityThreshExceeded(boolean disparityThreshExceeded) {
        this.disparityThreshExceeded = disparityThreshExceeded;
    }

    /**
     * @return the max_X
     */
    public int getMin_X() {
        return min_X;
    }

    /**
     * @param min_X the max_X to set
     */
    public void setMin_X(int min_X) {
        if(min_X>this.min_X){
            this.min_X = min_X;
        }
    }

    /**
     * @return the max_Y
     */
    public int getMin_Y() {
        return min_Y;
    }

    /**
     * @param min_Y the max_Y to set
     */
    public void setMin_Y(int min_Y) {
        if(min_Y>this.min_Y){
            this.min_Y = min_Y;
        }
    }

    /**
     * @return the id
     */
    public EntityID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(EntityID id) {
        this.id = id;
    }

}
