<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Edit Div -->
<div id="editPointDiv" style="display: none" class="borderDiv marB marR">
  <div style="float:left">
    <tag:img src="${modulePath}/web/img/neural_networking.png" title="common.edit" />
    <span class="smallTitle"><fmt:message key="neuralnet.point.description"/></span>
    <tag:help id="neural-point-help"/>
  </div>
  
  <div style="float:right">
    <tag:img id="savePointImg" png="save" onclick="points.save()" title="common.save"/>
    <tag:img id="closePointEditImg" png="cross" onclick="points.close()" title="common.close"/>
  </div>
  
  <div class="formItem"><label class="formLabelRequired" for="pointName"><fmt:message key="common.name"/></label>
  <div class="formField"><div id="pointName"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="pointXid"><fmt:message key="common.xid"/></label>
  <div class="formField"><div id="pointXid"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="pointEnabled"><fmt:message key="common.enabled"/></label>
  <div class="formField"><div id="pointEnabled"></div></div></div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="pointTypePicker"><fmt:message key="neuralnet.point.properties.pointType"/></label>
    <div class="formField">
      <div id="pointTypePicker"></div>
    </div>
  </div>
  
  <div class="formItem"><label class="formLabelRequired" for="pointDelay"><fmt:message key="neuralnet.point.properties.delay"/></label>
  <div class="formField"><div id="pointDelay"></div></div></div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="pointPicker"><fmt:message key="neuralnet.point.properties.dataPointId"/></label>
    <div class="formField">
      <div id="pointPicker"></div>
    </div>
  </div>

  <div class="formItem">
    <label class="formLabelRequired" for="trainingDataPointId"><fmt:message key="neuralnet.point.properties.trainingDataPointId"/></label>
    <div class="formField">
      <div id="trainingDataPointId"></div>
    </div>
  </div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="networkPicker"><fmt:message key="neuralnet.point.properties.networkId"/></label>
    <div class="formField">
      <div id="networkPicker"></div>
    </div>
  </div>
  
</div>