/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.rt;

import com.serotonin.m2m2.rt.dataImage.DataPointListener;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetInputPointListener implements DataPointListener{
	
	private NeuralNetPointRT rt;
	private NeuralNetNetworkRT network;

	/**
	 * @param network
	 * @param pointRT
	 */
	public NeuralNetInputPointListener(NeuralNetNetworkRT network, NeuralNetPointRT rt) {

		this.rt = rt;
		this.network = network;
	}


	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointInitialized()
	 */
	@Override
	public void pointInitialized() {
		//Not interested
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointUpdated(com.serotonin.m2m2.rt.dataImage.PointValueTime)
	 */
	@Override
	public void pointUpdated(PointValueTime newValue) {
		//TODO May want to listen to this for the network
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointChanged(com.serotonin.m2m2.rt.dataImage.PointValueTime, com.serotonin.m2m2.rt.dataImage.PointValueTime)
	 */
	@Override
	public void pointChanged(PointValueTime oldValue, PointValueTime newValue) {
		//Only called when value changes
		rt.updatePoint(newValue);
		this.network.updateInputs(rt);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointSet(com.serotonin.m2m2.rt.dataImage.PointValueTime, com.serotonin.m2m2.rt.dataImage.PointValueTime)
	 */
	@Override
	public void pointSet(PointValueTime oldValue, PointValueTime newValue) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointBackdated(com.serotonin.m2m2.rt.dataImage.PointValueTime)
	 */
	@Override
	public void pointBackdated(PointValueTime value) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointTerminated()
	 */
	@Override
	public void pointTerminated() {
		// TODO Auto-generated method stub
		
	}
	/* Getters and Setters */
	public NeuralNetPointRT getRt() {
		return rt;
	}
	public void setRt(NeuralNetPointRT rt) {
		this.rt = rt;
	}
	
}
