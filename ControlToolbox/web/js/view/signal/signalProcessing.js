

//Load the data
var signalData;
var dftData;

require(["dojo/domReady!"],
   function() {

	SignalProcessingDwr.dft(463,100,function(response){
		
		signalData = response.data.signal;
		createDateGraph(signalData,'signal');
		
		dftData = response.data.fft;
		createDftGraph();
		
		
	});

}); // require



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
