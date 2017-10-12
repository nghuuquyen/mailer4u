angular.module('mailer4u')
.config(function($stateProvider, $urlRouterProvider , $locationProvider) {

	$stateProvider
	.state('mail-box', {
		abstract: true,
		url : '/home',
		controller : "MailBoxController",
		templateUrl : '/templates/views/mail-box.html'
	})
	.state('mail-box.list-mail', {
		parent: 'mail-box',
		url : '/list',
		controller : "ListMailController",
		params : {folderName: null},
		templateUrl : '/templates/views/fragments/list-mail.html'
	})
	.state('mail-box.mail-detail', {
		parent: 'mail-box',
		url : '/mail',
		controller : "MailDetailController",
		params : {emailMessage: null},
		templateUrl : '/templates/views/fragments/mail-detail.html'
	})
	.state('login', {
		url : '/login',
		templateUrl : '/templates/views/login.html'      
	});
	
	$urlRouterProvider.otherwise('/login');
	
	$locationProvider.html5Mode({
		  enabled: true,
		  requireBase: false
	});
	$locationProvider.html5Mode(true);
});