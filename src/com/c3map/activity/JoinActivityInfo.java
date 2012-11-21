package com.c3map.activity;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class JoinActivityInfo extends ActionForm implements Serializable{
	private int actId;            	//活动id
	private int joinerId;        	//活动参与者id
	private String joinername;  	//参与者姓名
	private int status;         	//活动参与者状态 1表示加入；2表示关注;3表示留言状态
	private String message;      	//留言内容
	private Timestamp mestime;  	//留言时间
	private Timestamp jointime; 	//加入时间
	private String joinerschool; 	//留言者学校
	
	public int getActId() {
		return actId;
	}
	public void setActId(int actId) {
		this.actId = actId;
	}
	
	public int getJoinerId() {
		return joinerId;
	}
	public void setJoinerId(int joinerId) {
		this.joinerId = joinerId;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Timestamp getJointime() {
		return jointime;
	}
	public void setJointime(Timestamp jointime) {
		this.jointime = jointime;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Timestamp getMestime() {
		return mestime;
	}
	public void setMestime(Timestamp mestime) {
		this.mestime = mestime;
	}
	
	public String getJoinername() {
		return joinername;
	}
	public void setJoinername(String joinername) {
		this.joinername = joinername;
	}

	public String getJoinerschool() {
		return joinerschool;
	}
	public void setJoinerschool(String string) {
		joinerschool = string;
	}

}
