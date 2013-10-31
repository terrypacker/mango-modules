<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Edit Div -->
<div id="editAlgorithmDiv" style="display: none" class="borderDiv marB marR">
  <div style="float:left">
    <tag:img src="${modulePath}/web/img/pid_loop.png" title="common.edit" />
    <span class="smallTitle"><fmt:message key="controltoolbox.algorithm.description"/></span>
    <tag:help id="control-algorithm-help"/>
  </div>
  
  <div style="float:right">
    <tag:img id="saveAlgorithmImg" png="save" onclick="algorithms.save()" title="common.save"/>
    <tag:img id="closeAlgorithmEditImg" png="cross" onclick="algorithms.close()" title="common.close"/>
  </div>
  
  <div class="formItem"><label class="formLabelRequired" for="algorithmName"><fmt:message key="common.name"/></label>
  <div class="formField"><div id="algorithmName"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="algorithmXid"><fmt:message key="common.xid"/></label>
  <div class="formField"><div id="algorithmXid"></div></div></div>
    
  <div class="formItem">
    <label class="formLabelRequired" for="algorithmTypePicker"><fmt:message key="controltoolbox.algorithm.properties.algorithmType"/></label>
    <div class="formField">
      <div id="algorithmTypePicker"></div><tag:help id="control-algorithm-help"/>
    </div>
  </div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="algorithmPropertiesDiv"><fmt:message key="controltoolbox.algorithm.properties.properties"/></label>
    <div class="formField">
      <div id="algorithmPropertiesDiv"></div>
    </div>
  </div>
</div>
