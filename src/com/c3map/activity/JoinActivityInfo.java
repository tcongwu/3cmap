package com.c3map.activity;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class JoinActivityInfo extends ActionForm implements Serializable{
	private int actId;            	//�id
	private int joinerId;        	//�������id
	private String joinername;  	//����������
	private int status;         	//�������״̬ 1��ʾ���룻2��ʾ��ע;3��ʾ����״̬
	private String message;      	//��������
	private Timestamp mestime;  	//����ʱ��
	private Timestamp jointime; 	//����ʱ��
	private String joinerschool; 	//������ѧУ
	
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
