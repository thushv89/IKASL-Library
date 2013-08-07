/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core.aggregate;

import com.ikasl.core.aggregate.IKASLGeneralizer;
import com.ikasl.objects.GNode;
import com.ikasl.objects.Node;
import com.ikasl.utils.ArrayHelper;
import com.ikasl.utils.Utils;
import java.util.*;

/**
 *
 * @author Lasindu
 */
public class FuzzyAggregator implements IKASLGeneralizer {

    private static double SUGENO_LAMBDA_MEASURE = 0.05;

    @Override
    public GNode generalize(Node hit, List<Node> neigh1, List<Node> neigh2, double disthreshold, int dimensions) {

        ArrayList<Node> allNodes = new ArrayList<Node>();
        ArrayList<Double> allNodesDistances = new ArrayList<Double>();
        double[] weights = new double[dimensions];
        // add all nodes to a list
        allNodes.add(hit);
        allNodes.addAll(neigh1);
        allNodes.addAll(neigh2);
        
        //calculate euclidean distances for all the nodes with respect to winning node.
        //(1-distance) to get the fuzzy g(X) value
        for (int i = 0; i < allNodes.size(); i++) {
            allNodesDistances.add(1 - (Utils.calcEucDist(allNodes.get(0).getWeights(), allNodes.get(i).getWeights(), dimensions)) / 2 * Math.sqrt(2));
        }

        ArrayList<Double> tempWeights = new ArrayList<Double>();
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < allNodes.size(); j++) {
                tempWeights.add(allNodes.get(j).getWeights()[i]);
            }
            
            weights[i] = sugenoFuzzyIntegral(getArrayFromArrayList(tempWeights), getArrayFromArrayList(allNodesDistances));
            tempWeights.clear();
        }

//        System.out.println("============ Fuzzy Weights ===================");
//        for(int i=0;i<weights.length;i++){
//            System.out.println(weights[i]);
//        }
//        System.out.println("==============================================");

        return new GNode(0, 0, weights, null);
    }

    /**
     * 
     * @param values - array of combinations of distances
     * @return combination value
     */
    public static double getCombinationValue(double[] values) {

        if (values.length == 1) {
            return values[0];
        } else {
            double tmp = getCombinationValue(Arrays.copyOfRange(values, 1, values.length));
            return (values[0] + tmp + SUGENO_LAMBDA_MEASURE * values[0] * tmp);
        }
    }

    /**
     * Get the Sugeno Integral Weight value of the  Generalized node
     * @param weights - double weight array of nodes
     * @param distances - double (1 - distances) from the hit node
     * @return weight value of the Generalized node
     */
    public static double sugenoFuzzyIntegral(double[] weights, double[] distances) {

        //sorting weight array with indexes
        int n = weights.length;
        double[] minArray = new double[n];
        double temp;
        int temp2;
        ArrayList<Double> arrayList = new ArrayList<Double>();

        int[] indexArray = new int[n];
        for (int i = 0; i < n; i++) {
            indexArray[i] = i;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (weights[j - 1] < weights[j]) {
                    temp = weights[j - 1];
                    temp2 = indexArray[j - 1];
                    weights[j - 1] = weights[j];
                    indexArray[j - 1] = indexArray[j];
                    weights[j] = temp;
                    indexArray[j] = temp2;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            arrayList.add(distances[indexArray[i]]);
            minArray[i] = Math.min(weights[i], getCombinationValue(getArrayFromArrayList(arrayList)));
        }

        Arrays.sort(minArray);
        return minArray[n - 1];
    }

    /**
     * Get the Choqet Integral Weight value of the  Generalized node
     * @param weights - double weight array of nodes
     * @param distances - double (1 - distances) from the hit node
     * @return weight value of the Generalized node
     */
    public static double choqetFuzzyIntegral(double[] weights, double[] distances) {

        //sorting weight array with indexes
        int n = weights.length;
        double fuzzyValue = 0;
        double temp;
        int temp2;
        ArrayList<Double> arrayList = new ArrayList<Double>();
        double previousValue = 0;
        int[] indexArray = new int[n];

        for (int i = 0; i < n; i++) {
            indexArray[i] = i;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (weights[j - 1] < weights[j]) {
                    temp = weights[j - 1];
                    temp2 = indexArray[j - 1];
                    weights[j - 1] = weights[j];
                    indexArray[j - 1] = indexArray[j];
                    weights[j] = temp;
                    indexArray[j] = temp2;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            arrayList.add(distances[indexArray[i]]);
            double currentValue = getCombinationValue(getArrayFromArrayList(arrayList));
            fuzzyValue += weights[i] * (currentValue - previousValue);
            previousValue = currentValue;
        }

        return fuzzyValue;
    }

    
    /**
     * Convert double ArrayList to a double Array
     * @param a <Double> arrayList 
     * @return double[]
     */
    public static double[] getArrayFromArrayList(ArrayList<Double> a) {

        double[] ret = new double[a.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = a.get(i).doubleValue();
        }
        return ret;
    }
}
