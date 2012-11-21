/*
 * �������� 2006-8-2
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.mailbox;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

public class MailInfo extends ActionForm implements Serializable {

	private int id;
	private int posterId; //������id
	private String posterName; //����������
	private String letterSubject;//�ż�����
	private String letterBody;   //�ż�����
	private int senderId;   // ������id
	private String senderName;// ����������
	private Timestamp sendDate;// ����ʱ��
	
	private String status;//�ż�״̬ ��0����ʾδ������1����ʾ�Ѷ���
	
	public MailInfo(){}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getLetterBody() {
		return letterBody;
	}

	/**
	 * @return
	 */
	public String getLetterSubject() {
		return letterSubject;
	}

	/**
	 * @return
	 */
	public String getPosterName() {
		return posterName;
	}

	/**
	 * @return
	 */
	public int getPosterId() {
		return posterId;
	}

	/**
	 * @return
	 */
	public Timestamp getSendDate() {
		return sendDate;
	}

	/**
	 * @return
	 */
	public int getSenderId() {
		return senderId;
	}

	/**
	 * @return
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param string
	 */
	public void setLetterBody(String string) {
		letterBody = string;
	}

	/**
	 * @param string
	 */
	public void setLetterSubject(String string) {
		letterSubject = string;
	}

	/**
	 * @param string
	 */
	public void setPosterName(String string) {
		posterName = string;
	}

	/**
	 * @param i
	 */
	public void setPosterId(int i) {
		posterId = i;
	}

	/**
	 * @param timestamp
	 */
	public void setSendDate(Timestamp timestamp) {
		sendDate = timestamp;
	}

	/**
	 * @param i
	 */
	public void setSenderId(int i) {
		senderId = i;
	}

	/**
	 * @param string
	 */
	public void setSenderName(String string) {
		senderName = string;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

}
