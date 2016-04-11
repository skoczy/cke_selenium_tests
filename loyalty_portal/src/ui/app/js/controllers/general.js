'use strict';

var controllersModule = require('./_index');

/**
 * @ngInject
 */
function GeneralController($scope, $window, $location, $translate, Auth, $cookies, $http, FoundationApi, $timeout, $filter) {
  $scope.search = {};
  $scope.user = Auth.getUser();
  $scope.sidebarVisible = $cookies.get('show-sidebar') !== 'false';

  $scope.isLoggedIn = function() {
    return Auth.isLoggedIn();
  }

  $scope.logout = function() {
    Auth.logout();
    $location.path('/login');
  }

  $scope.sidebarToggle = function() {
    $scope.sidebarVisible = !$scope.sidebarVisible;
    $cookies.put('show-sidebar', $scope.sidebarVisible);
  }

  $scope.simpleSearch = function() {
    $scope.hideAdvancedSearch();

    if (parseInt($scope.search.searchValue) > 0) {
      $location.path('/site/' + $scope.search.searchValue);

      return;
    }
  }
}

controllersModule.controller('GeneralController', GeneralController);
