'use strict';

var controllersModule = require('./_index');

/**
* @ngInject
*/
function SiteFuelsController($scope, FoundationApi, $translate, AppSettings, $http, $stateParams, CsvDownload, Sites, $rootScope) {
  var siteId = $stateParams.id;
  $scope.siteId = siteId;
  $rootScope.siteId = siteId;

  $http.get('/api/v1/res/fuel')
    .then(function(result) {
      $scope.fuelTypes = result.data;
    });

  $scope.loadSite = Sites.find(siteId)
    .then(function(result) {
      $scope.site = result.data;
      $scope.clean = angular.copy($scope.site);
      $scope.site.fuels = $scope.site.fuels || [];
    },
    function(error) {
      FoundationApi.publish('error-notifications', { title: $translate.instant('SITE_NOT_FOUND', { status: $scope.siteStatus }), autoclose: 5000  });
    });

  $scope.getUnusedFuels = function(include) {
    if ($scope.site === undefined || $scope.site.fuels.length == 0) {
      return $scope.fuelTypes;
    }

    var fuelIds = [],
        result = [];

    angular.forEach($scope.site.fuels, function(s) {
      fuelIds.push(s.id);
    });

    angular.forEach($scope.fuelTypes, function(s) {
      if ((include !== undefined && s.id == include.id) || fuelIds.indexOf(s.id) < 0) {
        result.push(s);
      }
    });

    return result;
  }

  $scope.addFuel = function() {
    $scope.site.fuels.push($scope.getUnusedFuels()[0]);
  }

  $scope.removeFuel = function(fuel) {
    $scope.site.fuels = $scope.site.fuels.filter(function (s) {
      return s.id !== fuel.id;
    });
  }

  $scope.resetFuels = function() {
    $scope.site.fuels = $scope.clean.fuels;
  }

  $scope.saveFuels = function() {
    if ($scope.fuelsForm.$valid) {
      Sites.save($scope.site)
        .then(function(result) {
          FoundationApi.publish('main-notifications', { title: $translate.instant('CONTENT_UPDATED'), autoclose: 3000 });
        }, function(result) {
          FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 3000 });
        });
    }
    else {
      FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 3000 });
    }
  }

  /**
   * Get the CSV export data.
   */
  $rootScope.getCsvData = function() {
    Sites.find(siteId)
      .then(function(result) {
        var fuels = result.data.fuels;

        var dataRows = [[
          $translate.instant('FUEL')
        ]];

        for (var i=0; i<fuels.length; i++) {
          dataRows.push([
            fuels[i].name
          ]);
        }

        CsvDownload.getCsv(dataRows, 'site-' + siteId + '-fuels.csv');
      },
      function(error) {
        FoundationApi.publish('error-notifications', { title: $translate.instant('SITE_NOT_FOUND', { status: $scope.siteStatus }), autoclose: 5000  });
      });
  }
}

controllersModule.controller('SiteFuelsController', SiteFuelsController);
