package com.c3map.mail;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

/** 
 * 邮件信息对象，用于保存用户站内邮件信息数据
 * @author Lee Bin
 */
public class MailInfo extends ActionForm implements Serializable{

	private int id;
	private int receiverId;
	private String receiverName;
	private int senderId;
	private String senderName;
	private String sub;
	private String content;
	private int status;
	private Timestamp sendTime;

	public int getId() {
		return id;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public int getSenderId() {
		return senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public String getSub() {
		return sub;
	}

	public String getContent() {
		return content;
	}

	public int getStatus() {
		return status;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	//----------------------------------------
	//----------------------------------------
	public void setId(int it) {
		id = it;
	}
	
	public void setReceiverId(int it) {
		receiverId = it;
	}
	
	public void setReceiverName(String string) {
		receiverName = string;
	}
	
	public void setSenderId(int it) {
		senderId = it;
	}
	
	public void setSenderName(String string) {
		senderName = string;
	}
	
	public void setSub(String string) {
		sub = string;
	}
	
	public void setContent(String string) {
		content = string;
	}
	
	public void setStatus(int it) {
		status = it;
	}
	
	public void setSendTime(Timestamp ts) {
		sendTime = ts;
	}
}
