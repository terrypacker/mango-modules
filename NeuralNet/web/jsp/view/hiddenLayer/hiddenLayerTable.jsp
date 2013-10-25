<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Table Div -->
<div id="hiddenLayerDiv" class="borderDivPadded marB" >
  <tag:img png="icon_ds" title="dsList.dataSources"/>
  <span class="smallTitle"><fmt:message key="neuralnet.edit.configuredLayers"/></span>
  <tag:help id="neural-hiddenLayer-help"/>

  <div id="hiddenLayerTable"></div>

  <span class="smallTitle"><fmt:message key="neuralnet.edit.addHiddenLayer"/></span>
  <tag:img png="add" title="common.add" id="addHiddenLayer" onclick="hiddenLayers.open(-1)"/>
</div>

<!-- Include the Edit Div -->
<jsp:include page="hiddenLayerEdit.jsp"/>