/**
 * Copyright (C) 2013 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.mango.neuralnet;

import com.serotonin.m2m2.i18n.Translations;
import com.serotonin.m2m2.module.AuditEventTypeDefinition;
import com.serotonin.web.taglib.Functions;

/**
 * @author Terry Packer
 *
 */
public class NeuralNetHiddenLayerAuditEventTypeDefinition extends AuditEventTypeDefinition{

	
    public static final String TYPE_NAME = "NN_HL";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getDescriptionKey() {
        return "neuralnet.event.audit.hiddenlayer.name";
    }

    @Override
    public String getEventListLink(int ref1, int ref2, Translations translations) {
        //TODO Fix this link when we code up the process UI
        String alt = Functions.quotEncode(translations.translate("common.edit"));
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='home.shtm?hlid=");
        sb.append(ref1);
        sb.append("'><img src='");
        sb.append(getModule().getWebPath()).append("/web/link.png");
        sb.append("' alt='").append(alt);
        sb.append("' title='").append(alt);
        sb.append("'/></a>");
        return sb.toString();
    }
	
	
}
