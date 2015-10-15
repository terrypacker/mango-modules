'use strict';

/* Filters */
/**
 * Filter to return a checkmark or an X based on a true or false
 */
angular.module('mangoFilters', []).filter('checkmark', function() {
  return function(input) {
    return input ? '\u2713' : '\u2718';
  };
});
