/*
 * Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 *
 */
//Ensure Debugger loads the js file on browser
//@ sourceURL=/resources/pidAlgorithmProperties_ajaxLoaded.js
var algorithmProperties;

require(["deltamation/StoreView", "dijit/form/CheckBox","dojo/store/Memory",
         "dijit/form/DateTextBox","dijit/form/TimeTextBox","dijit/form/Textarea",
         "dijit/form/FilteringSelect", "dijit/form/ValidationTextBox", "dojo/domReady!"],
function(StoreView, CheckBox, Memory, DateTextBox, TimeTextBox, TextArea, FilteringSelect, ValidationTextBox) {
	
	
	var samplePeriod = new ValidationTextBox({}, "samplePeriod");
	var pValue = new ValidationTextBox({}, "pValue");
	var iValue = new ValidationTextBox({}, "iValue");
	var dValue = new ValidationTextBox({}, "dValue");
	

	algorithmProperties = {
	/**
	 * Get the values from the page
	 * 
	 * @param vo
	 */
	getAlgorithmProperties: function(vo){
		
		var properties = new PidAlgorithmProperties();
		vo.properties = properties;
		
		vo.properties.samplePeriod = samplePeriod.get('value');
		vo.properties.p = pValue.get('value');
		vo.properties.i = iValue.get('value');
		vo.properties.d = dValue.get('value');
		
	},

	/**
	 * Set the values on the page
	 * @param vo
	 */
	setAlgorithmProperties: function(vo){
		
		if(vo.properties != null){
			samplePeriod.set('value',vo.properties.samplePeriod);
			pValue.set('value',vo.properties.p);
			iValue.set('value',vo.properties.i);
			dValue.set('value',vo.properties.d);
		}
		
	},
	};
	
	
});

