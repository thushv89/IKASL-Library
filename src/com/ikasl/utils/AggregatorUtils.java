/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

import com.ikasl.objects.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class AggregatorUtils {

    public static ArrayList<Node> getHitNodeList(Map<String, Node> map) {
        ArrayList<Node> hitList = new ArrayList<Node>();

        for (Map.Entry<String, Node> entry : map.entrySet()) {
            if (entry.getValue().getHitValue() > 0) {
                hitList.add(entry.getValue());
            }
        }
        return hitList;
    }

    public static ArrayList<Node> getHitNodeList(Map<String, Node> map, int hitThresh) {
        ArrayList<Node> hitList = new ArrayList<Node>();

        for (Map.Entry<String, Node> entry : map.entrySet()) {
            if (entry.getValue().getHitValue() > hitThresh) {
                hitList.add(entry.getValue());
            }
        }
        return hitList;
    }

    //return all the neighbors 1step away from the primnode
    public static ArrayList<Node> get1stLvlNeighbors(Map<String, Node> map, Node primNode) {
        int X = primNode.getX();
        int Y = primNode.getY();
        
        String lStr = Utils.generateIndexString(X-1, Y);
        String rStr = Utils.generateIndexString(X+1, Y);
        String bStr = Utils.generateIndexString(X, Y-1);
        String tStr = Utils.generateIndexString(X, Y+1);
        String lbStr = Utils.generateIndexString(X-1, Y-1);
        String rbStr = Utils.generateIndexString(X+1, Y-1);
        String rtStr = Utils.generateIndexString(X+1, Y+1);
        String ltStr = Utils.generateIndexString(X-1, Y+1);
        String[] neighbors = {lStr,rStr,bStr,tStr,lbStr,rbStr,rtStr,ltStr};
        ArrayList<Node> neigh1 = new ArrayList<Node>();
        
        for(String neighbor : neighbors){
            if(map.containsKey(neighbor)){
                neigh1.add(map.get(neighbor));
            }
        }
        return neigh1;
    }

    //return all the neighbors 2 steps away from the primNode
    public static ArrayList<Node> get2ndLvlNeighbors(Map<String, Node> map, Node primNode) {
        int X = primNode.getX();
        int Y = primNode.getY();
        String llbbStr = X-2 +","+ (Y-2);
        String lbbStr = X-1 + "," + (Y-2);
        String bbStr = X + "," + (Y-2);
        String rbbStr = X+1 + "," + (Y-2);
        String rrbbStr = X+2 + "," + (Y-2);
        String llbStr = X-2 + "," + (Y-1);
        String rrbStr = X+2 + "," + (Y-1);
        String rrStr = primNode.getX()+2 +","+primNode.getY();
        String llStr = X-2 + "," + Y;
        String lltStr = X-2 + "," + (Y+1);
        String rrtStr = X+2 + "," + (Y+1);
        String llttStr = X-2 +","+ (Y+2);
        String lttStr = X-1 + "," + (Y+2);
        String ttStr = X + "," + (Y+2);
        String rttStr = X+1 + "," + (Y+2);
        String rrttStr = X+2 + "," + (Y+2);
        
        String[] neighbors2Str = {llbbStr,lbbStr,bbStr,rbbStr,rrbbStr,llbStr,rrbStr,rrStr,llStr,lltStr,rrtStr,llttStr,lttStr,ttStr,rttStr,rrttStr};
        
        ArrayList<Node> neigh2 = new ArrayList<Node>();
        for(String neigh2Str : neighbors2Str){
            if(map.containsKey(neigh2Str)){
                neigh2.add(map.get(neigh2Str));
            }
        }
        
        return neigh2;
    }

    //get the highest hit neuron
    private static ArrayList<Node> getHighestHitNeuron(Map<String, Node> map) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        nodeList.addAll(map.values());
        ArrayList<Node> highestHitNueron = new ArrayList<Node>();

        Collections.sort(nodeList, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getHitValue() > o2.getHitValue()) {
                    return 1;
                } else if (o2.getHitValue() > o1.getHitValue()) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });


        highestHitNueron.add(nodeList.get(0));


        return highestHitNueron;
    }
}
