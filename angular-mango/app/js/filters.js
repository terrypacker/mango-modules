'use strict';

/* Filters */
/**
 * Filter to return a checkmark or an X based on a true or false
 */
var mangoFilters = angular.module('mangoFilters', []).filter('checkmark', function() {
  return function(input) {
    return input ? '\u2713' : '\u2718';
  };
});

mangoFilters.filter('dataPointFilter', function() {
	/**
	 *	Filter the datapoints list 
	 */
	return function(datapoints, query) {
		
		if((query.dataType.length === 0)
				&&(query.names.length === 0)
				&&(query.name.length === 0))
			return datapoints;
		
		var out = [];

		// Filter logic here, adding matches to the out var.
		var added = false;
		for(var i=0; i<datapoints.length; i++){
			added = false;
			//Filter on dataSourceType
			for(var j=0; j<query.dataSourceType.length; j++){
				if(datapoints[i].dataSourceType === query.dataSourceType[j]){
					added = true;
					out.push(datapoints[i]);
					break;
				}
			}
			if(added === true)
				continue;
			
			//Filter on name and deviceName (from search input)
			if(query.name !== ''){
				if(datapoints[i].name.indexOf(query.name) >= 0){
					added = true;
					out.push(datapoints[i]);
					continue;
				}
				if(datapoints[i].deviceName.indexOf(query.name) >= 0){
					added = true;
					out.push(datapoints[i]);
					continue;
				}
			}
			
			//Filter on names
			for(var j=0; j<query.names.length; j++){
				if(datapoints[i].name.indexOf(query.names[j]) >= 0){
					added = true;
					out.push(datapoints[i]);
					break;
				}
			}
			if(added === true)
				continue;
			
			//Filter on Data Type
			for(var j=0; j<query.dataType.length; j++){
				if(datapoints[i].dataType === query.dataType[j]){
					added = true;
					out.push(datapoints[i]);
					break;
				}
			}
			if(added === true)
				continue;

		}
		return out;
	}
});


mangoFilters.filter('dataSourceFolderFilter', function() {
	/**
	 *	Filter the data source folder list 
	 */
	return function(dataSourceFolders, query) {
		
		//If the filter is empty, return all
		if((query.dataSourceType.length == 0)&&(query.dataType.length === 0)
				&&(query.names.length === 0)
				&&(query.name.length === 0))
			return dataSourceFolders;
		
		var tempFolders = [];
		if(query.dataSourceType.length !== 0){
			//Top Down filter approach
			for(var i=0; i<dataSourceFolders.length; i++){
				//Filter on dataSourceType
				for(var j=0; j<query.dataSourceType.length; j++){
					if(dataSourceFolders[i].dataSourceType === query.dataSourceType[j]){
						tempFolders.push(dataSourceFolders[i]);
						break; //Keeping this folder
					}
				}
			}
		}else{
			tempFolders = dataSourceFolders;
		}
		
		
		var out = [];
		//Filter out any empty ones too
		for(var x=0; x<tempFolders.length; x++){
			var dataSources = tempFolders[x].dataSources;
			var add = false;
			for(var i=0; i<dataSources.length; i++){
				//If any of the data source points match then add the data source
				var ds = dataSources[i];
				add = false;
				for(var j=0; j<ds.dataPoints.length; j++){
					var dp = ds.dataPoints[j];
					if(query.name !== ''){
						//Filter on name/device name
						if(dp.name.indexOf(query.name) >= 0){
							add = true;
							break;
						}else if(dp.deviceName.indexOf(query.name) >= 0){
							add = true;
							break;
						}
					}
					for(var k=0; k<query.names.length; k++){
						if(dp.name.indexOf(query.names[k]) >= 0){
							add = true;
							break;
						}
					}
					if(add === true)
						break;
					
					//Filter on Data Type
					for(var k=0; k<query.dataType.length; k++){
						if(dp.dataType === query.dataType[k]){
							add = true;
							break;
						}
					}
					if(add === true)
						break;
				}
				if(add === true)
					break;
			}
			if(add === true)
				out.push(tempFolders[x]);
		}

		
		
		return out;
	}
});

mangoFilters.filter('dataSourceFilter', function() {
	/**
	 *	Filter the data source folder list 
	 */
	return function(dataSources, query) {
		
		//If the filter is empty, return all
		if((query.dataType.length === 0)
				&&(query.names.length === 0)
				&&(query.name.length === 0))
			return dataSources;

		var out = [];
		
		for(var i=0; i<dataSources.length; i++){
			//If any of the data source points match then add the data source
			var ds = dataSources[i];
			var add = false;
			for(var j=0; j<ds.dataPoints.length; j++){
				var dp = ds.dataPoints[j];
				if(query.name !== ''){
					//Filter on name/device name
					if(dp.name.indexOf(query.name) >= 0){
						add = true;
						break;
					}else if(dp.deviceName.indexOf(query.name) >= 0){
						add = true;
						break;
					}
				}
				for(var k=0; k<query.names.length; k++){
					if(dp.name.indexOf(query.names[k]) >= 0){
						add = true;
						break;
					}
				}
				if(add === true)
					break;
				
				//Filter on Data Type
				for(var k=0; k<query.dataType.length; k++){
					if(dp.dataType === query.dataType[k]){
						add = true;
						break;
					}
				}
				if(add === true)
					break;
			}
			if(add === true)
				out.push(ds);
		}
		return out;
	}
});
