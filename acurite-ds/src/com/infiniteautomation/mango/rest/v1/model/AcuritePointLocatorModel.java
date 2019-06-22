/**
 * Copyright (C) 2019  Infinite Automation Software. All rights reserved.
 */
package com.infiniteautomation.mango.rest.v1.model;

import com.serotonin.m2m2.web.mvc.rest.v1.model.dataPoint.PointLocatorModel;
import com.terrypacker.acurite.AcuritePointLocatorVO;
import com.terrypacker.acurite.WeatherMeasurement;

/**
 * @author Terry Packer
 *
 */
public class AcuritePointLocatorModel extends PointLocatorModel<AcuritePointLocatorVO> {

    public static final String TYPE_NAME = "PL.ACURITE";
    
    public AcuritePointLocatorModel() {
        super(new AcuritePointLocatorVO());
    }
    
    public AcuritePointLocatorModel(AcuritePointLocatorVO data) {
        super(data);
    }
    
    public WeatherMeasurement getMeasurement() {
        return this.data.getMeasurement();
    }
    
    public void setMeasurement(WeatherMeasurement measurement) {
        this.data.setMeasurement(measurement);
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

}
