/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var points;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, FilteringSelect, ValidationTextBox) {
    
    ControlToolboxPointDwr.getPointTypes(function(response){
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
        
        this.pointType.set('value', vo.pointType);
        this.delay.set('value',vo.delay);
        this.highLimit.set('value', vo.highLimit);
        this.lowLimit.set('value', vo.lowLimit);
        
        
        if(vo.dataPointId > 0)
        	this.dataPoint.set('value',vo.dataPointId);
        else
        	this.dataPoint.reset();


        if(vo.controllerId > 0)
        	this.controller.set('value',vo.controllerId);
        else
        	this.controller.reset();
        
    },
    
    getInputs: function() {
        var vo = new ControlToolboxPointVO();
        vo.id = this.currentId;
        vo.name = this.name.get('value');
        vo.xid = this.xid.get('value');
        if (this.enabled.get('value')) // sometimes value is returned as "on" rather than true
            vo.enabled = true;
        else
            vo.enabled = false;
        
        vo.pointType = this.pointType.get('value');
        vo.delay = this.delay.get('value');
        vo.highLimit = this.highLimit.get('value');
        vo.lowLimit = this.lowLimit.get('value');
        
       	vo.dataPointId = this.dataPoint.get('value');
       	vo.controllerId = this.controller.get('value');

        
        return vo;
    },
    
    name: new ValidationTextBox({}, "pointName"),
    xid: new ValidationTextBox({}, "pointXid"),
    enabled: new CheckBox({}, "pointEnabled"),
    
    pointType: null,
    delay: new ValidationTextBox({},"pointDelay"),
    highLimit: new ValidationTextBox({},"pointHighLimit"),
    lowLimit: new ValidationTextBox({},"pointLowLimit"),
    
    dataPoint: new FilteringSelect({
        store: stores.allDataPoints.cache
    }, "pointPicker"),

    
    controller: new FilteringSelect({
    	store: stores.controller.cache
    }, "controllerPicker"),
    
    toggle: function(id) {
        ControlToolboxPointDwr.toggle(id, function(result) {
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

points.refresh();

}); // require
    