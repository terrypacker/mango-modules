/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.learning.vo;

import java.util.List;

import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataSource.DataSourceRT;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;
import com.serotonin.m2m2.vo.dataSource.PointLocatorVO;
import com.serotonin.m2m2.vo.event.EventTypeVO;
import com.serotonin.m2m2.web.mvc.rest.v1.model.AbstractDataSourceModel;

/**
 * @author Terry Packer
 *
 */
public class LearningDataSourceVO extends DataSourceVO<LearningDataSourceVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#getModel()
	 */
	@Override
	public AbstractDataSourceModel<LearningDataSourceVO> getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#getConnectionDescription()
	 */
	@Override
	public TranslatableMessage getConnectionDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#createPointLocator()
	 */
	@Override
	public PointLocatorVO createPointLocator() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#createDataSourceRT()
	 */
	@Override
	public DataSourceRT createDataSourceRT() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#getEventCodes()
	 */
	@Override
	public ExportCodes getEventCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#addEventTypes(java.util.List)
	 */
	@Override
	protected void addEventTypes(List<EventTypeVO> eventTypes) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#addPropertiesImpl(java.util.List)
	 */
	@Override
	protected void addPropertiesImpl(List<TranslatableMessage> list) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#addPropertyChangesImpl(java.util.List, com.serotonin.m2m2.vo.dataSource.DataSourceVO)
	 */
	@Override
	protected void addPropertyChangesImpl(List<TranslatableMessage> list,
			LearningDataSourceVO from) {
		// TODO Auto-generated method stub
		
	}

}
