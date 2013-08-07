package com.ikasl.objects;

import com.ikasl.id.EntityID;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.Utils;
import java.io.Serializable;

public abstract class Node implements Serializable{

    protected int X;
    protected int Y;
    protected double[] weights;
    protected String parentID;
    protected int hitValue;

    public Node(){
        
    }
    protected Node(int x, int y, double[] weights) {
        this.X = x;
        this.Y = y;
        this.weights = weights.clone();
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights.clone();
    }

    public void adjustWeights(double[] iWeights, double influence, double learningRate) {

        for (int i = 0; i < IKASLConstants.DIMENSIONS; i++) {
            weights[i] += influence * learningRate * (iWeights[i] - weights[i]);
        }
    }

    

    /**
     * @return the hitValue
     */
    public int getHitValue() {
        return hitValue;
    }

    /**
     * @param hitValue the hitValue to set
     */
    public void setHitValue(int hitValue) {
        this.hitValue = hitValue;
    }

    /**
     * @return the parentID
     */
    public String getParentID() {
        return parentID;
    }

    /**
     * @param parentID the parentID to set
     */
    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

}
