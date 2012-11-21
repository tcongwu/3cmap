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
 * 活动信息数据操作接口，任何与活动有关的数据操作都通过此类完成。
 * 用于封装对ActivityInfo类的访问，
 * 也就是activity表的访问
 * @author tcw
 */
public class ActivityDAO {

	/**
	* 默认的构造函数
	*/
	public ActivityDAO(){}
	 
	
	/**
	* 获取某个人的参与/关注活动信息列表
	* @param id  活动ID
	* @param joiner_id  参与者id
	* @param curPage 当前页面
	* @param pageSize	分页大小
	* @param status      参与活动状态 1表示加入 2表示关注
	* @return 页面对象（含分页信息和当前页面的数据记录）
	* @throws SQLException 数据库异常
	*/
	public static Page getJoinActivity(int joiner_id, int curPage, int pageSize,int status) throws SQLException {
	 
		if (pageSize<=0){
			  pageSize=SysUtils.PAGE_SIZE;
		  }
		      DBSource dbsrc = new DBSource();
		      Page page=null;
		      ActivityInfo  act=null;
		      try {
		    	  /** 从数据库中某人的活动信息列表 */

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
	 * 获取某个人的参与/关注活动信息列表最近6条,参与的3条,关注的3条,用于提醒
	 * @param id  活动ID
	 * @param joiner_id  参与者id
	 * @param status      参与活动状态 1表示加入 2表示关注
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	*/
	public static List getJoinActivityRemind(int joiner_id,int status) throws SQLException {

		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		DBSource dbsrc = new DBSource();
		List list=new ArrayList();
		ActivityInfo  act=null;
		try {
			/** 从数据库中某人的参与/关注活动信息列表 */

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
	 * 根据查询条件获取活动信息列表
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
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
			/** 从数据库中获取符合条件的活动信息 */
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
	* 获取某个人的参与/关注活动信息列表第一条
	* @param joiner_id   参与者id
	* @param status      参与活动状态 1表示加入 2表示关注
	* @throws SQLException 数据库异常
	*/
	public static ActivityInfo getJoinActivityFirst(int joiner_id,int status) throws SQLException {
	 
		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		
		try {
			/** 从数据库中某人的活动信息列表 */
			
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
	* 获取热点活动信息列表
	* @param curPage 当前页面
	* @param pageSize	分页大小
	* @param status      参与活动状态 1表示普通 2表示热点
	* @return 页面对象（含分页信息和当前页面的数据记录）
	* @throws SQLException 数据库异常
	*/
	public static Page getHotActivity(int curPage, int pageSize,int status) throws SQLException {
	 
		if (pageSize<=0){
			  pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		ActivityInfo  act=null;
		try {
			/** 从数据库中某人的活动信息列表 */

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
	* 获取热点活动信息列表第一条
	* @param status      参与活动状态 1表示普通 2表示热点
	* @throws SQLException 数据库异常
	*/
	public static ActivityInfo getHotActivityFirst(int status) throws SQLException {
		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		try {
			/** 从数据库中某人的活动信息列表 */
			
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
	* 获取最新活动信息列表,取最新20个活动;
	* @param curPage 当前页面
	* @param pageSize	分页大小
	* @return 页面对象（含分页信息和当前页面的数据记录）
	* @throws SQLException 数据库异常
	*/
	public static Page getNewActivity(int curPage, int pageSize) throws SQLException {

		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		DBSource dbsrc = new DBSource();
		Page page=null;
		ActivityInfo  act=null;
		try {
			/** 从数据库中某人的活动信息列表 */

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
	* 获取最新活动信息列表第一条
	* @throws SQLException 数据库异常
	* */
	public static ActivityInfo getNewActivityFirst() throws SQLException {

		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		try {
			/** 从数据库中某人的活动信息列表 */
			
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
	* 根据活动id获取活动信息表
	* @param id  活动ID
	* @return 活动详细信息
	* @throws SQLException 数据库异常
	*/
	public static ActivityInfo getActivityInfoById(int id) throws SQLException {

		DBSource dbsrc = new DBSource();
		ActivityInfo  act=null;
		try {
			/** 从数据库中某活动信息列表 */

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
	 * 创建一个活动,同时插入到活动参与表中joinactivity
	 * @param activity 讨论信息对象
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int createActivity(ActivityInfo act) throws SQLException {
		if(act==null)
			return -1;
		int uid=0;		
	
		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
	
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());
	
		/** 获取活动ID值 */
		try{
			SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_ACTIVITYID);
			uid=keygen.getNextKey();
		}catch(Exception e)
		{ 
			e.printStackTrace();            
	
		}

		try {
			/** 在活动信息表中插入新记录 */
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
			/** 插入新记录是否成功 */
			if (affectedRows < 1) {
				dbErrors = true;
				return -1;
			}
		 
			sql="insert into joinactivity(id,joiner_id,status,joiner_name,jointime) values (?,?,?,?,?)"; 
					
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, uid);//对应activity表中的文章id
			dbtrac.setInt(2,act.getAuthorId());
			dbtrac.setInt(3,Utils.ACTIVITY_JOIN);//status为1,表示参与此活动,2表示关注此活动;
			dbtrac.setString(4,act.getAuthorname());
			dbtrac.setTimestamp(5,now);
			affectedRows = dbtrac.executeUpdate();
			/** 插入新记录是否成功 */
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
			/**判断是否出现数据库错误，如果出现，回滚所有数据库操作，否则执行*/
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
	* 根据活动名称，活动作者和活动创建时间获取活动id
	* @return 活动id
	* @throws SQLException 数据库异常
	*/
	public static int getActIdByActname(String actname,String author_name,Timestamp create_time) throws SQLException {

		int actid=0;
		DBSource dbsrc = new DBSource();
		
		try {
			/** 从数据库中某活动id */

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
	* 修改一个活动
	* @param activity 讨论信息对象
	* @return	int 操作状态
	* @throws SQLException 数据库异常
	*/
	public static int editActivity(ActivityInfo act) throws SQLException {
		DBSource dbsrc = new DBSource();
		/** 获取系统当前时间 */
		Timestamp now=new Timestamp(System.currentTimeMillis());
		try {
			/** 修改活动信息表中记录 */
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
	 * 删除某一活动,同时删除joinactivity表中的参与活动信息
	 * @param id 活动ID
	 * @return	int 操作状态
	 * @throws SQLException 数据库异常
	 */
	public static int deleteActivity(int id) throws SQLException {
 

		DBTransaction dbtrac = new DBTransaction();
		boolean dbErrors = false;
		int affectedRows=0;
		String sql = null;
		
		try {
			/** 删除某一活动 */
			sql="delete from activity where id=?";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, id);
	  
			affectedRows = dbtrac.executeUpdate();
			/** 删除记录是否成功 */
			if (affectedRows < 1) {
				dbErrors = true;
				return -1;
			}
			sql="delete from joinactivity where id=?";
			dbtrac.prepareStatement(sql);
			dbtrac.setInt(1, id);
	  
			affectedRows = dbtrac.executeUpdate();
			/** 删除记录是否成功 */
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
			/**判断是否出现数据库错误，如果出现，回滚所有数据库操作，否则执行*/
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
	* 获取某用户参加/关注某活动的统计数量
	* @param uid 用户ID
	* @param status 用户参与活动状态 
	* @return 活动数量
	* @throws SQLException 数据库异常
	*/
	public static int getCount(int uid,int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
			/** 从数据库中检索出参与或者关注统计信息 */
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
	* 获取热点活动，最新活动统计数量
	* @return 数量
	* @throws SQLException 数据库异常
	*/
	public static int getCountHot(int status) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
			/** 从数据库中检索出数目统计信息 */
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
	* 获取所有活动统计数量
	* @return 数量
	* @throws SQLException 数据库异常
	*/
	public static int getCountNew() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
			/** 从数据库中检索出数目统计信息 */
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
	* 根据条件获取参加某一活动所有用户的分页列表
	* @param actid 活动id
	* @param curPage 当前页面
	* @param pageSize	分页大小
	* @return 页面对象（含分页信息和当前页面的数据记录）
	* @throws SQLException 数据库异常
	*/
	public static Page getPageUsers(int actid, int status,int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.PAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		RefineInfo joiner=null;
		try {
			/** 从数据库中检索参加者信息 */
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
