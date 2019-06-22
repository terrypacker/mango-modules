/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.terrypacker.acurite;

import com.serotonin.m2m2.rt.dataSource.PointLocatorRT;

/**
 * @author Terry Packer
 *
 */
public class AcuritePointLocatorRT extends PointLocatorRT<AcuritePointLocatorVO> {

    public AcuritePointLocatorRT(AcuritePointLocatorVO vo) {
        super(vo);
    }

    public int getReportType() {
        switch(vo.getMeasurement()) {
            case HUMIDITY:
                return AcuriteDataSourceRT.REPORT_1;
            case RAIN:
                return AcuriteDataSourceRT.REPORT_1;
            case TEMPERATURE_INSIDE:
                return AcuriteDataSourceRT.REPORT_2;
            case TEMPERATURE_OUTSIDE:
                return AcuriteDataSourceRT.REPORT_1;
            case WIND_DIRECTION:
                return AcuriteDataSourceRT.REPORT_1;
            case WIND_SPEED:
                return AcuriteDataSourceRT.REPORT_1;
            case PRESSURE:
                return AcuriteDataSourceRT.REPORT_2;
            default:
                return -1;
        }
    }
    
}
