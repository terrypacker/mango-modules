/**
 * Copyright (C) 2019  Terry Packer. All rights reserved.
 */
package com.terrypacker.acurite;

import com.serotonin.m2m2.module.AngularJSModuleDefinition;

/**
 * @author Terry Packer
 *
 */
public class AcuriteAngularJSModuleDefinition extends AngularJSModuleDefinition {
    @Override
    public String getJavaScriptFilename() {
        return "/angular/acurite.js";
    }
}
