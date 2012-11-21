package com.c3map.mail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.auth.UserInfo;
import com.zjiao.db.DBSource;
import com.zjiao.db.DBTransaction;
import com.zjiao.key.KeyException;
import com.zjiao.key.KeyInit;
import com.zjiao.key.SimpleKeyGen;
import com.zjiao.util.StringUtils;

/**
 * 邮箱信息数据操作接口，任何与邮箱有关的数据操作都通过此类完成。
 * 用于封装对MailInfo类的访问，
 * 也就是mailbox表和sendbox表的访问
 * @author Lee Bin
 */
public class MailDAO {

	/**
	 * 默认的构造函数
	 */
  public MailDAO() {
	}
	
	/**
	 * 发送邮件，并保存邮件到送件箱
	 * @param minfo 邮件信息对象
	 * @return	int 邮件的ID值
	 * @throws SQLException 数据库异常
	 */
	public static int ssMail(MailInfo minfo) throws SQLException,KeyException {
		if(minfo==null)
			return -1;
			
		/** 获取收件箱邮件ID值 */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_MAILID);
		int mid=keygen.getNextKey();
			
		/** 获取送件箱邮件ID值 */
		keygen = SimpleKeyGen.getInstance(KeyInit.KEY_SENDID);
		int sid=keygen.getNextKey();
		
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
		  /** 在收件箱信息表中插入新记录 */
		  sql = "insert into mailbox (id,receiverid,receivername,senderid,sendername,sub,content,mstatus,sendtime) values(?,?,?,?,?,?,?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, mid);
		  dbtrac.setInt(2, minfo.getReceiverId());
		  dbtrac.setString(3, minfo.getReceiverName());
		  dbtrac.setInt(4, minfo.getSenderId());
		  dbtrac.setString(5, minfo.getSenderName());
		  dbtrac.setString(6, minfo.getSub());
		  dbtrac.setString(7, minfo.getContent());
		  dbtrac.setInt(8, minfo.getStatus());
		  dbtrac.setTimestamp(9, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** 插入新记录是否成功 */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }
		  
		  /** 在送件箱信息表中插入新记录 */
		  sql = "insert into sendbox (id,receiverid,receivername,senderid,sendername,sub,content,sendtime) values(?,?,?,?,?,?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, mid);
		  dbtrac.setInt(2, minfo.getReceiverId());
		  dbtrac.setString(3, minfo.getReceiverName());
		  dbtrac.setInt(4, minfo.getSenderId());
		  dbtrac.setString(5, minfo.getSenderName());
		  dbtrac.setString(6, minfo.getSub());
		  dbtrac.setString(7, minfo.getContent());
		  dbtrac.setTimestamp(8, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** 插入新记录是否成功 */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }

		  return mid;
		}
		catch (SQLException ex) {
		  dbErrors = true;
		  throw ex;
		}
		finally {
			//判断是否出现数据库错误，如果出现，回滚所有数据库操作，否则执行
			if (dbErrors) {
			  dbtrac.rollback();
			}else {
			  dbtrac.commit();
			}
			if(dbtrac!=null)
			  dbtrac.close();
		}
	}
	
	/**
	 * 发送邮件
	 * @param minfo 邮件信息对象
	 * @return	int 邮件的ID值
	 * @throws SQLException 数据库异常
	 */
	public static int sendMail(MailInfo minfo) throws SQLException,KeyException {
		if(minfo==null)
			return -1;

		/** 获取收件箱邮件ID值 */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_MAILID);
		int mid=keygen.getNextKey();
	
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** 在收件箱信息表中插入新记录 */
		  String sql="insert into mailbox (id,receiverid,receivername,senderid,sendername,sub,content,mstatus,sendtime) values(?,?,?,?,?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, mid);
		  dbsrc.setInt(2, minfo.getReceiverId());
		  dbsrc.setString(3, minfo.getReceiverName());
		  dbsrc.setInt(4, minfo.getSenderId());
		  dbsrc.setString(5, minfo.getSenderName());
		  dbsrc.setString(6, minfo.getSub());
		  dbsrc.setString(7, minfo.getContent());
		  dbsrc.setInt(8, minfo.getStatus());
		  dbsrc.setTimestamp(9, now);

		  int affectedRows = dbsrc.executeUpdate();
		  /** 插入新记录是否成功 */
		  if (affectedRows < 1) {
				return -1;
		  }
  
		  return mid;
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 获取邮件信息对象
	 * @param uinfo 读邮件的用户对象
	 * @param mid 邮件ID
	 * @return MailInfo 邮件信息对象
	 */
	public static MailInfo getMailInfo(UserInfo uinfo, int mid) throws SQLException {
		if(uinfo==null)
			return null;
		
		DBSource dbsrc = new DBSource();
		MailInfo minfo=null;
		try {
		  /** 从数据库中检索邮件信息 */
		  String sql = "select id,senderid,sendername,sub,content,mstatus,sendtime from mailbox where id=? and receiverid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, mid);
		  dbsrc.setInt(2, uinfo.getId());
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				minfo=new MailInfo();
				
				minfo.setId(dbsrc.getInt("id"));
				minfo.setSenderId(dbsrc.getInt("senderid"));
				minfo.setSenderName(dbsrc.getString("sendername"));
				minfo.setSub(dbsrc.getString("sub"));
				minfo.setContent(dbsrc.getString("content"));
				minfo.setStatus(dbsrc.getInt("mstatus"));
				minfo.setSendTime(dbsrc.getTimestamp("sendtime"));
		  }
		  
		  if(minfo!=null){
				sql = "update mailbox set mstatus=? where id=? and receiverid=?";
				dbsrc.prepareStatement(sql);
				dbsrc.setInt(1, SysUtils.MAIL_STATUS_READ);
				dbsrc.setInt(2, minfo.getId());
				dbsrc.setInt(3, uinfo.getId());
				int ret=dbsrc.executeUpdate();
				if(ret!=1){
					return null;
				}
				
				return minfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * 获取送件箱邮件信息对象
	 * @param uinfo 读邮件的用户对象
	 * @param mid 邮件ID
	 * @return MailInfo 邮件信息对象
	 */
	public static MailInfo getSendInfo(UserInfo uinfo, int mid) throws SQLException {
		if(uinfo==null)
			return null;
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索邮件信息 */
		  String sql = "select id,receiverid,receivername,sub,content,sendtime from sendbox where id=? and senderid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, mid);
		  dbsrc.setInt(2, uinfo.getId());
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				MailInfo minfo=new MailInfo();
				
				minfo.setId(dbsrc.getInt("id"));
				minfo.setReceiverId(dbsrc.getInt("receiverid"));
				minfo.setReceiverName(dbsrc.getString("receivername"));
				minfo.setSub(dbsrc.getString("sub"));
				minfo.setContent(dbsrc.getString("content"));
				minfo.setSendTime(dbsrc.getTimestamp("sendtime"));
	  	
				return minfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * 删除邮件
	 * @param uinfo 用户信息对象
	 * @param mlist 要删除的邮件ID列表
	 * @return	int 操作状态，-1－失败
	 * @throws SQLException 数据库异常
	 */
	public static int deleteMail(UserInfo uinfo, String mlist) throws SQLException {
		if((uinfo==null)||StringUtils.isNull(mlist)){
			return -1;
		}

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除收件箱信息表中记录 */
		  StringBuffer sql=new StringBuffer("delete from mailbox where id in (");
		  sql.append(mlist);
		  sql.append(") and receiverid=");
		  sql.append(uinfo.getId());
		  
		  return dbsrc.executeUpdate(sql.toString());
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 删除发件箱中邮件
	 * @param uinfo 用户信息对象
	 * @param mlist 要删除的邮件ID列表
	 * @return	int 操作状态，-1－失败
	 * @throws SQLException 数据库异常
	 */
	public static int delSendMail(UserInfo uinfo, String mlist) throws SQLException {
		if((uinfo==null)||StringUtils.isNull(mlist)){
			return -1;
		}

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除收件箱信息表中记录 */
		  StringBuffer sql=new StringBuffer("delete from sendbox where id in (");
		  sql.append(mlist);
		  sql.append(") and senderid=");
		  sql.append(uinfo.getId());
		  
		  return dbsrc.executeUpdate(sql.toString());
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 获取用户邮件的分页列表
	 * @param uid 用户ID
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageMails(int uid, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		MailInfo mail=null;
		try {
		  /** 从数据库中检索邮件信息 */
		  String sql = "select id,senderid,sendername,sub,mstatus,sendtime from mailbox where receiverid=? order by id desc";
		  dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				mail=new MailInfo();
				mail.setId(dbsrc.getInt("id"));
				mail.setSenderId(dbsrc.getInt("senderid"));
				mail.setSenderName(dbsrc.getString("sendername"));
				mail.setSub(dbsrc.getString("sub"));
				mail.setStatus(dbsrc.getInt("mstatus"));
				mail.setSendTime(dbsrc.getTimestamp("sendtime"));
			  
				page.addData(mail);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * 获取用户送件箱中邮件的分页列表
	 * @param uid 用户ID
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageSends(int uid, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		MailInfo mail=null;
		try {
		  /** 从数据库中检索邮件信息 */
		  String sql = "select id,receiverid,receivername,sub,sendtime from sendbox where senderid=? order by id desc";
		  dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				mail=new MailInfo();
				mail.setId(dbsrc.getInt("id"));
				mail.setReceiverId(dbsrc.getInt("receiverid"));
				mail.setReceiverName(dbsrc.getString("receivername"));
				mail.setSub(dbsrc.getString("sub"));
				mail.setSendTime(dbsrc.getTimestamp("sendtime"));
			  
				page.addData(mail);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * 设置邮件状态
	 * @param minfo 邮件信息对象
	 * @return 操作结果，-1－失败
	 * @throws SQLException 数据库异常
	 */
	public static int setMailStatus(MailInfo minfo) throws SQLException {
		if(minfo==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** 修改学校信息表中记录 */
			String sql = "update mailbox set mstatus=? where id=? and receiverid=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, minfo.getStatus());
			dbsrc.setInt(2, minfo.getId());
			dbsrc.setInt(3, minfo.getReceiverId());
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 获取新邮件的计数
	 * @param uid 用户ID
	 * @return 新邮件的数量
	 * @throws SQLException 数据库异常
	 */
	public static int getNewMailCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索出好友数目统计信息 */
		  String sql = "select count(*) cnt from mailbox where receiverid=? and mstatus=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, SysUtils.MAIL_STATUS_NEW);
		  dbsrc.executeQuery();
		  
		  if(dbsrc.next()){
				return dbsrc.getInt("cnt");
			}
			
			return 0;
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
	}
}