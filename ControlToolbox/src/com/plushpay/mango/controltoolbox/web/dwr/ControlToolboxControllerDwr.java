/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.dwr;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxControllerDao;
import com.plushpay.mango.controltoolbox.rt.ControlToolboxControllerRT;
import com.plushpay.mango.controltoolbox.rt.ControlToolboxControllerRTMDefinition;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.web.dwr.AbstractRTDwr;
import com.serotonin.m2m2.web.dwr.util.DwrPermission;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxControllerDwr extends AbstractRTDwr<ControlToolboxControllerVO,ControlToolboxControllerDao,ControlToolboxControllerRT,ControlToolboxControllerRTMDefinition>{

	/**
	 * @param dao
	 * @param keyName
	 * @param runtimeManager
	 */
	public ControlToolboxControllerDwr() {
		super(ControlToolboxControllerDao.instance, "controllers", ControlToolboxControllerRTMDefinition.instance);
		LOG = LogFactory.getLog(ControlToolboxControllerDwr.class);
	}

		
	@DwrPermission(user = true)
	@Override
	public ProcessResult saveFull(ControlToolboxControllerVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
	
	
}
