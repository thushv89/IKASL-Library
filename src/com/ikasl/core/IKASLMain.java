/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core;

import com.ikasl.objects.*;
import com.ikasl.persistence.SerializeHandler;
import com.ikasl.persistence.SerializeHandler_Obsolete;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.InputReadUtils;
import com.ikasl.utils.Utils;
import com.ikasl.utils.config.IKASLStackConfigConstants;
import com.ikasl.utils.config.PropertyFileConstants;
import com.ikasl.utils.config.PropertyFileReader;
import com.ikasl.xml.objects.GNodeHitValueData;
import com.ikasl.xml.writer.GNodeDataWriter;
import com.ikasl.xml.writer.GNodeHitValueDataWriter;
import com.ikasl.xml.writer.NeuronLayerDataWriter;
import com.ikasl.xml.writer.SpecialDataWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thush
 */
public class IKASLMain {

    
    
    private String ID;
    private String inputLocation;
    
    private IKASLParams params;    
    private IKASLAggregator aggregator;
    private IKASLLearner learner;
    private GeneralizeLayerTester gTester;
    private IKASLLayer currIKASLLayer;
    int currLearnCycle;
    private ArrayList<double[]> iValues;
    private ArrayList<String> iNames;
    private Map<String, String> gTesterTestResults;
    private ArrayList<String> specialDataList;

    private final static Logger LOGGER = Logger.getLogger(IKASLMain.class.getName());
    
    public IKASLMain(IKASLParams params, String id) {
        this.params = params;
        learner = new IKASLLearner(params);
        aggregator = new IKASLAggregator(params);
        gTester = new GeneralizeLayerTester();
        currLearnCycle = 0;
        
        gTesterTestResults = new HashMap<String, String>();
        specialDataList = new ArrayList<String>();
        
        LOGGER.info("IKASL Started");
        
        setConfigLocation(id);
        readConfigFile();
        
        
    }

    public void setConfigLocation(String id){
        IKASLStackConfigConstants.IKASL_STACK_FOLDER = IKASLStackConfigConstants.IKASL_ROOT_FOLDER + 
                File.separator + id;
        PropertyFileReader.setPropertyFilePath(IKASLStackConfigConstants.IKASL_STACK_FOLDER +
                File.separator + PropertyFileConstants.PROPERTY_FILE_NAME);
        LOGGER.info("Config paramters: "+
                IKASLStackConfigConstants.IKASL_STACK_FOLDER+","+PropertyFileReader.getPropertyFilePath());
    }
    
    /**
     * Read the configuration file and obtain necessary variables
     */
    private void readConfigFile(){                
        inputLocation = PropertyFileReader.getPropertyValue(PropertyFileConstants.INPUT_LOCATION_CONFIG);
        LOGGER.info("Serialzied file name: " + 
                PropertyFileReader.getPropertyValue(PropertyFileConstants.SERIALIZE_FILE_NAME_CONFIG));
    }    

    /**
     * This method runs IKASL for one learn cycle. When inputs and their labels are provided in the 
     * correct format, 
     * 1. Bring the previous generalized layer (Neuronlayer) to memory
     * 2. Normalize inputs
     * 3. learn new knowledge
     * @param inputs inputs to be learnt
     * @param nameList name label of the inputs
     */
    public void runIKASLForCycle(LastIKASLLayer lastLayer, ArrayList<double[]> inputs, ArrayList<String> nameList){
        
        LOGGER.info("Deserializing last layer");
        
        if(lastLayer == null){
            LOGGER.warning("Last layer file could not be found");
        }else{
            LOGGER.info("Deserialized last layer. Last learn cycle: " + lastLayer.getLastLearningCycle() +
                    " NeuronLayer Size: " + lastLayer.getNeuronLayer().getNeurons().size());
        }
        
        //normalize each column in inputs with max and min bounds for each column
        InputReadUtils.normalizeWithBounds(IKASLConstants.DIMENSIONS, inputs, IKASLConstants.MIN_BOUND, IKASLConstants.MAX_BOUND);
        LOGGER.info("Inputs are normalized with MIN Bound: "+IKASLConstants.MIN_BOUND + ", MAX Bound:"+IKASLConstants.MAX_BOUND);
        
        if(lastLayer == null){
            currLearnCycle=0;
            LOGGER.info("Current Learn Cycle: "+currLearnCycle);
            
            LOGGER.info("Learning Started...");
            learnOneCycle(currLearnCycle, null, inputs, nameList);
            LOGGER.info("Learning Completed...");
        }else{
            LOGGER.info("Last GLayer has "+lastLayer.getNeuronLayer().getNeurons().size());
            
            LOGGER.info("Learning Started...");
            learnOneCycle(currLearnCycle, lastLayer.getNeuronLayer(), inputs, nameList);
            LOGGER.info("Current Learn Cycle: "+currLearnCycle);
            LOGGER.info("Learning Completed...");
        }
        
        
        
        
        SerializeHandler.serializeLastGLayer(
                PropertyFileReader.getPropertyValue(PropertyFileConstants.SERIALIZE_FILE_NAME_CONFIG),
                new LastIKASLLayer(currIKASLLayer.getGenLayer(),currLearnCycle));
        LOGGER.info("Serializing Completed. Saved as: " + 
                PropertyFileConstants.SERIALIZE_FILE_NAME_CONFIG);
    }
    
    public int getCurrLearnCycle(){
        return currLearnCycle;
    }
    
    public LastIKASLLayer retrieveLastLayer(String filename){
        LastIKASLLayer lastLayer = SerializeHandler.getLastSerializedGLayer(filename);
        return lastLayer;
    }
    
    /**
     * This method learns given inputs as a single learn cycle
     * @param cycle index of the current learn cycle to be learnt
     * @param lastLayer IKASL learns new knowledge (new layer) based on previous knowledge (last layer). This is the previously learnt Generalized NeuronLayer
     * @param inputs inputs to be learnt
     * @param nameList names of the inputs
     * @return Newly learnt "learn layer" and "generalized layer"
     */
    private IKASLLayer learnOneCycle(int cycle, NeuronLayer lastLayer,ArrayList<double[]> inputs, ArrayList<String> nameList) {

        currIKASLLayer = new IKASLLayer();
        
        Map<String, ArrayList<NeuronLayer>> learnLayer;
        if (cycle == 0) {
            learnLayer = learner.learn(null, inputs, nameList);
        } else {            
            learnLayer = learner.learn(lastLayer, inputs, nameList);

            if (learnLayer.containsKey("")) {
                LOGGER.info("Warning Learn Layer with empty key has been created. Will override the initial map");
            }
        }
        
        currIKASLLayer.setLearningLayer(learnLayer);
        ArrayList<GNode> leftOutG = learner.getLeftOutGenNodes();

        NeuronLayer gLayer = aggregator.aggregate(currLearnCycle, learnLayer, params.getHitThresh(), learner.getDisparityThreshold());

        //add the left out nodes from the previous gen layer to the new layer
        for (GNode leftOutGNode : leftOutG) {
            gLayer.addNeuron(leftOutGNode, leftOutGNode.getNodeSequence());
        }

        Map<String, String> temp = gTester.testGenLayer(cycle, gLayer, inputs, nameList);

        gTesterTestResults.putAll(temp);

        specialDataList.addAll(gTester.getSpecialData());  //data that is going to be written to special data.xml

        currIKASLLayer.setGenLayer(gLayer);        
        currLearnCycle++;
        
        return currIKASLLayer;
    }

    public Map<String, String> getTesterTestResults() {
        return gTesterTestResults;
    }

    public void writeLearnCycleXML() {
        GNodeDataWriter gnWriter = new GNodeDataWriter();
        SpecialDataWriter sdWriter = new SpecialDataWriter();

        gnWriter.write(currIKASLLayer.getGenLayer(), gTester.getGNodeHitValueData(), currLearnCycle);
        sdWriter.append(specialDataList, currLearnCycle);
        
    }

    /**
     * Serializing the last generalized neuron layer
     */
    public void serializeLastGLayer() {
        SerializeHandler_Obsolete.serializeLastGLayer(currIKASLLayer.getGenLayer());
    }

    /**
     * Get the serialized last generalized neuron layer return: NeuronLayer
     */
    public NeuronLayer getLastSerializedNeuronLayer() {
        NeuronLayer gLayer = null;
        gLayer = SerializeHandler_Obsolete.getLastSerializedGLayer();
        return gLayer;
    }

}
