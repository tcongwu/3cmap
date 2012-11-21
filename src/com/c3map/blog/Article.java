/*
 * �������� 2006-7-30
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.blog;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

public class Article extends ActionForm implements Serializable{
	
	private int id ;    //����id;
	private int authorId; //����id;
	private String name;  //����name;
	private String keyword; //���¹ؼ��ʣ�
	private String   body;  //�������ݣ�
	private int openType; //���¹������ͣ�
	private Timestamp issueDate; //���·�������
	private int readNum; //�����Ķ�������
	private int replyNum; //���»ظ�������
	private String artType;//�������
	private Timestamp replyDate;//����ظ�ʱ��
	
	public int getAuthorId() {
		return authorId;
	}

	public String getBody() {
		return body;
	}

	public int getId() {
		return id;
	}

	public Timestamp getIssueDate() {
		return issueDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getName() {
		return name;
	}

	public int getOpenType() {
		return openType;
	}

	public int getReadNum() {
		return readNum;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setAuthorId(int i) {
		authorId = i;
	}

	public void setBody(String l) {
		body = l;
	}

	public void setId(int i) {
		id = i;
	}

	public void setIssueDate(Timestamp timestamp) {
		issueDate = timestamp;
	}

	public void setKeyword(String string) {
		keyword = string;
	}

	public void setName(String string) {
		name = string;
	}

	public void setOpenType(int i) {
		openType = i;
	}

	public void setReadNum(int i) {
		readNum = i;
	}

	public void setReplyNum(int i) {
		replyNum = i;
	}

	public String getArtType() {
		return artType;
	}

	public void setArtType(String string) {
		artType = string;
	}

	public Timestamp getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Timestamp timestamp) {
		replyDate = timestamp;
	}

}
