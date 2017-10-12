package com.mailer4u.services;

import java.util.List;

import javax.mail.internet.MimeBodyPart;

import org.springframework.web.multipart.MultipartFile;

import com.mailer4u.persistence.models.EmailMessage;
import com.mailer4u.persistence.models.FolderMessage;
import com.mailer4u.persistence.models.User;

public interface MailService {
	public boolean login(User user);
	public EmailMessage sendMail(EmailMessage emailMessage , List<MultipartFile> files);
	public EmailMessage replyMail(EmailMessage repliedMessage , String content , List<MultipartFile> files);
	public boolean isAuthenticated();
	public List<EmailMessage> getAll(String folderName);
	public List<EmailMessage> getAll(Integer from, Integer to , String folderName);
	public List<EmailMessage> getAllReplyMessage(EmailMessage emailMessage);
	public MimeBodyPart getAttachmentByName(EmailMessage emailMessage , String fileName);
	public List<FolderMessage> getAllFolderName();
	public long getTotalFolderMessage(String folderName);
}
