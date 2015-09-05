var controllers = angular.module('controllers', []);

controllers.controller('IndexController', function ($rootScope, $window, HttpFactory, $location){
  var vm = this;

  vm.username = $window.localStorage['username'];

  vm.login = function() {
    HttpFactory.authenticate(vm.username, vm.password).then(function (response) {
      $window.localStorage['jwtToken'] = response.token;
      $window.localStorage['username'] = vm.username; //Better would be to decode token
      vm.isLoggedIn = true;
      vm.password = null;
      $location.path("/totalisatorList");
    });
  };

  vm.isLoggedIn = $window.localStorage['jwtToken'] && $window.localStorage['username'];

  vm.logout = function() {
    $window.localStorage.removeItem('jwtToken');
    $window.localStorage.removeItem('username');
    vm.username = undefined;
    vm.isLoggedIn = false;
    $location.path("/unauthenticated");
  };
}).controller('TotalisatorsController', function (HttpFactory) {

  var vm = this;

  HttpFactory.getTotalisators().then(function (response){
    vm.totalisators = response;
  });
}).controller('CreateTotalisatorController', function ($scope, HttpFactory) {
  var vm = this;

  vm.totalisator = {};

  vm.teams = [];
  vm.matches = [];

  vm.homeQuery = "";
  vm.awayQuery = "";

  vm.addTeam = function(tag) {
    console.log(tag);
    vm.teams.push({name: tag.text});
  };

  vm.addNewMatch = function() {
    console.log($scope.username)
    vm.matches.push({});
  };

  vm.removeMatch = function(index) {
    vm.matches.splice(index, 1)
  };

  vm.saveTotalisator = function() {
    HttpFactory.newTotalisator(vm.totalisator).then(function (savedTotalisators) {
      var savedTotalisatorId = savedTotalisators[0].id;
      HttpFactory.newTotalisatorTeams(vm.teams, savedTotalisatorId);
    });
  };

  vm.atLeastTwoTeamsExist = function() {
    return vm.teams.length > 1;
  };
}).controller('ViewTotalisatorController', function ($scope, $routeParams, HttpFactory) {
  var vm = this;

  vm.totalisator = {};
  vm.teams = [];
  vm.winnerTeam = {};
  vm.totalisatorPayouts = [];

  var totalisatorId = $routeParams.totalisatorId;

  function init() {
    HttpFactory.getTotalisator(totalisatorId).then(function(response){
      vm.totalisator = response;
    });
    HttpFactory.getTotalisatorTeams(totalisatorId).then(function(response){
      vm.teams = response;
    });
  };

  vm.hasMoneyInvested = function(team) {
    return team.moneyInvested && team.moneyInvested > 0;
  };

  vm.placeWinnerBet = function(team) {
    HttpFactory.placeWinnerBet(vm.totalisator.id, team.id, team.betAmount)
      .then(init);
  };

  vm.switchTotalisatorPayouts = function(team) {
    vm.winnerTeam = team;
    return HttpFactory.getTotalisatorPayouts(vm.totalisator.id, team.id)
      .then(function (response) { vm.totalisatorPayouts = response});
  };

  init();
}).config(function($httpProvider) {
  $httpProvider.interceptors.push('JwtInterceptor');
});