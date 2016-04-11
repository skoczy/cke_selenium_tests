'use strict';

var controllersModule = require('./_index');

/**
* @ngInject
*/
function SearchController($scope, $http, Sites, $location, AppSettings, $rootScope, $stateParams, FoundationApi, $translate, CsvDownload, $window) {
  $scope.headers = [];
  $scope.perPage = getPerPage();
  $scope.gridOptions = {
    exporterMenuCsv: false,
    exporterMenuPdf: false,
    onRegisterApi: function(gridApi){
      $scope.gridApi = gridApi;
    }
  };

  if ($stateParams.filters !== null) {
    $http.post('/api/v1/res/site/search', $stateParams.filters)
      .then(function(result) {
        setupResults(result.data);
      }, function() {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SEARCHING'), autoclose: 3000 });
      });
  }
  else {
    $http.get('/api/v1/res/site')
      .then(function(result) {
        setupResults(result.data);
      }, function() {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SEARCHING'), autoclose: 3000 });
      });
  }

  function setupResults(results) {
    $scope.resultCount = results.length;
    $scope.perPage = getPerPage();
    console.log($scope.perPage);

    if (results.length == 0) {
      return;
    }

    var searchResults = results,
        data = [],
        displayFields;

    angular.forEach(results, function(site, key) {
      displayFields = Sites.getDisplayFields(
        site,
        {
          types: AppSettings.searchMeta.types,
          fields: AppSettings.searchMeta.fields
        },
        AppSettings.searchFields
      );

      var siteRow = {};
      for (var i=0; i<displayFields.length; i++) {
        siteRow[$scope.getFieldName(displayFields[i].fieldName)] = displayFields[i].value;
      }

      siteRow.id = site.id;

      data.push(siteRow);
    });

    $scope.headers = Sites.getDisplayFields(
      results[0],
      {
        types: AppSettings.searchMeta.types,
        fields: AppSettings.searchMeta.fields
      },
      AppSettings.searchFields
    );

    for (var i=0; i<$scope.headers.length; i++) {
      $scope.headers[i].active = true;
    }

    $scope.results = data;
  }

  $scope.getFieldName = function(fieldName) {
    return fieldName.replace(/\./, '');
  }

  $scope.getFieldValue = function(site, fieldName) {
    return site[$scope.getFieldName(fieldName)];
  }

  $scope.activeHeaders = function() {
    return $scope.headers.filter(function(field) {
      return field.active;
    });
  }

  $scope.getCsvData = function() {
    var fields = $scope.activeHeaders(),
        sites = $scope.results,
        dataRows = [[]];

    for (var i=0; i<sites.length; i++) {
      var row = [];

      for (var j=0; j<fields.length; j++) {
        if (i == 0) {
          dataRows[0].push(fields[j].label);
        }

        row.push($scope.getFieldValue(sites[i], fields[j].fieldName));
      }

      dataRows.push(row);
    }

    CsvDownload.getCsv(dataRows, 'search-result.csv');
  }

  $scope.gotoSite = function(siteId) {
    $location.path('/site/' + siteId);
  }

  var w = angular.element($window);
  w.bind('resize', setupPagination);

  function setupPagination() {
    $scope.$apply(function() {
      $scope.perPage = getPerPage();
    });
  }

  function getPerPage() {
    var mainContent = document.getElementById('content'),
        header = document.getElementById('search-header'),
        results = document.getElementById('search-results'),
        resultsHeader = document.getElementById('search-results-header'),
        resultsHeaderHeight = resultsHeader ? resultsHeader.offsetHeight : 45,
        rows = document.querySelector('#search-results td'),
        rowHeight = rows === null || rows.length == 0 ? 45 : rows.offsetHeight,
        availableSpace = mainContent.offsetHeight - (header.offsetHeight + resultsHeaderHeight),
        rowsAvailable = Math.floor(availableSpace / rowHeight) - 3;

    return rowsAvailable;
  }
}

controllersModule.controller('SearchController', SearchController);
