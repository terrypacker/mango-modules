/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.dwr;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxPointDao;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxPointVO;
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
public class ControlToolboxPointDwr extends AbstractDwr<ControlToolboxPointVO,ControlToolboxPointDao>{

	/**
	 * @param dao
	 * @param keyName
	 * @param runtimeManager
	 */
	public ControlToolboxPointDwr() {
		super(ControlToolboxPointDao.instance, "points");
		LOG = LogFactory.getLog(ControlToolboxPointDwr.class);
	}

	
	@DwrPermission(user = true)
    public ProcessResult getPointTypes() {
		ExportCodes codes = ControlToolboxPointVO.POINT_TYPES;
		ProcessResult result = new ProcessResult();
		DojoMemoryStoreListItem[] list = new DojoMemoryStoreListItem[codes.size()];
		int i=0;
		for(IntStringPair code : codes.getIdKeys()){
			list[i] = (new DojoMemoryStoreListItem(translate(code.getValue()),code.getKey()));
			i++;
		}
		result.addData("pointTypes", list);
		return result;
    }
	
	@DwrPermission(user = true)
	@Override
	public ProcessResult saveFull(ControlToolboxPointVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
	@DwrPermission(user = true)
	public ProcessResult removeAllInputs(int controllerId){
		ProcessResult result = new ProcessResult();
		List<ControlToolboxPointVO> points = this.dao.getControllerPoints(ControlToolboxPointVO.INPUT_TYPE, controllerId);
		for(ControlToolboxPointVO vo : points){
			this.dao.delete(vo.getId());
		}
		return result;
	}
}
