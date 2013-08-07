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
 *
 * @author BUDDHIMA
 */
public class SpecialDataWriter {

    private String idSeperator = ",";

    /**
     * Write List data to an XML file
     *
     * @param list list of values
     * @param cycleId learning cycle number
     *
     * @return boolean success or failure of the writing process
     */
    public boolean write(ArrayList<String> list, int cycleId) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            // Create top-level Data element
            Element dataElement = doc.createElement("Data");

            doc.appendChild(dataElement);

            Element elementElement = doc.createElement("element");
            elementElement.setAttribute("learn_cycle_id", String.valueOf(cycleId));

            // Append value -  values in the list
            Element valueElement = doc.createElement("value");

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        valueElement.appendChild(doc.createTextNode(list.get(i)));
                    } else {
                        valueElement.appendChild(doc.createTextNode(list.get(i) + idSeperator));
                    }
                }
            }
            elementElement.appendChild(valueElement);

            dataElement.appendChild(elementElement);


            // Write to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            //Enable indentation in the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("SpecialData.xml"));// file name is created with neuron id
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
     * This method is to append a List data to a already existing XML file
     *
     * @param list list of values
     * @param cycleId learning cycle number
     *
     * @return
     */
    public boolean append(ArrayList<String> list, int cycleId) {//write(ArrayList<String> list, int cycleId)


        String fileName = "SpecialData.xml";

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);
            Element root = document.getDocumentElement();

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            document.getDocumentElement().normalize();


            {
                // server elements
                Element elementElement = document.createElement("element");
                elementElement.setAttribute("learn_cycle_id", String.valueOf(cycleId)); 
                // Append value -  values in the list
                Element valueElement = document.createElement("value");

                String value="";
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        value+=list.get(i);
                    } else {
                        value+=list.get(i) + idSeperator;
                    }
                }
                                
                valueElement.appendChild(document.createTextNode(value));
                elementElement.appendChild(valueElement);

                root.appendChild(elementElement);
            }

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            //Enable indentation in the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult result = new StreamResult(fileName);
            transformer.transform(source, result);

        } catch (FileNotFoundException fnfe) {
            this.write(list, cycleId);
        } catch (Exception e) {
            System.out.println("Error while appending XML file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
