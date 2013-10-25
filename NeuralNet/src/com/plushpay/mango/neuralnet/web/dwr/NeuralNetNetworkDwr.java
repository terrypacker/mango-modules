/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.web.dwr;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.neuralnet.dao.NeuralNetNetworkDao;
import com.plushpay.mango.neuralnet.dao.NeuralNetPointDao;
import com.plushpay.mango.neuralnet.rt.NeuralNetNetworkRT;
import com.plushpay.mango.neuralnet.rt.NeuralNetNetworkRTMDefinition;
import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
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
public class NeuralNetNetworkDwr extends AbstractRTDwr<NeuralNetNetworkVO,NeuralNetNetworkDao,NeuralNetNetworkRT,NeuralNetNetworkRTMDefinition>{

	/**
	 * @param dao
	 * @param keyName
	 * @param runtimeManager
	 */
	public NeuralNetNetworkDwr() {
		super(NeuralNetNetworkDao.instance, "networks", NeuralNetNetworkRTMDefinition.instance);
		LOG = LogFactory.getLog(NeuralNetNetworkDwr.class);
	}

	
	@DwrPermission(user = true)
    public ProcessResult getTransferFunctionTypes() {
		ExportCodes codes = NeuralNetNetworkVO.TRANSFER_FUNCTION_CODES;
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
	public ProcessResult saveFull(NeuralNetNetworkVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
	
	
}
