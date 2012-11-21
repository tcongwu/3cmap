package com.zjiao.db;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.zjiao.Logger;

public class DBSource implements DataSet{
  
  private Connection conn = null;
  private ResultSet rs = null;
  private Statement stmt = null;
  private PreparedStatement prestmt = null;
  private CallableStatement callstmt = null;
  private boolean isautocommit=true;

  /* ��ҳ���� */
  private int rownum = -1;
  private int page_num = 0;
  private int pageNO = 0;
  private int pagesize = 20;
  private int pagecount = -1;

  private int rsType=ResultSet.TYPE_FORWARD_ONLY;
  private int rsConcurrency=ResultSet.CONCUR_READ_ONLY;
  
  /**
   * Ĭ�ϵĹ��캯��
   *
   */
  public DBSource() {
  }

  /**
   * ʹ�����е�Connection����ʼ��
   * @param newconn a exist DBConnection
   */
  public DBSource(Connection newconn) {
    conn=newconn;
  }

  /**
   * �����Զ�Commit
   * @param autocommit �Ƿ��Զ�Commit
   * @throws java.sql.SQLException
   */
  public void setAutoCommit(boolean autocommit) throws SQLException {
    if (conn==null)
      isautocommit=autocommit;
    else
      conn.setAutoCommit(autocommit);
  }

  /**
   * ����Զ�Commit״̬
   * @return �Ƿ�autocommit
   * @throws java.sql.SQLException
   */
  public boolean getAutoCommit() throws SQLException {
    if (conn==null)
      return isautocommit;
    isautocommit=conn.getAutoCommit();
    return isautocommit;
  }

  /**
   * commit�ύ����
   * @throws java.sql.SQLException
   */
  public void commit() throws SQLException {
    if (conn!=null)
      conn.commit();
  }

  /**
   * rollback�ع�����
   * @throws java.sql.SQLException
   */
  public void rollback() throws SQLException {
    if (conn!=null)
      conn.rollback();
  }

  /**
   * ִ��֮ǰ�趨��preparedStatement
   * @return �Ƿ�ɹ�
   */
  public boolean execute() throws SQLException {

    pagecount = -1;
    if (conn == null) {
    	return false;
    }
      
    if (prestmt != null) {
    	return prestmt.execute();
    } else if (callstmt != null) {
    	return callstmt.execute();
    }

    return false;
  }

  /**
   * ��preparedstatement����Callablestatement ִ�в�ѯ���
   * @return null���߲�ѯʧ��
   */
  public ResultSet executeQuery() throws SQLException {
  	
  	//�����¼���д������ݣ����ͷ�
  	if(rs!=null){
  		rs.close();
  		rs=null;
  	}

	pagecount = -1;
	if (conn == null) {
		return null;
	}
	
	if (prestmt != null) {
		rs = prestmt.executeQuery();
	} else if (callstmt != null) {
		rs = callstmt.executeQuery();
	}
	
	return rs;
  }

  /**
   * ִ�в�ѯ���
   * @param sql ��ѯ���
   * @return null�����Ƕ�Ӧ��ResultSet
   */
  public ResultSet executeQuery(String sql) throws SQLException {
  	
	//�����¼���д������ݣ����ͷ�
	if(rs!=null){
		rs.close();
		rs=null;
	}
	
    pagecount = -1;
    if (conn == null) {
      if (getConnection() == null) {
        return null;
      }
    }
    
    if (stmt == null) {
        createStatement();
    }
    rs = stmt.executeQuery(sql);

    return rs;
  }

  /**
   *  ��preparedstatement ִ���޸���� (update,insert)
   * @return affected row
   */
  public int executeUpdate() throws SQLException {
  	
    int ret = -1;
    pagecount = -1;
    if (conn == null) {
      return -1;
    }
    
    if (prestmt != null) {
      ret = prestmt.executeUpdate();
    }else if (callstmt != null) {
      ret = callstmt.executeUpdate();
    }
    return ret;
  }

  /**
   *  ��preparedstatement ִ���޸���� (update,insert)
   * @return affected row
   */
  public int executeUpdate(String sql) throws SQLException {
  	
    int ret = -1;
    pagecount = -1;
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

  /**
   * �ж��Ƿ������ݼ�
   * @return �Ƿ����
   */
  public boolean isNullResult() {
    return (rs == null);
  }

  /** �ж����ݼ��Ƿ�Ϊ��
   * @return ���ݼ��Ƿ�Ϊ��
   */
  public boolean isEmpty() throws SQLException {
  	
    if(rs!=null){
      return (rs.isBeforeFirst() && rs.isAfterLast());
    }
    
    return true;
  }

  /** �ƶ���ȷ��λ�õļ�¼
   * @param row ��Ҫ�ƶ�����λ��
   * @return ��ǰ�α��Ƿ������ݼ�����
   */
  public boolean moveto(int row) throws SQLException {
  	
    if (rs != null) {
      if (row!=0)
        return rs.absolute(row);
      rs.beforeFirst();
      return true;
    }

    return false;
  }

  /** �ƶ�����һ����¼
   * @return �Ƿ��˵�һ����¼��false˵��û�м�¼�����ݼ�����
   */
  public boolean first() throws SQLException {

    if (rs != null) {
      return rs.first();
    }

    return false;
  }

  /** �ƶ������һ����¼
   * @return �Ƿ������һ����¼��false˵��û�м�¼�����ݼ�����
   */
  public boolean last() throws SQLException {

    if (rs != null) {
      return rs.last();
    }

    return false;
  }

  /**
   * ��ǰ�ļ�¼��,ע��������޸��α�λ��
   * @return the count of row
   */
  public int getRowNum() throws SQLException {
  	
		if ((rownum==-1)&&(rs!=null)){
      rs.last();
      rownum=rs.getRow();
      rs.beforeFirst();
      return rownum;
    }

    return rownum;
  }

  /**
   *  �ƶ�����һ����¼
   *  @return �Ƿ������һ����¼
   */
  public boolean next() throws SQLException {
  	
    if (rs != null) {
      return rs.next();
    }

    return false;
  }

  /** ��ò�ѯ��ResuletSet
   * ע��:��������ṩ����Ҫ�����ײ����ݿ��bean
   * @return ��ѯ�����ResultSet
   */
  public ResultSet getResultSet() {
    return rs;
  }

  /** ����ַ������͵��ֶ�
   * @param fieldname �ֶ���
   * @return �ַ����ֶ�ֵ
   */
  public String getString(String fieldname) throws SQLException {
  	
    if (rs != null) {
      return rs.getString(fieldname);
    }

    return null;
  }

  /** ���int���͵��ֶ�
   * @param fieldname �ֶ�����
   * @return int�����ֶ�ֵ
   */
  public int getInt(String fieldname) throws SQLException {
  	
    if (rs != null) {
      return rs.getInt(fieldname);
    }

    return 0;
  }

  /** ��ó������͵��ֶ�
   * @param fieldname �ֶ�����
   * @return Long�����ֶ�ֵ
   */
  public long getLong(String fieldname) throws SQLException {
  	
    if (rs != null) {
      return rs.getLong(fieldname);
    }
      
    return 0;
  }

  /** ���double���͵��ֶ�
   * @param fieldname �ֶ�����
   * @return double�����ֶ�ֵ
   */
  public double getDouble(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getDouble(fieldname);
    }

    return 0;
  }

  /** ��ø��������͵��ֶ�
   * @param fieldname �ֶ�����
   * @return ���������ֶ�ֵ
   */
  public float getFloat(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getFloat(fieldname);
    }

    return 0;
  }

  /**
   * ����������͵��ֶ�
   * @param fieldname �ֶ�����
   * @return ���������ֶ�ֵ
   */
  public Date getDate(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getDate(fieldname);
    }

    return null;
  }

  /** ���ʱ�����͵��ֶ�
   * @param fieldname �ֶ���
   * @return Time�����ֶ�ֵ
   */
  public Time getTime(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getTime(fieldname);
    }

    return null;
  }

  /** ���Timestamp���͵��ֶ�
   * @param fieldname �ֶ���
   * @return Timestamp�����ֶ�ֵ
   */
  public Timestamp getTimestamp(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getTimestamp(fieldname);
    }

    return null;
  }

  /** ���byte[]���͵��ֶ�
   * @param fieldname �ֶ���
   * @return byte[]�����ֶ�ֵ
   */
  public byte[] getBytes(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getBytes(fieldname);
    }

    return null;
  }

  /**
   *  ���Byte���͵��ֶ�
   * @param fieldname �ֶ���
   * @return Byte���͵��ֶ�ֵ
   */
  public byte getByte(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getByte(fieldname);
    }

    return 0;
  }

  /**
   *  ���Blob���͵��ֶ�
   * @param fieldname �ֶ���
   * @return Blob���͵��ֶ�ֵ
   */
  public Blob getBlob(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getBlob(fieldname);
    }

    return null;
  }

  /** ��ò������͵��ֶ�
   * @param fieldname �ֶ���
   * @return ���������ֶ�ֵ
   */
  public boolean getBoolean(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getBoolean(fieldname);
    }

    return false;
  }

  /**
   * ���Object���͵��ֶ�
   * @param fieldname �ֶ���
   * @return Object�����ֶ�ֵ
   */
  public Object getObject(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getObject(fieldname);
    }

    return null;
  }

  /** ����prestmt���ַ������͵Ĳ���
   * @param index λ��
   * @param buf ����ֵ
   */
  public void setString(int index, String buf) throws SQLException {
    if(prestmt!=null) {
      prestmt.setString(index, buf);
    }
  }

  /** ����long���͵Ĳ���
   * @param index λ��
   * @param value ����ֵ
   */
  public void setLong(int index, long value) throws SQLException {
    if(prestmt!=null) {
      prestmt.setLong(index, value);
    }
  }

  /** ����prestmt���������͵Ĳ���
   * @param index λ��
   * @param date ���ڲ���ֵ
   */
  public void setDate(int index, Date date) throws SQLException {
    if(prestmt!=null) {
      prestmt.setDate(index, date);
    }
  }

  /** ����prestmt��Timestamp���͵Ĳ���
   * @param index λ��
   * @param timestamp ���ڲ���ֵ
   */
  public void setTimestamp(int index, Timestamp date) throws SQLException {
    if(prestmt!=null) {
      prestmt.setTimestamp(index, date);
    }
  }

  public void setOraLong(int index, String buf) throws SQLException {
    if(prestmt!=null) {
      java.io.StringReader stringreader = new java.io.StringReader(buf);
      prestmt.setCharacterStream(index, stringreader, buf.length());
    }
  }

  /** ����prestmt��int���͵Ĳ���
   * @param index λ��
   * @param int ����ֵ
   */
  public void setInt(int index, int name) throws SQLException {
    if(prestmt!=null) {
      prestmt.setInt(index, name);
    }
  }

  /** ����prestmt��float���͵Ĳ���
   * @param index λ��
   * @param float ����ֵ
   */
  public void setFloat(int index, float name) throws SQLException {
    if(prestmt!=null) {
      prestmt.setFloat(index, name);
    }
  }

  /** ����prestmt��double���͵Ĳ���
   * @param index λ��
   * @param double ����ֵ
   */
  public void setDouble(int index, double name) throws SQLException {
    if(prestmt!=null) {
      prestmt.setDouble(index, name);
    }
  }

  /** ����prestmt��byte[]���͵Ĳ���
   * @param index λ��
   * @param byte[] ����ֵ
   */
  public void setBytes(int index, byte[] bytes) throws SQLException {
    if(prestmt!=null) {
      prestmt.setBytes(index, bytes);
    }
  }

  /** ����prestmt��object���͵Ĳ���
   * @param index λ��
   * @param object ����ֵ
   */
  public void setObject(int index, Object obj) throws SQLException {
    if(prestmt!=null) {
      prestmt.setObject(index, obj);
    }
  }

  /** �趨ȱʡ��ResultSet����
  * @param resultSetType a result set type; one of ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
  * @param resultSetConcurrency a concurrency type; one of ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
  */
  public void setDefaultResultType(int resultSetType,int resultSetConcurrency)
  {
    rsType=resultSetType;
    rsConcurrency=resultSetConcurrency;
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
   * ׼��prepareStatement����
   * @param sql ��Ҫ���õ�Sql���
   * @return a new PreparedStatement object
   */
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return prepareStatement(sql,rsType,rsConcurrency);
  }

  /**
   * ׼��prepareStatement����
   * @param sql ��Ҫ���õ�Sql���
   * @param resultSetType a result set type; one of ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
   * @param resultSetConcurrency a concurrency type; one of ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
   * @return a new PreparedStatement object
   */
  public PreparedStatement prepareStatement(String sql,int resultSetType,int resultSetConcurrency) throws SQLException {
    getConnection();
		closeResultSet();
		closePreStatement();
    prestmt = conn.prepareStatement(sql,resultSetType,resultSetConcurrency);
    return prestmt;
  }

  /**
   * ΪpreparedStatement����һ��������
   */
  public void addBatch() throws SQLException {
    if(prestmt!=null) {
      prestmt.addBatch();
    }
  }

  /**
   * Ϊstatement����һ�����������
   * @param sql һ��sql���
   */
  public void addBatch(String sql) throws SQLException {
    if (stmt == null) {
      stmt = createStatement();
    }
    stmt.addBatch(sql);
  }

  /**
   * ִ��������SQL
   * @return ����ÿһ�������ִ�н������
   */
  public int[] executeBatch() throws SQLException {
  	
    int[] ret;

    if (stmt != null) {
      return stmt.executeBatch();
    }
    
    if (prestmt != null) {
      return prestmt.executeBatch();
    }

    ret = new int[1];
    ret[0] = -1;
    return ret;
  }

  /**
   * �ر����ݿ����
   */
  public void close() {
  	try{
			closeResultSet();
			closePreStatement();
			if (conn != null) {
			  conn.close();
			  conn = null;
			}
  	}catch(SQLException e){
  		Logger.Log(Logger.LOG_ERROR,e);
  	}
  }

  /**
   * �ж����ݼ��Ƿ�Ϊ��
   */
  public boolean wasNull() throws SQLException {
  	
    if (rs != null) {
      return rs.wasNull();
    }
    
    return false;
  }

  /**
   * ���õ�ǰҳ��
   * @param pagenum ��ǰҳ
   */
  public void setPageNo(int pagenum) throws SQLException {
  	
    if (getPageSize() <= 0) {
      return;
    }
    
    if(getPageCount()==0){
    	return;
    }
    
    if (pagenum >= getPageCount()) {
      pageNO = getPageCount();
    }else if(pagenum<=0){
			pageNO = 1;
    }else{
			pageNO=pagenum;
    }
    
    moveto((pageNO-1)*getPageSize());
  }
  
  /**
   * ��ȡ��ǰҳ��
   * @return ��ǰҳ��
   */
  public int getPageNo(){
  	return pageNO;
  }
  
  /**
   * ���÷�ҳ��С
   * @param psize ��ҳ��С
   */
  public void setPageSize(int psize){
  	if(psize<=0)
  		return;
  	
  	this.pagesize=psize;
  }

  /**
   * ��next����,��������ڷ�ҳ������,��ҳ��β��ʱ��᷵��false
   * @return �Ƿ���ҳ��β
   */
  public boolean pageNext() throws SQLException {

    //����������һ��
    if (rs.isAfterLast()) {
      return false;
    }
    
    if (page_num < pagesize) {
      page_num++;
      return rs.next();
    }else{
    	page_num=0;
    }

    return false;
  }

  /**
   * ���ҳ����
   * @return page count
   */
  public int getPageCount() throws SQLException {
  	
    if (pagecount == -1) {
      pagecount = (getRowNum()-1) / pagesize+1;
      if (getRowNum()==0) pagecount = 0;
    }
    return pagecount;
  }

  /**
   * ���ÿҳ�ĸ���
   * @return page size
   */
  public int getPageSize() {
    return pagesize;
  }

  /**
   * ��������
   */
  protected void finalize() throws Throwable {
    close();
  }

  /**
   * �رռ�¼��
   * @throws SQLException ���ݿ��쳣
   */
  private void closeResultSet() throws SQLException {
    if (rs != null) {
      rs.close();
      rs = null;
    }
  }

  /**
   * �ر�Ԥ�������
   * @throws SQLException ���ݿ��쳣
   */
  private void closePreStatement() throws SQLException {
	if (prestmt != null) {
	  prestmt.close();
	  prestmt = null;
	}
  }

  /**
   * �������ݿ�����
   * @return ����
   * @throws SQLException ���ݿ��쳣
   */
  protected Connection getConnection() throws SQLException {
    if (conn == null) {
      conn = ManageConn.getConnection();
      conn.setAutoCommit(isautocommit);
    }
    return conn;
  }
}