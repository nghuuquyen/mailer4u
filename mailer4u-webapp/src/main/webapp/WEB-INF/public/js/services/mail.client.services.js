'use strict';

angular.module('mailer4u').factory('MailService',
		[ '$resource', '$rootScope', function($resource, $rootScope) {
			return $resource("http://localhost:8080/mail", {}, {
				update : {
					method : 'PUT'
				},
				getAll : {
					url : "http://localhost:8080/mail/getAll",
					method : 'GET',
					isArray : true
				},
				uploadAndSave : {
					method : 'POST',
					url : "http://localhost:8080/mail/upload",
					transformRequest : function(data) {
						var fd = new FormData();
						fd.append('files', data.files);
						fd.append('emailMessage', data.emailMessage);
						return fd;
					},
					headers : {
						'Content-Type' : undefined,
						enctype : 'multipart/form-data'
					},
					isArray : false
				},
				downloadAttachment : {
					method : 'GET',
					url : "http://localhost:8080/mail/download"
				},
				getReplyMessage : {
					method : 'POST',
					url : "http://localhost:8080/mail/getReply",
					isArray : true
				},
				getAllFolders : {
					method : 'GET',
					url : "http://localhost:8080/mail/folders",
					isArray : true
				},
				getFolderMessageCount : {
					method : 'GET',
					url : "http://localhost:8080/mail/folders/messages/count",
					isArray : false
				}
			});
		} ]);
