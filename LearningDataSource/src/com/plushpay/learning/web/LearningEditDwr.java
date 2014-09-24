/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.plushpay.learning.web;

import com.plushpay.learning.vo.LearningDataSourceVO;
import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.vo.dataSource.BasicDataSourceVO;
import com.serotonin.m2m2.web.dwr.DataSourceEditDwr;
import com.serotonin.m2m2.web.dwr.util.DwrPermission;

/**
 * @author Terry Packer
 *
 */
public class LearningEditDwr extends DataSourceEditDwr{

    @DwrPermission(user = true)
    public ProcessResult saveLearningDataSource(BasicDataSourceVO basic, int updatePeriods, int updatePeriodType) {
        LearningDataSourceVO ds = (LearningDataSourceVO) Common.getUser().getEditDataSource();

        setBasicProps(ds, basic);

        return tryDataSourceSave(ds);
    }

	
}
