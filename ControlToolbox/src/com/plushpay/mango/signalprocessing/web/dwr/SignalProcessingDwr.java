/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.signalprocessing.web.dwr;

import java.util.ArrayList;
import java.util.List;

import math.jwave.Transform;
import math.jwave.transforms.DiscreteFourierTransform;

import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.db.dao.PointValueDao;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.rt.dataImage.PointValueTime;
import com.serotonin.m2m2.web.dwr.ModuleDwr;
import com.serotonin.m2m2.web.dwr.util.DwrPermission;

/**
 * @author Terry Packer
 *
 */
public class SignalProcessingDwr extends ModuleDwr{
	
	
	@DwrPermission(user = true)
	public ProcessResult dft(int dataPointId, int windowSize){
		
		ProcessResult result = new ProcessResult();
		
		//Collect the data
		PointValueDao pvDao = Common.databaseProxy.newPointValueDao();
		
		//TODO Create a row mapper and fill an array
		List<PointValueTime> signal = pvDao.getLatestPointValues(dataPointId, windowSize);
		
		Transform t = new Transform( new DiscreteFourierTransform());
		double[] arrTime = new double[signal.size()*2];
		
		List<D3TimeDataSeries> signalSeriesList = new ArrayList<D3TimeDataSeries>();
		D3TimeValue[] signalValues = new D3TimeValue[signal.size()];
		
		int i=0;
		int k=0;
		double avgSamplePeriod = 0.0;
		Long lastSampleTime = null;
		for(PointValueTime value : signal){
			arrTime[k++] = value.getDoubleValue();
			arrTime[k++] = 0; //Complex portion unknown
			signalValues[i++] = new D3TimeValue(value.getTime(),value.getDoubleValue());
			
			if(lastSampleTime != null){
				avgSamplePeriod = avgSamplePeriod + (lastSampleTime - value.getTime());
				lastSampleTime = value.getTime();
			}else{
				lastSampleTime = value.getTime();
			}
		}
		
		//Compute the sample Frequ
		avgSamplePeriod = (avgSamplePeriod/1000)/signal.size();
		double sampleFreq = 1/avgSamplePeriod;
		
		double[] fft = t.forward(arrTime);
		
		
		D3TimeDataSeries series = new D3TimeDataSeries("Signal","#2ca02c",signalValues);
		signalSeriesList.add(series);
		result.addData("signal", signalSeriesList);
		
		//Create the FFT Series
		List<D3DataSeries> fftSeriesList = new ArrayList<D3DataSeries>();
		D3Value[] fftValues = new D3Value[fft.length/2];
		double maxValue = 0;
		int maxValuePosition = 0;
		double binFreq;
		int fftHalfLength = fft.length/2;
		for(int j=0; j<fftHalfLength; j++){
			binFreq = ((double)(j)*(sampleFreq/(double)fft.length));
			fftValues[j] = new D3Value(binFreq,Math.abs(fft[j]));
			if(j>0){
				//Search for max signal after DC Offset at fft[0]
				if(maxValue < fft[j])
					maxValue = fft[j];
			}
		}
		D3DataSeries fftSeries = new D3DataSeries("FFT","ff7f0e",fftValues);
		fftSeriesList.add(fftSeries);
		result.addData("fft", fftSeriesList);
		
		
		//Compute Max Frequency
		double maxFrequency = 0.0; //(maxValuePosition*sampleFreq/2)/(fft.length/2)
		result.addData("maxFrequency",maxFrequency);
		
		return result;
	}
	
	

}
