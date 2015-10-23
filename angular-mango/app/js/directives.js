'use strict';

/* Directives */
/**
 * Directive to Display a Data Point
 */
angular.module('mangoDirectives', [])
.directive('mangoDatapointView', function() {
  return {
    templateUrl: 'directives/mango-datapoint-view.html'
  };
}).directive('mangoDatapointQuickview', function() {
  return {
    templateUrl: 'directives/mango-datapoint-quickview.html'
  };
}).directive('amChart',
   function () {
       return {
           restrict: 'E',
           replace:true,
           template: '<div class="amchart"></div>',
           link: function ($scope, $el, attrs) {
            var id = $el[0].id;
             
            // we can't render a chart without any data
            if ($scope.amChartOptions.dataProvider) {
                var renderChart = function (amChartOptions) {
                    var o = amChartOptions || $scope.amChartOptions;
                    // set height and width
                    var height = $scope.height || '400px';
                    var width = $scope.width || '100%';
                    $el.css({
                        'height': height,
                        'width': width
                    });
                    $scope.amChart = AmCharts.makeChart(id, o);
             
                }; //End Render Chart
                renderChart();
            }
         }//end watch
       }
}).directive('footer', function(){
	 return {
		    restrict: 'A',
		    templateUrl: 'partials/footer.html',
		    scope: true,
		    transclude : false,
		    controller: 'footerCtrl'
		  };
}).directive('header', function(){
	 return {
		    restrict: 'A',
		    templateUrl: 'partials/header.html',
		    scope: true,
		    transclude : false,
		    controller: 'headerCtrl'
		  };
});

//Am Charts Gauge Directive
angular.module('mangoDirectives').directive('amGauge', mangoAmGauge);

function mangoAmGauge() { 

    return { 
        restrict: 'E',
        replace:true,
        template: '<div class="amchart"></div>',
        link: function ($scope, $el, attrs) {
	         var id = $el[0].id;
             var renderChart = function (amChartOptions) {
                 //Get a config to use
            	 var o = amChartOptions || $scope.amChartData.configs[id];
                 if(typeof o === 'undefined')
                	 o = $scope.defaultAmGaugeConfig;
                 
                 var titleSize = 15;
                 if(typeof attrs.titleSize !== 'undefined')
                	 titleSize = attrs.titleSize;
                 if(typeof attrs.title !== 'undefined'){
                	 o.titles = [
                	     {
                	    	text: attrs.title,
                	    	size: titleSize
                	     }];
                 }
                 
                 // set height and width
                 var height = $scope.height || '400px';
                 var width = $scope.width || '100%';
                 $el.css({
                     'height': height,
                     'width': width
                 });
                 //Create our array if necessary
                 if(typeof $scope.amChart == 'undefined'){
                	 $scope.amChart = [];
                 }
                 $scope.amChart[id] = AmCharts.makeChart(id, o);
                 $scope.amChart[id].xid = attrs.xid;
                 $scope.registerForPointEvents(id, attrs.xid);
             }; //End Render Chart
             renderChart();
	      },//link
	      controller: mangoAmGaugeController,
    };
}

mangoAmGaugeController.$inject = ['$scope', 'AmChartData', 'PointValueEvents']

function mangoAmGaugeController($scope, AmChartData, PointValueEvents) { 
	
	$scope.amChartData = AmChartData;
	
	//Setup any configurations for the charts
	if(typeof amChartConfigs !== 'undefined'){
		for(var i in amChartConfigs){
			$scope.amChartData.configs[i] = amChartConfigs[i];
		}
	}
	
    //Register for Point Events and update the current Value
    $scope.registerForPointEvents = function(id, xid){
    	PointValueEvents.register(
            xid,
            ['UPDATE'],
            function(message){
            	if(($scope.amChart[id].arrows[0].setValue)&&(message.payload.enabled === true)){
            		$scope.amChart[id].arrows[0].setValue(message.payload.value.value);
            		$scope.amChart[id].axes[0].setBottomText(message.payload.renderedValue);
                    $scope.amChart[id].validateData();
            	}
                console.log(message);
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
    };
    
    $scope.defaultAmGaugeConfig = {                    
        type: "gauge",
        pathToImages: "/resources/amcharts/images/",
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
}

//Serial Chart Directive
angular.module('mangoDirectives').directive('amSerial', mangoAmSerial);

function mangoAmSerial() { 

    return { 
        restrict: 'E',
        scope: {
        	chartDataPoint: '=',
        	apiOptions: '='
        },
        replace:true,
        template: '<div class="amchart"></div>',
        link: function ($scope, $el, attrs) {
	         var id = $el[0].id;
             var renderChart = function (amChartOptions) {
            	 //TODO Multiple XIDS
                 //Get a config to use
            	 var o = amChartOptions || $scope.amChartData.configs[id];
                 if(typeof o === 'undefined')
                	 o = $scope.defaultAmSerialConfig;
                 
                 //Add a Title
                 var titleSize = 15;
                 if(typeof attrs.titleSize !== 'undefined')
                	 titleSize = attrs.titleSize;
                 if(typeof attrs.title !== 'undefined'){
                	 o.titles = [
                	     {
                	    	text: attrs.title,
                	    	size: titleSize
                	     }];
                 }
                 
                 var chartXid;
                 if(typeof attrs.xid !== 'undefined')
                	 chartXid = attrs.xid;
                 else if(typeof $scope.chartDataPoint !== 'undefined')
                	 chartXid = $scope.chartDataPoint.xid;
                 
                 // set height and width
                 var height = $scope.height || '400px';
                 var width = $scope.width || '100%';
                 $el.css({
                     'height': height,
                     'width': width
                 });
                 
                 $scope.amChartData.charts[id] = AmCharts.makeChart(id, o);
                 $scope.amChartData.charts[id].mangoApiOptions = {
                	id: id,
                	xid: chartXid,
                	from: new Date(new Date().getTime() - 1000 * 60 * 60 * 8),
                    to: new Date(),
                    rollup: 'AVERAGE',
                    timePeriodType: 'HOURS',
                    timePeriods: 1,
                    useRendered: false,
                    unitConversion: true
                 };
                 $scope.apiOptions = $scope.amChartData.charts[id].mangoApiOptions;
                 
                 //Go Get the Data to start up
                 $scope.loadSerialChartData($scope.amChartData.charts[id].mangoApiOptions);
                 
             }; //End Render Chart
             renderChart();
	      },//link
	      controller: mangoAmSerialController,
    };
}

mangoAmSerialController.$inject = ['$scope', 'AmChartData', 'DataPoint','HistoricalValues']

function mangoAmSerialController($scope, AmChartData, DataPoint, HistoricalValues) { 

	//For use in scope
	$scope.amChartData = AmChartData;

	//Setup any configurations for the charts
	if(typeof amChartConfigs !== 'undefined'){
		for(var i in amChartConfigs){
			$scope.amChartData.configs[i] = amChartConfigs[i];
		}
	}
		
	$scope.getDataPoint = DataPoint.get;
	$scope.loadSerialChartData = function(options){
        HistoricalValues.get(options).$promise.then(function(pointValues){
            $scope.amChartData.charts[options.id].dataProvider = pointValues;
            $scope.amChartData.charts[options.id].validateData();
        });
    };
	
	$scope.defaultAmSerialConfig = {                    
	            "type": "serial",
	            "theme": "light",
	            "marginLeft": 20,
	            "pathToImages": "../bower_components/amcharts/dist/amcharts/images/",
                dataProvider: [],
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
}  

//Date Rollup Directive
angular.module('mangoDirectives').directive('mangoDateRollupPicker', mangoDateRollupPicker);

function mangoDateRollupPicker() { 

    return { 
        restrict: 'E',
        replace:true,
        scope: {
        	apiOptions: '=',
        },
        templateUrl: 'directives/date-rollup-picker.html',
        link: function ($scope, $el, attrs) {
	         var id = $el[0].id;
	         $scope.dateRollupPickerId = id;
	         $scope.dateRollupPickers[id] = {
	        	chartId: attrs.chartId	 
	         };
	         $scope.amChartData.charts[attrs.chartId].options = $scope.apiOptions;
	      },//link
	      controller: mangoDateRollupPickerController,
    };
}

mangoDateRollupPickerController.$inject = ['$scope', 'AmChartData', 'DataPoint','HistoricalValues']

function mangoDateRollupPickerController($scope, AmChartData, DataPoint, HistoricalValues) { 
	
	//Scope the picker list
	$scope.dateRollupPickers = [];
	//For use in scope
	$scope.amChartData = AmChartData;
	
	$scope.loadChartData = function(id){
		var options = AmChartData.charts[$scope.dateRollupPickers[id].chartId].mangoApiOptions;
		
		if(typeof options === 'undefined')
             options = $scope.defaultChartSettings;

		
		HistoricalValues.get(options).$promise.then(function(pointValues){
			AmChartData.charts[$scope.dateRollupPickers[id].chartId].dataProvider = pointValues;
			AmChartData.charts[$scope.dateRollupPickers[id].chartId].validateData();
        });
	};
	$scope.defaultChartSettings = {
            xid: null,
            from: new Date(new Date().getTime() - 1000 * 60 * 60 * 8),
            to: new Date(),
            rollup: 'AVERAGE',
            timePeriodType: 'HOURS',
            timePeriods: 1,
            useRendered: false,
            unitConversion: true
        };
}  



