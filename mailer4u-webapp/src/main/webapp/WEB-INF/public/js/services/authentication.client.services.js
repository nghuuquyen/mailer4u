angular.module('mailer4u').factory('Authentication',
		['$rootScope', 'LocalStorageService', function($rootScope, LocalStorageService) {
			return LocalStorageService.getValue("authentication");
		}]);