/*
 * 创建日期 2006-7-30
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.c3map.blog;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

public class Article extends ActionForm implements Serializable{
	
	private int id ;    //文章id;
	private int authorId; //作者id;
	private String name;  //文章name;
	private String keyword; //文章关键词；
	private String   body;  //文章内容；
	private int openType; //文章公开类型；
	private Timestamp issueDate; //文章发表日期
	private int readNum; //文章阅读次数；
	private int replyNum; //文章回复次数；
	private String artType;//文章类别；
	private Timestamp replyDate;//最近回复时间
	
	public int getAuthorId() {
		return authorId;
	}

	public String getBody() {
		return body;
	}

	public int getId() {
		return id;
	}

	public Timestamp getIssueDate() {
		return issueDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getName() {
		return name;
	}

	public int getOpenType() {
		return openType;
	}

	public int getReadNum() {
		return readNum;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setAuthorId(int i) {
		authorId = i;
	}

	public void setBody(String l) {
		body = l;
	}

	public void setId(int i) {
		id = i;
	}

	public void setIssueDate(Timestamp timestamp) {
		issueDate = timestamp;
	}

	public void setKeyword(String string) {
		keyword = string;
	}

	public void setName(String string) {
		name = string;
	}

	public void setOpenType(int i) {
		openType = i;
	}

	public void setReadNum(int i) {
		readNum = i;
	}

	public void setReplyNum(int i) {
		replyNum = i;
	}

	public String getArtType() {
		return artType;
	}

	public void setArtType(String string) {
		artType = string;
	}

	public Timestamp getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Timestamp timestamp) {
		replyDate = timestamp;
	}

}
