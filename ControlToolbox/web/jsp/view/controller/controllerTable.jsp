<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Table Div -->
<div id="controllerDiv" class="borderDivPadded marB" >
  <tag:img png="icon_ds" title="dsList.dataSources"/>
  <span class="smallTitle"><fmt:message key="controltoolbox.edit.configuredControllers"/></span>
  <tag:help id="control-controller-help"/>

  <div id="controllerTable"></div>

  <span class="smallTitle"><fmt:message key="controltoolbox.edit.addController"/></span>
  <tag:img png="add" title="common.add" id="addController" onclick="controllers.open(-1)"/>

</div>

<!-- Include the Edit Div -->
<jsp:include page="controllerEdit.jsp"/>