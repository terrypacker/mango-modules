/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.rt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.serotonin.db.MappedRowCallback;
import com.serotonin.m2m2.db.dao.PointValueDao;
import com.serotonin.m2m2.rt.dataImage.IdPointValueTime;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetTrainer implements LearningEventListener{

	private NeuralNetwork<BackPropagation> network;
	private String filename;
	private NeuralNetNetworkVO vo;
	
	/**
	 * @param network
	 */
	public NeuralNetTrainer(NeuralNetNetworkVO vo, NeuralNetwork<BackPropagation> network, String filename) {
		this.vo = vo;
		this.network = network;
		this.filename = filename;

	}

	/**
	 * Do the training
	 * @param trainingTimeStart
	 * @param trainingTimeEnd
	 * @param inputPointMap
	 * @param outputPointMap
	 * @return
	 */
	public long doTraining(long trainingTimeStart, long trainingTimeEnd,
			Map<Integer,NeuralNetPointRT> inputPointMap,
			Map<Integer,NeuralNetPointRT> outputPointMap){
		
		//Each point will have a point mapped to it for its training
		
		//Create a list of all the data points we will need for the training
		//Also create a final map to use to map the values to the input/output array
		Set<Integer> dataPointIds = new HashSet<Integer>();
		final Map<Integer,List<NeuralNetPointRT>> inputMap = new HashMap<Integer,List<NeuralNetPointRT>>(); //Map of point id to vector index
		final Map<Integer,List<NeuralNetPointRT>> outputMap = new HashMap<Integer,List<NeuralNetPointRT>>(); //Map of point id to vector index
		//Create a bit set for use to determine when we have data to feed into the simulation
		final BitSet dataNotReady = new BitSet();

		Iterator<NeuralNetPointRT> it = inputPointMap.values().iterator();
		while(it.hasNext()){
			NeuralNetPointRT rt = it.next();
			dataPointIds.add(rt.getVo().getTrainingDataPointId());
			
			if(inputMap.containsKey(rt.getVo().getTrainingDataPointId())){
				inputMap.get(rt.getVo().getTrainingDataPointId()).add(rt);
			}else{
				List<NeuralNetPointRT> list = new ArrayList<NeuralNetPointRT>();
				list.add(rt);
				inputMap.put(rt.getVo().getTrainingDataPointId(), list);
			}
			dataNotReady.set(rt.getVectorIndex());
		}
		it = outputPointMap.values().iterator();
		while(it.hasNext()){
			NeuralNetPointRT rt = it.next();
			dataPointIds.add(rt.getVo().getTrainingDataPointId());
			if(outputMap.containsKey(rt.getVo().getTrainingDataPointId())){
				outputMap.get(rt.getVo().getTrainingDataPointId()).add(rt);
			}else{
				List<NeuralNetPointRT> list = new ArrayList<NeuralNetPointRT>();
				list.add(rt);
				outputMap.put(rt.getVo().getTrainingDataPointId(), list);
			}
			
			dataNotReady.set(rt.getVectorIndex());
		}
		
		//We need to sort the data into rows of inputs and outputs matched by timestamp
		PointValueDao dao = new PointValueDao();
	
		//Values for use inside the callback
		final double[] inputs = new double[inputPointMap.size()];
		final double[] outputs = new double[outputPointMap.size()];

		//The training set
		final DataSet trainingSet = new DataSet(network.getInputsCount(),network.getOutputsCount());
		
		//Create the callback to operate on the values in time order
		MappedRowCallback<IdPointValueTime> callback = new MappedRowCallback<IdPointValueTime>() {
            @Override
            public void row(IdPointValueTime ipvt, int index) {
            	

            		
        		//First see if we are an input or output
        		List<NeuralNetPointRT> inputPoints = inputMap.get(ipvt.getDataPointId());
        		if(inputPoints != null){
        			
        			//Update the point value
        			for(NeuralNetPointRT inputPoint : inputPoints){
   
        				//Save Max
            			Double max = inputPoint.getMax();
            			if(max == null)
            				inputPoint.setMax(ipvt.getDoubleValue());
            			else if(max < ipvt.getDoubleValue())
            				inputPoint.setMax(ipvt.getDoubleValue());
            			
            			//Save Min
            			Double min = inputPoint.getMin();
            			if(min == null)
            				inputPoint.setMin(ipvt.getDoubleValue());
            			else if(min > ipvt.getDoubleValue())
            				inputPoint.setMin(ipvt.getDoubleValue());
        				
	        			inputPoint.updatePoint(ipvt);
	        			if(inputPoint.isReady()){
	        				dataNotReady.clear(inputPoint.getVectorIndex());
	        				inputs[inputPoint.getVectorIndex()] = inputPoint.getCurrentValue().getDoubleValue();
	        			}
        			}
        		}
        		
        		//Are we an output
        		List<NeuralNetPointRT> outputPoints = outputMap.get(ipvt.getDataPointId());
        		if(outputPoints != null){
        			
        			for(NeuralNetPointRT outputPoint : outputPoints){
	          			//Save Max
	        			Double max = outputPoint.getMax();
	        			if(max == null)
	        				outputPoint.setMax(ipvt.getDoubleValue());
	        			else if(max < ipvt.getDoubleValue())
	        				outputPoint.setMax(ipvt.getDoubleValue());
	        			//Save Min
	        			Double min = outputPoint.getMin();
	        			if(min == null)
	        				outputPoint.setMin(ipvt.getDoubleValue());
	        			else if(min > ipvt.getDoubleValue())
	        				outputPoint.setMin(ipvt.getDoubleValue());
	        			
	        			//Update the point value
	                	outputPoint.updatePoint(ipvt);
	                	if(outputPoint.isReady()){
	        				dataNotReady.clear(outputPoint.getVectorIndex()); //Ready with data
	                		outputs[outputPoint.getVectorIndex()] = outputPoint.getCurrentValue().getDoubleValue();
	                	}
        			}
        		}
        		
        		//When we have a full set of data
            	if(dataNotReady.cardinality() == 0){
            		trainingSet.addRow(new DataSetRow(Arrays.copyOf(inputs, inputs.length),Arrays.copyOf(outputs, outputs.length)));
            	}
            }
		};
		
		//Pull in the data
		dao.getPointValuesBetween(new ArrayList<Integer>(dataPointIds), trainingTimeStart, trainingTimeEnd,callback);

		//Determine Normalizer
		//DecimalScaleNormalizer
		MaxMinNormalizer normalizer = new MaxMinNormalizer();
		//MaxNormalizer
		trainingSet.normalize(normalizer);
		
		
		//LMS rule = (LMS)network.getLearningRule();
		BackPropagation rule = (BackPropagation)network.getLearningRule();
		rule.setMaxError(vo.getMaxError());
		rule.setLearningRate(vo.getLearningRate());
		rule.setMaxIterations(vo.getLearningMaxIterations());
		rule.addListener(this);
		
		//TODO Run training in separate thread
		long start = System.currentTimeMillis();
		network.learn(trainingSet,rule);
		long end = System.currentTimeMillis();
		
		if(filename != null)
			//When done we can save it to the drive
			network.save(filename);
		return end-start; //Return training time

	}
	
	
    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp.getTotalNetworkError());
    }
	
	
}
