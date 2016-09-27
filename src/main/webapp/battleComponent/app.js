require.config({
    paths: {
        "BattleModule":"BattleState/BattleModule"
    },
});
require(['angular', 'BattleModule'], function (angular, app) {
    var $html = angular.element(document.getElementsByTagName('html')[0]);
    angular.element().ready(function() {
        // bootstrap the app manually
        angular.bootstrap(document, ['BattleModule']);
    });
})