/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.xml.writer;

import com.ikasl.id.EntityID;
import com.ikasl.objects.GNode;
import com.ikasl.objects.NeuronLayer;
import com.ikasl.objects.Node;
import com.ikasl.utils.EntityIDGenerator;
import com.ikasl.xml.objects.GNodeHitValueData;
import java.io.File;
import java.util.ArrayList;
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
 *
 * Write both GNodeHitValueData and Neuron Layer objects in the same file
 *
 * <Data learn_cycle_id="a_id"> 
 * <element x=3 y=5> 
 *      <NodeID>xxx</ NodeID > 
 *      <ParentID>yyy</ParentID>
 *      <value>zzz</value> 
 * </element>+
 *
 * @author BUDDHIMA
 */
public class GNodeDataWriter {

    private String idSeperator = ",";

    /**
     * Write Neuron Layer & GNodeHitValueData to an XML file
     * 
     * @param layer Neuron Layer
     * @param gnhvd GNodeHitValueData object
     * @param cycleId learning cycle number
     *
     * @return boolean success or failure of the writing process
     */
    public boolean write(NeuronLayer layer, GNodeHitValueData gnhvd, int cycleId) {

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

            Map<EntityID, ArrayList<String>> dataMap = gnhvd.getData();

            // Go through the array list of neurons
            for (Map.Entry<String, Node> entry : map.entrySet()) {
                Element elementElement = doc.createElement("element");

                // Set element x, y attributes
                elementElement.setAttribute("x", String.valueOf(entry.getValue().getX()));
                elementElement.setAttribute("y", String.valueOf(entry.getValue().getY()));

                // Adding node ids to NodeID
                Element nodeIdElement = doc.createElement("NodeID");

                nodeIdElement.appendChild(doc.createTextNode(
                        EntityIDGenerator.generateEntityIDString(
                        ((GNode) entry.getValue()).getId())));
                elementElement.appendChild(nodeIdElement);

                // Adding parent ids to ParentID
                Element parentElement = doc.createElement("ParentID");

                String parentIDStr = entry.getValue().getParentID();

                //if parentID is null we'll insert an empty string
                if (parentIDStr != null) {
                    parentElement.appendChild(doc.createTextNode(parentIDStr));
                }                

                elementElement.appendChild(parentElement);

                // Deal with GNodeHitValueData object
                ArrayList<String> iList = dataMap.get(((GNode) entry.getValue()).getId());

                // Append value - 6 values in the list
                Element valueElement = doc.createElement("value");

                String value="";
                if (iList != null) {
                    for (int i = 0; i < iList.size(); i++) {
                        if (i == iList.size() - 1) {
                            value+=iList.get(i);
                        } else {
                            value+=iList.get(i) + idSeperator;
                        }
                    }
                }
                valueElement.appendChild(doc.createTextNode(value));
                
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
            StreamResult result = new StreamResult(new File("LC " + String.valueOf(cycleId) + ".xml"));// file name is created with neuron id
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Error while wrting XML file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
