var totalisatorApp = angular.module('totalisatorApp', [
    "ngRoute",
    "ngTagsInput",
    "controllers"
]);

totalisatorApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/totalisatorList', {
        templateUrl: 'partials/totalisator-list.html',
        controller: 'TotalisatorsController',
        controllerAs: 'vm'
      }).
      when('/newTotalisator', {
        templateUrl: 'partials/new-totalisator.html',
        controller: 'CreateTotalisatorController',
        controllerAs: 'vm'
      }).
      when('/viewTotalisator', {
        templateUrl: 'partials/view-totalisator.html',
        controller: 'ViewTotalisatorController',
        controllerAs: 'vm'
      }).
      otherwise({
        redirectTo: '/totalisatorList'
      });
  }]);