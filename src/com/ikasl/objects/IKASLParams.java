/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikasl.objects;

import java.awt.geom.Dimension2D;

/**
 *
 * @author Thush
 */
public class IKASLParams {

    private int dimensions;
    private int learningCycleCount;
    private double GT;
    private double spreadFactor;
    private double maxNeighborhoodRadius;
    private double FD;
    private double startLearningRate;
    private int maxIte;
    private int hitThresh;
    private int aggregationType;

    public IKASLParams(){}
    
    public IKASLParams(int dims, int learnCycleCount, double SF,
            double NR, double FD, double LR, int maxIter, int hitThresh, int aggregationType) {
        this.dimensions = dims;
        this.learningCycleCount = learnCycleCount;
        this.spreadFactor = SF;
        this.maxNeighborhoodRadius = NR;
        this.FD = FD;
        this.startLearningRate = LR;
        this.maxIte = maxIter;
        this.hitThresh = hitThresh;
        this.aggregationType = aggregationType;
    }

    public double getGT() {
        this.GT = -this.dimensions * this.dimensions * Math.log(this.spreadFactor);
        return GT;
    }

    /**
     * @return the dimensions
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * @return the learningCycleCount
     */
    public int getLearningCycleCount() {
        return learningCycleCount;
    }

    /**
     * @return the spreadFactor
     */
    public double getSpreadFactor() {
        return spreadFactor;
    }

    /**
     * @return the maxNeighborhoodRadius
     */
    public double getMaxNeighborhoodRadius() {
        return maxNeighborhoodRadius;
    }

    /**
     * @return the FD
     */
    public double getFD() {
        return FD;
    }

    /**
     * @return the startLearningRate
     */
    public double getStartLearningRate() {
        return startLearningRate;
    }

    /**
     * @return the maxIte
     */
    public int getMaxIte() {
        return maxIte;
    }

    /**
     * @return the hitThresh
     */
    public int getHitThresh() {
        return hitThresh;
    }

    /**
     * @param dimensions the dimensions to set
     */
    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * @param spreadFactor the spreadFactor to set
     */
    public void setSpreadFactor(double spreadFactor) {
        this.spreadFactor = spreadFactor;
    }

    /**
     * @return the aggregationType
     */
    public int getAggregationType() {
        return aggregationType;
    }

    /**
     * @param learningCycleCount the learningCycleCount to set
     */
    public void setLearningCycleCount(int learningCycleCount) {
        this.learningCycleCount = learningCycleCount;
    }

    /**
     * @param maxNeighborhoodRadius the maxNeighborhoodRadius to set
     */
    public void setMaxNeighborhoodRadius(double maxNeighborhoodRadius) {
        this.maxNeighborhoodRadius = maxNeighborhoodRadius;
    }

    /**
     * @param FD the FD to set
     */
    public void setFD(double FD) {
        this.FD = FD;
    }

    /**
     * @param startLearningRate the startLearningRate to set
     */
    public void setStartLearningRate(double startLearningRate) {
        this.startLearningRate = startLearningRate;
    }

    /**
     * @param maxIte the maxIte to set
     */
    public void setMaxIterations(int maxIte) {
        this.maxIte = maxIte;
    }

    /**
     * @param hitThresh the hitThresh to set
     */
    public void setHitThreshold(int hitThresh) {
        this.hitThresh = hitThresh;
    }

    /**
     * @param aggregationType the aggregationType to set
     */
    public void setAggregationType(int aggregationType) {
        this.aggregationType = aggregationType;
    }
}
