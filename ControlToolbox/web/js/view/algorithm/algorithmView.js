/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var networks;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/DateTextBox","dijit/form/TimeTextBox","dijit/form/Textarea",
         "dijit/form/FilteringSelect", "dojox/layout/ContentPane", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, DateTextBox, TimeTextBox, TextArea, FilteringSelect, ContentPane, ValidationTextBox) {
    
    ControlToolboxAlgorithmDwr.getAlgorithmTypes(function(response){
    	algorithms.algorithmType = new FilteringSelect({
            store: new Memory({data: response.data.algorithmTypes})
        }, "algorithmTypePicker");
    	algorithms.algorithmType.set('value',1); //Default to something
    	algorithms.algorithmType.watch('value',function(type,oldValue,value){
    		algorithms.loadView(value); //Switch the values
    	});
    	
    });
	
	//Content Div for loading in Properties
    var algorithmPropertiesDiv = new ContentPane({
    		executeScripts: true,
    		parseOnLoad: true,
    		onDownloadError: function(error){
    			addErrorDiv(error);
    		}
    }, "algorithmPropertiesDiv");		
    
algorithms = new StoreView({
    prefix: 'Algorithm',
    varName: 'algorithms',
    viewStore: stores.algorithm,
    editStore: stores.algorithm,
    gridId: 'algorithmTable',
    editId: 'editAlgorithmDiv',
    closeEditOnSave: false,
    defaultSort: [{attribute: "name"}],
    
    columns: {
        name: mangoMsg['common.name']
    },
    
    buttons: [ 'edit', 'delete', 'copy', 'export'],
    
    setInputs: function(vo) {
        var _this = this;
        
        this.currentId = vo.id;
        this.name.set('value', vo.name);
        this.xid.set('value', vo.xid);
        this.algorithmType.set('value',vo.algorithmType);
        
        //
        algorithmProperties.setAlgorithmProperties(vo);
        
        
    },
    
    getInputs: function() {
        var vo = new ControlToolboxAlgorithmVO();
        vo.id = this.currentId;
        vo.name = this.name.get('value');
        vo.xid = this.xid.get('value');
        
        vo.algorithmType = this.algorithmType.get('value');
        
        algorithmProperties.getAlgorithmProperties(vo);
        
        return vo;
    },
    
    name: new ValidationTextBox({}, "algorithmName"),
    xid: new ValidationTextBox({}, "algorithmXid"),    
    algorithmType: null,

    
    /**
     * Used to load the properties
     */
    preOpen: function(){
    	this.loadView(this.algorithmType.get('value'));
    },
    
    
    loadViewCallback: null, // will be called after if it is set
    loadView: function(typeId,callback){
       	//Create the base URL
    	var xhrUrl = "/algorithm_properties.shtm?type=" + typeId;
    	if(typeof(algorithms.loadViewCallback) != 'undefined')
    		this.loadViewCallback = callback;
    	else
    		this.loadViewCallback = null; //ensure we clear out old ones
    	
    	var deferred = algorithmPropertiesDiv.set('href',xhrUrl); //Content Pane get
    	algorithmPropertiesDiv.set('class','borderDiv marB marR');
    	deferred.then(function(value){
//    		if(callback != null)
//    			callback();
    	},function(err){
    		addErrorDiv(err);
    	},function(update){
    		//Progress Info
    	});
    },


});

//Temp callback setup  
// to be replaced with scriptHasHooks concept from dojox/dijit content pane
dojo.connect(algorithmPropertiesDiv, "onDownloadEnd", function(){
	   init();
	   algorithmPropertiesDiv.startup();
	   algorithmPropertiesDiv.resize();
	   if(typeof(algorithms.loadViewCallback) != 'undefined')
		   algorithms.loadViewCallback();
	});


}); // require
