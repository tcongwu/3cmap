package com.c3map.debate;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

public class DebateInfo extends ActionForm implements Serializable{
	
	private int id;    //讨论区id
	private int authorId;//讨论区创建作者
	private String authorName;//创建者姓名
	private String debateName;//讨论区名称
	private String debateBody;//讨论区内容
	private Timestamp debateDate;//讨论区创建时间
	
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
