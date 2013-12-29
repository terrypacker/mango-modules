<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Table Div -->
<div id="pointDiv" class="borderDivPadded marB" >
  <tag:img png="icon_ds" title="dsList.dataSources"/>
  <span class="smallTitle"><fmt:message key="controltoolbox.edit.configuredPoints"/></span>
  <tag:help id="controlPointHelp"/>

  <div id="pointTable"></div>

  <span class="smallTitle"><fmt:message key="controltoolbox.edit.addPoint"/></span>
  <tag:img png="add" title="common.add" id="addPoint" onclick="points.open(-1)"/>


</div>

<!-- Include the Edit Div -->
<jsp:include page="pointEdit.jsp"/>