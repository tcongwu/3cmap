package com.c3map.school;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * 学校信息对象，用于保存学校信息数据
 * @author Lee Bin
 */
public class SchoolInfo extends ActionForm implements Serializable{

	private String name;
	private int type;
	private String province;
	private String city;
	private String description;
	
	/**
	 * 判断当前学校是否是初中
	 * @return boolean 是否符合
	 */
	public boolean isJunior() {
		return ((type==SysUtils.SCHOOL_JUNIOR)||(type==SysUtils.SCHOOL_MIDDLE));
	}
	
	/**
	 * 判断当前学校是否是高中
	 * @return boolean 是否符合
	 */
	public boolean isSenior() {
		return ((type==SysUtils.SCHOOL_SENIOR)||(type==SysUtils.SCHOOL_MIDDLE));
	}
	
	/**
	 * 判断当前学校是否是大学
	 * @return boolean 是否符合
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
