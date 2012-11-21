package com.zjiao.auth;

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.zjiao.Page;
import com.zjiao.SysUtils;
import com.zjiao.key.KeyException;

import com.zjiao.key.KeyInit;
import com.zjiao.key.SimpleKeyGen;
import com.zjiao.util.Digest;
import com.zjiao.util.StringUtils;

import com.zjiao.db.DBSource;
import com.zjiao.db.DBTransaction;

/**
 * �û���Ϣ���ݲ����ӿڣ��κ����û��йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��UserInfo��Ҳ����UserInfo��ķ���
 * @author Lee Bin
 */
public class AuthUserDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
    public AuthUserDAO() {
	}
	
	/**
	 * �ж��Ƿ����ĳ�û�
	 * @param email �û�email
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistUser(String email) throws SQLException {
		if (email==null)
			return false;
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����û���Ϣ */
		  String sql = "select id from users where email=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, email);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				return true;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return false;
	}
	
	/**
	 * �ж��Ƿ����ĳ�û�
	 * @param uid �û�ID
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistUser(int uid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����û���Ϣ */
		  String sql = "select id from users where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
			return true;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return false;
	}
	
	/**
	 * ��ȡĳ�û����û�email
	 * @param uid �û�ID
	 * @return �û�email
	 * @throws SQLException ���ݿ��쳣
	 */
	public static String getUserEmail(int uid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����û���Ϣ */
		  String sql = "select email from users where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				return dbsrc.getString("email");
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ��ȡ�û���Ϣ����
	 * @param uid �û�ID
	 * @return UserInfo ������ϸ���ϵ��û�����
	 */
	public static UserInfo getUserInfo(int uid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����û���Ϣ */
		  String sql = "select * from users where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
		  	UserInfo uinfo=new UserInfo();
					
				uinfo.setId(dbsrc.getInt("id"));
				uinfo.setEmail(dbsrc.getString("email"));
				uinfo.setRealName(dbsrc.getString("realname"));
				uinfo.setGender(dbsrc.getString("gender"));
				uinfo.setBirthday(dbsrc.getDate("birthday"));
				uinfo.setBornPlace(dbsrc.getString("bornplace"));
				uinfo.setPhoto(dbsrc.getString("photo"));
				uinfo.setProvince(dbsrc.getString("province"));
				uinfo.setSchool(dbsrc.getString("school"));
				uinfo.setGrade(dbsrc.getString("grade"));
				uinfo.setScore(dbsrc.getInt("score"));
				uinfo.setUserType(dbsrc.getInt("usertype"));
				uinfo.setUserRole(dbsrc.getInt("userrole"));
				uinfo.setUserStatus(dbsrc.getInt("userstatus"));
				uinfo.setOptyInfo(dbsrc.getInt("opty_info"));
				uinfo.setOptyContact(dbsrc.getInt("opty_contact"));
				uinfo.setOptyPhoto(dbsrc.getInt("opty_photo"));
				uinfo.setOptyBlog(dbsrc.getInt("opty_blog"));
				uinfo.setOptyFriend(dbsrc.getInt("opty_friend"));
				uinfo.setOptyMail(dbsrc.getInt("opty_mail"));
				uinfo.setRegDate(dbsrc.getTimestamp("regdate"));
				uinfo.setLogCount(dbsrc.getInt("logcount"));
				uinfo.setReadCount(dbsrc.getInt("readcount"));
				uinfo.setRecentLogin(dbsrc.getTimestamp("recentlogin"));
	  	
				return uinfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ��ȡ�û���Ϣ����
	 * @param email �û�email
	 * @return UserInfo ������ϸ���ϵ��û���Ϣ����
	 */
	public static UserInfo getUserInfo(String email) throws SQLException {
		if(email==null)
			return null;
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����û���Ϣ */
		  String sql = "select * from users where email=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, email);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				UserInfo uinfo=new UserInfo();
					
				uinfo.setId(dbsrc.getInt("id"));
				uinfo.setEmail(dbsrc.getString("email"));
				uinfo.setRealName(dbsrc.getString("realname"));
				uinfo.setGender(dbsrc.getString("gender"));
				uinfo.setBirthday(dbsrc.getDate("birthday"));
				uinfo.setBornPlace(dbsrc.getString("bornplace"));
				uinfo.setPhoto(dbsrc.getString("photo"));
				uinfo.setProvince(dbsrc.getString("province"));
				uinfo.setSchool(dbsrc.getString("school"));
				uinfo.setGrade(dbsrc.getString("grade"));
				uinfo.setScore(dbsrc.getInt("score"));
				uinfo.setUserType(dbsrc.getInt("usertype"));
				uinfo.setUserRole(dbsrc.getInt("userrole"));
				uinfo.setUserStatus(dbsrc.getInt("userstatus"));
				uinfo.setOptyInfo(dbsrc.getInt("opty_info"));
				uinfo.setOptyContact(dbsrc.getInt("opty_contact"));
				uinfo.setOptyPhoto(dbsrc.getInt("opty_photo"));
				uinfo.setOptyBlog(dbsrc.getInt("opty_blog"));
				uinfo.setOptyFriend(dbsrc.getInt("opty_friend"));
				uinfo.setOptyMail(dbsrc.getInt("opty_mail"));
				uinfo.setRegDate(dbsrc.getTimestamp("regdate"));
				uinfo.setLogCount(dbsrc.getInt("logcount"));
				uinfo.setReadCount(dbsrc.getInt("readcount"));
				uinfo.setRecentLogin(dbsrc.getTimestamp("recentlogin"));
			  	
				return uinfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ������û�
	 * @param uinfo �û���Ϣ����
	 * @return	String �û�������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static String addUser(UserInfo uinfo) throws SQLException,KeyException {
		if(uinfo==null)
			return "-1";
			
		/** ��ȡ�û�IDֵ */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_USERID);
		int uid=keygen.getNextKey();
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
		  /** ���û���Ϣ���в����¼�¼ */
		  sql = "insert into users (id,email,realname,gender,password,province,school,usertype,userrole,userstatus,opty_info,opty_contact,opty_photo,opty_blog,opty_friend,opty_mail,regdate,recentlogin,readcount) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, uid);
		  dbtrac.setString(2, uinfo.getEmail());
		  dbtrac.setString(3, uinfo.getRealName());
		  dbtrac.setString(4, uinfo.getGender());
		  dbtrac.setString(5, StringUtils.encrypt(uinfo.getPassword()));
		  dbtrac.setString(6, uinfo.getProvince());
		  dbtrac.setString(7, uinfo.getSchool());
		  dbtrac.setInt(8, uinfo.getUserType());
		  dbtrac.setInt(9, uinfo.getUserRole());
		  dbtrac.setInt(10, uinfo.getUserStatus());
		  dbtrac.setInt(11, uinfo.getOptyInfo());
		  dbtrac.setInt(12, uinfo.getOptyContact());
		  dbtrac.setInt(13, uinfo.getOptyPhoto());
		  dbtrac.setInt(14, uinfo.getOptyBlog());
		  dbtrac.setInt(15, uinfo.getOptyFriend());
		  dbtrac.setInt(16, uinfo.getOptyMail());
		  dbtrac.setTimestamp(17, now);
		  dbtrac.setTimestamp(18, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return "-1";
		  }
		  
		  /** ���û���Ҫ��Ϣ���в����¼�¼ */
		  sql = "insert into users_refined (id,realname,school) values(?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, uid);
		  dbtrac.setString(2, uinfo.getRealName());
		  dbtrac.setString(3, uinfo.getSchool());
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return "-1";
		  }
			
		  /** ��ȡ������ */
		  long nl=System.currentTimeMillis();
		  Random rand = new Random(nl);
		  String acode=Digest.SHA_Id(Long.toString(rand.nextLong()) + Long.toString(nl));

		  /** ���û��������в���һ���¼�¼ */
			sql = "insert into activeinfo (activecode,userid,astatus,createtime) values(?,?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setString(1, acode);
		  dbtrac.setInt(2, uid);
		  dbtrac.setInt(3, SysUtils.BOOLEAN_YES);
		  dbtrac.setTimestamp(4, now);
			
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return "-1";
		  }

		  return acode;
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
	 * �����û��ʺ�
	 * @param acode ������
	 * @return	int ����״̬��-1�����������ѹ��ڣ�0���������Ӵ���1��δ֪����2�������ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int activateUser(String acode) throws SQLException,KeyException {
		/** ��ȡ��ǰ��������Ϣ */
		int uid=0,status=0;
		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м�����������Ϣ */
			String sql = "select userid,astatus from activeinfo where activecode=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1, acode);
			dbsrc.executeQuery();
			if (dbsrc.next()){
				uid=dbsrc.getInt("userid");
				status=dbsrc.getInt("astatus");
			}else{
				return 0;
			}
			
			if(status!=SysUtils.BOOLEAN_YES){
				return -1;
			}
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
		  /** �ڼ�����Ϣ���������û��ļ���ʱ�� */
		  sql = "update activeinfo set activetime=?,astatus=? where activecode=?";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setTimestamp(1, now);
		  dbtrac.setInt(2, SysUtils.BOOLEAN_NO);
		  dbtrac.setString(3, acode);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return 1;
		  }

		  /** ���û���Ϣ��׼�������û�״̬Ϊע���û���δ������ */
			sql = "update users set userstatus=? where id=?";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, SysUtils.USER_STATUS_APPLICATION);
		  dbtrac.setInt(2, uid);
			
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return 1;
		  }

		  return 2;
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
	 * �����û���������
	 * @param uinfo �û���Ϣ����
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int updateUser(UserInfo uinfo) throws SQLException {
		if(uinfo==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
		  /** �����û���Ϣ���е��û���Ϣ */
		  String sql = "update users set realname=?,gender=?,birthday=?,bornplace=?,photo=?,province=?,school=?,grade=?,usertype=?,userrole=?,userstatus=?,opty_info=?,opty_contact=?,opty_photo=?,opty_blog=?,opty_friend=?,opty_mail=? where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, uinfo.getRealName());
		  dbsrc.setString(2, uinfo.getGender());
		  dbsrc.setDate(3, uinfo.getBirthday());
		  dbsrc.setString(4, uinfo.getBornPlace());
		  dbsrc.setString(5, uinfo.getPhoto());
		  dbsrc.setString(6, uinfo.getProvince());
		  dbsrc.setString(7, uinfo.getSchool());
		  dbsrc.setString(8, uinfo.getGrade());
		  dbsrc.setInt(9, uinfo.getUserType());
		  dbsrc.setInt(10, uinfo.getUserRole());
		  dbsrc.setInt(11, uinfo.getUserStatus());
		  dbsrc.setInt(12, uinfo.getOptyInfo());
		  dbsrc.setInt(13, uinfo.getOptyContact());
		  dbsrc.setInt(14, uinfo.getOptyPhoto());
		  dbsrc.setInt(15, uinfo.getOptyBlog());
		  dbsrc.setInt(16, uinfo.getOptyFriend());
		  dbsrc.setInt(17, uinfo.getOptyMail());
		  dbsrc.setInt(18, uinfo.getId());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
			dbsrc.close();
		}
	}
	
	/**
	 * �����û���������
	 * @param uinfo �û���Ϣ����
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int updateUserF(UserInfo uinfo) throws SQLException {
		if(uinfo==null)
			return -1;

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
			/** �����û���Ϣ���е��û���Ϣ */
			sql = "update users set realname=?,gender=?,birthday=?,bornplace=?,photo=?,province=?,school=?,grade=?,usertype=?,userrole=?,userstatus=?,opty_info=?,opty_contact=?,opty_photo=?,opty_blog=?,opty_friend=?,opty_mail=? where id=?";
			dbtrac.prepareStatement(sql);
			dbtrac.setString(1, uinfo.getRealName());
			dbtrac.setString(2, uinfo.getGender());
			dbtrac.setDate(3, uinfo.getBirthday());
			dbtrac.setString(4, uinfo.getBornPlace());
			dbtrac.setString(5, uinfo.getPhoto());
			dbtrac.setString(6, uinfo.getProvince());
			dbtrac.setString(7, uinfo.getSchool());
			dbtrac.setString(8, uinfo.getGrade());
			dbtrac.setInt(9, uinfo.getUserType());
			dbtrac.setInt(10, uinfo.getUserRole());
			dbtrac.setInt(11, uinfo.getUserStatus());
			dbtrac.setInt(12, uinfo.getOptyInfo());
			dbtrac.setInt(13, uinfo.getOptyContact());
			dbtrac.setInt(14, uinfo.getOptyPhoto());
			dbtrac.setInt(15, uinfo.getOptyBlog());
			dbtrac.setInt(16, uinfo.getOptyFriend());
			dbtrac.setInt(17, uinfo.getOptyMail());
			dbtrac.setInt(18, uinfo.getId());
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }

		  /** �����û�������Ϣ���м�¼ */
		  sql = "update users_refined set realname=?,photo=?,school=?,grade=? where id=?";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setString(1, uinfo.getRealName());
		  dbtrac.setString(2, uinfo.getPhoto());
		  dbtrac.setString(3, uinfo.getSchool());
		  dbtrac.setString(4, uinfo.getGrade());
		  dbtrac.setInt(5, uinfo.getId());
			
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 0) {
				dbErrors = true;
				return -1;
		  }

		  return 1;
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
	 * �޸��û�����
	 * @param uid �û�ID
	 * @param oldPass ԭ������
	 * @param newPass ������
	 * @return int ״̬��Ϣ��1���ɹ���101�������ڴ��û���102��ԭ���������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int changePass(int uid, String oldPass, String newPass) throws SQLException {
		if((uid<=0)||(oldPass==null)||(newPass==null))
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м����û���Ϣ */
			String sql = "select password from users where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, uid);
			dbsrc.executeQuery();
			if (dbsrc.next()){
				if(!dbsrc.getString("password").equals(StringUtils.encrypt(oldPass))){
					return 102;
				}
			}else{
				return 101;
			}

	  	/** �����û���Ϣ���е�������Ϣ */
	  	sql = "update users set password=? where id=?";
	  	dbsrc.prepareStatement(sql);
	  	dbsrc.setString(1, StringUtils.encrypt(newPass));
	  	dbsrc.setInt(2, uid);
	  
	  	return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡ�û�����
	 * @param email �û�email
	 * @return String �û�����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static String getPass(String email) throws SQLException {
		if(email==null)
			return null;

		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м����û�������Ϣ */
			String sql = "select password from users where email=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1, email);
			dbsrc.executeQuery();
			if (dbsrc.next()){
				return StringUtils.decrypt(dbsrc.getString("password"));
			}else{
				return null;
			}

		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	/**
	 * �û���¼
	 * @param uname �û���
	 * @param pass �û�����
	 * @return UserInfo �û���Ϣ����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static UserInfo userLogin(String email, String pass) throws SQLException{
		if((email==null)||(pass==null))
			return null;

		DBSource dbsrc = new DBSource();
		UserInfo uinfo=null;
		int uid=0;
		try {
			/** �����ݿ��м����û���Ϣ */
			String sql = "select id,email,realname,password,gender,birthday,bornplace,photo,province,school,grade,score,usertype,userrole,userstatus,opty_info,opty_contact,opty_photo,opty_blog,opty_friend,opty_mail,logcount,readcount,recentlogin,regdate from users where email=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1, email);
			dbsrc.executeQuery();
			if (dbsrc.next()){
				if(!dbsrc.getString("password").equals(StringUtils.encrypt(pass))){
					return null;
				}else{
					uinfo=new UserInfo();
					
					uid=dbsrc.getInt("id");
					uinfo.setId(uid);
					uinfo.setEmail(dbsrc.getString("email"));
					uinfo.setRealName(dbsrc.getString("realname"));
					uinfo.setGender(dbsrc.getString("gender"));
					uinfo.setBirthday(dbsrc.getDate("birthday"));
					uinfo.setBornPlace(dbsrc.getString("bornplace"));
					uinfo.setPhoto(dbsrc.getString("photo"));
					uinfo.setProvince(dbsrc.getString("province"));
					uinfo.setSchool(dbsrc.getString("school"));
					uinfo.setGrade(dbsrc.getString("grade"));
					uinfo.setScore(dbsrc.getInt("score"));
					uinfo.setUserType(dbsrc.getInt("usertype"));
					uinfo.setUserRole(dbsrc.getInt("userrole"));
					uinfo.setUserStatus(dbsrc.getInt("userstatus"));
					uinfo.setOptyInfo(dbsrc.getInt("opty_info"));
					uinfo.setOptyContact(dbsrc.getInt("opty_contact"));
					uinfo.setOptyPhoto(dbsrc.getInt("opty_photo"));
					uinfo.setOptyBlog(dbsrc.getInt("opty_blog"));
					uinfo.setOptyFriend(dbsrc.getInt("opty_friend"));
					uinfo.setOptyMail(dbsrc.getInt("opty_mail"));
					uinfo.setLogCount(dbsrc.getInt("logcount")+1);
					uinfo.setReadCount(dbsrc.getInt("readcount"));
					uinfo.setRecentLogin(dbsrc.getTimestamp("recentlogin"));
					uinfo.setRegDate(dbsrc.getTimestamp("regdate"));
				}
			}else
				return null;
			
			//��¼������1������¼�����¼ʱ��
			sql = "update users set logcount=logcount+1,recentlogin=? where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			dbsrc.setInt(2, uid);
			dbsrc.executeUpdate();
					
			return uinfo;
		}
		finally {
		  if (dbsrc != null)
			dbsrc.close();
		}
	}
	
	/**
	 * �༭�������û���������
	 * @param ujunior �������û�������Ϣ����
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int updateSpecial(UserJunior ujunior) throws SQLException {
		if(ujunior==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м����������û�������Ϣ */
			String sql = "select * from users_junior where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, ujunior.getId());
			dbsrc.executeQuery();
			if (dbsrc.next()){
				if(ujunior.getMuSchool()==null){
					ujunior.setMuSchool(dbsrc.getString("mu_school"));
				}
				if(ujunior.getMuGrade()==null){
					ujunior.setMuGrade(dbsrc.getString("mu_grade"));
				}
				if(ujunior.getIdealSchool()==null){
					ujunior.setIdealSchool(dbsrc.getString("ideal_school"));
				}
				if(ujunior.getStrongSub()==null){
					ujunior.setStrongSub(dbsrc.getString("strong_sub"));
				}
				if(ujunior.getWeakSub()==null){
					ujunior.setWeakSub(dbsrc.getString("weak_sub"));
				}
				if(ujunior.getWantedAbility()==null){
					ujunior.setWantedAbility(dbsrc.getString("wanted_ability"));
				}
				if(ujunior.getInfoCare()==null){
					ujunior.setInfoCare(dbsrc.getString("info_care"));
				}
				if(ujunior.getHobbySport()==null){
					ujunior.setHobbySport(dbsrc.getString("hobby_sport"));
				}
				if(ujunior.getHobbyBook()==null){
					ujunior.setHobbyBook(dbsrc.getString("hobby_book"));
				}
				if(ujunior.getHobbyFilm()==null){
					ujunior.setHobbyFilm(dbsrc.getString("hobby_film"));
				}
				if(ujunior.getHobbyMusic()==null){
					ujunior.setHobbyMusic(dbsrc.getString("hobby_music"));
				}
				if(ujunior.getMotto()==null){
					ujunior.setMotto(dbsrc.getString("motto"));
				}
				if(ujunior.getAward()==null){
					ujunior.setAward(dbsrc.getString("award"));
				}
				if(ujunior.getCertificate()==null){
					ujunior.setCertificate(dbsrc.getString("certificate"));
				}
				if(ujunior.getPhone()==null){
					ujunior.setPhone(dbsrc.getString("phone"));
				}
				if(ujunior.getMobile()==null){
					ujunior.setMobile(dbsrc.getString("mobile"));
				}
				if(ujunior.getQq()==null){
					ujunior.setQq(dbsrc.getString("qq"));
				}
				if(ujunior.getMsn()==null){
					ujunior.setMsn(dbsrc.getString("msn"));
				}
				if(ujunior.getProvince()==null){
					ujunior.setProvince(dbsrc.getString("province"));
				}
				if(ujunior.getSchool()==null){
					ujunior.setSchool(dbsrc.getString("school"));
				}
				if(ujunior.getGrade()==null){
					ujunior.setGrade(dbsrc.getString("grade"));
				}
				if(ujunior.getAddress()==null){
					ujunior.setAddress(dbsrc.getString("address"));
				}
				if(ujunior.getCode()==null){
					ujunior.setCode(dbsrc.getString("code"));
				}
				
				//���³������û���������
				sql="update users_junior set mu_school=?,mu_grade=?,ideal_school=?,strong_sub=?,weak_sub=?,info_care=?,hobby_sport=?,hobby_book=?,hobby_film=?,hobby_music=?,motto=?,award=?,phone=?,mobile=?,qq=?,msn=?,province=?,school=?,grade=?,address=?,code=?,wanted_ability=?,certificate=? where id=?";
				
			}else{
				//����������û���������
				sql = "insert into users_junior (mu_school,mu_grade,ideal_school,strong_sub,weak_sub,info_care,hobby_sport,hobby_book,hobby_film,hobby_music,motto,award,phone,mobile,qq,msn,province,school,grade,address,code,wanted_ability,certificate,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}

		  /** �������ݿ� */
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, ujunior.getMuSchool());
		  dbsrc.setString(2, ujunior.getMuGrade());
		  dbsrc.setString(3, ujunior.getIdealSchool());
		  dbsrc.setString(4, ujunior.getStrongSub());
		  dbsrc.setString(5, ujunior.getWeakSub());
		  dbsrc.setString(6, ujunior.getInfoCare());
		  dbsrc.setString(7, ujunior.getHobbySport());
		  dbsrc.setString(8, ujunior.getHobbyBook());
		  dbsrc.setString(9, ujunior.getHobbyFilm());
		  dbsrc.setString(10, ujunior.getHobbyMusic());
		  dbsrc.setString(11, ujunior.getMotto());
		  dbsrc.setString(12, ujunior.getAward());
		  dbsrc.setString(13, ujunior.getPhone());
		  dbsrc.setString(14, ujunior.getMobile());
		  dbsrc.setString(15, ujunior.getQq());
		  dbsrc.setString(16, ujunior.getMsn());
		  dbsrc.setString(17, ujunior.getProvince());
		  dbsrc.setString(18, ujunior.getSchool());
		  dbsrc.setString(19, ujunior.getGrade());
		  dbsrc.setString(20, ujunior.getAddress());
		  dbsrc.setString(21, ujunior.getCode());
		  dbsrc.setString(22, ujunior.getWantedAbility());
		  dbsrc.setString(23, ujunior.getCertificate());
		  dbsrc.setInt(24, ujunior.getId());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
			dbsrc.close();
		}
	}
	
	/**
	 * �༭�������û���������
	 * @param usenior �������û�������Ϣ����
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int updateSpecial(UserSenior usenior) throws SQLException {
		if(usenior==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м����������û�������Ϣ */
			String sql = "select * from users_senior where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, usenior.getId());
			dbsrc.executeQuery();
			if (dbsrc.next()){
				if(usenior.getMuSchool()==null){
					usenior.setMuSchool(dbsrc.getString("mu_school"));
				}
				if(usenior.getMuGrade()==null){
					usenior.setMuGrade(dbsrc.getString("mu_grade"));
				}
				if(usenior.getIdealSchool()==null){
					usenior.setIdealSchool(dbsrc.getString("ideal_school"));
				}
				if(usenior.getStrongSub()==null){
					usenior.setStrongSub(dbsrc.getString("strong_sub"));
				}
				if(usenior.getWeakSub()==null){
					usenior.setWeakSub(dbsrc.getString("weak_sub"));
				}
				if(usenior.getWantedAbility()==null){
					usenior.setWantedAbility(dbsrc.getString("wanted_ability"));
				}
				if(usenior.getInfoCare()==null){
					usenior.setInfoCare(dbsrc.getString("info_care"));
				}
				if(usenior.getHobbySport()==null){
					usenior.setHobbySport(dbsrc.getString("hobby_sport"));
				}
				if(usenior.getHobbyBook()==null){
					usenior.setHobbyBook(dbsrc.getString("hobby_book"));
				}
				if(usenior.getHobbyFilm()==null){
					usenior.setHobbyFilm(dbsrc.getString("hobby_film"));
				}
				if(usenior.getHobbyMusic()==null){
					usenior.setHobbyMusic(dbsrc.getString("hobby_music"));
				}
				if(usenior.getMotto()==null){
					usenior.setMotto(dbsrc.getString("motto"));
				}
				if(usenior.getAward()==null){
					usenior.setAward(dbsrc.getString("award"));
				}
				if(usenior.getCertificate()==null){
					usenior.setCertificate(dbsrc.getString("certificate"));
				}
				if(usenior.getPhone()==null){
					usenior.setPhone(dbsrc.getString("phone"));
				}
				if(usenior.getMobile()==null){
					usenior.setMobile(dbsrc.getString("mobile"));
				}
				if(usenior.getQq()==null){
					usenior.setQq(dbsrc.getString("qq"));
				}
				if(usenior.getMsn()==null){
					usenior.setMsn(dbsrc.getString("msn"));
				}
				if(usenior.getProvince()==null){
					usenior.setProvince(dbsrc.getString("province"));
				}
				if(usenior.getSchool()==null){
					usenior.setSchool(dbsrc.getString("school"));
				}
				if(usenior.getGrade()==null){
					usenior.setGrade(dbsrc.getString("grade"));
				}
				if(usenior.getAddress()==null){
					usenior.setAddress(dbsrc.getString("address"));
				}
				if(usenior.getCode()==null){
					usenior.setCode(dbsrc.getString("code"));
				}
				
				//���³������û���������
				sql="update users_senior set mu_school=?,mu_grade=?,ideal_school=?,strong_sub=?,weak_sub=?,info_care=?,hobby_sport=?,hobby_book=?,hobby_film=?,hobby_music=?,motto=?,award=?,phone=?,mobile=?,qq=?,msn=?,province=?,school=?,grade=?,address=?,code=?,wanted_ability=?,certificate=? where id=?";
				
			}else{
				//����������û���������
				sql = "insert into users_senior (mu_school,mu_grade,ideal_school,strong_sub,weak_sub,info_care,hobby_sport,hobby_book,hobby_film,hobby_music,motto,award,phone,mobile,qq,msn,province,school,grade,address,code,wanted_ability,certificate,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}

		  /** �������ݿ� */
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, usenior.getMuSchool());
		  dbsrc.setString(2, usenior.getMuGrade());
		  dbsrc.setString(3, usenior.getIdealSchool());
		  dbsrc.setString(4, usenior.getStrongSub());
		  dbsrc.setString(5, usenior.getWeakSub());
		  dbsrc.setString(6, usenior.getInfoCare());
		  dbsrc.setString(7, usenior.getHobbySport());
		  dbsrc.setString(8, usenior.getHobbyBook());
		  dbsrc.setString(9, usenior.getHobbyFilm());
		  dbsrc.setString(10, usenior.getHobbyMusic());
		  dbsrc.setString(11, usenior.getMotto());
		  dbsrc.setString(12, usenior.getAward());
		  dbsrc.setString(13, usenior.getPhone());
		  dbsrc.setString(14, usenior.getMobile());
		  dbsrc.setString(15, usenior.getQq());
		  dbsrc.setString(16, usenior.getMsn());
		  dbsrc.setString(17, usenior.getProvince());
		  dbsrc.setString(18, usenior.getSchool());
		  dbsrc.setString(19, usenior.getGrade());
		  dbsrc.setString(20, usenior.getAddress());
		  dbsrc.setString(21, usenior.getCode());
		  dbsrc.setString(22, usenior.getWantedAbility());
		  dbsrc.setString(23, usenior.getCertificate());
		  dbsrc.setInt(24, usenior.getId());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
			dbsrc.close();
		}
	}
	
	/**
	 * �༭��ѧ���û���������
	 * @param uuniv ��ѧ���û�������Ϣ����
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int updateSpecial(UserUniv uuniv) throws SQLException {
		if(uuniv==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м�����ѧ���û�������Ϣ */
			String sql = "select * from users_univ where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, uuniv.getId());
			dbsrc.executeQuery();
			if (dbsrc.next()){
				if(uuniv.getMuSchool()==null){
					uuniv.setMuSchool(dbsrc.getString("mu_school"));
				}
				if(uuniv.getMuGrade()==null){
					uuniv.setMuGrade(dbsrc.getString("mu_grade"));
				}
				if(uuniv.getMuTeacher()==null){
					uuniv.setMuTeacher(dbsrc.getString("mu_teacher"));
				}
				if(uuniv.getMuPhone()==null){
					uuniv.setMuPhone(dbsrc.getString("mu_phone"));
				}
				if(uuniv.getMuEmail()==null){
					uuniv.setMuEmail(dbsrc.getString("mu_email"));
				}
				if(uuniv.getMuMobile()==null){
					uuniv.setMuMobile(dbsrc.getString("mu_mobile"));
				}
				if(uuniv.getMuAddress()==null){
					uuniv.setMuAddress(dbsrc.getString("mu_address"));
				}
				if(uuniv.getOtherContact()==null){
					uuniv.setOtherContact(dbsrc.getString("other_contact"));
				}
				if(uuniv.getIdealWork()==null){
					uuniv.setIdealWork(dbsrc.getString("ideal_work"));
				}
				if(uuniv.getInfoCare()==null){
					uuniv.setInfoCare(dbsrc.getString("info_care"));
				}
				if(uuniv.getHobbySport()==null){
					uuniv.setHobbySport(dbsrc.getString("hobby_sport"));
				}
				if(uuniv.getHobbyBook()==null){
					uuniv.setHobbyBook(dbsrc.getString("hobby_book"));
				}
				if(uuniv.getHobbyFilm()==null){
					uuniv.setHobbyFilm(dbsrc.getString("hobby_film"));
				}
				if(uuniv.getHobbyMusic()==null){
					uuniv.setHobbyMusic(dbsrc.getString("hobby_music"));
				}
				if(uuniv.getMotto()==null){
					uuniv.setMotto(dbsrc.getString("motto"));
				}
				if(uuniv.getAward()==null){
					uuniv.setAward(dbsrc.getString("award"));
				}
				if(uuniv.getCertificate()==null){
					uuniv.setCertificate(dbsrc.getString("certificate"));
				}
				if(uuniv.getPhone()==null){
					uuniv.setPhone(dbsrc.getString("phone"));
				}
				if(uuniv.getMobile()==null){
					uuniv.setMobile(dbsrc.getString("mobile"));
				}
				if(uuniv.getQq()==null){
					uuniv.setQq(dbsrc.getString("qq"));
				}
				if(uuniv.getMsn()==null){
					uuniv.setMsn(dbsrc.getString("msn"));
				}
				if(uuniv.getProvince()==null){
					uuniv.setProvince(dbsrc.getString("province"));
				}
				if(uuniv.getSchool()==null){
					uuniv.setSchool(dbsrc.getString("school"));
				}
				if(uuniv.getGrade()==null){
					uuniv.setGrade(dbsrc.getString("grade"));
				}
				if(uuniv.getAddress()==null){
					uuniv.setAddress(dbsrc.getString("address"));
				}
				if(uuniv.getCode()==null){
					uuniv.setCode(dbsrc.getString("code"));
				}
				
				//���´�ѧ���û���������
				sql="update users_univ set mu_school=?,mu_grade=?,mu_teacher=?,mu_phone=?,mu_email=?,mu_mobile=?,mu_address=?,other_contact=?,ideal_work=?,info_care=?,hobby_sport=?,hobby_book=?,hobby_film=?,hobby_music=?,motto=?,award=?,phone=?,mobile=?,qq=?,msn=?,province=?,school=?,grade=?,address=?,code=?,certificate=? where id=?";
				
			}else{
				//�����ѧ���û���������
				sql = "insert into users_univ (mu_school,mu_grade,mu_teacher,mu_phone,mu_email,mu_mobile,mu_address,other_contact,ideal_work,info_care,hobby_sport,hobby_book,hobby_film,hobby_music,motto,award,phone,mobile,qq,msn,province,school,grade,address,code,certificate,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}

		  /** �������ݿ� */
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, uuniv.getMuSchool());
		  dbsrc.setString(2, uuniv.getMuGrade());
		  dbsrc.setString(3, uuniv.getMuTeacher());
		  dbsrc.setString(4, uuniv.getMuPhone());
		  dbsrc.setString(5, uuniv.getMuEmail());
		  dbsrc.setString(6, uuniv.getMuMobile());
		  dbsrc.setString(7, uuniv.getMuAddress());
		  dbsrc.setString(8, uuniv.getOtherContact());
		  dbsrc.setString(9, uuniv.getIdealWork());
		  dbsrc.setString(10, uuniv.getInfoCare());
		  dbsrc.setString(11, uuniv.getHobbySport());
		  dbsrc.setString(12, uuniv.getHobbyBook());
		  dbsrc.setString(13, uuniv.getHobbyFilm());
		  dbsrc.setString(14, uuniv.getHobbyMusic());
		  dbsrc.setString(15, uuniv.getMotto());
		  dbsrc.setString(16, uuniv.getAward());
		  dbsrc.setString(17, uuniv.getPhone());
		  dbsrc.setString(18, uuniv.getMobile());
		  dbsrc.setString(19, uuniv.getQq());
		  dbsrc.setString(20, uuniv.getMsn());
		  dbsrc.setString(21, uuniv.getProvince());
		  dbsrc.setString(22, uuniv.getSchool());
		  dbsrc.setString(23, uuniv.getGrade());
		  dbsrc.setString(24, uuniv.getAddress());
		  dbsrc.setString(25, uuniv.getCode());
		  dbsrc.setString(26, uuniv.getCertificate());
		  dbsrc.setInt(27, uuniv.getId());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
			dbsrc.close();
		}
	}
	
	/**
	 * ��ȡ�������û��ĸ�������
	 * @param id �û�ID
	 * @return UserJunior ���и������ϵĳ������û�����
	 */
	public static UserJunior getUserJunior(int id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����������û�������Ϣ */
		  String sql = "select * from users_junior where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				UserJunior uinfo=new UserJunior();
					
				uinfo.setId(dbsrc.getInt("id"));
				uinfo.setMuSchool(dbsrc.getString("mu_school"));
				uinfo.setMuGrade(dbsrc.getString("mu_grade"));
				uinfo.setIdealSchool(dbsrc.getString("ideal_school"));
				uinfo.setStrongSub(dbsrc.getString("strong_sub"));
				uinfo.setWeakSub(dbsrc.getString("weak_sub"));
				uinfo.setWantedAbility(dbsrc.getString("wanted_ability"));
				uinfo.setInfoCare(dbsrc.getString("info_care"));
				uinfo.setHobbySport(dbsrc.getString("hobby_sport"));
				uinfo.setHobbyBook(dbsrc.getString("hobby_book"));
				uinfo.setHobbyFilm(dbsrc.getString("hobby_film"));
				uinfo.setHobbyMusic(dbsrc.getString("hobby_music"));
				uinfo.setMotto(dbsrc.getString("motto"));
				uinfo.setAward(dbsrc.getString("award"));
				uinfo.setCertificate(dbsrc.getString("certificate"));
				uinfo.setPhone(dbsrc.getString("phone"));
				uinfo.setMobile(dbsrc.getString("mobile"));
				uinfo.setQq(dbsrc.getString("qq"));
				uinfo.setMsn(dbsrc.getString("msn"));
				uinfo.setProvince(dbsrc.getString("province"));
				uinfo.setSchool(dbsrc.getString("school"));
				uinfo.setGrade(dbsrc.getString("grade"));
				uinfo.setAddress(dbsrc.getString("address"));
				uinfo.setCode(dbsrc.getString("code"));
			  	
				return uinfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ��ȡ�������û��ĸ�������
	 * @param id �û�ID
	 * @return UserSenior ���и������ϵĸ������û�����
	 */
	public static UserSenior getUserSenior(int id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м����������û�������Ϣ */
		  String sql = "select * from users_senior where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				UserSenior uinfo=new UserSenior();
					
				uinfo.setId(dbsrc.getInt("id"));
				uinfo.setMuSchool(dbsrc.getString("mu_school"));
				uinfo.setMuGrade(dbsrc.getString("mu_grade"));
				uinfo.setIdealSchool(dbsrc.getString("ideal_school"));
				uinfo.setStrongSub(dbsrc.getString("strong_sub"));
				uinfo.setWeakSub(dbsrc.getString("weak_sub"));
				uinfo.setWantedAbility(dbsrc.getString("wanted_ability"));
				uinfo.setInfoCare(dbsrc.getString("info_care"));
				uinfo.setHobbySport(dbsrc.getString("hobby_sport"));
				uinfo.setHobbyBook(dbsrc.getString("hobby_book"));
				uinfo.setHobbyFilm(dbsrc.getString("hobby_film"));
				uinfo.setHobbyMusic(dbsrc.getString("hobby_music"));
				uinfo.setMotto(dbsrc.getString("motto"));
				uinfo.setAward(dbsrc.getString("award"));
				uinfo.setCertificate(dbsrc.getString("certificate"));
				uinfo.setPhone(dbsrc.getString("phone"));
				uinfo.setMobile(dbsrc.getString("mobile"));
				uinfo.setQq(dbsrc.getString("qq"));
				uinfo.setMsn(dbsrc.getString("msn"));
				uinfo.setProvince(dbsrc.getString("province"));
				uinfo.setSchool(dbsrc.getString("school"));
				uinfo.setGrade(dbsrc.getString("grade"));
				uinfo.setAddress(dbsrc.getString("address"));
				uinfo.setCode(dbsrc.getString("code"));
			  	
				return uinfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ��ȡ��ѧ���û��ĸ�������
	 * @param id �û�ID
	 * @return UserUniv ���и������ϵĴ�ѧ���û�����
	 */
	public static UserUniv getUserUniv(int id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м�����ѧ���û�������Ϣ */
		  String sql = "select * from users_univ where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				UserUniv uinfo=new UserUniv();
					
				uinfo.setId(dbsrc.getInt("id"));
				uinfo.setMuSchool(dbsrc.getString("mu_school"));
				uinfo.setMuGrade(dbsrc.getString("mu_grade"));
				uinfo.setMuTeacher(dbsrc.getString("mu_teacher"));
				uinfo.setMuPhone(dbsrc.getString("mu_phone"));
				uinfo.setMuEmail(dbsrc.getString("mu_email"));
				uinfo.setMuMobile(dbsrc.getString("mu_mobile"));
				uinfo.setMuAddress(dbsrc.getString("mu_address"));
				uinfo.setOtherContact(dbsrc.getString("other_contact"));
				uinfo.setIdealWork(dbsrc.getString("ideal_work"));
				uinfo.setInfoCare(dbsrc.getString("info_care"));
				uinfo.setHobbySport(dbsrc.getString("hobby_sport"));
				uinfo.setHobbyBook(dbsrc.getString("hobby_book"));
				uinfo.setHobbyFilm(dbsrc.getString("hobby_film"));
				uinfo.setHobbyMusic(dbsrc.getString("hobby_music"));
				uinfo.setMotto(dbsrc.getString("motto"));
				uinfo.setAward(dbsrc.getString("award"));
				uinfo.setCertificate(dbsrc.getString("certificate"));
				uinfo.setPhone(dbsrc.getString("phone"));
				uinfo.setMobile(dbsrc.getString("mobile"));
				uinfo.setQq(dbsrc.getString("qq"));
				uinfo.setMsn(dbsrc.getString("msn"));
				uinfo.setProvince(dbsrc.getString("province"));
				uinfo.setSchool(dbsrc.getString("school"));
				uinfo.setGrade(dbsrc.getString("grade"));
				uinfo.setAddress(dbsrc.getString("address"));
				uinfo.setCode(dbsrc.getString("code"));
			  	
				return uinfo;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return null;
	}
	
	
	/**
	 * ��ȡ�û��ĸ�������
	 * @param uinfo �û���Ϣ����
	 * @return Object ���и������ϵ��û�����
	 */
	public static Object getUserSpecial(UserInfo uinfo) throws SQLException {
		if(uinfo==null)
			return null;
			
		int type=uinfo.getUserType();
		int id=uinfo.getId();
		
		if(type==SysUtils.USER_TYPE_JUNIOR)
			return getUserJunior(id);
		else
		if(type==SysUtils.USER_TYPE_SENIOR)
			return getUserSenior(id);
		else
		if(type==SysUtils.USER_TYPE_UNIV)
			return getUserUniv(id);
			
		return null;
	}
	
	/**
	 * ���ӻ���
	 * @param uinfo �û���Ϣ����
	 * @param score ���ӵĻ���
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int addScore(UserInfo uinfo, int score) throws Exception {
		if(uinfo==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �������ݿ��û���Ϣ���л�����Ϣ */
			StringBuffer sql=new StringBuffer("update users set score=score");
			if(score>=0){
				sql.append('+');
				sql.append(score);
			}else
				sql.append(score);
			sql.append(" where id=");
			sql.append(uinfo.getId());
			dbsrc.prepareStatement(sql.toString());
			return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��¼�����ҳ���û�����Ϊ������û����ּӷ�
	 * @param uinfo ����û�����Ϣ����
	 * @param bid ������û�ID
	 * @return int ״̬��Ϣ��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int recordBrowse(UserInfo uinfo, int bid) throws SQLException {
		if(uinfo==null)
			return -1;
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
			/** ɾ���û�������Ϣ���м�¼ */
			sql = "delete from users_visited where userid=? and visitorid=?";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, bid);
			dbtrac.setInt(2, uinfo.getId());
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** ɾ���¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 0) {
				dbErrors = true;
				return -1;
		  }
		  
			/** ���û�������Ϣ���в�����ʼ�¼ */
			sql = "insert into users_visited (userid,visitorid,realname,photo,visittime) values(?,?,?,?,?)";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, bid);
			dbtrac.setInt(2, uinfo.getId());
			dbtrac.setString(3, uinfo.getRealName());
			dbtrac.setString(4, uinfo.getPhoto());
			dbtrac.setTimestamp(5, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }

		  /** �����û���Ϣ���м�¼ */
		  sql = "update users set score=score+?,readcount=readcount+1 where id=?";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, SysUtils.SCORE_BROWSED);
		  dbtrac.setInt(2, bid);
			
		  affectedRows = dbtrac.executeUpdate();
		  /** �����Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }

		  return affectedRows;
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
	 * ��ȡ������ʵ��û��б�
	 * @param uid �û�email
	 * @return RefineInfo[] �û����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static RefineInfo[] getVisitedUser(int uid) throws SQLException {

		RefineInfo[] visitors=new RefineInfo[6];
		RefineInfo visitor=null;
		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м����û�������Ϣ */
			String sql = "select visitorid,realname,photo,visittime from users_visited where userid=? order by visittime desc limit 6";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, uid);
			dbsrc.executeQuery();
			
			int i=0;
			while (dbsrc.next()&&(i<6)){
				visitor=new RefineInfo();
				visitor.setId(dbsrc.getInt("visitorid"));
				visitor.setRealName(dbsrc.getString("realname"));
				visitor.setPhoto(dbsrc.getString("photo"));
				visitor.setBlogTime(dbsrc.getTimestamp("visittime"));
				visitors[i]=visitor;
				
				i++;
			}
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
		
		return visitors;
	}
	
	/**
	 * ��������������ȡ�û��ķ�ҳ�б�
	 * @param request ��������������http�������
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageUsers(HttpServletRequest request, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		String school=null;
		String blog=null;
		if(request!=null){
			school=request.getParameter("school");
			blog=request.getParameter("blog");
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		RefineInfo friend=null;
		try {
		  /** �����ݿ��м���������Ϣ */
		  StringBuffer sql = new StringBuffer("select id,realname,photo,school,grade,blogid,blogtitle from users_refined");
			if(!StringUtils.isNull(school)){
			  sql.append(" where school=?");
			}
			if(!StringUtils.isNull(blog)){
			  sql.append(" order by blogtime desc");
			}
		  dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  if(!StringUtils.isNull(school)){
				dbsrc.setString(1, school);
		  }
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				friend=new RefineInfo();
				friend.setId(dbsrc.getInt("id"));
				friend.setRealName(dbsrc.getString("realname"));
				friend.setPhoto(dbsrc.getString("photo"));
				friend.setSchool(dbsrc.getString("school"));
				friend.setGrade(dbsrc.getString("grade"));
				friend.setBlogId(dbsrc.getInt("blogid"));
				friend.setBlogTitle(dbsrc.getString("blogtitle"));
			  
				page.addData(friend);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
}
