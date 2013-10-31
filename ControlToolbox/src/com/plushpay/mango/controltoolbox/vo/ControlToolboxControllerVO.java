/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.vo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxAlgorithmDao;
import com.serotonin.ShouldNeverHappenException;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.type.AuditEventType;
import com.serotonin.m2m2.vo.AbstractActionVO;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxControllerVO extends AbstractActionVO<ControlToolboxControllerVO>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    /**
     * XID prefix
     */
    public static final String XID_PREFIX = "CTC_";
    
    private int algorithmId = 0;
    
    /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonWrite(com.serotonin.json.ObjectWriter)
     */
    @Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        super.jsonWrite(writer);
        writer.writeEntry("algorithm", ControlToolboxAlgorithmDao.instance.get(algorithmId).getXid());
    }
    
    /* (non-Javadoc)
     * @see com.serotonin.json.spi.JsonSerializable#jsonRead(com.serotonin.json.JsonReader, com.serotonin.json.type.JsonObject)
     */
    @Override
    public void jsonRead(JsonReader reader, JsonObject jsonObject) throws JsonException {
        super.jsonRead(reader, jsonObject);  
        String xid = jsonObject.getString("algorithm");
        ControlToolboxAlgorithmVO net = ControlToolboxAlgorithmDao.instance.getByXid(xid);
        if (net == null) {
            throw new TranslatableJsonException("controltoolbox.validate.algorithmMissing", "algorithm", xid);
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.deltamation.mango.downtime.vo.AbstractVO#validate(com.serotonin.m2m2.i18n.ProcessResult)
     */
    @Override
    public void validate(ProcessResult response) {
        super.validate(response);
              
        ControlToolboxAlgorithmVO net = ControlToolboxAlgorithmDao.instance.get(algorithmId);
        if (net == null) {
            response.addContextualMessage("algorithmId", "controltoolbox.validate.algorithmMissing");
        }

        
    }
    
	/* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addProperties(java.util.List)
     */
    @Override
    public void addProperties(List<TranslatableMessage> list) {
        super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "controltoolbox.controller.properties.algorithmId",
                ControlToolboxAlgorithmDao.instance.get(algorithmId).getName());        

    }
    
    /* (non-Javadoc)
     * @see com.serotonin.m2m2.util.ChangeComparable#addPropertyChanges(java.util.List, java.lang.Object)
     */
    @Override
    public void addPropertyChanges(List<TranslatableMessage> list, ControlToolboxControllerVO from) {
        super.addPropertyChanges(list, from);
        AuditEventType.maybeAddPropertyChangeMessage(list, "controltoolbox.controller.properties.algorithmId",
        		ControlToolboxAlgorithmDao.instance.get(from.algorithmId).getName(),
        		ControlToolboxAlgorithmDao.instance.get(algorithmId).getName());
    }
    
    /*
     * Serialization
     */
    
    private static final int version = 1;
    
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.writeInt(version);
        SerializationHelper.writeSafeUTF(out, name);
        out.writeBoolean(enabled);
        out.writeInt(algorithmId);
    }
    
    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {            
        	name = SerializationHelper.readSafeUTF(in);
            enabled = in.readBoolean();
            algorithmId = in.readInt();
        }
        else {
            throw new ShouldNeverHappenException("Unknown serialization version.");
        }
    }
    
	/*
	 * Helper methods
	 */
    
    
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "controltoolbox.controller.description";
	}
	
	/* Getters and Setters */
	
	public int getAlgorithmId() {
		return algorithmId;
	}
	public void setAlgorithmId(int algorithmId) {
		this.algorithmId = algorithmId;
	}


}
