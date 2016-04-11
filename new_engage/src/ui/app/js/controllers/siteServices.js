'use strict';

var controllersModule = require('./_index');

/**
* @ngInject
*/
function SiteServicesController($scope, FoundationApi, $translate, AppSettings, $http, $stateParams, CsvDownload, Sites, $rootScope) {
  var siteId = $stateParams.id;
  $scope.siteId = siteId;
  $rootScope.siteId = siteId;

  $http.get('/api/v1/res/service')
    .then(function(result) {
      $scope.serviceTypes = result.data;
    });

  $scope.loadSite = Sites.find(siteId)
    .then(function(result) {
      $scope.site = result.data;
      $scope.clean = angular.copy($scope.site);
      $scope.site.services = $scope.site.services || [];
    },
    function(error) {
      FoundationApi.publish('error-notifications', { title: $translate.instant('SITE_NOT_FOUND', { status: $scope.siteStatus }), autoclose: 5000  });
    });

  $scope.getUnusedServices = function(include) {
    if ($scope.site === undefined || $scope.site.services.length == 0) {
      return $scope.serviceTypes;
    }

    var serviceIds = [],
        result = [];

    angular.forEach($scope.site.services, function(s) {
      serviceIds.push(s.id);
    });

    angular.forEach($scope.serviceTypes, function(s) {
      if ((include !== undefined && s.id == include.id) || serviceIds.indexOf(s.id) < 0) {
        result.push(s);
      }
    });

    return result;
  }

  $scope.addService = function() {
    $scope.site.services.push($scope.getUnusedServices()[0]);
  }

  $scope.removeService = function(service) {
    $scope.site.services = $scope.site.services.filter(function (s) {
      return s.id !== service.id;
    });
  }

  $scope.resetServices = function() {
    $scope.site.services = $scope.clean.services;
  }

  $scope.saveServices = function() {
    if ($scope.servicesForm.$valid) {
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
        var services = result.data.services;

        var dataRows = [[
          $translate.instant('SERVICE')
        ]];

        for (var i=0; i<services.length; i++) {
          dataRows.push([
            services[i].name
          ]);
        }

        CsvDownload.getCsv(dataRows, 'site-' + siteId + '-services.csv');
      },
      function(error) {
        FoundationApi.publish('error-notifications', { title: $translate.instant('SITE_NOT_FOUND', { status: $scope.siteStatus }), autoclose: 5000  });
      });
  }
}

controllersModule.controller('SiteServicesController', SiteServicesController);
