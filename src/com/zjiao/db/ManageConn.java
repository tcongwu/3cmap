package com.zjiao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import javax.servlet.ServletContext;

/**
 * <p>Title:���ݿ����� </p>
 * <p>Description: ��װ���ݿ�����</p>
 * @author LiBin
 * @version 1.0
 */

public class ManageConn {
	
	public final static String DBN = "zjiao_dbconn";
    
	private static ServletContext context;
    
	/**
	 * ��ʼ����������
	 * @param servletContext servlet��������
	 */
	public static void init(ServletContext servletContext) {
		if(servletContext!=null)
			context = servletContext;
	}
	
	/**
	 * ��ȡ�����ݿ�����Ӿ��
	 * @return Connection һ�����ݿ�����
	 * @throws SQLException �׳����ݿ��쳣
	 */
	public static Connection getConnection() throws SQLException{
		DataSource dataSource = (DataSource)context.getAttribute(DBN);
		return dataSource.getConnection();		
	}

	/**
	 * ��Դ�ͷ�
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
	 * ͬʱ�ͷŶ����Դ
	 * @param obj1 ����1
	 * @param obj2 ����2
	 * @param obj3 ����3
	 */
	public static void close(Object obj1,Object obj2,Object obj3){
		close(obj1);close(obj2);close(obj3);
	}
}