'use strict';

var controllersModule = require('./_index');

/**
 * @ngInject
 */
function LoginController($scope, $window, $translate, $state, Auth) {
    $scope.username = '';
    $scope.password = '';

    $scope.login = function () {
        Auth.login($scope.username, $scope.password).then(
            function successCallback(response) {
                Auth.setUser(response);
                $state.go('home');
            }, function errorCallback(response) {
                $scope.errorMessage = $translate.instant('COULD_NOT_LOG_IN', { message: response.data.message });
                $scope.loginGeneralError = true;
            }
        );
    }
}

controllersModule.controller('LoginController', LoginController);
