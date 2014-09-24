/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.learning.rt;

import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataImage.DataPointRT;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;
import com.serotonin.m2m2.rt.dataImage.SetPointSource;
import com.serotonin.m2m2.rt.dataSource.EventDataSource;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;

/**
 * @author Terry Packer
 *
 */
public class LearningDataSourceRT extends EventDataSource{

	/**
	 * @param vo
	 */
	public LearningDataSourceRT(DataSourceVO<?> vo) {
		super(vo);
	}

	@Override
	public void initialize() {
		
		//Do setup work here
		
		super.initialize();
	}

	@Override
	public void terminate() {
		super.terminate();

	}

	@Override
	public void setPointValue(DataPointRT dataPoint, PointValueTime valueTime,
			SetPointSource source) {

		// No Points for this source

	}

	/**
	 * @param eventId
	 */
	public void returnToNormal(int eventId) {
		this.returnToNormal(eventId,System.currentTimeMillis());
	}

	/**
	 * @param eventId
	 * @param message
	 */
	public void raiseDataSourceEvent(int eventId, TranslatableMessage message) {
		this.raiseEvent(eventId, System.currentTimeMillis(), true, message);

	}
	
	
}
