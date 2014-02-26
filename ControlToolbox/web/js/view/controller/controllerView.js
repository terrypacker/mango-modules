/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var networks;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/DateTextBox","dijit/form/TimeTextBox","dijit/form/Textarea",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, DateTextBox, TimeTextBox, TextArea, FilteringSelect, ValidationTextBox) {
	
controllers = new StoreView({
    prefix: 'Controller',
    varName: 'controllers',
    viewStore: stores.controller,
    editStore: stores.controller,
    gridId: 'controllerTable',
    editId: 'editControllerDiv',
    closeEditOnSave: false,
    defaultSort: [{attribute: "name"}],
    defaultQuery: {},
    
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
        
        if(vo.algorithmId > 0)
        	this.algorithm.set('value',vo.algorithmId);
        else
        	this.algorithm.reset();
        
        
    },
    
    getInputs: function() {
        var vo = new ControlToolboxControllerVO();
        vo.id = this.currentId;
        vo.name = this.name.get('value');
        vo.xid = this.xid.get('value');
        if (this.enabled.get('value')) // sometimes value is returned as "on" rather than true
            vo.enabled = true;
        else
            vo.enabled = false;
        
        vo.algorithmId = this.algorithm.get('value');
        
        return vo;
    },
    
    name: new ValidationTextBox({}, "controllerName"),
    xid: new ValidationTextBox({}, "controllerXid"),
    enabled: new CheckBox({}, "controllerEnabled"),
    
    algorithm: new FilteringSelect({
        store: stores.algorithm.cache
    }, "controllerAlgorithmPicker"),
    
    toggle: function(id) {
        ControlToolboxControllerDwr.toggle(id, function(result) {
            if(result.data.enabled){
                updateImg(
                        $("toggleController"+ result.data.id),
                        mangoImg("database_go.png"),
                        mango.i18n["common.enabledToggle"],
                        true
                );
            }else{
                updateImg(
                        $("toggleController"+ result.data.id),
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
    

    openCallback: function(){
    	points.filters['controllerId'] = this.currentId;
    	points.refresh();
    },
    
    closeCallback: function(){
    	delete points.filters['controllerId'];
    	points.refresh();

     },
    
    /**
     * Refresh the Grid
     */
    refresh: function(){
        this.grid.set('query', null);
    },

});

    controllers.refresh();

}); // require
