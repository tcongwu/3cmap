package com.zjiao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import javax.servlet.ServletContext;

/**
 * <p>Title:数据库连接 </p>
 * <p>Description: 包装数据库连接</p>
 * @author LiBin
 * @version 1.0
 */

public class ManageConn {
	
	public final static String DBN = "zjiao_dbconn";
    
	private static ServletContext context;
    
	/**
	 * 初始化环境变量
	 * @param servletContext servlet环境变量
	 */
	public static void init(ServletContext servletContext) {
		if(servletContext!=null)
			context = servletContext;
	}
	
	/**
	 * 获取到数据库的连接句柄
	 * @return Connection 一个数据库连接
	 * @throws SQLException 抛出数据库异常
	 */
	public static Connection getConnection() throws SQLException{
		DataSource dataSource = (DataSource)context.getAttribute(DBN);
		return dataSource.getConnection();		
	}

	/**
	 * 资源释放
	 * @param obj
	 */
	public static void close(Object obj){
		if(obj==null)
			return;
		try{
			if(obj instanceof Connection)
				((Connection)obj).close();
			if(obj instanceof Statement)
				((Statement)obj).close();
			if(obj instanceof ResultSet)
				((ResultSet)obj).close();
		}catch(Exception e){}
		obj = null;
	}
	
	/**
	 * 同时释放多个资源
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @param obj3 对象3
	 */
	public static void close(Object obj1,Object obj2,Object obj3){
		close(obj1);close(obj2);close(obj3);
	}
}