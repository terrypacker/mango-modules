'use strict';

/* Filters */
/**
 * Filter to return a checkmark or an X based on a true or false
 */
angular.module('mangoFilters', []).filter('checkmark', function() {
  return function(input) {
    return input ? '\u2713' : '\u2718';
  };
});

angular.module('mangoFilters', []).
filter('datapointFilter', function() {
	/**
	 *	Filter the datapoints list 
	 */
	return function(datapoints, query) {
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
