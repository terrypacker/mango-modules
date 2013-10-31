/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonSerializable;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.util.ChangeComparable;
import com.serotonin.m2m2.vo.DataPointVO;

/**
 * @author Terry Packer
 *
 */
public abstract class AlgorithmProperties<T> implements Serializable, Cloneable, JsonSerializable, ChangeComparable<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
	 */
	@Override
	public abstract void jsonRead(JsonReader reader, JsonObject jsonObject)
			throws JsonException;

	/* (non-Javadoc)
	 * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
	 */
	@Override
	public abstract void jsonWrite(ObjectWriter writer) throws IOException,
			JsonException ;
	
	
	public abstract void validate(ProcessResult response);

	/**
	 * @param list
	 */
	public abstract void addProperties(List<TranslatableMessage> list);

	/**
	 * @param list
	 * @param properties
	 */
	public abstract void addPropertyChanges(List<TranslatableMessage> list,
			AlgorithmProperties<T> properties) ;

	/**
	 * @param out
	 */
	public abstract void writeObject(ObjectOutputStream out)  throws IOException;

	/**
	 * @param in
	 */
	public abstract void readObject(ObjectInputStream in)  throws IOException;


}
