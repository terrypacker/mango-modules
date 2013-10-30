/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.controltoolbox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.serotonin.m2m2.module.MenuItemDefinition;

/**
 * @author Terry Packer
 *
 */
public class ControlToolboxMenuItemDefinition extends MenuItemDefinition{

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.MenuItemDefinition#getVisibility()
	 */
	@Override
	public Visibility getVisibility() {
		return Visibility.USER;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.MenuItemDefinition#getTextKey(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getTextKey(HttpServletRequest request,
			HttpServletResponse response) {
		return "neuralnet.header.icontext";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.MenuItemDefinition#getImage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getImage(HttpServletRequest request,
			HttpServletResponse response) {
		return "web/img/neural_networking.png";
	}

    /* (non-Javadoc)
     * @see com.serotonin.m2m2.module.MenuItemDefinition#getHref(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public String getHref(HttpServletRequest request, HttpServletResponse response) {
        return "/neural_net.shtm";
    }
	
}
