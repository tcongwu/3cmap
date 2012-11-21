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
 * 好友信息数据操作接口，任何与好友有关的数据操作都通过此类完成。
 * 用于封装对FriendInfo类的访问，
 * 也就是users_friends表的访问
 * @author Lee Bin
 */
public class FriendDAO {

	/**
	 * 默认的构造函数
	 */
  public FriendDAO() {
	}
	
	/**
	 * 申请好友
	 * @param bid 被申请者的ID
	 * @param aid 申请者的ID
	 * @return	int 操作状态，1－成功
	 * @throws SQLException 数据库异常
	 */
	public static int applyFriend(int bid, int aid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** 在好友申请表中插入记录 */
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
	 * 拒绝好友
	 * @param bid 被申请者的ID
	 * @param aid 申请者的ID
	 * @return	int 操作状态，1－成功
	 * @throws SQLException 数据库异常
	 */
	public static int deleteApply(int bid, int aid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除好友申请表中记录 */
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
	 * 添加好友
	 * @param id1 好友ID1
	 * @param id2 好友ID2
	 * @return	int 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int addFriend(int id1, int id2) throws SQLException {
		
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		try {
		  /** 在用户好友信息表中插入新记录 */
		  sql = "insert into users_friends (userid,friendid,createtime) values(?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, id1);
		  dbtrac.setInt(2, id2);
		  dbtrac.setTimestamp(3, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** 插入新记录是否成功 */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }
		  
		  /** 在用户好友信息表中插入新记录 */
		  sql = "insert into users_friends (userid,friendid,createtime) values(?,?,?)";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, id2);
		  dbtrac.setInt(2, id1);
		  dbtrac.setTimestamp(3, now);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** 插入新记录是否成功 */
		  if (affectedRows < 1) {
				dbErrors = true;
				return -1;
		  }
		  
		  /** 删除好友申请表中记录记录 */
		  sql = "delete from friendapply where userid=? and friendid=?";
		  dbtrac.prepareStatement(sql);
		  dbtrac.setInt(1, id1);
		  dbtrac.setInt(2, id2);
		  
		  affectedRows = dbtrac.executeUpdate();
		  /** 删除记录是否成功 */
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
	 * 删除好友
	 * @param id1 好友1的ID
	 * @param id2 好友2的ID
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int deleteFriend(int id1, int id2) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除用户好友信息表中的好友记录 */
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
	 * 获取好友的分页列表
	 * @param uid 用户ID
	 * @param request 含有搜索条件的http请求对象
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
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
		  /** 从数据库中检索好友信息 */
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
	 * 判断是否存好友申请
	 * @param uid 用户ID
	 * @param fid 好友ID
	 * @return boolean 是否存在
	 */
	public static boolean isExistApply(int uid, int fid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索学校信息 */
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
	 * 判断是否存在该好友
	 * @param uid 用户ID
	 * @param fid 好友ID
	 * @return boolean 是否存在
	 */
	public static boolean isExistFriend(int uid, int fid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索学校信息 */
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
	 * 判断是否存为2度好友
	 * @param uid 用户ID
	 * @param fid 好友ID
	 * @return boolean 是否存在
	 */
	public static boolean isExist2Friend(int uid, int fid) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索好友信息 */
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
	 * 获取某用户各学校好友计数
	 * @param uid 用户ID
	 * @return 各学校好友数量列表
	 * @throws SQLException 数据库异常
	 */
	public static List getSchoolCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		RefineInfo sch=null;
		try {
		  /** 从数据库中检索出各学校好友数目统计信息 */
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
	 * 获取某用户好友计数
	 * @param uid 用户ID
	 * @return 好友数量
	 * @throws SQLException 数据库异常
	 */
	public static int getFriendCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索出好友数目统计信息 */
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
	 * 获取某用户好友申请计数
	 * @param uid 用户ID
	 * @return 好友申请数量
	 * @throws SQLException 数据库异常
	 */
	public static int getApplyCount(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索出好友数目统计信息 */
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
	 * 获取某用户的好友申请列表
	 * @param uid 用户ID
	 * @return 好友申请列表
	 * @throws SQLException 数据库异常
	 */
	public static List getApplyFriends(int uid) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList apps=new ArrayList();
		RefineInfo app=null;
		try {
		  /** 从数据库中检索省份列表 */
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