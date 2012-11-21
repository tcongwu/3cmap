package com.c3map.board;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

/** 
 * 留言板信息对象，用于保存用户留言信息数据
 * @author Lee Bin
 */
public class BoardInfo extends ActionForm implements Serializable{

	private int id;
	private int userId;
	private int leaveId;
	private String realName;
	private String photo;
	private String school;
	private String content;
	private Timestamp leaveTime;

	public int getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public int getLeaveId() {
		return leaveId;
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

	public String getContent() {
		return content;
	}

	public Timestamp getLeaveTime() {
		return leaveTime;
	}

	//----------------------------------------
	//----------------------------------------
	public void setId(int it) {
		id = it;
	}
	
	public void setUserId(int it) {
		userId = it;
	}
	
	public void setLeaveId(int it) {
		leaveId = it;
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
	
	public void setContent(String string) {
		content = string;
	}
	
	public void setLeaveTime(Timestamp ts) {
		leaveTime = ts;
	}
}
