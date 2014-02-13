/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.signalprocessing.web.dwr;

/**
 * @author Terry Packer
 *
 */
public class D3TimeDataSeries {
	
	private String key; //Series Name
	private String color; //
	private D3TimeValue[] values;
	
	public D3TimeDataSeries(String key, String color, D3TimeValue[] values){
		this.key = key;
		this.color = color;
		this.values = values;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public D3TimeValue[] getValues() {
		return values;
	}

	public void setValues(D3TimeValue[] values) {
		this.values = values;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
