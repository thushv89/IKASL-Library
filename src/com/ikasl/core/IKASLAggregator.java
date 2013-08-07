/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core;

import com.ikasl.core.aggregate.AggregatorFactory;
import com.ikasl.core.aggregate.IKASLGeneralizer;
import com.ikasl.enums.AggregatorType;
import com.ikasl.enums.LayerType;
import com.ikasl.id.EntityID;
import com.ikasl.objects.GNode;
import com.ikasl.objects.IKASLParams;
import com.ikasl.objects.NeuronLayer;
import com.ikasl.objects.Node;
import com.ikasl.utils.AggregatorUtils;
import com.ikasl.utils.EntityIDGenerator;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class IKASLAggregator {

    private AggregatorFactory aggFactory;
    private IKASLGeneralizer aggregator;
    private IKASLParams params;
    private int gNodeCount;
    
    public IKASLAggregator(IKASLParams params) {
        this.params = params;
        aggFactory = new AggregatorFactory();
        if(params.getAggregationType()==0){
            aggregator = aggFactory.getAggregator(AggregatorType.FUZZY);
        }
        else{
            aggregator = aggFactory.getAggregator(AggregatorType.AVERAGE);
        }
    }

    public NeuronLayer aggregate(int currLearningCycle, Map<String, ArrayList<NeuronLayer>> learnLayer, int hitThresh, double disThresh) {

        gNodeCount = 0;
        
        NeuronLayer genNodes = new NeuronLayer();
        genNodes.setlType(LayerType.GENERALIZED);
        
        //run aggregation for each neuron layer
        for (Map.Entry<String, ArrayList<NeuronLayer>> entry : learnLayer.entrySet()) {
            String parentIDStr = "";
            //find the parent node of the learn layer
            for(NeuronLayer nl : entry.getValue()){
                if(nl.getNeurons().size()>0){
                    ArrayList<Node> nodes = new ArrayList<Node>(nl.getNeurons().values());
                    parentIDStr = nodes.get(0).getParentID();
                    break;
                }
            }
            
            //get each neuron of the neuron layers
            for (NeuronLayer nl : entry.getValue()) {
                ArrayList<Node> hitNodes = AggregatorUtils.getHitNodeList(nl.getNeurons(), hitThresh);
                
                //if current hit threshold doesn't give any hit nodes, 
                //iterate till hitNodes has atleast 1 hit node
                for(int thresh=hitThresh;thresh>=0;thresh--){
                    hitNodes = removeRedundantHits(nl.getNeurons(), hitNodes);
                    if(!hitNodes.isEmpty()){
                        break;
                    }
                }
                
                if(hitNodes.isEmpty()){
                    System.out.println("Warning! Map born from "+entry.getKey()+" has no node with hit value > "+ hitThresh+". Map will be forgotten");
                }
                for (Node hit : hitNodes) {

                    //add 1st and 2nd level neighbors
                    ArrayList<Node> neigh1 = AggregatorUtils.get1stLvlNeighbors(nl.getNeurons(), hit);
                    ArrayList<Node> neigh2 = AggregatorUtils.get2ndLvlNeighbors(nl.getNeurons(), hit);

                    GNode aggNode = aggregator.generalize(hit, neigh1, neigh2, disThresh, params.getDimensions());
                    
                    //Setting the ID for Node
                    aggNode.setId(new EntityID(currLearningCycle,0,0,gNodeCount));
                    aggNode.setParentID(parentIDStr);
                    
                    String newNodeKey = Utils.generateNodeSequence(entry.getKey(), hit.getX(), hit.getY());
                    aggNode.setNodeSequence(newNodeKey);
                    
                    System.out.println("ID for GNode: "+EntityIDGenerator.generateEntityIDString(aggNode.getId())+
                            "("+aggNode.getNodeSequence()+")"+
                            " Parent ID: "+parentIDStr);
                    //Now parentNode has been replaced by parentID
                    //aggNode.setParentNode(hit);
                    
                    genNodes.addNeuron(aggNode, newNodeKey);
                    
                    gNodeCount++;
                }
            }
        }
        
        
        return genNodes;

    }

    //If one hit node is in the other's neighborhood, we ignore that node
    private ArrayList<Node> removeRedundantHits(Map<String, Node> map, ArrayList<Node> hitNodes) {
        ArrayList<Node> refinedArr = new ArrayList<Node>();
        Collections.sort(hitNodes, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return o1.getHitValue() - o2.getHitValue();
            }
        });

        for (int i = hitNodes.size() - 1; i >= 0; i--) {
            Node n = hitNodes.get(i);
            if (n.getHitValue() > 0) {
                ArrayList<Node> neigh1 = AggregatorUtils.get1stLvlNeighbors(map, n);
                //ArrayList<Node> neigh2 = AggregatorUtils.get2ndLvlNeighbors(map, n);
                for (Node temp : hitNodes) {

                    //Ignore the node if it has a 1st level neighbor AND
                    //if error value per hit is less than disparity threshold
                    if (neigh1.contains(temp)) {
                        temp.setHitValue(0);
                    }
                }
            }
        }

        for (Node n : hitNodes) {
            if (n.getHitValue() > 0) {
                refinedArr.add(n);
            }
        }

        return refinedArr;
    }
}
