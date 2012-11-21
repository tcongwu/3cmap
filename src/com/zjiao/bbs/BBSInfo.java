package com.zjiao.bbs;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * 讨论信息对象，用于保存讨论信息
 * @author Lee Bin
 */
public class BBSInfo extends ActionForm implements Serializable{

	private int id;
	private int classId;
	private int parentId;
	private String title;
	private String content;
	private int isTop;
	private String status;
	private int authorId;
	private String authorType;
	private String authorName;
	private Timestamp createTime;
	private int visitCount;
	private int replyCount;
	
	/**
	 * 判断当前讨论是否有效
	 * @return boolean 是否有效
	 */
	public boolean isValid() {
		return SysUtils.STATUS_NORMAL.equals(status);
	}

	public int getId() {
		return id;
	}

	public int getClassId() {
		return classId;
	}

	public int getParentId() {
		return parentId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public int getIsTop() {
		return isTop;
	}

	public String getStatus() {
		return status;
	}

	public int getAuthorId() {
		return authorId;
	}

	public String getAuthorType() {
		return authorType;
	}

	public String getAuthorName() {
		return authorName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	//----------------------------------------
	//----------------------------------------
	public void setId(int it) {
		id = it;
	}
	
	public void setClassId(int it) {
		classId = it;
	}
	
	public void setParentId(int it) {
		parentId = it;
	}
	
	public void setTitle(String string) {
		title = string;
	}
	
	public void setContent(String string) {
		content = string;
	}
	
	public void setIsTop(int it) {
		isTop = it;
	}
	
	public void setStatus(String string) {
		status = string;
	}
	
	public void setAuthorId(int it) {
		authorId = it;
	}
	
	public void setAuthorType(String string) {
		authorType = string;
	}
	
	public void setAuthorName(String string) {
		authorName = string;
	}
	
	public void setCreateTime(Timestamp ts) {
		createTime = ts;
	}
	
	public void setVisitCount(int it) {
		visitCount = it;
	}
	
	public void setReplyCount(int it) {
		replyCount = it;
	}

}
