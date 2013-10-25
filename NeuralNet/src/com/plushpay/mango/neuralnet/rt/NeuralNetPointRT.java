/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.rt;

import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetPointRT {
	
	private NeuralNetPointVO vo;
	private int currentWindowSize = 0;
	private PointValueTime currentValue;
    private Integer vectorIndex; //index in vector (To match training to real input/output in MIMO systems)
    private Double max; //Max value from training
    private Double min; //Min value from training
    private boolean ready = false; //We are ready when we have a current value;
    
	public NeuralNetPointRT(NeuralNetPointVO vo, int vectorIndex){
		this.vo = vo;
		this.vectorIndex = vectorIndex;
	}
	
	/**
	 * Add a value to the point, if it is a delayed point
	 * the value will not be stored until we have reached out delay
	 * @param newValue
	 */
	public void updatePoint(PointValueTime newValue){
		if(this.currentWindowSize < this.vo.getDelay()){
			//Increment the window size
			this.currentWindowSize++;
			//But we don't have the current value yet so can't save it
		}else{
			this.ready = true;
			this.currentValue = newValue;
		}
		
	}
	
	/**
	 * Is the window full of data
	 * This means a current value exists
	 * 
	 * @return
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Get the current value.
	 * 
	 * Caution may be null if the window size isn't met
	 * @return
	 */
	public PointValueTime getCurrentValue(){
		return this.currentValue;
	}
	
	/**
	 * Used for preparing an input to the network
	 * @return
	 */
	public Double getNormalizedCurrentValue(){
		Double doubleValue = this.currentValue.getDoubleValue();
		if(Double.isNaN(doubleValue))
			return doubleValue;
		double norm = (doubleValue - min)/(max-min);
		return norm;
	}
	
	/**
	 * Used to prepare an output for the world
	 * @param d
	 * @return
	 */
	public Double getUnNormalizedCurrentValue() {
		Double norm = this.currentValue.getDoubleValue();
		if(Double.isNaN(norm))
			return norm;
		double doubleValue = norm * (max - min) + min;
		return doubleValue;	
	}

	
	
	public NeuralNetPointVO getVo() {
		return vo;
	}
	public void setVo(NeuralNetPointVO vo) {
		this.vo = vo;
	}
	public int getCurrentWindowSize(){
		return this.currentWindowSize;
	}
	public Integer getVectorIndex() {
		return this.vectorIndex;
	}
	public void setVectorIndex(Integer index){
		this.vectorIndex = index;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}


	
}
