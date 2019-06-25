/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.terrypacker.acurite;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.module.license.DataSourceTypePointsLimit;
import com.serotonin.m2m2.rt.dataSource.DataSourceRT;
import com.serotonin.m2m2.util.ExportCodes;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;
import com.serotonin.m2m2.vo.dataSource.PointLocatorVO;
import com.serotonin.m2m2.vo.dataSource.PollingDataSourceVO;
import com.serotonin.m2m2.vo.event.EventTypeVO;

/**
 * @author Terry Packer
 *
 */
public class AcuriteDataSourceVO extends PollingDataSourceVO<AcuriteDataSourceVO> {

    private static final ExportCodes EVENT_CODES = new ExportCodes();
    static {
        EVENT_CODES.addElement(AcuriteDataSourceRT.POLL_ABORTED_EVENT, POLL_ABORTED);
        EVENT_CODES.addElement(AcuriteDataSourceRT.USB_DEVICE_NOT_FOUND_EVENT, "USB_DEVICE_NOT_FOUND_EVENT");
        EVENT_CODES.addElement(AcuriteDataSourceRT.USB_COMMUNICATION_ERROR_EVENT, "USB_COMMUNICATION_ERROR_EVENT");
        EVENT_CODES.addElement(AcuriteDataSourceRT.DATA_SOURCE_ERROR, "DATA_SOURCE_ERROR");
        EVENT_CODES.addElement(AcuriteDataSourceRT.NO_SENSOR_FOUND_EVENT, "NO_SENSOR_FOUND_EVENT");
        EVENT_CODES.addElement(AcuriteDataSourceRT.INVALID_RESPONSE_EVENT, "INVALID_RESPONSE_EVENT");
        EVENT_CODES.addElement(AcuriteDataSourceRT.LOW_BATTERY_EVENT, "LOW_BATTERY_EVENT");
        EVENT_CODES.addElement(AcuriteDataSourceRT.LOW_SIGNAL_EVENT, "LOW_SIGNAL_EVENT");
    }
    
    @Override
    protected void addEventTypes(List<EventTypeVO> ets) {
        super.addEventTypes(ets);
        ets.add(createEventType(AcuriteDataSourceRT.USB_DEVICE_NOT_FOUND_EVENT, new TranslatableMessage( "acurite.event.description.deviceNotFound")));
        ets.add(createEventType(AcuriteDataSourceRT.USB_COMMUNICATION_ERROR_EVENT, new TranslatableMessage( "acurite.event.description.usbCommsError")));
        ets.add(createEventType(AcuriteDataSourceRT.DATA_SOURCE_ERROR, new TranslatableMessage( "acurite.event.description.dataSourceException")));
        ets.add(createEventType(AcuriteDataSourceRT.NO_SENSOR_FOUND_EVENT, new TranslatableMessage( "acurite.event.description.noSensorsFound")));
        ets.add(createEventType(AcuriteDataSourceRT.INVALID_RESPONSE_EVENT, new TranslatableMessage( "acurite.event.description.invalidResponseFormat")));
        ets.add(createEventType(AcuriteDataSourceRT.LOW_BATTERY_EVENT, new TranslatableMessage( "acurite.event.description.lowBattery")));
        ets.add(createEventType(AcuriteDataSourceRT.LOW_SIGNAL_EVENT, new TranslatableMessage( "acurite.event.description.lowSignal")));
    }
    
    @Override
    public int getPollAbortedExceptionEventId() {
        return AcuriteDataSourceRT.POLL_ABORTED_EVENT;
    }

    @Override
    public TranslatableMessage getConnectionDescription() {
        return Common.getPeriodDescription(updatePeriodType, updatePeriods);
    }

    @Override
    public PointLocatorVO<?> createPointLocator() {
        return new AcuritePointLocatorVO();
    }

    @Override
    public DataSourceRT<? extends DataSourceVO<?>> createDataSourceRT() {
        return new AcuriteDataSourceRT(this);
    }

    @Override
    public ExportCodes getEventCodes() {
        return EVENT_CODES;
    }

    @Override
    public void validate(ProcessResult response) {
        super.validate(response);
        DataSourceTypePointsLimit.checkLimit(AcuriteDataSourceDefinition.TYPE_NAME, response);
    }
    /*
     * Serialization 
     */
    private static final long serialVersionUID = 1L;
    private static final int version = 1;
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        in.readInt();
    }
    
}
