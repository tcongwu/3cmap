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
 * 参与活动信息数据操作接口，任何与参与活动有关的数据操作都通过此类完成。
 * 用于封装对JoinActivityInfo类的访问，
 * 也就是joinactivity表的访问
 * @author tcw
 */

public class JoinActivityDAO {

	/**
	 * 默认的构造函数
	 */
	public JoinActivityDAO(){}

	/**
	* 加入或关注某一活动
	* @param id 活动ID
	* @param joiner_id 活动ID
	* @param joiner_status 状态1表示加入，2表示关注
	* @return	int 操作状态
	* @throws SQLException 数据库异常
	*/
	public static int joinActivity(int id,int joiner_id,int join_status) throws SQLException{
		
		Timestamp jointime=new Timestamp(System.currentTimeMillis());
		DBSource dbsrc = new DBSource();
		try {
			/** 加入某一活动 */
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
	* 改变状态，加入或关注某一活动
	* @param id 活动ID
	* @param joiner_id 活动ID
	* @param joiner_status 状态1表示加入，2表示关注
	* @return	int 操作状态
	* @throws SQLException 数据库异常
	*/
	public static int UpdateActivityStatus(int id,int joiner_id,int join_status) throws SQLException{
		Timestamp jointime=new Timestamp(System.currentTimeMillis());
		DBSource dbsrc = new DBSource();
		try {
			/** 加入或者关注某一活动 */
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
	 * 获取某活动 关注/参加活动总数
	 * @param uid 活动ID
	 * @return 统计数量
	 * @throws SQLException 数据库异常
	 */
	public static int getJoinCount(int uid,int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		
		try {
			/** 从数据库中检索出统计信息 */
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
	* 给活动留言,所有留言者状态对应joinactivity中的status值为3
	* @param act 参与活动信息对象
	* @return	int 操作状态
	* @throws SQLException 数据库异常
	*/
	public static int insertMessage(JoinActivityInfo act) throws SQLException {
		if(act==null)
			return -1;
		DBSource dbsrc = new DBSource();
		
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		try {
			/** 在参与活动信息表留言中插入新记录 */
	   
			String	sql="insert into joinactivity(id,joiner_id,joiner_name,status,jointime,message,mestime,joiner_school) values (?,?,?,?,?,?,?,?)"; 
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, act.getActId());//对应activity表中的文章id
			dbsrc.setInt(2,act.getJoinerId());
			dbsrc.setString(3,act.getJoinername());
			dbsrc.setInt(4,Utils.ACTIVITY_MESSAGE);//给活动留言，对应状态为3
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
	* 获取某活动的留言信息列表
	* @param id  活动ID
	* @param curPage 当前页面
	* @param pageSize	分页大小
	* @param status      参与活动状态 1表示加入 2表示关注,3表示留言
	* @return 页面对象（含分页信息和当前页面的数据记录）
	* @throws SQLException 数据库异常
	*/
	public static Page getActivityMessage(int id, int curPage, int pageSize,int status) throws SQLException {
	 
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		JoinActivityInfo  act=null;
		try {
			/** 从数据库中检索某人的活动留言信息列表 */

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
	* 获取某活动的留言信息列表,不分页
	* @param id  活动ID
	* @param status      参与活动状态 1表示加入 2表示关注,3表示留言
	* @return 页面对象（含分页信息和当前页面的数据记录）
	* @throws SQLException 数据库异常
	*/
	public static List getActivityMessageList(int id,int status) throws SQLException {
	
		DBSource dbsrc = new DBSource();
		List list=new ArrayList();
		JoinActivityInfo  act=null;
		try {
			/** 从数据库中某活动留言信息列表 */

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
	 * 获取参加或者关注某活动用户列表最初6位参与者
	 * @param uid 用户email
	 * @return RefineInfo[] 用户类别 返回最初6位参与者
	 * @throws SQLException 数据库异常
	 */
	public static RefineInfo[] getJoiner(int id,int status) throws SQLException {

		RefineInfo[] joiners=new RefineInfo[6];
		RefineInfo joiner=null;
		DBSource dbsrc = new DBSource();
		try {
			/** 从数据库中检索参与用户信息*/
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
	 * 根据条件获取参与某活动所有用户的分页列表
	 * @param request 含有搜索条件的http请求对象
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageJoiners(int id,int status, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
			}
	
		DBSource dbsrc = new DBSource();
		Page page=null;
		RefineInfo joiner=null;
		try {
			/** 从数据库中检索参与者信息 */
	
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
	* 从列表中删除某关注/参加活动 
	* @param id 活动id
	* @param joiner_id 参与者id
	* @param status 参与状态
	* @return int 操作状态，1－成功
	* @throws SQLException 数据库异常
	*/
	public static int deleteJoinAct(int id, int joiner_id,int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		
		try {
			/** 删除信息 */
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
	* 从列表中删除某关注/参加/活动 
	* @param id 活动id
	* @param joiner_id 参与者id
	* @param status 参与状态,状态不为留言状态3
	* @return int 操作状态，1－成功
	* @throws SQLException 数据库异常
	*/
	public static int deleteJoinAndCareAct(int id, int joiner_id) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		
		try {
			/** 删除信息 */
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
	* 从列表中删除某活动留言
	* @param id 活动id
	* @param joiner_id 参与者id
	* @param status 参与状态
	* @param mestime 留言时间
	* @return int 操作状态，1－成功
	* @throws SQLException 数据库异常
	*/
	public static int deleteJoinMes(int id, int joiner_id,int status,Timestamp mestime) throws SQLException {
		DBSource dbsrc = new DBSource();
		
		try {
			/** 删除信息 */
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
	* 从加入活动表joinactiviry中根据id 和joiner_id 获得某人的状态（不为留言状态3）；
	* @param id 活动id
	* @param joiner_id 参与者id
	* @param status 参与状态
	* @return 获取status
	* @throws SQLException 数据库异常
	*/
	public static int getJoinerStatus(int id, int joiner_id,int mestatus) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		int status=-1;
		try {
			/** 获取status状态信息 */
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
