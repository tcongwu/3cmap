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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
public class Rearticle extends ActionForm implements Serializable {

private int id;//回复文章id;
private int authorId; //回复作者id;
private String authorName;//回复作者id;
private int articleId;//原文章id;
private String artName;//回复文章标题;
private String artBody;//回复文章内容;
private Timestamp artDate;//回复日期;


/**
 * @return
 */
public String getArtBody() {
	return artBody;
}

/**
 * @return
 */
public Timestamp getArtDate() {
	return artDate;
}

/**
 * @return
 */
public int getArticleId() {
	return articleId;
}

/**
 * @return
 */
public String getArtName() {
	return artName;
}

/**
 * @return
 */
public int getAuthorId() {
	return authorId;
}

/**
 * @return
 */
public int getId() {
	return id;
}

/**
 * @param l
 */
public void setArtBody(String l) {
	artBody = l;
}

/**
 * @param timestamp
 */
public void setArtDate(Timestamp timestamp) {
	artDate = timestamp;
}

/**
 * @param i
 */
public void setArticleId(int i) {
	articleId = i;
}

/**
 * @param string
 */
public void setArtName(String string) {
	artName = string;
}

/**
 * @param i
 */
public void setAuthorId(int i) {
	authorId = i;
}

/**
 * @param i
 */
public void setId(int i) {
	id = i;
}

public String getAuthorName() {
	return authorName;
}

public void setAuthorName(String authorName) {
	this.authorName = authorName;
}

}
