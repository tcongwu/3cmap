package com.c3map.guild;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * 行业分类信息对象，用于保存行业信息数据
 * @author Lee Bin
 */
public class GuildInfo extends ActionForm implements Serializable{

	private int id;
	private String name;
	private String classify;
	private int type;
	
	/**
	 * 判断当前分类是否属于初中
	 * @return boolean 是否符合
	 */
	public boolean isJunior() {
		return ((type==SysUtils.GUILD_TYPE_ALL)||(type==SysUtils.GUILD_TYPE_MIDDLE)||(type==SysUtils.GUILD_TYPE_JUNIOR));
	}
	
	/**
	 * 判断当前分类是否属于高中
	 * @return boolean 是否符合
	 */
	public boolean isSenior() {
		return ((type==SysUtils.GUILD_TYPE_ALL)||(type==SysUtils.GUILD_TYPE_MIDDLE)||(type==SysUtils.GUILD_TYPE_SENIOR));
	}
	
	/**
	 * 判断当前分类是否属于大学
	 * @return boolean 是否符合
	 */
	public boolean isUniv() {
		return ((type==SysUtils.GUILD_TYPE_ALL)||(type==SysUtils.GUILD_TYPE_UNIV));
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getClassify() {
		return classify;
	}

	public int getType() {
		return type;
	}

	//----------------------------------------
	//----------------------------------------
	public void setId(int it) {
		id = it;
	}
	
	public void setName(String string) {
		name = string;
	}
	
	public void setClassify(String string) {
		classify = string;
	}
	
	public void setType(int it) {
		type = it;
	}
}
