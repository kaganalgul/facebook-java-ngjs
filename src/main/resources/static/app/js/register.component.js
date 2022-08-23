angular.module('fb-app')
    .component('register', {
        templateUrl: '/app/template/register.html',
        controller: function($scope, AccountApi, $location) {
            
            $scope.register = function() {
                AccountApi.register($scope.user, function() {
                    $location.path('/login')
                });
            };

            $scope.init = function() {
                $scope.user = { };
            };

            $scope.init();
        }
    })