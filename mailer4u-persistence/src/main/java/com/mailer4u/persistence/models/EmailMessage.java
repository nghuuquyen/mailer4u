package com.mailer4u.persistence.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessage {
	
	private Integer id;
	private String subject;
	private String messageId;
	private List<AddressMessage> from;
	private List<AddressMessage> to;
	private List<AddressMessage> cc;
	private List<AddressMessage> bcc;
	private List<Attachment> attachments;
	private Date receivedDate;
	private Date sentDate;
	private boolean isSeen;
	
	public EmailMessage(String subject, List<AddressMessage> to, String content) {
		super();
		this.subject = subject;
		this.to = to;
		this.content = content;
	}
	
	public EmailMessage() {}
	
	private String content;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<AddressMessage> getFrom() {
		return from;
	}
	public void setFrom(List<AddressMessage> from) {
		this.from = from;
	}
	public List<AddressMessage> getTo() {
		return to;
	}
	public void setTo(List<AddressMessage> to) {
		this.to = to;
	}
	public List<AddressMessage> getCc() {
		return cc;
	}
	public void setCc(List<AddressMessage> cc) {
		this.cc = cc;
	}
	public List<AddressMessage> getBcc() {
		return bcc;
	}
	public void setBcc(List<AddressMessage> bcc) {
		this.bcc = bcc;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isSeen() {
		return isSeen;
	}
	public void setSeen(boolean isSeen) {
		this.isSeen = isSeen;
	}
	public List<Attachment> getAttachments() {
		if(attachments == null) attachments = new ArrayList<>();
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	
}
