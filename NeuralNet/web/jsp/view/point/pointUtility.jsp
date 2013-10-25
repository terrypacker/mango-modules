<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<div id="addNetworkPointsUtility" class="borderDivPadded marB">
  <span class="smallTitle"><fmt:message key="neuralnet.point.utility.description"/></span>
  
  <tag:img png="add" title="common.add" id="createDelayedInputPoints" onclick="pointUtility.createDelayedInputPoints()"/>
  
  <div class="formItem"><label class="formLabelRequired" for="networkPointUtility_numberOfPoints"><fmt:message key="neuralnet.point.utility.numberOfPoints"/></label>
  <div class="formField"><div id="networkPointUtility_numberOfPoints"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="networkPointUtility_pointDelayStart"><fmt:message key="neuralnet.point.utility.delayStart"/></label>
  <div class="formField"><div id="networkPointUtility_pointDelayStart"></div></div></div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="networkPointUtility_pointPicker"><fmt:message key="neuralnet.point.properties.dataPointId"/></label>
    <div class="formField">
      <div id="networkPointUtility_pointPicker"></div>
    </div>
  </div>

  <div class="formItem">
    <label class="formLabelRequired" for="networkPointUtility_trainingDataPointId"><fmt:message key="neuralnet.point.properties.trainingDataPointId"/></label>
    <div class="formField">
      <div id="networkPointUtility_trainingDataPointId"></div>
    </div>
  </div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="networkPointUtility_networkPicker"><fmt:message key="neuralnet.point.properties.networkId"/></label>
    <div class="formField">
      <div id="networkPointUtility_networkPicker"></div>
    </div>
  </div>
</div>