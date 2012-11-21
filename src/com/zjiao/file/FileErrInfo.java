package com.zjiao.file;

import com.zjiao.ErrorInformation;
import com.zjiao.util.StringUtils;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * ��֤ע��ģ��Ĵ�����Ϣ������
 */
public class FileErrInfo implements ErrorInformation {
	
	private static Hashtable errTable=new Hashtable();
	private static boolean isInit=false;
	
	private int errNumber=-1;

	public static final int NEED_LOGIN=0; //�û���Ҫ��¼
	public static final int FILE_ERR=1; //�ļ�����
	public static final int FILESIZE_ERR=2; //�ļ���С��������
	public static final int FILETYPE_ERR=3; //�ļ����ͱ�����
	public static final int ERR_UNKNOWN=4; //δ֪����
	public static final int DATABASE_ERR=5; //���ݿ����
	
	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public FileErrInfo() {
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
		errTable.put(new Integer(NEED_LOGIN),"�û���Ҫ��¼");
		errTable.put(new Integer(FILE_ERR),"�ļ�����");
		errTable.put(new Integer(FILESIZE_ERR),"�ļ����ʹ�����ļ���С��������");
		errTable.put(new Integer(FILETYPE_ERR),"�ļ����ͱ�����");
		errTable.put(new Integer(ERR_UNKNOWN),"δ֪����");
		errTable.put(new Integer(DATABASE_ERR),"���ݿ����");

		isInit=true;

	}
}
