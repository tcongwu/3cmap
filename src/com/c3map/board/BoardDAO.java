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
 * ���԰���Ϣ���ݲ����ӿڣ��κ������԰��йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��BoardInfo��ķ��ʣ�
 * Ҳ����board��ķ���
 * @author Lee Bin
 */
public class BoardDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public BoardDAO() {
	}
	
	/**
	 * ��������
	 * @param board ������Ϣ����
	 * @return	int ����״̬��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int leaveWord(BoardInfo board) throws SQLException {
		
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** �ں���������в����¼ */
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
	 * ɾ������
	 * @param board ������Ϣ����
	 * @return	int ����״̬��1���ɹ�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int deleteNote(BoardInfo board) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** ɾ��������Ϣ���м�¼ */
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
	 * ��ȡ�û����Եķ�ҳ�б�
	 * @param uid �û�ID
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageBoards(int uid, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		BoardInfo bd=null;
		try {
		  /** �����ݿ��м���������Ϣ */
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
	 * ��ȡ�û������ָ����Ŀ����
	 * @param uid �û�ID
	 * @param cnt ��ȡ��������
	 * @return �����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getRecentBoards(int uid, int cnt) throws SQLException {
		if (cnt<=0){
			cnt=SysUtils.MPAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		ArrayList boards=new ArrayList();
		BoardInfo bd=null;
		try {
		  /** �����ݿ��м���������Ϣ */
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