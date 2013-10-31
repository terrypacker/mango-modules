/**
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox.web.mapping;

import com.serotonin.m2m2.module.UriMappingDefinition;
import com.serotonin.m2m2.module.UriMappingDefinition.Permission;
import com.serotonin.m2m2.web.mvc.UrlHandler;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxAlgorithmPropertiesUriMappingDefinition extends UriMappingDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.UriMappingDefinition#getPermission()
	 */
	@Override
	public Permission getPermission() {
		return Permission.USER;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.UriMappingDefinition#getPath()
	 */
	@Override
	public String getPath() {
		return "/algorithm_properties.shtm";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.UriMappingDefinition#getHandler()
	 */
	@Override
	public UrlHandler getHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.UriMappingDefinition#getJspPath()
	 */
	@Override
	public String getJspPath() {
		return "web/jsp/view/algorithm/properties/properties.jsp";
	}
}
