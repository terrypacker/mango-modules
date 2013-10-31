/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.dwr;

import com.plushpay.mango.controltoolbox.vo.AlgorithmProperties;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxAlgorithmVO;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxPointVO;
import com.plushpay.mango.controltoolbox.vo.PidAlgorithmProperties;
import com.serotonin.m2m2.module.DwrConversionDefinition;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxDwrConversionDefinition extends DwrConversionDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DwrConversionDefinition#addConversions()
	 */
	@Override
	public void addConversions() {
		this.addConversion(ControlToolboxControllerVO.class, "bean");
		this.addConversion(ControlToolboxPointVO.class,"bean");
		this.addConversion(ControlToolboxAlgorithmVO.class,"bean");
		this.addConversion(AlgorithmProperties.class,"bean");
		this.addConversion(PidAlgorithmProperties.class,"bean");
		
	}

}
