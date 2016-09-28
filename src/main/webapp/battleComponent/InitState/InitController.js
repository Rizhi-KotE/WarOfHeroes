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
                        var subscription = $stomp.subscribe("/user/queue/play", function (resp) {
                            $log.info(resp);
                        })
                    })
            }

            function disconnect() {
                $stomp.disconnect()
                    .then(function disconnectSuccess() {
                        $log.info("disconnected");
                    })
            }
        })
})