/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.rt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

import com.plushpay.mango.neuralnet.NeuralNetSystemEventTypeDefinition;
import com.plushpay.mango.neuralnet.dao.NeuralNetHiddenLayerDao;
import com.plushpay.mango.neuralnet.dao.NeuralNetPointDao;
import com.plushpay.mango.neuralnet.vo.NeuralNetHiddenLayerVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
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
public class NeuralNetNetworkRT extends AbstractRT<NeuralNetNetworkVO> implements SetPointSource{

	private Log LOG = LogFactory.getLog(NeuralNetNetworkRT.class);
	private final NeuralNetSetPointSource setPointSource;
    private final SystemEventType eventType;
    boolean allOK = true;
    
    //To retry init if it fails
    private final int initRetryDelay = 1000;
    
    
    //The brain
    private NeuralNetwork<BackPropagation> network;
    private double[] inputVector; //Referenceable vector of the inputs
    private Object networkLock = new Object(); //To lock network on input change
    
    private Map<Integer,NeuralNetPointRT> outputPointMap; //Map of output array index to points
    private Map<Integer,NeuralNetPointRT> inputPointMap; //Map of input array index to points
    
    //The input listeners
    private List<NeuralNetInputPointListener> listeners = new ArrayList<NeuralNetInputPointListener>();
 	
    /**
	 * @param vo
	 */
	public NeuralNetNetworkRT(NeuralNetNetworkVO vo) {
		super(vo);
		this.eventType = new SystemEventType(NeuralNetSystemEventTypeDefinition.TYPE_NAME,
                vo.getId(), EventType.DuplicateHandling.IGNORE_SAME_MESSAGE);
		this.setPointSource = new NeuralNetSetPointSource(this);
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
            configureNetwork();
            trainNetwork();
            addDataPointListeners();
        } catch (DataPointException e) {
            // retry the initialization in 1 second
        	scheduleInitialize(initRetryDelay);
            return;
        }
	}


	/**
	 * Configure the network and check all points to ensure they are available.
	 * 
	 * Also does first read and output of network using current point values
	 * 
	 * @return
	 * @throws DataPointException
	 */
	private void configureNetwork() throws DataPointException{
		
		TransferFunctionType values[] = TransferFunctionType.values();
		TransferFunctionType type = TransferFunctionType.LINEAR;
		for(TransferFunctionType value : values){
			if(value.ordinal() == vo.getTransferFunctionType()){
				type = value;
				break;
			}
		}
		
		//TODO Add option to select network type
		//TODO Add option to select the number of layers and nodes per layer
		//this.network = new Perceptron(inputPoints.size(),outputPoints.size(),type);
		
		//Add the hidden layers
		List<NeuralNetHiddenLayerVO> hiddenLayers = NeuralNetHiddenLayerDao.instance.getLayersForNetwork(vo.getId());
		Integer[] neuronsInLayers = new Integer[2 + hiddenLayers.size()];
		neuronsInLayers[0] = this.listeners.size();
		int i=1;
		for(NeuralNetHiddenLayerVO hiddenLayer : hiddenLayers){
			neuronsInLayers[hiddenLayer.getLayerNumber()] = hiddenLayer.getNumberOfNeurons();
			i++;
		}
		
		//Add the Output Layer
		neuronsInLayers[i] = this.outputPointMap.size();
		
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("useBias", Boolean.valueOf(true));
		neuronProperties.setProperty("transferFunction", type);
		neuronProperties.setProperty("inputFunction", WeightedSum.class);
		
		//Pull in any properties
		Map<String,String> properties = NeuralNetNetworkVO.getPropertiesMap(this.vo.getPropertiesString());
		Iterator<String> it = properties.keySet().iterator();
		String key;
		while(it.hasNext()){
			key = it.next();
			neuronProperties.setProperty(key, Double.parseDouble(properties.get(key)));
		}
		
		//TODO eventually use NeuralNetworkFactory to get the type
		
		this.network = new MultiLayerPerceptron(Arrays.asList(neuronsInLayers),neuronProperties);

	}
	
	/**
	 * Configure the listeners but don't add them
	 * @throws DataPointException
	 */
	private void configureDataPointListeners() throws DataPointException{
		
		List<NeuralNetPointVO> points = NeuralNetPointDao.instance.getNetworkPoints(NeuralNetPointVO.INPUT_TYPE,this.vo.getId());
		inputVector = new double[points.size()];
		int inputVectorIndex = 0;
		inputPointMap = new HashMap<Integer,NeuralNetPointRT>();
		for(NeuralNetPointVO point : points){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			NeuralNetPointRT rt = new NeuralNetPointRT(point,inputVectorIndex);
			inputPointMap.put(inputVectorIndex, rt);
			NeuralNetInputPointListener listener = new NeuralNetInputPointListener(this,rt);
			listeners.add(listener);			
			inputVectorIndex++;
		}
	}
	
	private void configureOutputPoints() throws DataPointException{
		//Setup the Output Map
		List<NeuralNetPointVO> points = NeuralNetPointDao.instance.getNetworkPoints(NeuralNetPointVO.OUTPUT_TYPE,this.vo.getId());
		int vectorIndex = 0;
		outputPointMap = new HashMap<Integer,NeuralNetPointRT>();
		for(NeuralNetPointVO point : points){
			safeGetPoint(point.getDataPointId()); //Ensure the point is enabled
			NeuralNetPointRT rt = new NeuralNetPointRT(point,vectorIndex);
			outputPointMap.put(vectorIndex,rt);
			vectorIndex++;
		}

	}
	
	/**
	 * Train the network
	 * 
	 * TODO Train network in separate thread
	 */
	private void trainNetwork(){
		
		//TODO Use the VO to choose the size of the training set
		long trainingTimeEnd = this.vo.getTrainingPeriodEnd();
		long trainingTimeStart = this.vo.getTrainingPeriodStart();

		String filename = Common.MA_HOME + "/" + vo.getName() + ".nnet";
		NeuralNetTrainer trainer = new NeuralNetTrainer(this.vo,network,filename);
		long trainingTime = trainer.doTraining(trainingTimeStart,trainingTimeEnd,inputPointMap,outputPointMap);
		LOG.info("Training took: " + trainingTime + "ms");
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
		for(NeuralNetInputPointListener listener : this.listeners){
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
		for(NeuralNetInputPointListener listener : this.listeners){
			Common.runtimeManager.removeDataPointListener(listener.getRt().getVo().getDataPointId(), listener);
		}
	}
	
	/**
	 * Update the input vector, calculate and set outputs
	 * 
	 * @param myInputVectorIndex
	 * @param newValue
	 */
	public void updateInputs(NeuralNetPointRT pointRt) {
		synchronized(networkLock){
			
			this.inputVector[pointRt.getVectorIndex()] = pointRt.getNormalizedCurrentValue();
			this.network.setInput(inputVector);
			
			//Calculate the new output
			this.network.calculate();
			
			//Set the outputs
			double[] outputs = this.network.getOutput();
			long time = new Date().getTime();
			
			for(int i=0; i<outputs.length; i++){
				NeuralNetPointRT outputPoint = this.outputPointMap.get(i);
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
		return "NEURAL_NET";
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

	public NeuralNetwork<?> getNetwork() {
		return network;
	}
	public void setNetwork(NeuralNetwork<BackPropagation> network) {
		this.network = network;
	}
	public double[] getInputVector() {
		return inputVector;
	}
	public void setInputVector(double[] inputVector) {
		this.inputVector = inputVector;
	}

}
