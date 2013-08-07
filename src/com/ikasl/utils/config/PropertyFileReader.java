/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Ruchira
 */
public class PropertyFileReader {

    private static String propFilePath = "";
    
    public static void setPropertyFilePath(String path){
        propFilePath = path;
    }
    public static String getPropertyFilePath(){
        return propFilePath;
    }

    public static String getPropertyValue(String name) {
        // to convert Enum to String
        String value = "";
        String key = name;
        
        // To read property file
        Properties prop = new Properties();

        try {
            //load a properties file
            prop.load(new FileInputStream(propFilePath));

            if (!key.isEmpty()) {
                //get the property value and print it out
                value = (prop.getProperty(key));
            } else {
                //System.out.println("Property key does not exist in the " + propertyFile + " file!");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return value;
    }
}
