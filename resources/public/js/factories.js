var factories = angular.module('factories', []);

factories.factory('HttpFactory', ['$http', function httpFactory($http) {
  return {
    getTotalisators: function() {
        return $http.get("api/totalisators").then(function (response) { return response.data});
    },

    newTotalisator: function(totalisator) {
        return $http.post("api/users/1/totalisators", [totalisator]);
    },

    authenticate: function(username, password) {
        return $http.post("authapi/authentication", {username: username, password: password})
            .then(function (response) {return response.data});
    }
  };
}]).factory('JwtInterceptor', ['$window', function jwtInterceptor($window) {
  return {
    request: function(config) {
      var token = $window.localStorage['jwtToken'];
      if(token) {
        config.headers.Authorization = 'Bearer ' + token;
      }

      return config;
    },

    response: function(res) {
        return res;
    }
  }
}]);