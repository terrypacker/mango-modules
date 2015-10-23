'use strict';

/* Controllers */
var mangoControllers = angular.module('mangoControllers', []);

mangoControllers.controller('DataPointListCtrl', ['$scope', '$http', 'Login', 'DataPoint', 'CurrentValueByXid', function($scope, $http, Login, DataPoint, CurrentValueByXid) {
        //auto login
        $http({
                method: 'GET',
                url: 'http://' + mangoHost + '/rest/v1/login/' + mangoUser,
                headers: {'password': mangoPwd}
            }).success(function(data){
                $scope.dataPoints = DataPoint.query();
            });
    
        $scope.orderProp = 'name';
        $scope.quickView = {hide: true};
  
        //Setup the quick view
        $scope.setQuickView = function(dataPoint){
            $scope.quickView.hide = false;
            $scope.quickView.dataPoint = dataPoint;
            $scope.quickView.current = CurrentValueByXid.get({xid: dataPoint.xid});
        };
        $scope.clearQuickView = function(){
            $scope.quickView.hide = true;
            $scope.quickView.dataPoint = null;
        };
}]);

mangoControllers.controller('DataPointCarouselCtrl', ['$scope', '$http', 'Login', 'DataPoint', 'CurrentValueByXid', function($scope, $http, Login, DataPoint, CurrentValueByXid) {

	$scope.dataPoints = DataPoint.query();
	
}]);

mangoControllers.controller('DashboardCtrl', ['$scope', '$http', 'Login', 'DataPoint', 'PointValueEvents', 
    function($scope, $http, Login, DataPoint, PointValueEvents) {

	$scope.dataPoints = DataPoint.query();
	
	$scope.amChartOptions = {                    
        type: "gauge",
        pathToImages: "../bower_components/amcharts/dist/amcharts/images/",
        marginBottom: 20,
        marginTop: 40,
        fontSize: 13,
        theme: "dark",
        arrows: [
            {
                id: "GaugeArrow-1",
                value: 0
            }
        ],
        axes: [
            {
                axisThickness: 1,
                bottomText: "",
                bottomTextYOffset: -20,
                endValue: 220,
                id: "GaugeAxis-1",
                valueInterval: 10,
                bands: [
                    {
                        alpha: 0.7,
                        color: "#00CC00",
                        endValue: 90,
                        id: "GaugeBand-1",
                        startValue: 0
                    },
                    {
                        alpha: 0.7,
                        color: "#ffac29",
                        endValue: 130,
                        id: "GaugeBand-2",
                        startValue: 90
                    },
                    {
                        alpha: 0.7,
                        color: "#ea3838",
                        endValue: 220,
                        id: "GaugeBand-3",
                        innerRadius: "95%",
                        startValue: 130
                    }
                ]
            }
        ],
        allLabels: [],
        balloon: {},
        titles: []
};

}]);


mangoControllers.controller('DataPointDetailCtrl', ['$scope', '$http', '$routeParams', 'DataPoint','CurrentValueByXid', 'PointValueEvents',
	function($scope, $http, $routeParams, DataPoint, CurrentValueByXid, PointValueEvents){
		$scope.dataPointXid = $routeParams.dataPointXid;
        $scope.dataPoint = DataPoint.get({xid: $scope.dataPointXid});
        $scope.currentValue = CurrentValueByXid.get({xid: $scope.dataPointXid});
        
        //Register for Point Events and update the current Value
        PointValueEvents.register(
                $scope.dataPointXid,
                ['INITIALIZE', 'UPDATE', 'CHANGE', 'SET', 'BACKDATE', 'TERMINATE'],
                function(message){
                    var scope = angular.element($("#currentValue")).scope();
                    console.log(message);
                    scope.$apply(function() {
                        scope.currentValue = message.payload;
                    });
                    
                },
                function(error){
                    console.log('wse: ' + error);
                },
                function(open){
                    console.log('wso: ' + open);
                },
                function(close){
                    console.log('wsc: ' + close);
                }
            );
        
        
    }]);

mangoControllers.controller('DataPointChartCtrl', ['$scope', '$http', '$routeParams', 'DataPoint','HistoricalValues',
	function($scope, $http, $routeParams, DataPoint, HistoricalValues){
		$scope.dataPointXid = $routeParams.dataPointXid;
        $scope.dataPoint = DataPoint.get({xid: $scope.dataPointXid});
        $scope.chartSettings = {
            xid: $scope.dataPointXid,
            from: new Date(new Date().getTime() - 1000 * 60 * 60 * 8),
            to: new Date(),
            rollup: 'AVERAGE',
            timePeriodType: 'HOURS',
            timePeriods: 1,
            useRendered: false,
            unitConversion: true
        };
        
        $scope.loadChartData = function(options){
            //Use scope settings if none passed in
            if(typeof options === 'undefined')
                options = $scope.chartSettings;
                
            HistoricalValues.get(options).$promise.then(function(pointValues){
                $scope.amChart.dataProvider = pointValues;
                $scope.amChart.validateData();
            });
        };
        
        
        $scope.amChartOptions = {
	            "type": "serial",
	            "theme": "light",
	            "marginLeft": 20,
	            "pathToImages": "../bower_components/amcharts/dist/amcharts/images/",
                dataProvider: [],
                /*Left here for reference as to how the data should look
	            "dataProvider": [{
                    "annotation": null,
	                "timestamp": 1438866000000,
	                "value": -0.307
	            },{
                    "annotation": null,
	                "timestamp": 1438966000000,
	                "value": 1.0
	            },{
                    "annotation": null,
	                "timestamp": 1439066000000,
	                "value": 3.0
	            }
                ],*/
	            "valueAxes": [{
	                "axisAlpha": 0,
	                "inside": true,
	                "position": "left",
	                "ignoreAxisWidth": true
	            }],
	            "graphs": [{
	                "balloonText": "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>",
	                "bullet": "round",
	                "bulletSize": 6,
	                "lineColor": "#d1655d",
	                "lineThickness": 2,
	                "negativeLineColor": "#637bb6",
	                "type": "smoothedLine",
	                "valueField": "value"
	            }],
	            "chartScrollbar": {},
	            "chartCursor": {
	                "categoryBalloonDateFormat": "YYYY",
	                "cursorAlpha": 0,
	                "cursorPosition": "mouse"
	            },
	            "categoryField": "timestamp",
	            "categoryAxis": {
	                "minPeriod": "ss",
	                "parseDates": true,
	                "minorGridAlpha": 0.1,
	                "minorGridEnabled": true
	            }
	        };
            
            //Init with some recent data
            $scope.loadChartData({xid: $scope.dataPointXid});
    }]);

mangoControllers.controller('MangoLogListCtrl', ['$scope', '$http', 'MangoLogs',
	function($scope, $http, MangoLogs){
		$scope.logfiles = MangoLogs.list();
    }]);

mangoControllers.controller('MangoLogDetailCtrl', ['$scope', '$http', '$routeParams', 'MangoLogs',
	function($scope, $http, $routeParams, MangoLogs){
		$scope.logfileName = $routeParams.logfileName;
        $scope.logfile = MangoLogs.get({filename: $scope.logfileName});
    }]);

mangoControllers.controller('ExplorerCtrl', ['$scope', '$compile', 'DataSource', 'DataPoint', function($scope, $compile, DataSource, DataPoint){
	//List of massaged data points
	$scope.dataPoints = [];
	
	//Create a query model
	$scope.query = {
		text: [],
		dataType: [],
		dataSourceType: [],
		names: [],
	};
	
	$scope.getDataSource = function(xid){
		for(var i=0; i<$scope.dataSources.length; i++){
			if($scope.dataSources[i].xid === xid)
				return $scope.dataSources[i];
		}
		return null;
	}
	
	//TODO Build the list of points with data source attributes set
	DataSource.query().$promise.then(function(dataSources){
		$scope.dataSources = dataSources;
		
		DataPoint.query().$promise.then(function(dataPoints){
			for(var i=0; i<dataPoints.length; i++){
				var ds = $scope.getDataSource(dataPoints[i].dataSourceXid);
				if(dataPoints[i].pointLocator === null){
					console.log("No Point Locator Model!");
					console.log(dataPoints[i]);
					continue;
				}
				
				var dp = {
					name: dataPoints[i].name,
					xid: dataPoints[i].xid,
					deviceName: dataPoints[i].deviceName,
					dataType: dataPoints[i].pointLocator.dataType,
					dataSourceType: ds.modelType,
				};
				$scope.dataPoints.push(dp);
			}
		});
	});
	
	//Track the ids for the tags
	$scope.tagIdCounter = 0;
	$scope.tagIdValues = [];
	
	$scope.addNameFilter = function(){
		if($scope.query.names.indexOf($scope.query.name) >= 0)
			return;
		
		$scope.query.names.push($scope.query.name);
		var tagId = $scope.tagIdCounter++;
		$scope.tagIdValues[tagId] = $scope.query.name;
		//Now add a tag to the list of filters for the UI
		$('#search-tags').append($compile('<span id="tag-' + tagId + '" class="label label-primary" style="margin-right: 5px; font-size: 15px;" ng-click="removeNameFilter(' + tagId + ')">' + $scope.query.name + '<span class="glyphicon glyphicon-remove-circle" style="padding-left: 5px"></span></span>')($scope));
		$scope.query.name = ''; //Clear the input
	};
	$scope.removeNameFilter = function(tagId){
		var value = $scope.tagIdValues[tagId];
		for(var i=0; i<$scope.query.names.length; i++){
			if($scope.query.names[i] === value){
				//Splice out
				$scope.query.names.splice(i,1);
				//Remove the span
				
				$('#tag-' + tagId).remove();
				break;
			}
		}
	};	
	
	$scope.addQueryFilter = function(name, value, label){
		//TODO Check for already contains
		if(typeof $scope.query[name] === 'undefined')
			$scope.query[name] = [];
		
		if($scope.query[name].indexOf(value) >= 0)
			return;
		$scope.query[name].push(value);
		var tagId = $scope.tagIdCounter++;
		$scope.tagIdValues[tagId] = value;
		//Now add a tag to the list of filters for the UI
		$('#search-tags').append($compile('<span id="tag-' + tagId + '" class="label label-primary" style="margin-right: 5px; font-size: 15px;" ng-click="removeQueryFilter(\'' + name +'\',' + tagId + ')">' + label + '<span class="glyphicon glyphicon-remove-circle" style="padding-left: 5px"></span></span>')($scope));
	};
	
	$scope.removeQueryFilter = function(name, tagId){
		if(typeof $scope.query[name] === 'undefined')
			return;
		var value = $scope.tagIdValues[tagId];
		
		var filterList = $scope.query[name];
		for(var i=0; i<filterList.length; i++){
			if(filterList[i] === value){
				//Splice out
				filterList.splice(i,1);
				$('#tag-' + tagId).remove();
				break;
			}
		}
	};
	
	
}]);

mangoControllers.controller('TreeCtrl', ['$scope', '$compile', 'AmChartData', 'DataSource', 'DataPoint', 'HistoricalValues', function($scope, $compile, AmChartData, DataSource, DataPoint, HistoricalValues){
	//List of massaged data points
	$scope.dataPoints = [];
	
	//Create a query model
	$scope.query = {
		name: "",
		dataType: [],
		dataSourceType: [],
		names: [],
	};
	
	$scope.apiOptions = {
            xid: null,
            from: new Date(new Date().getTime() - 1000 * 60 * 60 * 8),
            to: new Date(),
            rollup: 'AVERAGE',
            timePeriodType: 'HOURS',
            timePeriods: 1,
            useRendered: false,
            unitConversion: true
    };
	
	$scope.dataSourceFolders = [];
	$scope.chartDataPoint = {
			xid: 'voltage',
			name: 'Voltage'
	};
	$scope.setChartDataPoint = function(xid, chartId){
		$scope.chartDataPoint = $scope.getDataPoint(xid);
		var options = AmChartData.charts[chartId].mangoApiOptions;
		
		if(typeof options === 'undefined')
             options = $scope.defaultChartSettings;
		options.xid = xid;
		
		
		HistoricalValues.get(options).$promise.then(function(pointValues){
			AmChartData.charts[chartId].titles[0].text = $scope.chartDataPoint.name;
			AmChartData.charts[chartId].dataProvider = pointValues;
			AmChartData.charts[chartId].validateData();
        });

	};
	
	$scope.getDataPoint = function(xid){
		for(var i=0; i<$scope.dataPoints.length; i++){
			if($scope.dataPoints[i].xid === xid)
				return $scope.dataPoints[i];
		}
	};
	
	$scope.getDataSourceFolder = function(dataSourceType){
		for(var i=0; i<$scope.dataSourceFolders.length; i++){
			if($scope.dataSourceFolders[i].dataSourceType === dataSourceType)
				return $scope.dataSourceFolders[i];
		}
	};
	
	$scope.getDataSource = function(xid){
		//Check each folder for the ds with xid
		for(var i=0; i<$scope.dataSourceFolders.length; i++){
			var folder = $scope.dataSourceFolders[i];
			for(var j=0; j<folder.dataSources.length; j++){
				if(folder.dataSources[j].xid === xid)
					return folder.dataSources[j];	
			}
		}
		return null;
	};
	
	/*Build the Folders:
	  folder[dataSourceType][dataSourceXid][pointXid]
	*/
	DataSource.query().$promise.then(function(dataSources){
		//First build all the folders
		for(var i=0; i<dataSources.length; i++){
			var ds = dataSources[i];
			ds.dataPoints = []; //Create holder for points

			var dataSourceFolder = $scope.getDataSourceFolder(ds.modelType);
			
			//If didn't find one, create it
			if(typeof dataSourceFolder === 'undefined'){
				dataSourceFolder = {
					dataSourceType: ds.modelType,
					dataSources: [],
				};
				$scope.dataSourceFolders.push(dataSourceFolder);
			}
			dataSourceFolder.dataSources.push(ds);
		}
		
		DataPoint.query().$promise.then(function(dataPoints){
			for(var i=0; i<dataPoints.length; i++){
				//Get the data source from memory
				var ds = $scope.getDataSource(dataPoints[i].dataSourceXid);
				if(dataPoints[i].pointLocator === null){
					console.log("No Point Locator Model: ");
					console.log(dataPoints[i]);
					continue;
				}
				var dp = {
					name: dataPoints[i].name,
					xid: dataPoints[i].xid,
					deviceName: dataPoints[i].deviceName,
					dataType: dataPoints[i].pointLocator.dataType,
					dataSourceType: ds.modelType,
				};
				$scope.dataPoints.push(dp);
				ds.dataPoints.push(dp);
			}
		});
	});
	
	//Track the ids for the tags
	$scope.tagIdCounter = 0;
	$scope.tagIdValues = [];
	
	$scope.addNameFilter = function(){
		if($scope.query.names.indexOf($scope.query.name) >= 0)
			return;
		
		$scope.query.names.push($scope.query.name);
		var tagId = $scope.tagIdCounter++;
		$scope.tagIdValues[tagId] = $scope.query.name;
		//Now add a tag to the list of filters for the UI
		$('#search-tags').append($compile('<span id="tag-' + tagId + '" class="label label-primary" style="margin-right: 5px; font-size: 15px;" ng-click="removeNameFilter(' + tagId + ')">' + $scope.query.name + '<span class="glyphicon glyphicon-remove-circle" style="padding-left: 5px"></span></span>')($scope));
		$scope.query.name = ''; //Clear the input
	};
	$scope.removeNameFilter = function(tagId){
		var value = $scope.tagIdValues[tagId];
		for(var i=0; i<$scope.query.names.length; i++){
			if($scope.query.names[i] === value){
				//Splice out
				$scope.query.names.splice(i,1);
				//Remove the span
				
				$('#tag-' + tagId).remove();
				break;
			}
		}
	};	
	
	$scope.addQueryFilter = function(name, value, label){
		//TODO Check for already contains
		if(typeof $scope.query[name] === 'undefined')
			$scope.query[name] = [];
		
		if($scope.query[name].indexOf(value) >= 0)
			return;
		$scope.query[name].push(value);
		var tagId = $scope.tagIdCounter++;
		$scope.tagIdValues[tagId] = value;
		//Now add a tag to the list of filters for the UI
		$('#search-tags').append($compile('<span id="tag-' + tagId + '" class="label label-primary" style="margin-right: 5px; font-size: 15px;" ng-click="removeQueryFilter(\'' + name +'\',' + tagId + ')">' + label + '<span class="glyphicon glyphicon-remove-circle" style="padding-left: 5px"></span></span>')($scope));
	};
	
	$scope.removeQueryFilter = function(name, tagId){
		if(typeof $scope.query[name] === 'undefined')
			return;
		var value = $scope.tagIdValues[tagId];
		
		var filterList = $scope.query[name];
		for(var i=0; i<filterList.length; i++){
			if(filterList[i] === value){
				//Splice out
				filterList.splice(i,1);
				$('#tag-' + tagId).remove();
				break;
			}
		}
	};
	
}]);

mangoControllers.controller('headerCtrl', ['$scope', '$http',
    function($scope, $http){
    }]);

mangoControllers.controller('footerCtrl', ['$scope', '$http',
   function($scope, $http){
   }]);
