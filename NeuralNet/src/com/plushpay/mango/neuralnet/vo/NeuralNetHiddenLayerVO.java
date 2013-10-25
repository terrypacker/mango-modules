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
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.type.AuditEventType;
import com.serotonin.m2m2.vo.AbstractVO;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 * 
 */
public class NeuralNetHiddenLayerVO extends AbstractVO<NeuralNetHiddenLayerVO> {

	public static final String XID_PREFIX = "NEUHL_";

	@JsonProperty
	int layerNumber; // Starting from 0 being the input layer and then 1 being
						// the first hidden layer
	@JsonProperty
	int numberOfNeurons;
	int networkId;

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
		writer.writeEntry("network", NeuralNetNetworkDao.instance
				.get(networkId).getXid());

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

		String xid = jsonObject.getString("network");
		NeuralNetNetworkVO net = NeuralNetNetworkDao.instance.getByXid(xid);
		if (net == null) {
			throw new TranslatableJsonException(
					"neuralnet.validate.networkMissing", "network", xid);
		}

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

		// Can't be before the inputs
		if (layerNumber < 1)
			response.addContextualMessage("layerNumber",
					"validate.invalidValue");

		// Can't be before the inputs
		if (numberOfNeurons < 1)
			response.addContextualMessage("numberOfNeurons",
					"validate.invalidValue");

		NeuralNetNetworkVO net = NeuralNetNetworkDao.instance.get(networkId);
		if (net == null) {
			response.addContextualMessage("networkId",
					"neuralnet.validate.networkMissing");
		}
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
		AuditEventType.addPropertyMessage(list,
				"neuralnet.hiddenlayer.properties.layerNumber", layerNumber);
		AuditEventType.addPropertyMessage(list,
				"neuralnet.hiddenlayer.properties.numberOfNeurons",
				numberOfNeurons);
		AuditEventType.addPropertyMessage(list,
				"neuralnet.hiddenlayer.properties.networkId",
				NeuralNetNetworkDao.instance.get(networkId).getName());
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
			NeuralNetHiddenLayerVO from) {
		super.addPropertyChanges(list, from);
		AuditEventType.maybeAddPropertyChangeMessage(list,
				"neuralnet.hiddenlayer.properties.layerNumber",
				from.layerNumber, layerNumber);
		AuditEventType.maybeAddPropertyChangeMessage(list,
				"neuralnet.hiddenlayer.properties.numberOfNeurons",
				from.numberOfNeurons, numberOfNeurons);

		AuditEventType.maybeAddPropertyChangeMessage(list,
				"neuralnet.hiddenlayer.properties.networkId",
				NeuralNetNetworkDao.instance.get(from.networkId).getName(),
				NeuralNetNetworkDao.instance.get(networkId).getName());
	}

	/*
	 * Serialization
	 */

	private static final int version = 1;

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(version);
		SerializationHelper.writeSafeUTF(out, name);
		out.writeInt(layerNumber);
		out.writeInt(numberOfNeurons);
		out.writeInt(networkId);
	}

	private void readObject(ObjectInputStream in) throws IOException {
		int ver = in.readInt();

		// Switch on the version of the class so that version changes can be
		// elegantly handled.
		if (ver == 1) {
			name = SerializationHelper.readSafeUTF(in);
			layerNumber = in.readInt();
			numberOfNeurons = in.readInt();
			networkId = in.readInt();
		} else {
			throw new ShouldNeverHappenException(
					"Unknown serialization version.");
		}
	}

	/* JSP Page Helpers */
	public String getNetworkName(){
		NeuralNetNetworkVO network = NeuralNetNetworkDao.instance.get(this.networkId);
		if(network != null)
			return network.getName();
		else
			return "Network Missing"; //TODO Translate this
	}
	
	public void setNetworkName(String name){
		//No Op
	}
	
	
	/* Getters and Setters */
	public int getLayerNumber() {
		return layerNumber;
	}

	public void setLayerNumber(int layerNumber) {
		this.layerNumber = layerNumber;
	}

	public int getNumberOfNeurons() {
		return numberOfNeurons;
	}

	public void setNumberOfNeurons(int numberOfNeurons) {
		this.numberOfNeurons = numberOfNeurons;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.serotonin.m2m2.util.ChangeComparable#getTypeKey()
	 */
	@Override
	public String getTypeKey() {
		return "neuralnet.hiddenlayer.description";
	}

}
