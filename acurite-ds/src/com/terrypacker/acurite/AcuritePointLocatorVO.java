/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.terrypacker.acurite;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonSerializable;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.DataTypes;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataSource.PointLocatorRT;
import com.serotonin.m2m2.vo.DataPointVO;
import com.serotonin.m2m2.vo.dataSource.AbstractPointLocatorVO;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;

/**
 * @author Terry Packer
 *
 */
public class AcuritePointLocatorVO extends AbstractPointLocatorVO<AcuritePointLocatorVO> implements JsonSerializable{

    
    private WeatherMeasurement measurement;
    
    @Override
    public int getDataTypeId() {
        return DataTypes.NUMERIC;
    }

    @Override
    public TranslatableMessage getConfigurationDescription() {
        return measurement.getDescription();
    }

    @Override
    public boolean isSettable() {
        return false;
    }

    @Override
    public PointLocatorRT<AcuritePointLocatorVO> createRuntime() {
        return new AcuritePointLocatorRT(this);
    }

    @Override
    public void validate(ProcessResult response, DataPointVO dpvo, DataSourceVO<?> dsvo) {
        if(measurement == null)
            response.addContextualMessage("measurement", "validate.required");
    }

    public WeatherMeasurement getMeasurement() {
        return measurement;
    }
    
    public void setMeasurement(WeatherMeasurement measurement) {
        this.measurement = measurement;
    }
    
    //
    //
    // Serialization
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeInt(measurement.value());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            measurement = WeatherMeasurement.fromValue(in.readInt());
        }
    }
    
    @Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        writer.writeEntry("measurement", measurement.name());
    }

    @Override
    public void jsonRead(JsonReader reader, JsonObject jsonObject) throws JsonException {
        String text = jsonObject.getString("measurement");
        if(text == null)
            throw new TranslatableJsonException("emport.error.missing", "measurement", WeatherMeasurement.values());
        else 
            measurement = WeatherMeasurement.fromName(text);
    }

}
