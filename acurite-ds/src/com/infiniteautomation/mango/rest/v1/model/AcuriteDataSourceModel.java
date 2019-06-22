/**
 * Copyright (C) 2019  Infinite Automation Software. All rights reserved.
 */
package com.infiniteautomation.mango.rest.v1.model;

import com.serotonin.m2m2.web.mvc.rest.v1.model.dataSource.AbstractPollingDataSourceModel;
import com.terrypacker.acurite.AcuriteDataSourceDefinition;
import com.terrypacker.acurite.AcuriteDataSourceVO;

/**
 * @author Terry Packer
 *
 */
public class AcuriteDataSourceModel extends AbstractPollingDataSourceModel<AcuriteDataSourceVO> {

    public AcuriteDataSourceModel() { }
    
    public AcuriteDataSourceModel(AcuriteDataSourceVO vo) {
        super(vo);
    }
    
    @Override
    public String getModelType() {
        return AcuriteDataSourceDefinition.TYPE_NAME;
    }

}
