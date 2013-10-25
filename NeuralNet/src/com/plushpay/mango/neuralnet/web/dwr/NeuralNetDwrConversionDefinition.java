/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.web.dwr;

import com.plushpay.mango.neuralnet.vo.NeuralNetHiddenLayerVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
import com.serotonin.m2m2.module.DwrConversionDefinition;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetDwrConversionDefinition extends DwrConversionDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DwrConversionDefinition#addConversions()
	 */
	@Override
	public void addConversions() {
		this.addConversion(NeuralNetNetworkVO.class, "bean");
		this.addConversion(NeuralNetPointVO.class,"bean");
		this.addConversion(NeuralNetHiddenLayerVO.class,"bean");
		
	}

}
