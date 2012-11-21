package com.zjiao.key;

import java.sql.Timestamp;

import com.zjiao.db.DBSource;

import com.zjiao.Logger;

/**
 * @author Lee Bin
 *
 * 键类信息的初始化
 */
public class KeyInit {
	public final static String KEY_USERID="userid";
	public final static String KEY_MAILID="mailboxid";
	public final static String KEY_SENDID="sendboxid";
	
	public final static String KEY_ARTICLEID="articleid";
	public final static String KEY_REARTICLEID="rearticleid";
	
	public final static String KEY_ACTIVITYID="activityid";
	
	protected final static int MIN=0;
	protected final static int MAX=1;
	protected final static int CURVALUE=2;
	protected final static int STEP=3;
	protected final static int POOLSIZE=4;

	/**
	 * 默认的构造函数
	 */
	public KeyInit() {
	}
	
	/**
	 * 获取键值信息
	 * @param keyName 键值名称
	 * @return int[] 键值信息数组
	 */
	protected static int[] getKeyInfo(String keyName){
		if(keyName==null)
			return null;
		
		DBSource dbsrc = new DBSource();
		int[] result=null;
		try {
		  /** 从数据库中检索键值信息 */
		  String sql = "select kmin,kmax,curvalue,step,poolsize from keytable where keyname=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, keyName);
		  dbsrc.executeQuery();
		  if (dbsrc.next()) {
		  	result=new int[5];
		  	result[MIN]=dbsrc.getInt("kmin");
				result[MAX]=dbsrc.getInt("kmax");
				result[CURVALUE]=dbsrc.getInt("curvalue");
				result[STEP]=dbsrc.getInt("step");
				result[POOLSIZE]=dbsrc.getInt("poolsize");
			  	
				return result;
		  }
		  
		  sql="insert into keytable (keyname,kmin,kmax,curvalue,step,poolsize,createtime) values(?,1,2147483646,1,1,20,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, keyName);
		  dbsrc.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
		  if(dbsrc.executeUpdate()==1){
				result=new int[5];
				result[MIN]=1;
				result[MAX]=2147483646;
				result[CURVALUE]=1;
				result[STEP]=1;
				result[POOLSIZE]=20;
			  	
				return result;
		  }
		  
		  return null;
		} catch (Exception ex) {
		  Logger.Log(Logger.LOG_ERROR, ex);
		  return null;
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
	}
	
	/**
	 * 获取新的键值缓冲池
	 * @param poolSize 缓冲池大小
	 * @param keyName 键值名称
	 * @return 是否成功
	 */
	protected static boolean getNewKey(int poolSize, String keyName){
		DBSource dbsrc = new DBSource();
		try {
		  /** 更新数据库中的键值信息 */
		  String sql = "update keytable set curvalue=curvalue+"+poolSize+" where keyname=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, keyName);
		  int affectedRows = dbsrc.executeUpdate();
		  /* 是否成功 */
		  if (affectedRows == 1)
				return true;
		  else
				return false;
		}
		catch (Exception ex) {
		  Logger.Log(Logger.LOG_ERROR, ex);
		  return false;
		}
		finally {
		  if (dbsrc != null)
			dbsrc.close();
		}
	}
}
