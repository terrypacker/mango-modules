<%--
    Copyright (C) 2014 PlushPay Software. All rights reserved.
    @author Terry Packer
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

    <link href="${modulePath}/web/js/nv.d3.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${modulePath}/web/js/d3.v3.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/nv.d3.js"></script>
    <script type="text/javascript" src="${modulePath}/web/js/view/signal/signalProcessing.js"></script>
    
    
<style>
.chart svg {
  height: 350px;
  min-width: 100px;
  min-height: 100px;
/*
  margin: 50px;
  Minimum height and width is a good idea to prevent negative SVG dimensions...
  For example width should be =< margin.left + margin.right + 1,
  of course 1 pixel for the entire chart would not be very useful, BUT should not have errors
*/
}
</style>   

<div style='borderDiv marB marR'>
  <div class="formItem"><label class="formLabelRequired" for="signalPoint"><fmt:message key="common.point"/></label>
  <div class="formField"><div id="signalPoint"></div></div></div>

  <div class="formItem"><label class="formLabelRequired" for="signalLength"><fmt:message key="signal.signalLength"/></label>
  <div class="formField"><div id="signalLength"></div></div></div>

  <div class="formField"><div id="chartButton"></div></div>

</div>

  <div id="signal" class='chart with-transitions borderDiv marB marR'>
    <strong>Signal (with-transitions)</strong>
    <svg></svg>
  </div>
  <hr></hr>
  <div id="fft" class='chart with-transitions borderDiv marB marR'>
    <strong>DFT of Signal (with-transitions)</strong>
    <svg></svg>
  </div>
       
