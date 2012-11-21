package com.c3map.debate;

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;


import com.zjiao.db.DBSource;

import com.c3map.key.*;

public class DebateDAO {
	
	public DebateDAO(){}
	
	/**
	 * 获取某讨论区信息列表
	 * @param id 讨论区ID
	 * @throws SQLException 数据库异常
	 */
	public static DebateInfo getDebateInfo(int id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		DebateInfo debate=null;
		
		try {
		  /** 从数据库中检索班级讨论信息 */
		  String sql = "select author_id,author_name,debate_name,debate_body,debate_date from debate where id=?";
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, id);
		  dbsrc.executeQuery();
			
		  debate=new DebateInfo();
		  
		  while(dbsrc.next()){
			
			  debate.setAuthorId(dbsrc.getInt("author_id"));
			  debate.setAuthorName(dbsrc.getString("author_name"));
			  debate.setDebateName(dbsrc.getString("debate_name"));
			  debate.setDebateBody(dbsrc.getString("debate_body"));
			  debate.setDebateDate(dbsrc.getTimestamp("debate_date"));
				
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return debate;
	}
	

	
	/**
	 * 创建讨论区
	 * @param debate 讨论信息对象
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int createDebate(DebateInfo debate) throws SQLException {
		if(debate==null)
			return -1;
	   int uid=0;		
		
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		/** 获取讨论区ID值 */
					try{
		
						SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_DEBATEID);
						 uid=keygen.getNextKey();
					}catch(Exception e)
					  { 
						 e.printStackTrace();            
		
					 }

		DBSource dbsrc = new DBSource();
		try {
		  /** 在讨论信息表中插入新记录 */
		  String sql = "insert into debate(id,author_id,author_name,debate_name,debate_body,debate_date) values(?,?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, debate.getAuthorId());
		  dbsrc.setString(3, debate.getAuthorName());
		  dbsrc.setString(4, debate.getDebateName());
		  dbsrc.setString(5,debate.getDebateBody());
		  dbsrc.setTimestamp(6, now);
		 
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	
	/**
	 * 修改讨论区
	 * @param id 讨论区ID
	 * @return 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int editDebate(DebateInfo debate) throws SQLException {

		if(debate==null)
			return -1;
		Timestamp debate_date=new Timestamp(System.currentTimeMillis());
		DBSource dbsrc = new DBSource();
		try {
		  /** 根据id修改讨论区信息 */
		  String sql = "update  debate set author_id=?,author_name=?,debate_name=?,debate_body=?,debate_date=? where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1,debate.getAuthorId());
		  dbsrc.setString(2, debate.getAuthorName());
		  dbsrc.setString(3, debate.getDebateName());
		  dbsrc.setString(4, debate.getDebateBody());
		  dbsrc.setTimestamp(5,debate_date);
		  dbsrc.setInt(6,debate.getId());
	  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}


	/**
	 * 删除某一讨论区
	 * @param id 文章ID
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int deleteDebate(int id) throws SQLException {
	

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除某一讨论区 */
			String sql="delete from debate where id=?";
		    dbsrc.prepareStatement(sql.toString());
		     dbsrc.setInt(1, id);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 加入某一讨论区
	 * @param id 讨论区ID
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int joinDebate(int id,int join_id,String join_name,String join_status) throws SQLException{
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 加入某一讨论区 */
			String sql="insert into join_debate(debate_id,join_id,join_name,join_status) values(?,?,?,?)";
		
		  
		  dbsrc.prepareStatement(sql.toString());
		  dbsrc.setInt(1, id);
		  dbsrc.setInt(2,join_id);
		  dbsrc.setString(3,join_name);
		  dbsrc.setString(4,join_status);
		  
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
		
	}
	
	
}
