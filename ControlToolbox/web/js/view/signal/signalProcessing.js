

//Load the data
var signalData;
var dftData;
var signalProcessor;
var allPoints = [];
var selectedPoints = [];

//Run on page load
require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dijit/form/Button",
         "dojo/domReady!"],
function(StoreView, CheckBox, Memory, FilteringSelect, ValidationTextBox, Button) {
	
	signalProcessor = {
			//Edit for Dojo quiery bug for < 2.3 Core
//			dataPoint: new FilteringSelect({
//				store: stores.allDataPoints.cache,
//				labelAttr: 'extendedName'
//				}, "signalPoint"),
				
			signalLength: new ValidationTextBox({},"signalLength"),
			
			chartButton: new Button({
				label: "Generate",
				onClick: function(){
					createGraphs(signalProcessor.dataPoint.get('value'),
							signalProcessor.signalLength.get('value'));
				}
			},"chartButton"),
	};
	
	
	//********** START FOR PRE-2.3 Core Dojo Query Bug **************//
    SignalProcessingDwr.getDataPoints(function(response) {
        dojo.forEach(response.data.allDataPoints, function(item) {
            allPoints.push({
                id: item.id, 
                name: item.name, 
                enabled: item.enabled, 
                fancyName: item.name,
                dataTypeId: item.dataType
            });
        });
        

        // Create the lookup
        signalProcessor.dataPoint = new dijit.form.FilteringSelect({
            store: new dojo.store.Memory({ data: allPoints }),
            labelAttr: "fancyName",
            labelType: "html",
            searchAttr: "name",
            autoComplete: false,
            style: "width: 254px;",
            queryExpr: "*\${0}*",
            highlightMatch: "all",
            required: false,
            onChange: function(point) {
                if (this.item) {
                    selectPoint(this.item.id);
                    //UN-comment when ready to use multiple points this.reset();
                }
            }
        }, "signalPoint");  
    });
	
      //**********  END FOR PRE-2.3 Core Dojo Query Bug **************//
	
	

	
	

}); // require

/**
 * Test to see if point is in our selected list.
 * @param pointId
 * @returns {Boolean}
 */
function containsPoint(pointId) {
    return getElement(selectedPoints, pointId, "id") != null;
}
/**
 * Select a point utility
 * @param pointId
 */
function selectPoint(pointId) {
    if (!containsPoint(pointId)) {
        addToSelectedArray(pointId);
    }
}

/**
 * Add a point to the selected Array
 */
function addToSelectedArray(pointId) {
    var data = getElement(allPoints, pointId);
    
    if (data) {
        
        data.fancyName = "<span class='disabled'>"+ data.name +"</span>";
        
        // Missing names imply that the point was deleted, so ignore.
        selectedPoints[selectedPoints.length] = {
            id : pointId,
            pointName : data.name,
            enabled : data.enabled,
            dataTypeString : data.dataTypeString,
        };
    }
}

/**
 * Remove the point from the selected points
 */
function removeFromSelectedPoints(pointId) {
    removeElement(selectedPoints, pointId);
    refreshSelectedPoints();
    
    var data = getElement(allPoints, pointId);
    if (data)
        data.fancyName = data.name;
}


/**
 * Create the Signal and DFT charts
 * 
 * @param pointId
 * @param signalLength
 */
function createGraphs(pointId,signalLength){
	
	SignalProcessingDwr.dft(pointId,signalLength,function(response){
		
		signalData = response.data.signal;
		createDateGraph(signalData,'signal');
		
		dftData = response.data.fft;
		createDftGraph();
		
		
	});
}


// Wrapping in nv.addGraph allows for '0 timeout render', stores rendered charts in nv.graphs, and may do more in the future... it's NOT required

function createDateGraph(chartData, chartName){

	nv.addGraph(function() {  
	   var chart = nv.models.lineWithFocusChart();
	
	   chart.xAxis
	   .tickFormat(function(d) {
	       return d3.time.format('%H:%M:%S.%L')(new Date(d))
	     });
	   
	   chart.x2Axis
	   .tickFormat(function(d) {
	        return d3.time.format('%H:%M:%S.%L')(new Date(d))
	     });
	   
	   var x = d3.time.scale().domain([chartData[0].values[0].x, chartData[0].values[chartData[0].values.length-1].x]);
	   chart.xAxis.scale(d3.time.scale());
	   chart.x2Axis.scale(d3.time.scale());
	   

	   
	//  chart.xAxis
	//          .tickFormat(d3.format(',f'));
	  chart.yAxis
	      .tickFormat(d3.format(',.4f'));
	  chart.y2Axis
	  	  .tickFormat(d3.format(',.4f'));
	
	  	  
	  d3.select('#' + chartName + ' svg')
	      .datum(chartData)
	      .transition().duration(500)
	      .call(chart);
	
	  //TODO: Figure out a good way to do this automatically
	  nv.utils.windowResize(chart.update);
	
	  //chart.dispatch.on('stateChange', function(e) { nv.log('New State:', JSON.stringify(e)); });
	
	  return chart;
	});
}


function createDftGraph(){

	nv.addGraph(function() {  
	   var chart = nv.models.lineWithFocusChart();
	   
	   
	   
	  chart.xAxis
	          .tickFormat(d3.format(',f'));
	  chart.yAxis
	      .tickFormat(d3.format(',.4f'));
	  chart.y2Axis
	  	  .tickFormat(d3.format(',.4f'));
	
	  	  
	  d3.select('#fft svg')
	      .datum(dftData)
	      .transition().duration(500)
	      .call(chart);
	
	  //TODO: Figure out a good way to do this automatically
	  nv.utils.windowResize(chart.update);
		
	  return chart;
	});
}

function flatTestData() {
  return [
    {
      key: "Snakes",
      values: [0,1,2,3,4,5,6,7,8,9].map(function(d) {
        var currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + d);
        return {x: currentDate, y: d}
      })
    }
  ];
}
