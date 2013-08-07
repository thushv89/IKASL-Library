package com.ikasl.utils;

import java.util.Map;

import com.ikasl.objects.Node;
import java.util.ArrayList;
import java.util.Collections;

public class Utils {

    public static double[] generateRandomArray(int dimensions) {
        double[] arr = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            arr[i] = Math.random();
        }
        return arr;
    }

    public static String generateIndexString(int x, int y) {
        return x + "," + y;
    }

    public static String generateNodeSequence(String prevSeq, int x, int y) {
        if (prevSeq == null || prevSeq.length() == 0) {
            return x + "," + y;
        } else {
            return prevSeq + ":" + x + "," + y;
        }
    }

    public static double getLearningRate(int iter, int nodeCount,double startLR,int maxIter) {
        double minPhi = 0.95;
        //if 3.8 used for a node count < 4 learning rate becomes negative
        if(nodeCount<4){
            return startLR * Math.exp(-(double) iter / maxIter) * (1 - minPhi);
        }else{
            return startLR * Math.exp(-(double) iter / maxIter) * (1 - (3.8 / nodeCount));
        }
    }

    public static double getTimeConst(int maxIter,double maxNeighRad) {
        return (double) maxIter / Math.log(maxNeighRad);
    }

    //get the node with the minimal ED to the input (winner)
    public static Node selectWinner(Map<String, Node> nodeMap, double[] input) {
        Node winner = null;
        double currDist = Double.MAX_VALUE;
        double minDist = Double.MAX_VALUE;
        for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
            currDist = Utils.calcEucDist(input, entry.getValue().getWeights(), IKASLConstants.DIMENSIONS);
            if (currDist < minDist) {
                winner = entry.getValue();
                minDist = currDist;
            }

        }
        return winner;
    }

    public static void adjustNeighbourWeight(Node node, Node winner, double[] input, double radius, double learningRate) {
        double nodeDistSqr = Math.pow(winner.getX() - node.getX(), 2) + Math.pow(winner.getY() - node.getY(), 2);
        double radiusSqr = radius*radius;
        
        //if node is within the radius
        if (nodeDistSqr < radiusSqr) {         
            double influence = Math.exp(-(double)nodeDistSqr / (2 * radiusSqr));
            node.adjustWeights(input, influence, learningRate);  
        }
    }

    public static double getRadius(int iter, double maxRad,double timeConst) {
        return maxRad * Math.exp(-(double) iter / timeConst);
    }

    public static double calcEucDist(double[] in1, double[] in2, int dimensions) {
        double dist = 0.0;
        for (int i = 0; i < dimensions; i++) {
            dist += Math.pow(in1[i] - in2[i], 2);
        }

        return Math.sqrt(dist);
    }
    
    public static int[] getMinMaxMapCoord(Map<String,Node> map){
        ArrayList<Integer> allX= new ArrayList<Integer>();
        ArrayList<Integer> allY= new ArrayList<Integer>();
        int[] result = new int[4];
        for(String s : map.keySet()){
            String[] tokens = s.split(",");
            allX.add(Integer.parseInt(tokens[0]));
            allY.add(Integer.parseInt(tokens[1]));
        }
        
        result[0] = Collections.min(allX);
        result[1] = Collections.min(allY);
        result[2] = Collections.max(allX);
        result[3] = Collections.max(allY);
        
        return result;
    }
    
    public static int getSTDevCountForColsGreaterThan(ArrayList<double[]> inputs,double thresh){
        ArrayList<Double> stdevs = getSTDForColumns(inputs, IKASLConstants.DIMENSIONS);
        int colCount=0;
        for(double val : stdevs){
            if(val>thresh){
                colCount++;
            }
        }
        if(colCount==0){
            colCount=1;
        }
        return colCount;
    }
    
        // calculate the standard deviations for the dataset
    public static ArrayList<Double> getSTDForColumns(ArrayList<double[]> inputs, int dimensions){
        if(inputs.size()<500){
            return calcSTDForColumns(inputs, dimensions);
        }else{
            //TODO: get random sample of 500 items and calc the std
            return calcSTDForColumns((ArrayList<double[]>) inputs.subList(0, 500),dimensions);
        }
    }
    
    private static ArrayList<Double> calcSTDForColumns(ArrayList<double[]> inputs,int dimensions){
    ArrayList<ArrayList<Double>> samplePopl = new ArrayList<ArrayList<Double>>();
            if(samplePopl.size()<=0){
                for(int i=0;i<dimensions;i++){
                    ArrayList<Double> sampleInput = new ArrayList<Double>();
                    sampleInput.add(inputs.get(0)[i]);
                    samplePopl.add(sampleInput);
                }
            }
            
            for(int a=1;a<inputs.size();a++){
                 for(int i=0;i<dimensions;i++){
                     samplePopl.get(i).add(inputs.get(a)[i]);                        
                 }
            }
                        
            ArrayList<Double> poplMean = new ArrayList<Double>();
            for(ArrayList<Double> arr : samplePopl){
                double mean=0;
                for(double val : arr){
                    mean += val;
                }
                mean = mean/arr.size();
                poplMean.add(mean);
            }
            
            ArrayList<Double> poplSTD = new ArrayList<Double>();
            for(int i=0;i<samplePopl.size();i++){
                double std = 0;
                for(double val : samplePopl.get(i)){
                    std += Math.pow(val-poplMean.get(i), 2);
                }
                std = Math.sqrt(std/samplePopl.get(i).size());
                
                poplSTD.add(std);
            }
            return poplSTD;
    }
}
