package com.c3map.board;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;

/**
 * 留言板信息数据操作接口，任何与留言板有关的数据操作都通过此类完成。
 * 用于封装对BoardInfo类的访问，
 * 也就是board表的访问
 * @author Lee Bin
 */
public class BoardDAO {

	/**
	 * 默认的构造函数
	 */
  public BoardDAO() {
	}
	
	/**
	 * 给人留言
	 * @param board 留言信息对象
	 * @return	int 操作状态，1－成功
	 * @throws SQLException 数据库异常
	 */
	public static int leaveWord(BoardInfo board) throws SQLException {
		
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** 在好友申请表中插入记录 */
		  String sql="insert into board (userid,leaveid,realname,photo,school,content,leavetime) values(?,?,?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, board.getUserId());
		  dbsrc.setInt(2, board.getLeaveId());
		  dbsrc.setString(3, board.getRealName());
		  dbsrc.setString(4, board.getPhoto());
		  dbsrc.setString(5, board.getSchool());
		  dbsrc.setString(6, board.getContent());
		  dbsrc.setTimestamp(7, now);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 删除留言
	 * @param board 留言信息对象
	 * @return	int 操作状态，1－成功
	 * @throws SQLException 数据库异常
	 */
	public static int deleteNote(BoardInfo board) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** 删除留言信息表中记录 */
		  String sql="delete from board where id=? and userid=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, board.getId());
		  dbsrc.setInt(2, board.getUserId());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 获取用户留言的分页列表
	 * @param uid 用户ID
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageBoards(int uid, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		BoardInfo bd=null;
		try {
		  /** 从数据库中检索留言信息 */
		  String sql = "select id,leaveid,realname,photo,school,content,leavetime from board where userid=? order by id desc";
		  dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, uid);
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				bd=new BoardInfo();
				bd.setId(dbsrc.getInt("id"));
				bd.setLeaveId(dbsrc.getInt("leaveid"));
				bd.setRealName(dbsrc.getString("realname"));
				bd.setPhoto(dbsrc.getString("photo"));
				bd.setSchool(dbsrc.getString("school"));
				bd.setContent(dbsrc.getString("content"));
				bd.setLeaveTime(dbsrc.getTimestamp("leavetime"));
			  
				page.addData(bd);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * 获取用户最近的指定数目留言
	 * @param uid 用户ID
	 * @param cnt 获取的留言数
	 * @return 留言列表
	 * @throws SQLException 数据库异常
	 */
	public static List getRecentBoards(int uid, int cnt) throws SQLException {
		if (cnt<=0){
			cnt=SysUtils.MPAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		ArrayList boards=new ArrayList();
		BoardInfo bd=null;
		try {
		  /** 从数据库中检索留言信息 */
		  String sql = "select id,leaveid,realname,photo,school,content,leavetime from board where userid=? order by id desc limit ?";
		  dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, uid);
		  dbsrc.setInt(2, cnt);
		  dbsrc.executeQuery();
		  
		  while(dbsrc.next()){
				bd=new BoardInfo();
				bd.setId(dbsrc.getInt("id"));
				bd.setLeaveId(dbsrc.getInt("leaveid"));
				bd.setRealName(dbsrc.getString("realname"));
				bd.setPhoto(dbsrc.getString("photo"));
				bd.setSchool(dbsrc.getString("school"));
				bd.setContent(dbsrc.getString("content"));
				bd.setLeaveTime(dbsrc.getTimestamp("leavetime"));
			  
				boards.add(bd);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return boards;
	}
}