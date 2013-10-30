/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataImage.SetPointSource;

/**
 * @author Terry Packer
 * 
 * Class to set output points based on a neural network output
 *
 */
public class ControlToolboxSetPointSource implements SetPointSource{

	private ControlToolboxControllerRT rt;
	
	public ControlToolboxSetPointSource(ControlToolboxControllerRT rt){
		this.rt = rt;
	}
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#getSetPointSourceType()
	 */
	@Override
	public String getSetPointSourceType() {
		return rt.getSetPointSourceType();
	}
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#getSetPointSourceId()
	 */
	@Override
	public int getSetPointSourceId() {
		return rt.getVo().getId();
	}
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#getSetPointSourceMessage()
	 */
	@Override
	public TranslatableMessage getSetPointSourceMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#raiseRecursionFailureEvent()
	 */
	@Override
	public void raiseRecursionFailureEvent() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
