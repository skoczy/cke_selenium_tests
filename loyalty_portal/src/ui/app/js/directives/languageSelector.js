'use strict';

var directivesModule = require('./_index.js');

/**
 * @ngInject
 */
function languageSelector($translate, $rootScope) {
  return {
    templateUrl: 'directives/language-selector.html',
    link: function(scope, element, attrs, ctrls) {
      scope.languages = [
        {'code': 'en', 'name': 'English'},
        {'code': 'dk', 'name': 'Dansk'},
        {'code': 'et', 'name': 'Eesti'},
        {'code': 'lt', 'name': 'Lietuvių'},
        {'code': 'lv', 'name': 'Latviešu'},
        {'code': 'nb', 'name': 'Norsk (bokmål)'},
        {'code': 'pl', 'name': 'Polski'},
        {'code': 'ru', 'name': 'Русский'},
        {'code': 'sv', 'name': 'Svenska'}
      ];

      scope.userLanguage = $translate.use();

      $rootScope.$on('$translateChangeSuccess', function() {
        scope.userLanguage = $translate.use();
      });

      scope.changeLanguage = function() {
        $translate.use(scope.userLanguage);
      }
    }
  };
}

directivesModule.directive('languageSelector', languageSelector);
