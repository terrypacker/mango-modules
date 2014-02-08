/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.signalprocessing.web.dwr;

/**
 * @author Terry Packer
 *
 */
public class D3TimeValue {

	private long x; //Date
	private double y; //Value
	
	public D3TimeValue(long x, double y){
		this.x = x;
		this.y = y;
	}

	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
	
}
