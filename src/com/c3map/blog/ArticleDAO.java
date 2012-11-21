/*
 * �������� 2006-7-30
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.blog;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
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
	   * ��ȡĳ���˵�blog������Ϣ�б�
	   * @param id  �û�ID
	   * @param curPage ��ǰҳ��
	   * @param pageSize	��ҳ��С
	   * @param arttype  blog�������
	   * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	   * @throws SQLException ���ݿ��쳣
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
			/** �����ݿ���ĳ����blog������Ϣ */
			String sql = "select id,article_name,issue_date,read_num,reply_num,reply_date from article where author_id=?";
			
			if(!StringUtils.isNull(artType)){
		  	sql=sql+" and article_type='"+artType+"'";
			}
			if(StringUtils.isNull(replyOrder)){sql=sql+" order by issue_date  desc";}
			if(!StringUtils.isNull(replyOrder)){
				if(replyOrder.equals("�������")){replyOrder="issue_date";}
				if(replyOrder.equals("����ظ�")){replyOrder="reply_date";}
				if(replyOrder.equals("���Ȼظ�")){replyOrder="reply_num";}
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
		  * �����·�ͳ�ƻ�ȡĳ���˸��·ݵ�blog������Ϣ�б�
		  * @param id  �û�ID
		  * @param curPage ��ǰҳ��
		  * @param pageSize	��ҳ��С
		  * @param arttype  blog�������
		  * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
		  * @throws SQLException ���ݿ��쳣
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
			   /** �����ݿ���ĳ����blog������Ϣ */
			   String sql = "select id,article_name,issue_date,read_num,reply_num,reply_date from article where author_id=? and issue_date>=? and issue_date<=?";
			
			   if(!StringUtils.isNull(artType)){
			   sql=sql+" and article_type='"+artType+"'";
			   }
			   if(StringUtils.isNull(replyOrder)){sql=sql+" order by issue_date  desc";}
			   if(!StringUtils.isNull(replyOrder)){
				   if(replyOrder.equals("�������")){replyOrder="issue_date";}
				   if(replyOrder.equals("����ظ�")){replyOrder="reply_date";}
				   if(replyOrder.equals("���Ȼظ�")){replyOrder="reply_num";}
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
		 * ��ȡĳһ������Ϣ
		 * @param ����ID
		 * @return ������Ϣ�б�
		 * @throws SQLException ���ݿ��쳣
		 */
		public static List getArticle(int id) throws SQLException {
			if(id==0){
				return null;
			}
			
			DBSource dbsrc = new DBSource();
			ArrayList list=new ArrayList();
			Article art=null;
			try {
			  /** �����ݿ��м����༶������Ϣ */
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
				
				//���������1
				if(list.size()>0){
					/** ���������1 */
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
	 * ��������id��ȡ��������һƪ������Ϣ�����ڸ���users_refined�е�blog��Ϣ
	 */
	public  static Article getBlogByAuthorId(UserInfo uinfo) throws SQLException {
			
				DBSource dbsrc = new DBSource();
				Article  art=null;
				try {
				  /** �����ݿ���ĳ����blog������Ϣ */
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
		 * ��������£�
		 * @param art ������Ϣ����
		 * @return	int ����״̬
		 * @throws SQLException ���ݿ��쳣
		 */
		public static int insertBlog(Article art) throws SQLException ,KeyException {
			if(art==null)
				return -1;
			 int uid=0;	
		
			DBTransaction dbtrac = new DBTransaction();
			boolean dbErrors = false;
			int affectedRows=0;
			String sql = null;
			
		/** ��ȡblog������ϢIDֵ */
		
		try{
		
		 	SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_ARTICLEID);
			 uid=keygen.getNextKey();
	  	}catch(Exception e)
		   { 
			  e.printStackTrace();            
		
		  }
			/** ��ȡϵͳ��ǰʱ�� */
			   Timestamp now=new Timestamp(System.currentTimeMillis());
		     String monn=StringUtils.exportDate(now, "yyyy��MM��");
           
			try {
       /** �����¼�¼ */
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
			  /** �����¼�¼�Ƿ�ɹ� */
			  if (affectedRows < 1) {
				  	dbErrors = true;
				  	return -1;
			    }
			  
		    	sql="insert into monarticle(id,author_id,mon_time) values (?,?,?)"; 
			    dbtrac.prepareStatement(sql);
			    dbtrac.setInt(1, uid);//��Ӧarticle���е�����id
			    dbtrac.setInt(2,art.getAuthorId());
			    dbtrac.setString(3,monn);
			    affectedRows = dbtrac.executeUpdate();
			    /** �����¼�¼�Ƿ�ɹ� */
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
						  /** ���¼�¼�Ƿ�ɹ� */
						  if (affectedRows < 1) {
								dbErrors = true;
								return -1;
						  }
						  
			  	sql="update users set score=score+? where id=?"; 
										dbtrac.prepareStatement(sql);
										dbtrac.setInt(1,SysUtils.SCORE_WRITE_BLOG);
										dbtrac.setInt(2,art.getAuthorId());
										affectedRows = dbtrac.executeUpdate();
										/** ���¼�¼�Ƿ�ɹ� */
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
				 //�ж��Ƿ�������ݿ����������֣��ع��������ݿ����������ִ��
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
			 * �޸����£�
			 * @param art ������Ϣ����
			 * @return	int ����״̬
			 * @throws SQLException ���ݿ��쳣
			 */
			public static int editBlog(Article art) throws SQLException ,KeyException {
				if(art==null)
					return -1;
				DBSource dbsrc = new DBSource();
		
				/** ��ȡϵͳ��ǰʱ�� */
				Timestamp now=new Timestamp(System.currentTimeMillis());

			try {
	     /** ���¼�¼ */
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
				  /** �����ݿ���ĳ����blog������Ϣ */
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
			 * �����û���Ҫ�������blog������Ϣ
			 * @param id ����ID
			 * @return �������
			 * @throws SQLException ���ݿ��쳣
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
		 * ɾ��ĳһ����
		 * @param id ����ID
		 * @return	int ����״̬
		 * @throws SQLException ���ݿ��쳣
		 */
		public  static int deleteArt(int id,UserInfo uinfo) throws SQLException {
		
			   DBTransaction dbtrac = new DBTransaction();
			   boolean dbErrors = false;
			   int affectedRows=0;
			   String sql = null;
			
			try {
			  /** ɾ��ĳһblog�����б� */
			   sql="delete from article where id=?";
			   dbtrac.prepareStatement(sql.toString());
			   dbtrac.setInt(1, id);
			   affectedRows = dbtrac.executeUpdate();
				  /** ɾ����¼�Ƿ�ɹ� */
			 if (affectedRows < 1) {
						dbErrors = true;
						return -1;
				  }
				  
				/** ɾ��ĳһblog����ͳ�������б� */
					affectedRows=0;  
					sql="delete from monarticle where id=?";	
		      dbtrac.prepareStatement(sql.toString());
          dbtrac.setInt(1, id);
					affectedRows = dbtrac.executeUpdate();
				 /** ɾ����¼�Ƿ�ɹ� */
			  if (affectedRows < 1) {
						 dbErrors = true;
						  return -1;
					}
				  
			  /** ɾ��ĳһblog�ظ������б� */
			     affectedRows=0;
				   sql="delete from rearticle where article_id=?";
			     dbtrac.prepareStatement(sql.toString());
					 dbtrac.setInt(1, id);
			     affectedRows = dbtrac.executeUpdate();
			
			 /** ���ɾ��ĳһblog�ظ����£�����Ӧ�Ŀ۷� */
			    	sql="update users set score=score-? where id=?"; 
						dbtrac.prepareStatement(sql);
						dbtrac.setInt(1,SysUtils.SCORE_WRITE_BLOG);
						dbtrac.setInt(2,uinfo.getId());
						affectedRows = dbtrac.executeUpdate();
				/** ���¼�¼�Ƿ�ɹ� */
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
					
				  /** ���¼�¼�Ƿ�ɹ� */
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
				   //�ж��Ƿ�������ݿ����������֣��ع��������ݿ����������ִ��
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
		 * ��ȡ�������blog��־���û��б�
		 * @param uid �û�email
		 * @return RefineInfo[] �û����
		 * @throws SQLException ���ݿ��쳣
		 */
		public static RefineInfo[] getVisitedUser(int uid) throws SQLException {

			RefineInfo[] visitors=new RefineInfo[6];
			RefineInfo visitor=null;
			DBSource dbsrc = new DBSource();
			try {
				/** �����ݿ��м����û�������Ϣ */
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
