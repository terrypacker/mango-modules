/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var points;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, FilteringSelect, ValidationTextBox) {
    
    NeuralNetPointDwr.getPointTypes(function(response){
    	points.pointType = new FilteringSelect({
            store: new Memory({data: response.data.pointTypes})
        }, "pointTypePicker");
    });
	
points = new StoreView({
    prefix: 'Point',
    varName: 'points',
    viewStore: stores.point,
    editStore: stores.point,
    gridId: 'pointTable',
    editId: 'editPointDiv',
    closeEditOnSave: false,
    filters: new Array(),
    defaultSort: [{attribute: "name"}],
    editUpdatesView: true,
    
    columns: {
        name: mangoMsg['common.name'],
        pointTypeString: 'Type', //TODO Fix up to use translatable message
        dataPointName: 'Point Name',
    },
    
    buttons: ['toggle', 'edit', 'delete', 'copy', 'export'],
    
    setInputs: function(vo) {
        var _this = this;
        
        this.currentId = vo.id;
        this.name.set('value', vo.name);
        this.xid.set('value', vo.xid);
        this.enabled.set('value', vo.enabled);

        this.pointType.set('value',vo.pointType);
        this.delay.set('value',vo.delay);
        if(vo.dataPointId > 0)
        	this.dataPoint.set('value',vo.dataPointId);
        else
        	this.dataPoint.reset();

        if(vo.trainingDataPointId > 0)
        	this.trainingDataPoint.set('value',vo.trainingDataPointId);
        else
        	this.trainingDataPoint.reset();

        if(vo.networkId > 0)
        	this.network.set('value',vo.networkId);
        else
        	this.network.reset();
        
        
    },
    
    getInputs: function() {
        var vo = new NeuralNetPointVO();
        vo.id = this.currentId;
        vo.name = this.name.get('value');
        vo.xid = this.xid.get('value');
        if (this.enabled.get('value')) // sometimes value is returned as "on" rather than true
            vo.enabled = true;
        else
            vo.enabled = false;
        
        vo.pointType = this.pointType.get('value');
        vo.delay = this.delay.get('value');
       	vo.dataPointId = this.dataPoint.get('value');
       	vo.trainingDataPointId = this.trainingDataPoint.get('value');
       	vo.networkId = this.network.get('value');

        
        return vo;
    },
    
    name: new ValidationTextBox({}, "pointName"),
    xid: new ValidationTextBox({}, "pointXid"),
    enabled: new CheckBox({}, "pointEnabled"),
    
    pointType: null,
    delay: new ValidationTextBox({},"pointDelay"),
    dataPoint: new FilteringSelect({
        store: stores.allDataPoints.cache
    }, "pointPicker"),

    trainingDataPoint: new FilteringSelect({
        store: stores.allDataPoints.cache
    }, "trainingDataPointId"),
    
    network: new FilteringSelect({
    	store: stores.network.cache
    }, "networkPicker"),
    
    toggle: function(id) {
        NeuralNetPointDwr.toggle(id, function(result) {
            if(result.data.enabled){
                updateImg(
                        $("togglePoint"+ result.data.id),
                        mangoImg("database_go.png"),
                        mango.i18n["common.enabledToggle"],
                        true
                );
            }else{
                updateImg(
                        $("togglePoint"+ result.data.id),
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

    
    /**
     * Refresh the Grid
     */
    refresh: function(){
    	this.grid.set('query',this.filters);
    },
    
});

}); // require
    