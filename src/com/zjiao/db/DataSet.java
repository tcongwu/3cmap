package com.zjiao.db;

import java.sql.SQLException;

public interface DataSet {

  	/**
  	 * 关闭操作
  	 */
  	public void close() throws SQLException;
  	/**
  	 * 获得Object类型的字段
  	 * @param fieldname 字段名
  	 * @return Object类型字段值
  	 */
  	public Object getObject(String fieldname) throws SQLException;
  	/**
   	 * 获得页总数
   	 * @return page count
   	 */
  	public int getPageCount() throws SQLException;
  	/**
   	 * 获得每页的个数
   	 * @return page size
   	 */
  	public int getPageSize();

  	/** 判断数据集是否为空
     * @return 数据集是否为空
     */
    public boolean wasNull() throws SQLException;
    /** 移动到最后一个记录
     * @return 是否到了最后一条记录，false说明没有记录在数据集里面
     */
    public boolean last() throws SQLException;
    /** 移动到确定位置的记录
     * @param row 需要移动到的位置
     * @return 是否能够移动
     */
    public boolean moveto(int row) throws SQLException;
    /**
     *  移动到下一个记录
     *  @return 是否到了最后一条记录
     */
    public boolean next() throws SQLException;
    /**
     * 和next类似,不过如果在分页控制下,到页结尾的时候会返回false
     * @return 是否到了页结尾
     */
    public boolean pageNext() throws SQLException;
    /**
     * 设置当前页号
     * @param pagenum 当前页
     */
    public void setPageNo(int pagenum) throws SQLException;
}