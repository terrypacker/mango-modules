<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Edit Div -->
<div id="editHiddenLayerDiv" style="display: none" class="borderDiv marB marR">
  <div style="float:left">
    <tag:img src="${modulePath}/web/img/neural_networking.png" title="common.edit" />
    <span class="smallTitle"><fmt:message key="neuralnet.hiddenlayer.description"/></span>
    <tag:help id="neural-hiddenLayer-help"/>
  </div>
  
  <div style="float:right">
    <tag:img id="saveHiddenLayerImage" png="save" onclick="hiddenLayers.save()" title="common.save"/>
    <tag:img id="closeHiddenLayerEditImg" png="cross" onclick="hiddenLayers.close()" title="common.close"/>
  </div>
  
  <div class="formItem"><label class="formLabelRequired" for="HiddenLayerName"><fmt:message key="common.name"/></label>
  <div class="formField"><div id="HiddenLayerName"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="HiddenLayerXid"><fmt:message key="common.xid"/></label>
  <div class="formField"><div id="HiddenLayerXid"></div></div></div>
  
  
  <div class="formItem"><label class="formLabelRequired" for="HiddenLayerLayerNumber"><fmt:message key="neuralnet.hiddenlayer.properties.layerNumber"/></label>
  <div class="formField"><div id="HiddenLayerLayerNumber"></div></div></div>

  <div class="formItem"><label class="formLabelRequired" for="HiddenLayerNumberOfNeurons"><fmt:message key="neuralnet.hiddenlayer.properties.numberOfNeurons"/></label>
  <div class="formField"><div id="HiddenLayerNumberOfNeurons"></div></div></div>

  <div class="formItem">
    <label class="formLabelRequired" for="HiddenLayerNetworkPicker"><fmt:message key="neuralnet.hiddenlayer.properties.networkId"/></label>
    <div class="formField">
      <div id="HiddenLayerNetworkPicker"></div>
    </div>
  </div>
  
</div>