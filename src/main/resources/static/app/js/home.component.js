angular.module('fb-app')
    .component('home', {
        templateUrl: '/app/template/home.html',
        controller: function($scope, PostApi, AccountApi, $uibModal, $window, SocketService, $location) {

            $scope.share = function() {
                PostApi.share($scope.request, function() {
                    toastr.info("post paylaşıldı");
                    let postInput = document.getElementById('post');
                    postInput.value = '';
                    $location.path('/home');
                })
            }

            $scope.showAddModal = function () {
                var modalInstance = $uibModal.open({
                templateUrl: '/app/template/add-friends-modal.html',
                controller: 'AddFriendsModalController',
                size: 'md'
                });
   
                modalInstance.result.then(function() {
                   $location.path('/home');
                })
           }

           $scope.showFriendsModal = function () {
            var modalInstance = $uibModal.open({
            templateUrl: '/app/template/friends-modal.html',
            controller: 'ShowFriendsModalController',
            size: 'md',
            resolve: {
                
                }
                }
            )};

           $scope.showRequestModal = function () {
            var modalInstance = $uibModal.open({
            templateUrl: '/app/template/acpt-friend-req-modal.html',
            controller: 'AcceptFriendsModalController',
            size: 'md',
            resolve: {
                
            }
            });

            modalInstance.result.then(function() {
               $window.location = '/home';
            })
            }

            $scope.init = function() {
                $scope.user = current_user;   
                $scope.posts = PostApi.list();
                $scope.requests = AccountApi.requestList();
                
                let promise = SocketService.connect();

                promise.then(function() {
                    SocketService.subscribe("/post/list/" + $scope.user.id, function(data) {
                        $scope.posts.push(data);
                        $scope.$apply();
                    })
    
                    SocketService.subscribe("/user/send-friend-request/" + $scope.user.id, function(data) {
                        $scope.requests.push(data);
                        $scope.$apply();
                    })
                })  
            }

            $scope.init();
        }
    })

    app.controller("AddFriendsModalController", function ($scope, $uibModalInstance, AccountApi) {

        $scope.add = function () {
            let receiverEmail = $scope.useremail;
            AccountApi.sendFriendRequest(receiverEmail, function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.init = function () {

        };
        $scope.init();
    });

    app.controller("ShowFriendsModalController", function ($scope, $uibModalInstance, AccountApi) {

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.init = function () {
            $scope.friendList = AccountApi.friendList();
        };
        $scope.init();
    });

    app.controller("AcceptFriendsModalController", function ($scope, $uibModalInstance, AccountApi) {

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.accept = function(user) {
            let ret = AccountApi.accept({id: user.id}, function(data) {
                $uibModalInstance.close(data);
            });
            ret.$promise.then(function() {
                _.remove($scope.requests, {id: user.id});
                console.log($scope.requests)
            })  
        };

        $scope.reject = function(user) {
            let ret = AccountApi.reject({id: user.id}, function(data) {
                $uibModalInstance.close(data);
            });
            ret.$promise.then(function() {
                _.remove($scope.requests, {id: user.id});
            })
        }

        $scope.init = function () {
            $scope.requests = AccountApi.requestList();
        };
        $scope.init();
    });