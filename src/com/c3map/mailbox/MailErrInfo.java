/*
 * �������� 2006-8-2
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.mailbox;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import com.zjiao.ErrorInformation;
import com.zjiao.util.StringUtils;

public class  MailErrInfo implements ErrorInformation {
	private static Hashtable errTable=new Hashtable();
	private static boolean isInit=false;
	
	private int errNumber=-1;

	public static final int ERR_UNKNOWN=0; //δ֪����
	public static final int DATABASE_ERR=1; //���ݿ����
	public static final int OPERATION_NOT_ALLOW=2; //�Ƿ�����
	public static final int NEED_LOGIN=3; //�û���Ҫ��¼
	public static final int CANT_ACCESS=4; //û���㹻Ȩ��
	public static final int USER_INVALID=5; //�û���Ч
	public static final int PARAM_ERROR=6; //��������
	public static final int GENERATE_KEY_FAILED=7; //�ڲ�������������ʧ��
	public static final int NEEDED_FIELD_NOT_NULL=8; //����д���еı�����
	
	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public MailErrInfo() {
		if(!isInit)
			initErrTable();
	}

	/* ���� Javadoc��
	 * @see com.zjiao.ErrorInformation#saveError(int)
	 */
	public void saveError(int err) {
		if(err<0)
			return;
		
		this.errNumber=err;
	}

	/* ���� Javadoc��
	 * @see com.zjiao.ErrorInformation#isEmpty()
	 */
	public boolean isEmpty() {
		if(errNumber==-1)
			return true;

		return false;
	}

	/* ���� Javadoc��
	 * @see com.zjiao.ErrorInformation#getInformation()
	 */
	public String getInformation() {
		
		if(errNumber==-1)
			return "";
		
		String information=null;

		try {
			
			if (errTable != null) {
				information = (String) errTable.get(new Integer(errNumber));
			}
		  	
			if (!StringUtils.isNull(information))
				return information;
				
		} catch (Exception ex) {
		}
		
		return "";
	}
	
	/* ���� Javadoc��
	 * @see com.zjiao.ErrorInformation#getInformation()
	 */
	public String getInformation(int err) {
		
		if(err==-1)
			return "";
		
		String information=null;

		try {
			
			if (errTable != null) {
				information = (String) errTable.get(new Integer(err));
			}
		  	
			if (!StringUtils.isNull(information))
				return information;
				
		} catch (Exception ex) {
		}
		
		return "";
	}

	/* ���� Javadoc��
	 * @see com.zjiao.ErrorInformation#saveToRequest()
	 */
	public void saveToRequest(String name, HttpServletRequest req){
		req.setAttribute(name,this);
	}

	/**
	 * ��ʼ��������Ϣ��
	 */
	private void initErrTable(){
		
		errTable.clear();

		/* ���������ɫģ��Ĵ�����Ϣ�� */
		errTable.put(new Integer(ERR_UNKNOWN),"δ֪����");
		errTable.put(new Integer(DATABASE_ERR),"���ݿ����");
		errTable.put(new Integer(OPERATION_NOT_ALLOW),"�Ƿ�����");
		errTable.put(new Integer(NEED_LOGIN),"�û���Ҫ��¼");
		errTable.put(new Integer(CANT_ACCESS),"û���㹻Ȩ��");
		errTable.put(new Integer(USER_INVALID),"�û���Ч");
		errTable.put(new Integer(PARAM_ERROR),"��������");
		errTable.put(new Integer(GENERATE_KEY_FAILED),"�ڲ�������������ʧ��");
		errTable.put(new Integer(NEEDED_FIELD_NOT_NULL),"����д���еı�����");

		isInit=true;

	}

}
