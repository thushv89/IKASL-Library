/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.utils.config;

import java.io.File;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLStackConfigConstants {
    
    //This is the path to the root folder where all the IKASL stack folders will be generated
    //So several folders for various IKASL stacks will be located inside this folder (ex. Stacks\L1F1, Stacks\L1F6, ...)
    public static String IKASL_ROOT_FOLDER = "Stacks";
    
    //This is the path to a single folder located inside the root folder
    //An example would be, Stacks\L1F2
    public static String IKASL_STACK_FOLDER;
}
