'use strict';

var directivesModule = require('./_index.js');

directivesModule.factory('clickAnywhereElseService', function($document){
  var tracker = [];

  return function($scope, expr) {
    var i, t, len;
    for(i = 0, len = tracker.length; i < len; i++) {
      t = tracker[i];
      if(t.expr === expr && t.scope === $scope) {
        return t;
      }
    }
    var handler = function() {
      $scope.$apply(expr);
    };

    $document.on('click', handler);

    // Tear down this event handler when the scope is destroyed.
    $scope.$on('$destroy', function(){
      $document.off('click', handler);
    });

    t = { scope: $scope, expr: expr };
    tracker.push(t);
    return t;
  };
});

directivesModule.directive('clickAnywhereElse', function($document, clickAnywhereElseService){
  return {
    restrict: 'A',
    link: function(scope, elem, attr, ctrl) {
      var handler = function(e) {
        e.stopPropagation();
      };
      elem.on('click', handler);

      scope.$on('$destroy', function(){
        elem.off('click', handler);
      });

      clickAnywhereElseService(scope, attr.clickAnywhereElse);
    }
  };
});
