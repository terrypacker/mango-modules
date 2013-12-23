/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import com.serotonin.m2m2.rt.dataImage.DataPointListener;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxInputPointListener implements DataPointListener{
	
	private ControlToolboxPointRT rt;
	private ControlToolboxControllerRT controller;

	/**
	 * @param controller
	 * @param pointRT
	 */
	public ControlToolboxInputPointListener(ControlToolboxControllerRT controller, ControlToolboxPointRT rt) {

		this.rt = rt;
		this.controller = controller;
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
		//Called anytime a value is written to the point
		rt.updatePoint(newValue);
		this.controller.updateInputs(rt);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.DataPointListener#pointChanged(com.serotonin.m2m2.rt.dataImage.PointValueTime, com.serotonin.m2m2.rt.dataImage.PointValueTime)
	 */
	@Override
	public void pointChanged(PointValueTime oldValue, PointValueTime newValue) {
		//Only called when value changes
		
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
	public ControlToolboxPointRT getRt() {
		return rt;
	}
	public void setRt(ControlToolboxPointRT rt) {
		this.rt = rt;
	}
	
}
