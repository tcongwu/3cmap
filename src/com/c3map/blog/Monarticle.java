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
public class Monarticle {

	private int articleId;    //id
	private int authorId;//作者id;
	private String montime;//发表时间issuedate,按月计;
	private int monnum;//每月统计文章数;
	
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public int getMonnum() {
		return monnum;
	}
	public void setMonnum(int monnum) {
		this.monnum = monnum;
	}
	public String getMontime() {
		return montime;
	}
	public void setMontime(String montime) {
		this.montime = montime;
	}
	
	
	
}
