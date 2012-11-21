/*
 * 创建日期 2006-7-30
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.c3map.blog;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.zjiao.Page;
import com.zjiao.SysUtils;
import com.zjiao.key.*;
import com.zjiao.db.DBSource;
import com.zjiao.db.DBTransaction;
import com.zjiao.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.zjiao.auth.*;
import java.util.Calendar;
public class ArticleDAO {

  public ArticleDAO(){}
  
  /**
	   * 获取某个人的blog文章信息列表
	   * @param id  用户ID
	   * @param curPage 当前页面
	   * @param pageSize	分页大小
	   * @param arttype  blog文章类别
	   * @return 页面对象（含分页信息和当前页面的数据记录）
	   * @throws SQLException 数据库异常
	   */
	  public static Page getBlog(UserInfo uinfo, int curPage, int pageSize,HttpServletRequest request) throws SQLException {
	  	if((uinfo==null)&&(request==null)){
	  		 return null;
	    	}
		  if (pageSize<=0){
			  pageSize=SysUtils.PAGE_SIZE;
		   }
		  DBSource dbsrc = new DBSource();
		  Page page=null;
		  Article  art=null;
		  
		  String replyOrder=request.getParameter("replyOrder");
		  String artType=request.getParameter("artType");
		  
		  try {
			/** 从数据库中某作者blog文章信息 */
			String sql = "select id,article_name,issue_date,read_num,reply_num,reply_date from article where author_id=?";
			
			if(!StringUtils.isNull(artType)){
		  	sql=sql+" and article_type='"+artType+"'";
			}
			if(StringUtils.isNull(replyOrder)){sql=sql+" order by issue_date  desc";}
			if(!StringUtils.isNull(replyOrder)){
				if(replyOrder.equals("最近发表")){replyOrder="issue_date";}
				if(replyOrder.equals("最近回复")){replyOrder="reply_date";}
				if(replyOrder.equals("最热回复")){replyOrder="reply_num";}
		  	sql=sql+" order by "+replyOrder +" desc";
			  }
		  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			dbsrc.setInt(1, uinfo.getId());
			dbsrc.executeQuery();
		  
			dbsrc.setPageSize(pageSize);
			dbsrc.setPageNo(curPage);
			page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  while(dbsrc.pageNext()){
				  art=new Article();
				  art.setId(dbsrc.getInt("id"));
				 // art.setAuthorId(dbsrc.getInt("author_id"));
				  art.setName(dbsrc.getString("article_name"));
				  art.setIssueDate(dbsrc.getTimestamp("issue_date"));
				  art.setReadNum(dbsrc.getInt("read_num"));
				  art.setReplyNum(dbsrc.getInt("reply_num"));
				  art.setReplyDate(dbsrc.getTimestamp("reply_date"));
				  art.setAuthorId(uinfo.getId());
				  page.addData(art);
			  }
		  
		  } finally {
			if (dbsrc!=null)
				  dbsrc.close();
		  }
		
		  return page;
	  }
	
	
	/**
		  * 根据月份统计获取某个人该月份的blog文章信息列表
		  * @param id  用户ID
		  * @param curPage 当前页面
		  * @param pageSize	分页大小
		  * @param arttype  blog文章类别
		  * @return 页面对象（含分页信息和当前页面的数据记录）
		  * @throws SQLException 数据库异常
		  */
		 public static Page getBlogByMontime(UserInfo uinfo,String mon, int curPage, int pageSize,HttpServletRequest request) throws SQLException {
		   if((uinfo==null)&&(request==null)){
			    return null;
		     }
			 if (pageSize<=0){
				  pageSize=SysUtils.PAGE_SIZE;
			   }
			  DBSource dbsrc = new DBSource();
			  Page page=null;
			  Article  art=null;
		  
			  String replyOrder=request.getParameter("replyOrder");
			  String artType=request.getParameter("artType");
		  
		
		  	Calendar cal = Calendar.getInstance();
		    mon =request.getParameter("montime");
		    String yy=mon.substring(0,4);
		    String mm=mon.substring(5,7);
			
			 if(mon.substring(5,6).equals("0")){mm=mon.substring(6,7);}
		    
		    
		    int y=Integer.parseInt(yy);
		    int m=Integer.parseInt(mm);
		    cal.set( y, m-1, 1,0,0,0 );
		    Timestamp minissue=new Timestamp(cal.getTimeInMillis());
		    
		    cal.clear();
		    int max = cal.getMaximum(Calendar.DAY_OF_MONTH);
		    cal.set(y,m-1,max,23,59,59);
		    Timestamp maxissue=new Timestamp(cal.getTimeInMillis());
			  
			 try {
			   /** 从数据库中某作者blog文章信息 */
			   String sql = "select id,article_name,issue_date,read_num,reply_num,reply_date from article where author_id=? and issue_date>=? and issue_date<=?";
			
			   if(!StringUtils.isNull(artType)){
			   sql=sql+" and article_type='"+artType+"'";
			   }
			   if(StringUtils.isNull(replyOrder)){sql=sql+" order by issue_date  desc";}
			   if(!StringUtils.isNull(replyOrder)){
				   if(replyOrder.equals("最近发表")){replyOrder="issue_date";}
				   if(replyOrder.equals("最近回复")){replyOrder="reply_date";}
				   if(replyOrder.equals("最热回复")){replyOrder="reply_num";}
			   sql=sql+" order by "+replyOrder +" desc";
				 }
		  
			   dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			   dbsrc.setInt(1, uinfo.getId());
			   dbsrc.setTimestamp(2,minissue);
			   dbsrc.setTimestamp(3,maxissue);
		     dbsrc.executeQuery();
		     dbsrc.setPageSize(pageSize);
			   dbsrc.setPageNo(curPage);
			   page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
			   while(dbsrc.pageNext()){
					 art=new Article();
					 art.setId(dbsrc.getInt("id"));
					// art.setAuthorId(dbsrc.getInt("author_id"));
					 art.setName(dbsrc.getString("article_name"));
					 art.setIssueDate(dbsrc.getTimestamp("issue_date"));
					 art.setReadNum(dbsrc.getInt("read_num"));
					 art.setReplyNum(dbsrc.getInt("reply_num"));
					 art.setReplyDate(dbsrc.getTimestamp("reply_date"));
				   page.addData(art);
				 }
		  
			 } finally {
			   if (dbsrc!=null)
					 dbsrc.close();
			 }
		
			 return page;
		 }
	
	
	
	  /**
		 * 获取某一文章信息
		 * @param 文章ID
		 * @return 文章信息列表
		 * @throws SQLException 数据库异常
		 */
		public static List getArticle(int id) throws SQLException {
			if(id==0){
				return null;
			}
			
			DBSource dbsrc = new DBSource();
			ArrayList list=new ArrayList();
			Article art=null;
			try {
			  /** 从数据库中检索班级讨论信息 */
				 String sql = "select author_id,article_name,article_body,issue_date,article_type,read_num,reply_num,reply_date from article where id=?";
				 sql=sql+" order by issue_date desc";
			   dbsrc.prepareStatement(sql);
			   dbsrc.setInt(1, id);
			
			   dbsrc.executeQuery();
			   art=new Article();
			   while(dbsrc.next()){
			  	  art.setAuthorId(dbsrc.getInt("author_id"));
				    art.setName(dbsrc.getString("article_name"));
				  	art.setBody(dbsrc.getString("article_body"));
				  	art.setIssueDate(dbsrc.getTimestamp("issue_date"));
				  	art.setArtType(dbsrc.getString("article_type"));
				  	art.setReadNum(dbsrc.getInt("read_num"));
			  	  art.setReplyNum(dbsrc.getInt("reply_num"));
			    	art.setReplyDate(dbsrc.getTimestamp("reply_date"));
					  list.add(art);
				}
				
				//浏览次数加1
				if(list.size()>0){
					/** 浏览次数加1 */
				  	sql = "update article set read_num=read_num+1 where id=?";
				  	dbsrc.prepareStatement(sql);
				  	dbsrc.setInt(1, id);
					  dbsrc.executeUpdate();
				}
				
			    sql="update users set score=score+? where id=?"; 
				  dbsrc.prepareStatement(sql);
			   	dbsrc.setInt(1,SysUtils.SCORE_BROWSED);
				  dbsrc.setInt(2,id);
				  dbsrc.executeUpdate();
					
			} finally {
			  if (dbsrc!=null)
					dbsrc.close();
			}
			
			return list;
		}
		
	
	
	/*
	 * 根据作者id获取最近发表的一篇文章信息，用于更新users_refined中的blog信息
	 */
	public  static Article getBlogByAuthorId(UserInfo uinfo) throws SQLException {
			
				DBSource dbsrc = new DBSource();
				Article  art=null;
				try {
				  /** 从数据库中某作者blog文章信息 */
	     String 	sql = "select id,article_name,article_body,issue_date  from article where author_id=? order by id desc limit 1";
			
				  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				  dbsrc.setInt(1,uinfo.getId());
				  dbsrc.executeQuery();
				  
			   while(dbsrc.next()){
			    	art=new Article();
				   	art.setId(dbsrc.getInt("id"));
					  art.setName(dbsrc.getString("article_name"));
						art.setBody(dbsrc.getString("article_body"));
						art.setIssueDate(dbsrc.getTimestamp("issue_date"));
				 
					}
				} finally {
				  if (dbsrc!=null)
						dbsrc.close();
				}
		
				return art;
			}
	
	
	
	
	
	/**
		 * 添加新文章；
		 * @param art 讨论信息对象
		 * @return	int 操作状态
		 * @throws SQLException 数据库异常
		 */
		public static int insertBlog(Article art) throws SQLException ,KeyException {
			if(art==null)
				return -1;
			 int uid=0;	
		
			DBTransaction dbtrac = new DBTransaction();
			boolean dbErrors = false;
			int affectedRows=0;
			String sql = null;
			
		/** 获取blog文章信息ID值 */
		
		try{
		
		 	SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_ARTICLEID);
			 uid=keygen.getNextKey();
	  	}catch(Exception e)
		   { 
			  e.printStackTrace();            
		
		  }
			/** 获取系统当前时间 */
			   Timestamp now=new Timestamp(System.currentTimeMillis());
		     String monn=StringUtils.exportDate(now, "yyyy年MM月");
           
			try {
       /** 插入新记录 */
        sql = "insert into article(id,author_id,article_name,keyword,article_body,opentype,issue_date,read_num,reply_num,article_type,reply_date) values(?,?,?,?,?,?,?,?,?,?,?)";
            dbtrac.prepareStatement(sql);
            dbtrac.setInt(1, uid);
            dbtrac.setInt(2, art.getAuthorId());
            dbtrac.setString(3, art.getName());
            dbtrac.setString(4, art.getKeyword());
            dbtrac.setString(5, art.getBody ());
            dbtrac.setInt(6, art.getOpenType());
            dbtrac.setTimestamp(7,now);
            dbtrac.setInt(8, art.getReadNum());
            dbtrac.setInt(9,art.getReplyNum());
            dbtrac.setString(10,art.getArtType());
            dbtrac.setTimestamp(11,art.getReplyDate());
			 
		   
            affectedRows = dbtrac.executeUpdate();
			  /** 插入新记录是否成功 */
			  if (affectedRows < 1) {
				  	dbErrors = true;
				  	return -1;
			    }
			  
		    	sql="insert into monarticle(id,author_id,mon_time) values (?,?,?)"; 
			    dbtrac.prepareStatement(sql);
			    dbtrac.setInt(1, uid);//对应article表中的文章id
			    dbtrac.setInt(2,art.getAuthorId());
			    dbtrac.setString(3,monn);
			    affectedRows = dbtrac.executeUpdate();
			    /** 插入新记录是否成功 */
			   if (affectedRows < 1) {
					  dbErrors = true;
					  return -1;
			      }
			   
			    sql="update users_refined set blogid=?,blogtitle=?,blogtime=?,blogcontent=?,blogcount=blogcount+1 where id=?"; 
					String content="";	  
					
						  dbtrac.prepareStatement(sql);
						  content=art.getBody();
						  if(content.length()>255){
						  	content=content.substring(0,255);
						  }
						  dbtrac.setInt(1, uid);//
						  dbtrac.setString(2,art.getName());
						  dbtrac.setTimestamp(3,now);
						  dbtrac.setString(4,content);
						  dbtrac.setInt(5,art.getAuthorId());
						  affectedRows = dbtrac.executeUpdate();
						  /** 更新记录是否成功 */
						  if (affectedRows < 1) {
								dbErrors = true;
								return -1;
						  }
						  
			  	sql="update users set score=score+? where id=?"; 
										dbtrac.prepareStatement(sql);
										dbtrac.setInt(1,SysUtils.SCORE_WRITE_BLOG);
										dbtrac.setInt(2,art.getAuthorId());
										affectedRows = dbtrac.executeUpdate();
										/** 更新记录是否成功 */
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
			 * 修改文章；
			 * @param art 讨论信息对象
			 * @return	int 操作状态
			 * @throws SQLException 数据库异常
			 */
			public static int editBlog(Article art) throws SQLException ,KeyException {
				if(art==null)
					return -1;
				DBSource dbsrc = new DBSource();
		
				/** 获取系统当前时间 */
				Timestamp now=new Timestamp(System.currentTimeMillis());

			try {
	     /** 更新记录 */
	     	String sql = "update article set article_name=?,article_body=?,issue_date=?,article_type=? where id=?";
			  dbsrc.prepareStatement(sql);
			  dbsrc.setString(1,art.getName());
				dbsrc.setString(2,art.getBody ());
				dbsrc.setTimestamp(3,now);
				dbsrc.setString(4,art.getArtType());
			  dbsrc.setInt(5,art.getId());
			 return dbsrc.executeUpdate();
				 }
				
				finally {
				  if (dbsrc != null)
				  dbsrc.close();
				}
			}
	
	

	public  Article getBlogByArtId(int id) throws SQLException {
			
				DBSource dbsrc = new DBSource();
				
				ArticleDAO article=new ArticleDAO();
				Article  art=null;
				try {
				  /** 从数据库中某作者blog文章信息 */
				  String sql = "select author_id,article_name,article_body,issue_date,read_num,reply_num,article_type from article where id=?";
			    sql=sql+" order by issue_date desc";
				  dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				  dbsrc.setInt(1, id);
			    dbsrc.executeQuery();
			 	  Timestamp now=new Timestamp(System.currentTimeMillis());
				  art=new Article();
			  while(dbsrc.next()){
				  	art.setAuthorId(dbsrc.getInt("author_id"));
					  art.setName(dbsrc.getString("article_name"));
						art.setBody(dbsrc.getString("article_body"));
						art.setIssueDate(dbsrc.getTimestamp("issue_date"));
						art.setReadNum(dbsrc.getInt("read_num"));
						art.setReplyNum(dbsrc.getInt("reply_num"));
						art.setArtType(dbsrc.getString("article_type"));
					}
				
				} finally {
				  if (dbsrc!=null)
						dbsrc.close();
				}
		
				return art;
			}
	
	
	

	/**
			 * 更新用户精要表的最新blog文章信息
			 * @param id 作者ID
			 * @return 操作结果
			 * @throws SQLException 数据库异常
			 */
			public static int updateRefineUser(Article art) throws SQLException {
			
				
				DBSource dbsrc = new DBSource();
				try {
			
		  String  sql="update users_refined set blogid=?,blogtitle=?,blogtime=? where id=?"; 
				  dbsrc.prepareStatement(sql);
				  dbsrc.setInt(1, art.getId());
				  dbsrc.setString(2, art.getName());
				  dbsrc.setTimestamp(3, art.getIssueDate());
				  dbsrc.setInt(4, art.getAuthorId());
			   return dbsrc.executeUpdate();
				}
				finally {
				  if (dbsrc != null)
						dbsrc.close();
				}
			}


		/**
		 * 删除某一文章
		 * @param id 文章ID
		 * @return	int 操作状态
		 * @throws SQLException 数据库异常
		 */
		public  static int deleteArt(int id,UserInfo uinfo) throws SQLException {
		
			   DBTransaction dbtrac = new DBTransaction();
			   boolean dbErrors = false;
			   int affectedRows=0;
			   String sql = null;
			
			try {
			  /** 删除某一blog文章列表 */
			   sql="delete from article where id=?";
			   dbtrac.prepareStatement(sql.toString());
			   dbtrac.setInt(1, id);
			   affectedRows = dbtrac.executeUpdate();
				  /** 删除记录是否成功 */
			 if (affectedRows < 1) {
						dbErrors = true;
						return -1;
				  }
				  
				/** 删除某一blog按月统计文章列表 */
					affectedRows=0;  
					sql="delete from monarticle where id=?";	
		      dbtrac.prepareStatement(sql.toString());
          dbtrac.setInt(1, id);
					affectedRows = dbtrac.executeUpdate();
				 /** 删除记录是否成功 */
			  if (affectedRows < 1) {
						 dbErrors = true;
						  return -1;
					}
				  
			  /** 删除某一blog回复文章列表 */
			     affectedRows=0;
				   sql="delete from rearticle where article_id=?";
			     dbtrac.prepareStatement(sql.toString());
					 dbtrac.setInt(1, id);
			     affectedRows = dbtrac.executeUpdate();
			
			 /** 如果删除某一blog回复文章，则相应的扣分 */
			    	sql="update users set score=score-? where id=?"; 
						dbtrac.prepareStatement(sql);
						dbtrac.setInt(1,SysUtils.SCORE_WRITE_BLOG);
						dbtrac.setInt(2,uinfo.getId());
						affectedRows = dbtrac.executeUpdate();
				/** 更新记录是否成功 */
					if (affectedRows < 1) {
						  dbErrors = true;
							return -1;
							}
		  
			Article Art=ArticleDAO.getBlogByAuthorId(uinfo);
			
			sql="update users_refined set blogid=?,blogtitle=?,blogtime=?,blogcontent=?,blogcount=blogcount-1 where id=?"; 
		       	dbtrac.prepareStatement(sql);
				    String content="";	  
					  content=Art.getBody();
					  if(content.length()>255){
								 content=content.substring(0,255);
							  }
				  	dbtrac.setInt(1,Art.getId());
						dbtrac.setString(2,Art.getName());
				    dbtrac.setTimestamp(3,Art.getIssueDate());
			    	dbtrac.setString(4,content);
			    	dbtrac.setInt(5,uinfo.getId());
						affectedRows = dbtrac.executeUpdate();
					
				  /** 更新记录是否成功 */
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
		 * 获取最近更新blog日志的用户列表
		 * @param uid 用户email
		 * @return RefineInfo[] 用户类别
		 * @throws SQLException 数据库异常
		 */
		public static RefineInfo[] getVisitedUser(int uid) throws SQLException {

			RefineInfo[] visitors=new RefineInfo[6];
			RefineInfo visitor=null;
			DBSource dbsrc = new DBSource();
			try {
				/** 从数据库中检索用户密码信息 */
				StringBuffer sql = new StringBuffer("select b.id,b.realname,b.photo,b.school,b.grade,b.blogid,b.blogtitle,b.blogtime,a.createtime from users_friends a,users_refined b where a.friendid=b.id and a.userid=?");
				sql.append(" order by b.blogtime desc limit 6");
				
			//	String sql = "select visitorid,realname,photo,visittime from users_visited where userid=? order by visittime desc limit 6";
				dbsrc.prepareStatement(sql.toString());
				
				dbsrc.setInt(1, uid);
				dbsrc.executeQuery();
			
				int i=0;
				while (dbsrc.next()&&(i<6)){
				  	visitor=new RefineInfo();
				    visitor.setId(dbsrc.getInt("id"));
					  visitor.setRealName(dbsrc.getString("realname"));
					  visitor.setPhoto(dbsrc.getString("photo"));
					  visitor.setSchool(dbsrc.getString("school"));
					  visitor.setGrade(dbsrc.getString("grade"));
				   	visitor.setBlogId(dbsrc.getInt("blogid"));
					  visitor.setBlogTitle(dbsrc.getString("blogtitle"));
			  
					visitors[i]=visitor;
				
					i++;
				}
			}
			finally {
			  if (dbsrc != null)
					dbsrc.close();
			}
		
			return visitors;
		}
	


		
}
