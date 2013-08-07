/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thush
 */
public class FileWriter {

    BufferedWriter out;

    public void initialize(String filename) {
        try {
            out = new BufferedWriter(new java.io.FileWriter(filename));
        } catch (IOException ex) {
            System.out.println("Error while opening the file to write");
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            out.close();
        } catch (IOException ex) {
            System.out.println("Error while closing");
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeData(Map<String, double[]> data) {
        DecimalFormat df = new DecimalFormat("#.000");
        for (Map.Entry<String, double[]> entry : data.entrySet()) {
            String line = entry.getKey() + "\t";
            for (double val : entry.getValue()) {
                line += df.format(val) + "\t";
            }
            line = line + "\n";
            try {
                out.write(line);
            } catch (IOException ex) {
                System.out.println("Error while writing to file");
                Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void writeData(String str) {
        try {
            out.write(str+"\n");
        } catch (IOException ex) {
            System.out.println("Error while writing to file");
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
