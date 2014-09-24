<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>

<%@page import="com.serotonin.m2m2.Constants"%>
<%@page import="com.serotonin.m2m2.Common"%>
<%@page import="com.serotonin.m2m2.view.ShareUser"%>

<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<tag:page dwr="DataPointDwr,ControlToolboxControllerDwr,ControlToolboxPointDwr,ControlToolboxAlgorithmDwr,SignalProcessingDwr">
    <tag:versionedJavascript src="${modulePath}/web/js/stores.js" />
    <tag:versionedJavascript src="${modulePath}/web/js/view/controller/controllerView.js" />
    <tag:versionedJavascript  src="${modulePath}/web/js/view/point/pointView.js" />
    <tag:versionedJavascript  src="${modulePath}/web/js/view/algorithm/algorithmView.js" />
    <tag:versionedJavascript  src="${modulePath}/web/js/view/controltoolbox.js" />
       
   <div id="controlToolboxTabContainer" >
   
       <div id="controlTab">
        <jsp:include page="jsp/view/controller/controllerTable.jsp"/>
       </div>
       <div id="signalProcessingTab">
        <jsp:include page="jsp/view/signal/signalProcessor.jsp"/>
       </div>
   </div>
    
    
</tag:page>