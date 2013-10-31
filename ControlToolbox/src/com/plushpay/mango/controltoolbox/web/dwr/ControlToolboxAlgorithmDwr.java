/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.dwr;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxAlgorithmDao;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxAlgorithmVO;
import com.serotonin.db.pair.IntStringPair;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.web.dojo.DojoMemoryStoreListItem;
import com.serotonin.m2m2.web.dwr.AbstractDwr;
import com.serotonin.m2m2.web.dwr.util.DwrPermission;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxAlgorithmDwr extends AbstractDwr<ControlToolboxAlgorithmVO,ControlToolboxAlgorithmDao>{

	/**
	 * @param dao
	 * @param keyName
	 * @param runtimeManager
	 */
	public ControlToolboxAlgorithmDwr() {
		super(ControlToolboxAlgorithmDao.instance, "algorithms");
		LOG = LogFactory.getLog(ControlToolboxAlgorithmDwr.class);
	}

	
	@DwrPermission(user = true)
    public ProcessResult getAlgorithmTypes() {
		ExportCodes codes = ControlToolboxAlgorithmVO.ALGORITHM_TYPE_CODES;
		ProcessResult result = new ProcessResult();
		DojoMemoryStoreListItem[] list = new DojoMemoryStoreListItem[codes.size()];
		int i=0;
		for(IntStringPair code : codes.getIdKeys()){
			list[i] = (new DojoMemoryStoreListItem(translate(code.getValue()),code.getKey()));
			i++;
		}
		result.addData("algorithmTypes", list);
		return result;
    }
	
	@DwrPermission(user = true)
	@Override
	public ProcessResult saveFull(ControlToolboxAlgorithmVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
	
	
}
