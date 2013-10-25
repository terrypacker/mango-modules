/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.rt;

import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataImage.SetPointSource;

/**
 * @author Terry Packer
 * 
 * Class to set output points based on a neural network output
 *
 */
public class NeuralNetSetPointSource implements SetPointSource{

	private NeuralNetNetworkRT rt;
	
	public NeuralNetSetPointSource(NeuralNetNetworkRT rt){
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
