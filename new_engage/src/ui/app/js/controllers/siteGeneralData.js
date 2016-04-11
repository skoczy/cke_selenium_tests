'use strict';

var controllersModule = require('./_index');

/**
* @ngInject
*/
function SiteGeneralDataController($scope, FoundationApi, $stateParams, $translate, $rootScope, AppSettings, $http, $document, $timeout, CsvDownload, Sites, Dates) {
  var siteId = $stateParams.id,
      controller = this;

  $scope.siteId = siteId;
  $rootScope.siteId = siteId;

  $translate('STATUS_OPEN').then(function(open) {
    $scope.open = open.toLowerCase();
  });

  $translate('STATUS_CLOSED').then(function(closed) {
    $scope.closed = closed.toLowerCase();
  });

  // Load default site
  $scope.loadSite = Sites.find(siteId)
    .then(function(result) {
      $scope.site = result.data;
      $scope.clean = angular.copy($scope.site);
      $scope.site.openingInfo = $scope.site.openingInfo || getEmptyOpeningInfo();
      $scope.siteStatus = $scope.site.status == 'Active' ? $scope.open : $scope.closed;
      $scope.metadata = result.data.meta;

      $scope.site.fields = Sites.getDisplayFields($scope.site, $scope.metadata);

      $timeout(function() {
        $scope.setMinTempClosedFromDate();
        $scope.setMaxTempClosedToDate();
      }, 500);
    },
    function(error) {
      FoundationApi.publish('error-notifications', { title: $translate.instant('SITE_NOT_FOUND', { status: $scope.siteStatus }), autoclose: 5000  });
    });

  /**
   * Change the open status of the station.
   */
  $scope.changeSiteStatus = function() {
    $scope.site.status = $scope.site.status == 1 ? 0 : 1;
    $scope.siteStatus = $scope.site.status > 0 ? $scope.open : $scope.closed;

    FoundationApi.publish('main-notifications', { title: $translate.instant('STATION_NOW_STATUS', { status: $scope.siteStatus }), autoclose: 3000  });
  }

  /**
   * Clear the values in the temporary closed fields.
   */
  $scope.clearTempClosed = function() {
    $scope.site.openingInfo.temporarilyClosed[0].from = '';
    $scope.site.openingInfo.temporarilyClosed[0].to = '';
  }

  $scope.setMinTempClosedFromDate = function() {
    var tempClosed = $scope.site.openingInfo.temporarilyClosed[0];

    if (tempClosed.to && tempClosed.to != '') {
      $scope.tempClosedFrom.setMaxDate($scope.tempClosedTo.getDate());
    }
    else {
      $scope.tempClosedFrom.setMinDate(null);
    }
  }

  $scope.setMaxTempClosedToDate = function() {
    var tempClosed = $scope.site.openingInfo.temporarilyClosed[0];

    if (tempClosed.from && tempClosed.from != '') {
      $scope.tempClosedTo.setMinDate($scope.tempClosedFrom.getDate());
    }
    else {
      $scope.tempClosedTo.setMaxDate(null);
    }
  }

  /**
   * Setup for the time selector (dropdowns with hour).
   */
  $scope.timeEquals = function(time, count) {
    return time == count + ":00";
  }

  $scope.timeOptions = [];
  for (var i=0; i<24; i++) {
    var hour = i < 10 ? '0' + i : i;
    $scope.timeOptions.push(hour + ":00");
    $scope.timeOptions.push(hour + ":30");
  }

  /**
   * Save opening info.
   */
  $scope.saveOpeningInfo = function() {
    if (!$scope.openingInfoForm.$valid) {
      FoundationApi.publish('error-notifications', { title: $translate.instant('VALIDATION_ERRORS'), autoclose: 3000 });
      return;
    }

    var site = angular.copy($scope.site);

    if (site.openingInfo.temporarilyClosed.length == 1) {
      var tempClosed = site.openingInfo.temporarilyClosed[0];

      if (tempClosed.from == '' || tempClosed.to == '') {
        site.openingInfo.temporarilyClosed = null;
      }
      else {
        tempClosed.from = Dates.dateToArray(tempClosed.from);
        tempClosed.to = Dates.dateToArray(tempClosed.to);
      }
    }

    if (site.openingInfo.alwaysOpen) {
      site.openingInfo.openingTimes = null;
    }

    Sites.save(site)
      .then(function() {
        $scope.clean.openingInfo = site.openingInfo;
        FoundationApi.publish('main-notifications', { title: $translate.instant('OPENING_TIMES_UPDATED'), autoclose: 3000 });
      }, function(result) {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 3000 });
      });
  }

  /**
   * Changelog.
   */
  $scope.showChangelog = function() {
    FoundationApi.publish('basicModal', 'open');

    $http.get('/api/v1/res/site/' + siteId + '/changeLog')
      .then(function(result) {
        // Run through the changelog rows and gather the field info for the changed fields.
        angular.forEach(result.data, function(change) {
          change.date = Dates.arrayToDateTime(change.date);
          change.rows = [];
          change.fuels = [];
          change.services = [];

          angular.forEach(change.changes, function(row) {
            // Special handling for fuels and services
            if (row.path[0] == 'services' || row.path[0] == 'fuels') {
              if (row.path[2] == 'name') {
                change[row.path[0]].push(row);
              }

              return;
            }

            row.fieldMeta = angular.copy(Sites.getFieldMeta(row.path, $scope.metadata));

            // Quick fix for getting translated field names for composite fields
            var hiddenFields = row.path.filter(function(p) {
              return p.match(/[{}]/);
            });

            if (hiddenFields.length > 0) {
              if (row.path[1] == 'temporarilyClosed') {
                row.fieldMeta.label = $translate.instant('TEMP_CLOSED') + ' - ' + row.fieldMeta.label;
              }
              else {
                angular.forEach(hiddenFields, function(field) {
                  row.fieldMeta.label = $translate.instant(field.replace(/[{}]/g, '').toUpperCase()) + ' - ' + row.fieldMeta.label;
                });
              }
            }

            change.rows.push(row);
          });
        });

        result.data.reverse();

        $scope.changes = result.data;
      }, function() {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_GETTING_CONTENT'), autoclose: 3000 });
      });
  }

  $scope.exportChangelog = function() {
    var dataRows = [[
        $translate.instant('DATE'),
        $translate.instant('USER'),
        $translate.instant('FIELD'),
        $translate.instant('FROM'),
        $translate.instant('TO')
    ]];

    angular.forEach($scope.changes, function(change) {
      angular.forEach(change.rows, function(row) {
        dataRows.push([
          change.date,
          change.modifiedBy,
          row.fieldMeta.label,
          row.before === null ? '' : row.before,
          row.after === null ? '' : row.after
        ]);
      });

      angular.forEach(change.services, function(row) {
        dataRows.push([
          change.date,
          change.modifiedBy,
          $translate.instant('SERVICES'),
          row.before === null ? '' : row.before,
          row.after === null ? '' : row.after
        ]);
      });
    });

    CsvDownload.getCsv(dataRows, 'changelog-site-' + $scope.site.id + '.csv');
  }

  /**
   * Reset opening info.
   */
  $scope.resetOpeningInfo = function() {
    $scope.site.openingInfo = angular.copy($scope.clean.openingInfo);
  }

  $scope.$watch('site', function() {
    if ($scope.site) {
      $scope.siteFields = chunk($scope.site.fields, Math.round($scope.site.fields.length / 2));

      var tempClosed = $scope.site.openingInfo == null ? undefined : $scope.site.openingInfo.temporarilyClosed[0];

      if (tempClosed !== undefined) {
        tempClosed.from = tempClosed.from ==  null ? '' : Dates.arrayToDate(tempClosed.from);
        tempClosed.to = tempClosed.to == null ? '' : Dates.arrayToDate(tempClosed.to);
      }
    }
  });

  /**
   * Custom field editing.
   */
  $scope.saveCustomFields = function() {
    if (!$scope.customFieldsForm.$valid) {
      FoundationApi.publish('error-notifications', { title: $translate.instant('VALIDATION_ERRORS'), autoclose: 3000 });
      return;
    }

    for (var i=0; i<$scope.site.fields.length; i++) {
      var field = $scope.site.fields[i];
      setDeepValue($scope.site, field.fieldName, field.value);
    }

    var site = angular.copy($scope.site);
    site.fields = null;

    site.openingInfo = $scope.clean.openingInfo;

    Sites.save(site)
      .then(function() {
        FoundationApi.publish('main-notifications', { title: $translate.instant('CONTENT_UPDATED'), autoclose: 3000 });
      }, function(result) {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 3000 });
      });
  }

  /**
   * Get empty opening info.
   */
  function getEmptyOpeningInfo() {
    return {
      alwaysOpen: false,
      openingTimes: {
        weekdays: {open: '', close: ''},
        saturday: {open: '', close: ''},
        sunday: {open: '', close: ''}
      },
      temporarilyClosed: [
        {
          from: '',
          to: ''
        }
      ]
    };
  }

  function setDeepValue(obj, keys, val) {
		keys = keys.split('.');

  	var last = keys.pop();

  	for(var i in keys) {
  		if(!obj.hasOwnProperty(keys[i]))
  			break;
  		obj = obj[keys[i]];
  	}

  	if(obj.hasOwnProperty(last))
  		obj[last] = val;
  }

  /**
   * Reset all custom fields.
   */
  $scope.resetCustomFields = function() {
    $scope.site.fields = getDisplayFields($scope.clean, $scope.metadata);
  }

  /**
   * Get the CSV export data.
   */
  $rootScope.getCsvData = function() {
    Sites.find(siteId)
      .then(function(result) {
        var dataRows = [[], []],
            site = result.data,
            exportData = Sites.getDisplayFields(site, $scope.metadata);

        for (var i=0; i<exportData.length; i++) {
          dataRows[0].push(exportData[i].label);
          dataRows[1].push(exportData[i].value);
        }

        CsvDownload.getCsv(dataRows, 'site-' + site.id + '.csv');
      });
  }

  function chunk(arr, size) {
    var newArr = [];
    for (var i=0; i<arr.length; i+=size) {
      newArr.push(arr.slice(i, i+size));
    }
    return newArr;
  }
}

controllersModule.controller('SiteGeneralDataController', SiteGeneralDataController);
