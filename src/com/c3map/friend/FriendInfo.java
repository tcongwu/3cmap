package com.c3map.friend;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

/** 
 * 好友信息对象，用于保存好友信息数据
 * @author Lee Bin
 */
public class FriendInfo extends ActionForm implements Serializable{

	private int userId;
	private int friendId;
	private String realName;
	private String photo;
	private String school;
	private String grade;
	private int blogId;
	private String blogTitle;
	private Timestamp blogTime;
	private Timestamp createTime;

	public int getUserId() {
		return userId;
	}

	public int getFriendId() {
		return friendId;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	//----------------------------------------
	//----------------------------------------
	
	public void setUserId(int it) {
		userId = it;
	}
	
	public void setFriendId(int it) {
		friendId = it;
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
	
	public void setCreateTime(Timestamp ts) {
		createTime = ts;
	}
	
}
