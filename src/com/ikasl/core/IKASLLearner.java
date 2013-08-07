/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core;

import com.ikasl.enums.LayerType;
import com.ikasl.gsom.GCoordAdjuster;
import com.ikasl.gsom.GSOMSmoothner;
import com.ikasl.gsom.GSOMTester;
import com.ikasl.gsom.GSOMTrainer;
import com.ikasl.objects.GNode;
import com.ikasl.objects.IKASLParams;
import com.ikasl.objects.InputNameWithDisparityCheck;
import com.ikasl.objects.NeuronLayer;
import com.ikasl.objects.Node;
import com.ikasl.utils.EntityIDGenerator;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Thush
 */
public class IKASLLearner {

    
    
    //GSOM Functionality 
    private GSOMTrainer trainer;
    private GCoordAdjuster adjuster;
    private GSOMSmoothner smoothner;
    private GSOMTester tester;
    
    private Map<String, ArrayList<NeuronLayer>> learnLayers;
    private ArrayList<GNode> leftOutGNodes;
    
    //The first layer of IKASL (Learn layer 0) has only 1 GSOM
    //but any learning layer after Learn layer 0 can have multiple GSOMs,
    //as each GNode can grow into an individual GSOM
    private int numOfMaps;  
    
    private IKASLParams params;
    private double disThreshold;    //disparity threshold value

    public IKASLLearner(IKASLParams params) {
        this.params = params;

    }
    //need to generate one map for each node in the Map

    public Map<String, ArrayList<NeuronLayer>> learn(NeuronLayer nLayer, 
            ArrayList<double[]> inputs, ArrayList<String> nameList) {

        initializeGSOMObjects();

        learnLayers = new HashMap<String, ArrayList<NeuronLayer>>();
        leftOutGNodes = new ArrayList<GNode>(); //this arraylist is used to carry non-hit and nodes having only dispersed inputs

        //create an arraylist of all GNodes weights + inputs weights
        ArrayList<double[]> gNodesAndInputWeights = new ArrayList<double[]>();
        //add GNode weights
        if(nLayer!=null){
            for(Node n : nLayer.getNeurons().values()){
                gNodesAndInputWeights.add(n.getWeights());
            }
        }
        //add input weights
        gNodesAndInputWeights.addAll(inputs);
        
        int colCount = Utils.getSTDevCountForColsGreaterThan(gNodesAndInputWeights, IKASLConstants.STD_DEV_THRESHOLD);
        disThreshold = IKASLConstants.getDisparityThreshold(colCount);
        
        //initial time: starting with 4 nodes
        if (nLayer == null) {
            
            //create more generalized nodes
            double sfOriginal = params.getSpreadFactor();
            
            //initial phase having a large spread factor will spread the data more
            double newSF = getSpreadFactorForCycle0(sfOriginal);
            params.setSpreadFactor(newSF);
            
            this.numOfMaps = 1;
            Map<String, Node> map = runGSOMForInputs(params,nameList, inputs);
            
            ArrayList<NeuronLayer> initLayer = new ArrayList<NeuronLayer>();
            initLayer.add(new NeuronLayer(map, LayerType.LEARNING));
            learnLayers.put("", initLayer);

            //set SF to orignal value`
            params.setSpreadFactor(sfOriginal);

        } else if (nLayer.getlType() == LayerType.GENERALIZED) {

            //TODO: Do this other way instead of going through nodes go through inputs
            
            this.numOfMaps = nLayer.getNeurons().size();    //number of individual GSOMs
            
            Map<String, ArrayList<InputNameWithDisparityCheck>> iNamesGNodeMap = new HashMap<String, ArrayList<InputNameWithDisparityCheck>>();

            divideInputsToGNodes(inputs, nameList, nLayer, iNamesGNodeMap, disThreshold);   //divide inputs to GNodes

            for (Map.Entry<String, Node> entry : nLayer.getNeurons().entrySet()) {
                GNode gNode = (GNode) entry.getValue();

                Map<String, Node> mapForGNode1 = new HashMap<String, Node>();   //to keep similar inputs to the node
                Map<String, Node> mapForGNode2 = new HashMap<String, Node>();   //to keep inputs which exceeded the disparity thresh

                ArrayList<double[]> nodeInputList1 = new ArrayList<double[]>(); //inputList for mapForGNode1
                ArrayList<double[]> nodeInputList2 = new ArrayList<double[]>(); //inputList for mapForGNode2

                ArrayList<String> nodeInputNameList1 = new ArrayList<String>(); //inputNameList for mapForGNode1
                ArrayList<String> nodeInputNameList2 = new ArrayList<String>(); //inputNameList for mapForGNode2

                //if the selected node (gNode) has some inputs for it
                if (iNamesGNodeMap.containsKey(gNode.getNodeSequence())) {
                    
                    //for each input belonging to "gNode"
                    for (InputNameWithDisparityCheck iNameObj : iNamesGNodeMap.get(gNode.getNodeSequence())) {
                        int inputIdx = nameList.indexOf(iNameObj.getInputName());   //index of the current input

                        //if current input doesn't exceed the disparity threshold,
                        //put it to "mapForGNode1"
                        if (!iNameObj.isDisparityExceeded()) {
                            mapForGNode1.put(Utils.generateIndexString(0, 0), gNode);
                            nodeInputList1.add(inputs.get(inputIdx));
                            nodeInputNameList1.add(iNameObj.getInputName());
                        } 
                        //if current input exceeds the disparity threshold,
                        //put it to "mapForGNode2"
                        else {
                            mapForGNode2.put(Utils.generateIndexString(0, 0), gNode);
                            nodeInputList2.add(inputs.get(inputIdx));
                            nodeInputNameList2.add(iNameObj.getInputName());
                        }
                    }
                }
                
                ArrayList<NeuronLayer> lmapsForCurrCycle = new ArrayList<NeuronLayer>();
                //train for the inputs which did not exceed disparity thresh
                if (mapForGNode1.size() > 0) {

                    Map<String, Node> map = trainer.trainNetwork(params, mapForGNode1, nodeInputNameList1, nodeInputList1);
                    
                    //this block is to avoid the problem - GNodes with same coordinates can appear in different levels
                    //Ex. Let's say there's a gnode 0,0 in G1
                    //Then from 0,0 spawns 0,0:0,1 & 0,0:1,2 Gnodes in G2
                    //But if gNode 0,0 exceeded disparity threshold, 0,0:0,1 can appear again in G3
                    //to solve this issue each time a node with high dis. thresh. grows into learn layer,
                    //we adjust the coordinates of the map with an offset                      
                    if (gNode.getMin_X() >= 0 || gNode.getMin_Y() >= 0) {
                        //adjust coords of map
                        map = adjuster.adjustMapCoords(map, gNode.getMin_X() + 1, gNode.getMin_Y() + 1);
                    }

                    int[] minMax = Utils.getMinMaxMapCoord(map);    //get the min/max coordinates of the GSOM
                    gNode.setMin_X(minMax[2]);
                    gNode.setMin_Y(minMax[3]);

                    map = smoothner.smoothGSOM(params, map, nodeInputList1);    //smoothing phase
                    map = tester.testGSOM(map, nodeInputList1, nodeInputNameList1); //testing phase

                    if (learnLayers.containsKey(gNode.getNodeSequence())) {
                        System.out.println("Warning! learn layer with parent" + gNode.getNodeSequence() + " is being overwritten");
                    }
                    
                    lmapsForCurrCycle.add(new NeuronLayer(map,LayerType.LEARNING));

                } //train for the inputs exceeded disparity thresh
                if (mapForGNode2.size() > 0) {
                    Map<String, Node> map = trainer.trainNetwork(params, mapForGNode2, nodeInputNameList2, nodeInputList2);

                    if (gNode.getMin_X() >= 0 || gNode.getMin_Y() >= 0) {
                        //adjust coords of map
                        map = adjuster.adjustMapCoords(map, gNode.getMin_X() + 1, gNode.getMin_Y() + 1);
                    }
                    int[] minMax = Utils.getMinMaxMapCoord(map);
                    gNode.setMin_X(minMax[2]);
                    gNode.setMin_Y(minMax[3]);

                    map = smoothner.smoothGSOM(params, map, nodeInputList2);    //smoothing phase
                    map = tester.testGSOM(map, nodeInputList2, nodeInputNameList2); //testing phase

                    if (learnLayers.containsKey(gNode.getNodeSequence())) {
                        System.out.println("Warning! learn layer with parent " + gNode.getNodeSequence() + " is being overwritten");
                    }

                    lmapsForCurrCycle.add(new NeuronLayer(map, LayerType.LEARNING));
                } 
                
                if(lmapsForCurrCycle.size()>0){
                    learnLayers.put(gNode.getNodeSequence(), lmapsForCurrCycle);
                }else{
                    System.out.println("No learning layers for the current Cycle");
                    //TODO: Do learning with a lower spread factor
                }
                
                //There's no hit for this gNode
                if(mapForGNode1.isEmpty() && mapForGNode2.isEmpty()) {
                    //TODO: see if we need to send a copy of gNode to leftOutGNodes array
                    leftOutGNodes.add(gNode);
                }
            }
        } else {
            System.out.println("Error! A Learning layer was sent to learn. Only generalized layers can learn.");
        }

        return learnLayers;
    }

    private Map<String, Node> runGSOMForInputs(IKASLParams params, ArrayList<String> nameList, ArrayList<double[]> inputs) {
        Map<String, Node> map =trainer.trainNetwork(params, nameList, inputs);
        map = smoothner.smoothGSOM(params, map, inputs);
        map = tester.testGSOM(map, inputs, nameList);
        return map;
    }

    /**
     * Initialize GSOM related classes to be used in learning phase. 
     * Namely they are,
     * 1. GSOMTrainer - Train the network
     * 2. GCoordAduster - Adjust network coordinates in case they are negative
     * 3. GSOMSmoothner - Smooth the neural network
     * 4. GSOMTester - Test the GSOM network by feeding the input entries
     */
    private void initializeGSOMObjects() {
        trainer = new GSOMTrainer();
        adjuster = new GCoordAdjuster();
        smoothner = new GSOMSmoothner();
        tester = new GSOMTester();
    }

    private double getSpreadFactorForCycle0(double sfOriginal){
        double std = 0.2;
        return sfOriginal*(1+(1.2/(std*Math.sqrt(2*Math.PI)))*Math.exp(-Math.pow((sfOriginal/std), 2)));
    }
    
    //left out nodes are, gen nodes that are not hit 
    //and nodes which gets very different input
    public ArrayList<GNode> getLeftOutGenNodes() {
        return leftOutGNodes;
    }

    public ArrayList<Node> getCloneLeftOutGenNodes() {
        return new ArrayList<Node>(leftOutGNodes);
    }

    /*=========== OBSOLETE =====================================================
    private int getAvgNumOfDiffElementValue(ArrayList<double[]> inputs) {
        double threshold = 0.125;
        double[] total = new double[IKASLConstants.DIMENSIONS];
        for (double[] val : inputs) {
            for (int i = 0; i < IKASLConstants.DIMENSIONS; i++) {
                total[i] += val[i];
            }
        }
        for (int i = 0; i < IKASLConstants.DIMENSIONS; i++) {
            total[i] = total[i] / inputs.size();
        }
        int result = 0;
        for (int i = 0; i < IKASLConstants.DIMENSIONS; i++) {
            if (total[i] > threshold) {
                result++;
            }
        }
        if (result == 0) {
            return 1;
        } else {
            return result;
        }
    }*/
    
    /*
     * Other than the 1st input sequence, all the other input sequences are fed
     * to a generalized layer. And using new inputs the generalized layer grows the next
     * learning layer. Therefore, inputs in the input sequence needs to be divided to each node.
     * 
     */
    private void divideInputsToGNodes(ArrayList<double[]> inputs, ArrayList<String> nameList,
            NeuronLayer nLayer, Map<String, ArrayList<InputNameWithDisparityCheck>> iNamesGNodeMap, double disThreshold) {

        Map<String, Boolean> nodeToNoSimilarInputsMap = new HashMap<String, Boolean>();

        for (String key : nLayer.getNeurons().keySet()) {
            nodeToNoSimilarInputsMap.put(key, Boolean.TRUE);
        }
        /*
         * for(Map.Entry<String,Node> entry : nLayer.getNeurons().entrySet()){
         * ((GNode)entry.getValue()).setDisparityThreshExceeded(false); }
         */

        //TODO: Add a better disparity checker. 
        //Calc the average disparity of dataset rather than doing it when 1 different node is present. 

        for (int i = 0; i < inputs.size(); i++) {
            InputNameWithDisparityCheck inputObj = new InputNameWithDisparityCheck(nameList.get(i), false);
            GNode winner = (GNode) Utils.selectWinner(nLayer.getNeurons(), inputs.get(i));

            //winner.calcAndUpdateErr(inputs.get(i)); //=================== Error value updation is not required

            //check whether this node is lot different from the new input
            //if so, keep the parent node                   
            if (!leftOutGNodes.contains(winner)
                    && Utils.calcEucDist(inputs.get(i), winner.getWeights(), IKASLConstants.DIMENSIONS) > disThreshold) {
                //winner.setDisparityThreshExceeded(true);
                inputObj.setDisparityExceeded(true);
                leftOutGNodes.add(winner);
                System.out.println("Node " + winner.getNodeSequence()
                        + " is dispersed lot from input " + nameList.get(i) + ". Added as gNode");
            }

            if (leftOutGNodes.contains(winner)
                    && Utils.calcEucDist(inputs.get(i), winner.getWeights(), IKASLConstants.DIMENSIONS) < disThreshold) {
                nodeToNoSimilarInputsMap.put(winner.getNodeSequence(), Boolean.FALSE);
            }

            if (iNamesGNodeMap.get(winner.getNodeSequence()) == null) {
                ArrayList<InputNameWithDisparityCheck> tempArr = new ArrayList<InputNameWithDisparityCheck>();
                tempArr.add(inputObj);
                iNamesGNodeMap.put(winner.getNodeSequence(), tempArr);

            } else {
                ArrayList<InputNameWithDisparityCheck> tempStr = iNamesGNodeMap.get(winner.getNodeSequence());
                tempStr.add(inputObj);
                iNamesGNodeMap.put(winner.getNodeSequence(), tempStr);
            }

        }

        for (Map.Entry<String, Boolean> entry : nodeToNoSimilarInputsMap.entrySet()) {
            if (entry.getValue() == Boolean.FALSE) {
                leftOutGNodes.remove((GNode) nLayer.getNeurons().get(entry.getKey()));
                System.out.println(entry.getKey()+" has some similar intpus to it. Deleting from left out nodes");
            }
        }
    }

    /**
     * @return the disThreshold
     */
    public double getDisparityThreshold() {
        return disThreshold;
    }
}
