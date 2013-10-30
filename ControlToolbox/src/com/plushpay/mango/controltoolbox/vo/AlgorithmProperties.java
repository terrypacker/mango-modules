/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonSerializable;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;

/**
 * @author Terry Packer
 *
 */
public class AlgorithmProperties implements JsonSerializable{

	/* (non-Javadoc)
	 * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
	 */
	@Override
	public void jsonRead(JsonReader reader, JsonObject jsonObject)
			throws JsonException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
	 */
	@Override
	public void jsonWrite(ObjectWriter writer) throws IOException,
			JsonException {
		// TODO Auto-generated method stub
		
	}
	
	
	public void validate(ProcessResult response) {
		
	}

	/**
	 * @param list
	 */
	public void addProperties(List<TranslatableMessage> list) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param list
	 * @param properties
	 */
	public void addPropertyChanges(List<TranslatableMessage> list,
			AlgorithmProperties properties) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param out
	 */
	public void writeObject(ObjectOutputStream out) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param in
	 */
	public void readObject(ObjectInputStream in) {
		// TODO Auto-generated method stub
		
	}
	

}
