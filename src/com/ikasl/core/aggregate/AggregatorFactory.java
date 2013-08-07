/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core.aggregate;

import com.ikasl.enums.AggregatorType;

/**
 *
 * @author Thush
 */
public class AggregatorFactory {
    
    public IKASLGeneralizer getAggregator(AggregatorType type){
        if(type == AggregatorType.FUZZY){
            return new FuzzyAggregator();
        }else if(type == AggregatorType.AVERAGE){
             return new AverageAggregator();
        }else{
            return null;
        }
    }
}
