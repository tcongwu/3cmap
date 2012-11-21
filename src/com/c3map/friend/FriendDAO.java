package com.c3map.friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.auth.RefineInfo;
import com.zjiao.db.DBSource;
import com.zjiao.db.DBTransaction;
import com.zjiao.util.StringUtils;

/**
 * ������Ϣ���ݲ����ӿڣ��κ�������йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��FriendInfo��ķ��ʣ�
 * Ҳ����users_friends��ķ���
 * @author Lee Bin
 */
public class FriendDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public FriendDAO() {
	}
	
	/**
	 * �������
	 * @param bid �������ߵ�ID
	 * @param aid �����ߵ�ID
	 * @return	int ����״̬��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int applyFriend(int bid, int aid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** �ں���������в����¼ */
		  String sql="insert into friendapply (userid,friendid) values(?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, bid);
		  dbsrc.setInt(2, aid);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * �ܾ�����
	 * @param bid �������ߵ�ID
	 * @param aid �����ߵ�ID
	 * @return	int ����״̬��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int deleteApply(int bid, int aid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** ɾ������������м�¼ */
		  String sql="delete from friendapply where userid=? and friendid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, bid);
		  dbsrc.setInt(2, aid);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��Ӻ���
	 * @param id1 ����ID1
	 * @param id2 ����ID2
	 * @return	int �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int addFriend(int id1, int id2) throws SQLException {
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
		  /** ���û�������Ϣ���в����¼�¼ */
		  sql = "insert into users_friends (userid,friendid,createtime) values(?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, id1);
		  dbtrac.setInt(2, id2);
		  dbtrac.setTimestamp(3, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }
		  
		  /** ���û�������Ϣ���в����¼�¼ */
		  sql = "insert into users_friends (userid,friendid,createtime) values(?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, id2);
		  dbtrac.setInt(2, id1);
		  dbtrac.setTimestamp(3, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** �����¼�¼�Ƿ�ɹ� */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }
		  
		  /** ɾ������������м�¼��¼ */
		  sql = "delete from friendapply where userid=? and friendid=?";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, id1);
		  dbtrac.setInt(2, id2);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** ɾ����¼�Ƿ�ɹ� */
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
	 * ɾ������
	 * @param id1 ����1��ID
	 * @param id2 ����2��ID
	 * @return	int ����״̬
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int deleteFriend(int id1, int id2) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** ɾ���û�������Ϣ���еĺ��Ѽ�¼ */
		  String sql="delete from users_friends where (userid=? and friendid=?) or (userid=? and friendid=?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id1);
		  dbsrc.setInt(2, id2);
		  dbsrc.setInt(3, id2);
		  dbsrc.setInt(4, id1);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡ���ѵķ�ҳ�б�
	 * @param uid �û�ID
	 * @param request ��������������http�������
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageFriends(int uid, HttpServletRequest request, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		String school=null;
		String realname=null;
		if(request!=null){
			school=request.getParameter("school");
			realname=request.getParameter("realName");
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		RefineInfo friend=null;
		try {
		  /** �����ݿ��м���������Ϣ */
		  StringBuffer sql = new StringBuffer("select b.id,b.realname,b.photo,b.school,b.grade,b.blogid,b.blogtitle,a.createtime from users_friends a,users_refined b where a.friendid=b.id and a.userid=?");
			if(!StringUtils.isNull(school)){
			  sql.append("and b.school=?");
			}
			if(!StringUtils.isNull(realname)){
			  sql.append("and b.realname=?");
			}
			sql.append(" order by a.createtime desc");
			int i=2;
		  dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, uid);
		  if(!StringUtils.isNull(school)){
				dbsrc.setString(i, school);
				i++;
		  }
		  if(!StringUtils.isNull(realname)){
				dbsrc.setString(i, realname);
				i++;
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
	
	/**
	 * �ж��Ƿ���������
	 * @param uid �û�ID
	 * @param fid ����ID
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistApply(int uid, int fid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���ѧУ��Ϣ */
		  String sql = "select userid from friendapply where (userid=? and friendid=?) or (userid=? and friendid=?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, fid);
		  dbsrc.setInt(3, fid);
		  dbsrc.setInt(4, uid);
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
	 * �ж��Ƿ���ڸú���
	 * @param uid �û�ID
	 * @param fid ����ID
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistFriend(int uid, int fid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���ѧУ��Ϣ */
		  String sql = "select userid from users_friends where userid=? and friendid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, fid);
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
	 * �ж��Ƿ��Ϊ2�Ⱥ���
	 * @param uid �û�ID
	 * @param fid ����ID
	 * @return boolean �Ƿ����
	 */
	public static boolean isExist2Friend(int uid, int fid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���������Ϣ */
		  String sql = "select a.userid from users_friends a,users_friends b where a.userid=? and b.friendid=? and a.friendid=b.userid";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, fid);
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
	 * ��ȡĳ�û���ѧУ���Ѽ���
	 * @param uid �û�ID
	 * @return ��ѧУ���������б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getSchoolCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		RefineInfo sch=null;
		try {
		  /** �����ݿ��м�������ѧУ������Ŀͳ����Ϣ */
		  String sql = "select b.school,count(*) cnt from users_friends a,users_refined b where a.friendid=b.id and a.userid=? group by b.school";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  ArrayList schs=new ArrayList();
		  while(dbsrc.next()){
		  	sch=new RefineInfo();
		  	sch.setSchool(dbsrc.getString("school"));
		  	sch.setId(dbsrc.getInt("cnt"));
		  	
		  	schs.add(sch);
			}
			
			return schs;
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡĳ�û����Ѽ���
	 * @param uid �û�ID
	 * @return ��������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int getFriendCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м�����������Ŀͳ����Ϣ */
		  String sql = "select count(*) cnt from users_friends where userid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
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
	
	/**
	 * ��ȡĳ�û������������
	 * @param uid �û�ID
	 * @return ������������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int getApplyCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м�����������Ŀͳ����Ϣ */
		  String sql = "select count(*) cnt from friendapply where userid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
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
	
	/**
	 * ��ȡĳ�û��ĺ��������б�
	 * @param uid �û�ID
	 * @return ���������б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getApplyFriends(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList apps=new ArrayList();
		RefineInfo app=null;
		try {
		  /** �����ݿ��м���ʡ���б� */
		  String sql = "select b.id,b.realname,b.photo,b.school,b.grade,b.blogid,b.blogtitle from friendapply a,users_refined b where a.friendid=b.id and a.userid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  while(dbsrc.next()){
		  	app=new RefineInfo();
		  	app.setId(dbsrc.getInt("id"));
		  	app.setRealName(dbsrc.getString("realname"));
		  	app.setPhoto(dbsrc.getString("photo"));
		  	app.setSchool(dbsrc.getString("school"));
		  	app.setGrade(dbsrc.getString("grade"));
		  	app.setBlogId(dbsrc.getInt("blogid"));
		  	app.setBlogTitle(dbsrc.getString("blogtitle"));
		  	
		  	apps.add(app);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return apps;
	}
}