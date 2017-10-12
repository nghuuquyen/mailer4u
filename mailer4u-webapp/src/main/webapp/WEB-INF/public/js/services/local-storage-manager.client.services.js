angular.module('mailer4u').factory('LocalStorageService',
		['$rootScope', '$window', function($rootScope, $window) {
			
			return {
				setValue : function (key , value) {
					$window.localStorage.setItem(key, JSON.stringify(value));
				},
				getValue : function (key) {
					try {
						return JSON.parse($window.localStorage.getItem(key));
					} catch (e) {
						return null;
					}
				}
			}
		}]);