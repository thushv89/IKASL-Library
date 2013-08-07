/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core;

import com.ikasl.id.EntityID;
import com.ikasl.objects.GNode;
import com.ikasl.objects.NeuronLayer;
import com.ikasl.objects.Node;
import com.ikasl.utils.ArrayHelper;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.Utils;
import com.ikasl.xml.objects.GNodeHitValueData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class GeneralizeLayerTester {

    private GNodeHitValueData ghData;
    private ArrayList<String> specialData;  //keeps track of non-hit data
    
    
    public GeneralizeLayerTester() {
    }

    public Map<String, String> testGenLayer(int cycle, NeuronLayer layer, ArrayList<double[]> inputs, ArrayList<String> names) {
        ghData = new GNodeHitValueData();
        specialData = new ArrayList<String>();
        String nonHitData = "";  //inputs that are not belonging to any node
        
        int colCount = Utils.getSTDevCountForColsGreaterThan(inputs, IKASLConstants.STD_DEV_THRESHOLD);
        Map<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < inputs.size(); i++) {
            GNode winner = (GNode)Utils.selectWinner(layer.getNeurons(), inputs.get(i));
            
            
            //Do not put the input to the test result map if the disparity threshold is high
            if (Utils.calcEucDist(winner.getWeights(), inputs.get(i), IKASLConstants.DIMENSIONS) <
                    IKASLConstants.getDisparityThreshold(colCount)) {                
                
                if (!map.containsKey(winner.getNodeSequence())) {
                    map.put(winner.getNodeSequence(), names.get(i));
                    
                    //for xml writing LC<x>.xml file
                    ArrayList<String> iNamesList = new ArrayList<String>();
                    iNamesList.add(names.get(i));
                    ghData.addKVP(winner.getId(), iNamesList);
                } else {
                    String currStr = map.get(winner.getNodeSequence());
                    currStr += "," + names.get(i);
                    map.put(winner.getNodeSequence(), currStr);
                    
                    //for xml writing LC<x>.xml file
                    ArrayList<String> currList = ghData.getData().get(winner.getId());
                    currList.add(names.get(i));
                    ghData.addKVP(winner.getId(), currList);
                }
            }
            else{
                nonHitData += names.get(i)+",";
                specialData.add(names.get(i));
                
            }
        }
        printValues(cycle, map, nonHitData);
        
        return map;
    }
    
    //===================== FOR TESTING ================================//
    public void printValues(int cycle,Map<String,String> map,String nonHitData){
        System.out.println("\n============================ Cycle "+cycle+ "======================================");
        for(Map.Entry<String,String> entry : map.entrySet()){
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
        System.out.println("Non-hit nodes: "+nonHitData);
        System.out.println("\n===================================================================================\n");
    }
    //=================================================================//
    
    public GNodeHitValueData getGNodeHitValueData(){
        return ghData;
    }
    
    public ArrayList<String> getSpecialData(){
        return specialData;
    }
    
    private int getAvgNumOfDiffElementValue(double[] input,double[] winner){
        
        double[] total = ArrayHelper.add(input, winner, IKASLConstants.DIMENSIONS);
        
        int result=0;
        for(int i=0;i<IKASLConstants.DIMENSIONS;i++){
            if(total[i]>0){
                result++;
            }
        }
        if(result==0){return 1;}
        else{return result;}
    }
    
    private int getAvgNumOfDiffElementValue(ArrayList<double[]> inputs){
        double[] total = new double[IKASLConstants.DIMENSIONS];
        for(double[] val : inputs){
            for(int i=0;i<IKASLConstants.DIMENSIONS;i++){
                total[i]+=val[i];
            }
        }
        int result=0;
        for(int i=0;i<IKASLConstants.DIMENSIONS;i++){
            if(total[i]>0){
                result++;
            }
        }
        if(result==0){return 1;}
        else{return result;}
    }
}
