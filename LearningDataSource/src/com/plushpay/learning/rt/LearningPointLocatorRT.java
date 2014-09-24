/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.learning.rt;

import com.plushpay.learning.vo.LearningPointLocatorVO;
import com.serotonin.m2m2.rt.dataSource.PointLocatorRT;

/**
 * @author Terry Packer
 *
 */
public class LearningPointLocatorRT extends PointLocatorRT{

	private final boolean settable;
	
	public LearningPointLocatorRT(LearningPointLocatorVO vo){
		this.settable = vo.isSettable();
	}
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataSource.PointLocatorRT#isSettable()
	 */
	@Override
	public boolean isSettable() {
		return this.settable;
	}

}
