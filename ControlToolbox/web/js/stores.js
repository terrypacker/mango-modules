/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */

if(typeof stores == 'undefined')
	var stores = {};


require(["deltamation/CachedDwrStore", "dojo/Deferred","dojo/store/util/QueryResults"],
function(CachedDwrStore,QueryResults) {

if (typeof NeuralNetNetworkDwr !== 'undefined') {
    stores.network = new CachedDwrStore(NeuralNetNetworkDwr, "NeuralNetNetworkDwr");
    stores.network.dwr.queryLocally = false;
}

if (typeof NeuralNetPointDwr !== 'undefined') {
    stores.point = new CachedDwrStore(NeuralNetPointDwr, "NeuralNetPointDwr");
    stores.point.dwr.queryLocally = false;
}

if (typeof NeuralNetHiddenLayerDwr !== 'undefined') {
    stores.layer = new CachedDwrStore(NeuralNetHiddenLayerDwr, "NeuralNetHiddenLayerDwr");
    stores.layer.dwr.queryLocally = false;
}

if (typeof DataPointDwr !== 'undefined') {
    stores.allDataPoints = new CachedDwrStore(DataPointDwr, "DataPointDwr");
    stores.allDataPoints.dwr.queryLocally = false;
    stores.allDataPoints.dwr.loadData = true;
    stores.allDataPoints.dwr.or = false; //Use AND in Queries to restrict to DataSource of interest
}




}); // require
