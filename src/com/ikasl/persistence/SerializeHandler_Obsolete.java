/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.persistence;

import com.ikasl.objects.NeuronLayer;
import com.ikasl.utils.config.PropertyFileConstants;
import com.ikasl.utils.config.PropertyFileReader;
import java.io.*;

/**
 *
 * @author Ruchira
 */
public class SerializeHandler_Obsolete {

    
    /**
     * This method will serialize last Generalized Neuron layer
     */
    public static void serializeLastGLayer(NeuronLayer lastGLayer) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(PropertyFileReader.getPropertyValue(PropertyFileConstants.SERIALIZE_FILE_NAME_CONFIG));
            ObjectOutputStream out =
                    new ObjectOutputStream(fileOut);
            out.writeObject(lastGLayer);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * This method returns the last serialized generalized neuron layer object
     */
    public static NeuronLayer getLastSerializedGLayer(){
        NeuronLayer neuronLayer = null;
        try {
            FileInputStream fileIn = new FileInputStream(PropertyFileReader.getPropertyValue(PropertyFileConstants.SERIALIZE_FILE_NAME_CONFIG));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            neuronLayer = (NeuronLayer) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return neuronLayer;
    }
}
