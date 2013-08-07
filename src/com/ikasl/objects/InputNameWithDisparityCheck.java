/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

/**This class is specifically used for dividing inputs to GNodes divideInputsToGNodes(...)
 * This class has two attributes
 * String - input name
 * Boolean - whether the EucDist(winnerNode,input) exceeded the disparity thresh
 * 
 * @author Thush
 */
public class InputNameWithDisparityCheck {
    
    private String inputName;
    private boolean disparityExceeded;

    public InputNameWithDisparityCheck(String name, boolean disparityExceeded){
        this.inputName = name;
        this.disparityExceeded = false;
    }
    
    /**
     * @return the disparityExceeded
     */
    public boolean isDisparityExceeded() {
        return disparityExceeded;
    }

    /**
     * @param disparityExceeded the disparityExceeded to set
     */
    public void setDisparityExceeded(boolean disparityExceeded) {
        this.disparityExceeded = disparityExceeded;
    }

    /**
     * @return the inputName
     */
    public String getInputName() {
        return inputName;
    }

    /**
     * @param inputName the inputName to set
     */
    public void setInputName(String inputName) {
        this.inputName = inputName;
    }
    
    
}
