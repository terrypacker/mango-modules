/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.dwr;

import com.serotonin.m2m2.module.DwrDefinition;
import com.serotonin.m2m2.web.dwr.ModuleDwr;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxAlgorithmDwrDefinition extends DwrDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DwrDefinition#getDwrClass()
	 */
	@Override
	public Class<? extends ModuleDwr> getDwrClass() {
		return ControlToolboxAlgorithmDwr.class;
	}
	

}
