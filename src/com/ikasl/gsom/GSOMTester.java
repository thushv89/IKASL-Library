package com.ikasl.gsom;

import com.ikasl.objects.Node;
import com.ikasl.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GSOMTester {

    Map<String,String> testResultMap;

    public GSOMTester() {
        testResultMap = new HashMap<String, String>();
    }
    
    public Map<String,Node> testGSOM(Map<String,Node> nodeMap,ArrayList<double[]> iWeights,ArrayList<String> iStrings){
        for(int i = 0; i<iWeights.size();i++){
            
            Node winner = Utils.selectWinner(nodeMap, iWeights.get(i));
            String winnerStr = Utils.generateIndexString(winner.getX(), winner.getY());
            
            Node winnerNode = nodeMap.get(winnerStr);
            winnerNode.setHitValue(winner.getHitValue()+1);
            
            if(!testResultMap.containsKey(winnerStr)){
                testResultMap.put(winnerStr, iStrings.get(i));
            }else{
                String currStr = testResultMap.get(winnerStr);
                String newStr = currStr +","+ iStrings.get(i);
                testResultMap.remove(winnerStr);
                testResultMap.put(winnerStr,newStr);
            }
        }
        return nodeMap;
        
    }
    
    public Map<String,String> getResultMap(){
        return testResultMap;
    }
}
