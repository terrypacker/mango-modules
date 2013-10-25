/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var networks;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/DateTextBox","dijit/form/TimeTextBox","dijit/form/Textarea",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, DateTextBox, TimeTextBox, TextArea, FilteringSelect, ValidationTextBox) {
    
    NeuralNetNetworkDwr.getTransferFunctionTypes(function(response){
    	networks.transferFunctionType = new FilteringSelect({
            store: new Memory({data: response.data.transferFunctionTypes})
        }, "transferFunctionPicker");
    });
	
networks = new StoreView({
    prefix: 'Network',
    varName: 'networks',
    viewStore: stores.network,
    editStore: stores.network,
    gridId: 'networkTable',
    editId: 'editNetworkDiv',
    closeEditOnSave: false,
    defaultSort: [{attribute: "name"}],
    
    columns: {
        name: mangoMsg['common.name']
    },
    
    buttons: ['toggle', 'edit', 'delete', 'copy', 'export'],
    
    setInputs: function(vo) {
        var _this = this;
        
        this.currentId = vo.id;
        this.name.set('value', vo.name);
        this.xid.set('value', vo.xid);
        this.enabled.set('value', vo.enabled);
        this.transferFunctionType.set('value',vo.transferFunctionType);
        this.maxError.set('value',vo.maxError);
        this.learningRate.set('value',vo.learningRate);
        this.learningMaxIterations.set('value',vo.learningMaxIterations);
        
        if(vo.trainingPeriodStart <= 0){
        	var startDate = new Date();
        	this.trainingStartTime.set('value',startDate);
        	this.trainingStartDate.set('value',startDate);       	
        }else{
        	var startDate = new Date(vo.trainingPeriodStart);
        	this.trainingStartTime.set('value',startDate);
        	this.trainingStartDate.set('value',startDate);
        }

        if(vo.trainingPeriodEnd <= 0){
        	var endDate = new Date();
        	this.trainingEndTime.set('value',endDate);
        	this.trainingEndDate.set('value',endDate);       	
        }else{
        	var endDate = new Date(vo.trainingPeriodEnd);
        	this.trainingEndTime.set('value',endDate);
        	this.trainingEndDate.set('value',endDate);
        }
        this.properties.set('value',vo.propertiesString);
        
    },
    
    getInputs: function() {
        var vo = new NeuralNetNetworkVO();
        vo.id = this.currentId;
        vo.name = this.name.get('value');
        vo.xid = this.xid.get('value');
        if (this.enabled.get('value')) // sometimes value is returned as "on" rather than true
            vo.enabled = true;
        else
            vo.enabled = false;
        
        vo.transferFunctionType = this.transferFunctionType.get('value');
        vo.maxError = this.maxError.get('value');
        vo.learningRate = this.learningRate.get('value');
        vo.learningMaxIterations = this.learningMaxIterations.get('value');
        
        vo.trainingPeriodStart = createDateTime(this.trainingStartDate.get('value'),this.trainingStartTime.get('value')).getTime();
        vo.trainingPeriodEnd = createDateTime(this.trainingEndDate.get('value'),this.trainingEndTime.get('value')).getTime();
        
        vo.propertiesString = this.properties.get('value');
        
        return vo;
    },
    
    name: new ValidationTextBox({}, "networkName"),
    xid: new ValidationTextBox({}, "networkXid"),
    enabled: new CheckBox({}, "networkEnabled"),
    
    transferFunctionType: null,
    maxError: new ValidationTextBox({},"networkMaxError"),
    learningRate: new ValidationTextBox({},"networkLearningRate"),
    learningMaxIterations: new ValidationTextBox({},"networkLearningMaxIterations"),
    
    trainingStartDate: new DateTextBox({
	    value: null,
	    style: "width: 10em; color: gray",
	}, "networkTrainingPeriodStart"),
    trainingStartTime: new TimeTextBox({
		value: null,
		style: "width: 10em; color: gray",
	}, "trainingStartTime"),

    trainingEndDate: new DateTextBox({
	    value: null,
	    style: "width: 10em; color: gray",
	}, "networkTrainingPeriodEnd"),
    trainingEndTime: new TimeTextBox({
		value: null,
		style: "width: 10em; color: gray",
	}, "trainingEndTime"),
	
	properties: new TextArea({
		value: null,
	}, "networkProperties"),
    
    toggle: function(id) {
        NeuralNetNetworkDwr.toggle(id, function(result) {
            if(result.data.enabled){
                updateImg(
                        $("toggleNetwork"+ result.data.id),
                        mangoImg("database_go.png"),
                        mango.i18n["common.enabledToggle"],
                        true
                );
            }else{
                updateImg(
                        $("toggleNetwork"+ result.data.id),
                        mangoImg("database_stop.png"),
                        mango.i18n["common.enabledToggle"],
                        true
                );
            }
        });
    },
    
    preInit: function() {
        this.imgMap.toggleOn = mangoImg("database_go.png");
        this.imgMap.toggleOff = mangoImg("database_stop.png");
    },
    
//    /**
//     * Replace the open call with one that has a callback
//     */
//    openImpl: this.open;
//    
//    open: function(id, options) {
//    	if(options){
//    		options.callback = this.openCallback;
//    	}else{
//    		options = {callback: this.openCallback};
//    	}
//    	this.openImpl(id,options);
//    }
    
    openCallback: function(){
    	points.filters['networkId'] = this.currentId;
    	points.refresh();
    	hiddenLayers.filters['networkId'] = this.currentId;
    	hiddenLayers.refresh();
    	
    },
    
    closeCallback: function(){
    	delete points.filters['networkId'];
    	points.refresh();
    	delete hiddenLayers.filters['networkId'];
    	hiddenLayers.refresh();

     }
    
});


/**
 * Create a date from the 
 */
function createDateTime(date,time){
	
	if(date==null && time==null)
		return null;
	if(date == null)
		date = new Date(); //Default to today
	if(time == null)
		time = new Date(date.getFullYear(), date.getMonth(), date.getDate(),
				0,0,0,0); //Default to Midnight
	return new Date(
			date.getFullYear(), date.getMonth(), date.getDate(),
			time.getHours(), time.getMinutes(), time.getSeconds(),
			time.getMilliseconds()
			);
}


}); // require
