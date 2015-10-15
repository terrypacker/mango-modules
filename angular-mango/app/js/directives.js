'use strict';

/* Directives */
/**
 * Directive to Display a Data Point
 */
angular.module('mangoDirectives', [])
.directive('mangoDatapointView', function() {
  return {
    templateUrl: 'directives/mango-datapoint-view.html'
  };
}).directive('mangoDatapointQuickview', function() {
  return {
    templateUrl: 'directives/mango-datapoint-quickview.html'
  };
}).directive('amChart',
   function () {
       return {
           restrict: 'E',
           replace:true,
          
           template: '<div class="amchart"></div>',
           link: function ($scope, $el, attrs) {
            var id = $el[0].id;
             
            // we can't render a chart without any data
            if ($scope.amChartOptions.dataProvider) {
                var renderChart = function (amChartOptions) {
                    var o = amChartOptions || $scope.amChartOptions;
                    // set height and width
                    var height = $scope.height || '400px';
                    var width = $scope.width || '100%';
                    $el.css({
                        'height': height,
                        'width': width
                    });
                    $scope.amChart = AmCharts.makeChart(id, o);
             
                }; //End Render Chart
                renderChart();
            }
         }//end watch
       }
   });