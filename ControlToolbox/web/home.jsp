<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>

<%@page import="com.serotonin.m2m2.Constants"%>
<%@page import="com.serotonin.m2m2.Common"%>
<%@page import="com.serotonin.m2m2.view.ShareUser"%>

<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<tag:page dwr="DataPointDwr,ControlToolboxControllerDwr,ControlToolboxPointDwr,ControlToolboxAlgorithmDwr">
    <script type="text/javascript" src="${modulePath}/web/js/stores.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/controller/controllerView.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/point/pointView.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/algorithm/algorithmView.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/controltoolbox.js"></script>


       
   <div id="controlToolboxTabContainer" >
   
       <div id="controlTab">
        <jsp:include page="jsp/view/controller/controllerTable.jsp"/>
       </div>
       <div id="signalProcessingTab">
        <jsp:include page="jsp/view/signal/signalProcessor.jsp"/>
       </div>
   </div>
    
    
</tag:page>