package com.ikasl.gsom;

import com.ikasl.objects.GNode;
import com.ikasl.objects.IKASLParams;
import com.ikasl.objects.LNode;
import com.ikasl.objects.Node;
import com.ikasl.utils.ArrayHelper;
import com.ikasl.utils.EntityIDGenerator;
import com.ikasl.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GSOMTrainer {

    private Map<String, Node> nodeMap;
    private NodeGrowthHandler growthHandler;
    private int maxIter;
    private int dims;
    private double GT;
    private double FD;
    private double startLR;
    private double maxNR;

    public GSOMTrainer() {
        
        growthHandler = new NodeGrowthHandler();
    }

    public Map<String, Node> trainNetwork(IKASLParams params, ArrayList<String> iStrings, ArrayList<double[]> iWeights) {
        nodeMap = new HashMap<String, Node>();
        
        maxIter = params.getMaxIte();
        dims = params.getDimensions();
        GT = params.getGT();
        FD = params.getFD();
        startLR = params.getStartLearningRate();
        maxNR = params.getMaxNeighborhoodRadius();

        initFourNodes();	//init the map with four nodes
        for (int i = 0; i < maxIter; i++) {
            int k = 0;
            double learningRate = Utils.getLearningRate(i, nodeMap.size(), startLR, maxIter);
            double radius = Utils.getRadius(i, maxNR, Utils.getTimeConst(maxIter, maxNR));
            for (double[] input : iWeights) {
                trainForSingleIterAndSingleInput(i, input, iStrings.get(k), learningRate, radius);
                k++;
            }
        }
        return nodeMap;
    }

    public Map<String, Node> trainNetwork(IKASLParams params, Map<String, Node> map, ArrayList<String> iStrings, ArrayList<double[]> iWeights) {
        
        nodeMap = new HashMap<String, Node>();
        
        if(map == null || map.size()<=0){
            return null;
        }
        for(Node n : map.values()){
            if(!(n instanceof GNode)){
                return null;
            }
        }
        
        maxIter = params.getMaxIte();
        dims = params.getDimensions();
        GT = params.getGT();
        FD = params.getFD();
        startLR = params.getStartLearningRate();
        maxNR = params.getMaxNeighborhoodRadius();

        //TODO : Convert GNOde to LNode
        for(Node n : map.values()){
            LNode initNode = new LNode(n.getX(), n.getY(), n.getWeights());
            initNode.setParentID(EntityIDGenerator.generateEntityIDString(((GNode)n).getId()));
            this.nodeMap.put(Utils.generateIndexString(initNode.getX(), initNode.getY()), initNode);
        }
        
        //======================================================================
        //Having putAll(map) is giving troubles
        //just assign the map seems to solve the problem
        //Cause : If using putAll(map) make sure you clear the nodemap before putting nodes
        //======================================================================
        
        
        for (int i = 0; i < maxIter; i++) {
            int k = 0;
            double learningRate = Utils.getLearningRate(i, this.nodeMap.size(), startLR, maxIter);

            double radius = Utils.getRadius(i, maxNR, Utils.getTimeConst(maxIter, maxNR));
            for (double[] input : iWeights) {
                trainForSingleIterAndSingleInput(i, input, iStrings.get(k), learningRate, radius);
                k++;
            }
        }
        
        return this.nodeMap;
    }

    private void trainForSingleIterAndSingleInput(int iter, double[] input, String str, double learningRate, double radius) {

        LNode winner = (LNode)Utils.selectWinner(this.nodeMap, input);
        winner.calcAndUpdateErr(input);

        for (Map.Entry<String, Node> entry : this.nodeMap.entrySet()) {
            Utils.adjustNeighbourWeight(entry.getValue(), winner, input, radius, learningRate);
        }

        if (winner.getErrorValue() > GT) {
            adjustWinnerError(winner);
        }
    }

    //Initialization of the map. 
    //this will create 4 nodes with random weights
    private void initFourNodes() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                LNode initNode = new LNode(i, j, Utils.generateRandomArray(dims));
                this.nodeMap.put(Utils.generateIndexString(i, j), initNode);
            }
        }
    }

    //when a neuron wins its error value needs to be adjusted
    private void adjustWinnerError(LNode winner) {

        //on x-axis
        String nodeLeftStr = Utils.generateIndexString(winner.getX() - 1, winner.getY());
        String nodeRightStr = Utils.generateIndexString(winner.getX() + 1, winner.getY());

        //on y-axis
        String nodeTopStr = Utils.generateIndexString(winner.getX(), winner.getY() + 1);
        String nodeBottomStr = Utils.generateIndexString(winner.getX(), winner.getY());

        if (this.nodeMap.containsKey(nodeLeftStr)
                && this.nodeMap.containsKey(nodeRightStr)
                && this.nodeMap.containsKey(nodeTopStr)
                && this.nodeMap.containsKey(nodeBottomStr)) {
            distrErrToNeighbors(winner, nodeLeftStr, nodeRightStr, nodeTopStr, nodeBottomStr);
        } else {
            this.nodeMap = growthHandler.growNodes(this.nodeMap, winner); //NodeGrowthHandler takes over
        }
    }

    //When the node growing is complete this event fill be fired
    //distributing error to the neighbors of thw winning node
    private void distrErrToNeighbors(LNode winner, String leftK, String rightK, String topK, String bottomK) {
        winner.setErrorValue(GT / 2);
        LNode leftNode = (LNode) this.nodeMap.get(leftK);
        LNode rightNode = (LNode) this.nodeMap.get(rightK);
        LNode bottomNode = (LNode) this.nodeMap.get(bottomK);
        LNode topNode = (LNode) this.nodeMap.get(topK);
                
        leftNode.setErrorValue(calcErrForNeighbour(leftNode));
        rightNode.setErrorValue(calcErrForNeighbour(rightNode));
        topNode.setErrorValue(calcErrForNeighbour(topNode));
        bottomNode.setErrorValue(calcErrForNeighbour(bottomNode));
    }

    //error calculating equation for neighbours of a winner
    private double calcErrForNeighbour(LNode node) {
        return node.getErrorValue() + (FD * node.getErrorValue());
    }
}
