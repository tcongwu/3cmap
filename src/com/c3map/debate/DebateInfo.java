package com.c3map.debate;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

public class DebateInfo extends ActionForm implements Serializable{
	
	private int id;    //������id
	private int authorId;//��������������
	private String authorName;//����������
	private String debateName;//����������
	private String debateBody;//����������
	private Timestamp debateDate;//����������ʱ��
	
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public String getDebateBody() {
		return debateBody;
	}
	public void setDebateBody(String debateBody) {
		this.debateBody = debateBody;
	}
	public Timestamp getDebateDate() {
		return debateDate;
	}
	public void setDebateDate(Timestamp debateDate) {
		this.debateDate = debateDate;
	}
	public String getDebateName() {
		return debateName;
	}
	public void setDebateName(String debateName) {
		this.debateName = debateName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

}
