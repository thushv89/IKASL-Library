/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

/**
 *
 * @author Thushan Ganegedara
 */
public class GSOMConstants {
    public static int DIMENSIONS;

    private static double GT;

    public static double SPREAD_FACTOR;
    public static double MAX_NEIGHBORHOOD_RADIUS;
    public static double FD;
    public static double START_LEARNING_RATE;
    public static int MAX_ITERATIONS;
        
    public static double getGT(){
        GT = - DIMENSIONS * DIMENSIONS * Math.log(SPREAD_FACTOR);
        return GT;
    }
}
