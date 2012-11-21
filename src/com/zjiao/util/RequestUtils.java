package com.zjiao.util;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * ������󹤾߼����û���װ����������һЩ����
 */
public class RequestUtils {
	
	private HttpServletRequest req;

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	private RequestUtils() {
	}
	
	/**
	 * �������Ĺ��캯��
	 * @param req �������
	 */
	private RequestUtils(HttpServletRequest req){
		this.req=req;
	}
	
	/**
	 * ʵ����������ͨ���˺�����ȡ����ʵ��
	 * @param req �������
	 * @return ���󹤾߼�����
	 */
	public static RequestUtils getInstance(HttpServletRequest req){
		return new RequestUtils(req);
	}
	
	/**
	 * ����������л�ȡ��������
	 * @param key ֵ��
	 * @return ��������
	 */
	public int getInt(String key){
		if(req==null){
			return 0;
		}else{
			String tmp=req.getParameter(key);
			try{
				return Integer.parseInt(tmp);
			}catch(Exception e){
				return 0;
			}
		}
	}
	
	/**
	 * ����������л�ȡ���ڲ���
	 * @param key ֵ��
	 * @return ���ڲ���
	 */
	public Date getDate(String key){
		String dt=req.getParameter(key);
		if((dt==null)||dt.length()!=10){
			return null;
		}
		
		try{
			int year=Integer.parseInt(dt.substring(0,4));
			int month=Integer.parseInt(dt.substring(5,7))-1;
			int day=Integer.parseInt(dt.substring(8));
			
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(year, month, day);
			
			return new Date(cal.getTimeInMillis());
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��ȡ��Ч���ִ�
	 * @param i ���
	 * @return ���ַ���
	 */
	public String getAnswer(int i) throws Exception{
		
		String ad=req.getParameter("ad"+i);
		String am=req.getParameter("am"+i);
		String az=req.getParameter("az"+i);
		
		if(StringUtils.isNull(am)||StringUtils.isNull(az)){
			return null;
		}
		
		StringBuffer ans=new StringBuffer();
		if(StringUtils.isNull(ad)){
			ans.append('+');
		}else{
			ans.append(ad);
		}
		ans.append("0.");
		ans.append(am);
		ans.append("��10(");
		if((az.charAt(0)!='+')&&(az.charAt(0)!='-')){
			ans.append('+');
		}
		ans.append(az);
		ans.append(')');
		
		return ans.toString();
	}
	
	/**
	 * ��ȡ��Ч���ִ�
	 * @param no ����
	 * @param i ���
	 * @return ���ַ���
	 */
	public String getAnswer(int no, int i) throws Exception{
		
		String ad=req.getParameter("a"+no+"d"+i);
		String am=req.getParameter("a"+no+"m"+i);
		String az=req.getParameter("a"+no+"z"+i);
		
		if(StringUtils.isNull(am)||StringUtils.isNull(az)){
			return null;
		}
		
		StringBuffer ans=new StringBuffer();
		if(StringUtils.isNull(ad)){
			ans.append('+');
		}else{
			ans.append(ad);
		}
		ans.append("0.");
		ans.append(am);
		ans.append("��10(");
		if((az.charAt(0)!='+')&&(az.charAt(0)!='-')){
			ans.append('+');
		}
		ans.append(az);
		ans.append(')');
		
		return ans.toString();
	}
	
	/**
	 * ����������л�ȡ�ַ�������
	 * @param key ֵ��
	 * @return �ַ�������
	 */
	public String getString(String key){
		return req.getParameter(key);
	}
	
	/**
	 * ����������л�ȡ�����ַ�������
	 * @param key ֵ��
	 * @return �ַ�������
	 */
	public String getArrayString(String key){
		String[] params=req.getParameterValues(key);
		String lvalue="";
		if((params!=null)&&(params.length>0)){
			for (int i=0;i<params.length;i++)
			{
				if(i==0){
					lvalue=params[i];
				}else{
					lvalue=lvalue+","+params[i];
				}
			}
		}
		return lvalue;
	}
}
