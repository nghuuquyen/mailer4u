angular.module('mailer4u')
.controller('ComposeMailModalController',['$scope','UserService','MailService',
'Upload','$http', '$uibModalInstance',
function($scope, UserService, MailService, Upload, $http ,$uibModalInstance) {
	$scope.mail = {};
	$scope.files = {};
	
	$scope.submit = function() {
		Upload.upload({
			url : 'http://localhost:8080/mail/send',
			method : 'POST',
			headers: {'Content-Type': undefined },
			data : {
				files : $scope.files,
				emailMessage : Upload.jsonBlob(getMailMessage($scope.mail))
			},
			arrayKey: ''
		}).progress(function(evt) {
			var progressPercentage = parseInt(100.0
				* evt.loaded / evt.total);
				console.log('progress: '
				+ progressPercentage + '% ');
			}).success(
				function(data, status, headers, config) {
					$uibModalInstance.close("Upload Is Okay !!");
				});
			};

			function getMailMessage(mail) {
				var mailMessage = {
					subject : "",
					to : [],
					cc : [],
					bcc : [],
					content : ""
				};

				mailMessage.subject = mail.subject;
				mailMessage.content = mail.content;
				
				mail.cc.forEach(function (value) {
					mailMessage.cc.push({email : value.text , name : ""});
				});
				
				mail.bcc.forEach(function (value) {
					mailMessage.bcc.push({email : value.text , name : ""});
				});
				
				mail.to.forEach(function (value) {
					mailMessage.to.push({email : value.text , name : ""});
				});

				return mailMessage;
			}
		}]);
