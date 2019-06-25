/**
 * Copyright (C) 2019  Infinite Automation Software. All rights reserved.
 */
package com.terrypacker.acurite;

import com.serotonin.m2m2.module.DataSourceDefinition;
import com.serotonin.m2m2.module.ModuleRegistry;
import com.serotonin.m2m2.module.license.DataSourceTypePointsLimit;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;

/**
 * @author Terry Packer
 *
 */
public class AcuriteDataSourceDefinition extends DataSourceDefinition {
    
    public static final String TYPE_NAME = "ACURITE";
    
    @Override
    public void preInitialize(boolean install, boolean upgrade) { 
        ModuleRegistry.addLicenseEnforcement(new DataSourceTypePointsLimit(getModule().getName(), TYPE_NAME, 2, null));
    }
    
    @Override
    public String getDataSourceTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getDescriptionKey() {
        return "acurite.description";
    }

    @Override
    public DataSourceVO<?> createDataSourceVO() {
        return new AcuriteDataSourceVO();
    }

    @Override
    public String getEditPagePath() {
        return null;
    }

    @Override
    public Class<?> getDwrClass() {
        return null;
    }
}
