var totalisatorApp = angular.module('totalisatorApp', [
    "ngRoute",
    "ngTagsInput",
    "controllers",
    "factories"
]);

totalisatorApp.config(['$routeProvider', function($routeProvider) {
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
    when('/viewTotalisator/:totalisatorId', {
      templateUrl: 'partials/view-totalisator.html',
      controller: 'ViewTotalisatorController',
      controllerAs: 'vm'
    }).
    when('/unauthenticated', {
      templateUrl: 'partials/please-login.html'
    }).
    otherwise({
      redirectTo: '/totalisatorList'
    });
}])
.run(function($rootScope, $location, $window) {
 $rootScope.$on( "$routeChangeStart", function(event, next, current) {
   if (!$window.localStorage['jwtToken']) {
     // no logged user, redirect to /login
     if ( next.templateUrl === "partials/please-login.html") {
     } else {
       $location.path("/unauthenticated");
     }
   }
 });
});