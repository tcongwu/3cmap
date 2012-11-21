package com.c3map.school;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * ѧУ��Ϣ�������ڱ���ѧУ��Ϣ����
 * @author Lee Bin
 */
public class SchoolInfo extends ActionForm implements Serializable{

	private String name;
	private int type;
	private String province;
	private String city;
	private String description;
	
	/**
	 * �жϵ�ǰѧУ�Ƿ��ǳ���
	 * @return boolean �Ƿ����
	 */
	public boolean isJunior() {
		return ((type==SysUtils.SCHOOL_JUNIOR)||(type==SysUtils.SCHOOL_MIDDLE));
	}
	
	/**
	 * �жϵ�ǰѧУ�Ƿ��Ǹ���
	 * @return boolean �Ƿ����
	 */
	public boolean isSenior() {
		return ((type==SysUtils.SCHOOL_SENIOR)||(type==SysUtils.SCHOOL_MIDDLE));
	}
	
	/**
	 * �жϵ�ǰѧУ�Ƿ��Ǵ�ѧ
	 * @return boolean �Ƿ����
	 */
	public boolean isUniv() {
		return (type==SysUtils.SCHOOL_UNIV);
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getDescription() {
		return description;
	}

	//----------------------------------------
	//----------------------------------------
	public void setName(String string) {
		name = string;
	}
	
	public void setType(int it) {
		type = it;
	}
	
	public void setProvince(String string) {
		province = string;
	}
	
	public void setCity(String string) {
		city = string;
	}
	
	public void setDescription(String string) {
		description = string;
	}
	
}
