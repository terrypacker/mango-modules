/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neuroph.util.TransferFunctionType;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonProperty;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.type.AuditEventType;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.vo.AbstractActionVO;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetNetworkVO extends AbstractActionVO<NeuralNetNetworkVO>{

    public static final ExportCodes TRANSFER_FUNCTION_CODES = new ExportCodes();
    static {
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.LINEAR.ordinal(), "LINEAR",
                "neuralnet.network.transferfunction.type.linear");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.RAMP.ordinal(), "RAMP",
                "neuralnet.network.transferfunction.type.ramp");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.STEP.ordinal(), "STEP",
                "neuralnet.network.transferfunction.type.step");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.SIGMOID.ordinal(), "SIGMOID",
                "neuralnet.network.transferfunction.type.sigmoid");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.TANH.ordinal(), "TANH",
                "neuralnet.network.transferfunction.type.tanh");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.GAUSSIAN.ordinal(), "GAUSSIAN",
                "neuralnet.network.transferfunction.type.gaussian");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.TRAPEZOID.ordinal(), "TRAPEZOID",
                "neuralnet.network.transferfunction.type.trapezoid");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.SGN.ordinal(), "SGN",
                "neuralnet.network.transferfunction.type.sgn");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.SIN.ordinal(), "SIN",
                "neuralnet.network.transferfunction.type.sin");
    	TRANSFER_FUNCTION_CODES.addElement(TransferFunctionType.LOG.ordinal(), "LOG",
                "neuralnet.network.transferfunction.type.log");}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    /**
     * XID prefix for monitors
     */
    public static final String XID_PREFIX = "NEUNET_";
    
    private int transferFunctionType = TransferFunctionType.LINEAR.ordinal();
    @JsonProperty
    private double learningRate = 0.7;
    @JsonProperty
    private double maxError = 0.02;
    @JsonProperty
    private int learningMaxIterations = 1000;
    @JsonProperty
    private long trainingPeriodStart; //ms timestamp
    @JsonProperty
    private long trainingPeriodEnd; //ms timestamp
 
    private String propertiesString;;
    
    /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
     */
    @Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        super.jsonWrite(writer);
        writer.writeEntry("transferFunctionType", TRANSFER_FUNCTION_CODES.getCode(this.transferFunctionType));
        writer.writeEntry("properties", this.getPropertiesString());
    }
    
    /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
     */
    @Override
    public void jsonRead(JsonReader reader, JsonObject jsonObject) throws JsonException {
        super.jsonRead(reader, jsonObject);       
        
        String text = jsonObject.getString("transferFunctionType");
        if (text == null) {
            throw new TranslatableJsonException("emport.error.missing", "transferFunctionType", TRANSFER_FUNCTION_CODES.getCodeList());
        }
        transferFunctionType = TRANSFER_FUNCTION_CODES.getId(text);
        if (!TRANSFER_FUNCTION_CODES.isValidId(transferFunctionType)) {
            throw new TranslatableJsonException("emport.error.invalid", "transferFunctionType", text, TRANSFER_FUNCTION_CODES.getCodeList());
        }
        
        this.setPropertiesString(jsonObject.getString("properties"));
    }
    
    /* (non-Javadoc)
     * @see com.deltamation.mango.downtime.vo.AbstractVO#validate(com.serotonin.m2m2.i18n.ProcessResult)
     */
    @Override
    public void validate(ProcessResult response) {
        super.validate(response);
              
        if (!TRANSFER_FUNCTION_CODES.isValidId(transferFunctionType)) {
            response.addContextualMessage("transferFunctionType", "validate.invalidValue");
        }
        if((learningRate > 1.0)||(learningRate < 0.0)){
        	response.addContextualMessage("learningRate","validate.invalidValue");
        }
        if((maxError > 1.0)||(maxError < 0.0)){
        	response.addContextualMessage("maxError","validate.invalidValue");
        }
        if(trainingPeriodEnd < trainingPeriodStart){
        	response.addContextualMessage("trainingPeriodStart", "neuralnet.validate.trainingPeriodEndBeforeStart");
        	response.addContextualMessage("trainingPeriodEnd", "neuralnet.validate.trainingPeriodEndBeforeStart");
        }
        Date now = new Date();
        if(trainingPeriodEnd > now.getTime()){
        	response.addContextualMessage("trainingPeriodEnd", "validate.invalidValue");
        }
        if(trainingPeriodStart > now.getTime()){
        	response.addContextualMessage("trainingPeriodStart", "validate.invalidValue");
        }
        //Not needed yet validateProperties(response);
        
    }
    
	/* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addProperties(java.util.List)
     */
    @Override
    public void addProperties(List<TranslatableMessage> list) {
        super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.transferFunctionType", transferFunctionType);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.maxError", maxError);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.learningRate", learningRate);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.learningMaxIterations", learningMaxIterations);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.trainingPeriodStart", trainingPeriodStart);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.trainingPeriodEnd", trainingPeriodEnd);
        AuditEventType.addPropertyMessage(list, "neuralnet.network.properties.properties", propertiesString);
        

    }
    
    /* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util.List, java.lang.Object)
     */
    @Override
    public void addPropertyChanges(List<TranslatableMessage> list, NeuralNetNetworkVO from) {
        super.addPropertyChanges(list, from);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.transferFunctionType", from.transferFunctionType, transferFunctionType);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.maxError", from.maxError, maxError);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.learningRate", from.learningRate, learningRate);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.learningMaxIterations", from.learningMaxIterations, learningMaxIterations);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.trainingPeriodStart", from.trainingPeriodStart, trainingPeriodStart);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.trainingPeriodEnd", from.trainingPeriodEnd, trainingPeriodEnd);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.network.properties.properties", from.propertiesString, propertiesString);

    }
    
    /*
     * Serialization
     */
    
    private static final int version = 1;
    
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.writeInt(version);
        SerializationHelper.writeSafeUTF(out, name);
        out.writeBoolean(enabled);
        out.writeInt(transferFunctionType);
        out.writeDouble(learningRate);
        out.writeDouble(maxError);
        out.writeInt(learningMaxIterations);
        out.writeLong(trainingPeriodStart);
        out.writeLong(trainingPeriodEnd);
        SerializationHelper.writeSafeUTF(out,getPropertiesString());
    }
    
    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {            
        	name = SerializationHelper.readSafeUTF(in);
            enabled = in.readBoolean();
            transferFunctionType = in.readInt();
            learningRate = in.readDouble();
            maxError = in.readDouble();
            learningMaxIterations = in.readInt();
            trainingPeriodStart = in.readLong();
            trainingPeriodEnd = in.readLong();
            setPropertiesString(SerializationHelper.readSafeUTF(in));
        }
        else {
            throw new ShouldNeverHappenException("Unknown serialization version.");
        }
    }
    
	/*
	 * Helper methods
	 */
	public static Map<String, String> getPropertiesMap(String propertiesString) {
		HashMap<String,String> properties = new HashMap<String,String>();
		String[] nameValuePairs = propertiesString.split(",");
		String name,value;
		String[] parts;
		//Set the properties 
		for(String nameValuePair : nameValuePairs){
			nameValuePair = nameValuePair.trim();
			if(nameValuePair.contains("=")){
				parts = nameValuePair.split("=");
				name = parts[0];
				value = parts[1];
				if((name!=null)&&(value!=null))
					properties.put(name, value);
			}
		}
		
		return properties;
	}

	public static String createPropertiesString(Map<String, String> properties){
		Iterator<String> it = properties.keySet().iterator();
		String key,value;
		String propertiesString = "";
		while(it.hasNext()){
			key = it.next();
			value = properties.get(key);
			propertiesString += key + "=" + value + ",";
		}
		return propertiesString;
	}
    
    
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "neuralnet.network.description";
	}
	
	/* Getters and Setters */
	
	public int getTransferFunctionType() {
		return transferFunctionType;
	}
	public void setTransferFunctionType(int transferFunctionType) {
		this.transferFunctionType = transferFunctionType;
	}
	public double getLearningRate() {
		return learningRate;
	}
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	public double getMaxError() {
		return maxError;
	}
	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}
	public int getLearningMaxIterations() {
		return learningMaxIterations;
	}
	public void setLearningMaxIterations(int learningMaxIterations) {
		this.learningMaxIterations = learningMaxIterations;
	}
	public long getTrainingPeriodStart() {
		return trainingPeriodStart;
	}
	public void setTrainingPeriodStart(long trainingPeriodStart) {
		this.trainingPeriodStart = trainingPeriodStart;
	}
	public long getTrainingPeriodEnd() {
		return trainingPeriodEnd;
	}
	public void setTrainingPeriodEnd(long trainingPeriodEnd) {
		this.trainingPeriodEnd = trainingPeriodEnd;
	}

	public String getPropertiesString() {
		return this.propertiesString;
	}
	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}







}
