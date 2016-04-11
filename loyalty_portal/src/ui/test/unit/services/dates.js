/*global angular */

'use strict';

describe('Dates', function() {
  var datesService,
      dateArray = [2015, 6, 14],
      dateArrayZeroMonth = [2015, 5, 14],
      dateString = '2015-06-14';

  beforeEach(function() {
    // instantiate the app module
    angular.mock.module('app');

    // mock the directive
    angular.mock.inject(function(Dates) {
      datesService = Dates;
    });
  });

  it('should exist', function() {
    expect(datesService).toBeDefined();
  });

  it('should convert a date array to a d.m.Y string', function() {
    expect(datesService.arrayToDate(dateArray)).toEqual(dateString);
    expect(datesService.arrayToDate(dateArrayZeroMonth, true)).toEqual(dateString);
  });

  it('should convert a date to a date array', function() {
    expect(datesService.dateToArray(dateString)).toEqual(dateArray);
    expect(datesService.dateToArray(datesService.arrayToDate(dateArray))).toEqual(dateArray);

  });
});
