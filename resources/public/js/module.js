var totalisatorApp = angular.module('totalisatorApp', [
    "ngRoute",
    "controllers"
]);

totalisatorApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/totalisatorList', {
        templateUrl: 'partials/totalisator-list.html',
        controller: 'TotalisatorsController'
      }).
      when('/newTotalisator', {
        templateUrl: 'partials/new-totalisator.html',
        controller: 'ManageTotalisatorController'
      }).
      otherwise({
        redirectTo: '/totalisatorList'
      });
  }]);