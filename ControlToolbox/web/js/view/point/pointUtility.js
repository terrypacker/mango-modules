require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, FilteringSelect, ValidationTextBox) {

	pointUtility = {
		
		delayStart: new ValidationTextBox({},"networkPointUtility_pointDelayStart"),
		
		numberOfPoints: new ValidationTextBox({},"networkPointUtility_numberOfPoints"),
	    
		dataPoint: new FilteringSelect({
	        store: stores.allDataPoints.cache
	    }, "networkPointUtility_pointPicker"),
		    
		trainingDataPoint: new FilteringSelect({
	        store: stores.allDataPoints.cache
	    }, "networkPointUtility_trainingDataPointId"),
			
	    network: new FilteringSelect({
	    	store: stores.network.cache
	    }, "networkPointUtility_networkPicker"),
	    
	    
	    
	    
	    
		createDelayedInputPoints: function(){
			NeuralNetPointDwr.createDelayedInputPoints(
					this.network.get('value'),
					this.dataPoint.get('value'),
					this.trainingDataPoint.get('value'),
					this.numberOfPoints.get('value'),
					this.delayStart.get('value'),
					function(response){
						points.refresh();
					});
		},
			
			
	};

}); // require