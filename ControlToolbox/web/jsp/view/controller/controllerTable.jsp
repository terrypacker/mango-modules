<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Table Div -->
<div id="networkDiv" class="borderDivPadded marB" >
  <tag:img png="icon_ds" title="dsList.dataSources"/>
  <span class="smallTitle"><fmt:message key="neuralnet.edit.configuredNetworks"/></span>
  <tag:help id="neural-network-help"/>

  <div id="networkTable"></div>

  <span class="smallTitle"><fmt:message key="neuralnet.edit.addNetwork"/></span>
  <tag:img png="add" title="common.add" id="addNetwork" onclick="networks.open(-1)"/>

</div>

<!-- Include the Edit Div -->
<jsp:include page="networkEdit.jsp"/>