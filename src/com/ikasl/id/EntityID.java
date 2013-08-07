/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.id;

import com.sun.org.apache.xpath.internal.operations.Equals;
import java.io.Serializable;

/**
 *
 * @author Thush
 */
//Entity ID is used to identify a GNode within a specific IKASL Stack
//IKASL Stack is identified by IKASL_ID
public class EntityID implements Serializable{
    
    private int learnCycleID;
    private int gClusterID;
    private int gNodeID;

    public EntityID(){
        this.learnCycleID = -1;
        this.gClusterID = -1;
        this.gNodeID = -1;
    }
    
    public EntityID(int learnCycleID,
            int gClusterID,int sClusterID, int gNodeID){
        this.learnCycleID = learnCycleID;
        this.gClusterID = gClusterID;
        this.gNodeID = gNodeID;
    }

    /**
     * @return the learnCycleID
     */
    public int getLearnCycleID() {
        return learnCycleID;
    }

    /**
     * @return the gClusterID
     */
    public int getgClusterID() {
        return gClusterID;
    }


    /**
     * @return the gNodeID
     */
    public int getgNodeID() {
        return gNodeID;
    }

    
    /**
     * @param learnCycleID the learnCycleID to set
     */
    public void setLearnCycleID(int learnCycleID) {
        this.learnCycleID = learnCycleID;
    }

    /**
     * @param gClusterID the gClusterID to set
     */
    public void setgClusterID(int gClusterID) {
        this.gClusterID = gClusterID;
    }

    /**
     * @param gNodeID the gNodeID to set
     */
    public void setgNodeID(int gNodeID) {
        this.gNodeID = gNodeID;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof EntityID){
            EntityID eID = (EntityID) o;
            if(this.gNodeID == eID.gNodeID &&
                    this.learnCycleID == eID.learnCycleID){
                return true;
            }
        }
        return false;
    }
    
}
