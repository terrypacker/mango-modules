/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.plushpay.mango.neuralnet.dao.NeuralNetNetworkDao;
import com.serotonin.ShouldNeverHappenException;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonProperty;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.DataTypes;
import com.serotonin.m2m2.DeltamationCommon;
import com.serotonin.m2m2.db.dao.DataPointDao;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.type.AuditEventType;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.vo.AbstractActionVO;
import com.serotonin.m2m2.vo.DataPointVO;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetPointVO extends AbstractActionVO<NeuralNetPointVO>{


	public static final int INPUT_TYPE = 1;
	public static final int OUTPUT_TYPE = 2;
	public static final int TRAINING_INPUT_TYPE = 3;
	public static final int TRAINING_OUTPUT_TYPE = 4;
    public static final ExportCodes POINT_TYPES = new ExportCodes();
    static {
        POINT_TYPES.addElement(INPUT_TYPE, "INPUT",
                "neuralnet.point.type.input");
        POINT_TYPES.addElement(OUTPUT_TYPE, "OUTPUT",
        		"neuralnet.point.type.output");
        POINT_TYPES.addElement(TRAINING_OUTPUT_TYPE, "TRAINING_OUTPUT",
        		"neuralnet.point.type.trainingOutput");
        POINT_TYPES.addElement(TRAINING_INPUT_TYPE, "TRAINING_INPUT",
        		"neuralnet.point.type.trainingInput");
        
    }

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    /**
     * XID prefix for monitors
     */
    public static final String XID_PREFIX = "NEUPT_";
	

    private int pointType = INPUT_TYPE;
    private int dataPointId; //The data point id to use in the real world
    private int trainingDataPointId; //The training data point id
    private int networkId; //The Neural Network Id
    @JsonProperty
    private int delay; //The delay used to offset one point to another
    
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "neuralnet.point.description";
	}
	
	   /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
     */
    @Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        super.jsonWrite(writer);
        writer.writeEntry("pointType", POINT_TYPES.getCode(pointType));
        writer.writeEntry("dataPoint", DataPointDao.instance.getDataPoint(dataPointId).getXid());
        writer.writeEntry("trainingDataPoint", DataPointDao.instance.getDataPoint(trainingDataPointId).getXid());
        writer.writeEntry("network", NeuralNetNetworkDao.instance.get(networkId).getXid());
        
        
    }
    
    /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
     */
    @Override
    public void jsonRead(JsonReader reader, JsonObject jsonObject) throws JsonException {
        super.jsonRead(reader, jsonObject);
        
        String text = jsonObject.getString("pointType");
        if (text == null) {
            throw new TranslatableJsonException("emport.error.missing", "pointType", POINT_TYPES.getCodeList());
        }
        pointType = POINT_TYPES.getId(text);
        if (!POINT_TYPES.isValidId(pointType)) {
            throw new TranslatableJsonException("emport.error.invalid", "pointType", text, POINT_TYPES.getCodeList());
        }

        this.dataPointId = DeltamationCommon.parsePoint(jsonObject, "dataPoint");
        this.trainingDataPointId = DeltamationCommon.parsePoint(jsonObject, "trainingDataPoint");

        String xid = jsonObject.getString("network");
        NeuralNetNetworkVO net = NeuralNetNetworkDao.instance.getByXid(xid);
        if (net == null) {
            throw new TranslatableJsonException("neuralnet.validate.networkMissing", "network", xid);
        }
        this.networkId = net.getId();
        
    }
    
    /* (non-Javadoc)
     * @see com.deltamation.mango.downtime.vo.AbstractVO#validate(com.serotonin.m2m2.i18n.ProcessResult)
     */
    @Override
    public void validate(ProcessResult response) {
        super.validate(response);

        if (!POINT_TYPES.isValidId(pointType)) {
            response.addContextualMessage("pointType", "validate.invalidValue");
        }
        switch(pointType){
        case INPUT_TYPE:
        case TRAINING_INPUT_TYPE:
        	 DeltamationCommon.validatePoint(dataPointId, "dataPointId", response, DataTypes.NUMERIC, false);
        	 DeltamationCommon.validatePoint(trainingDataPointId, "trainingDataPointId", response, DataTypes.NUMERIC, false);
        	break;
        case OUTPUT_TYPE:
        case TRAINING_OUTPUT_TYPE:
        	 DeltamationCommon.validatePoint(dataPointId, "dataPointId", response, DataTypes.NUMERIC, true);
        	 DeltamationCommon.validatePoint(trainingDataPointId, "trainingDataPointId", response, DataTypes.NUMERIC, true);
        	break;
        }
        
        
        NeuralNetNetworkVO net = NeuralNetNetworkDao.instance.get(networkId);
        if (net == null) {
            response.addContextualMessage("networkId", "neuralnet.validate.networkMissing");
        }
        if(delay < 0)
        	response.addContextualMessage("delay","validate.invalidValue");
    }

    /* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addProperties(java.util.List)
     */
    @Override
    public void addProperties(List<TranslatableMessage> list) {
        super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "neuralnet.point.properties.pointType", pointType);
        AuditEventType.addPropertyMessage(list, "neuralnet.point.properties.dataPointId",
                DataPointDao.instance.getDataPoint(dataPointId, false).getName());
        AuditEventType.addPropertyMessage(list, "neuralnet.point.properties.networkId",
                NeuralNetNetworkDao.instance.get(networkId).getName());
        AuditEventType.addPropertyMessage(list, "neuralnet.point.properties.trainingDataPointId",
                DataPointDao.instance.getDataPoint(trainingDataPointId, false).getName());
        AuditEventType.addPropertyMessage(list, "neuralnet.point.properties.delay", delay);
    }

    /* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util.List, java.lang.Object)
     */
    @Override
    public void addPropertyChanges(List<TranslatableMessage> list, NeuralNetPointVO from) {
        super.addPropertyChanges(list, from);
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.point.properties.pointType", from.pointType, pointType);

        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.point.properties.dataPointId",
                DataPointDao.instance.getDataPoint(from.dataPointId, false).getName(),
                DataPointDao.instance.getDataPoint(dataPointId, false).getName());
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.point.properties.networkId",
                NeuralNetNetworkDao.instance.get(from.networkId).getName(),
                NeuralNetNetworkDao.instance.get(networkId).getName());
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.point.properties.trainingDataPointId",
                DataPointDao.instance.getDataPoint(from.trainingDataPointId, false).getName(),
                DataPointDao.instance.getDataPoint(trainingDataPointId, false).getName());
        
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.point.properties.delay", from.delay, delay);
    }

    
    /*
     * Serialization
     */
    
    private static final int version = 1;
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        SerializationHelper.writeSafeUTF(out, name);
        out.writeBoolean(enabled);
        out.writeInt(pointType);
        out.writeInt(dataPointId);
        out.writeInt(networkId);
        out.writeInt(trainingDataPointId);
        out.writeInt(delay);
    }
    
    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            name = SerializationHelper.readSafeUTF(in);
            enabled = in.readBoolean();
            pointType = in.readInt();
            dataPointId = in.readInt();
            networkId = in.readInt();
            trainingDataPointId = in.readInt();
            delay = in.readInt();
        }
        else {
            throw new ShouldNeverHappenException("Unknown serialization version.");
        }
    }



	/* Getters and Setters */

	public int getPointType() {
		return pointType;
	}
	public void setPointType(int type) {
		this.pointType = type;
	}
	public int getDataPointId() {
		return dataPointId;
	}
	public void setDataPointId(int dataPointId) {
		this.dataPointId = dataPointId;
	}
	public int getNetworkId() {
		return networkId;
	}
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public int getTrainingDataPointId() {
		return trainingDataPointId;
	}
	public void setTrainingDataPointId(int trainingDataPointId) {
		this.trainingDataPointId = trainingDataPointId;
	}

	/* JSP Helpers */
	public String getPointTypeString(){
		TranslatableMessage msg = new TranslatableMessage(POINT_TYPES.getKey(pointType));
		return msg.translate(Common.getTranslations());
	}
	public void setPointTypeString(String s){
		//No Op
	}
	public String getDataPointName(){
		DataPointVO vo = DataPointDao.instance.get(this.dataPointId);
		if(vo != null)
			return vo.getName();
		else
			return "Data Point Missing"; //TODO translate this
	}
	public void setDataPointName(String name){
		//No Op
	}
}
