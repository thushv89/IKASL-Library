/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.Utils;

/**
 *
 * @author Thush
 */
public class LNode extends Node{
    private double errorValue;
    private int hitValue;
    
    public LNode(int x, int y, double[] weights){
        super(x,y,weights);
        this.errorValue=0;
        this.hitValue=0;
    }
    
    public void calcAndUpdateErr(double[] iWeight) {
        this.errorValue += Utils.calcEucDist(this.weights, iWeight, iWeight.length);
    }

    public double getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(double errorValue) {
        this.errorValue = errorValue;
    }
    
    
    
}
