package com.c3map.activity;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class ActivityInfo extends ActionForm implements Serializable{ 
	private int id ;               //�id
	private int authorId;          //����id
	private String authorname;     //����name
	private String actname;        //�����
	private String actcontent;     //�����
	private String acthost;        //��ٰ췽
	private String actplace;       //��ص�
	private Timestamp start;       //���ʼ����
	private Timestamp end;         //���������
	private String phone;          //��ϵ�绰
	private String email;          //��ϵemail
	private Timestamp creattime;   //����ʱ��
	private int status ;           //�״̬ 1��ʾ��ͨ 2��ʾ�ȵ�
	
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
