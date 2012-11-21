/*
 * 创建日期 2006-8-2
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.c3map.mailbox;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
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
	 * 获取某信件箱信息列表
	 * @param id 讨论区ID
	 * @throws SQLException 数据库异常
	 */
	public static MailInfo getMailInfo(int id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		MailInfo  mail=null;
		
		try {
		  /** 从数据库中检索信件信息 */
		  String sql = "select poster_id,poster_name,letter_subject,letter_body,sender_id,sender_name,send_date，status from mailbox where id=?";
		  
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
	 * 根据收信者id和信件状态status获取某信件箱信息列表 (信件状态 ‘0’表示未读；‘1’表示已读；)
	 * @param poster_id  收信者Poster_ID
	 * @param status 信件状态
	 * @throws SQLException 数据库异常
	 */
	public static MailInfo getMailByPosterId(int poster_id,String status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		MailInfo  mail=null;
		
		try {
		  /** 从数据库中检索信件信息 */
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
	 * 发信件
	 * @param mail  MailInfo对象
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	
	public static int sendMail(MailInfo mail) throws SQLException {
		if(mail==null)
			return -1;
		int uid=0;	
		
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		/** 获取信件箱ID值 */
			try{
		
				SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_MAILID);
				 uid=keygen.getNextKey();
			}catch(Exception e)
			  { 
				 e.printStackTrace();            
		
			 }

		DBSource dbsrc = new DBSource();
		
		/** 在信件箱中插入新记录 */

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
