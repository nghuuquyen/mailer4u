angular.module('mailer4u').controller('MailDetailController',
[ '$scope', 'UserService' , 'MailService' , '$stateParams', 'Upload' , '$location', 
  '$anchorScroll', '$state',
  function($scope, UserService , 
		MailService , $stateParams , Upload , $location , $anchorScroll , $state) {
	
	$scope.mails = [];
	$scope.replyContent = "";
	$scope.replySelectedMsg = {};
	
	$scope.replyMail = function(selectedMail){
		$scope.replySelectedMsg = selectedMail;
		
		$location.hash("reply-form");
		$anchorScroll();
	};
	
	$scope.replyFormSubmit = function() {
		Upload.upload({
			url : 'http://localhost:8080/mail/reply',
			method : 'POST',
			headers: {'Content-Type': undefined },
			data : {
				files : $scope.files,
				repliedMessage : Upload.jsonBlob($scope.replySelectedMsg),
				replyContent : Upload.jsonBlob(getBlockQuote())
			},
			arrayKey: ''
		}).progress(function(evt) {
			var progressPercentage = parseInt(100.0
				* evt.loaded / evt.total);
				console.log('progress: '
				+ progressPercentage + '% ');
			}).success(
				function(data, status, headers, config) {
					console.log("File uploaded Ok");
			});
		
		/**
		 * Get string with block quote content of selected reply message.
		 */
		function getBlockQuote() {
			var result = $scope.replyContent
					   + "<blockquote class=\"gmail_quote\" " 
					   + "style=\"margin:0 0 0 .8ex;border-left:1px #ccc solid;padding-left:1ex\">"
					   + $scope.replySelectedMsg.content
					   + "</blockquote>";
					   
		    return result;
		}
	};
	
	
	$scope.initMailDetailPage = function() {
		console.log("=============");
		console.log($stateParams.emailMessage);
		$scope.mails.push($stateParams.emailMessage);
		
		MailService.getReplyMessage($stateParams.emailMessage , function(data){
			$scope.mails = $scope.mails.concat(data);
		}, function(error) {
			console.log(error);
		});
	};
	
	$scope.backToMailList = function () {
		$state.transitionTo('mail-box.list-mail',{},{
		    // prevent the events onStart and onSuccess from firing
		    notify:false,
		    // prevent reload of the current state
		    reload:false, 
		    // replace the last record when changing the parameters so you don't hit the back button and get old params
		    location:'replace', 
		    // inherit the current parameters on the URL
		    inherit:true
		});
	};
	
}])
.filter('toTrusted', ['$sce', function($sce){
	return function(text) {
		return $sce.trustAsHtml(text);
	};
}]);
