'use strict';

angular.module('mailer4u').factory('UserService', ['$resource', '$rootScope',
    function ($resource, $rootScope) {
        return $resource("http://localhost:8080" + '/login', {
        
        }, {
            update: {
                method: 'PUT'
            },
            login : {
            	method: 'POST'
            }
        });
    }
]);
