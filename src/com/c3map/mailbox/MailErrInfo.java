/*
 * 创建日期 2006-8-2
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.c3map.mailbox;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import com.zjiao.ErrorInformation;
import com.zjiao.util.StringUtils;

public class  MailErrInfo implements ErrorInformation {
	private static Hashtable errTable=new Hashtable();
	private static boolean isInit=false;
	
	private int errNumber=-1;

	public static final int ERR_UNKNOWN=0; //未知错误
	public static final int DATABASE_ERR=1; //数据库错误
	public static final int OPERATION_NOT_ALLOW=2; //非法操作
	public static final int NEED_LOGIN=3; //用户需要登录
	public static final int CANT_ACCESS=4; //没有足够权限
	public static final int USER_INVALID=5; //用户无效
	public static final int PARAM_ERROR=6; //参数错误
	public static final int GENERATE_KEY_FAILED=7; //内部错误，主键生成失败
	public static final int NEEDED_FIELD_NOT_NULL=8; //请填写所有的必填项
	
	/**
	 * 默认的构造函数
	 */
	public MailErrInfo() {
		if(!isInit)
			initErrTable();
	}

	/* （非 Javadoc）
	 * @see com.zjiao.ErrorInformation#saveError(int)
	 */
	public void saveError(int err) {
		if(err<0)
			return;
		
		this.errNumber=err;
	}

	/* （非 Javadoc）
	 * @see com.zjiao.ErrorInformation#isEmpty()
	 */
	public boolean isEmpty() {
		if(errNumber==-1)
			return true;

		return false;
	}

	/* （非 Javadoc）
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
	
	/* （非 Javadoc）
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

	/* （非 Javadoc）
	 * @see com.zjiao.ErrorInformation#saveToRequest()
	 */
	public void saveToRequest(String name, HttpServletRequest req){
		req.setAttribute(name,this);
	}

	/**
	 * 初始化错误信息表
	 */
	private void initErrTable(){
		
		errTable.clear();

		/* 建立申请角色模块的错误信息表 */
		errTable.put(new Integer(ERR_UNKNOWN),"未知错误");
		errTable.put(new Integer(DATABASE_ERR),"数据库错误");
		errTable.put(new Integer(OPERATION_NOT_ALLOW),"非法操作");
		errTable.put(new Integer(NEED_LOGIN),"用户需要登录");
		errTable.put(new Integer(CANT_ACCESS),"没有足够权限");
		errTable.put(new Integer(USER_INVALID),"用户无效");
		errTable.put(new Integer(PARAM_ERROR),"参数错误");
		errTable.put(new Integer(GENERATE_KEY_FAILED),"内部错误，主键生成失败");
		errTable.put(new Integer(NEEDED_FIELD_NOT_NULL),"请填写所有的必填项");

		isInit=true;

	}

}
