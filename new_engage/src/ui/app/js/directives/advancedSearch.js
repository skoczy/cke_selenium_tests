'use strict';

var directivesModule = require('./_index.js');

directivesModule.directive('advancedSearch', function($document, clickAnywhereElseService){
  return {
    restrict: 'E',
    templateUrl: 'advanced-search.html',
    controller: function($scope, $window, $location, $translate, Auth, $cookies, $http, FoundationApi, $timeout, $filter, AppSettings, $rootScope, $state) {
      $scope.savedSearches = [getEmptySavedSearch()];
      $scope.savedSearch = $scope.savedSearches[0];
      $scope.searchFilters = [];
      $scope.advancedSearch = false;
      $scope.searchFields = AppSettings.searchFilterFields;

      $http.get('/api/v1/res/predefinedSearches')
        .then(function(response) {
          $scope.savedSearches = $scope.savedSearches.concat(response.data);
        });

      $scope.addSearchFilter = function() {
        $scope.searchFilters.push(getEmptySearchFilter());
      }

      $scope.removeSearchFilter = function(index) {
        $scope.searchFilters.splice(index, 1);
      }

      $scope.toggleAdvancedSearch = function() {
        $scope.advancedSearch = !$scope.advancedSearch;
      }

      $scope.getFieldLabel = function(fieldName) {
        return AppSettings.searchMeta.fields[fieldName].label;
      }

      $scope.hideAdvancedSearch = function() {
        if ($scope.dontHide) {
          $scope.dontHide = false;
          return;
        }

        $scope.advancedSearch = false;
      }

      $scope.keepAdvancedSearch = function() {
        $scope.dontHide = true;
      }

      $scope.$watch('savedSearch', function() {
        $scope.searchFilters = $scope.savedSearch.searchFilters;
      });

      $scope.deleteSearch = function() {
        var index = getCurrentSavedSearchIndex();
        $scope.advancedSearch = true;
        $scope.dontHide = true;

        if (index == 0) {
          $scope.cancelSearch();
          return;
        }

        $http.delete('/api/v1/res/predefinedSearches/' + $scope.savedSearch.id)
          .then(function(result) {
            $scope.savedSearches.splice(index, 1);
            $scope.savedSearch = $scope.savedSearches[0];
            FoundationApi.publish('main-notifications', { title: $translate.instant('SEARCH_DELETED'), autoclose: 5000 });
          }, function(result) {
            FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_DELETING_CONTENT'), autoclose: 5000 });
          });
      }

      $scope.saveSearch = function() {
        $scope.showSaveForm = true;

        $timeout(function() {
          document.getElementById('saveSearchName').select();
        });
      }

      $scope.doSaveSearch = function() {
        $scope.savedSearch.searchFilters = $scope.searchFilters;

        $scope.savedSearches[getCurrentSavedSearchIndex()] = $scope.savedSearch;

        if ($scope.savedSearch.id === undefined) {
          $http.post('/api/v1/res/predefinedSearches', $scope.savedSearch)
            .then(function(result) {
              FoundationApi.publish('main-notifications', { title: $translate.instant('SEARCH_SAVED'), autoclose: 5000 });
              $scope.savedSearch = result.data;
              $scope.savedSearches.push($scope.savedSearch);
            }, function(result) {
              FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 5000 });
            });
          $scope.savedSearches[0] = getEmptySavedSearch();
        }
        else {
          $http.put('/api/v1/res/predefinedSearches/' + $scope.savedSearch.id, $scope.savedSearch)
            .then(function(result) {
              FoundationApi.publish('main-notifications', { title: $translate.instant('SEARCH_SAVED'), autoclose: 5000 });
            }, function(result) {
              FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 5000 });
            });
        }

        $scope.showSaveForm = false;
      }

      $scope.cancelSaveSearch = function() {
        $scope.searchFilters = $scope.savedSearches[getCurrentSavedSearchIndex()].searchFilters;
        $scope.showSaveForm = false;
      }

      function getEmptySavedSearch() {
        return {
          name: "New search",
          searchFilters: [getEmptySearchFilter()]
        };
      }

      function getEmptySearchFilter() {
        return {
          field: '',
          operator: 'EQUALS',
          value: []
        };
      }

      $scope.addItem = function(query) {
        return query;
      };

      $scope.doAdvancedSearch = function() {
        var filters = [];

        angular.forEach($scope.searchFilters, function(filter) {
          if (filter.field !== '' && filter.value.length > 0) {
            var searchFilter = {
              field: filter.field,
              operator: filter.operator,
              value: []
            };

            searchFilter.value = filter.value;

            filters.push(searchFilter);
          }
        });

        $state.go('results', { filters: filters }, { reload: true });
      };

      $scope.cancelSearch = function() {
        $scope.advancedSearch = false;
      }

      function getCurrentSavedSearchIndex() {
        return $scope.savedSearches.map(function(e) { return e.id; }).indexOf($scope.savedSearch.id);
      }

      $scope.addSearchFilter();
    }
  };
});
