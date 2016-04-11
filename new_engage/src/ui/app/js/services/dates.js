'use strict';

var servicesModule = require('./_index.js');
var angular        = require('angular');

/**
* @ngInject
*/
function Dates(amMoment) {
  return  {
    self: this,

    dateToArray: function(date) {
      return date.split('-').map(Number);
    },

    arrayToDate: function(array, zeroIndexedMonth) {
      array = angular.copy(array);

      if (zeroIndexedMonth === undefined || !zeroIndexedMonth) {
        array[1] -= 1;
      }

      return amMoment.preprocessDate(array).format('YYYY-MM-DD');
    },

    arrayToDateTime: function(array) {
      return amMoment.preprocessDate(array.slice(0, 5)).format('YYYY-MM-DD HH:mm');
    },

    arrayToTime: function(array) {
      return amMoment.preprocessDate(array).format('HH:mm');
    }
  };
}

servicesModule.service('Dates', Dates);
