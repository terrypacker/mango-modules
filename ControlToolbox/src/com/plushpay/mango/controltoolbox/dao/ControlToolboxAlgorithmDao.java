/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import com.plushpay.mango.controltoolbox.ControlToolboxAlgorithmAuditEventTypeDefinition;
import com.plushpay.mango.controltoolbox.db.SchemaDefinition;
import com.plushpay.mango.controltoolbox.vo.AlgorithmProperties;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxAlgorithmVO;
import com.serotonin.m2m2.db.dao.AbstractDao;
import com.serotonin.util.SerializationHelper;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxAlgorithmDao extends AbstractDao<ControlToolboxAlgorithmVO>{
    public static final ControlToolboxAlgorithmDao instance = new ControlToolboxAlgorithmDao();
    
    private ControlToolboxAlgorithmDao() {
        super(ControlToolboxAlgorithmAuditEventTypeDefinition.TYPE_NAME);
        LOG = LogFactory.getLog(ControlToolboxControllerDao.class);
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
                "algorithmType",
                "data"
                );
    }

	/**
	 * This method is required because we are using a lob type
	 * TODO make this a map to sync with voToObjectArray
	 * 
	 * 
	 */
	@Override
	protected List<Integer> getPropertyTypes(){
		return Arrays.asList(
					Types.VARCHAR, //xid
					Types.VARCHAR, //name
					Types.INTEGER, //algo type
					Types.BLOB //data
				);
	}
    
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return SchemaDefinition.CONTROL_ALGORITHMS_TABLE;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getXidPrefix()
	 */
	@Override
	protected String getXidPrefix() {
		return ControlToolboxAlgorithmVO.XID_PREFIX;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#voToObjectArray(com.serotonin.m2m2.vo.AbstractVO)
	 */
	@Override
	protected Object[] voToObjectArray(ControlToolboxAlgorithmVO vo) {
		return new Object[]{
			vo.getXid(),
			vo.getName(),
			vo.getAlgorithmType(),
			SerializationHelper.writeObject(vo.getProperties())
		};
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getNewVo()
	 */
	@Override
	public ControlToolboxAlgorithmVO getNewVo() {
		ControlToolboxAlgorithmVO vo =  new ControlToolboxAlgorithmVO();
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
	public RowMapper<ControlToolboxAlgorithmVO> getRowMapper() {
		return new ControlToolboxAlgorithmRowMapper();
	}
	
    class ControlToolboxAlgorithmRowMapper implements RowMapper<ControlToolboxAlgorithmVO> {
        @Override
        public ControlToolboxAlgorithmVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            ControlToolboxAlgorithmVO net = new ControlToolboxAlgorithmVO();
            net.setId(rs.getInt(++i));
            net.setXid(rs.getString(++i));
            net.setName(rs.getString(++i));
            net.setAlgorithmType(rs.getInt(++i));
            AlgorithmProperties props = (AlgorithmProperties) SerializationHelper.readObjectInContext(rs.getBinaryStream(++i));
            net.setProperties(props);
            return net;
        }
    }


}
