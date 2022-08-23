var app = angular.module('fb-app', [
    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'timeago'
]);

app.config(function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);

    $routeProvider
        .when('/login', {
            template: '<login></login>'
        })
        .when('/', {
            template: '<login></login>'
        })
        .when('/register', {
            template: '<register></register>'
         })
         .when('/home', {
            template: '<home></home>'
         })
         .otherwise({
            redirectTo: '/'
         })
})

app.factory('AccountApi', ['$resource', function($resource) {
    var baseUrl = '/user';

    return $resource('/id', { id: '@id' }, {
        login: {
            method: 'POST',
            url: baseUrl + '/login'
        },
        register: {
            method: 'POST',
            url: baseUrl + '/register'
        },
        sendFriendRequest: {
            method: 'POST',
            url: baseUrl + '/send-friend-request'
        },
        sendFriendResponse: {
            method: 'POST',
            url: baseUrl + '/send-friend-response'
        },
        accept: {
            method: 'POST',
            url: baseUrl + '/accept/:id'
        },
        reject: {
            method: 'POST',
            url: baseUrl + '/reject/:id'
        },
        requestList: {
            method: 'GET',
            url: baseUrl + '/request-list',
            isArray: true
        },
        friendList: {
            method: 'GET',
            url: baseUrl + '/friend-list',
            isArray: true
        }
    })
}])

app.factory('PostApi', ['$resource', function($resource) {
    var baseUrl = '/post';

    return $resource('', { }, {
        share: {
            method: 'POST',
            url: baseUrl + '/share'
        },
        list: {
            method: 'GET',
            url: baseUrl + '/list',
            isArray: true
        }
    })
}])

app.factory("SocketService", function ($q) {

    let connected = false;

    function connect() {
        let deferred = $q.defer();

        let socket = new SockJS('/facebook-app');
        stompClient = Stomp.over(socket);
        //stompClient.debug = null;
        stompClient.connect({}, function (frame) {
            //console.log(frame);
            connected = true;
            deferred.resolve();
        });

        return deferred.promise;
    }

    function subscribe(destination, cb) {

        let deferred = $q.defer();

        if (connected) {
            deferred.resolve();
        } else {
            let promise = connect();
            promise.then(function () {
                deferred.resolve();
            });
        }

        deferred.promise.then(function () {
            stompClient.subscribe(destination, function (message) {
                let data = JSON.parse(message.body);
                cb(data);
            });
        });

    }

    function unsubscribe(destination) {
        // TBD
    }

    function disconnect() {
        if(stompClient != null) {
            stompClient.disconnect();
        }
        connected = false;
        console.log("Disconnected");
    }

    return {
        connect: connect,
        subscribe: subscribe,
        unsubscribe: unsubscribe,
        disconnect: disconnect
    }
});