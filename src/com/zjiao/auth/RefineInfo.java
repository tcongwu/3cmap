package com.zjiao.auth;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

/** 
 * 用户精要信息对象，用于保存用户精要信息数据
 * @author Lee Bin
 */
public class RefineInfo extends ActionForm implements Serializable{

	private int id;
	private String realName;
	private String photo;
	private String school;
	private String grade;
	private int blogId;
	private String blogTitle;
	private Timestamp blogTime;

	public int getId() {
		return id;
	}

	public String getRealName() {
		return realName;
	}

	public String getPhoto() {
		return photo;
	}

	public String getSchool() {
		return school;
	}

	public String getGrade() {
		return grade;
	}

	public int getBlogId() {
		return blogId;
	}

	public String getBlogTitle() {
		return blogTitle;
	}

	public Timestamp getBlogTime() {
		return blogTime;
	}

	//----------------------------------------
	//----------------------------------------
	
	public void setId(int it) {
		id = it;
	}

	public void setRealName(String string) {
		realName = string;
	}
	
	public void setPhoto(String string) {
		photo = string;
	}
	
	public void setSchool(String string) {
		school = string;
	}
	
	public void setGrade(String string) {
		grade = string;
	}
	
	public void setBlogId(int it) {
		blogId = it;
	}
	
	public void setBlogTitle(String string) {
		blogTitle = string;
	}
	
	public void setBlogTime(Timestamp ts) {
		blogTime = ts;
	}
}