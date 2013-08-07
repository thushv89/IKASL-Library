/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.xml.writer;

import com.ikasl.objects.GNode;
import com.ikasl.objects.NeuronLayer;
import com.ikasl.objects.Node;
import com.ikasl.utils.EntityIDGenerator;
import java.io.File;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XML file writer for Neuron Layer
 *
 * <Data learn_cycle_id="a_id">
 * <element>
 *  <NodeID>xxx</ NodeID >
 *  <ParentID> yyy </ParentID>
 * </element>+
 * 
 * </Data>
 * 
 * @deprecated 
 * 
 * @author BUDDHIMA
 */
public class NeuronLayerDataWriter {


    /**
     * Constructor for Neuron Layer Data Writer
     *
     * @param layer Neuron Layer
     * @param neuronId Id of layer
     */
    public NeuronLayerDataWriter() {
        
    }

    /**
     * Write Neuron Layer to an XML file
     *
     * @return boolean success or failure of the writing process
     */
    public boolean write(NeuronLayer layer, int cycleId) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            // Create top-level Data element
            Element dataElement = doc.createElement("Data");

            dataElement.setAttribute("learn_cycle_id", String.valueOf(cycleId));

            doc.appendChild(dataElement);

            // Neuron Layer processing
            Map<String, Node> map = layer.getNeurons();

            // Go through the array list of neurons
            for (Map.Entry<String, Node> entry : map.entrySet()) {
                Element elementElement = doc.createElement("element");

                // Adding node ids to NodeID
                Element nodeIdElement = doc.createElement("NodeID");
//                TODO: Need to implement a method in Node class
                nodeIdElement.appendChild(doc.createTextNode(
                        EntityIDGenerator.generateEntityIDString(
                        ((GNode)entry.getValue()).getId())));
                elementElement.appendChild(nodeIdElement);

                // Adding parent ids to ParentID
                Element valueElement = doc.createElement("ParentID");
//                 TODO: Need to implement a method in Node class
                String parentIDStr = entry.getValue().getParentID();
                
                //if parentID is null we'll insert an empty string
                if(parentIDStr == null){
                    parentIDStr = "";
                }
                valueElement.appendChild(doc.createTextNode(parentIDStr));
                
//                valueElement.appendChild(doc.createTextNode("Hello"));
                
                elementElement.appendChild(valueElement);
                
                elementElement.appendChild(valueElement);

                dataElement.appendChild(elementElement);
            }

            // Write to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            //Enable indentation in the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("LC " + String.valueOf(cycleId)+".xml"));// file name is created with neuron id
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Error while wrting XML file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
