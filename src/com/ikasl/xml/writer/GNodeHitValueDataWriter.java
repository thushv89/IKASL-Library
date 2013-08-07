/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.xml.writer;

import com.ikasl.id.EntityID;
import com.ikasl.utils.EntityIDGenerator;
import com.ikasl.xml.objects.GNodeHitValueData;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
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
 * This class is to write relevant XML file from the given gNodeHitValueData
 * Required file name along should be given to write method
 *
 * @deprecated 
 * 
 * @author BUDDHIMA
 */
public class GNodeHitValueDataWriter {

    private String idSeperator = ",";

    public GNodeHitValueDataWriter() {
    }

    /**
     * Method for writing the XML file
     *
     * @param gnhvd gNodeHitValue object
     * @param outputFilePath file name along with .XML extension
     */
    public boolean write(GNodeHitValueData gnhvd, String outputFilePath) {
        try {

            GNodeHitValueData gNodeHitValueData = gnhvd;
            Map<EntityID, ArrayList<String>> dataMap = gNodeHitValueData.getData();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            // Create top-level Data element
            Element dataElement = doc.createElement("Data");

            dataElement.setAttribute("ikaslId", "wwww");
            dataElement.setAttribute("parentId", "vvvv");

            doc.appendChild(dataElement);


            // Get Key set
            Set<EntityID> ids = dataMap.keySet();

            for (EntityID id : ids) {

                Element elementElement = doc.createElement("element");


                // Append NodeId
                /**
                 * learnCycleID; gClusterID; sClusterID; gNodeID;
                 */
                Element nodeIdElement = doc.createElement("NodeID");
                nodeIdElement.appendChild(doc.createTextNode(EntityIDGenerator.generateEntityIDString(id)));
                elementElement.appendChild(nodeIdElement);

                // Getting value list
                ArrayList<String> iList = dataMap.get(id);

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
            StreamResult result = new StreamResult(new File(outputFilePath));
            transformer.transform(source, result);



        } catch (Exception e) {

            System.out.println("Error while wrting XML file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * This method is to append a GNodeHitValue to a already existing XML file
     *
     * @param gNodeHitValueData1 gNodeHitValue object
     * @param outputFilePath file to write
     * @return
     */
    public boolean append(GNodeHitValueData gNodeHitValueData1, String outputFilePath) {

        Map<EntityID, ArrayList<String>> appendDataMap = gNodeHitValueData1.getData();
        // Get Key set
        Set<EntityID> ids = appendDataMap.keySet();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(outputFilePath);
            Element root = document.getDocumentElement();

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            document.getDocumentElement().normalize();


            for (EntityID id : ids) {
                // server elements
                Element elementElement = document.createElement("element");
                //rootElement.appendChild(elementElement);

                Element nodeIdElement = document.createElement("NodeID");
                nodeIdElement.appendChild(document.createTextNode(EntityIDGenerator.generateEntityIDString(id)));
                elementElement.appendChild(nodeIdElement);

                // Getting value list
                ArrayList<String> iList = appendDataMap.get(id);

                // Append value - 6 values in the list
                Element valueElement = document.createElement("value");
                for(int i=0;i<iList.size();i++){
                    if(i==iList.size()-1){
                        valueElement.appendChild(document.createTextNode(iList.get(i)));
                    }else{
                        valueElement.appendChild(document.createTextNode(iList.get(i)+idSeperator));
                    }
                }
                elementElement.appendChild(valueElement);

                root.appendChild(elementElement);
            }

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            //Enable indentation in the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            StreamResult result = new StreamResult(outputFilePath);
            transformer.transform(source, result);

        } catch (FileNotFoundException fnfe) {
            this.write(gNodeHitValueData1, outputFilePath);
        } catch (Exception e) {
            System.out.println("Error while appending XML file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
