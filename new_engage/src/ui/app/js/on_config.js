'use strict';

angular.module("pascalprecht.translate").factory("$translateStaticFilesLoader",["$q","$http",function(a,b){return function(c){if(!c||!angular.isString(c.prefix)||!angular.isString(c.suffix))throw new Error("Couldn't load static files, no prefix or suffix specified!");var d=a.defer();return b({url:[c.prefix,c.key,c.suffix].join(""),method:"GET",params:""}).success(function(a){d.resolve(a)}).error(function(){d.reject(c.key)}),d.promise}}]);


/**
 * @ngInject
 */
function OnConfig($stateProvider, $locationProvider, $urlRouterProvider, $translateProvider, pikadayConfigProvider) {
  $locationProvider.html5Mode({
    enabled: false,
    requireBase: false
  });

  $stateProvider
    .state('home', {
      url: '/',
      title: 'Home',
      views: {
        "sidebar": { templateUrl: "sidebars/sidebar-general.html" },
        "content": { templateUrl: "home.html", controller: 'MainController' }
      }
    })
    .state('results', {
      url: '/results',
      params: { filters: null },
      title: 'Search',
      views: {
        "sidebar": { templateUrl: "sidebars/sidebar-general.html" },
        "content": { templateUrl: "search.html", controller: 'SearchController' }
      }
    })
    .state('login', {
      url: '/login',
      title: 'Login',
      views: {
        "content": { templateUrl: "login.html", controller: 'LoginController' }
      }
    })
    .state('siteGeneralData', {
      url: '/site/:id',
      views: {
        "sidebar": { templateUrl: "sidebars/sidebar-sites.html" },
        "content": { templateUrl: "sites/generaldata.html", controller: 'SiteGeneralDataController' }
      }
    })
    .state('siteServices', {
      url: '/site/:id/services',
      views: {
        "sidebar": { templateUrl: "sidebars/sidebar-sites.html" },
        "content": { templateUrl: "sites/services.html", controller: 'SiteServicesController' }
      }
    })
    .state('siteFuels', {
      url: '/site/:id/fuels',
      views: {
        "sidebar": { templateUrl: "sidebars/sidebar-sites.html" },
        "content": { templateUrl: "sites/fuels.html", controller: 'SiteFuelsController' }
      }
    })
    .state('sitePersons', {
      url: '/site/:id/persons',
      views: {
        "sidebar": { templateUrl: "sidebars/sidebar-sites.html" },
        "content": { templateUrl: "sites/persons.html", controller: 'SitePersonsController' }
      }
    })
  ;

  $translateProvider.useStaticFilesLoader({
    prefix: '/language/',
    suffix: '.json'
  }).preferredLanguage('en')
    .fallbackLanguage('en')
    .useCookieStorage()
    .useSanitizeValueStrategy('escape');

    pikadayConfigProvider.setConfig({
      format: 'YYYY-MM-DD',
      numberOfMonths: 1
    });

  $urlRouterProvider.otherwise('/');
}

module.exports = OnConfig;
