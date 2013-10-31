<%--
    Copyright (C) 2013 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<script type="text/javascript" src="${modulePath}/web/js/view/algorithm/properties/pidAlgorithmProperties.js"></script>

<!-- Edit Div -->
<div id="editPidAlgorithmPropertiesDiv">


  <div class="formItem"><label class="formLabelRequired" for="samplePeriod"><fmt:message key="controltoolbox.algorithm.properties.samplePeriod"/></label>
  <div class="formField"><div id="samplePeriod"></div></div></div>

  <div class="formItem"><label class="formLabelRequired" for="pValue"><fmt:message key="controltoolbox.algorithm.type.pid.pValue"/></label>
  <div class="formField"><div id="pValue"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="iValue"><fmt:message key="controltoolbox.algorithm.type.pid.iValue"/></label>
  <div class="formField"><div id="iValue"></div></div></div>
  
  <div class="formItem"><label class="formLabelRequired" for="dValue"><fmt:message key="controltoolbox.algorithm.type.pid.dValue"/></label>
  <div class="formField"><div id="dValue"></div></div></div>
  

</div>
 