<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<!-- Edit Div -->
<div id="editControllerDiv" style="display: none" class="borderDiv marB marR">
  <div style="float:left">
    <tag:img src="${modulePath}/web/img/pid_loop.png" title="common.edit" />
    <span class="smallTitle"><fmt:message key="controltoolbox.controller.description"/></span>
    <tag:help id="control-controller-help"/>
  </div>
  
  <div style="float:right">
    <tag:img id="saveControllerImg" png="save" onclick="controllers.save()" title="common.save"/>
    <tag:img id="closeControllerEditImg" png="cross" onclick="controllers.close()" title="common.close"/>
  </div>
  
  <div class="formItem"><label class="formLabelRequired" for="controllerName"><fmt:message key="common.name"/></label>
  <div class="formField"><div id="controllerName"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="conterollerXid"><fmt:message key="common.xid"/></label>
  <div class="formField"><div id="controllerXid"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="controllerEnabled"><fmt:message key="common.enabled"/></label>
  <div class="formField"><div id="controllerEnabled"></div></div></div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="controllerAlgorithmPicker"><fmt:message key="controltoolbox.controller.properties.algorithmId"/></label>
    <div class="formField">
      <div id="controllerAlgorithmPicker"></div><tag:help id="control-algorithm-help"/>
    </div>
  </div>
  
  <div class="formItem">
    <label class="formLabelRequired" for="pointTable"><fmt:message key="controltoolbox.controller.properties.points"/></label>
    <div class="formField">
      <jsp:include page="${modulePath}/web/jsp/view/point/pointTable.jsp"/>
    </div>
  </div>
  
  
  <div class="formItem">
    <label class="formLabelRequired" for="algorithmTable"><fmt:message key="controltoolbox.algorithm.description"/></label>
    <div class="formField">
      <jsp:include page="${modulePath}/web/jsp/view/algorithm/algorithmTable.jsp"/>
    </div>
  </div>
  

  
</div>
