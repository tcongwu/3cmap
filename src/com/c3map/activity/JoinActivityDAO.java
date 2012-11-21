package com.c3map.activity;

import java.sql.SQLException;
import java.sql.Timestamp;
import com.zjiao.Page;
import com.zjiao.SysUtils;
import com.zjiao.auth.RefineInfo;
import com.zjiao.db.DBSource;
import java.sql.ResultSet;
import java.util.*;


/**
 * ������Ϣ���ݲ����ӿڣ��κ�������йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��JoinActivityInfo��ķ��ʣ�
 * Ҳ����joinactivity��ķ���
 * @author tcw
 */

public class JoinActivityDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public JoinActivityDAO(){}

	/**
	* ������עĳһ�
	* @param id �ID
	* @param joiner_id �ID
	* @param joiner_status ״̬1��ʾ���룬2��ʾ��ע
	* @return	int ����״̬
	* @throws SQLException ���ݿ��쳣
	*/
	public static int joinActivity(int id,int joiner_id,int join_status) throws SQLException{
		
		Timestamp jointime=new Timestamp(System.currentTimeMillis());
		DBSource dbsrc = new DBSource();
		try {
			/** ����ĳһ� */
			String sql="insert into joinactivity(id,joiner_id,status,jointime) values(?,?,?,?)";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2,joiner_id);
			dbsrc.setInt(3,join_status);
			dbsrc.setTimestamp(4,jointime);
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	* �ı�״̬��������עĳһ�
	* @param id �ID
	* @param joiner_id �ID
	* @param joiner_status ״̬1��ʾ���룬2��ʾ��ע
	* @return	int ����״̬
	* @throws SQLException ���ݿ��쳣
	*/
	public static int UpdateActivityStatus(int id,int joiner_id,int join_status) throws SQLException{
		Timestamp jointime=new Timestamp(System.currentTimeMillis());
		DBSource dbsrc = new DBSource();
		try {
			/** ������߹�עĳһ� */
			String sql="update joinactivity set status=?,jointime=? where id=? and joiner_id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, join_status);
			dbsrc.setTimestamp(2,jointime);
			dbsrc.setInt(3,id);
			dbsrc.setInt(4,joiner_id);
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡĳ� ��ע/�μӻ����
	 * @param uid �ID
	 * @return ͳ������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int getJoinCount(int uid,int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		
		try {
			/** �����ݿ��м�����ͳ����Ϣ */
			String sql = "select count(*) cnt from joinactivity where id=? and status=?";
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
	* �������,����������״̬��Ӧjoinactivity�е�statusֵΪ3
	* @param act ������Ϣ����
	* @return	int ����״̬
	* @throws SQLException ���ݿ��쳣
	*/
	public static int insertMessage(JoinActivityInfo act) throws SQLException {
		if(act==null)
			return -1;
		DBSource dbsrc = new DBSource();
		
		/** ��ȡϵͳ��ǰʱ�� */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		try {
			/** �ڲ�����Ϣ�������в����¼�¼ */
	   
			String	sql="insert into joinactivity(id,joiner_id,joiner_name,status,jointime,message,mestime,joiner_school) values (?,?,?,?,?,?,?,?)"; 
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, act.getActId());//��Ӧactivity���е�����id
			dbsrc.setInt(2,act.getJoinerId());
			dbsrc.setString(3,act.getJoinername());
			dbsrc.setInt(4,Utils.ACTIVITY_MESSAGE);//������ԣ���Ӧ״̬Ϊ3
			dbsrc.setTimestamp(5,now);
			dbsrc.setString(6,act.getMessage());
			dbsrc.setTimestamp(7,now);
			dbsrc.setString(8,act.getJoinerschool());
		
			return dbsrc.executeUpdate();
		}
		finally {
			if (dbsrc != null)
				dbsrc.close();
		}	
		}
	
	
	
	/**
	* ��ȡĳ���������Ϣ�б�
	* @param id  �ID
	* @param curPage ��ǰҳ��
	* @param pageSize	��ҳ��С
	* @param status      ����״̬ 1��ʾ���� 2��ʾ��ע,3��ʾ����
	* @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	* @throws SQLException ���ݿ��쳣
	*/
	public static Page getActivityMessage(int id, int curPage, int pageSize,int status) throws SQLException {
	 
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		JoinActivityInfo  act=null;
		try {
			/** �����ݿ��м���ĳ�˵Ļ������Ϣ�б� */

			String sql="select joiner_id,joiner_name,message, mestime,joiner_school  from joinactivity where id=? and status=? order by mestime";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2,status);
			dbsrc.executeQuery();
		  
			dbsrc.setPageSize(pageSize);
			dbsrc.setPageNo(curPage);
			page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
			while(dbsrc.pageNext()){
				act=new JoinActivityInfo();
				act.setJoinerId(dbsrc.getInt("joiner_id"));
				act.setJoinername(dbsrc.getString("joiner_name"));
				act.setMessage(dbsrc.getString("message"));
				act.setMestime(dbsrc.getTimestamp("mestime"));
				act.setJoinerschool(dbsrc.getString("joiner_school"));
		 	 
				page.addData(act);
			}
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}


	/**
	* ��ȡĳ���������Ϣ�б�,����ҳ
	* @param id  �ID
	* @param status      ����״̬ 1��ʾ���� 2��ʾ��ע,3��ʾ����
	* @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	* @throws SQLException ���ݿ��쳣
	*/
	public static List getActivityMessageList(int id,int status) throws SQLException {
	
		DBSource dbsrc = new DBSource();
		List list=new ArrayList();
		JoinActivityInfo  act=null;
		try {
			/** �����ݿ���ĳ�������Ϣ�б� */

			String sql="select joiner_id,joiner_name,message, mestime,joiner_school  from joinactivity where id=? and status=? order by mestime desc";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2,status);
			dbsrc.executeQuery();
		  
			while(dbsrc.pageNext()){
				act=new JoinActivityInfo();
				act.setJoinerId(dbsrc.getInt("joiner_id"));
				act.setJoinername(dbsrc.getString("joiner_name"));
				act.setMessage(dbsrc.getString("message"));
				act.setMestime(dbsrc.getTimestamp("mestime"));
				act.setJoinerschool(dbsrc.getString("joiner_school"));
		 	 
				list.add(act);
			}
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		
		return list;
	}




	/**
	 * ��ȡ�μӻ��߹�עĳ��û��б����6λ������
	 * @param uid �û�email
	 * @return RefineInfo[] �û���� �������6λ������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static RefineInfo[] getJoiner(int id,int status) throws SQLException {

		RefineInfo[] joiners=new RefineInfo[6];
		RefineInfo joiner=null;
		DBSource dbsrc = new DBSource();
		try {
			/** �����ݿ��м��������û���Ϣ*/
			StringBuffer sql = new StringBuffer("select a.id,a.realname,a.photo,a.school,a.grade,b.jointime from users_refined a,joinactivity b where a.id=b.joiner_id and b.id=? and b.status=?");
			sql.append(" order by b.jointime desc limit 6");
			dbsrc.prepareStatement(sql.toString());
		
			dbsrc.setInt(1, id);
			dbsrc.setInt(2,status);
			dbsrc.executeQuery();
	
			int i=0;
			while (dbsrc.next()&&(i<6)){
				joiner=new RefineInfo();
				joiner.setId(dbsrc.getInt("id"));
				joiner.setRealName(dbsrc.getString("realname"));
				joiner.setPhoto(dbsrc.getString("photo"));
				joiner.setSchool(dbsrc.getString("school"));
				joiner.setGrade(dbsrc.getString("grade"));
		   
				joiners[i]=joiner;
		
				i++;
			}	
		}
		finally {
			if (dbsrc != null)
				dbsrc.close();
			}

		return joiners;
	}


	/**
	 * ����������ȡ����ĳ������û��ķ�ҳ�б�
	 * @param request ��������������http�������
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageJoiners(int id,int status, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
			}
	
		DBSource dbsrc = new DBSource();
		Page page=null;
		RefineInfo joiner=null;
		try {
			/** �����ݿ��м�����������Ϣ */
	
			String sql="select a.id,a.realname,a.photo,a.school,a.grade,b.jointime from users_refined a,joineractivity b where a.id=b.joiner_id and b.id=? and b.status=?  order by b.jointime desc";
			dbsrc.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			dbsrc.setInt(1,id);
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
		  
				page.addData(joiner);
			}
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		return page;
	}


	
	/**
	* ���б���ɾ��ĳ��ע/�μӻ 
	* @param id �id
	* @param joiner_id ������id
	* @param status ����״̬
	* @return int ����״̬��1���ɹ�
	* @throws SQLException ���ݿ��쳣
	*/
	public static int deleteJoinAct(int id, int joiner_id,int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		
		try {
			/** ɾ����Ϣ */
			String sql = "delete  from joinactivity where id=? and joiner_id=? and status=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2, joiner_id);
			dbsrc.setInt(3,status);
	
			return dbsrc.executeUpdate();
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		}	

	
	/**
	* ���б���ɾ��ĳ��ע/�μ�/� 
	* @param id �id
	* @param joiner_id ������id
	* @param status ����״̬,״̬��Ϊ����״̬3
	* @return int ����״̬��1���ɹ�
	* @throws SQLException ���ݿ��쳣
	*/
	public static int deleteJoinAndCareAct(int id, int joiner_id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		
		try {
			/** ɾ����Ϣ */
			String sql = "delete  from joinactivity where id=? and joiner_id=? and status!=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2, joiner_id);
			dbsrc.setInt(3,Utils.ACTIVITY_MESSAGE);
	
			return dbsrc.executeUpdate();
		  
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
	}	

	
	/**
	* ���б���ɾ��ĳ�����
	* @param id �id
	* @param joiner_id ������id
	* @param status ����״̬
	* @param mestime ����ʱ��
	* @return int ����״̬��1���ɹ�
	* @throws SQLException ���ݿ��쳣
	*/
	public static int deleteJoinMes(int id, int joiner_id,int status,Timestamp mestime) throws SQLException {
		DBSource dbsrc = new DBSource();
		
		try {
			/** ɾ����Ϣ */
			String sql = "delete  from joinactivity where id=? and joiner_id=? and status=? and mestime=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2, joiner_id);
			dbsrc.setInt(3,status);
			dbsrc.setTimestamp(4,mestime);
	
			return dbsrc.executeUpdate();
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
	}	


	/**
	* �Ӽ�����joinactiviry�и���id ��joiner_id ���ĳ�˵�״̬����Ϊ����״̬3����
	* @param id �id
	* @param joiner_id ������id
	* @param status ����״̬
	* @return ��ȡstatus
	* @throws SQLException ���ݿ��쳣
	*/
	public static int getJoinerStatus(int id, int joiner_id,int mestatus) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		int status=-1;
		try {
			/** ��ȡstatus״̬��Ϣ */
			String sql = "select status from joinactivity where id=? and joiner_id=? and status!=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, id);
			dbsrc.setInt(2, joiner_id);
			dbsrc.setInt(3,mestatus);
			dbsrc.executeQuery();	
	      
			if(dbsrc.next()){
				status=dbsrc.getInt("status");
			  }
			
		} finally {
			if (dbsrc!=null)
				dbsrc.close();
		}
		return  status;
	}	
	}
