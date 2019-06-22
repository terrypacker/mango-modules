/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.infiniteautomation.mango.rest.v2.model;

import com.infiniteautomation.mango.rest.v2.model.datasource.AbstractPollingDataSourceModel;
import com.terrypacker.acurite.AcuriteDataSourceDefinition;
import com.terrypacker.acurite.AcuriteDataSourceVO;

/**
 * @author Terry Packer
 *
 */
public class AcuriteDataSourceModel extends AbstractPollingDataSourceModel<AcuriteDataSourceVO>{

    public AcuriteDataSourceModel() {
        super();
    }
    
    public AcuriteDataSourceModel(AcuriteDataSourceVO data) {
        fromVO(data);
    }

    @Override
    public String getModelType() {
        return AcuriteDataSourceDefinition.TYPE_NAME;
    }
    
    @Override
    public AcuriteDataSourceVO toVO() {
        AcuriteDataSourceVO vo = super.toVO();
        return vo;
    }
    
    @Override
    public void fromVO(AcuriteDataSourceVO vo) {
        super.fromVO(vo);
    }
}
