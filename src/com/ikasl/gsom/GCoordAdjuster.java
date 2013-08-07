package com.ikasl.gsom;

import java.util.HashMap;
import java.util.Map;

import com.ikasl.objects.Node;
import com.ikasl.utils.Utils;

public class GCoordAdjuster {

	public Map<String,Node> adjustMapCoords(Map<String,Node> map,int offsetX,int offsetY){
		Map<String,Node> newMap = new HashMap<String,Node>();
		
		int minX=0;
		int minY=0;
		//find minimum x,y coordinates in the map
		for(Node node: map.values()){
			if(node.getX()<minX){
				minX = node.getX();
			}
			if(node.getY()<minY){
				minY = node.getY();
			}
		}
                
		//adjust node coordinates by adding the offset x,y to all nodes
                //using map.values here is OK because we're discarding the old map anyway
		for(Node node: map.values()){
			node.setX(node.getX()+(-minX)+offsetX);
			node.setY(node.getY()+(-minY)+offsetY);
			newMap.put(Utils.generateIndexString(node.getX(), node.getY()), node);
		}
		//discard the previous map
		
		return newMap;
	}
}
