package com.ikasl.xml.objects;




import com.ikasl.id.EntityID;
import com.ikasl.xml.objects.IKASLData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Thush
 */
//This class contains a map with following key and value
//key - Level ID, Feature ID, Learn Cycle ID, GCluster ID, GNode ID
//value - Names of files of hit data for that node
//For example, 
//P1
public class GNodeHitValueData extends IKASLData{
    
    private Map<EntityID,ArrayList<String>> data;
    
    public GNodeHitValueData(){
        data = new HashMap<EntityID,ArrayList<String>>();
    }
    
    //add key value pair
    public void addKVP(EntityID id, ArrayList<String> iList){
        data.put(id, iList);
    }
    
    public Map<EntityID,ArrayList<String>> getData(){
        return data;
    }

    public boolean containsKey(EntityID key){
        return data.containsKey(key);
    }
    /**
     * @param data the data to set
     */
    public void setData(Map<EntityID,ArrayList<String>> data) {
        this.data = data;
    }
}
