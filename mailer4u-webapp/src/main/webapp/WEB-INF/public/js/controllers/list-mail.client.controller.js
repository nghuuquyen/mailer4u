angular.module('mailer4u').controller(
	'ListMailController',
	[ '$scope', 'UserService', 'MailService', 'Authentication', 'LocalStorageService', '$stateParams',
	function($scope, UserService, MailService , Authentication , LocalStorageService , $stateParams) {
		$scope.mails = [];
		$scope.isLoadingData = false;
		
		//Variable for pagination.
        $scope.currentPageSearch = 1;
        $scope.pageSize = 20;
        $scope.totalRecord = 0;
        $scope.maxSizePagination = 5;
        $scope.viewBy = 20;
        
        $scope.initPage = function() {
        	//If user selected other folder ,we must update data.
        	if(!$stateParams.folderName)
        		getDataFromSession(loadMails);
        	else 
        		loadMails();
        	
        	getMessageCount();
		};
		
		function loadMails() {
			$scope.isLoadingData = true;
            var from = ($scope.currentPageSearch - 1 ) * $scope.pageSize;
            LocalStorageService.setValue("folderName", $stateParams.folderName || "INBOX");
            
            MailService.getAll({
				from : from,
				to : from + $scope.pageSize,
				folderName : $stateParams.folderName || "INBOX"
			}, function(data) {
				
				$scope.mails = data;
				
				//TODO: Improve cache data here!!
				LocalStorageService.setValue("mails", $scope.mails);
				LocalStorageService.setValue("currentPageSearch",$scope.currentPageSearch);
				
				$scope.isLoadingData = false;
			}, function(error) {
				$scope.isLoadingData = false;
				console.log(error);
			});
		}
		
		/**
		 * Using this method for check if we already have data.
		 * If have data , we don't need load again.
		 * It help for paging feature didn't reload when we switch between 
		 * other states.
		 * 
		 */
		function getDataFromSession(callback) {
			if(LocalStorageService.getValue("mails")) {
				$scope.mails = LocalStorageService.getValue("mails");
				$scope.currentPageSearch = LocalStorageService.getValue("currentPageSearch");
			} else {
				callback();
			}
		}
		
		function getMessageCount() {
			console.log($stateParams.folderName 
				|| LocalStorageService.getValue("folderName"));
			
			MailService.getFolderMessageCount({folderName : $stateParams.folderName 
				|| LocalStorageService.getValue("folderName")}, function(data){
				
				$scope.totalRecord = data.count;
			},function(error){
				console.log(error);
			});
		}
		
		$scope.pageChanged = function () {
			loadMails();
        };
        
        $scope.setItemsPerPage = function (number) {
            $scope.pageSize = number;
            $scope.currentPageSearch = 1; //reset to first page.
            $scope.pageChanged();
        };

	}]);
