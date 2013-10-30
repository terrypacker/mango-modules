/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import java.util.Arrays;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * @author Terry Packer
 *
 */
public class PidControlAlgorithmRT implements ControlToolboxControlAlgorithmRT{

	//Potential members to move to Algorithm superclass
	private double[] inputVector;
	private double[] setpointVector; //Ideal setpoint
	private double[] outputVector;
	private double[] inputLowLimit;
	private double[] inputHighLimit;
	private double[] outputLowLimit;
	private double[] outputHighLimit;
	private double samplePeriod; //The number of seconds between samples
	
	
	private RealVector previousErrorVector; //Holds error from last time
	private RealVector integralVector; //Holds the previous integrated value
	private RealVector derivativeVector; //Holds the previous derived value
	private double p; //P Value
	private double i; //I Value
	private double d; //D Value
	
	
	/**
	 * Simple PID Controller
	 * 
	 * 
	 * 
	 * @param numInputs
	 * @param numOutputs
	 * @param p
	 * @param i
	 * @param d
	 * @param samplePeriod
	 */
	public PidControlAlgorithmRT(int numInputs, int numOutputs, double p, double i, double d, double samplePeriod){
		
		
		this.inputVector = new double[numInputs];
		this.outputVector = new double[numOutputs];
		
		double[] previousErrorVectorValues = new double[numInputs];
		Arrays.fill(previousErrorVectorValues, 0D);
		this.previousErrorVector = new ArrayRealVector(previousErrorVectorValues,false);
		
		double[] integralVectorValues = new double[numInputs];
		Arrays.fill(integralVectorValues, 0D);
		this.integralVector = new ArrayRealVector(integralVectorValues,false);

		double[] derivativeVectorValues = new double[numInputs];
		Arrays.fill(derivativeVectorValues, 0D);
		this.derivativeVector = new ArrayRealVector(derivativeVectorValues,false);
		
		
		this.p = p;
		this.i = i;
		this.d = d;
		
		this.samplePeriod = samplePeriod;
		
	}
	
	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.controller.ControlToolboxControlAlgorithmRT#setSetpoint(double[])
	 */
	@Override
	public void setSetpoint(double[] setpointVector) {
		this.setpointVector = setpointVector;
		
	}
	
	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.controller.ControlToolboxControlAlgorithmRT#setInput(double[])
	 */
	@Override
	public void setInput(double[] inputVector) {
		//Trim the input
		if(this.inputHighLimit != null){
			for(int i=0; i<this.inputHighLimit.length; i++)
				if(inputVector[i] > this.inputHighLimit[i])
					inputVector[i] = this.inputHighLimit[i];
		}
		
		if(this.inputLowLimit != null){
			for(int i=0; i<this.inputLowLimit.length; i++)
				if(inputVector[i] < this.inputLowLimit[i])
					inputVector[i] = this.inputLowLimit[i];
		}

		this.inputVector = inputVector;
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.controller.ControlToolboxControlAlgorithmRT#getOutput()
	 */
	@Override
	public double[] getOutput() {
		
		//Trim the output (assume lengths are ok)
		
		if(this.outputHighLimit != null){
			for(int i=0; i<this.outputHighLimit.length; i++)
				if(this.outputVector[i] > this.outputHighLimit[i])
					this.outputVector[i] = this.outputHighLimit[i];
		}
		
		if(this.outputLowLimit != null){
			for(int i=0; i<this.outputLowLimit.length; i++)
				if(this.outputVector[i] < this.outputLowLimit[i])
					this.outputVector[i] = this.outputLowLimit[i];
		}
		
		return this.outputVector;
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.controller.ControlToolboxControlAlgorithmRT#calculate()
	 */
	@Override
	public void calculate() {
		
		//Perform calculation
		RealVector inputVector = new ArrayRealVector(this.inputVector,false);
		RealVector setpointVector = new ArrayRealVector(this.setpointVector,false);
		RealVector errorVector = setpointVector.subtract(inputVector);
		
		//Begin computing the output p*Error + i*Integral + d*Derivative
		RealVector outputVector = errorVector.mapMultiply(this.p);
		
		if(this.i != 0D){
			this.integralVector = this.integralVector.add(errorVector.mapMultiply(this.samplePeriod));
			outputVector.add(this.integralVector.mapMultiply(this.i));
		}
		if(this.d != 0D){
			this.derivativeVector = errorVector.subtract(this.previousErrorVector);
			this.derivativeVector.mapDivideToSelf(this.samplePeriod);
			outputVector.add(this.derivativeVector.mapMultiply(this.d));
		}

		//Set the output values
		this.outputVector = outputVector.toArray();
		
				
		this.previousErrorVector = errorVector; //Save for next time
	}

	public double[] getInputLowLimit() {
		return inputLowLimit;
	}

	public void setInputLowLimit(double[] inputLowLimit) {
		this.inputLowLimit = inputLowLimit;
	}

	public double[] getInputHighLimit() {
		return inputHighLimit;
	}

	public void setInputHighLimit(double[] inputHighLimit) {
		this.inputHighLimit = inputHighLimit;
	}

	public double[] getOutputLowLimit() {
		return outputLowLimit;
	}

	public void setOutputLowLimit(double[] outputLowLimit) {
		this.outputLowLimit = outputLowLimit;
	}

	public double[] getOutputHighLimit() {
		return outputHighLimit;
	}

	public void setOutputHighLimit(double[] outputHighLimit) {
		this.outputHighLimit = outputHighLimit;
	}




		
	
}
