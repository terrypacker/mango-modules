'use strict';

/* Services */
var mangoServices = angular.module('mangoServices', ['ngResource']);

mangoServices.factory('DataPoint', ['$resource',
  function($resource){
    return $resource('http://' + mangoHost + '/rest/v1/data-points/:xid', {}, {
      query: {
                method:'GET',
                params:{xid: ''},
                isArray:true,
                transformResponse: function(data,headersGetter){
                    //Quick fix to convert results into format desired
                    var result = angular.fromJson(data);
                    if(typeof result.items === 'undefined'){
                        return result;
                    }else{
                        return result.items;
                    }
                }
            },
        get: {
                method:'GET',
                isArray:false,
        }
    });
  }]).factory('CurrentValueByXid', ['$resource',
  function($resource){
    return $resource('http://' + mangoHost + '/rest/v1/realtime/by-xid/:xid', {}, {
      get: {
                method:'GET',
                isArray:false
            }
    });
  }]).factory('HistoricalValues', ['$resource',
  function($resource){
    return $resource('http://' + mangoHost + '/rest/v1/point-values/:xid', {}, {
      get: {
                params: {
                    from: new Date(new Date().getTime() - 1000*60*60*8),
                    to: new Date(),
                    rollup: 'AVERAGE',
                    timePeriodType: 'HOURS',
                    timePeriods: '1',
                    useRendered: false,
                    unitConversion: false
                },
                method:'GET',
                isArray:true
            }
    });
  }]).factory('MangoLogs', ['$resource',
  function($resource){
    return $resource('http://' + mangoHost + '/rest/v1/logging/:endpoint/:filename', {}, {
       list: {
                method:'GET',
                isArray:true,
                params: {endpoint: 'files', filename: ''}
             },
        get: {
                method: 'GET',
                isArray: true,
                params: {endpoint: 'by-filename'}
            }
    });
  }]).factory('Login', ['$resource',
  //Not working yet
  function($resource){
    return $resource('http://' + mangoHost + '/rest/v1/login/' + mangoUser, {}, {
      query: {
                method:'GET',
                headers: {'password': mangoPwd},
                isArray:false
            }
    });
  }]).factory('PointValueEvents', function(){
    return {
        register: function(xid, events, onMessage, onError, onOpen, onClose){
            
            if (!('WebSocket' in window)) {
                throw new Error('WebSocket not supported');
            }
            var host = document.location.host;
            var protocol = document.location.protocol;
    
            if (mangoHost) {
                var i = mangoHost.indexOf('//');
                if (i >= 0) {
                    protocol = mangoHost.substring(0, i);
                    host = mangoHost.substring(i+2);
                }
                else {
                    host = mangoHost;
                }
            }
    
            protocol = protocol === 'https:' ? 'wss:' : 'ws:';
    
            var socket = new WebSocket(protocol + '//' + host + '/rest/v1/websocket/point-value');
            socket.onopen = function() {
                //Register for recieving point values
                // using a PointValueRegistrationModel
                socket.send(JSON.stringify({
                    xid: xid,
                    eventTypes: events
                }));
                onOpen();
            };
            socket.onclose = onClose;
            socket.onmessage = function(event) {
                onMessage(JSON.parse(event.data));
            };
            return socket;        
        }
  }}).factory('AmChartData', function(){
    	return  {
    	    		charts: [],
    	    		configs: []
    	    	};
      });
