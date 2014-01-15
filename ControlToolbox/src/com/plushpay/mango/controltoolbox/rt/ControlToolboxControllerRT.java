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
import com.plushpay.mango.controltoolbox.dao.ControlToolboxAlgorithmDao;
import com.plushpay.mango.controltoolbox.dao.ControlToolboxPointDao;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxAlgorithmVO;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxPointVO;
import com.plushpay.mango.controltoolbox.vo.PidAlgorithmProperties;
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
    private double[] inputHighLimitVector; //Referenceable vector of the inputs
    private double[] inputLowLimitVector; //Referenceable vector of the inputs

    private double[] setpointVector; //Referenceable vector of the setpoints
    private double[] setpointHighLimitVector; //Referenceable vector of the setpoints
    private double[] setpointLowLimitVector; //Referenceable vector of the setpoints
    
    private double[] outputVector; //Referenceable vector of the outputs
    private double[] outputHighLimitVector; //Referenceable vector of the output limits
    private double[] outputLowLimitVector; //Referenceable vector of the output limits
    
    
    private Object lock = new Object(); //To lock algorithm on input change
    
    private Map<Integer,ControlToolboxPointRT> outputPointMap; //Map of output array index to points
    private Map<Integer,ControlToolboxPointRT> inputPointMap; //Map of input array index to points
    private Map<Integer,ControlToolboxPointRT> setpointMap; //Map of setpoint array index to points
    
    
    //The input listeners
    private List<ControlToolboxInputPointListener> listeners = new ArrayList<ControlToolboxInputPointListener>();
    //The setpoint listeners
    private List<ControlToolboxInputPointListener> setpointListeners = new ArrayList<ControlToolboxInputPointListener>();
 	
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
		ControlToolboxAlgorithmVO algorithm = ControlToolboxAlgorithmDao.instance.get(this.vo.getAlgorithmId());
		if(algorithm != null){
			
			switch(algorithm.getAlgorithmType()){
				case ControlToolboxAlgorithmVO.PID_TYPE:
					PidAlgorithmProperties props = (PidAlgorithmProperties)algorithm.getProperties();
					this.algorithm = new PidControlAlgorithmRT(
							this.inputVector, this.inputHighLimitVector, this.inputLowLimitVector,
							this.setpointVector, this.setpointHighLimitVector, this.setpointLowLimitVector, 
							this.outputVector, this.outputHighLimitVector, this.outputLowLimitVector, 
							props.getP(), props.getI(), props.getD(),props.getSamplePeriod());
				break;
			}
		}else{
			//TODO Notify user that the algorithm DNE
		}
		
	}

	/**
	 * Configure the listeners but don't add them
	 * @throws DataPointException
	 */
	private void configureDataPointListeners() throws DataPointException{
		
		//Setup Setpoints
		List<ControlToolboxPointVO> setPoints = ControlToolboxPointDao.instance.getControllerPoints(ControlToolboxPointVO.SETPOINT_TYPE,this.vo.getId());
		
		setpointVector = new double[setPoints.size()];
		setpointHighLimitVector = new double[setPoints.size()];
		setpointLowLimitVector = new double[setPoints.size()];
		int setpointVectorIndex = 0;
		setpointMap = new HashMap<Integer,ControlToolboxPointRT>();
		for(ControlToolboxPointVO point : setPoints){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			ControlToolboxPointRT rt = new ControlToolboxPointRT(point,setpointVectorIndex);
			setpointHighLimitVector[setpointVectorIndex] = point.getHighLimit();
			setpointLowLimitVector[setpointVectorIndex] = point.getLowLimit();
			setpointMap.put(setpointVectorIndex, rt);
			ControlToolboxInputPointListener listener = new ControlToolboxInputPointListener(this,rt);
			setpointListeners.add(listener);			
			setpointVectorIndex++;
		}

		
		
		//Setup Inputs
		List<ControlToolboxPointVO> inputPoints = ControlToolboxPointDao.instance.getControllerPoints(ControlToolboxPointVO.INPUT_TYPE,this.vo.getId());
		
		inputVector = new double[inputPoints.size()];
		inputHighLimitVector = new double[inputPoints.size()];
		inputLowLimitVector = new double[inputPoints.size()];

		int inputVectorIndex = 0;
		inputPointMap = new HashMap<Integer,ControlToolboxPointRT>();
		for(ControlToolboxPointVO point : inputPoints){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			ControlToolboxPointRT rt = new ControlToolboxPointRT(point,inputVectorIndex);
			inputHighLimitVector[inputVectorIndex] = point.getHighLimit();
			inputLowLimitVector[inputVectorIndex] = point.getLowLimit();
			inputPointMap.put(inputVectorIndex, rt);
			ControlToolboxInputPointListener listener = new ControlToolboxInputPointListener(this,rt);
			listeners.add(listener);			
			inputVectorIndex++;
		}
		
		
		
		
		
	}
	
	/**
	 * 
	 * @throws DataPointException
	 */
	private void configureOutputPoints() throws DataPointException{
		//Setup the Output Map
		List<ControlToolboxPointVO> points = ControlToolboxPointDao.instance.getControllerPoints(ControlToolboxPointVO.OUTPUT_TYPE,this.vo.getId());
		
		outputVector = new double[points.size()];
		outputHighLimitVector = new double[points.size()];
		outputLowLimitVector = new double[points.size()];

		int vectorIndex = 0;
		outputPointMap = new HashMap<Integer,ControlToolboxPointRT>();
		for(ControlToolboxPointVO point : points){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			ControlToolboxPointRT rt = new ControlToolboxPointRT(point,vectorIndex);
			outputHighLimitVector[vectorIndex] = point.getHighLimit();
			outputLowLimitVector[vectorIndex] = point.getLowLimit();
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
		PointValueDao dao = Common.databaseProxy.newPointValueDao();
		
		//Configure the setpoints
		for(ControlToolboxInputPointListener listener : this.setpointListeners){
			//TODO Could speed this up by only grabbing a list for a given point once
			// for systems with a huge delay you will be grabbing one list per point
			List<PointValueTime> values = dao.getLatestPointValues(listener.getRt().getVo().getDataPointId(), listener.getRt().getVo().getDelay()+1);
			for(PointValueTime value : values){
				listener.getRt().updatePoint(value);
			}
			//Also set the input vector too
			this.setpointVector[listener.getRt().getVectorIndex()] = listener.getRt().getCurrentValue().getDoubleValue();
			this.algorithm.setSetpoint(setpointVector);
			Common.runtimeManager.addDataPointListener(listener.getRt().getVo().getDataPointId(), listener);
		}
		
		//Now for the inputs
		for(ControlToolboxInputPointListener listener : this.listeners){
			//TODO Could speed this up by only grabbing a list for a given point once
			// for systems with a huge delay you will be grabbing one list per point
			List<PointValueTime> values = dao.getLatestPointValues(listener.getRt().getVo().getDataPointId(), listener.getRt().getVo().getDelay()+1);
			for(PointValueTime value : values){
				listener.getRt().updatePoint(value);
			}
			//Also set the input vector too
			this.inputVector[listener.getRt().getVectorIndex()] = listener.getRt().getCurrentValue().getDoubleValue();
			this.algorithm.setInput(inputVector);
			Common.runtimeManager.addDataPointListener(listener.getRt().getVo().getDataPointId(), listener);
		}
	}
	
	/**
	 *  Remove the listeners for the points
	 */
	private void removeDataPointListeners() {
		for(ControlToolboxInputPointListener listener : this.setpointListeners){
			Common.runtimeManager.removeDataPointListener(listener.getRt().getVo().getDataPointId(), listener);
		}
		
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
		synchronized(lock){
			
			//Are we an input or a setpoint
			if(pointRt.getVo().getPointType() == ControlToolboxPointVO.INPUT_TYPE){
				this.inputVector[pointRt.getVectorIndex()] = pointRt.getNormalizedCurrentValue();
				this.algorithm.setInput(inputVector);
			}else if(pointRt.getVo().getPointType() == ControlToolboxPointVO.SETPOINT_TYPE){
				this.setpointVector[pointRt.getVectorIndex()] = pointRt.getNormalizedCurrentValue();
				this.algorithm.setSetpoint(setpointVector);
			}
			
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
        return new TranslatableMessage("controltoolbox.algorithm.outputPointAnnotation", vo.getName());
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.dataImage.SetPointSource#raiseRecursionFailureEvent()
	 */
	@Override
	public void raiseRecursionFailureEvent() {
	       raiseFailureEvent(new TranslatableMessage("controltoolbox.event.system.recursionFailure", vo.getName()));		
	}

	public double[] getInputVector() {
		return inputVector;
	}
	public void setInputVector(double[] inputVector) {
		this.inputVector = inputVector;
	}

}
