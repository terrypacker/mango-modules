package com.plushpay.mango.neuralnet.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import com.plushpay.mango.neuralnet.NeuralNetNetworkAuditEventTypeDefinition;
import com.plushpay.mango.neuralnet.db.SchemaDefinition;
import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.serotonin.m2m2.db.dao.AbstractDao;

/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */

/**
 * @author Terry Packer
 *
 */
public class NeuralNetNetworkDao extends AbstractDao<NeuralNetNetworkVO>{

    public static final NeuralNetNetworkDao instance = new NeuralNetNetworkDao();
    
    private NeuralNetNetworkDao() {
        super(NeuralNetNetworkAuditEventTypeDefinition.TYPE_NAME);
        LOG = LogFactory.getLog(NeuralNetNetworkDao.class);
    }
    
    /* (non-Javadoc)
     * @see com.deltamation.mango.downtime.db.GenericDao#getProperties()
     */
    @Override
    protected List<String> getProperties() {
        return Arrays.asList(
                "id",
                "xid",
                "name",
                "enabled",
                "transferFunctionType",
                "learningRate",
                "maxError",
                "learningMaxIterations",
                "trainingPeriodStart",
                "trainingPeriodEnd",
                "properties"
                );
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return SchemaDefinition.NEURAL_NETWORKS_TABLE;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getXidPrefix()
	 */
	@Override
	protected String getXidPrefix() {
		return NeuralNetNetworkVO.XID_PREFIX;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#voToObjectArray(com.serotonin.m2m2.vo.AbstractVO)
	 */
	@Override
	protected Object[] voToObjectArray(NeuralNetNetworkVO vo) {
		return new Object[]{
			vo.getXid(),
			vo.getName(),
			boolToChar(vo.isEnabled()),
			vo.getTransferFunctionType(),
			vo.getLearningRate(),
			vo.getMaxError(),
			vo.getLearningMaxIterations(),
			vo.getTrainingPeriodStart(),
			vo.getTrainingPeriodEnd(),
			vo.getPropertiesString()
		};
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getNewVo()
	 */
	@Override
	public NeuralNetNetworkVO getNewVo() {
		NeuralNetNetworkVO vo =  new NeuralNetNetworkVO();
		vo.setXid(this.generateUniqueXid());
		return vo;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getPropertiesMap()
	 */
	@Override
	protected Map<String, String> getPropertiesMap() {
		return new HashMap<String,String>();
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getRowMapper()
	 */
	@Override
	public RowMapper<NeuralNetNetworkVO> getRowMapper() {
		return new NeuralNetNetworkRowMapper();
	}
	
    class NeuralNetNetworkRowMapper implements RowMapper<NeuralNetNetworkVO> {
        @Override
        public NeuralNetNetworkVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            NeuralNetNetworkVO net = new NeuralNetNetworkVO();
            net.setId(rs.getInt(++i));
            net.setXid(rs.getString(++i));
            net.setName(rs.getString(++i));
            net.setEnabled(charToBool(rs.getString(++i)));
            net.setTransferFunctionType(rs.getInt(++i));
            net.setLearningRate(rs.getDouble(++i));
            net.setMaxError(rs.getDouble(++i));
            net.setLearningMaxIterations(rs.getInt(++i));
            net.setTrainingPeriodStart(rs.getLong(++i));
            net.setTrainingPeriodEnd(rs.getLong(++i));
            net.setPropertiesString(rs.getString(++i));
            return net;
        }
    }


}
