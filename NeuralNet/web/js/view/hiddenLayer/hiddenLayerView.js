/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var points;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, FilteringSelect, ValidationTextBox) {

	
hiddenLayers = new StoreView({
    prefix: 'HiddenLayer',
    varName: 'hiddenLayers',
    viewStore: stores.layer,
    editStore: stores.layer,
    gridId: 'hiddenLayerTable',
    editId: 'editHiddenLayerDiv',
    closeEditOnSave: false,
    filters: new Array(),
    defaultSort: [{attribute: "name"}],
    editUpdatesView: true,
    
    columns: {
        name: mangoMsg['common.name'],
        networkName: 'Network',
        layerNumber: 'Number', //TODO Fix up to use translatable message
        numberOfNeurons: 'Neurons',
    },
    
    buttons: ['edit', 'delete', 'copy', 'export'],
    
    setInputs: function(vo) {
        var _this = this;
        
        this.currentId = vo.id;
        this.name.set('value', vo.name);
        this.xid.set('value', vo.xid);
 
        this.layerNumber.set('value',vo.layerNumber);
        this.numberOfNeurons.set('value',vo.numberOfNeurons);
        
        if(vo.networkId > 0)
        	this.network.set('value',vo.networkId);
        else
        	this.network.reset();
        
        
    },
    
    getInputs: function() {
        var vo = new NeuralNetHiddenLayerVO();
        vo.id = this.currentId;
        vo.name = this.name.get('value');
        vo.xid = this.xid.get('value');
        
        vo.layerNumber = this.layerNumber.get('value');
        vo.numberOfNeurons = this.numberOfNeurons.get('value');
       	vo.networkId = this.network.get('value');
        
        return vo;
    },
    
    name: new ValidationTextBox({}, "HiddenLayerName"),
    xid: new ValidationTextBox({}, "HiddenLayerXid"),
    
    layerNumber: new ValidationTextBox({},"HiddenLayerLayerNumber"),
    numberOfNeurons: new ValidationTextBox({},"HiddenLayerNumberOfNeurons"),
    
    network: new FilteringSelect({
    	store: stores.network.cache
    }, "HiddenLayerNetworkPicker"),
    
    
    /**
     * Refresh the Grid
     */
    refresh: function(){
    	this.grid.set('query',this.filters);
    },
    
});

}); // require
    