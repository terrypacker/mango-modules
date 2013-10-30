<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Edit Div -->
<div id="editNetworkDiv" style="display: none" class="borderDiv marB marR">
  <div style="float:left">
    <tag:img src="${modulePath}/web/img/neural_networking.png" title="common.edit" />
    <span class="smallTitle"><fmt:message key="neuralnet.network.description"/></span>
    <tag:help id="neural-network-help"/>
  </div>
  
  <div style="float:right">
    <tag:img id="saveNetworkImg" png="save" onclick="networks.save()" title="common.save"/>
    <tag:img id="closeNetworkEditImg" png="cross" onclick="networks.close()" title="common.close"/>
  </div>
  
  <div class="formItem"><label class="formLabelRequired" for="networkName"><fmt:message key="common.name"/></label>
  <div class="formField"><div id="networkName"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="networkXid"><fmt:message key="common.xid"/></label>
  <div class="formField"><div id="networkXid"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="networkEnabled"><fmt:message key="common.enabled"/></label>
  <div class="formField"><div id="networkEnabled"></div></div></div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="transferFunctionPicker"><fmt:message key="neuralnet.network.properties.transferFunction"/></label>
    <div class="formField">
      <div id="transferFunctionPicker"></div><tag:help id="neural-transferFunction-help"/>
    </div>
  </div>

  <div class="formItem"><label class="formLabelRequired" for="networkProperties"><fmt:message key="neuralnet.network.properties.properties"/></label>
  <div class="formField"><div id="networkProperties"></div></div></div>


  <div class="formItem"><label class="formLabelRequired" for="networTrainingPeriodStart"><fmt:message key="neuralnet.network.properties.trainingPeriodStart"/></label>
  <div class="formField"><div id="networkTrainingPeriodStart"></div><div id="trainingStartTime"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="networkTrainingPeriodEnd"><fmt:message key="neuralnet.network.properties.trainingPeriodEnd"/></label>
  <div class="formField"><div id="networkTrainingPeriodEnd"></div><div id="trainingEndTime"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="networkMaxError"><fmt:message key="neuralnet.network.properties.maxError"/></label>
  <div class="formField"><div id="networkMaxError"></div></div></div>
  <div class="formItem"><label class="formLabelRequired" for="networkLearningRate"><fmt:message key="neuralnet.network.properties.learningRate"/></label>
  <div class="formField"><div id="networkLearningRate"></div></div></div>
  <div class="formItem"><label class="formLabelRequired" for="networkLearningMaxIterations"><fmt:message key="neuralnet.network.properties.learningMaxIterations"/></label>
  <div class="formField"><div id="networkLearningMaxIterations"></div></div></div>

  
  <div class="formItem">
    <label class="formLabelRequired" for="pointTable"><fmt:message key="neuralnet.network.properties.points"/></label>
    <div class="formField">
      <jsp:include page="${modulePath}/web/jsp/view/point/pointTable.jsp"/>
    </div>
  </div>
  
  
  <div class="formItem">
    <label class="formLabelRequired" for="hiddenLayerTable"><fmt:message key="neuralnet.network.properties.hiddenLayers"/></label>
    <div class="formField">
      <jsp:include page="${modulePath}/web/jsp/view/hiddenLayer/hiddenLayerTable.jsp"/>
    </div>
  </div>
  

  
</div>
