/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.infiniteautomation.mango.rest.v2;

import org.springframework.stereotype.Component;

import com.infiniteautomation.mango.rest.v1.model.AcuritePointLocatorModel;
import com.infiniteautomation.mango.rest.v2.model.RestModelJacksonMapping;
import com.infiniteautomation.mango.rest.v2.model.RestModelMapper;
import com.serotonin.m2m2.vo.User;
import com.terrypacker.acurite.AcuritePointLocatorVO;


/**
 * This class is used for the v2 Model Mapper but replicates the 
 * functionality of the v1 mapper exactly
 * @author Terry Packer
 *
 */
@Component
public class AcuritePointLocatorModelMapping implements RestModelJacksonMapping<AcuritePointLocatorVO, AcuritePointLocatorModel> {

    @Override
    public Class<? extends AcuritePointLocatorVO> fromClass() {
        return AcuritePointLocatorVO.class;
    }

    @Override
    public Class<? extends AcuritePointLocatorModel> toClass() {
        return AcuritePointLocatorModel.class;
    }

    @Override
    public AcuritePointLocatorModel map(Object from, User user, RestModelMapper mapper) {
        return new AcuritePointLocatorModel((AcuritePointLocatorVO)from);
    }

    @Override
    public String getTypeName() {
        return AcuritePointLocatorModel.TYPE_NAME;
    }

}
