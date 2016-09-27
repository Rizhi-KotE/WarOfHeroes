require("WarOfHeroesApp", function () {
    angular.module("WarOfHeroesApp")
        .controller("battleController", ["$scope", function battleController($scope) {
            var vm = this;
            $scope.lines = [];
            for (var i = 0; i < 10; i++) {
                var line = [];
                for (var j = 0; j < 10; j++) {
                    var cell = {};
                    line.push(cell);
                }
                $scope.lines.push(line);
            }
        }])
})