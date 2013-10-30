/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.rt;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.controltoolbox.dao.ControlToolboxControllerDao;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.serotonin.m2m2.rt.AbstractRTM;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxControllerRTMDefinition extends AbstractRTM<ControlToolboxControllerVO,ControlToolboxControllerRT,ControlToolboxControllerDao>{

    public static ControlToolboxControllerRTMDefinition instance;
    
    public ControlToolboxControllerRTMDefinition(int initializationPriority) {
        super(initializationPriority);
        instance = this;
        LOG = LogFactory.getLog(ControlToolboxControllerRTMDefinition.class);
    }
    
    public ControlToolboxControllerRTMDefinition(){
    	this(10);
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.AbstractRTM#getRt(com.serotonin.m2m2.vo.AbstractActionVO)
	 */
	@Override
	public ControlToolboxControllerRT getRt(ControlToolboxControllerVO vo) {
		return new ControlToolboxControllerRT(vo);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.AbstractRTM#getDao()
	 */
	@Override
	public ControlToolboxControllerDao getDao() {
		return ControlToolboxControllerDao.instance;
	}

	
	
}
