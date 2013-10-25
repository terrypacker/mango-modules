/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet.db;

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
	public static final String NEURAL_NETWORKS_TABLE = "neuralNetworks";
	public static final String NEURAL_POINTS_TABLE = "neuralPoints";
	public static String NEURAL_HIDDENLAYERS_TABLE = "neuralHiddenLayers";
	
	
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#newInstallationCheck(com.serotonin.db.spring.ExtendedJdbcTemplate)
	 */
	@Override
	public void newInstallationCheck(ExtendedJdbcTemplate ejt) {
        if (!Common.databaseProxy.tableExists(ejt, NEURAL_NETWORKS_TABLE)) {
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
		tableNames.add(NEURAL_NETWORKS_TABLE);
		tableNames.add(NEURAL_POINTS_TABLE);
		tableNames.add(NEURAL_HIDDENLAYERS_TABLE);
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#getUpgradePackage()
	 */
	@Override
	public String getUpgradePackage() {
		return "com.plushpay.mango.neuralnet.db.upgrade";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.DatabaseSchemaDefinition#getDatabaseSchemaVersion()
	 */
	@Override
	public int getDatabaseSchemaVersion() {
		return 1;
	}

}
