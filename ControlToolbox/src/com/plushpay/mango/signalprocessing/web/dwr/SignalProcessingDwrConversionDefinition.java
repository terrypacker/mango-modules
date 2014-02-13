/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.signalprocessing.web.dwr;

import com.serotonin.m2m2.module.DwrConversionDefinition;

/**
 * @author Terry Packer
 *
 */
public class SignalProcessingDwrConversionDefinition extends DwrConversionDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DwrConversionDefinition#addConversions()
	 */
	@Override
	public void addConversions() {
		this.addConversion(D3DataSeries.class, "bean");
		this.addConversion(D3Value.class, "bean");
		this.addConversion(D3TimeDataSeries.class, "bean");
		this.addConversion(D3TimeValue.class,"bean");
	}


}
