/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class generates the folder heirarchy representing the generalized nodes
 * and copies relevant images into them
 *
 * @author Thush
 */
public class FolderHeirarchyGenerator {

    private static int cycle=0;
    public static void createHierarchy(ArrayList<Map<String, String>> data, String source, String destOriginal) {
        cycle = 0;
        for (Map<String, String> map : data) {
            String dest = destOriginal + "\\" + "Cycle"+cycle;
            File dir = new File(dest);
            if(!dir.exists()){
                dir.mkdir();
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {

                String dirName = entry.getKey().replaceAll(":", "_");
                File sourceDir = new File(source);
                File subDir = new File(dest + "\\" + dirName);
                if (!subDir.exists()) {
                    subDir.mkdir();
                }
                String[] hitValTokens = entry.getValue().split(",");
                for (final String hitVal : hitValTokens) {
                    File[] matchingFiles = sourceDir.listFiles(new FilenameFilter() {

                        @Override
                        public boolean accept(File dir, String name) {
                            return name.equals(hitVal + ".jpg");
                        }
                    });

                    if (matchingFiles != null && matchingFiles.length > 0) {
                        try {
                            Path FROM = Paths.get(source + "\\" + hitVal + ".jpg");
                            Path TO = Paths.get(dest + "\\" + dirName + "\\" + hitVal + ".jpg");
                            //overwrite existing file, if exists
                            CopyOption[] options = new CopyOption[]{
                                StandardCopyOption.REPLACE_EXISTING,
                                StandardCopyOption.COPY_ATTRIBUTES
                            };
                            Files.copy(FROM, TO, options);
                        } catch (IOException ex) {
                            Logger.getLogger(FolderHeirarchyGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            cycle++;
        }
    }
}
