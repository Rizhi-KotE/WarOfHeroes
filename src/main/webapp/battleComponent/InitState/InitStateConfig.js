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