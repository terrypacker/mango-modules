package com.plushpay.mango.controltoolbox.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import com.plushpay.mango.controltoolbox.ControlToolboxControllerAuditEventTypeDefinition;
import com.plushpay.mango.controltoolbox.db.SchemaDefinition;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxControllerVO;
import com.serotonin.db.pair.IntStringPair;
import com.serotonin.m2m2.db.dao.AbstractDao;

/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxControllerDao extends AbstractDao<ControlToolboxControllerVO>{

    public static final ControlToolboxControllerDao instance = new ControlToolboxControllerDao();
    
    private ControlToolboxControllerDao() {
        super(ControlToolboxControllerAuditEventTypeDefinition.TYPE_NAME);
        LOG = LogFactory.getLog(ControlToolboxControllerDao.class);
    }
    
    /* (non-Javadoc)
     * @see com.deltamation.mango.downtime.db.GenericDao#getProperties()
     */
    @Override
    protected LinkedHashMap<String, Integer> getPropertyTypeMap() {
    	LinkedHashMap<String, Integer> types = new LinkedHashMap<String, Integer>();
    	types.put("id", Types.INTEGER);
    	types.put("xid", Types.VARCHAR);
    	types.put("name", Types.VARCHAR);
    	types.put("enabled", Types.CHAR);
    	types.put("algorithmId", Types.INTEGER);

    	return types;
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return SchemaDefinition.CONTROL_CONTROLLERS_TABLE;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getXidPrefix()
	 */
	@Override
	protected String getXidPrefix() {
		return ControlToolboxControllerVO.XID_PREFIX;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#voToObjectArray(com.serotonin.m2m2.vo.AbstractVO)
	 */
	@Override
	protected Object[] voToObjectArray(ControlToolboxControllerVO vo) {
		return new Object[]{
			vo.getXid(),
			vo.getName(),
			boolToChar(vo.isEnabled()),
			vo.getAlgorithmId()
		};
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getNewVo()
	 */
	@Override
	public ControlToolboxControllerVO getNewVo() {
		ControlToolboxControllerVO vo =  new ControlToolboxControllerVO();
		vo.setXid(this.generateUniqueXid());
		return vo;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getRowMapper()
	 */
	@Override
	public RowMapper<ControlToolboxControllerVO> getRowMapper() {
		return new ControlToolboxControllerRowMapper();
	}
	
    class ControlToolboxControllerRowMapper implements RowMapper<ControlToolboxControllerVO> {
        @Override
        public ControlToolboxControllerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            ControlToolboxControllerVO net = new ControlToolboxControllerVO();
            net.setId(rs.getInt(++i));
            net.setXid(rs.getString(++i));
            net.setName(rs.getString(++i));
            net.setEnabled(charToBool(rs.getString(++i)));
            net.setAlgorithmId(rs.getInt(++i));
            return net;
        }
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getPropertiesMap()
	 */
	@Override
	protected Map<String, IntStringPair> getPropertiesMap() {
		// TODO Auto-generated method stub
		return null;
	}


}
