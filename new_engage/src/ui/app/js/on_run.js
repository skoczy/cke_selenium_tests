'use strict';

/**
* @ngInject
*/
function OnRun($rootScope, AppSettings, Auth, $location) {
  // change page title based on state
  $rootScope.$on('$stateChangeSuccess', function(event, toState) {
    $rootScope.controller = toState.name;
    $rootScope.pageTitle = '';

    if ( toState.title ) {
      $rootScope.pageTitle += toState.title;
      $rootScope.pageTitle += ' \u2014 ';
    }

    $rootScope.pageTitle += AppSettings.appTitle;
  });

  // Check for login status when changing page URL
  $rootScope.$on('$locationChangeStart', function (event, next) {
    if (!Auth.isLoggedIn()) {
      $location.path('/login');
    }
  });

  $rootScope.$on('$routeChangeSuccess', function(ev, data) {
    if (data.$route && data.$route.controller) {
      $rootScope.controller = data.$route.controller;
    }
  });
}

module.exports = OnRun;
