package com.zjiao.db;

import com.zjiao.Logger;
import java.sql.*;

/**
 * <p>Title: 通用事务处理</p>
 * <p>Description: 实现事务处理过程中语句和参数的管理</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Tsinghua Univ.</p>
 * @author Lee Bin
 * @version 1.0
 */

public class DBTransaction {
	/** 事务中需要执行的编译语句 */
	private Statement stmt = null;
  /** 事务中需要执行的预编译语句 */
  private PreparedStatement prestmt=null;
  /** 进行数据库操作的连接 */
  private Connection conn = null;

  private int rsType=ResultSet.TYPE_FORWARD_ONLY;
  private int rsConcurrency=ResultSet.CONCUR_READ_ONLY;

  /**
   * 默认构造函数
   */
  public DBTransaction() {
  }

  /**
   * 把自动提交语句设成false
   */
  private void disableAutoCommit() throws SQLException {
	  DatabaseMetaData dbmeta = conn.getMetaData();
	  if (dbmeta.supportsTransactions()) {
				conn.setAutoCommit(false);
	  }
  }

  /**
   * commit数据
   * @throws java.sql.SQLException
   */
  public void commit() throws java.sql.SQLException {
		if (conn!=null)
			conn.commit();
  }

  /**
   * rollback数据
   * @throws java.sql.SQLException
   */
  public void rollback() throws java.sql.SQLException {
		if (conn!=null)
			conn.rollback();
  }

  /**
   * 准备预编译语句方法
   * @param sql 需要调用的Sql语句
   * @param resultSetType 结果集类型; one of ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
   * @param resultSetConcurrency 同步方式; one of ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
   * @return PreparedStatement 预编译语句
   * @throws java.sql.SQLException
   */
  public PreparedStatement prepareStatement(String sql, int resultSetType,
                                            int resultSetConcurrency) throws SQLException {
		getConnection();
    if(prestmt!=null){
      prestmt.close();
      prestmt=null;
    }
    prestmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    return prestmt;
  }

  /**
   * 准备预编译语句方法
   * @param sql 需要调用的Sql语句
   * @return PreparedStatement 预编译语句
   * @throws java.sql.SQLException
   */
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
  }

  /**
   *  用预编译语句执行修改语句(update,insert)
   * @return affected row
   * @throws java.sql.SQLException
   */
  public int executeUpdate() throws SQLException {
    int ret = -1;
    if (conn == null) {
      return -1;
    }
    if (prestmt != null) {
      ret = prestmt.executeUpdate();
    }
    return ret;
  }

  /**
   *  用preparedstatement 执行修改语句 (update,insert)
   * @return affected row
   */
  public int executeUpdate(String sql) throws SQLException {
  	
	int ret = -1;
	if (conn == null) {
	  if (getConnection() == null) {
			return -1;
	  }
	}
    
	if (stmt == null) {
	  createStatement();
	}
	ret = stmt.executeUpdate(sql);
    
	return ret;
  }

  /** 设置prestmt的字符串类型的参数
   * @param index 位置
   * @param buf 参数值
   * @throws java.sql.SQLException
   */
  public void setString(int index, String buf) throws SQLException {
    prestmt.setString(index, buf);
  }

  /** 设置long类型的参数
   * @param index 位置
   * @param value 参数值
   * @throws java.sql.SQLException
   */
  public void setLong(int index, long value) throws SQLException {
    prestmt.setLong(index, value);
  }

  /** 设置prestmt的日期类型的参数
   * @param index 位置
   * @param date 日期参数值
   * @throws java.sql.SQLException
   */
  public void setDate(int index, Date date) throws SQLException {
    prestmt.setDate(index, date);
  }

  /** 设置prestmt的整数类型的参数
   * @param index 位置
   * @param name 整数参数值
   * @throws java.sql.SQLException
   */
  public void setInt(int index, int name) throws SQLException {
    prestmt.setInt(index, name);
  }

  /** 设置prestmt的双精度浮点类型的参数
   * @param index 位置
   * @param name 双精度浮点参数值
   * @throws java.sql.SQLException
   */
  public void setDouble(int index, double name) throws SQLException {
    prestmt.setDouble(index, name);
  }

  /** 设置prestmt的byte[]类型的参数
   * @param index 位置
   * @param bytes byte[]参数值
   * @throws java.sql.SQLException
   */
  public void setBytes(int index, byte[] bytes) throws SQLException {
    prestmt.setBytes(index, bytes);
  }

  /** 设置prestmt的对象类型的参数
   * @param index 位置
   * @param obj 对象参数值
   * @throws java.sql.SQLException
   */
  public void setObject(int index, Object obj) throws SQLException {
    prestmt.setObject(index, obj);
  }

  /** 设置prestmt的Timestamp类型的参数
   * @param index 位置
   * @param tmstamp 对象参数值
   * @throws java.sql.SQLException
   */
  public void setTimestamp(int index, Timestamp tmstamp) throws SQLException {
    prestmt.setTimestamp(index, tmstamp);
  }

  /**
   *  创建Statement方法
   * @return a new Statement object
   */
  public Statement createStatement() throws SQLException {
	  return createStatement(rsType,rsConcurrency);
  }

  /**
   *  指定Resultset类型的createStatement方法
   * @param resultSetType a result set type; one of ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
   * @param resultSetConcurrency a concurrency type; one of ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
   * @return a new Statement object
   */
  public Statement createStatement(int resultSetType,int resultSetConcurrency) throws SQLException {
		getConnection();
		stmt = conn.createStatement(resultSetType,resultSetConcurrency);
		return stmt;
  }

  /**
   * 建立数据库连接
   * @return 连接
   * @throws SQLException 数据库异常
   */
  private Connection getConnection() throws SQLException {
		if (conn == null) {
		  conn = ManageConn.getConnection();
		  disableAutoCommit();
		}
		return conn;
  }

  /**
   * 释放资源
   */
  public void close() {
    try {
      if (prestmt != null) {
        prestmt.close();
				prestmt = null;
      }
		  if (conn != null) {
				conn.close();
				conn = null;
		  }
    }
    catch (SQLException ex) {
      Logger.Log(Logger.LOG_ERROR, ex);
    }
  }
}