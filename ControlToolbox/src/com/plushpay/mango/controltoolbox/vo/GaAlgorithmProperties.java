/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
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
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;

/**
 * @author Terry Packer
 *
 */
public class GaAlgorithmProperties extends AlgorithmProperties<GaAlgorithmProperties>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#getId()
	 */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "CT_GA";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util.List, java.lang.Object)
	 */
	@Override
	public void addPropertyChanges(List<TranslatableMessage> list,
			GaAlgorithmProperties from) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
	 */
	@Override
	public void jsonRead(JsonReader reader, JsonObject jsonObject)
			throws JsonException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#jsonWrite(com.serotonin.json.ObjectWriter)
	 */
	@Override
	public void jsonWrite(ObjectWriter writer) throws IOException,
			JsonException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#validate(com.serotonin.m2m2.i18n.ProcessResult)
	 */
	@Override
	public void validate(ProcessResult response) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#addProperties(java.util.List)
	 */
	@Override
	public void addProperties(List<TranslatableMessage> list) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#addPropertyChanges(java.util.List, com.plushpay.mango.controltoolbox.vo.AlgorithmProperties)
	 */
	@Override
	public void addPropertyChanges(List<TranslatableMessage> list,
			AlgorithmProperties<GaAlgorithmProperties> properties) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#writeObject(java.io.ObjectOutputStream)
	 */
	@Override
	public void writeObject(ObjectOutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.plushpay.mango.controltoolbox.vo.AlgorithmProperties#readObject(java.io.ObjectInputStream)
	 */
	@Override
	public void readObject(ObjectInputStream in) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
