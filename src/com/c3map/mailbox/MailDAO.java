/*
 * �������� 2006-8-2
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.mailbox;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.c3map.key.*;


import com.zjiao.db.DBSource;

public class MailDAO {

	public MailDAO() {
		}


	/**
	 * ��ȡĳ�ż�����Ϣ�б�
	 * @param id ������ID
	 * @throws SQLException ���ݿ��쳣
	 */
	public static MailInfo getMailInfo(int id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		MailInfo  mail=null;
		
		try {
		  /** �����ݿ��м����ż���Ϣ */
		  String sql = "select poster_id,poster_name,letter_subject,letter_body,sender_id,sender_name,send_date��status from mailbox where id=?";
		  
		  //sql=sql+" order by istop desc, id desc";
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, id);
		  
		  dbsrc.executeQuery();
			
		  mail=new MailInfo();
		  
		  while(dbsrc.next()){
			
			mail.setPosterId(dbsrc.getInt("poster_id"));
			mail.setPosterName(dbsrc.getString("poster_name"));
			mail.setLetterSubject(dbsrc.getString("letter_subject"));
			mail.setLetterSubject(dbsrc.getString("letter_body"));
			mail.setSenderId(dbsrc.getInt("poster_id"));
			mail.setSenderName(dbsrc.getString("poster_name"));
			mail.setSendDate(dbsrc.getTimestamp("send_date"));
			mail.setStatus(dbsrc.getString("status"));
				
			  
				//page.addData(bbs);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return mail;
	}
	



	/**
	 * ����������id���ż�״̬status��ȡĳ�ż�����Ϣ�б� (�ż�״̬ ��0����ʾδ������1����ʾ�Ѷ���)
	 * @param poster_id  ������Poster_ID
	 * @param status �ż�״̬
	 * @throws SQLException ���ݿ��쳣
	 */
	public static MailInfo getMailByPosterId(int poster_id,String status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		MailInfo  mail=null;
		
		try {
		  /** �����ݿ��м����ż���Ϣ */
		  String sql = "select id,poster_name,letter_subject,letter_body,sender_id,sender_name,send_date from mailbox where poster_id=? and status=?";
		  
		  //sql=sql+" order by istop desc, id desc";
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, poster_id);
		  dbsrc.setString(2,status);
		  
		  dbsrc.executeQuery();
		
		  mail=new MailInfo();
		  
		  while(dbsrc.next()){
			
			mail.setPosterId(dbsrc.getInt("id"));
			mail.setPosterName(dbsrc.getString("poster_name"));
			mail.setLetterSubject(dbsrc.getString("letter_subject"));
			mail.setLetterSubject(dbsrc.getString("letter_body"));
			mail.setSenderId(dbsrc.getInt("poster_id"));
			mail.setSenderName(dbsrc.getString("poster_name"));
			mail.setSendDate(dbsrc.getTimestamp("send_date"));
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return mail;
	}
	
	/**
	 * ���ż�
	 * @param mail  MailInfo����
	 * @return	int ����״̬
	 * @throws SQLException ���ݿ��쳣
	 */
	
	public static int sendMail(MailInfo mail) throws SQLException {
		if(mail==null)
			return -1;
		int uid=0;	
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		/** ��ȡ�ż���IDֵ */
			try{
		
				SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_MAILID);
				 uid=keygen.getNextKey();
			}catch(Exception e)
			  { 
				 e.printStackTrace();            
		
			 }

		DBSource dbsrc = new DBSource();
		
		/** ���ż����в����¼�¼ */

	try {
		  String sql = "insert into mailbox(id,poster_id,poster_name,letter_subject,letter_body,sender_id,sender_name,send_date,status) values(?,?,?,?,?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, mail.getPosterId());
		  dbsrc.setString(3, mail.getPosterName());
		  dbsrc.setString(4, mail.getLetterSubject());
		  dbsrc.setString(5,mail.getLetterBody());
		  dbsrc.setInt(6, mail.getSenderId());
			dbsrc.setString(7, mail.getSenderName());
		  dbsrc.setTimestamp(8, now);
		  dbsrc.setString(9,mail.getStatus());
		 
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}

	

}
