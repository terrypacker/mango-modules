'use strict';

/* App Module */
var mangoApp = angular.module('mangoApp', [
    'ngRoute',
    'mangoAnimations',
    'mangoControllers',
    'mangoFilters',
    'mangoServices',
    'mangoDirectives',
    'ui.bootstrap'
]);

mangoApp.config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
      //Enable cross domain calls
      $httpProvider.defaults.useXDomain = true;
      //Ensure we use Cookie Based Auth
      $httpProvider.defaults.withCredentials = true;
      
      //Setup Routes
      $routeProvider.
      	when('/dashboard', {
          templateUrl: 'partials/dashboard.html',
          controller: 'DashboardCtrl'
        }).
        when('/datapoints', {
          templateUrl: 'partials/datapoint-list.html',
          controller: 'DataPointListCtrl'
        }).
        when('/datapoints_carousel', {
            templateUrl: 'partials/datapoint-carousel.html',
            controller: 'DataPointCarouselCtrl'
        }).
        when('/datapoints/:dataPointXid', {
      	  templateUrl: 'partials/datapoint-detail.html',
      	  controller: 'DataPointDetailCtrl'
      	}).
        when('/datapoints/:dataPointXid/chart', {
      	  templateUrl: 'partials/datapoint-chart.html',
      	  controller: 'DataPointChartCtrl'
      	}).
      	when('/explore', {
        	  templateUrl: 'partials/explorer.html',
        	  controller: 'ExplorerCtrl'
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