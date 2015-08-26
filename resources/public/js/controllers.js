var controllers = angular.module('controllers', []);

controllers.controller('IndexController', function ($rootScope){
  $rootScope.username = undefined; //Simple hack while we do not have real login.
}).controller('TotalisatorsController', function ($scope) {

  var vm = {};
  $scope.vm = vm;

  vm.totalisators = [
  {
    "id": 1,
    "name": "Eurobasket 2015",
    "active" : true
  },
  {
    "id": 2,
    "name": "Fuzbal 13"
  }];
}).controller('ManageTotalisatorController', function ($scope) {
  var vm = {};
  $scope.vm = vm;

  vm.totalisator = {
    "matches": []
  };

  vm.addNewMatch = function() {
    console.log($scope.username)
    vm.totalisator.matches.push({});
  };

  vm.removeMatch = function(index) {
    vm.totalisator.matches.splice(index, 1)
  };
});