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
		double[] arrTime = new double[signal.size()];
		
		List<D3DataSeries> signalSeriesList = new ArrayList<D3DataSeries>();
		D3TimeValue[] signalValues = new D3TimeValue[signal.size()];
		
		int i=0;
		for(PointValueTime value : signal){
			arrTime[i] = value.getDoubleValue();
			signalValues[i++] = new D3TimeValue(value.getTime(),value.getDoubleValue());
			
		}
		double[] fft = t.forward(arrTime);
		
		D3DataSeries series = new D3DataSeries("Signal","#2ca02c",signalValues);
		signalSeriesList.add(series);
		result.addData("signal", signalSeriesList);
		
		//Create the FFT Series
		List<D3DataSeries> fftSeriesList = new ArrayList<D3DataSeries>();
		D3TimeValue[] fftValues = new D3TimeValue[fft.length];
		i=0;
		for(double d : fft){
			fftValues[i] = new D3TimeValue(i,d);
			i++;
		}
		D3DataSeries fftSeries = new D3DataSeries("FFT","ff7f0e",fftValues);
		fftSeriesList.add(fftSeries);
		result.addData("fft", fftSeriesList);
		
		return result;
	}
	
	

}
