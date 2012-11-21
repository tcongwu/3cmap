package com.c3map.activity;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class ActivityInfo extends ActionForm implements Serializable{ 
	private int id ;               //活动id
	private int authorId;          //作者id
	private String authorname;     //作者name
	private String actname;        //活动名称
	private String actcontent;     //活动内容
	private String acthost;        //活动举办方
	private String actplace;       //活动地点
	private Timestamp start;       //活动开始日期
	private Timestamp end;         //活动结束日期
	private String phone;          //联系电话
	private String email;          //联系email
	private Timestamp creattime;   //创建时间
	private int status ;           //活动状态 1表示普通 2表示热点活动
	
	public String getActcontent() {
		return actcontent;
	}
	public void setActcontent(String actcontent) {
		this.actcontent = actcontent;
	}
		
	public String getActhost() {
		return acthost;
	}
	public void setActhost(String acthost) {
		this.acthost = acthost;
	}
	
	public String getActname() {
		return actname;
	}
	public void setActname(String actname) {
		this.actname = actname;
	}
	
	public String getActplace() {
		return actplace;
	}
	public void setActplace(String actplace) {
		this.actplace = actplace;
	}
	
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	
	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	
	public Timestamp getCreattime() {
		return creattime;
	}
	public void setCreattime(Timestamp creattime) {
		this.creattime = creattime;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Timestamp getEnd() {
		return end;
	}
	public Timestamp getStart() {
		return start;
	}

	public void setEnd(Timestamp timestamp) {
		end = timestamp;
	}
	public void setStart(Timestamp timestamp) {
		start = timestamp;
	}

}
