/*
 * �������� 2006-7-30
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.blog;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
public class Rearticle extends ActionForm implements Serializable {

private int id;//�ظ�����id;
private int authorId; //�ظ�����id;
private String authorName;//�ظ�����id;
private int articleId;//ԭ����id;
private String artName;//�ظ����±���;
private String artBody;//�ظ���������;
private Timestamp artDate;//�ظ�����;


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
