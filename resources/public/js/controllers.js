var controllers = angular.module('controllers', []);

controllers.controller('IndexController', function ($rootScope, HttpFactory){
  var vm = this;

  vm.username = undefined;
  vm.signIn = function() {
    $rootScope.username = vm.username;
    HttpFactory.signIn(vm.username).then(function (response) {
      $rootScope.userId = response[0].id;
    });
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
    vm.teams.push(tag.text);
  };

  vm.addNewMatch = function() {
    console.log($scope.username)
    vm.matches.push({});
  };

  vm.removeMatch = function(index) {
    vm.matches.splice(index, 1)
  };

  vm.saveTotalisator = function() {
    console.log("Saving " + JSON.stringify(vm.totalisator)); //TODO
    HttpFactory.newTotalisator(vm.totalisator);
  };

  vm.atLeastTwoTeamsExist = function() {
    return vm.teams.length > 1;
  };
}).controller('ViewTotalisatorController', function ($scope) {
  var vm = this;

  vm.totalisator = {
    id: 1,
    name: "Eurobasket 2015",
    teams: [{name: "Zalgiris", odds: 1.5, moneyInvested: 25}, {name: "Rytas", odds: 2.5}]
  }

  vm.hasMoneyInvested = function(team) {
    return team.moneyInvested && team.moneyInvested > 0;
  };
});