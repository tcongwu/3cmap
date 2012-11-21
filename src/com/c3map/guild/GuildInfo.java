package com.c3map.guild;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * ��ҵ������Ϣ�������ڱ�����ҵ��Ϣ����
 * @author Lee Bin
 */
public class GuildInfo extends ActionForm implements Serializable{

	private int id;
	private String name;
	private String classify;
	private int type;
	
	/**
	 * �жϵ�ǰ�����Ƿ����ڳ���
	 * @return boolean �Ƿ����
	 */
	public boolean isJunior() {
		return ((type==SysUtils.GUILD_TYPE_ALL)||(type==SysUtils.GUILD_TYPE_MIDDLE)||(type==SysUtils.GUILD_TYPE_JUNIOR));
	}
	
	/**
	 * �жϵ�ǰ�����Ƿ����ڸ���
	 * @return boolean �Ƿ����
	 */
	public boolean isSenior() {
		return ((type==SysUtils.GUILD_TYPE_ALL)||(type==SysUtils.GUILD_TYPE_MIDDLE)||(type==SysUtils.GUILD_TYPE_SENIOR));
	}
	
	/**
	 * �жϵ�ǰ�����Ƿ����ڴ�ѧ
	 * @return boolean �Ƿ����
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
