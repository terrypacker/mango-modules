/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.type.AuditEventType;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.vo.AbstractVO;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxAlgorithmVO extends AbstractVO<ControlToolboxAlgorithmVO>{

	public static final int PID_TYPE = 1;
    public static final ExportCodes ALGORITHM_TYPE_CODES = new ExportCodes();

    static {
    	ALGORITHM_TYPE_CODES.addElement(PID_TYPE, "PID",
                "controltoolbox.algorithm.type.pid");}

	public static final String XID_PREFIX = "CTA_";

	private int algorithmType = PID_TYPE;  //Algorithm Code 
	private AlgorithmProperties properties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.
	 * ObjectWriter)
	 */
	@Override
	public void jsonWrite(ObjectWriter writer) throws IOException,
			JsonException {
		super.jsonWrite(writer);

        writer.writeEntry("algorithmType", ALGORITHM_TYPE_CODES.getCode(algorithmType));
        properties.jsonWrite(writer);
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.
	 * JsonReader, com.serotonin.json.type.JsonObject)
	 */
	@Override
	public void jsonRead(JsonReader reader, JsonObject jsonObject)
			throws JsonException {
		super.jsonRead(reader, jsonObject);
		
        String text = jsonObject.getString("algorithmType");
        if (text == null) {
            throw new TranslatableJsonException("emport.error.missing", "algorithmType", ALGORITHM_TYPE_CODES.getCodeList());
        }
        algorithmType = ALGORITHM_TYPE_CODES.getId(text);
        if (!ALGORITHM_TYPE_CODES.isValidId(algorithmType)) {
            throw new TranslatableJsonException("emport.error.invalid", "algorithmType", text, ALGORITHM_TYPE_CODES.getCodeList());
        }

        switch(algorithmType){
        case PID_TYPE:
            properties  = new PidAlgorithmProperties();
        	break;
        }
        properties.jsonRead(reader, jsonObject);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.deltamation.mango.downtime.vo.AbstractVO#validate(com.serotonin.m2m2
	 * .i18n.ProcessResult)
	 */
	@Override
	public void validate(ProcessResult response) {
		super.validate(response);
		
        if (!ALGORITHM_TYPE_CODES.isValidId(algorithmType)) {
            response.addContextualMessage("algorithmType", "validate.invalidValue");
        }

        properties.validate(response);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.serotonin.m2m2.util.ChangeComparable#addProperties(java.util.List)
	 */
	@Override
	public void addProperties(List<TranslatableMessage> list) {
		super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "controltoolbox.algorithm.properties.type", algorithmType);
        this.properties.addProperties(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util
	 * .List, java.lang.Object)
	 */
	@Override
	public void addPropertyChanges(List<TranslatableMessage> list,
			ControlToolboxAlgorithmVO from) {
		super.addPropertyChanges(list, from);
        
		AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.algorithm.properties.type", from.algorithmType, algorithmType);
		this.properties.addPropertyChanges(list,from.getProperties());
	
	}

	/*
	 * Serialization
	 */

	private static final int version = 1;

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(version);
		SerializationHelper.writeSafeUTF(out, name);
		out.writeInt(algorithmType);
		this.properties.writeObject(out);
	}

	private void readObject(ObjectInputStream in) throws IOException {
		int ver = in.readInt();

		// Switch on the version of the class so that version changes can be
		// elegantly handled.
		if (ver == 1) {
			name = SerializationHelper.readSafeUTF(in);
			algorithmType = in.readInt();
			switch(algorithmType){
				case PID_TYPE:
				properties = new PidAlgorithmProperties();
				break;
			}
			properties.readObject(in);

		} else {
			throw new ShouldNeverHappenException(
					"Unknown serialization version.");
		}
	}

	/* JSP Page Helpers */

	
	/* Getters and Setters */
	public int getAlgorithmType() {
		return algorithmType;
	}
	public void setAlgorithmType(int type) {
		this.algorithmType = type;
	}
	public AlgorithmProperties getProperties() {
		return properties;
	}
	public void setProperties(AlgorithmProperties properties) {
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "controltoolbox.algorithm.description";
	}

	
}
