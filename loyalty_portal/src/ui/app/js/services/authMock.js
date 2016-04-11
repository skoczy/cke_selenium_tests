'use strict';

var servicesModule = require('./_index.js');

/**
 * @ngInject
 */
function MockAuth($http, $cookies) {
  this.userLoggedIn = $cookies.get('user');

  return  {
    login: function(user, password) {
      if (user === 'admin' && password === 'pass') {
        $cookies.put('user', 'admin');

        return true;
      }

      return false;
    },

    logout: function() {
      $cookies.remove('user');
    },

    isLoggedIn: function() {
      return $cookies.get('user');
    },

    getUser: function() {
      return $cookies.get('user');
    }
  };
}

//servicesModule.service('Auth', Auth);
