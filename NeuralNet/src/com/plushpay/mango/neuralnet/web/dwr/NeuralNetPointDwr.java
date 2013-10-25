/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.web.dwr;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.neuralnet.dao.NeuralNetPointDao;
import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
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
public class NeuralNetPointDwr extends AbstractDwr<NeuralNetPointVO,NeuralNetPointDao>{

	/**
	 * @param dao
	 * @param keyName
	 * @param runtimeManager
	 */
	public NeuralNetPointDwr() {
		super(NeuralNetPointDao.instance, "points");
		LOG = LogFactory.getLog(NeuralNetPointDwr.class);
	}

	
	@DwrPermission(user = true)
    public ProcessResult getPointTypes() {
		ExportCodes codes = NeuralNetPointVO.POINT_TYPES;
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
	public ProcessResult saveFull(NeuralNetPointVO vo){
		ProcessResult result = super.saveFull(vo);
		
		return result;
	}
	
	@DwrPermission(user = true)
	public ProcessResult createDelayedInputPoints(int networkId, int dataPointId, int trainingDataPointId, int numberOfPoints, int delayStart){
		ProcessResult result = new ProcessResult();
		int delay = delayStart;
		for(int i=0; i<numberOfPoints; i++){
			NeuralNetPointVO newPoint = new NeuralNetPointVO();
			newPoint.setDataPointId(dataPointId);
			newPoint.setTrainingDataPointId(trainingDataPointId);
			newPoint.setPointType(NeuralNetPointVO.INPUT_TYPE);
			newPoint.setNetworkId(networkId);
			newPoint.setName(newPoint.getDataPointName() + " delay " + delay);
			newPoint.setDelay(delay);
			newPoint.setEnabled(true);
			delay++;
			
			//Save it
			NeuralNetPointDao.instance.save(newPoint);
		}
		
		return result;
	}
	
	@DwrPermission(user = true)
	public ProcessResult removeAllInputs(int networkId){
		ProcessResult result = new ProcessResult();
		List<NeuralNetPointVO> points = this.dao.getNetworkPoints(NeuralNetPointVO.INPUT_TYPE, networkId);
		for(NeuralNetPointVO vo : points){
			this.dao.delete(vo.getId());
		}
		return result;
	}
}
