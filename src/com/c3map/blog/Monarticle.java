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
public class Monarticle {

	private int articleId;    //id
	private int authorId;//����id;
	private String montime;//����ʱ��issuedate,���¼�;
	private int monnum;//ÿ��ͳ��������;
	
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
