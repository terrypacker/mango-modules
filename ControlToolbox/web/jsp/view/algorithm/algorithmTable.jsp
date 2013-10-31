<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Table Div -->
<div id="algorithmDiv" class="borderDivPadded marB" >
  <tag:img png="icon_ds" title="dsList.dataSources"/>
  <span class="smallTitle"><fmt:message key="controltoolbox.edit.configuredAlgorithms"/></span>
  <tag:help id="control-algorithm-help"/>

  <div id="algorithmTable"></div>

  <span class="smallTitle"><fmt:message key="controltoolbox.edit.addAlgorithm"/></span>
  <tag:img png="add" title="common.add" id="addAlgorithm" onclick="algorithms.open(-1)"/>

</div>

<!-- Include the Edit Div -->
<jsp:include page="algorithmEdit.jsp"/>