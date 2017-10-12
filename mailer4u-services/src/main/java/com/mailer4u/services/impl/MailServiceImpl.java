package com.mailer4u.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.HeaderTerm;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mailer4u.persistence.models.AddressMessage;
import com.mailer4u.persistence.models.Attachment;
import com.mailer4u.persistence.models.EmailMessage;
import com.mailer4u.persistence.models.FolderMessage;
import com.mailer4u.persistence.models.User;
import com.mailer4u.services.MailService;

@Service
public class MailServiceImpl implements MailService {

	private static final String HOST = "smtp.gmail.com";
	private static final int PORT = 25; // 587 Secure , 25 Non secure
	private static final String GET_MAIL_PROTOCOL = "imap";
	private static final String GET_MAIL_HOST = "imap.gmail.com";

	// 993 Secure , 143 Non secure
	private static final String GET_MAIL_PORT = "993";

	public static Store store;
	public static List<Folder> folders = new ArrayList<>();

	@Autowired
	User user;

	private JavaMailSenderImpl sender;

	public MailServiceImpl() {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);

		sender = new JavaMailSenderImpl();
		sender.setJavaMailProperties(props);
		sender.setSession(
				Session.getDefaultInstance(getServerProperties(GET_MAIL_PROTOCOL, GET_MAIL_HOST, GET_MAIL_PORT)));
		;
	}

	/**
	 * Returns a Properties object which is configured for a POP3/IMAP server
	 *
	 * @param protocol
	 *            either "IMAP" or "POP3"
	 * @param host
	 * @param port
	 * @return a Properties object
	 */
	private Properties getServerProperties(String protocol, String host, String port) {
		Properties properties = new Properties();

		// server setting
		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);

		// SSL setting
		properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

		return properties;
	}

	@SuppressWarnings("static-access")
	@Override
	public EmailMessage sendMail(EmailMessage emailMessage, List<MultipartFile> attachFiles) {
		List<File> tempFiles = new ArrayList<>();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);

		Session session = Session.getInstance(props);

		MimeMessage message = new MimeMessage(session);

		try {
			Transport transport = session.getTransport();

			message.setFrom(user.getEmail());
			message.setSubject(emailMessage.getSubject(), "utf-8");

			// Create HTML Part Content
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailMessage.getContent(), "text/html; charset=utf-8");

			// Creates multiple-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Adds attachments
			if (attachFiles != null && attachFiles.size() > 0) {
				for (MultipartFile item : attachFiles) {
					MimeBodyPart attachPart = new MimeBodyPart();

					File convFile = new File(item.getOriginalFilename());
					// We will delete temporary file after upload completed.
					tempFiles.add(convFile);
					try {
						item.transferTo(convFile);

						attachPart.attachFile(convFile);
						attachPart.setFileName(item.getOriginalFilename());
					} catch (IllegalStateException | IOException e) {
						System.out.println(e.getMessage());
					}
					multipart.addBodyPart(attachPart);
				}
			}

			message.setContent(multipart);

			if (emailMessage.getCc() != null)
				message.setRecipients(RecipientType.CC, getListInternetAddress(emailMessage.getCc()));
			if (emailMessage.getBcc() != null)
				message.setRecipients(RecipientType.BCC, getListInternetAddress(emailMessage.getBcc()));
			if (emailMessage.getTo() != null)
				message.setRecipients(RecipientType.TO, getListInternetAddress(emailMessage.getTo()));

			message.setSentDate(new Date());

			transport.send(message, sender.getUsername(), sender.getPassword());
			transport.close();

			for (File file : tempFiles) {
				FileUtils.forceDelete(file);
			}

			return emailMessage;
		} catch (MessagingException | IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean isAuthenticated() {
		return store.isConnected();
	}

	@Override
	public List<EmailMessage> getAll(String folderName) {
		return getAll(null, null, folderName);
	}

	@Override
	public List<EmailMessage> getAll(Integer from, Integer to , String folderName) {
		try {
			//Set default folder is "INBOX"
			if(folderName == null) folderName = "INBOX";
			
			System.out.println("Folder Name: " + folderName);
			
			Folder folder = getFolderByName(folderName);

			List<EmailMessage> emailMessages = new ArrayList<>();
			Message[] messages = {};

			// If From and to equal NULL we will get all.
			if (from == null && to == null) {
				messages = folder.getMessages();
			} else {
				//TODO : Not Safe, we must make method get correct number message from and to.
				messages = folder.getMessages(folder.getMessageCount() - to + 1, folder.getMessageCount() - from);
			}

			for (int i = 0; i < messages.length; i++) {
				EmailMessage emailMessage;
				emailMessage = getEmailMessage(new EmailMessage(), messages[i]);

				emailMessages.add(emailMessage);
			}
			return emailMessages;

		} catch (MessagingException e) {

			System.out.println(e.getMessage());
			return new ArrayList<EmailMessage>();
		}
	}

	/**
	 * This method using for parsing data from @Message and inject
	 * to @EmailMessage
	 * <p>
	 * 
	 * @param message
	 *            Object Data message
	 * @param msg
	 * @return
	 */
	private EmailMessage getEmailMessage(EmailMessage message, Message msg) {

		try {
			message.setId(msg.getMessageNumber());
			message.setTo(getListAddressMessage(msg.getRecipients(RecipientType.TO)));
			message.setCc(getListAddressMessage(msg.getRecipients(RecipientType.CC)));
			message.setBcc(getListAddressMessage(msg.getRecipients(RecipientType.BCC)));
			message.setFrom(getListAddressMessage(msg.getFrom()));
			message.setSubject(msg.getSubject());
			message.setSentDate(msg.getSentDate());
			message.setReceivedDate(msg.getReceivedDate());
			message.setSeen(msg.isSet(Flags.Flag.SEEN));
			message.setMessageId(getMessageHeaderValue(msg, "Message-ID"));

			String contentType = msg.getContentType();
			String messageContent = "";

			if (contentType.contains("multipart")) {
				// content may contain attachments
				Multipart multiPart = (Multipart) msg.getContent();
				int numberOfParts = multiPart.getCount();
				for (int partCount = 0; partCount < numberOfParts; partCount++) {
					MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
					if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
						// this part is attachment
						Attachment item = new Attachment();
						item.setContentType(part.getContentType());
						item.setFileName(part.getFileName());
						item.setSize(part.getSize());
						item.setLink(String.format("%s/mail/download/%s/%s", "", message.getMessageId(),
								part.getFileName()));

						message.getAttachments().add(item);

					} else if (part.isMimeType("text/html")) {
						Object content = part.getContent();
						if (content != null) {
							messageContent = content.toString();
						}
					}
				}
			} else if (contentType.contains("text/html")) {
				Object content = msg.getContent();
				if (content != null) {
					messageContent = content.toString();
				}
			}

			message.setContent(messageContent);

		} catch (MessagingException | IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return message;
	}

	@Override
	public boolean login(User user) {
		try {
			sender.setUsername(user.getEmail());
			sender.setPassword(user.getPassword());
			store = sender.getSession().getStore(GET_MAIL_PROTOCOL);
			store.connect(user.getEmail(), user.getPassword());

			return true;
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * Get correct @Folder by name. The most important is we can cache it.
	 * <p>
	 * 
	 * @param name
	 *            - Name of folder want to get
	 * @return
	 */
	private Folder getFolderByName(String name) {
		for (Folder item : folders) {
			if (item.getName().equalsIgnoreCase(name) && item.isOpen())
				return item;
		}

		try {
			Folder folder = store.getFolder(name);
			folder.open(Folder.READ_ONLY);
			folders.add(folder);
			return folder;

		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private List<AddressMessage> getListAddressMessage(Address[] listAddress) {
		List<AddressMessage> addressMessages = new ArrayList<>();

		if (listAddress != null) {
			for (Address address : listAddress) {
				addressMessages.add(new AddressMessage(address.toString(), address.toString()));
			}
		}
		return addressMessages;
	}

	private InternetAddress[] getListInternetAddress(List<AddressMessage> addressMessages) {
		List<InternetAddress> internetAddresses = new ArrayList<InternetAddress>();
		if (addressMessages != null) {
			for (AddressMessage item : addressMessages) {
				try {
					internetAddresses.add(new InternetAddress(item.getEmail()));
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}
		}
		return internetAddresses.toArray(new InternetAddress[internetAddresses.size()]);
	}

	/**
	 * Get header value of this @Message by name.
	 * <p>
	 * 
	 * @param message
	 * @param name
	 *            : Header name want to get value
	 * @return value of correct header name , and return empty value if not
	 *         found.
	 */
	public String getMessageHeaderValue(Message message, String name) {
		try {
			Enumeration<?> headers = message.getAllHeaders();
			while (headers.hasMoreElements()) {
				Header header = (Header) headers.nextElement();
				if (header.getName().equalsIgnoreCase(name)) {
					return header.getValue();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	@Override
	public List<EmailMessage> getAllReplyMessage(EmailMessage emailMessage) {

		SearchTerm searchReplyMessage = new OrTerm(new SubjectTerm("Re: " + emailMessage.getSubject()),
				new HeaderTerm("In-Reply-To", emailMessage.getMessageId()));

		Folder folder = getFolderByName("INBOX");
		List<EmailMessage> emailMessages = new ArrayList<>();

		Message[] message = {};

		try {
			message = folder.search(searchReplyMessage);

			for (int i = 0; i < message.length; i++) {
				EmailMessage replyMsg = new EmailMessage();
				replyMsg = getEmailMessage(replyMsg, message[i]);

				emailMessages.add(replyMsg);
			}
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
		return emailMessages;
	}

	@Override
	public MimeBodyPart getAttachmentByName(EmailMessage emailMessage, String fileName) {
		SearchTerm findByMessageID = new MessageIDTerm(emailMessage.getMessageId());
		Folder folder = getFolderByName("INBOX");

		Message[] message = {};

		try {
			message = folder.search(findByMessageID);

			Multipart multiPart = (Multipart) message[0].getContent();
			int numberOfParts = multiPart.getCount();
			for (int partCount = 0; partCount < numberOfParts; partCount++) {
				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
					// this part is attachment
					if (part.getFileName().equalsIgnoreCase(fileName)) {
						System.out.println("Found !!");
						return part;
					}
				}
			}
		} catch (MessagingException | IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public EmailMessage replyMail(EmailMessage repliedMessage, String content, List<MultipartFile> files) {
		EmailMessage message = new EmailMessage("Re: " + repliedMessage.getSubject(), repliedMessage.getFrom(),
				content);
		return sendMail(message, files);
	}

	@Override
	public List<FolderMessage> getAllFolderName() {
		List<FolderMessage> result = new ArrayList<>();

		try {
			for (Folder folder : store.getDefaultFolder().list("*")) {
				//We need to avoid non-selectable folder like [Gmail]	
				if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0)
					result.add(new FolderMessage(folder.getName(), folder.getFullName(), folder.getMessageCount(),
							folder.getUnreadMessageCount()));
			}
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
		return result;

	}

	@Override
	public long getTotalFolderMessage(String folderName) {
		try {
			return getFolderByName(folderName).getMessageCount();
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

}
