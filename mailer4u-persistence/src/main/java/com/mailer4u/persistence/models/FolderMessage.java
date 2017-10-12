package com.mailer4u.persistence.models;

public class FolderMessage {
	private String name;
	private String fullName;
	private long messageCount;
	private long unreadMessageCount;
	
	public FolderMessage(){}
	
	public FolderMessage(String name, String fullName, long messageCount, long unreadMessageCount) {
		super();
		this.name = name;
		this.fullName = fullName;
		this.messageCount = messageCount;
		this.unreadMessageCount = unreadMessageCount;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public long getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}
	public long getUnreadMessageCount() {
		return unreadMessageCount;
	}
	public void setUnreadMessageCount(long unreadMessageCount) {
		this.unreadMessageCount = unreadMessageCount;
	}

	
}
