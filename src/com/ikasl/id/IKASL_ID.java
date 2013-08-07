/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.id;

/**
 *
 * @author Thush
 */
//used to identify a specific IKASL Stack
public class IKASL_ID {
    
    private int levelID;
    private int featureID;
    private IKASL_ID parentID;

    public IKASL_ID(IKASL_ID parent){
        levelID = -1;
        featureID = -1;
        parentID = parent;
    }
    public IKASL_ID(int levelID,int featureID,IKASL_ID parent){
        this.levelID = levelID;
        this.featureID = featureID;
        this.parentID = parent;
    }
    
    /**
     * @return the levelID
     */
    public int getLevelID() {
        return levelID;
    }

    /**
     * @param levelID the levelID to set
     */
    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    /**
     * @return the featureID
     */
    public int getFeatureID() {
        return featureID;
    }

    /**
     * @param featureID the featureID to set
     */
    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    /**
     * @return the parentIDStr
     */
    public IKASL_ID getParentIDStr() {
        return parentID;
    }

    /**
     * @param parentIDStr the parentIDStr to set
     */
    public void setParentIDStr(IKASL_ID parentID) {
        this.parentID = parentID;
    }
    
    
}
