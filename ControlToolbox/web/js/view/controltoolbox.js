/* Copyright (C) 2013 PlushPay Software. All rights reserved.
 * @author Terry Packer
 */
var controlToolbox;

require(["dijit/layout/TabContainer","dijit/layout/ContentPane", "dojo/domReady!"],
function(TabContainer, ContentPane) {

		var tc = new TabContainer({
			style: "height: auto",
			doLayout: false,
		}, "controlToolboxTabContainer");

		var cp1 = new ContentPane({
			title: "Control"
		}, "controlTab");
		
		var cp2 = new ContentPane({
			title: "Signal Processing"
		}, "signalProcessingTab");
		
		tc.addChild(cp1);
		tc.addChild(cp2);
		
		tc.startup();

}); // require
