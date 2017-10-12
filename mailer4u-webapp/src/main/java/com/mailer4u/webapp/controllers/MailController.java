package com.mailer4u.webapp.controllers;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mailer4u.persistence.models.EmailMessage;
import com.mailer4u.persistence.models.FolderMessage;
import com.mailer4u.services.MailService;
import com.mailer4u.webapp.exceptions.AuthenticationException;

@Controller
public class MailController {
	@Autowired
	MailService mailService;

	@ResponseBody
	@RequestMapping(value = "/mail/getAll", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	public List<EmailMessage> getAllMessages(@RequestParam("from") Integer from, @RequestParam("to") Integer to, @RequestParam("folderName") String folderName) {
		return mailService.getAll(from, to , folderName);
	}

	@RequestMapping(value = "/mail/send", method = RequestMethod.POST)
	public ResponseEntity<EmailMessage> sendEmail(@RequestPart("files") List<MultipartFile> files,
			@RequestPart("emailMessage") EmailMessage emailMessage) {
		if (mailService.isAuthenticated()) {
			EmailMessage msg = null;
			if ((msg = mailService.sendMail(emailMessage, files)) != null) {
				return new ResponseEntity<>(msg, HttpStatus.OK);
			} else {
				//TODO Can't send mail exception here!!
			}
		}
		throw new AuthenticationException();
	}
	
	@RequestMapping(value = "/mail/reply", method = RequestMethod.POST)
	public ResponseEntity<EmailMessage> replyEmail(@RequestPart("files") List<MultipartFile> files,
			@RequestPart("repliedMessage") EmailMessage repliedMessage , @RequestPart("replyContent") String content) {
		if (mailService.isAuthenticated()) {
			EmailMessage msg = null;
			if ((msg = mailService.replyMail(repliedMessage , content , files)) != null) {
				return new ResponseEntity<>(msg, HttpStatus.OK);
			} else {
				//TODO Can't send mail exception here!!
			}
		}
		throw new AuthenticationException();
	}

	@ResponseBody
	@RequestMapping(value = "/mail/getReply", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public List<EmailMessage> getAllReplyMessage(@RequestBody EmailMessage emailMessage) {
		return mailService.getAllReplyMessage(emailMessage);
	}

	@RequestMapping(value = "mail/download/{messageId}/{fileName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> testGetMailAttachment(@PathVariable("messageId") String messageId,
			@PathVariable("fileName") String fileName) {

		System.out.println("DOWNLOAD --- Message ID" + messageId + "File Name : " + fileName);

		EmailMessage em = new EmailMessage();
		em.setMessageId(messageId);

		byte[] b = {};
		MimeBodyPart part = mailService.getAttachmentByName(em, fileName);

		if (part != null) {
			try {
				b = IOUtils.toByteArray(part.getInputStream());

				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.set("charset", "utf-8");
				responseHeaders.setContentLength(b.length);
				responseHeaders.set("Content-disposition", "attachment; filename=" + part.getFileName());

				return new ResponseEntity<byte[]>(b, responseHeaders, HttpStatus.OK);
			} catch (IOException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			return new ResponseEntity<byte[]>(HttpStatus.BAD_GATEWAY);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "mail/folders", method = RequestMethod.GET)
	public ResponseEntity<List<FolderMessage>> getAllFolderName() {
		if (mailService.isAuthenticated()) {
			return new ResponseEntity<List<FolderMessage>>(mailService.getAllFolderName(), HttpStatus.OK);
		}
		throw new AuthenticationException();
	}
	
	@ResponseBody
	@RequestMapping(value = "mail/folders/messages/count", method = RequestMethod.GET)
	public ResponseEntity<String> getFolderMessageCount(@RequestParam String folderName) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result = mapper.createObjectNode();
		
		if (mailService.isAuthenticated()) {
			result.put("count", mailService.getTotalFolderMessage(folderName));
			
			return new ResponseEntity<String>(result.toString() , HttpStatus.OK);
		}
		throw new AuthenticationException();
	}

}
