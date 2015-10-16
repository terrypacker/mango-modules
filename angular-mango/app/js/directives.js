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
            	 var o = amChartOptions || $scope.amChartConfigs[attrs.xid];
                 if(typeof o === 'undefined')
                	 o = $scope.defaultAmGaugeConfig;
                 
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
                 $scope.amChart[attrs.xid] = AmCharts.makeChart(id, o);
                 $scope.registerForPointEvents(attrs.xid);
             }; //End Render Chart
             renderChart();
	      },//link
	      controller: mangoAmGaugeController,
    };
}

mangoAmGaugeController.$inject = ['$scope', 'PointValueEvents']

function mangoAmGaugeController($scope, PointValueEvents) { 
	//Setup any configurations for the charts
	if(typeof $scope.amChartConfigs === 'undefined')
		$scope.amChartConfigs = amChartConfigs;
	
    //Register for Point Events and update the current Value
    $scope.registerForPointEvents = function(xid){
    	PointValueEvents.register(
            xid,
            ['UPDATE'],
            function(message){
            	if($scope.amChart[xid].arrows[0].setValue){
            		$scope.amChart[xid].arrows[0].setValue(message.payload.value.value);
            		$scope.amChart[xid].axes[0].setBottomText(message.payload.renderedValue);
                    $scope.amChart[xid].validateData();
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
        replace:true,
        template: '<div class="amchart"></div>',
        link: function ($scope, $el, attrs) {
	         var id = $el[0].id;
             var renderChart = function (amChartOptions) {
            	 //TODO Multiple XIDS
                 //Get a config to use
            	 var o = amChartOptions || $scope.amChartConfigs[attrs.xid];
                 if(typeof o === 'undefined')
                	 o = $scope.defaultAmSerialConfig;
                 
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
                 $scope.amChart[attrs.xid] = AmCharts.makeChart(id, o);
                 
                 //Go Get the Data to start up
                 $scope.loadSerialChartData({
                	xid: attrs.xid,
                	from: new Date(new Date().getTime() - 1000 * 60 * 60 * 8),
                    to: new Date(),
                    rollup: 'AVERAGE',
                    timePeriodType: 'HOURS',
                    timePeriods: 1,
                    useRendered: false,
                    unitConversion: true
                 });
                 
             }; //End Render Chart
             renderChart();
	      },//link
	      controller: mangoAmSerialController,
    };
}

mangoAmSerialController.$inject = ['$scope', 'DataPoint','HistoricalValues']

function mangoAmSerialController($scope, DataPoint, HistoricalValues) { 
	//Setup any configurations for the charts
	if(typeof $scope.amChartConfigs === 'undefined')
		$scope.amChartConfigs = amChartConfigs;
		
	$scope.getDataPoint = DataPoint.get;
	$scope.loadSerialChartData = function(options){
        HistoricalValues.get(options).$promise.then(function(pointValues){
            $scope.amChart[options.xid].dataProvider = pointValues;
            $scope.amChart[options.xid].validateData();
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




