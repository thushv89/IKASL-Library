/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class IKASLConstants {
 
    public static int DIMENSIONS;
    public static double MAX_BOUND;
    public static double MIN_BOUND;
    public static double STD_DEV_THRESHOLD = 0.125;
    
    public static double getDisparityThreshold(int dimension){
        double val=0;
        val += Math.pow(0.6, 2)*dimension;
        return Math.sqrt(val);
    }
    
}
