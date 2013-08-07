/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

import com.ikasl.id.EntityID;
import com.ikasl.id.IKASL_ID;


/**
 *
 * @author Thush
 */
public class EntityIDGenerator {
    
    public static final String DELIMITER = ",";
    private static final String levelPrefix = "L";
    private static final String featurePrefix = "F";
    private static final String learnCyclePrefix = "LC";
    private static final String gClusterPrefix = "GC";
    private static final String gNodePrefix = "GN";
    
    public static String generateEntityIDString(EntityID id){
        if(id.getLearnCycleID()==-1){
            return null;
        }else if(id.getgClusterID()==-1){
            return  learnCyclePrefix+id.getLearnCycleID();
        }else if(id.getgNodeID()==-1){
            return learnCyclePrefix+id.getLearnCycleID()+DELIMITER+
                    gClusterPrefix+id.getgClusterID();
        }else {
            return learnCyclePrefix+id.getLearnCycleID()+
                    DELIMITER+gClusterPrefix+id.getgClusterID()+
                    DELIMITER+gNodePrefix+id.getgNodeID();
        }
    }
    
    public static EntityID generateEntityID(String id){
        EntityID eID = new EntityID();
        String[] tokens = id.split(DELIMITER);
        for(int i=0;i<tokens.length;i++){
            if(tokens[i].startsWith(learnCyclePrefix)){
                eID.setLearnCycleID(Integer.parseInt(tokens[i].substring(2)));
            }else if(tokens[i].startsWith(gClusterPrefix)){
                eID.setgClusterID(Integer.parseInt(tokens[i].substring(2)));
            }else if(tokens[i].startsWith(gNodePrefix)){
                eID.setgNodeID(Integer.parseInt(tokens[i].substring(2)));
            }
            
        }
        return eID;
    }
    
    public static String generateIKASL_IDString(IKASL_ID id){
        if(id.getLevelID()==-1){
            return null;
        }else if(id.getFeatureID()==-1){
            return  levelPrefix+id.getLevelID();
        }else {
            return levelPrefix+id.getLevelID()+
                    DELIMITER+featurePrefix+id.getFeatureID();
        }
    }
    
    public static IKASL_ID generateIKASL_ID(String id){
        IKASL_ID iID = new IKASL_ID(null);
        String[] tokens = id.split(DELIMITER);
        for(int i=0;i<tokens.length;i++){
            if(tokens[i].startsWith(levelPrefix)){
                iID.setLevelID(Integer.parseInt(tokens[i].substring(1)));
            }else if(tokens[i].startsWith(featurePrefix)){
                iID.setFeatureID(Integer.parseInt(tokens[i].substring(1)));
            }
        }
        return iID;
    }
    
}
