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

import com.plushpay.mango.neuralnet.NeuralNetHiddenLayerAuditEventTypeDefinition;
import com.plushpay.mango.neuralnet.db.SchemaDefinition;
import com.plushpay.mango.neuralnet.vo.NeuralNetHiddenLayerVO;
import com.serotonin.m2m2.db.dao.AbstractDao;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetHiddenLayerDao extends AbstractDao<NeuralNetHiddenLayerVO> {

	
    public static final NeuralNetHiddenLayerDao instance = new NeuralNetHiddenLayerDao();
    
    private NeuralNetHiddenLayerDao() {
        super(NeuralNetHiddenLayerAuditEventTypeDefinition.TYPE_NAME);
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
                "layerNumber",
                "numberOfNeurons",
                "networkId"
                );
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return SchemaDefinition.NEURAL_HIDDENLAYERS_TABLE;
	}
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getXidPrefix()
	 */
	@Override
	protected String getXidPrefix() {
		return NeuralNetHiddenLayerVO.XID_PREFIX;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#voToObjectArray(com.serotonin.m2m2.vo.AbstractVO)
	 */
	@Override
	protected Object[] voToObjectArray(NeuralNetHiddenLayerVO vo) {
		return new Object[]{
			vo.getXid(),
			vo.getName(),
			vo.getLayerNumber(),
			vo.getNumberOfNeurons(),
			vo.getNetworkId(),
		};
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getNewVo()
	 */
	@Override
	public NeuralNetHiddenLayerVO getNewVo() {
		NeuralNetHiddenLayerVO vo =  new NeuralNetHiddenLayerVO();
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
	public RowMapper<NeuralNetHiddenLayerVO> getRowMapper() {
		return new NeuralNetHiddenLayerRowMapper();
	}
	
    class NeuralNetHiddenLayerRowMapper implements RowMapper<NeuralNetHiddenLayerVO> {
        @Override
        public NeuralNetHiddenLayerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            NeuralNetHiddenLayerVO net = new NeuralNetHiddenLayerVO();
            net.setId(rs.getInt(++i));
            net.setXid(rs.getString(++i));
            net.setName(rs.getString(++i));
            net.setLayerNumber(rs.getInt(++i));
            net.setNumberOfNeurons(rs.getInt(++i));
            net.setNetworkId(rs.getInt(++i));
            return net;
        }
    }

	/**
	 * Get the hidden Layers for a network
	 * 
	 * @param id
	 * @return
	 */
	public List<NeuralNetHiddenLayerVO> getLayersForNetwork(int networkId) {
		return query(SELECT_ALL + " WHERE networkId = ?",new Object[]{networkId}, new NeuralNetHiddenLayerRowMapper());

	}
	
}
