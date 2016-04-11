'use strict';

var servicesModule = require('./_index.js');

/**
 * @ngInject
 */
function Auth($http) {
    this.userLoggedIn = null;

    return {
        login: function (user, password) {
            return $http({
                method: 'POST',
                url: '/api/v1/unres/login',
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: {'username': user, 'password': password},
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        },

        logout: function () {
            var user = this.userLoggedIn;
            this.userLoggedIn = null;
            return $http.post('/api/v1/unres/logout', {'username': user});
        },

        isLoggedIn: function () {
            return this.userLoggedIn != null;
        },

        getUser: function () {
            return this.userLoggedIn;
        },

        setUser: function (user) {
            this.userLoggedIn = user;
        }
    };
}

servicesModule.service('Auth', Auth);
