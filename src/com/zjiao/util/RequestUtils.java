package com.zjiao.util;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * 请求对象工具集，用户封装对请求对象的一些处理
 */
public class RequestUtils {
	
	private HttpServletRequest req;

	/**
	 * 默认的构造函数
	 */
	private RequestUtils() {
	}
	
	/**
	 * 带参数的构造函数
	 * @param req 请求对象
	 */
	private RequestUtils(HttpServletRequest req){
		this.req=req;
	}
	
	/**
	 * 实例化函数，通过此函数获取对象实例
	 * @param req 请求对象
	 * @return 请求工具集对象
	 */
	public static RequestUtils getInstance(HttpServletRequest req){
		return new RequestUtils(req);
	}
	
	/**
	 * 从请求对象中获取整数参数
	 * @param key 值键
	 * @return 整数参数
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
	 * 从请求对象中获取日期参数
	 * @param key 值键
	 * @return 日期参数
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
	 * 获取有效数字答案
	 * @param i 序号
	 * @return 答案字符串
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
		ans.append("×10(");
		if((az.charAt(0)!='+')&&(az.charAt(0)!='-')){
			ans.append('+');
		}
		ans.append(az);
		ans.append(')');
		
		return ans.toString();
	}
	
	/**
	 * 获取有效数字答案
	 * @param no 题序
	 * @param i 序号
	 * @return 答案字符串
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
		ans.append("×10(");
		if((az.charAt(0)!='+')&&(az.charAt(0)!='-')){
			ans.append('+');
		}
		ans.append(az);
		ans.append(')');
		
		return ans.toString();
	}
	
	/**
	 * 从请求对象中获取字符串参数
	 * @param key 值键
	 * @return 字符串参数
	 */
	public String getString(String key){
		return req.getParameter(key);
	}
	
	/**
	 * 从请求对象中获取数组字符串参数
	 * @param key 值键
	 * @return 字符串参数
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
