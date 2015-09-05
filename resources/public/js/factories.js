var factories = angular.module('factories', []);

factories.factory('HttpFactory', ['$http', function httpFactory($http) {
  return {

    getTotalisator: function(totalisatorId) {
      return $http.get("api/totalisators/" + totalisatorId).then(getData);
    },

    getTotalisatorTeams: function(totalisatorId) {
      return $http.get("api/totalisators/" + totalisatorId + "/teams").then(getData);
    },

    getTotalisators: function() {
        return $http.get("api/totalisators").then(getData);
    },

    newTotalisator: function(totalisator) {
        return $http.post("api/totalisators", [totalisator]).then(getData);
    },

    newTotalisatorTeams: function(teams, totalisatorId) {
        return $http.post("api/totalisators/" + totalisatorId + "/teams", teams).then(getData);
    },

    authenticate: function(username, password) {
        return $http.post("authapi/authentication", {username: username, password: password}).then(getData);
    },

    placeWinnerBet: function(totalisatorId, teamId, betAmount) {
      return $http.post("/api/totalisators/" + totalisatorId + "/teams/" + teamId + "/bets", [{amount: betAmount}]).then(getData);
    },

    getTotalisatorPayouts: function(totalisatorId, teamId) {
      return $http.get("/api/totalisators/" + totalisatorId + "/teams/" + teamId + "/payouts").then(getData);
    }
  };

  function getData(response) {
    return response.data;
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