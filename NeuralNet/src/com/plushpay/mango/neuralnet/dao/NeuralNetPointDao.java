/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
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
import com.plushpay.mango.neuralnet.NeuralNetPointAuditEventTypeDefinition;
import com.plushpay.mango.neuralnet.db.SchemaDefinition;
import com.plushpay.mango.neuralnet.vo.NeuralNetNetworkVO;
import com.plushpay.mango.neuralnet.vo.NeuralNetPointVO;
import com.serotonin.m2m2.db.dao.AbstractDao;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetPointDao extends AbstractDao<NeuralNetPointVO>{

    public static final NeuralNetPointDao instance = new NeuralNetPointDao();
    
    private NeuralNetPointDao() {
        super(NeuralNetPointAuditEventTypeDefinition.TYPE_NAME);
        LOG = LogFactory.getLog(NeuralNetNetworkDao.class);
    }


	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return SchemaDefinition.NEURAL_POINTS_TABLE;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getXidPrefix()
	 */
	@Override
	protected String getXidPrefix() {
		return NeuralNetPointVO.XID_PREFIX;
	}
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#voToObjectArray(com.serotonin.m2m2.vo.AbstractVO)
	 */
	@Override
	protected Object[] voToObjectArray(NeuralNetPointVO vo) {
		return new Object[]{
				vo.getXid(),
				vo.getName(),
				boolToChar(vo.isEnabled()),
				vo.getPointType(),
				vo.getDelay(),
				vo.getDataPointId(),
				vo.getTrainingDataPointId(),
				vo.getNetworkId()
			};
	}




	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getNewVo()
	 */
	@Override
	public NeuralNetPointVO getNewVo() {
		NeuralNetPointVO vo =  new NeuralNetPointVO();
		vo.setXid(this.generateUniqueXid());
		return vo;
	}




	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getProperties()
	 */
    @Override
    protected List<String> getProperties() {
        return Arrays.asList(
                "id",
                "xid",
                "name",
                "enabled",
                "pointType",
                "delay",
                "dataPointId",
                "trainingDataPointId",
                "networkId"
                );
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
	public RowMapper<NeuralNetPointVO> getRowMapper() {
		return new NeuralNetPointRowMapper();
	}
	
    class NeuralNetPointRowMapper implements RowMapper<NeuralNetPointVO> {
        @Override
        public NeuralNetPointVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            NeuralNetPointVO pt = new NeuralNetPointVO();
            pt.setId(rs.getInt(++i));
            pt.setXid(rs.getString(++i));
            pt.setName(rs.getString(++i));
            pt.setEnabled(charToBool(rs.getString(++i)));
            pt.setPointType(rs.getInt(++i));
            pt.setDelay(rs.getInt(++i));
            pt.setDataPointId(rs.getInt(++i));
            pt.setTrainingDataPointId(rs.getInt(++i));
            pt.setNetworkId(rs.getInt(++i));
            return pt;
        }
    }
	
	
	/**
	 * @param inputType
	 * @param id
	 */
	public List<NeuralNetPointVO> getNetworkPoints(int inputType, int networkId) {
		return query(SELECT_ALL + " WHERE pointType = ? AND networkId = ? ORDER BY delay",new Object[]{inputType,networkId}, new NeuralNetPointRowMapper());
	}

}
