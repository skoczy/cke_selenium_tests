'use strict';

var directivesModule = require('./_index.js');

/**
 * @ngInject
 */
function siteField() {
 return {
   templateUrl: 'directives/site-field.html',
   replace: false,
   scope: {
     'customField': '=',
     'site': '='
   },
   require: ['^form'],
   link: function(scope, element, attrs, ctrls) {
     scope.customField.fieldDisabled = function() {
       return scope.customField.owner != 'SITEMASTER';
     }

     scope.form = ctrls[0];
     scope.input = scope.form[scope.customField.fieldName];
   }
 };
}

directivesModule.directive('siteField', siteField);
