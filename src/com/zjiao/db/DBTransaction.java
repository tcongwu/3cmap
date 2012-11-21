package com.zjiao.db;

import com.zjiao.Logger;
import java.sql.*;

/**
 * <p>Title: ͨ��������</p>
 * <p>Description: ʵ����������������Ͳ����Ĺ���</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Tsinghua Univ.</p>
 * @author Lee Bin
 * @version 1.0
 */

public class DBTransaction {
	/** ��������Ҫִ�еı������ */
	private Statement stmt = null;
  /** ��������Ҫִ�е�Ԥ������� */
  private PreparedStatement prestmt=null;
  /** �������ݿ���������� */
  private Connection conn = null;

  private int rsType=ResultSet.TYPE_FORWARD_ONLY;
  private int rsConcurrency=ResultSet.CONCUR_READ_ONLY;

  /**
   * Ĭ�Ϲ��캯��
   */
  public DBTransaction() {
  }

  /**
   * ���Զ��ύ������false
   */
  private void disableAutoCommit() throws SQLException {
	  DatabaseMetaData dbmeta = conn.getMetaData();
	  if (dbmeta.supportsTransactions()) {
				conn.setAutoCommit(false);
	  }
  }

  /**
   * commit����
   * @throws java.sql.SQLException
   */
  public void commit() throws java.sql.SQLException {
		if (conn!=null)
			conn.commit();
  }

  /**
   * rollback����
   * @throws java.sql.SQLException
   */
  public void rollback() throws java.sql.SQLException {
		if (conn!=null)
			conn.rollback();
  }

  /**
   * ׼��Ԥ������䷽��
   * @param sql ��Ҫ���õ�Sql���
   * @param resultSetType ���������; one of ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
   * @param resultSetConcurrency ͬ����ʽ; one of ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
   * @return PreparedStatement Ԥ�������
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
   * ׼��Ԥ������䷽��
   * @param sql ��Ҫ���õ�Sql���
   * @return PreparedStatement Ԥ�������
   * @throws java.sql.SQLException
   */
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
  }

  /**
   *  ��Ԥ�������ִ���޸����(update,insert)
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
   *  ��preparedstatement ִ���޸���� (update,insert)
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

  /** ����prestmt���ַ������͵Ĳ���
   * @param index λ��
   * @param buf ����ֵ
   * @throws java.sql.SQLException
   */
  public void setString(int index, String buf) throws SQLException {
    prestmt.setString(index, buf);
  }

  /** ����long���͵Ĳ���
   * @param index λ��
   * @param value ����ֵ
   * @throws java.sql.SQLException
   */
  public void setLong(int index, long value) throws SQLException {
    prestmt.setLong(index, value);
  }

  /** ����prestmt���������͵Ĳ���
   * @param index λ��
   * @param date ���ڲ���ֵ
   * @throws java.sql.SQLException
   */
  public void setDate(int index, Date date) throws SQLException {
    prestmt.setDate(index, date);
  }

  /** ����prestmt���������͵Ĳ���
   * @param index λ��
   * @param name ��������ֵ
   * @throws java.sql.SQLException
   */
  public void setInt(int index, int name) throws SQLException {
    prestmt.setInt(index, name);
  }

  /** ����prestmt��˫���ȸ������͵Ĳ���
   * @param index λ��
   * @param name ˫���ȸ������ֵ
   * @throws java.sql.SQLException
   */
  public void setDouble(int index, double name) throws SQLException {
    prestmt.setDouble(index, name);
  }

  /** ����prestmt��byte[]���͵Ĳ���
   * @param index λ��
   * @param bytes byte[]����ֵ
   * @throws java.sql.SQLException
   */
  public void setBytes(int index, byte[] bytes) throws SQLException {
    prestmt.setBytes(index, bytes);
  }

  /** ����prestmt�Ķ������͵Ĳ���
   * @param index λ��
   * @param obj �������ֵ
   * @throws java.sql.SQLException
   */
  public void setObject(int index, Object obj) throws SQLException {
    prestmt.setObject(index, obj);
  }

  /** ����prestmt��Timestamp���͵Ĳ���
   * @param index λ��
   * @param tmstamp �������ֵ
   * @throws java.sql.SQLException
   */
  public void setTimestamp(int index, Timestamp tmstamp) throws SQLException {
    prestmt.setTimestamp(index, tmstamp);
  }

  /**
   *  ����Statement����
   * @return a new Statement object
   */
  public Statement createStatement() throws SQLException {
	  return createStatement(rsType,rsConcurrency);
  }

  /**
   *  ָ��Resultset���͵�createStatement����
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
   * �������ݿ�����
   * @return ����
   * @throws SQLException ���ݿ��쳣
   */
  private Connection getConnection() throws SQLException {
		if (conn == null) {
		  conn = ManageConn.getConnection();
		  disableAutoCommit();
		}
		return conn;
  }

  /**
   * �ͷ���Դ
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