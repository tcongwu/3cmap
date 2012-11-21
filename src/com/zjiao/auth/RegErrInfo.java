package com.zjiao.auth;

import com.zjiao.ErrorInformation;
import com.zjiao.util.StringUtils;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * ��֤ע��ģ��Ĵ�����Ϣ������
 */
public class RegErrInfo implements ErrorInformation {
	
	private static Hashtable errTable=new Hashtable();
	private static boolean isInit=false;
	
	private int errNumber=-1;

	public static final int ERR_UNKNOWN=0; //δ֪����
	public static final int PASSWORD_NOT_NULL=1; //��¼���벻��Ϊ��
	public static final int PASSWORD_LENGTH_NOT_SUITABLE=2; //���볤�ȱ������6��20���ַ�֮��
	public static final int EMAIL_ERROR=3; //��������ȷ��email
	public static final int EMAIL_EXIST=4; //email�Ѿ�����
	public static final int VERIFYCODE_ERROR=5; //ע����֤���������
	public static final int DATABASE_ERR=6; //���ݿ����
	public static final int GENERATE_KEY_FAILED=7; //�ڲ�������������ʧ��
	public static final int OPERATION_NOT_ALLOW=8; //�Ƿ�����������δ��¼
	public static final int SEX_NOT_NULL=9; //�Ա���Ϊ��
	public static final int CONFIRM_PASSWORD_ERROR=10; //ȷ���������
	public static final int PASSWORD_ERROR=11; //���벻��ȷ
	public static final int EMAIL_OR_PASSWORD_ERROR=12; //email���������
	public static final int NEED_LOGIN=13; //�û���Ҫ��¼
	public static final int CANT_ACCESS=14; //û���㹻Ȩ��
	public static final int USER_INVALID=15; //�û���Ч
	public static final int OLD_PASSWORD_ERROR=16; //ԭ�������������
	public static final int EMAIL_NOT_NULL=17; //email����Ϊ��
	public static final int USER_INACTIVE=18; //�û�δ�����ͨ��ϵͳ���͵�������ļ����ʼ������ʺ�
	public static final int REALNAME_ERROR=19; //��������Ϊ�գ���ֻ���Ǻ���
	public static final int ROLE_TYPE_NOT_NULL=20; //��ѧ��������Ϊ��
	public static final int PROVINCE_NOT_NULL=21; //��ѧУ����ʡ�С�����Ϊ��
	public static final int SCHOOL_NOT_NULL=22; //��ѧУ������Ϊ��
	public static final int ACTIVE_CODE_EXPIRE=23; //���������ѹ���
	public static final int ACTIVE_CODE_ERR=24; //�������Ӵ���
	
	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public RegErrInfo() {
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

		/* ����ע����֤ģ��Ĵ�����Ϣ�� */
		errTable.put(new Integer(ERR_UNKNOWN),"δ֪����");
		errTable.put(new Integer(PASSWORD_NOT_NULL),"��¼���벻��Ϊ��");
		errTable.put(new Integer(PASSWORD_LENGTH_NOT_SUITABLE),"���볤�ȱ������6��20���ַ�֮��");
		errTable.put(new Integer(EMAIL_ERROR),"��������ȷ��email");
		errTable.put(new Integer(EMAIL_EXIST),"email�Ѿ�����");
		errTable.put(new Integer(VERIFYCODE_ERROR),"ע����֤���������");
		errTable.put(new Integer(DATABASE_ERR),"���ݿ����");
		errTable.put(new Integer(GENERATE_KEY_FAILED),"�ڲ�������������ʧ��");
		errTable.put(new Integer(OPERATION_NOT_ALLOW),"�Ƿ�����������δ��¼");
		errTable.put(new Integer(SEX_NOT_NULL),"�Ա���Ϊ��");
		errTable.put(new Integer(CONFIRM_PASSWORD_ERROR),"ȷ���������");
		errTable.put(new Integer(PASSWORD_ERROR),"���벻��ȷ");
		errTable.put(new Integer(EMAIL_OR_PASSWORD_ERROR),"email���������");
		errTable.put(new Integer(NEED_LOGIN),"�û���Ҫ��¼");
		errTable.put(new Integer(CANT_ACCESS),"û���㹻Ȩ��");
		errTable.put(new Integer(USER_INVALID),"�û���Ч");
		errTable.put(new Integer(OLD_PASSWORD_ERROR),"ԭ�������������");
		errTable.put(new Integer(EMAIL_NOT_NULL),"email����Ϊ��");
		errTable.put(new Integer(USER_INACTIVE),"�û�δ�����ͨ��ϵͳ���͵�������ļ����ʼ������ʺ�");
		errTable.put(new Integer(ROLE_TYPE_NOT_NULL),"��ѧ��������Ϊ��");
		errTable.put(new Integer(PROVINCE_NOT_NULL),"��ѧУ����ʡ�С�����Ϊ��");
		errTable.put(new Integer(SCHOOL_NOT_NULL),"��ѧУ������Ϊ��");
		errTable.put(new Integer(ACTIVE_CODE_EXPIRE),"���������ѹ���");
		errTable.put(new Integer(ACTIVE_CODE_ERR),"�������Ӵ���");

		isInit=true;

	}
}
