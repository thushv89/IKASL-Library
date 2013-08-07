package com.ikasl.gsom;


import com.ikasl.objects.IKASLParams;
import com.ikasl.objects.Node;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.Utils;
import java.util.ArrayList;
import java.util.Map;



public class GSOMSmoothner{

	private int maxIter;
	private Map<String, Node> map;
	
        /* ---------- Deprecated --------------------------------------------
	public Map<String,Node> smoothGSOM(IKASLParams params,Map<String, Node> map,ArrayList<double[]> inputs){
		for(int iter=0;iter<params.getMaxIte();iter++){
			double learningRate = Utils.getLearningRate(iter, map.size(),params.getStartLearningRate(),maxIter);
                    double radius = Utils.getRadius(iter,params.getMaxNeighborhoodRadius(), Utils.getTimeConst(maxIter,params.getMaxNeighborhoodRadius()));
			for(double[] singleInput : inputs){
				if(singleInput.length==params.getDimensions()){
					smoothSingleIterSingleInput(map,iter,singleInput,learningRate,radius);
				}else{
					//error
				}
			}
		}
                return map;
	}*/
        
        public Map<String, Node> smoothGSOM(IKASLParams params, Map<String, Node> map, ArrayList<double[]> inputs) {
        double startLearningRate = params.getStartLearningRate() / 2;
        double maxRadius = params.getMaxNeighborhoodRadius() / 2;
        for (int iter = 0; iter < params.getMaxIte(); iter++) {
            double learningRate = Utils.getLearningRate(iter, map.size(),startLearningRate,params.getMaxIte());
            double radius = Utils.getRadius(iter, maxRadius, Utils.getTimeConst(params.getMaxIte(),maxRadius));
            for (double[] singleInput : inputs) {
                if (singleInput.length == IKASLConstants.DIMENSIONS) {
                    smoothSingleIterSingleInput(map, iter, singleInput, learningRate, radius);
                } else {
                    //error
                }
            }
        }
        return map;
    }

	private void smoothSingleIterSingleInput(Map<String, Node> map, int iter, double[] input,double learningRate,double radius) {
		Node winner = Utils.selectWinner(map, input);
		
		String leftNode = Utils.generateIndexString(winner.getX()-1, winner.getY());
		String rightNode = Utils.generateIndexString(winner.getX()+1, winner.getY());
		String topNode = Utils.generateIndexString(winner.getX(), winner.getY()+1);
		String bottomNode = Utils.generateIndexString(winner.getX(), winner.getY()-1);
		
		if(map.containsKey(leftNode)){
			Utils.adjustNeighbourWeight(map.get(leftNode), winner, input, radius, learningRate);
		}else if(map.containsKey(rightNode)){
			Utils.adjustNeighbourWeight(map.get(rightNode), winner, input, radius, learningRate);
		}else if(map.containsKey(topNode)){
			Utils.adjustNeighbourWeight(map.get(topNode), winner, input, radius, learningRate);
		} else if(map.containsKey(bottomNode)){
			Utils.adjustNeighbourWeight(map.get(bottomNode), winner, input, radius, learningRate);
		}
	}
}
