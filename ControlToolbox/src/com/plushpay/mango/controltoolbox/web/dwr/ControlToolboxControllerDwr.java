/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.dwr;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxControllerDao;
import com.plushpay.mango.controltoolbox.dao.ControlToolboxPointDao;
import com.plushpay.mango.controltoolbox.rt.ControlToolboxControllerRT;
import com.plushpay.mango.controltoolbox.rt.ControlToolboxControllerRTMDefinition;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxPointVO;
import com.serotonin.db.pair.IntStringPair;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.web.dojo.DojoMemoryStoreListItem;
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
		super(ControlToolboxControllerDao.instance, "networks", ControlToolboxControllerRTMDefinition.instance);
		LOG = LogFactory.getLog(ControlToolboxControllerDwr.class);
	}

	
	@DwrPermission(user = true)
    public ProcessResult getTransferFunctionTypes() {
		ExportCodes codes = ControlToolboxControllerVO.CONTROLLER_TYPE_CODES;
		ProcessResult result = new ProcessResult();
		DojoMemoryStoreListItem[] list = new DojoMemoryStoreListItem[codes.size()];
		int i=0;
		for(IntStringPair code : codes.getIdKeys()){
			list[i] = (new DojoMemoryStoreListItem(translate(code.getValue()),code.getKey()));
			i++;
		}
		result.addData("transferFunctionTypes", list);
		return result;
    }
	
	@DwrPermission(user = true)
	@Override
	public ProcessResult saveFull(ControlToolboxControllerVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
	
	
}
