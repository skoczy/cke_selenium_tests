'use strict';

var controllersModule = require('./_index');

/**
* @ngInject
*/
function SitePersonsController($scope, FoundationApi, $translate, AppSettings, $http, $stateParams, CsvDownload, Sites, $rootScope) {
  var siteId = $stateParams.id;
  $scope.siteId = siteId;
  $rootScope.siteId = siteId;
  $scope.persons = [];
  $scope.sitePersonRoles = AppSettings.sitePersonRoles;

  $scope.getPersons = function() {
    $scope.loadSite = $http.get('/api/v1/res/site/' + siteId + '/sitepersons')
      .then(function(result) {
        $scope.persons = result.data;
      }, function(result) {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_LOADING_CONTENT'), autoclose: 3000 });
        $scope.persons = [];
      });
  }

  Sites.find(siteId)
    .then(function(result) {
      $scope.site = result.data;
    }, function(result) {
      FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_LOADING_CONTENT'), autoclose: 3000 });
      $scope.persons = [];
    });

  $scope.getPersons();

  $scope.addPerson = function() {
    $scope.persons.push({
      role: '',
      name: '',
      phonecc: '',
      phone: '',
      email: ''
    });
  }

  $scope.removePerson = function(person) {
    $scope.persons = $scope.persons.filter(function (p) {
      return p.role !== person.role
        || p.name !== person.name
        || p.phone !== person.phone
        || p.email !== person.email;
    });
  }

  $scope.resetPersons = function() {
    $scope.getPersons();
  }

  $scope.savePersons = function() {
    if ($scope.personsForm.$valid) {
      $http.put('/api/v1/res/site/' + siteId + '/sitepersons', $scope.persons)
        .then(function(result) {
          FoundationApi.publish('main-notifications', { title: $translate.instant('CONTENT_UPDATED'), autoclose: 3000 });
        }, function(result) {
          FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 3000 });
        });
    }
    else {
      FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_SAVING_CONTENT'), autoclose: 3000 });
    }
  }

  $scope.personFromJDE = function(person) {
    return person.role.fromJDE;
  }

  /**
   * Get the CSV export data.
   */
  $rootScope.getCsvData = function() {
    $http.get('/api/v1/res/site/' + siteId + '/sitepersons')
      .then(function(result) {
        var persons = result.data;

        var dataRows = [[
          $translate.instant('ROLE'),
          $translate.instant('NAME'),
          $translate.instant('PHONE'),
          $translate.instant('EMAIL'),
        ]];

        for (var i=0; i<persons.length; i++) {
          var phoneNumber = persons[i].phonecc == '' || persons[i].phonecc === null
            ? ''
            : '+' + persons[i].phonecc + ' ';
          phoneNumber += persons[i].phone == null ? '' : persons[i].phone;

          dataRows.push([
            persons[i].role.label,
            persons[i].name,
            phoneNumber,
            persons[i].email
          ]);
        }

        CsvDownload.getCsv(dataRows, 'site-' + siteId + '-persons.csv');
      }, function(result) {
        FoundationApi.publish('error-notifications', { title: $translate.instant('ERROR_LOADING_CONTENT'), autoclose: 3000 });
      });
  }
}

controllersModule.controller('SitePersonsController', SitePersonsController);
