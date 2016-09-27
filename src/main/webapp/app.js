require.config({
    paths: {
        "WarOfHeroesApp": "WarOfHeroesApp",
        "ng-stomp": "/webjars/ng-stomp/0.2.0/dist/ng-stomp.standalone.min.js"
    },
});
require(['angular', 'WarOfHeroesApp'], function (angular, app) {
    var $html = angular.element(document.getElementsByTagName('html')[0]);
    angular.element().ready(function () {
        // bootstrap the app manually
        angular.bootstrap(document, ['WarOfHeroesApp']);
    });
})