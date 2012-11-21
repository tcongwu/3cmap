package com.zjiao;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * ������Ϣ����ӿ�
 * ������ģ��Ĵ�����Ϣ�����඼ʵ�ָýӿ�
 */
public interface ErrorInformation {
	
	/**
	 * ���������Ϣ
	 * @param err
	 */
	public void saveError(int err);
	
	/**
	 * �ж��Ƿ���ڴ�����Ϣ
	 * @return boolean �Ƿ����
	 */
	public boolean isEmpty();
	
	/**
	 * ��ȡ��������
	 * @return String ��������
	 */
	public String getInformation();

	/**
	 * ���ݴ���Ż�ȡ��������
	 * @param err ������
	 * @return String ��������
	 */
	public String getInformation(int err);
	
	/**
	 * �Ѵ�����Ϣ�������������
	 * @param req
	 */
	public void saveToRequest(String name, HttpServletRequest req);
	
}
