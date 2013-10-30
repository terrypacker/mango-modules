/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

/**
 * Basic interface for all control algorithms.
 * 
 * Set the Setpoints
 * 
 * Set the Inputs
 * 
 * 
 * Calculate
 * 
 * Set the outputs
 * 
 * 
 * @author Terry Packer
 *
 */
public interface ControlToolboxControlAlgorithmRT {

	/**
	 * Set the setpoints
	 * @param setpointVector
	 */
	public void setSetpoint(double[] setpointVector);
	/**
	 * @param inputVector
	 */
	public void setInput(double[] inputVector);

	/**
	 * @return
	 */
	public double[] getOutput();
	
	/**
	 * 
	 */
	public void calculate();

}
