/**
 * Copyright (C) 2014 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import java.util.ArrayList;
import java.util.List;

/**
 * Genetic Algorithm
 * 
 * @author Terry Packer
 *
 */
public class GaControlRT extends ControlToolboxControlAlgorithmRT{

	//Potential members to move to Algorithm superclass
	private double[] inputVector;
	private double[] setpointVector; //Ideal setpoint
	private double[] outputVector;
	private double[] systemOutputVector; //System output

	private List<Double> historicalInput;
	private List<Double> historicalSetpoint;
	private List<Double> historicalControllerOutput;
	private List<Double> historicalSystemOutput;
	
	public GaControlRT(){
		
		this.historicalInput = new ArrayList<Double>();
		this.historicalSetpoint = new ArrayList<Double>();
		this.historicalControllerOutput = new ArrayList<Double>();
		this.historicalSystemOutput = new ArrayList<Double>();
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.rt.ControlToolboxControlAlgorithmRT#setSetpoint(double[])
	 */
	@Override
	public void setSetpoint(double[] setpointVector) {
		this.setpointVector = setpointVector;
		
		//This is designed for 1 input right now
		for(double input: setpointVector)
			this.historicalSetpoint.add(input);
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.rt.ControlToolboxControlAlgorithmRT#setInput(double[])
	 */
	@Override
	public void setInput(double[] inputVector) {
		this.inputVector = inputVector;
		
		//This is designed for 1 input right now
		for(double input: inputVector)
			this.historicalInput.add(input);
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.rt.ControlToolboxControlAlgorithmRT#getOutput()
	 */
	@Override
	public double[] getOutput() {
		return outputVector;
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.rt.ControlToolboxControlAlgorithmRT#calculate()
	 */
	@Override
	public void calculate() {
		
		//For now we need a setpoint point value for every input point value 
		if(this.historicalInput.size() != this.historicalSetpoint.size())
			return;
		
		for(int i=0; i< this.historicalInput.size(); i++){
			double input = this.historicalInput.get(i);
			double setpoint = this.historicalSetpoint.get(i);
			double systemOutput = this.historicalControllerOutput.get(i);
			
			//Compute
			Double controllerOutput = this.compute(input);
			//Evolve?
			this.evolve(setpoint,systemOutput);
			
			
			//Save this at the end
			this.outputVector = new double[]{controllerOutput};
		}
		
	}

	/**
	 * @param output
	 * @param setpoint
	 */
	private boolean evolve(Double setpoint, double systemOutput) {
		
		//Selection Criteria
		if(Math.abs(setpoint - systemOutput) > .1){
			//Tune the loop
			return true;
		}else{
			return false;
		}
			
		
	}

	/**
	 * @param input
	 * @return
	 */
	private Double compute(double input) {
		
		//Select an output from previous outputs and assume that 
		// each output effected the next 
		return 0D;
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.rt.ControlToolboxControlAlgorithmRT#setSystemOutput(double[])
	 */
	@Override
	public void setSystemOutput(double[] systemOutputVector) {
		this.systemOutputVector = systemOutputVector;
		
		//This is designed for 1 input right now
		for(double input: systemOutputVector)
			this.historicalSystemOutput.add(input);
		
	}

	
	
	
	
}
