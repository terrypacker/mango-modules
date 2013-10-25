/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.rt;

import org.apache.commons.logging.LogFactory;

import com.plushpay.mango.neuralnet.dao.NeuralNetNetworkDao;
import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.serotonin.m2m2.rt.AbstractRTM;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetNetworkRTMDefinition extends AbstractRTM<NeuralNetNetworkVO,NeuralNetNetworkRT,NeuralNetNetworkDao>{

    public static NeuralNetNetworkRTMDefinition instance;
    
    public NeuralNetNetworkRTMDefinition(int initializationPriority) {
        super(initializationPriority);
        instance = this;
        LOG = LogFactory.getLog(NeuralNetNetworkRTMDefinition.class);
    }
    
    public NeuralNetNetworkRTMDefinition(){
    	this(10);
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.AbstractRTM#getRt(com.serotonin.m2m2.vo.AbstractActionVO)
	 */
	@Override
	public NeuralNetNetworkRT getRt(NeuralNetNetworkVO vo) {
		return new NeuralNetNetworkRT(vo);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.rt.AbstractRTM#getDao()
	 */
	@Override
	public NeuralNetNetworkDao getDao() {
		return NeuralNetNetworkDao.instance;
	}

	
	
}
