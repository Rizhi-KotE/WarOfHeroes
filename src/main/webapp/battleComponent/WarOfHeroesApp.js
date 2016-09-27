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
    })