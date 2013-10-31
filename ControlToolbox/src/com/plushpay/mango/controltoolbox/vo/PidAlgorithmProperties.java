/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
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
import com.serotonin.json.spi.JsonProperty;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 *
 */
public class PidAlgorithmProperties extends AlgorithmProperties<PidAlgorithmProperties>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int samplePeriod; 
	@JsonProperty 
	private double p;
	@JsonProperty 
	private double i;
	@JsonProperty 
	private double d;
	
	
	
	
	/* (non-Javadoc)
	 * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
	 */
	@Override
	public void jsonRead(JsonReader reader, JsonObject jsonObject)
			throws JsonException {
	}

	/* (non-Javadoc)
	 * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
	 */
	@Override
	public void jsonWrite(ObjectWriter writer) throws IOException,
			JsonException {
	}
	
	
	public void validate(ProcessResult response) {
		
	}


	
    /*
     * Serialization
     */
    
    private static final int version = 1;
    
    public void writeObject(ObjectOutputStream out) throws IOException {

        out.writeInt(version);
        out.writeInt(samplePeriod);
        out.writeDouble(p);
        out.writeDouble(i);
        out.writeDouble(d);
    }
    
    public void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {            
        	samplePeriod = in.readInt();
        	p = in.readDouble();
        	i = in.readDouble();
        	d = in.readDouble();
        }
        else {
            throw new ShouldNeverHappenException("Unknown serialization version.");
        }
    }

	/* Getters and Setters */
	public int getSamplePeriod() {
		return samplePeriod;
	}

	public void setSamplePeriod(int samplePeriod) {
		this.samplePeriod = samplePeriod;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getI() {
		return i;
	}

	public void setI(double i) {
		this.i = i;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

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
		return "CT_PID";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util.List, java.lang.Object)
	 */
	@Override
	public void addPropertyChanges(List<TranslatableMessage> list,
			PidAlgorithmProperties from) {
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
			AlgorithmProperties<PidAlgorithmProperties> properties) {
		// TODO Auto-generated method stub
		
	}


	

}
