/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.signalprocessing.web.dwr;

/**
 * @author Terry Packer
 *
 */
public class D3Value {

	private Number x; //x value
	private Number y; //y value
	
	public D3Value(Number x, Number y){
		this.x = x;
		this.y = y;
	}

	public Number getX() {
		return x;
	}

	public void setX(Number x) {
		this.x = x;
	}

	public Number getY() {
		return y;
	}

	public void setY(Number y) {
		this.y = y;
	}
	
	
	
}
