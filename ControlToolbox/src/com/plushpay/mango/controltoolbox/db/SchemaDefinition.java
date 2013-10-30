/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.db;

import java.util.List;

import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.module.DatabaseSchemaDefinition;

/**
 * @author Terry Packer
 *
 */
public class SchemaDefinition extends DatabaseSchemaDefinition{

	//Tables Used
	public static final String CONTROL_CONTROLLERS_TABLE = "controlControllers";
	public static final String CONTROL_POINTS_TABLE = "controlPoints";
	public static final String CONTROL_ALGORITHMS_TABLE = "controlAlgorithms";
	
	
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#newInstallationCheck(com.serotonin.db.spring.ExtendedJdbcTemplate)
	 */
	@Override
	public void newInstallationCheck(ExtendedJdbcTemplate ejt) {
        if (!Common.databaseProxy.tableExists(ejt, CONTROL_CONTROLLERS_TABLE)) {
            String path = Common.MA_HOME + getModule().getDirectoryPath() + "/web/db/createTables-"
                    + Common.databaseProxy.getType().name() + ".sql";
            Common.databaseProxy.runScriptFile(path, null);
        }
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#addConversionTableNames(java.util.List)
	 */
	@Override
	public void addConversionTableNames(List<String> tableNames) {
		tableNames.add(CONTROL_CONTROLLERS_TABLE);
		tableNames.add(CONTROL_POINTS_TABLE);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#getUpgradePackage()
	 */
	@Override
	public String getUpgradePackage() {
		return "com.plushpay.mango.controltoolbox.db.upgrade";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#getDatabaseSchemaVersion()
	 */
	@Override
	public int getDatabaseSchemaVersion() {
		return 1;
	}

}
