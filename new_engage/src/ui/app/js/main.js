'use strict';

var angular = require('angular');

// angular modules
require('angular-cookies');
require('angular-ui-router');
require('angular-translate');
require('angular-translate-storage-cookie');
require('angular-animate');
require('pikaday-angular');
require('angular-sanitize');
require('ng-csv');
require('oi.select');
require('foundation-apps');
require('foundation.mediaquery');
require('angular-moment');
require('angular-smart-table');
require('angular-busy');

require('./templates');
require('./controllers/_index');
require('./services/_index');
require('./directives/_index');

// create and bootstrap application
angular.element(document).ready(function() {

  var requires = [
    'foundation',
    'pascalprecht.translate',
    'ui.router',
    'templates',
    'app.controllers',
    'app.services',
    'app.directives',
    'ngCookies',
    'ngAnimate',
    'pikaday',
    'ngSanitize',
    'ngCsv',
    'oi.select',
    'angularMoment',
    'smart-table',
    'cgBusy'
  ];

  // mount on window for testing
  window.app = angular.module('app', requires);

  angular.module('app').constant('AppSettings', require('./constants'));

  angular.module('app').config(require('./on_config'));

  angular.module('app').run(require('./on_run'));

  angular.bootstrap(document, ['app']);

});
