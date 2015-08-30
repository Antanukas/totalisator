var controllers = angular.module('controllers', []);

controllers.controller('IndexController', function ($rootScope){
  $rootScope.username = undefined; //Simple hack while we do not have real login.
}).controller('TotalisatorsController', function () {

  var vm = this;

  vm.totalisators = [
  {
    id: 1,
    name: "Eurobasket 2015"
  },
  {
    id: 2,
    name: "Fuzbal 13"
  }];
}).controller('CreateTotalisatorController', function ($scope) {
  var vm = this;

  vm.totalisator = {
    teams: [],
    matches: []
  };

  vm.homeQuery = "";
  vm.awayQuery = "";

  vm.addTeam = function(tag) {
    console.log(tag);
    vm.totalisator.teams.push(tag.text);
  };

  vm.addNewMatch = function() {
    console.log($scope.username)
    vm.totalisator.matches.push({});
  };

  vm.removeMatch = function(index) {
    vm.totalisator.matches.splice(index, 1)
  };

  vm.saveTotalisator = function() {
    console.log("Saving " + JSON.stringify(vm.totalisator)); //TODO
  };

  vm.atLeastTwoTeamsExist = function() {
    return vm.totalisator.teams.length > 1;
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