package com.c3map.activity;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.zjiao.Page;
import com.zjiao.SysUtils;
import com.c3map.activity.ActivityInfo;
import com.zjiao.db.DBSource;
import com.zjiao.db.DBTransaction;
import com.zjiao.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.zjiao.auth.*;
import com.zjiao.key.*;


/**
 * ���Ϣ���ݲ����ӿڣ��κ����йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��ActivityInfo��ķ��ʣ�
 * Ҳ����activity��ķ���
 * @author tcw
 */
public class ActivityDAO {

	/**
	* Ĭ�ϵĹ��캯��
	*/
	public ActivityDAO(){}
	 
	
	/**
	* ��ȡĳ���˵Ĳ���/��ע���Ϣ�б�
	* @param id  �ID
	* @param joiner_id  ������id
	* @param curPage ��ǰҳ��
	* @param pageSize	��ҳ��С
	* @param status      ����״̬ 1��ʾ���� 2��ʾ��ע
	* @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	* @throws SQLException ���ݿ��쳣
	*/
	public static Page getJoinActivity(int joiner_id, int curPage, int pageSize,int status) throws SQLException {
	 
		if (pageSize<=0){
			  pageSize=SysUtils.PAGE_SIZE;
		  }
		      DBSource dbsrc = new DBSource();
		      Page page=null;
		      ActivityInfo  act=null;
		      try {
		    	  /** �����ݿ���ĳ�˵Ļ��Ϣ�б� */

					String sql="select a.id as id,b.author_id as author_id,b.author_name as author_name,b.activity_name as actname,b.activity_host as acthost,b.activity_content as actcontent,b.activity_place as actplace,b.start_time as start_time,b.end_time as end_time,b.phone as phone,b.email as email,b.create_time as create_time from  joinactivity a,activity b where a.id=b.id and a.joiner_id=? and a.status=? order by b.create_time desc";
					dbsrc.prepareStatement(sql);
					dbsrc.setInt(1, joiner_id);
					dbsrc.setInt(2,status);
					dbsrc.executeQuery();
		  
					dbsrc.setPageSize(pageSize);
					dbsrc.setPageNo(curPage);
					page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
					while(dbsrc.pageNext()){
						act=new ActivityInfo();
						act.setId(dbsrc.getInt("id"));
						act.setAuthorId(dbsrc.getInt("author_id"));
						act.setAuthorname(dbsrc.getString("author_name"));
						act.setActname(dbsrc.getString("actname"));
						act.setActhost(dbsrc.getString("acthost"));
						act.setActcontent(dbsrc.getString("actcontent"));
						act.setActplace(dbsrc.getString("actplace"));
						act.setStart(dbsrc.getTimestamp("start_time"));
						act.setEnd(dbsrc.getTimestamp("end_time"));
						act.setPhone(dbsrc.getString("phone"));
						act.setEmail(dbsrc.getString("email"));
						act.setCreattime(dbsrc.getTimestamp("create_time"));
				 
						page.addData(act);
					}
		  
		      } finally {
		    	  if (dbsrc!=null)
		    		  dbsrc.close();
		      }
		
		      return page;
	}
	
	/**
	 * ��ȡĳ���˵Ĳ���/��ע���Ϣ�б����6��,�����3��,��ע��3��,��������
	 * @param id  �ID
	 * @param joiner_id  ������id
	 * @param status      ����״̬ 1��ʾ���� 2��ʾ��ע
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	*/
	public static List getJoinActivityRemind(int joiner_id,int status) throws SQLException {

		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		DBSource dbsrc = new DBSource();
		List list=new ArrayList();
		ActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ�˵Ĳ���/��ע���Ϣ�б� */

			String sql="select a.id as id,b.author_name as author_name,b.activity_name as actname,b.activity_host as acthost,b.activity_content as actcontent,b.activity_place as actplace,b.start_time as start_time,b.end_time as end_time,b.phone as phone,b.email as email,b.create_time as create_time from  joinactivity a,activity b where a.id=b.id and a.joiner_id=? and a.status=? and b.end_time>=? order by b.start_time desc limit 3";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, joiner_id);
			dbsrc.setInt(2,status);
			dbsrc.setTimestamp(3,now);
			dbsrc.executeQuery();
	  
			while(dbsrc.next()){
				act=new ActivityInfo();
				act.setId(dbsrc.getInt("id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("actname"));
				act.setActhost(dbsrc.getString("acthost"));
				act.setActcontent(dbsrc.getString("actcontent"));
				act.setActplace(dbsrc.getString("actplace"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
			 
				list.add(act);
			}
	  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
	
		return list;
	}




	/**
	 * ���ݲ�ѯ������ȡ���Ϣ�б�
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getActivityByCondition(HttpServletRequest request, int curPage, int pageSize) throws SQLException {
		if(request==null){
			return null;
		}
		
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		ActivityInfo  act=null;
	  
		String actname=request.getParameter("actname");
		String actplace=request.getParameter("actplace");
		String actcontent=request.getParameter("actcontent");
	  
		try {
			/** �����ݿ��л�ȡ���������Ļ��Ϣ */
			StringBuffer sql = new StringBuffer("select id,author_id,author_name,activity_name,activity_host,activity_content,start_time,end_time,phone,email,activity_place,create_time from activity where 1=1 ");
		
			if(!StringUtils.isNull(actname)){
				sql.append(" and activity_name like ?");
			}
			if(!StringUtils.isNull(actplace)){
				sql.append(" and activity_place like ?");
			}
			if(!StringUtils.isNull(actcontent)){
				sql.append(" and activity_content like ?");
			}
			sql.append(" order by create_time desc");
	  
			dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			int i=1;
			if(!StringUtils.isNull(actname)){
				dbsrc.setString(i, '%'+actname+'%');
				i++;
			}
			if(!StringUtils.isNull(actplace)){
				dbsrc.setString(i, '%'+actplace+'%');
				i++;
			}
		
			if(!StringUtils.isNull(actcontent)){
				dbsrc.setString(i, '%'+actcontent+'%');
				i++;
			}
		
			dbsrc.executeQuery();
	
			dbsrc.setPageSize(pageSize);
			dbsrc.setPageNo(curPage);
			page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
			
			while(dbsrc.pageNext()){
				act=new ActivityInfo();
				act.setId(dbsrc.getInt("id"));
				act.setAuthorId(dbsrc.getInt("author_id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("activity_name"));
				act.setActhost(dbsrc.getString("activity_host"));
				act.setActcontent(dbsrc.getString("activity_content"));
				act.setActplace(dbsrc.getString("activity_place"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
				page.addData(act);
			}
	  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		return page;
	}
	

	/**
	* ��ȡĳ���˵Ĳ���/��ע���Ϣ�б��һ��
	* @param joiner_id   ������id
	* @param status      ����״̬ 1��ʾ���� 2��ʾ��ע
	* @throws SQLException ���ݿ��쳣
	*/
	public static ActivityInfo getJoinActivityFirst(int joiner_id,int status) throws SQLException {
	 
		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		
		try {
			/** �����ݿ���ĳ�˵Ļ��Ϣ�б� */
			
			String sql="select a.id as id,b.author_id as author_id,b.author_name as author_name,b.activity_name as actname,b.activity_host as acthost,b.activity_content as actcontent,b.activity_place as actplace,b.start_time as start_time,b.end_time as end_time,b.phone as phone,b.email as email,b.create_time as create_time from  joinactivity a,activity b where a.id=b.id and a.joiner_id=? and a.status=? order by a.jointime desc limit 1";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, joiner_id);
			dbsrc.setInt(2,status);
			dbsrc.executeQuery();
		  
			if(dbsrc.next()){
				act=new ActivityInfo();
				act.setId(dbsrc.getInt("id"));
				act.setAuthorId(dbsrc.getInt("author_id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("actname"));
				act.setActhost(dbsrc.getString("acthost"));
				act.setActcontent(dbsrc.getString("actcontent"));
				act.setActplace(dbsrc.getString("actplace"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
			}
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		
		return act;
	}
	

	 
	/**
	* ��ȡ�ȵ���Ϣ�б�
	* @param curPage ��ǰҳ��
	* @param pageSize	��ҳ��С
	* @param status      ����״̬ 1��ʾ��ͨ 2��ʾ�ȵ�
	* @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	* @throws SQLException ���ݿ��쳣
	*/
	public static Page getHotActivity(int curPage, int pageSize,int status) throws SQLException {
	 
		if (pageSize<=0){
			  pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		ActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ�˵Ļ��Ϣ�б� */

			String sql="select id,author_id,author_name,activity_name,activity_host,activity_content,activity_place,start_time,end_time,phone,email,create_time from activity where status=? order by create_time desc";
			dbsrc.prepareStatement(sql);
		  
			dbsrc.setInt(1,status);
			dbsrc.executeQuery();
		  
			dbsrc.setPageSize(pageSize);
			dbsrc.setPageNo(curPage);
			page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
			while(dbsrc.pageNext()){
				act=new ActivityInfo();
				act.setId(dbsrc.getInt("id"));
				act.setAuthorId(dbsrc.getInt("author_id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("activity_name"));
				act.setActhost(dbsrc.getString("activity_host"));
				act.setActcontent(dbsrc.getString("activity_content"));
				act.setActplace(dbsrc.getString("activity_place"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
				 
				page.addData(act);
			}
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
		}
	
	
	/**
	* ��ȡ�ȵ���Ϣ�б��һ��
	* @param status      ����״̬ 1��ʾ��ͨ 2��ʾ�ȵ�
	* @throws SQLException ���ݿ��쳣
	*/
	public static ActivityInfo getHotActivityFirst(int status) throws SQLException {
		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ�˵Ļ��Ϣ�б� */
			
			String sql="select id,author_id,author_name,activity_name,activity_host,activity_content,activity_place,start_time,end_time,phone,email,create_time from activity where status=? order by create_time desc limit 1";
			dbsrc.prepareStatement(sql);
		  
			dbsrc.setInt(1,status);
			dbsrc.executeQuery();
		  
			while(dbsrc.next()){
				act=new ActivityInfo();
				act.setId(dbsrc.getInt("id"));
				act.setAuthorId(dbsrc.getInt("author_id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("activity_name"));
				act.setActhost(dbsrc.getString("activity_host"));
				act.setActcontent(dbsrc.getString("activity_content"));
				act.setActplace(dbsrc.getString("activity_place"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
				 
			}
			  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		
		return act;
	}
	
	/**
	* ��ȡ���»��Ϣ�б�,ȡ����20���;
	* @param curPage ��ǰҳ��
	* @param pageSize	��ҳ��С
	* @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	* @throws SQLException ���ݿ��쳣
	*/
	public static Page getNewActivity(int curPage, int pageSize) throws SQLException {

		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		ActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ�˵Ļ��Ϣ�б� */

			String sql="select id,author_id,author_name,activity_name,activity_host,activity_content,activity_place,start_time,end_time,phone,email,create_time from activity order by create_time desc limit 20";
			dbsrc.prepareStatement(sql);
			dbsrc.executeQuery();
	  
			dbsrc.setPageSize(pageSize);
			dbsrc.setPageNo(curPage);
			page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
	  
			while(dbsrc.pageNext()){
				act=new ActivityInfo();
				act.setId(dbsrc.getInt("id"));
				act.setAuthorId(dbsrc.getInt("author_id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("activity_name"));
				act.setActhost(dbsrc.getString("activity_host"));
				act.setActcontent(dbsrc.getString("activity_content"));
				act.setActplace(dbsrc.getString("activity_place"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
							 
				page.addData(act);
			}
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
	
		return page;
	}




	/**
	* ��ȡ���»��Ϣ�б��һ��
	* @throws SQLException ���ݿ��쳣
	* */
	public static ActivityInfo getNewActivityFirst() throws SQLException {

		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ�˵Ļ��Ϣ�б� */
			
			String sql="select id,author_id,author_name,activity_name,activity_host,activity_content,activity_place,start_time,end_time,phone,email,create_time from activity order by create_time desc limit 1";
			dbsrc.prepareStatement(sql);
			dbsrc.executeQuery();
	  
			while(dbsrc.next()){
			act=new ActivityInfo();
			act.setId(dbsrc.getInt("id"));
			act.setAuthorId(dbsrc.getInt("author_id"));
			act.setAuthorname(dbsrc.getString("author_name"));
			act.setActname(dbsrc.getString("activity_name"));
			act.setActhost(dbsrc.getString("activity_host"));
			act.setActcontent(dbsrc.getString("activity_content"));
			act.setActplace(dbsrc.getString("activity_place"));
			act.setStart(dbsrc.getTimestamp("start_time"));
			act.setEnd(dbsrc.getTimestamp("end_time"));
			act.setPhone(dbsrc.getString("phone"));
			act.setEmail(dbsrc.getString("email"));
			act.setCreattime(dbsrc.getTimestamp("create_time"));
			 
	 
			}
	  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
	
		return act;
	}

	/**
	* ���ݻid��ȡ���Ϣ��
	* @param id  �ID
	* @return ���ϸ��Ϣ
	* @throws SQLException ���ݿ��쳣
	*/
	public static ActivityInfo getActivityInfoById(int id) throws SQLException {

		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ���Ϣ�б� */

			String sql="select author_id,author_name,activity_name,activity_host,activity_content,activity_place,start_time,end_time,phone,email,create_time from activity where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1,id);
			dbsrc.executeQuery();
 
			while(dbsrc.next()){
				act=new ActivityInfo();
				act.setId(id);
				act.setAuthorId(dbsrc.getInt("author_id"));
				act.setAuthorname(dbsrc.getString("author_name"));
				act.setActname(dbsrc.getString("activity_name"));
				act.setActhost(dbsrc.getString("activity_host"));
				act.setActcontent(dbsrc.getString("activity_content"));
				act.setActplace(dbsrc.getString("activity_place"));
				act.setStart(dbsrc.getTimestamp("start_time"));
				act.setEnd(dbsrc.getTimestamp("end_time"));
				act.setPhone(dbsrc.getString("phone"));
				act.setEmail(dbsrc.getString("email"));
				act.setCreattime(dbsrc.getTimestamp("create_time"));
				}
  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}

		return act;
	}

	/**
	 * ����һ���,ͬʱ���뵽��������joinactivity
	 * @param activity ������Ϣ����
	 * @return	int ����״̬
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int createActivity(ActivityInfo act) throws SQLException {
		if(act==null)
			return -1;
		int uid=0;		
	
		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
	
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());
	
		/** ��ȡ�IDֵ */
		try{
			SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_ACTIVITYID);
			uid=keygen.getNextKey();
		}catch(Exception e)
		{ 
			e.printStackTrace();            
	
		}

		try {
			/** �ڻ��Ϣ���в����¼�¼ */
			sql = "insert into activity(id,author_id,author_name,activity_name,activity_host,activity_content,activity_place,start_time,end_time,phone,email,create_time,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, uid);
			dbtrac.setInt(2, act.getAuthorId());
			dbtrac.setString(3, act.getAuthorname());
			dbtrac.setString(4, act.getActname());
			dbtrac.setString(5,act.getActhost());
			dbtrac.setString(6, act.getActcontent());
			dbtrac.setString(7,act.getActplace());
			dbtrac.setTimestamp(8,act.getStart());
			dbtrac.setTimestamp(9,act.getEnd());
			dbtrac.setString(10, act.getPhone());
			dbtrac.setString(11,act.getEmail());
			dbtrac.setTimestamp(12,now);
			dbtrac.setInt(13, act.getStatus());
	 
	  
			affectedRows = dbtrac.executeUpdate();
			/** �����¼�¼�Ƿ�ɹ� */
			if (affectedRows < 1) {
				dbErrors = true;
				return -1;
			}
		 
			sql="insert into joinactivity(id,joiner_id,status,joiner_name,jointime) values (?,?,?,?,?)"; 
					
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, uid);//��Ӧactivity���е�����id
			dbtrac.setInt(2,act.getAuthorId());
			dbtrac.setInt(3,Utils.ACTIVITY_JOIN);//statusΪ1,��ʾ����˻,2��ʾ��ע�˻;
			dbtrac.setString(4,act.getAuthorname());
			dbtrac.setTimestamp(5,now);
			affectedRows = dbtrac.executeUpdate();
			/** �����¼�¼�Ƿ�ɹ� */
			if (affectedRows < 1) {
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
			/**�ж��Ƿ�������ݿ����������֣��ع��������ݿ����������ִ��*/
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
	* ���ݻ���ƣ�����ߺͻ����ʱ���ȡ�id
	* @return �id
	* @throws SQLException ���ݿ��쳣
	*/
	public static int getActIdByActname(String actname,String author_name,Timestamp create_time) throws SQLException {

		int actid=0;
		DBSource dbsrc = new DBSource();
		
		try {
			/** �����ݿ���ĳ�id */

			String sql="select id from activity where activity_name=? and author_name=? and create_time=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1,actname);	
			dbsrc.setString(2,author_name);
			dbsrc.setTimestamp(3,create_time);
			dbsrc.executeQuery();
 
			if(dbsrc.next()){
				actid=dbsrc.getInt("id");
			}
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		return actid;
	}



	/**
	* �޸�һ���
	* @param activity ������Ϣ����
	* @return	int ����״̬
	* @throws SQLException ���ݿ��쳣
	*/
	public static int editActivity(ActivityInfo act) throws SQLException {
		DBSource dbsrc = new DBSource();
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		try {
			/** �޸Ļ��Ϣ���м�¼ */
			String sql = "update activity set activity_name=?,activity_host=?,activity_content=?,activity_place=?,start_time=?,end_time=?,phone=?,email=?,create_time=? where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1, act.getActname());
			dbsrc.setString(2, act.getActhost());
			dbsrc.setString(3,act.getActcontent());
			dbsrc.setString(4,act.getActplace());
			dbsrc.setTimestamp(5,act.getStart());
			dbsrc.setTimestamp(6,act.getEnd());
			dbsrc.setString(7,act.getPhone());
			dbsrc.setString(8,act.getEmail());
			dbsrc.setTimestamp(9,now);
			dbsrc.setInt(10,act.getId());
			return dbsrc.executeUpdate();
		}
		finally {
			if (dbsrc != null)
				dbsrc.close();
		}
	}

	/**
	 * ɾ��ĳһ�,ͬʱɾ��joinactivity���еĲ�����Ϣ
	 * @param id �ID
	 * @return	int ����״̬
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int deleteActivity(int id) throws SQLException {
 

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		
		try {
			/** ɾ��ĳһ� */
			sql="delete from activity where id=?";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, id);
	  
			affectedRows = dbtrac.executeUpdate();
			/** ɾ����¼�Ƿ�ɹ� */
			if (affectedRows < 1) {
				dbErrors = true;
				return -1;
			}
			sql="delete from joinactivity where id=?";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, id);
	  
			affectedRows = dbtrac.executeUpdate();
			/** ɾ����¼�Ƿ�ɹ� */
			if (affectedRows < 1) {
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
			/**�ж��Ƿ�������ݿ����������֣��ع��������ݿ����������ִ��*/
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
	* ��ȡĳ�û��μ�/��עĳ���ͳ������
	* @param uid �û�ID
	* @param status �û�����״̬ 
	* @return �����
	* @throws SQLException ���ݿ��쳣
	*/
	public static int getCount(int uid,int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м�����������߹�עͳ����Ϣ */
			String sql = "select count(*) cnt from joinactivity where joiner_id=? and status=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, uid);
			dbsrc.setInt(2,status);
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
	* ��ȡ�ȵ������»ͳ������
	* @return ����
	* @throws SQLException ���ݿ��쳣
	*/
	public static int getCountHot(int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м�������Ŀͳ����Ϣ */
			String sql = "select count(*) cnt from activity where status=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1,status);
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
	* ��ȡ���лͳ������
	* @return ����
	* @throws SQLException ���ݿ��쳣
	*/
	public static int getCountNew() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м�������Ŀͳ����Ϣ */
			String sql = "select count(*) cnt from activity ";
			dbsrc.prepareStatement(sql);
			 
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
	* ����������ȡ�μ�ĳһ������û��ķ�ҳ�б�
	* @param actid �id
	* @param curPage ��ǰҳ��
	* @param pageSize	��ҳ��С
	* @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	* @throws SQLException ���ݿ��쳣
	*/
	public static Page getPageUsers(int actid, int status,int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		RefineInfo joiner=null;
		try {
			/** �����ݿ��м����μ�����Ϣ */
			String sql = "select a.id as id,a.realname as realname,a.photo as photo,a.school as school,a.grade as grade,a.blogid as blogid,a.blogtitle as blogtitle, from users_refined a,joinactivity b where a.id=b.joiner_id and b.id=? and b.status=?";
				
			dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			dbsrc.setInt(1,actid);
			dbsrc.setInt(2,status);
			dbsrc.executeQuery();
					  
			dbsrc.setPageSize(pageSize);
			dbsrc.setPageNo(curPage);
			page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
			while(dbsrc.pageNext()){
				joiner=new RefineInfo();
				joiner.setId(dbsrc.getInt("id"));
				joiner.setRealName(dbsrc.getString("realname"));
				joiner.setPhoto(dbsrc.getString("photo"));
				joiner.setSchool(dbsrc.getString("school"));
				joiner.setGrade(dbsrc.getString("grade"));
				joiner.setBlogId(dbsrc.getInt("blogid"));
				joiner.setBlogTitle(dbsrc.getString("blogtitle"));
			  
				page.addData(joiner);
			}
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}

	}
