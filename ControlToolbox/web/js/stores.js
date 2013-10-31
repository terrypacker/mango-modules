/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */

if(typeof stores == 'undefined')
	var stores = {};


require(["deltamation/CachedDwrStore", "dojo/Deferred","dojo/store/util/QueryResults"],
function(CachedDwrStore,QueryResults) {

if (typeof ControlToolboxControllerDwr !== 'undefined') {
    stores.controller = new CachedDwrStore(ControlToolboxControllerDwr, "ControlToolboxControllerDwr");
    stores.controller.dwr.queryLocally = false;
}

if (typeof ControlToolboxPointDwr !== 'undefined') {
    stores.point = new CachedDwrStore(ControlToolboxPointDwr, "ControlToolboxPointDwr");
    stores.point.dwr.queryLocally = false;
}

if (typeof ControlToolboxAlgorithmDwr !== 'undefined') {
    stores.algorithm = new CachedDwrStore(ControlToolboxAlgorithmDwr, "ControlToolboxAlgorithmDwr");
    stores.algorithm.dwr.queryLocally = false;
}

if (typeof DataPointDwr !== 'undefined') {
    stores.allDataPoints = new CachedDwrStore(DataPointDwr, "DataPointDwr");
    stores.allDataPoints.dwr.queryLocally = false;
    stores.allDataPoints.dwr.loadData = true;
    stores.allDataPoints.dwr.or = false; //Use AND in Queries to restrict to DataSource of interest
}




}); // require
