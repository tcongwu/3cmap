package com.c3map.blog;


import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import com.zjiao.key.*;
import com.zjiao.Page;
import com.zjiao.SysUtils;
import com.zjiao.db.DBTransaction;
import com.zjiao.db.DBSource;


public class RearticleDAO {

	  public RearticleDAO(){}
	  
	  /**
	   * ��ȡ�ظ�ĳ���˵�blog������Ϣ�б�
	  
	   * @param curPage ��ǰҳ��
	   * @param pageSize	��ҳ��С
	   * @param parentId  blogԭ����id
	   * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	   * @throws SQLException ���ݿ��쳣
	   */
	  public static Page getRearticle(int artid, int curPage, int pageSize) throws SQLException {
		  if (pageSize<=0){
			   pageSize=SysUtils.PAGE_SIZE;
		   }
		
		      DBSource dbsrc = new DBSource();
		      Page page=null;
		      Rearticle  art=null;
		  try {
			/** �����ݿ���ĳ����blog������Ϣ */
		  	  String sql = "select id,author_id,author_name,article_name,article_body,article_date from rearticle where  article_id=?";
			    sql=sql+" order by article_date desc";
			    dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			    dbsrc.setInt(1,artid);
				  dbsrc.executeQuery();
		      dbsrc.setPageSize(pageSize);
			    dbsrc.setPageNo(curPage);
			    page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  while(dbsrc.pageNext()){
				  art=new Rearticle();
				  art.setId(dbsrc.getInt("id"));
				  art.setAuthorId(dbsrc.getInt("author_id"));
				  art.setAuthorName(dbsrc.getString("author_name"));
				  art.setArtName(dbsrc.getString("article_name"));
				  art.setArtBody(dbsrc.getString("article_body"));
				  art.setArtDate(dbsrc.getTimestamp("article_date"));
				 
				  page.addData(art);
			  }
		  
		  } finally {
			if (dbsrc!=null)
				  dbsrc.close();
		  }
		
		  return page;
	  }
	
	
	  /**
	   * ��ȡ�ظ�ĳ���˵�blog������Ϣ�б�
	  
	   * @param curPage ��ǰҳ��
	   * @param pageSize	��ҳ��С
	   * @param parentId  blogԭ����id
	   * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	   * @throws SQLException ���ݿ��쳣
	   */
	  public static Rearticle getRearticleById(int artid) throws SQLException {
		 
		    	DBSource dbsrc = new DBSource();
		    	Rearticle  art=null;
		  try {
			  /** �����ݿ���ĳ����blog������Ϣ */
			   	String sql = "select author_id,author_name,article_name,article_body,article_date from rearticle where id=?";
					sql=sql+" order by article_date desc";
					dbsrc.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					dbsrc.setInt(1,artid);
					dbsrc.executeQuery();
		   while(dbsrc.next()){
				  art=new Rearticle();
				 	art.setAuthorId(dbsrc.getInt("author_id"));
				  art.setAuthorName(dbsrc.getString("author_name"));
				  art.setArtName(dbsrc.getString("article_name"));
				  art.setArtBody(dbsrc.getString("article_body"));
				  art.setArtDate(dbsrc.getTimestamp("article_date"));
			  }
		  
		  } finally {
			if (dbsrc!=null)
				  dbsrc.close();
		  }
		
		  return art;
	  }
	
	
	    
	  /**
		 * ����»ظ����£�
		 * @param art ������Ϣ����
		 * @return	int ����״̬
		 * @throws SQLException ���ݿ��쳣
		 */
		public static int insertRearticle(Rearticle art) throws SQLException,KeyException {
			if(art==null)
					return -1;
			int uid=0;
		
			/** ��ȡϵͳ��ǰʱ�� */
			Timestamp now=new Timestamp(System.currentTimeMillis());
			DBTransaction dbtrac = new DBTransaction();
				boolean dbErrors = false;
				int affectedRows=0;
				String sql = null;
			
			try{
				
				SimpleKeyGen keygen = SimpleKeyGen.getInstance(KeyInit.KEY_REARTICLEID);
				 uid=keygen.getNextKey();
			}catch(Exception e)
			  { 
				 e.printStackTrace();            
			
			 }

			try {
				/** �����¼�¼ */
      	sql = "insert into rearticle(id,article_id,author_id,author_name,article_name,article_body,article_date) values(?,?,?,?,?,?,?)";
	      dbtrac.prepareStatement(sql);
		    dbtrac.setInt(1, uid);
			  dbtrac.setInt(2,art.getArticleId());
			  dbtrac.setInt(3, art.getAuthorId());
			  dbtrac.setString(4, art.getAuthorName());
			  dbtrac.setString(5,art.getArtName());
			  dbtrac.setString(6, art.getArtBody());
			  dbtrac.setTimestamp(7, now);
		
			  affectedRows = dbtrac.executeUpdate();
					  /** �����¼�¼�Ƿ�ɹ� */
					  if (affectedRows < 1) {
							dbErrors = true;
							return -1;
					  }
			   /** ����Article���еûظ��������ͻظ�ʱ�� */	  
			   sql="update article set reply_num=reply_num+1,reply_date=? where id="+art.getArticleId();
			   dbtrac.prepareStatement(sql);
			   dbtrac.setTimestamp(1, now);
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
				public static int editRearticle(Rearticle art) throws SQLException{
					if(art==null)
						return -1;
					DBSource dbsrc = new DBSource();
			
					/** ��ȡϵͳ��ǰʱ�� */
					Timestamp now=new Timestamp(System.currentTimeMillis());

					try {
		   		  /** ���¼�¼ */
				 	 String sql = "update  rearticle set article_body=?,article_date=? where id=?";
				 	 dbsrc.prepareStatement(sql);
				 	 dbsrc.setString(1, art.getArtBody());
				 	 dbsrc.setTimestamp(2,now);
				 	 dbsrc.setInt(3,art.getId());
			 	 return dbsrc.executeUpdate();
						  
					}	
					finally {
					  if (dbsrc != null)
					  dbsrc.close();
					}
				}
		
		
		
		
			/**
			 * ɾ��ĳһ�ظ�����
			 * @param id ����ID
			 * @return	int ����״̬
			 * @throws SQLException ���ݿ��쳣
			 */
			public static int deleteRearticle(int id,int artId) throws SQLException {
			
						DBTransaction dbtrac = new DBTransaction();
						boolean dbErrors = false;
						int affectedRows=0;
						String sql = null;
				
				try {
				  	/** ɾ�������� */
			      sql="delete from rearticle where id=?";
				 		dbtrac.prepareStatement(sql.toString());
				  	dbtrac.setInt(1, id);
				 	  affectedRows = dbtrac.executeUpdate();
		 			 /** �����¼�¼�Ƿ�ɹ� */
			 	 if (affectedRows < 1) {
						dbErrors = true;
						return -1;
					}

	 			 /** ����Article���еûظ������� */	  
							sql="update article set reply_num=reply_num-1 where id="+artId;
							dbtrac.prepareStatement(sql);
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
	
	
	
}
