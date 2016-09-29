define("WarOfHeroesApp",
    ["angular",
        "angular-ui-router",
        "ng-stomp",
        "sockjs-client",
        "stomp-websocket"
    ],
    function WarOfHeroesApp(angular) {
        angular.module("WarOfHeroesApp", ["ui.router", "ngStomp"])
            .config(["$stateProvider", function battleStateConfig($stateProvider) {
                $stateProvider
                    .state("battle", {
                        url: "battle",
                        views: {
                            "": {
                                templateUrl: "battleComponent/BattleState/battleTemplate.html",
                                controller: "battleController",
                                controllerAs: "vm"
                            }
                        }
                    })
            }]);
    });
require(["WarOfHeroesApp"], function () {
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
});
require(["sockjs-client", "WarOfHeroesApp"], function (SockJS) {
    angular.module("WarOfHeroesApp")
        .controller("InitController", function initController($scope, $stomp, $log, $state) {
            var vm = this;
            vm.connect = connect;
            vm.disconnect = disconnect;
            $stomp.setDebug(function (args) {
                $log.debug(args)
            })
            function connect() {
                $stomp
                    .connect('/battle')

                    // frame = CONNECTED headers
                    .then(function (frame) {
                        $log.info("connected");
                        function callback(payload){
                            $log.info(payload);
                        }
                        var subscription = $stomp.subscribe("/user/queue/game*", callback)
                        $stomp.send("/user/queue/game.start");
                    })
            }

            function disconnect() {
                $stomp.disconnect()
                    .then(function disconnectSuccess() {
                        $log.info("disconnected");
                    })
            }
        })
});
require(["WarOfHeroesApp"], function () {
    angular.module("WarOfHeroesApp")
        .config(function initStateConfig($stateProvider) {
            $stateProvider
                .state("init", {
                    url: "",
                    views: {
                        "": {
                            templateUrl: "battleComponent/InitState/InitTemplate.html",
                            controller: "InitController",
                            controllerAs: "vm"
                        }
                    }
                })
        })
})