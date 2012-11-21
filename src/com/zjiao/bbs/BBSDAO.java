package com.zjiao.bbs;

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;

/**
 * 讨论区信息数据操作接口，任何与讨论有关的数据操作都通过此类完成。
 * 用于封装对BBSInfo类的访问，
 * 也就是BBSInfo_XXX表的访问
 * @author Lee Bin
 */
public class BBSDAO {

	/**
	 * 默认的构造函数
	 */
  public BBSDAO() {
	}
	
	/**
	 * 获取某班级的讨论信息列表
	 * @param cid 班级ID
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @param status 讨论的状态
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageDiscuss(int cid, int curPage, int pageSize, String status) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		BBSInfo bbs=null;
		try {
		  /** 从数据库中检索班级讨论信息 */
		  String sql = "select id,title,istop,astatus,authorname,createtime,visitcount,replycount from BBSInfo_"+cid/40+" where classid=? and parentid=0";
		  if(!SysUtils.STATUS_ALL.equals(status)){
				sql=sql+" and astatus=?";
		  }
		  sql=sql+" order by istop desc, id desc";
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, cid);
		  if(!SysUtils.STATUS_ALL.equals(status)){
				dbsrc.setString(2, status);
		  }
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				bbs=new BBSInfo();
				bbs.setId(dbsrc.getInt("id"));
				bbs.setTitle(dbsrc.getString("title"));
				bbs.setIsTop(dbsrc.getInt("istop"));
				bbs.setStatus(dbsrc.getString("astatus"));
				bbs.setAuthorName(dbsrc.getString("authorname"));
				bbs.setCreateTime(dbsrc.getTimestamp("createtime"));
				bbs.setVisitCount(dbsrc.getInt("visitcount"));
				bbs.setReplyCount(dbsrc.getInt("replycount"));
			  
				page.addData(bbs);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * 获取某班级的有效讨论信息列表
	 * @param cid 班级ID
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageDiscuss(int cid, int curPage, int pageSize) throws SQLException {
		return getPageDiscuss(cid, curPage, pageSize, SysUtils.STATUS_NORMAL);
	}
	
	/**
	 * 获取某起讨论
	 * @param id 主讨论ID
	 * @param cid 班级ID
	 * @return 讨论列表
	 * @throws SQLException 数据库异常
	 */
	public static List getDiscuss(int id, int cid) throws SQLException {
		if(id==0){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		ArrayList list=new ArrayList();
		BBSInfo bbs=null;
		try {
		  /** 从数据库中检索班级讨论信息 */
		  String sql = "select id,classid,title,content,istop,astatus,authorname,createtime,visitcount,replycount from BBSInfo_"+cid/40+" where (id=? or parentid=?) and classid=? order by id";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.setInt(2, id);
		  dbsrc.setInt(3, cid);
		  dbsrc.executeQuery();
		  
		  while(dbsrc.next()){
				bbs=new BBSInfo();
				bbs.setId(dbsrc.getInt("id"));
				bbs.setClassId(dbsrc.getInt("classid"));
				bbs.setTitle(dbsrc.getString("title"));
				bbs.setContent(dbsrc.getString("content"));
				bbs.setIsTop(dbsrc.getInt("istop"));
				bbs.setStatus(dbsrc.getString("astatus"));
				bbs.setAuthorName(dbsrc.getString("authorname"));
				bbs.setCreateTime(dbsrc.getTimestamp("createtime"));
				bbs.setVisitCount(dbsrc.getInt("visitcount"));
				bbs.setReplyCount(dbsrc.getInt("replycount"));
			  
				list.add(bbs);
			}
			
			//浏览次数加1
			if(list.size()>0){
				/** 浏览次数加1 */
				sql = "update BBSInfo_"+cid/40+" set visitcount=visitcount+1 where id=? and classid=?";
				dbsrc.prepareStatement(sql);
				dbsrc.setInt(1, id);
				dbsrc.setInt(2, cid);
				dbsrc.executeUpdate();
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return list;
	}
	
	/**
	 * 参加讨论
	 * @param bbs 讨论信息对象
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int insertDiscuss(BBSInfo bbs) throws SQLException {
		if(bbs==null)
			return -1;
		
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** 在讨论信息表中插入新记录 */
		  String sql = "insert into BBSInfo_"+bbs.getClassId()/40+" (classid,parentid,title,content,astatus,authorid,authortype,authorname,createtime,visitcount) values(?,?,?,?,?,?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, bbs.getClassId());
		  dbsrc.setInt(2, bbs.getParentId());
		  dbsrc.setString(3, bbs.getTitle());
		  dbsrc.setString(4, bbs.getContent());
		  dbsrc.setString(5, SysUtils.STATUS_NORMAL);
		  dbsrc.setInt(6, bbs.getAuthorId());
		  dbsrc.setString(7, bbs.getAuthorType());
		  dbsrc.setString(8, bbs.getAuthorName());
		  dbsrc.setTimestamp(9, now);
		  dbsrc.setInt(10, 0);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	/**
	 * 为主文章的回复数加1
	 * @param id 主文章ID
	 * @param cid 班级ID
	 * @return 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int increaseReplyCount(int id, int cid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** 在讨论信息表中插入新记录 */
		  String sql = "update BBSInfo_"+cid/40+" set replycount=replycount+1 where id=? and classid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.setInt(2, cid);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	/**
	 * 设置文章的置顶属性值，值越大，排名越靠前
	 * @param id 文章ID
	 * @param va 要设置的值
	 * @param cid 班级ID
	 * @return 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int setTopValue(int id, int va, int cid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** 在讨论信息表中插入新记录 */
		  String sql = "update BBSInfo_"+cid/40+" set istop=? where id=? and classid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, va);
		  dbsrc.setInt(2, id);
		  dbsrc.setInt(3, cid);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
		  if (dbsrc != null)
				dbsrc.close();
		}
	}
	
	/**
	 * 删除讨论
	 * @param dels 要删除的文章ID列表
	 * @param cid 班级ID
	 * @param cas 是否删除跟贴
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int deleteDiscuss(List dels, int cid, boolean cas) throws SQLException {
		if((dels==null)||(dels.size()<=0))
			return 0;
		
		StringBuffer stulist=new StringBuffer();
		for(int i=0;i<dels.size();i++){
			stulist.append(dels.get(i).toString());
			if(i!=dels.size()-1){
				stulist.append(',');
			}
		}

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除班级讨论区中指定的文章列表 */
		  StringBuffer sql=new  StringBuffer();
		  sql.append("delete from BBSInfo_");
		  sql.append(cid/40);
		  sql.append(" where (id in (");
		  sql.append(stulist);
		  if(cas){
		  	sql.append(") or parentid in (");
		  	sql.append(stulist);
		  	sql.append(")) and classid=?");
		  }else{
				sql.append(")) and classid=?");
		  }
		  
		  dbsrc.prepareStatement(sql.toString());
		  dbsrc.setInt(1, cid);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
}