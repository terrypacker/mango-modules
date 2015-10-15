'use strict';

/* App Module */
var mangoApp = angular.module('mangoApp', [
    'ngRoute',
    'mangoAnimations',
    'mangoControllers',
    'mangoFilters',
    'mangoServices',
    'mangoDirectives',
    'ui.bootstrap.datetimepicker'
]);

mangoApp.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
      //Enable cross domain calls
      $httpProvider.defaults.useXDomain = true;
      //Ensure we use Cookie Based Auth
      $httpProvider.defaults.withCredentials = true;
      
      //Setup Routes
      $routeProvider.
        when('/datapoints', {
          templateUrl: 'partials/datapoint-list.html',
          controller: 'DataPointListCtrl'
        }).
        when('/datapoints/:dataPointXid', {
      	  templateUrl: 'partials/datapoint-detail.html',
      	  controller: 'DataPointDetailCtrl'
      	}).
        when('/datapoints/:dataPointXid/chart', {
      	  templateUrl: 'partials/datapoint-chart.html',
      	  controller: 'DataPointChartCtrl'
      	}).
        when('/logs', {
          templateUrl: 'partials/log-list.html',
          controller: 'MangoLogListCtrl'
        }).
        when('/logs/:logfileName', {
      	  templateUrl: 'partials/log-detail.html',
      	  controller: 'MangoLogDetailCtrl'
      	}).
      	otherwise({ redirectTo: '/datapoints'});

  }]);