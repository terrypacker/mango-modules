/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import com.plushpay.mango.controltoolbox.vo.ControlToolboxAlgorithmVO;

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
public abstract class ControlToolboxControlAlgorithmRT {

	

	/**
	 * Set the setpoints
	 * @param setpointVector
	 */
	public abstract void setSetpoint(double[] setpointVector);
	/**
	 * @param inputVector
	 */
	public abstract void setInput(double[] inputVector);

	/**
	 * @return
	 */
	public abstract double[] getOutput();
	
	/**
	 * 
	 */
	public abstract void calculate();

}
