/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import com.plushpay.mango.controltoolbox.ControlToolboxPointAuditEventTypeDefinition;
import com.plushpay.mango.controltoolbox.db.SchemaDefinition;
import com.plushpay.mango.controltoolbox.vo.ControlToolboxPointVO;
import com.serotonin.db.pair.IntStringPair;
import com.serotonin.m2m2.db.dao.AbstractDao;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxPointDao extends AbstractDao<ControlToolboxPointVO>{

    public static final ControlToolboxPointDao instance = new ControlToolboxPointDao();
    
    private ControlToolboxPointDao() {
        super(ControlToolboxPointAuditEventTypeDefinition.TYPE_NAME);
        LOG = LogFactory.getLog(ControlToolboxPointDao.class);
    }


	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return SchemaDefinition.CONTROL_POINTS_TABLE;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getXidPrefix()
	 */
	@Override
	protected String getXidPrefix() {
		return ControlToolboxPointVO.XID_PREFIX;
	}
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#voToObjectArray(com.serotonin.m2m2.vo.AbstractVO)
	 */
	@Override
	protected Object[] voToObjectArray(ControlToolboxPointVO vo) {
		return new Object[]{
				vo.getXid(),
				vo.getName(),
				boolToChar(vo.isEnabled()),
				vo.getPointType(),
				vo.getDelay(),
				vo.getDataPointId(),
				vo.getControllerId(),
				vo.getHighLimit(),
				vo.getLowLimit()
			};
	}




	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractDao#getNewVo()
	 */
	@Override
	public ControlToolboxPointVO getNewVo() {
		ControlToolboxPointVO vo =  new ControlToolboxPointVO();
		vo.setXid(this.generateUniqueXid());
		return vo;
	}




	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getProperties()
	 */
    @Override
    protected LinkedHashMap<String, Integer> getPropertyTypeMap() {
    	LinkedHashMap<String, Integer> types = new LinkedHashMap<String, Integer>();
    	types.put("id", Types.INTEGER);
    	types.put("xid", Types.VARCHAR);
    	types.put("name", Types.VARCHAR);
    	types.put("enabled", Types.CHAR);
    	types.put("pointType", Types.INTEGER);
    	types.put("delay", Types.INTEGER);
    	types.put("dataPointId", Types.INTEGER);
    	types.put("controllerId", Types.INTEGER);
    	types.put("highLimit", Types.DOUBLE);
    	types.put("lowLimit", Types.DOUBLE);
    	return types;
     }



	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.db.dao.AbstractBasicDao#getRowMapper()
	 */
	@Override
	public RowMapper<ControlToolboxPointVO> getRowMapper() {
		return new ControlNetworkPointRowMapper();
	}
	
    class ControlNetworkPointRowMapper implements RowMapper<ControlToolboxPointVO> {
        @Override
        public ControlToolboxPointVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            ControlToolboxPointVO pt = new ControlToolboxPointVO();
            pt.setId(rs.getInt(++i));
            pt.setXid(rs.getString(++i));
            pt.setName(rs.getString(++i));
            pt.setEnabled(charToBool(rs.getString(++i)));
            pt.setPointType(rs.getInt(++i));
            pt.setDelay(rs.getInt(++i));
            pt.setDataPointId(rs.getInt(++i));
            pt.setControllerId(rs.getInt(++i));
            pt.setHighLimit(rs.getDouble(++i));
            pt.setLowLimit(rs.getDouble(++i));
            return pt;
        }
    }
	
	
	/**
	 * @param inputType
	 * @param id
	 */
	public List<ControlToolboxPointVO> getControllerPoints(int inputType, int controllerId) {
		return query(SELECT_ALL + " WHERE pointType = ? AND controllerId = ? ORDER BY delay",new Object[]{inputType,controllerId}, new ControlNetworkPointRowMapper());
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
