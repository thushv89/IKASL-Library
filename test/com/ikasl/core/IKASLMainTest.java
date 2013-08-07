/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.core;

import com.ikasl.objects.IKASLParams;
import com.ikasl.objects.NeuronLayer;
import com.ikasl.utils.IKASLConstants;
import com.ikasl.utils.config.IKASLStackConfigConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLMainTest {
    
    IKASLMain ikasl;
    IKASLParams params = new IKASLParams(27, 1, 0.65, 0.4, 0.5, 0.3, 100, 1, 0);
    String IKASL_ID = "L1F2";
    
    public IKASLMainTest() {
    }
    
    @Before
    public void setUp() {        
        ikasl = new IKASLMain(params, IKASL_ID);        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setConfigLocation method, of class IKASLMain.
     */
    @Test
    public void testSetConfigLocation() {
        System.out.println("setConfigLocation");
        
        ikasl.setConfigLocation(IKASL_ID);
        assertTrue(IKASLStackConfigConstants.IKASL_STACK_FOLDER.equalsIgnoreCase("Stacks"+File.separator+IKASL_ID));
        
    }

    /**
     * Test of runIKASLForCycle method, of class IKASLMain.
     */
    @Test
    public void testRunIKASLForCycleWhenLastLayerIsNull() {
        System.out.println("runIKASLForCycle");
        ArrayList<double[]> inputs = null;
        ArrayList<String> nameList = null;
        ikasl.runIKASLForCycle(null,inputs, nameList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test whether current learn cycle is null if data provided to method is null
     */
    @Test
    public void testCurrLearnCycleIs0WhenLastLayerIsNull(){
        ArrayList<double[]> inputs = null;
        ArrayList<String> nameList = null;
        ikasl.runIKASLForCycle(null,inputs, nameList);
        assertEquals(ikasl.getCurrLearnCycle(), 0);
    }
    
}
