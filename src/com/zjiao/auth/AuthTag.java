package com.zjiao.auth;

import java.util.Hashtable;

import javax.servlet.jsp.JspException;
import javax.servlet.ServletException;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;

import com.zjiao.ErrorInformation;
import com.zjiao.SysUtils;
import com.zjiao.util.StringUtils;

/**
 * <p>Title:认证标签类 </p>
 * <p>Description:所有http的入口都通过此类进行认证 </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * @author Lee Bin
 * @version 1.0
 */

public class AuthTag extends BodyTagSupport {
	
  /** 当前模块名称 */
  private String module=null;
  /** 要求用户状态 */
  private String status=null;
  /** 认证失败后要跳转到的URL地址 */
  private String url=null;
  /** 当前模块名称 */
  private static Hashtable mrTable=null;
  /** 认证结果常量 */
  public static final int AUTH_OK=0;
  public static final int AUTH_FAIL=1;
  public static final int AUTH_SKIP=2;
  /** 系统模块信息常量 */
  public static final String MODULE_NORMAL="normal";
  public static final String MODULE_ANGEL="angel";
  public static final String MODULE_ADMIN="admin";
  /** 用户状态信息常量 */
  public static final String STATUS_NORMAL="norm";
  public static final String STATUS_APPLICATION="app";
  
  /**
   * 默认的构造函数
   */
  public AuthTag() {
  }

  /**
   * 构造函数
   * @param value 模块名称
   */
  public AuthTag(String value, String status, String url) {
		setModule(value);
		setStatus(status);
		setRedirection(url);
  }

  /**
   * 设置系统当前模块
   * @param value 模块名称
   */
  public void setModule(String value) {
  	if(value==null)
    	module=value;
    else
    	module=value.trim();
  }

  /**
   * 设置系统所要求的用户的状态
   * @param status 要求状态
   */
  public void setStatus(String status) {
	if(status==null)
		this.status=status;
	else
		this.status=status.trim();
  }

  /**
   * 设置系统要跳转到的URL地址
   * @param url URL地址
   */
  public void setRedirection(String url) {
		if(url==null)
			this.url=url;
		else
			this.url=url.trim();
  }
  
  /**
   * 判断某角色是否有权限进入当前模块
   * @param uinfo 用户信息
   * @return 是否有权限
   */
  public boolean isAccess(UserInfo uinfo){
  	
  	int rl=1;
  	
  	if(MODULE_NORMAL.equals(module)||(module==null)){
  		rl=SysUtils.USER_ROLE_NORMAL;
  	}else
  	if(MODULE_ANGEL.equals(module)){
  		rl=SysUtils.USER_ROLE_ANGEL;
  	}else
  	if(MODULE_ADMIN.equals(module)){
  		rl=SysUtils.USER_ROLE_ADMIN;
  	}
  	
  	if(uinfo.getUserRole()>=rl)
  		return true;
  		
  	return false;
  }
  
  /**
   * 判断用户是否匹配当前页面所要求的状态
   * @param uinfo 用户信息
   * @return 是否匹配
   */
  public boolean isMatchStatus(UserInfo uinfo){
	  	
		if((uinfo.getUserStatus()==SysUtils.USER_STATUS_NORMAL)||(STATUS_APPLICATION.equals(status))){
			return true;
		}
	  		
		return false;
  }

  protected void Auth_internal(HttpServletRequest request,HttpServletResponse response) throws Exception {
  	
  	ErrorInformation err=new RegErrInfo();
    
    int authret=AUTH_FAIL; //初始化认证结果
    
    /** 查看用户是否通过了认证 */
    UserInfo uinfo=UserInfo.getLoginUser(request);
    if(uinfo==null){
    	if(!StringUtils.isNull(url)){
				response.sendRedirect(url);
				return;
    	}else
    		throw new Exception(err.getInformation(RegErrInfo.NEED_LOGIN));
    }
    
		/** 对用户进行权限检查 */
		if(!isAccess(uinfo)){
			throw new Exception(err.getInformation(RegErrInfo.CANT_ACCESS));
		}
    
		/** 检查用户状态信息 */
		if(!isMatchStatus(uinfo)){
			response.sendRedirect("/profile");
			return;
		}
    
    authret=AUTH_OK;
    request.setAttribute("uinfo", uinfo);
  }

  public boolean doAuth(HttpServlet servlet,HttpServletRequest request,HttpServletResponse response)
  throws ServletException, java.io.IOException
  {
    try {
      Auth_internal(request,response);
    } catch (Exception ex) {
      request.setAttribute("javax.servlet.jsp.jspException",ex);
      response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
      servlet.getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
      return false;
    }
    return true;
  }

  public int doStartTag() throws javax.servlet.jsp.JspException {
    try {
      Auth_internal( (HttpServletRequest) pageContext.getRequest(),
             (HttpServletResponse) pageContext.getResponse());
    }
    catch (Exception ex) {
      throw new JspException("",ex);
    }
    return EVAL_BODY_INCLUDE;
  }

  public int doEndTag() throws javax.servlet.jsp.JspException {
    return super.doEndTag();
  }
}