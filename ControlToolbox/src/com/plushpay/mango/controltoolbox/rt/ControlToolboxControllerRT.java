/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.controltoolbox.ControlToolboxSystemEventTypeDefinition;
import com.plushpay.mango.controltoolbox.dao.ControlToolboxPointDao;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxPointVO;
import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.db.dao.DataPointDao;
import com.serotonin.m2m2.db.dao.PointValueDao;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.AbstractRT;
import com.serotonin.m2m2.rt.dataImage.DataPointRT;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;
import com.serotonin.m2m2.rt.dataImage.SetPointSource;
import com.serotonin.m2m2.rt.dataImage.types.NumericValue;
import com.serotonin.m2m2.rt.event.type.EventType;
import com.serotonin.m2m2.rt.event.type.SystemEventType;
import com.serotonin.m2m2.util.timeout.TimeoutClient;
import com.serotonin.m2m2.util.timeout.TimeoutTask;
import com.serotonin.m2m2.vo.DataPointVO;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxControllerRT extends AbstractRT<ControlToolboxControllerVO> implements SetPointSource{

	private Log LOG = LogFactory.getLog(ControlToolboxControllerRT.class);
	private final ControlToolboxSetPointSource setPointSource;
    private final SystemEventType eventType;
    boolean allOK = true;
    
    //To retry init if it fails
    private final int initRetryDelay = 1000;
    
    
    //The brain
    private ControlToolboxControlAlgorithmRT algorithm;
    private double[] inputVector; //Referenceable vector of the inputs
    private Object networkLock = new Object(); //To lock algorithm on input change
    
    private Map<Integer,ControlToolboxPointRT> outputPointMap; //Map of output array index to points
    private Map<Integer,ControlToolboxPointRT> inputPointMap; //Map of input array index to points
    
    //The input listeners
    private List<ControlToolboxInputPointListener> listeners = new ArrayList<ControlToolboxInputPointListener>();
 	
    /**
	 * @param vo
	 */
	public ControlToolboxControllerRT(ControlToolboxControllerVO vo) {
		super(vo);
		this.eventType = new SystemEventType(ControlToolboxSystemEventTypeDefinition.TYPE_NAME,
                vo.getId(), EventType.DuplicateHandling.IGNORE_SAME_MESSAGE);
		this.setPointSource = new ControlToolboxSetPointSource(this);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.AbstractRT#initialize()
	 */
	@Override
	public void initialize() {
        		
		//TODO Ensure we have some training points?
        try {
            configureDataPointListeners();
            configureOutputPoints();
            configureAlgorithm();
            addDataPointListeners();
        } catch (DataPointException e) {
            // retry the initialization in 1 second
        	scheduleInitialize(initRetryDelay);
            return;
        }
	}


	
	
	/**
	 * 
	 */
	private void configureAlgorithm() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Configure the listeners but don't add them
	 * @throws DataPointException
	 */
	private void configureDataPointListeners() throws DataPointException{
		
		List<ControlToolboxPointVO> points = ControlToolboxPointDao.instance.getNetworkPoints(ControlToolboxPointVO.INPUT_TYPE,this.vo.getId());
		inputVector = new double[points.size()];
		int inputVectorIndex = 0;
		inputPointMap = new HashMap<Integer,ControlToolboxPointRT>();
		for(ControlToolboxPointVO point : points){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			ControlToolboxPointRT rt = new ControlToolboxPointRT(point,inputVectorIndex);
			inputPointMap.put(inputVectorIndex, rt);
			ControlToolboxInputPointListener listener = new ControlToolboxInputPointListener(this,rt);
			listeners.add(listener);			
			inputVectorIndex++;
		}
	}
	
	private void configureOutputPoints() throws DataPointException{
		//Setup the Output Map
		List<ControlToolboxPointVO> points = ControlToolboxPointDao.instance.getNetworkPoints(ControlToolboxPointVO.OUTPUT_TYPE,this.vo.getId());
		int vectorIndex = 0;
		outputPointMap = new HashMap<Integer,ControlToolboxPointRT>();
		for(ControlToolboxPointVO point : points){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			ControlToolboxPointRT rt = new ControlToolboxPointRT(point,vectorIndex);
			outputPointMap.put(vectorIndex,rt);
			vectorIndex++;
		}

	}
	
	
	/**
	 * Schedule to try an init at a later time
	 * @param time
	 */
    private void scheduleInitialize(long time) {
        new TimeoutTask(time, new TimeoutClient() {
            public void scheduleTimeout(long fireTime) {
                initialize();
            }
        });
    }
	/**
	 * Safely get a data point from the runtime
	 * tracking errors.
	 * @param id
	 * @return
	 * @throws DataPointException
	 */
    private DataPointRT safeGetPoint(int id) throws DataPointException {
        DataPointRT rt = Common.runtimeManager.getDataPoint(id);
        if (rt == null) {
            DataPointVO point = new DataPointDao().getDataPoint(id);
            String name = point == null ? Integer.toString(id) : point.getName();
            raiseFailureEvent(new TranslatableMessage("downtime.event.system.pointUnavailable", name, vo.getName()));
            throw new DataPointException();
        }
        return rt;
    }
    class DataPointException extends Exception {
        private static final long serialVersionUID = 1L;
    }
    
    
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.AbstractRT#terminate()
	 */
	@Override
	public void terminate() {
		removeDataPointListeners();
        returnToNormal();
 		
	}
	

	/**
	 *  Add the listeners for the points and set the point to have the necessary window full 
	 *  of data so we can hit the ground running. 
	 *  
	 * @throws DataPointException 
	 */
	private void addDataPointListeners() throws DataPointException {
		PointValueDao dao = new PointValueDao();
		for(ControlToolboxInputPointListener listener : this.listeners){
			//TODO Could speed this up by only grabbing a list for a given point once
			// for systems with a huge delay you will be grabbing one list per point
			List<PointValueTime> values = dao.getLatestPointValues(listener.getRt().getVo().getDataPointId(), listener.getRt().getVo().getDelay());
			for(PointValueTime value : values){
				listener.getRt().updatePoint(value);
			}
			//Also set the input vector too
			this.inputVector[listener.getRt().getVectorIndex()] = listener.getRt().getCurrentValue().getDoubleValue();
			Common.runtimeManager.addDataPointListener(listener.getRt().getVo().getDataPointId(), listener);
		}
	}
	
	/**
	 *  Remove the listeners for the points
	 */
	private void removeDataPointListeners() {
		for(ControlToolboxInputPointListener listener : this.listeners){
			Common.runtimeManager.removeDataPointListener(listener.getRt().getVo().getDataPointId(), listener);
		}
	}
	
	/**
	 * Update the input vector, calculate and set outputs
	 * 
	 * @param myInputVectorIndex
	 * @param newValue
	 */
	public void updateInputs(ControlToolboxPointRT pointRt) {
		synchronized(networkLock){
			
			this.inputVector[pointRt.getVectorIndex()] = pointRt.getNormalizedCurrentValue();
			this.algorithm.setInput(inputVector);
			
			//Calculate the new output
			this.algorithm.calculate();
			
			//Set the outputs
			double[] outputs = this.algorithm.getOutput();
			long time = new Date().getTime();
			
			for(int i=0; i<outputs.length; i++){
				ControlToolboxPointRT outputPoint = this.outputPointMap.get(i);
				PointValueTime pvt = new PointValueTime(new NumericValue(outputs[i]),time);
				outputPoint.updatePoint(pvt);
				Double output = outputPoint.getUnNormalizedCurrentValue();
				if(!Double.isNaN(output)){
					Common.runtimeManager.setDataPointValue(outputPoint.getVo().getDataPointId(), new NumericValue(output), this);
				}else
					LOG.error("Network: " + this.vo.getName() + " computed output " + vo.getName() + " to be NaN.");

			}
		}
		
	}
	

	private void raiseFailureEvent(TranslatableMessage message) {
        raiseFailureEvent(System.currentTimeMillis(), message);
    }
    private void raiseFailureEvent(long time, TranslatableMessage message) {
        allOK = false;
        SystemEventType.raiseEvent(eventType, time, true, message);
    }
    private void returnToNormal() {
        SystemEventType.returnToNormal(eventType, System.currentTimeMillis());
    }
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#getSetPointSourceType()
	 */
	@Override
	public String getSetPointSourceType() {
		return "CONTROL";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#getSetPointSourceId()
	 */
	@Override
	public int getSetPointSourceId() {
		return vo.getId();
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#getSetPointSourceMessage()
	 */
	@Override
	public TranslatableMessage getSetPointSourceMessage() {
        return new TranslatableMessage("neuralnet.network.outputPointAnnotation", vo.getName());
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#raiseRecursionFailureEvent()
	 */
	@Override
	public void raiseRecursionFailureEvent() {
	       raiseFailureEvent(new TranslatableMessage("neuralnet.event.system.recursionFailure", vo.getName()));		
	}

	public double[] getInputVector() {
		return inputVector;
	}
	public void setInputVector(double[] inputVector) {
		this.inputVector = inputVector;
	}

}
