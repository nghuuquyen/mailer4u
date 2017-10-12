angular.module('mailer4u').controller(
	'MailBoxController',
	[ '$scope', 'UserService', 'MailService', '$uibModal', 'Authentication',
	function($scope, UserService, MailService , $uibModal , Authentication) {
		
		$scope.folders = [];
		
		$scope.initMailBoxPage = function() {
			getAllFolderName();
		};
		
		$scope.openComposeMailModal = function() {
			var modalInstance = $uibModal.open({
				templateUrl : '/templates/views/compose-mail-modal.html',
				controller : 'ComposeMailModalController',
				size : "lg",
				resolve : {
					items : function() {
						return "Some thing here!!!";
					}
				}
			});

			modalInstance.result.then(function(item) {
				console.log(item)
			}, function() {

			});
		};
		
		/**
		 * Get all default folders name for this account.
		 */
		function getAllFolderName() {
			MailService.getAllFolders(function(data){
				
				$scope.folders = data;
			},function(error){
				console.log(error);
			})
		}
		
	}]);
