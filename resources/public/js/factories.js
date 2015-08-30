var factories = angular.module('factories', []);

factories.factory('HttpFactory', ['$http', function httpFactory($http) {
  return {
    getTotalisators: function() {
        return $http.get("api/totalisators").then(function (response) { return response.data});
    },

    newTotalisator: function(totalisator) {
        return $http.post("api/users/1/totalisators", [totalisator]);
    },

    signIn: function(username) {
        return $http.post("api/users", [{name: username}]).then(function (response) {return response.data});
    }
  };
}]);