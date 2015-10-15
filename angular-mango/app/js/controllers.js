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
	            "pathToImages": "/bower_components/amcharts/dist/amcharts/images/",
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