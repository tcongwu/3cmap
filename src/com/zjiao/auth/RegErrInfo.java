package com.zjiao.auth;

import com.zjiao.ErrorInformation;
import com.zjiao.util.StringUtils;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lee Bin
 *
 * 认证注册模块的错误信息处理类
 */
public class RegErrInfo implements ErrorInformation {
	
	private static Hashtable errTable=new Hashtable();
	private static boolean isInit=false;
	
	private int errNumber=-1;

	public static final int ERR_UNKNOWN=0; //未知错误
	public static final int PASSWORD_NOT_NULL=1; //登录密码不能为空
	public static final int PASSWORD_LENGTH_NOT_SUITABLE=2; //密码长度必须介于6至20个字符之间
	public static final int EMAIL_ERROR=3; //请输入正确的email
	public static final int EMAIL_EXIST=4; //email已经存在
	public static final int VERIFYCODE_ERROR=5; //注册验证码输入错误
	public static final int DATABASE_ERR=6; //数据库错误
	public static final int GENERATE_KEY_FAILED=7; //内部错误，主键生成失败
	public static final int OPERATION_NOT_ALLOW=8; //非法操作，可能未登录
	public static final int SEX_NOT_NULL=9; //性别不能为空
	public static final int CONFIRM_PASSWORD_ERROR=10; //确认密码错误
	public static final int PASSWORD_ERROR=11; //密码不正确
	public static final int EMAIL_OR_PASSWORD_ERROR=12; //email或密码错误
	public static final int NEED_LOGIN=13; //用户需要登录
	public static final int CANT_ACCESS=14; //没有足够权限
	public static final int USER_INVALID=15; //用户无效
	public static final int OLD_PASSWORD_ERROR=16; //原有密码输入错误
	public static final int EMAIL_NOT_NULL=17; //email不能为空
	public static final int USER_INACTIVE=18; //用户未激活，请通过系统发送到您邮箱的激活邮件激活帐号
	public static final int REALNAME_ERROR=19; //姓名不能为空，且只能是汉字
	public static final int ROLE_TYPE_NOT_NULL=20; //“学历”不能为空
	public static final int PROVINCE_NOT_NULL=21; //“学校所在省市”不能为空
	public static final int SCHOOL_NOT_NULL=22; //“学校”不能为空
	public static final int ACTIVE_CODE_EXPIRE=23; //激活链接已过期
	public static final int ACTIVE_CODE_ERR=24; //激活链接错误
	
	/**
	 * 默认的构造函数
	 */
	public RegErrInfo() {
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
		errTable.put(new Integer(ERR_UNKNOWN),"未知错误");
		errTable.put(new Integer(PASSWORD_NOT_NULL),"登录密码不能为空");
		errTable.put(new Integer(PASSWORD_LENGTH_NOT_SUITABLE),"密码长度必须介于6至20个字符之间");
		errTable.put(new Integer(EMAIL_ERROR),"请输入正确的email");
		errTable.put(new Integer(EMAIL_EXIST),"email已经存在");
		errTable.put(new Integer(VERIFYCODE_ERROR),"注册验证码输入错误");
		errTable.put(new Integer(DATABASE_ERR),"数据库错误");
		errTable.put(new Integer(GENERATE_KEY_FAILED),"内部错误，主键生成失败");
		errTable.put(new Integer(OPERATION_NOT_ALLOW),"非法操作，可能未登录");
		errTable.put(new Integer(SEX_NOT_NULL),"性别不能为空");
		errTable.put(new Integer(CONFIRM_PASSWORD_ERROR),"确认密码错误");
		errTable.put(new Integer(PASSWORD_ERROR),"密码不正确");
		errTable.put(new Integer(EMAIL_OR_PASSWORD_ERROR),"email或密码错误");
		errTable.put(new Integer(NEED_LOGIN),"用户需要登录");
		errTable.put(new Integer(CANT_ACCESS),"没有足够权限");
		errTable.put(new Integer(USER_INVALID),"用户无效");
		errTable.put(new Integer(OLD_PASSWORD_ERROR),"原有密码输入错误");
		errTable.put(new Integer(EMAIL_NOT_NULL),"email不能为空");
		errTable.put(new Integer(USER_INACTIVE),"用户未激活，请通过系统发送到您邮箱的激活邮件激活帐号");
		errTable.put(new Integer(ROLE_TYPE_NOT_NULL),"“学历”不能为空");
		errTable.put(new Integer(PROVINCE_NOT_NULL),"“学校所在省市”不能为空");
		errTable.put(new Integer(SCHOOL_NOT_NULL),"“学校”不能为空");
		errTable.put(new Integer(ACTIVE_CODE_EXPIRE),"激活链接已过期");
		errTable.put(new Integer(ACTIVE_CODE_ERR),"激活链接错误");

		isInit=true;

	}
}
