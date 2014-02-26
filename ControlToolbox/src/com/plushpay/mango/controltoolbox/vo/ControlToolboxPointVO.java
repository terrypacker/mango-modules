/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxControllerDao;
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
public class ControlToolboxPointVO extends AbstractActionVO<ControlToolboxPointVO>{

	private static final double MIN_VALUE = -1.79769E+308; //DERBY DOESN't SUPPORT Double.MIN_VALUE; Gay
	private static final double MAX_VALUE = 1.79769E+308; //DERBY Doesn't support Double.MAX_VALUE; Gay
	
	public static final int INPUT_TYPE = 1;
	public static final int OUTPUT_TYPE = 2;
	public static final int SETPOINT_TYPE = 3;
	public static final int SYSTEM_OUTPUT_TYPE = 4;
	
    public static final ExportCodes POINT_TYPES = new ExportCodes();
    static {
        POINT_TYPES.addElement(INPUT_TYPE, "INPUT",
                "controltoolbox.point.type.input");
        POINT_TYPES.addElement(OUTPUT_TYPE, "OUTPUT",
        		"controltoolbox.point.type.output");
        POINT_TYPES.addElement(SETPOINT_TYPE, "SETPOINT",
        		"controltoolbox.point.type.setpoint");
        POINT_TYPES.addElement(SYSTEM_OUTPUT_TYPE, "SYSTEM_OUTPUT",
        		"controltoolbox.point.type.systemOutput");
        
        
    }

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    /**
     * XID prefix for monitors
     */
    public static final String XID_PREFIX = "CTP_";
	

    private int pointType = INPUT_TYPE;
    private int dataPointId; //The data point id to use in the real world
    private int controllerId; //The Controller Id
    @JsonProperty
    private int delay; //The delay used to offset a point in time
	@JsonProperty
	private double highLimit = MAX_VALUE;  //Maximum limit for input/output value (will be trimmed to this)
	@JsonProperty
	private double lowLimit =  MIN_VALUE; //Minimum limit for input/output value (will be trimmed to this)
    
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "controltoolbox.point.description";
	}
	
	   /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
     */
    @Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        super.jsonWrite(writer);
        writer.writeEntry("pointType", POINT_TYPES.getCode(pointType));
        writer.writeEntry("dataPoint", DataPointDao.instance.getDataPoint(dataPointId).getXid());
        writer.writeEntry("controller", ControlToolboxControllerDao.instance.get(controllerId).getXid());
        
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
 
        String xid = jsonObject.getString("controller");
        ControlToolboxControllerVO net = ControlToolboxControllerDao.instance.getByXid(xid);
        if (net == null) {
            throw new TranslatableJsonException("neuralnet.validate.networkMissing", "controller", xid);
        }
        this.controllerId = net.getId();
        
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
        	 DeltamationCommon.validatePoint(dataPointId, "dataPointId", response, DataTypes.NUMERIC, false);
        	break;
        case OUTPUT_TYPE:
        	 DeltamationCommon.validatePoint(dataPointId, "dataPointId", response, DataTypes.NUMERIC, true);
        	break;
        }
        
        
        ControlToolboxControllerVO net = ControlToolboxControllerDao.instance.get(controllerId);
        if (net == null) {
            response.addContextualMessage("controllerId", "controltoolbox.validate.controllerMissing");
        }
        if(delay < 0)
        	response.addContextualMessage("delay","validate.invalidValue");

    	if(highLimit <= lowLimit){
    		response.addContextualMessage("highLimit","validate.invalidValue");
    		response.addContextualMessage("lowLimit","validate.invalidValue");
    	}
    	if(highLimit > MAX_VALUE)
    		response.addContextualMessage("highLimit","validate.invalidValue");
    	if(lowLimit < MIN_VALUE)
    		response.addContextualMessage("lowLimit","validate.invalidValue");
        
    }

    /* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addProperties(java.util.List)
     */
    @Override
    public void addProperties(List<TranslatableMessage> list) {
        super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "controltoolbox.point.properties.pointType", pointType);
        AuditEventType.addPropertyMessage(list, "controltoolbox.point.properties.dataPointId",
                DataPointDao.instance.getDataPoint(dataPointId, false).getName());
        AuditEventType.addPropertyMessage(list, "controltoolbox.point.properties.networkId",
                ControlToolboxControllerDao.instance.get(controllerId).getName());
        AuditEventType.addPropertyMessage(list, "controltoolbox.point.properties.delay", delay);
        AuditEventType.addPropertyMessage(list, "controltoolbox.point.properties.highLimit", highLimit);
        AuditEventType.addPropertyMessage(list, "controltoolbox.point.properties.lowLimit", lowLimit);
    }

    /* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util.List, java.lang.Object)
     */
    @Override
    public void addPropertyChanges(List<TranslatableMessage> list, ControlToolboxPointVO from) {
        super.addPropertyChanges(list, from);
        AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.point.properties.pointType", from.pointType, pointType);

        AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.point.properties.dataPointId",
                DataPointDao.instance.getDataPoint(from.dataPointId, false).getName(),
                DataPointDao.instance.getDataPoint(dataPointId, false).getName());
        AuditEventType.maybeAddPropertyChangeMessage(list, "neuralnet.point.properties.networkId",
                ControlToolboxControllerDao.instance.get(from.controllerId).getName(),
                ControlToolboxControllerDao.instance.get(controllerId).getName());
        
        AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.point.properties.delay", from.delay, delay);
        AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.point.properties.highLimit", from.highLimit, highLimit);
        AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.point.properties.lowLimit", from.lowLimit, lowLimit);
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
        out.writeInt(controllerId);
        out.writeInt(delay);
        out.writeDouble(highLimit);
        out.writeDouble(lowLimit);
    }
    
    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            name = SerializationHelper.readSafeUTF(in);
            enabled = in.readBoolean();
            pointType = in.readInt();
            dataPointId = in.readInt();
            controllerId = in.readInt();
            delay = in.readInt();
            highLimit = in.readDouble();
            lowLimit = in.readDouble();
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
	public int getControllerId() {
		return controllerId;
	}
	public void setControllerId(int id) {
		this.controllerId = id;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public double getHighLimit() {
		return highLimit;
	}
	public void setHighLimit(double highLimit) {
		this.highLimit = highLimit;
	}
	public double getLowLimit() {
		return lowLimit;
	}
	public void setLowLimit(double lowLimit) {
		this.lowLimit = lowLimit;
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
