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

  /* 翻页控制 */
  private int rownum = -1;
  private int page_num = 0;
  private int pageNO = 0;
  private int pagesize = 20;
  private int pagecount = -1;

  private int rsType=ResultSet.TYPE_FORWARD_ONLY;
  private int rsConcurrency=ResultSet.CONCUR_READ_ONLY;
  
  /**
   * 默认的构造函数
   *
   */
  public DBSource() {
  }

  /**
   * 使用已有的Connection来初始化
   * @param newconn a exist DBConnection
   */
  public DBSource(Connection newconn) {
    conn=newconn;
  }

  /**
   * 设置自动Commit
   * @param autocommit 是否自动Commit
   * @throws java.sql.SQLException
   */
  public void setAutoCommit(boolean autocommit) throws SQLException {
    if (conn==null)
      isautocommit=autocommit;
    else
      conn.setAutoCommit(autocommit);
  }

  /**
   * 获得自动Commit状态
   * @return 是否autocommit
   * @throws java.sql.SQLException
   */
  public boolean getAutoCommit() throws SQLException {
    if (conn==null)
      return isautocommit;
    isautocommit=conn.getAutoCommit();
    return isautocommit;
  }

  /**
   * commit提交数据
   * @throws java.sql.SQLException
   */
  public void commit() throws SQLException {
    if (conn!=null)
      conn.commit();
  }

  /**
   * rollback回滚数据
   * @throws java.sql.SQLException
   */
  public void rollback() throws SQLException {
    if (conn!=null)
      conn.rollback();
  }

  /**
   * 执行之前设定的preparedStatement
   * @return 是否成功
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
   * 用preparedstatement或者Callablestatement 执行查询语句
   * @return null或者查询失败
   */
  public ResultSet executeQuery() throws SQLException {
  	
  	//如果记录集中存在数据，则释放
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
   * 执行查询语句
   * @param sql 查询语句
   * @return null或者是对应的ResultSet
   */
  public ResultSet executeQuery(String sql) throws SQLException {
  	
	//如果记录集中存在数据，则释放
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
   *  用preparedstatement 执行修改语句 (update,insert)
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
   *  用preparedstatement 执行修改语句 (update,insert)
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
   * 判断是否有数据集
   * @return 是否存在
   */
  public boolean isNullResult() {
    return (rs == null);
  }

  /** 判断数据集是否为空
   * @return 数据集是否为空
   */
  public boolean isEmpty() throws SQLException {
  	
    if(rs!=null){
      return (rs.isBeforeFirst() && rs.isAfterLast());
    }
    
    return true;
  }

  /** 移动到确定位置的记录
   * @param row 需要移动到的位置
   * @return 当前游标是否在数据集里面
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

  /** 移动到第一个记录
   * @return 是否到了第一条记录，false说明没有记录在数据集里面
   */
  public boolean first() throws SQLException {

    if (rs != null) {
      return rs.first();
    }

    return false;
  }

  /** 移动到最后一个记录
   * @return 是否到了最后一条记录，false说明没有记录在数据集里面
   */
  public boolean last() throws SQLException {

    if (rs != null) {
      return rs.last();
    }

    return false;
  }

  /**
   * 当前的记录数,注意这个会修改游标位置
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
   *  移动到下一个记录
   *  @return 是否到了最后一条记录
   */
  public boolean next() throws SQLException {
  	
    if (rs != null) {
      return rs.next();
    }

    return false;
  }

  /** 获得查询的ResuletSet
   * 注意:这个函数提供给需要操作底层数据库的bean
   * @return 查询结果的ResultSet
   */
  public ResultSet getResultSet() {
    return rs;
  }

  /** 获得字符串类型的字段
   * @param fieldname 字段名
   * @return 字符串字段值
   */
  public String getString(String fieldname) throws SQLException {
  	
    if (rs != null) {
      return rs.getString(fieldname);
    }

    return null;
  }

  /** 获得int类型的字段
   * @param fieldname 字段名称
   * @return int类型字段值
   */
  public int getInt(String fieldname) throws SQLException {
  	
    if (rs != null) {
      return rs.getInt(fieldname);
    }

    return 0;
  }

  /** 获得长整类型的字段
   * @param fieldname 字段名称
   * @return Long类型字段值
   */
  public long getLong(String fieldname) throws SQLException {
  	
    if (rs != null) {
      return rs.getLong(fieldname);
    }
      
    return 0;
  }

  /** 获得double类型的字段
   * @param fieldname 字段名称
   * @return double类型字段值
   */
  public double getDouble(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getDouble(fieldname);
    }

    return 0;
  }

  /** 获得浮点数类型的字段
   * @param fieldname 字段名称
   * @return 浮点类型字段值
   */
  public float getFloat(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getFloat(fieldname);
    }

    return 0;
  }

  /**
   * 获得日期类型的字段
   * @param fieldname 字段名称
   * @return 日期类型字段值
   */
  public Date getDate(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getDate(fieldname);
    }

    return null;
  }

  /** 获得时间类型的字段
   * @param fieldname 字段名
   * @return Time类型字段值
   */
  public Time getTime(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getTime(fieldname);
    }

    return null;
  }

  /** 获得Timestamp类型的字段
   * @param fieldname 字段名
   * @return Timestamp类型字段值
   */
  public Timestamp getTimestamp(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getTimestamp(fieldname);
    }

    return null;
  }

  /** 获得byte[]类型的字段
   * @param fieldname 字段名
   * @return byte[]类型字段值
   */
  public byte[] getBytes(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getBytes(fieldname);
    }

    return null;
  }

  /**
   *  获得Byte类型的字段
   * @param fieldname 字段名
   * @return Byte类型的字段值
   */
  public byte getByte(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getByte(fieldname);
    }

    return 0;
  }

  /**
   *  获得Blob类型的字段
   * @param fieldname 字段名
   * @return Blob类型的字段值
   */
  public Blob getBlob(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getBlob(fieldname);
    }

    return null;
  }

  /** 获得布尔类型的字段
   * @param fieldname 字段名
   * @return 布尔类型字段值
   */
  public boolean getBoolean(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getBoolean(fieldname);
    }

    return false;
  }

  /**
   * 获得Object类型的字段
   * @param fieldname 字段名
   * @return Object类型字段值
   */
  public Object getObject(String fieldname) throws SQLException {

    if (rs != null) {
      return rs.getObject(fieldname);
    }

    return null;
  }

  /** 设置prestmt的字符串类型的参数
   * @param index 位置
   * @param buf 参数值
   */
  public void setString(int index, String buf) throws SQLException {
    if(prestmt!=null) {
      prestmt.setString(index, buf);
    }
  }

  /** 设置long类型的参数
   * @param index 位置
   * @param value 参数值
   */
  public void setLong(int index, long value) throws SQLException {
    if(prestmt!=null) {
      prestmt.setLong(index, value);
    }
  }

  /** 设置prestmt的日期类型的参数
   * @param index 位置
   * @param date 日期参数值
   */
  public void setDate(int index, Date date) throws SQLException {
    if(prestmt!=null) {
      prestmt.setDate(index, date);
    }
  }

  /** 设置prestmt的Timestamp类型的参数
   * @param index 位置
   * @param timestamp 日期参数值
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

  /** 设置prestmt的int类型的参数
   * @param index 位置
   * @param int 参数值
   */
  public void setInt(int index, int name) throws SQLException {
    if(prestmt!=null) {
      prestmt.setInt(index, name);
    }
  }

  /** 设置prestmt的float类型的参数
   * @param index 位置
   * @param float 参数值
   */
  public void setFloat(int index, float name) throws SQLException {
    if(prestmt!=null) {
      prestmt.setFloat(index, name);
    }
  }

  /** 设置prestmt的double类型的参数
   * @param index 位置
   * @param double 参数值
   */
  public void setDouble(int index, double name) throws SQLException {
    if(prestmt!=null) {
      prestmt.setDouble(index, name);
    }
  }

  /** 设置prestmt的byte[]类型的参数
   * @param index 位置
   * @param byte[] 参数值
   */
  public void setBytes(int index, byte[] bytes) throws SQLException {
    if(prestmt!=null) {
      prestmt.setBytes(index, bytes);
    }
  }

  /** 设置prestmt的object类型的参数
   * @param index 位置
   * @param object 参数值
   */
  public void setObject(int index, Object obj) throws SQLException {
    if(prestmt!=null) {
      prestmt.setObject(index, obj);
    }
  }

  /** 设定缺省的ResultSet类型
  * @param resultSetType a result set type; one of ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
  * @param resultSetConcurrency a concurrency type; one of ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
  */
  public void setDefaultResultType(int resultSetType,int resultSetConcurrency)
  {
    rsType=resultSetType;
    rsConcurrency=resultSetConcurrency;
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
   * 准备prepareStatement方法
   * @param sql 需要调用的Sql语句
   * @return a new PreparedStatement object
   */
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return prepareStatement(sql,rsType,rsConcurrency);
  }

  /**
   * 准备prepareStatement方法
   * @param sql 需要调用的Sql语句
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
   * 为preparedStatement增加一个批处理
   */
  public void addBatch() throws SQLException {
    if(prestmt!=null) {
      prestmt.addBatch();
    }
  }

  /**
   * 为statement增加一个批处理语句
   * @param sql 一条sql语句
   */
  public void addBatch(String sql) throws SQLException {
    if (stmt == null) {
      stmt = createStatement();
    }
    stmt.addBatch(sql);
  }

  /**
   * 执行批处理SQL
   * @return 返回每一条命令的执行结果数组
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
   * 关闭数据库操作
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
   * 判断数据集是否为空
   */
  public boolean wasNull() throws SQLException {
  	
    if (rs != null) {
      return rs.wasNull();
    }
    
    return false;
  }

  /**
   * 设置当前页号
   * @param pagenum 当前页
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
   * 获取当前页号
   * @return 当前页号
   */
  public int getPageNo(){
  	return pageNO;
  }
  
  /**
   * 设置分页大小
   * @param psize 分页大小
   */
  public void setPageSize(int psize){
  	if(psize<=0)
  		return;
  	
  	this.pagesize=psize;
  }

  /**
   * 和next类似,不过如果在分页控制下,到页结尾的时候会返回false
   * @return 是否到了页结尾
   */
  public boolean pageNext() throws SQLException {

    //如果到了最后一项
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
   * 获得页总数
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
   * 获得每页的个数
   * @return page size
   */
  public int getPageSize() {
    return pagesize;
  }

  /**
   * 结束操作
   */
  protected void finalize() throws Throwable {
    close();
  }

  /**
   * 关闭记录集
   * @throws SQLException 数据库异常
   */
  private void closeResultSet() throws SQLException {
    if (rs != null) {
      rs.close();
      rs = null;
    }
  }

  /**
   * 关闭预编译语句
   * @throws SQLException 数据库异常
   */
  private void closePreStatement() throws SQLException {
	if (prestmt != null) {
	  prestmt.close();
	  prestmt = null;
	}
  }

  /**
   * 建立数据库连接
   * @return 连接
   * @throws SQLException 数据库异常
   */
  protected Connection getConnection() throws SQLException {
    if (conn == null) {
      conn = ManageConn.getConnection();
      conn.setAutoCommit(isautocommit);
    }
    return conn;
  }
}