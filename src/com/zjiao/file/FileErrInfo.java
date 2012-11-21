package com.zjiao.file;

import com.zjiao.ErrorInformation;
import com.zjiao.util.StringUtils;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * 认证注册模块的错误信息处理类
 */
public class FileErrInfo implements ErrorInformation {
	
	private static Hashtable errTable=new Hashtable();
	private static boolean isInit=false;
	
	private int errNumber=-1;

	public static final int NEED_LOGIN=0; //用户需要登录
	public static final int FILE_ERR=1; //文件错误
	public static final int FILESIZE_ERR=2; //文件大小超出限制
	public static final int FILETYPE_ERR=3; //文件类型被限制
	public static final int ERR_UNKNOWN=4; //未知错误
	public static final int DATABASE_ERR=5; //数据库错误
	
	/**
	 * 默认的构造函数
	 */
	public FileErrInfo() {
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

		/* 建立注册认证模块的错误信息表 */
		errTable.put(new Integer(NEED_LOGIN),"用户需要登录");
		errTable.put(new Integer(FILE_ERR),"文件错误");
		errTable.put(new Integer(FILESIZE_ERR),"文件类型错误或文件大小超出限制");
		errTable.put(new Integer(FILETYPE_ERR),"文件类型被限制");
		errTable.put(new Integer(ERR_UNKNOWN),"未知错误");
		errTable.put(new Integer(DATABASE_ERR),"数据库错误");

		isInit=true;

	}
}
