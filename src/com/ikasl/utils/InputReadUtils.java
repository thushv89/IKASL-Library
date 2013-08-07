/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

import java.util.ArrayList;

/**
 *
 * @author Thushan Ganegedara
 */
public class InputReadUtils {
    
    public static void normalizeWithBounds(int dimensions, ArrayList<double[]> inputs,double minBound,double maxBound) {
       
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < inputs.size(); j++) {
                double[] inArr = inputs.get(j);
                
                //do this if there's some value other than 0 is in column
                if(maxBound - minBound > 0){
                inArr[i] = (inArr[i] - minBound) / (maxBound - minBound);
                inputs.set(j, inArr);
                }
            }
        }
    }
}
