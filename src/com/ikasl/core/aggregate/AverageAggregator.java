/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core.aggregate;

import com.ikasl.objects.GNode;
import com.ikasl.objects.Node;
import com.ikasl.utils.ArrayHelper;
import com.ikasl.utils.Utils;
import java.util.List;

/**
 *
 * @author Thush
 */
public class AverageAggregator implements IKASLGeneralizer {

    @Override
    public GNode generalize(Node hit, List<Node> neigh1, List<Node> neigh2, double disthreshold, int dimensions) {
        double thresholdMultiplier = 1.25;  //this is to give GNodes some buffere space when calculating disparity threshold
        double[] weights;

        double[] totalNeigh1 = new double[dimensions];
        double[] totalNeigh2 = new double[dimensions];

        int countNeigh1 = 0;
        int countNeigh2 = 0;

        for (Node n1 : neigh1) {
            if(Utils.calcEucDist(hit.getWeights(), n1.getWeights(), dimensions)< disthreshold*thresholdMultiplier){
                totalNeigh1 = ArrayHelper.add(totalNeigh1, n1.getWeights(), dimensions);
                countNeigh1++;
            }else{
                System.out.println("Avg Aggregator - Ignored the neigh 1 node coz of disparity threshold ("+
                        Utils.calcEucDist(hit.getWeights(), n1.getWeights(),dimensions)+">"+disthreshold*thresholdMultiplier+")");
            }
        }
        for (Node n2 : neigh2) {
            if(Utils.calcEucDist(hit.getWeights(), n2.getWeights(), dimensions)<disthreshold){
                totalNeigh2 = ArrayHelper.add(totalNeigh2, n2.getWeights(), dimensions);
                countNeigh2++;
            }else{
                //System.out.println("Avg Aggregator - Ignored the neigh 2 node coz of disparity threshold");
            }
        }

        if(countNeigh1==0){countNeigh1=1;}
        if(countNeigh2==0){countNeigh2=1;}
        
        weights = ArrayHelper.multiplyArrayByConst(hit.getWeights(), 0.7);

        totalNeigh1 = ArrayHelper.multiplyArrayByConst(totalNeigh1, 1 / countNeigh1);
        totalNeigh1 = ArrayHelper.multiplyArrayByConst(totalNeigh1, 0.3);
        weights = ArrayHelper.add(weights, totalNeigh1, dimensions);

        for (double val : weights) {
            if (val > 1) {
                val = 1;
            } else if (val < 0) {
                val = 0;
            }
        }

        return new GNode(0, 0, weights, null);
    }
}
