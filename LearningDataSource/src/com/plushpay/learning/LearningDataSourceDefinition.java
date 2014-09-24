/**
 * Copyright (C) 2014 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.learning;

import com.plushpay.learning.vo.LearningDataSourceVO;
import com.plushpay.learning.web.LearningEditDwr;
import com.serotonin.m2m2.module.DataSourceDefinition;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;

/**
 * @author Terry Packer
 *
 */
public class LearningDataSourceDefinition extends DataSourceDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DataSourceDefinition#getDataSourceTypeName()
	 */
	@Override
	public String getDataSourceTypeName() {
		return "LEARNING";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DataSourceDefinition#getDescriptionKey()
	 */
	@Override
	public String getDescriptionKey() {
		return "learning.dataSource";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DataSourceDefinition#createDataSourceVO()
	 */
	@Override
	protected DataSourceVO<?> createDataSourceVO() {
		return new LearningDataSourceVO();
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DataSourceDefinition#getEditPagePath()
	 */
	@Override
	public String getEditPagePath() {
		return "web/editLearning.jspf";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DataSourceDefinition#getDwrClass()
	 */
	@Override
	public Class<?> getDwrClass() {
		return LearningEditDwr.class;
	}

}
