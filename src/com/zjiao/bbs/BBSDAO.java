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
 * ��������Ϣ���ݲ����ӿڣ��κ��������йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��BBSInfo��ķ��ʣ�
 * Ҳ����BBSInfo_XXX��ķ���
 * @author Lee Bin
 */
public class BBSDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public BBSDAO() {
	}
	
	/**
	 * ��ȡĳ�༶��������Ϣ�б�
	 * @param cid �༶ID
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @param status ���۵�״̬
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageDiscuss(int cid, int curPage, int pageSize, String status) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		BBSInfo bbs=null;
		try {
		  /** �����ݿ��м����༶������Ϣ */
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
	 * ��ȡĳ�༶����Ч������Ϣ�б�
	 * @param cid �༶ID
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageDiscuss(int cid, int curPage, int pageSize) throws SQLException {
		return getPageDiscuss(cid, curPage, pageSize, SysUtils.STATUS_NORMAL);
	}
	
	/**
	 * ��ȡĳ������
	 * @param id ������ID
	 * @param cid �༶ID
	 * @return �����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getDiscuss(int id, int cid) throws SQLException {
		if(id==0){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		ArrayList list=new ArrayList();
		BBSInfo bbs=null;
		try {
		  /** �����ݿ��м����༶������Ϣ */
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
			
			//���������1
			if(list.size()>0){
				/** ���������1 */
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
	 * �μ�����
	 * @param bbs ������Ϣ����
	 * @return	int ����״̬
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int insertDiscuss(BBSInfo bbs) throws SQLException {
		if(bbs==null)
			return -1;
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** ��������Ϣ���в����¼�¼ */
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
	 * Ϊ�����µĻظ�����1
	 * @param id ������ID
	 * @param cid �༶ID
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int increaseReplyCount(int id, int cid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** ��������Ϣ���в����¼�¼ */
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
	 * �������µ��ö�����ֵ��ֵԽ������Խ��ǰ
	 * @param id ����ID
	 * @param va Ҫ���õ�ֵ
	 * @param cid �༶ID
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int setTopValue(int id, int va, int cid) throws SQLException {

		DBSource dbsrc = new DBSource();
		try {
		  /** ��������Ϣ���в����¼�¼ */
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
	 * ɾ������
	 * @param dels Ҫɾ��������ID�б�
	 * @param cid �༶ID
	 * @param cas �Ƿ�ɾ������
	 * @return	int ����״̬
	 * @throws SQLException ���ݿ��쳣
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
		  /** ɾ���༶��������ָ���������б� */
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