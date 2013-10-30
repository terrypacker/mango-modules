<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Table Div -->
<div id="pointDiv" class="borderDivPadded marB" >
  <tag:img png="icon_ds" title="dsList.dataSources"/>
  <span class="smallTitle"><fmt:message key="neuralnet.edit.configuredPoints"/></span>
  <tag:help id="neural-point-help"/>

  <div id="pointTable"></div>

  <span class="smallTitle"><fmt:message key="neuralnet.edit.addPoint"/></span>
  <tag:img png="add" title="common.add" id="addPoint" onclick="points.open(-1)"/>


</div>

<!-- Include the Edit Div -->
<jsp:include page="pointEdit.jsp"/>
<!-- Include the Point Utility -->
<jsp:include page="pointUtility.jsp"/>