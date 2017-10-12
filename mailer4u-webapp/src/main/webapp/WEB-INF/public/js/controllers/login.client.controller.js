angular.module('mailer4u').controller('LoginController',
[ '$scope', 'UserService' , 'MailService' , '$state' , 'LocalStorageService', 
  function($scope, UserService , MailService , $state  , LocalStorageService) {

	$scope.user = {};
	$scope.message = "";

	$scope.submit = function(isValid) {
		if (!isValid)
		return;

		UserService.login({
			email : $scope.user.email,
			password : $scope.user.password
		}, function(response) {
			if(response.user.email) {
				$state.go('mail-box.list-mail');
				LocalStorageService.setValue("authentication", response);
			}
			
			$scope.message = "Email or password not correct";
		}, function(error) {
			console.log(error);
		});
	};

}])
.filter('toTrusted', ['$sce', function($sce){
	return function(text) {
		return $sce.trustAsHtml(text);
	};
}]);
