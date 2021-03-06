/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.signalprocessing.web.dwr;

/**
 * @author Terry Packer
 *
 */
public class D3DataSeries {
	
	private String key; //Series Name
	private String color; //
	private D3Value[] values;
	
	public D3DataSeries(String key, String color, D3Value[] values){
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

	public D3Value[] getValues() {
		return values;
	}

	public void setValues(D3Value[] values) {
		this.values = values;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
