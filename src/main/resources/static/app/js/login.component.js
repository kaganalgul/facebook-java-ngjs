angular.module('fb-app')
    .component('login', {
        templateUrl: '/app/template/login.html',
        controller: function($scope, AccountApi, $window) {
            $scope.login = function() {
                AccountApi.login($scope.loginRequest, function(authenticationResponse) {
                    if (authenticationResponse.code == 0) {
                        $window.location = '/home'
                    } else {
                        switch(authenticationResponse.code){
                            case 1:
                                toastr.info("password incorrect")
                                break;
                            case -1:
                                toastr.error("user not found");
                                break;
                        }
                    }
                });
            }

            $scope.init = function() {
                $scope.loginRequest = { }; 
            };

            $scope.init();
        }
    })