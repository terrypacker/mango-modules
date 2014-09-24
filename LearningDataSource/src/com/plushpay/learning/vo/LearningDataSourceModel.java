/**
 * Copyright (C) 2014 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.learning.vo;

import com.serotonin.m2m2.web.mvc.rest.v1.model.AbstractDataSourceModel;

/**
 * @author Terry Packer
 *
 */
public class LearningDataSourceModel extends AbstractDataSourceModel<LearningDataSourceVO>{

	public LearningDataSourceModel(){
		super(new LearningDataSourceVO());
	}
	/**
	 * @param data
	 */
	public LearningDataSourceModel(LearningDataSourceVO data) {
		super(data);
	}

}
