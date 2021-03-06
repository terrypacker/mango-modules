/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.infiniteautomation.mango.rest.v2;

import org.springframework.stereotype.Component;

import com.infiniteautomation.mango.rest.v2.model.AcuriteDataSourceModel;
import com.infiniteautomation.mango.rest.v2.model.RestModelJacksonMapping;
import com.infiniteautomation.mango.rest.v2.model.RestModelMapper;
import com.serotonin.m2m2.vo.User;
import com.terrypacker.acurite.AcuriteDataSourceDefinition;
import com.terrypacker.acurite.AcuriteDataSourceVO;

/**
 * @author Terry Packer
 *
 */
@Component
public class AcuriteDataSourceModelMapping implements RestModelJacksonMapping<AcuriteDataSourceVO, AcuriteDataSourceModel> {

    @Override
    public Class<? extends AcuriteDataSourceVO> fromClass() {
        return AcuriteDataSourceVO.class;
    }

    @Override
    public Class<? extends AcuriteDataSourceModel> toClass() {
        return AcuriteDataSourceModel.class;
    }

    @Override
    public AcuriteDataSourceModel map(Object from, User user, RestModelMapper mapper) {
        return new AcuriteDataSourceModel((AcuriteDataSourceVO)from);
    }

    @Override
    public String getTypeName() {
        return AcuriteDataSourceDefinition.TYPE_NAME;
    }

}
