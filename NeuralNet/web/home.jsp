<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>

<%@page import="com.serotonin.m2m2.Constants"%>
<%@page import="com.serotonin.m2m2.Common"%>
<%@page import="com.serotonin.m2m2.view.ShareUser"%>

<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<tag:page dwr="DataPointDwr,NeuralNetNetworkDwr,NeuralNetPointDwr,NeuralNetHiddenLayerDwr">
    <script type="text/javascript" src="${modulePath}/web/js/stores.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/network/networkView.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/point/pointView.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/point/pointUtility.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/hiddenLayer/hiddenLayerView.js"></script>
    
    
    <jsp:include page="jsp/view/network/networkTable.jsp"/>
</tag:page>