/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.web.dwr;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.neuralnet.dao.NeuralNetHiddenLayerDao;
import com.plushpay.mango.neuralnet.vo.NeuralNetHiddenLayerVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.web.dwr.AbstractDwr;
import com.serotonin.m2m2.web.dwr.util.DwrPermission;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetHiddenLayerDwr extends AbstractDwr<NeuralNetHiddenLayerVO,NeuralNetHiddenLayerDao>{
	
	/**
	 * @param dao
	 * @param keyName
	 * @param runtimeManager
	 */
	public NeuralNetHiddenLayerDwr() {
		super(NeuralNetHiddenLayerDao.instance, "hiddenLayers");
		LOG = LogFactory.getLog(NeuralNetHiddenLayerDwr.class);
	}

	
	@DwrPermission(user = true)
	@Override
	public ProcessResult saveFull(NeuralNetHiddenLayerVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
}
