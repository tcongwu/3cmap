package com.zjiao.db;

import java.sql.SQLException;

public interface DataSet {

  	/**
  	 * �رղ���
  	 */
  	public void close() throws SQLException;
  	/**
  	 * ���Object���͵��ֶ�
  	 * @param fieldname �ֶ���
  	 * @return Object�����ֶ�ֵ
  	 */
  	public Object getObject(String fieldname) throws SQLException;
  	/**
   	 * ���ҳ����
   	 * @return page count
   	 */
  	public int getPageCount() throws SQLException;
  	/**
   	 * ���ÿҳ�ĸ���
   	 * @return page size
   	 */
  	public int getPageSize();

  	/** �ж����ݼ��Ƿ�Ϊ��
     * @return ���ݼ��Ƿ�Ϊ��
     */
    public boolean wasNull() throws SQLException;
    /** �ƶ������һ����¼
     * @return �Ƿ������һ����¼��false˵��û�м�¼�����ݼ�����
     */
    public boolean last() throws SQLException;
    /** �ƶ���ȷ��λ�õļ�¼
     * @param row ��Ҫ�ƶ�����λ��
     * @return �Ƿ��ܹ��ƶ�
     */
    public boolean moveto(int row) throws SQLException;
    /**
     *  �ƶ�����һ����¼
     *  @return �Ƿ������һ����¼
     */
    public boolean next() throws SQLException;
    /**
     * ��next����,��������ڷ�ҳ������,��ҳ��β��ʱ��᷵��false
     * @return �Ƿ���ҳ��β
     */
    public boolean pageNext() throws SQLException;
    /**
     * ���õ�ǰҳ��
     * @param pagenum ��ǰҳ
     */
    public void setPageNo(int pagenum) throws SQLException;
}