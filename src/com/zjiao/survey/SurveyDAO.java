package com.zjiao.survey;

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;
import com.zjiao.key.KeyException;
import com.zjiao.key.SimpleKeyGen;

import com.zjiao.auth.Role;

/**
 * �ʾ������Ϣ���ݲ����ӿڣ��κ�������йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��SurveyInfo��ķ��ʣ�
 * Ҳ����SurveyQuesInfo_XXX���SurveyResult_XXX��ķ���
 * @author Lee Bin
 */
public class SurveyDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public SurveyDAO() {
	}
	
	/**
	 * ��ȡĳ�û���ӵ����ڽ��е���������б�
	 * @param uid �û�ID
	 * @param rl ��ȡ���ݵĽ�ɫ
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @param status ���۵�״̬
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageSurveyQues(int uid, Role rl, int curPage, int pageSize, String status) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		if(rl==null){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		SurveyInfo survey=null;
		try {
		  /** �����ݿ��м����û���ӵ�������Ϣ */
		  String sql = "select id,title,type,createtime from SurveyQuesInfo_"+rl.getSubject()+" where author=?";
		  if(!SysUtils.STATUS_ALL.equals(status)){
				sql=sql+" and qstatus=?";
		  }
		  sql=sql+" order by id desc";
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  dbsrc.setInt(1, uid);
		  if(!SysUtils.STATUS_ALL.equals(status)){
				dbsrc.setString(2, status);
		  }
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCreateTime(dbsrc.getTimestamp("createtime"));
			  
				page.addData(survey);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * ��ȡĳ�û���ӵ����ڽ��е������Ч�����б�
	 * @param uid �û�ID
	 * @param rl ��ȡ���ݵĽ�ɫ
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageSurveyQues(int uid, Role rl, int curPage, int pageSize) throws SQLException {
		return getPageSurveyQues(uid, rl, curPage, pageSize, SysUtils.STATUS_NORMAL);
	}
	
	/**
	 * ��ȡ���ڽ��е����ȫ�������б�
	 * @param rl ��ȡ���ݵĽ�ɫ
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @param status ���۵�״̬
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageSurveyQues(Role rl, int curPage, int pageSize, String status) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		if(rl==null){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		SurveyInfo survey=null;
		try {
		  /** �����ݿ��м������ڵ����ȫ��������Ϣ */
		  String sql = "select id,title,type,createtime from SurveyQuesInfo_"+rl.getSubject();
		  if(!SysUtils.STATUS_ALL.equals(status)){
				sql=sql+" where qstatus=?";
		  }
		  sql=sql+" order by id desc";
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  if(!SysUtils.STATUS_ALL.equals(status)){
				dbsrc.setString(1, status);
		  }
		  dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCreateTime(dbsrc.getTimestamp("createtime"));
			  
				page.addData(survey);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * ��ȡĳ�û���ӵ����ڽ��е����ȫ����Ч�����б�
	 * @param rl ��ȡ���ݵĽ�ɫ
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageSurveyQues(Role rl, int curPage, int pageSize) throws SQLException {
		return getPageSurveyQues(rl, curPage, pageSize, SysUtils.STATUS_NORMAL);
	}
	
	/**
	 * ���������
	 * @param survey ������Ϣ����
	 * @param rl ��ǰ��ɫ
	 * @return int ִ�н��
	 * @throws SQLException ���ݿ��쳣
	 * @throws KeyException ��ֵ�����쳣
	 */
	public static int newQues(SurveyInfo survey, Role rl) throws SQLException,KeyException {
		if((survey==null)||(rl==null))
			return -1;
			
		/** ��ȡ���������IDֵ */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance("surveyquesid_"+rl.getSubject());
		int qid=keygen.getNextKey();
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** �ڵ���������Ϣ���в���һ����¼ */
		  String sql = "insert into SurveyQuesInfo_"+rl.getSubject()+" (id,title,type,cnt,qstatus,content,a,b,c,d,e,f,g,h,author,createtime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, qid);
		  dbsrc.setString(2, survey.getTitle());
		  dbsrc.setString(3, survey.getType());
		  dbsrc.setInt(4, survey.getCnt());
		  dbsrc.setString(5, survey.getStatus());
		  dbsrc.setString(6, survey.getContent());
		  dbsrc.setString(7, survey.getA());
		  dbsrc.setString(8, survey.getB());
		  dbsrc.setString(9, survey.getC());
		  dbsrc.setString(10, survey.getD());
		  dbsrc.setString(11, survey.getE());
		  dbsrc.setString(12, survey.getF());
		  dbsrc.setString(13, survey.getG());
		  dbsrc.setString(14, survey.getH());
		  dbsrc.setInt(15, survey.getAuthor());
		  dbsrc.setTimestamp(16, now);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ���õ��������״̬
	 * @param survey ������Ϣ����
	 * @param rl ��ǰ��ɫ
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int setQuesStatus(SurveyInfo survey, Role rl) throws SQLException {
		if((survey==null)||(rl==null))
			return -1;

		DBSource dbsrc = new DBSource();
		StringBuffer sql=new StringBuffer();
		sql.append("update SurveyQuesInfo_");
		sql.append(rl.getSubject());
		sql.append(" set qstatus=? where id=? and author=?");
		try {
		  /** �޸ĵ���������Ϣ���м�¼ */
		  dbsrc.prepareStatement(sql.toString());
		  dbsrc.setString(1, survey.getStatus());
		  dbsrc.setInt(2, survey.getId());
		  dbsrc.setInt(3, survey.getAuthor());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * �޸�����
	 * @param survey ������Ϣ����
	 * @param rl �û��Ľ�ɫ��Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int modQues(SurveyInfo survey, Role rl) throws SQLException {
		if((survey==null)||(rl==null))
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �޸Ĺ��������Ϣ���м�¼ */
			String sql = "update SurveyQuesInfo_" + rl.getSubject() + " set title=?,type=?,cnt=?,content=?,a=?,b=?,c=?,d=?,e=?,f=?,g=?,h=? where id=? and author=? and qstatus=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1, survey.getTitle());
			dbsrc.setString(2, survey.getType());
			dbsrc.setInt(3, survey.getCnt());
			dbsrc.setString(4, survey.getContent());
			dbsrc.setString(5, survey.getA());
			dbsrc.setString(6, survey.getB());
			dbsrc.setString(7, survey.getC());
			dbsrc.setString(8, survey.getD());
			dbsrc.setString(9, survey.getE());
			dbsrc.setString(10, survey.getF());
			dbsrc.setString(11, survey.getG());
			dbsrc.setString(12, survey.getH());
			dbsrc.setInt(13, survey.getId());
			dbsrc.setInt(14, survey.getAuthor());
			dbsrc.setString(15, SysUtils.STATUS_NORMAL);
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡ�����������
	 * @param id ������
	 * @param rl ��ǰ�û���ɫ
	 * @return ������Ϣ����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static SurveyInfo getSurveyInfo(int id, Role rl) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���������Ϣ */
		  String sql = "select id,title,type,cnt,qstatus,content,a,b,c,d,e,f,g,h,author,createtime from SurveyQuesInfo_"+rl.getSubject()+" where id=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.executeQuery();
		  
		  if(dbsrc.next()){
				SurveyInfo survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCnt(dbsrc.getInt("cnt"));
				survey.setStatus(dbsrc.getString("qstatus"));
				survey.setContent(dbsrc.getString("content"));
				survey.setA(dbsrc.getString("a"));
				survey.setB(dbsrc.getString("b"));
				survey.setC(dbsrc.getString("c"));
				survey.setD(dbsrc.getString("d"));
				survey.setE(dbsrc.getString("e"));
				survey.setF(dbsrc.getString("f"));
				survey.setG(dbsrc.getString("g"));
				survey.setH(dbsrc.getString("h"));
				survey.setAuthor(dbsrc.getInt("author"));
				survey.setCreateTime(dbsrc.getTimestamp("createtime"));
			  	
				return survey;
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ��ȡĳ�༶���ڽ��еĵ����б�
	 * @param cid �༶���
	 * @param rl ��ǰ�û���ɫ
	 * @return �����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getCurrrentSurvey(int cid, Role rl) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList surveys=new ArrayList();
		try {
		  /** �����ݿ��м������ڽ��еĵ����б� */
		  String sql = "select a.id,a.quesid,a.classid,a.isshowresult,a.total,a.begintime,b.title,b.type,b.cnt,b.content,b.a,b.b,b.c,b.d,b.e,b.f,b.g,b.h from SurveyResult_"+rl.getSubject()+" a,SurveyQuesInfo_"+rl.getSubject()+" b where a.classid=? and a.isgoing=1 and a.quesid=b.id order by a.id desc";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, cid);
		  dbsrc.executeQuery();
		  
		  SurveyInfo survey;
		  while(dbsrc.next()){
				survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setQuesId(dbsrc.getInt("quesid"));
				survey.setClassId(dbsrc.getInt("classid"));
				survey.setIsShowResult(dbsrc.getInt("isshowresult"));
				survey.setTotal(dbsrc.getInt("total"));
				survey.setBeginTime(dbsrc.getTimestamp("begintime"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCnt(dbsrc.getInt("cnt"));
				survey.setContent(dbsrc.getString("content"));
				survey.setA(dbsrc.getString("a"));
				survey.setB(dbsrc.getString("b"));
				survey.setC(dbsrc.getString("c"));
				survey.setD(dbsrc.getString("d"));
				survey.setE(dbsrc.getString("e"));
				survey.setF(dbsrc.getString("f"));
				survey.setG(dbsrc.getString("g"));
				survey.setH(dbsrc.getString("h"));
			  	
				surveys.add(survey);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return surveys;
	}
	
	/**
	 * ��ȡ���ڽ��еĵ����б�
	 * @param ids �������б�
	 * @param rl ��ǰ�û���ɫ
	 * @return �����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Hashtable getCurrrentSurvey(int[] ids, Role rl) throws SQLException {
		if((ids==null)||(ids.length==0)||(rl==null)){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		Hashtable surveys=new Hashtable();
		try {
			/** �����ݿ��м������ڽ��еĵ����б� */
			StringBuffer sql=new StringBuffer();
			sql.append("select a.id,a.quesid,a.classid,a.isshowresult,a.total,a.a va,a.b vb,a.c vc,a.d vd,a.e.ve,a.f vf,a.g vg,a.h vh,a.votedetail,a.begintime,b.title,b.type,b.cnt,b.content,b.a,b.b,b.c,b.d,b.e,b.f,b.g,b.h from SurveyResult_");
			sql.append(rl.getSubject());
			sql.append(" a,SurveyQuesInfo_");
			sql.append(rl.getSubject());
			sql.append(" b where a.quesid=b.id and a.id in(");
			for(int i=0;i<ids.length;i++){
				if(i>0){
					sql.append(',');
				}
				sql.append(ids[i]);
			}
			sql.append(')');
		  dbsrc.prepareStatement(sql.toString());
		  dbsrc.executeQuery();
		  
		  SurveyInfo survey;
		  while(dbsrc.next()){
				survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setQuesId(dbsrc.getInt("quesid"));
				survey.setClassId(dbsrc.getInt("classid"));
				survey.setIsShowResult(dbsrc.getInt("isshowresult"));
				survey.setTotal(dbsrc.getInt("total"));
				survey.setBeginTime(dbsrc.getTimestamp("begintime"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCnt(dbsrc.getInt("cnt"));
				survey.setContent(dbsrc.getString("content"));
				survey.setA(dbsrc.getString("a"));
				survey.setB(dbsrc.getString("b"));
				survey.setC(dbsrc.getString("c"));
				survey.setD(dbsrc.getString("d"));
				survey.setE(dbsrc.getString("e"));
				survey.setF(dbsrc.getString("f"));
				survey.setG(dbsrc.getString("g"));
				survey.setH(dbsrc.getString("h"));
				survey.setVa(dbsrc.getInt("va"));
				survey.setVb(dbsrc.getInt("vb"));
				survey.setVc(dbsrc.getInt("vc"));
				survey.setVd(dbsrc.getInt("vd"));
				survey.setVe(dbsrc.getInt("ve"));
				survey.setVf(dbsrc.getInt("vf"));
				survey.setVg(dbsrc.getInt("vg"));
				survey.setVh(dbsrc.getInt("vh"));
				survey.setVoteDetail(dbsrc.getString("votedetail"));
			  	
				surveys.put(new Integer(survey.getId()), survey);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return surveys;
	}
	
	/**
	 * ��ȡ���ڽ��еĵ����б�
	 * @param rl ��ǰ�û���ɫ
	 * @return �����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Hashtable getCurrrentSurvey(Role rl) throws SQLException {
		if(rl==null){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		Hashtable surveys=new Hashtable();
		try {
			/** �����ݿ��м������ڽ��еĵ����б� */
			StringBuffer sql=new StringBuffer();
			sql.append("select a.id,a.quesid,a.classid,a.isshowresult,a.total,a.a va,a.b vb,a.c vc,a.d vd,a.e ve,a.f vf,a.g vg,a.h vh,a.votedetail,a.begintime,b.title,b.type,b.cnt,b.content,b.a,b.b,b.c,b.d,b.e,b.f,b.g,b.h from SurveyResult_");
			sql.append(rl.getSubject());
			sql.append(" a,SurveyQuesInfo_");
			sql.append(rl.getSubject());
			sql.append(" b where a.classid=? and a.isgoing=1 and a.quesid=b.id");
		  dbsrc.prepareStatement(sql.toString());
		  dbsrc.setInt(1, rl.getInt("classid"));
		  dbsrc.executeQuery();
		  
		  SurveyInfo survey;
		  while(dbsrc.next()){
				survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setQuesId(dbsrc.getInt("quesid"));
				survey.setClassId(dbsrc.getInt("classid"));
				survey.setIsShowResult(dbsrc.getInt("isshowresult"));
				survey.setTotal(dbsrc.getInt("total"));
				survey.setBeginTime(dbsrc.getTimestamp("begintime"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCnt(dbsrc.getInt("cnt"));
				survey.setContent(dbsrc.getString("content"));
				survey.setA(dbsrc.getString("a"));
				survey.setB(dbsrc.getString("b"));
				survey.setC(dbsrc.getString("c"));
				survey.setD(dbsrc.getString("d"));
				survey.setE(dbsrc.getString("e"));
				survey.setF(dbsrc.getString("f"));
				survey.setG(dbsrc.getString("g"));
				survey.setH(dbsrc.getString("h"));
				survey.setVa(dbsrc.getInt("va"));
				survey.setVb(dbsrc.getInt("vb"));
				survey.setVc(dbsrc.getInt("vc"));
				survey.setVd(dbsrc.getInt("vd"));
				survey.setVe(dbsrc.getInt("ve"));
				survey.setVf(dbsrc.getInt("vf"));
				survey.setVg(dbsrc.getInt("vg"));
				survey.setVh(dbsrc.getInt("vh"));
				survey.setVoteDetail(dbsrc.getString("votedetail"));
			  	
				surveys.put(new Integer(survey.getId()), survey);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return surveys;
	}
	
	/**
	 * ��ȡĳ�༶�ĵ�����ʷ��¼
	 * @param cid �༶ID
	 * @param rl ��ȡ���ݵĽ�ɫ
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @param status ���۵�״̬
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageHistory(int cid, Role rl, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.MPAGE_SIZE;
		}
		if(rl==null){
			return null;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		SurveyInfo survey=null;
		try {
			/** �����ݿ��м���������ʷ��¼ */
			String sql = "select a.id,a.total,a.begintime,a.endtime,b.title,b.type from SurveyResult_"+rl.getSubject()+" a,SurveyQuesInfo_"+rl.getSubject()+" b where a.classid=? and a.isgoing=0 and a.quesid=b.id order by a.id desc";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, cid);
			dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setTotal(dbsrc.getInt("total"));
				survey.setBeginTime(dbsrc.getTimestamp("begintime"));
				survey.setEndTime(dbsrc.getTimestamp("endtime"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
			  
				page.addData(survey);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * ��ʼ�µ���
	 * @param survey ������Ϣ����
	 * @param rl ��ǰ��ɫ
	 * @return int ִ�н��
	 * @throws SQLException ���ݿ��쳣
	 * @throws KeyException ��ֵ�����쳣
	 */
	public static int beginSurvey(SurveyInfo survey, Role rl) throws SQLException,KeyException {
		if((survey==null)||(rl==null))
			return -1;
			
		/** ��ȡ�µ����IDֵ */
		SimpleKeyGen keygen = SimpleKeyGen.getInstance("surveyresultid_"+rl.getSubject());
		int rid=keygen.getNextKey();
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
		  /** �ڵ�������Ϣ���в���һ����¼ */
		  String sql = "insert into SurveyResult_"+rl.getSubject()+" (id,quesid,classid,isgoing,isshowresult,total,a,b,c,d,e,f,g,h,begintime) values(?,?,?,1,0,0,0,0,0,0,0,0,0,0,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, rid);
		  dbsrc.setInt(2, survey.getQuesId());
		  dbsrc.setInt(3, survey.getClassId());
		  dbsrc.setTimestamp(4, now);
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��������
	 * @param survey ������Ϣ����
	 * @param rl �û��Ľ�ɫ��Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int closeSurvey(SurveyInfo survey, Role rl) throws SQLException {
		if((survey==null)||(rl==null))
			return -1;
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());

		DBSource dbsrc = new DBSource();
		try {
			/** �޸Ĺ��������Ϣ���м�¼ */
			String sql = "update SurveyResult_" + rl.getSubject() + " set isgoing=0,endtime=? where id=? and classid=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setTimestamp(1, now);
			dbsrc.setInt(2, survey.getId());
			dbsrc.setInt(3, survey.getClassId());
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ���õ���Ĺ��������״̬��1�����������0�����������
	 * @param survey ������Ϣ����
	 * @param rl �û��Ľ�ɫ��Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int setSurvey(SurveyInfo survey, Role rl) throws SQLException {
		if((survey==null)||(rl==null))
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �޸ĵ�������Ϣ���м�¼ */
			String sql = "update SurveyResult_" + rl.getSubject() + " set isshowresult=? where id=? and classid=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, survey.getIsShowResult());
			dbsrc.setInt(2, survey.getId());
			dbsrc.setInt(3, survey.getClassId());
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡĳ�ε���Ľ��
	 * @param id ������
	 * @param rl ��ǰ�û���ɫ
	 * @return �����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static SurveyInfo getSurveyResult(int id, Role rl) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м������ڽ��еĵ����б� */
		  String sql = "select a.id,a.quesid,a.classid,a.isshowresult,a.total,a.a va,a.b vb,a.c vc,a.d vd,a.e ve,a.f vf,a.g vg,a.h vh,a.begintime,b.title,b.type,b.cnt,b.content,b.a,b.b,b.c,b.d,b.e,b.f,b.g,b.h from SurveyResult_"+rl.getSubject()+" a,SurveyQuesInfo_"+rl.getSubject()+" b where a.id=? and a.quesid=b.id";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, id);
		  dbsrc.executeQuery();
		  
		  if(dbsrc.next()){
				SurveyInfo survey=new SurveyInfo();
				survey.setId(dbsrc.getInt("id"));
				survey.setQuesId(dbsrc.getInt("quesid"));
				survey.setClassId(dbsrc.getInt("classid"));
				survey.setIsShowResult(dbsrc.getInt("isshowresult"));
				survey.setTotal(dbsrc.getInt("total"));
				survey.setVa(dbsrc.getInt("va"));
				survey.setVb(dbsrc.getInt("vb"));
				survey.setVc(dbsrc.getInt("vc"));
				survey.setVd(dbsrc.getInt("vd"));
				survey.setVe(dbsrc.getInt("ve"));
				survey.setVf(dbsrc.getInt("vf"));
				survey.setVg(dbsrc.getInt("vg"));
				survey.setVh(dbsrc.getInt("vh"));
				survey.setBeginTime(dbsrc.getTimestamp("begintime"));
				survey.setTitle(dbsrc.getString("title"));
				survey.setType(dbsrc.getString("type"));
				survey.setCnt(dbsrc.getInt("cnt"));
				survey.setContent(dbsrc.getString("content"));
				survey.setA(dbsrc.getString("a"));
				survey.setB(dbsrc.getString("b"));
				survey.setC(dbsrc.getString("c"));
				survey.setD(dbsrc.getString("d"));
				survey.setE(dbsrc.getString("e"));
				survey.setF(dbsrc.getString("f"));
				survey.setG(dbsrc.getString("g"));
				survey.setH(dbsrc.getString("h"));
			  
				return survey;
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ����������
	 * @param survey ������Ϣ����
	 * @param rl �û��Ľ�ɫ��Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int updateSurvey(SurveyInfo survey, Role rl) throws SQLException {
		if((survey==null)||(rl==null))
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �޸ĵ�������Ϣ���м�¼ */
			String sql = "update SurveyResult_" + rl.getSubject() + " set total=?,a=?,b=?,c=?,d=?,e=?,f=?,g=?,h=?,votedetail=? where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, survey.getTotal());
			dbsrc.setInt(2, survey.getVa());
			dbsrc.setInt(3, survey.getVb());
			dbsrc.setInt(4, survey.getVc());
			dbsrc.setInt(5, survey.getVd());
			dbsrc.setInt(6, survey.getVe());
			dbsrc.setInt(7, survey.getVf());
			dbsrc.setInt(8, survey.getVg());
			dbsrc.setInt(9, survey.getVh());
			dbsrc.setString(10, survey.getVoteDetail());
			dbsrc.setInt(11, survey.getId());
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
}