package com.mailer4u.persistence.models;

public class Attachment {
	private String fileName;
	private String contentType;
	private long size;
	private String link;
	
	public Attachment(){}
	
	public Attachment(String fileName, String contentType, long size, String link) {
		super();
		this.fileName = fileName;
		this.contentType = contentType;
		this.size = size;
		this.link = link;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
