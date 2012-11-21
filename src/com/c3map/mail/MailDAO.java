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
 * ������Ϣ���ݲ����ӿڣ��κ��������йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��MailInfo��ķ��ʣ�
 * Ҳ����mailbox���sendbox��ķ���
 * @author Lee Bin
 */
public class MailDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public MailDAO() {
	}
	
	/**
	 * �����ʼ����������ʼ����ͼ���
	 * @param minfo �ʼ���Ϣ����
	 * @return	int �ʼ���IDֵ
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int ssMail(MailInfo minfo) throws SQLException,KeyException {
		if(minfo==null)
			return -1;
			
		/** ��ȡ�ռ����ʼ�IDֵ */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_MAILID);
		int mid=keygen.getNextKey();
			
		/** ��ȡ�ͼ����ʼ�IDֵ */
		keygen = SimpleKeyGen.getInstance(KeyInit.KEY_SENDID);
		int sid=keygen.getNextKey();
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
		  /** ���ռ�����Ϣ���в����¼�¼ */
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
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }
		  
		  /** ���ͼ�����Ϣ���в����¼�¼ */
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
		  /** �����¼�¼�Ƿ�ɹ� */
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
			//�ж��Ƿ�������ݿ����������֣��ع��������ݿ����������ִ��
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
	 * �����ʼ�
	 * @param minfo �ʼ���Ϣ����
	 * @return	int �ʼ���IDֵ
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int sendMail(MailInfo minfo) throws SQLException,KeyException {
		if(minfo==null)
			return -1;

		/** ��ȡ�ռ����ʼ�IDֵ */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_MAILID);
		int mid=keygen.getNextKey();
	
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** ���ռ�����Ϣ���в����¼�¼ */
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
		  /** �����¼�¼�Ƿ�ɹ� */
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
	 * ��ȡ�ʼ���Ϣ����
	 * @param uinfo ���ʼ����û�����
	 * @param mid �ʼ�ID
	 * @return MailInfo �ʼ���Ϣ����
	 */
	public static MailInfo getMailInfo(UserInfo uinfo, int mid) throws SQLException {
		if(uinfo==null)
			return null;
		
		DBSource dbsrc = new DBSource();
		MailInfo minfo=null;
		try {
		  /** �����ݿ��м����ʼ���Ϣ */
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
	 * ��ȡ�ͼ����ʼ���Ϣ����
	 * @param uinfo ���ʼ����û�����
	 * @param mid �ʼ�ID
	 * @return MailInfo �ʼ���Ϣ����
	 */
	public static MailInfo getSendInfo(UserInfo uinfo, int mid) throws SQLException {
		if(uinfo==null)
			return null;
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����ʼ���Ϣ */
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
	 * ɾ���ʼ�
	 * @param uinfo �û���Ϣ����
	 * @param mlist Ҫɾ�����ʼ�ID�б�
	 * @return	int ����״̬��-1��ʧ��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int deleteMail(UserInfo uinfo, String mlist) throws SQLException {
		if((uinfo==null)||StringUtils.isNull(mlist)){
			return -1;
		}

		DBSource dbsrc = new DBSource();
		try {
		  /** ɾ���ռ�����Ϣ���м�¼ */
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
	 * ɾ�����������ʼ�
	 * @param uinfo �û���Ϣ����
	 * @param mlist Ҫɾ�����ʼ�ID�б�
	 * @return	int ����״̬��-1��ʧ��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int delSendMail(UserInfo uinfo, String mlist) throws SQLException {
		if((uinfo==null)||StringUtils.isNull(mlist)){
			return -1;
		}

		DBSource dbsrc = new DBSource();
		try {
		  /** ɾ���ռ�����Ϣ���м�¼ */
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
	 * ��ȡ�û��ʼ��ķ�ҳ�б�
	 * @param uid �û�ID
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageMails(int uid, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		MailInfo mail=null;
		try {
		  /** �����ݿ��м����ʼ���Ϣ */
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
	 * ��ȡ�û��ͼ������ʼ��ķ�ҳ�б�
	 * @param uid �û�ID
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageSends(int uid, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		MailInfo mail=null;
		try {
		  /** �����ݿ��м����ʼ���Ϣ */
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
	 * �����ʼ�״̬
	 * @param minfo �ʼ���Ϣ����
	 * @return ���������-1��ʧ��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int setMailStatus(MailInfo minfo) throws SQLException {
		if(minfo==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �޸�ѧУ��Ϣ���м�¼ */
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
	 * ��ȡ���ʼ��ļ���
	 * @param uid �û�ID
	 * @return ���ʼ�������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int getNewMailCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м�����������Ŀͳ����Ϣ */
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