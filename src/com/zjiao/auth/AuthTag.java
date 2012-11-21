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
 * <p>Title:��֤��ǩ�� </p>
 * <p>Description:����http����ڶ�ͨ�����������֤ </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * @author Lee Bin
 * @version 1.0
 */

public class AuthTag extends BodyTagSupport {
	
  /** ��ǰģ������ */
  private String module=null;
  /** Ҫ���û�״̬ */
  private String status=null;
  /** ��֤ʧ�ܺ�Ҫ��ת����URL��ַ */
  private String url=null;
  /** ��ǰģ������ */
  private static Hashtable mrTable=null;
  /** ��֤������� */
  public static final int AUTH_OK=0;
  public static final int AUTH_FAIL=1;
  public static final int AUTH_SKIP=2;
  /** ϵͳģ����Ϣ���� */
  public static final String MODULE_NORMAL="normal";
  public static final String MODULE_ANGEL="angel";
  public static final String MODULE_ADMIN="admin";
  /** �û�״̬��Ϣ���� */
  public static final String STATUS_NORMAL="norm";
  public static final String STATUS_APPLICATION="app";
  
  /**
   * Ĭ�ϵĹ��캯��
   */
  public AuthTag() {
  }

  /**
   * ���캯��
   * @param value ģ������
   */
  public AuthTag(String value, String status, String url) {
		setModule(value);
		setStatus(status);
		setRedirection(url);
  }

  /**
   * ����ϵͳ��ǰģ��
   * @param value ģ������
   */
  public void setModule(String value) {
  	if(value==null)
    	module=value;
    else
    	module=value.trim();
  }

  /**
   * ����ϵͳ��Ҫ����û���״̬
   * @param status Ҫ��״̬
   */
  public void setStatus(String status) {
	if(status==null)
		this.status=status;
	else
		this.status=status.trim();
  }

  /**
   * ����ϵͳҪ��ת����URL��ַ
   * @param url URL��ַ
   */
  public void setRedirection(String url) {
		if(url==null)
			this.url=url;
		else
			this.url=url.trim();
  }
  
  /**
   * �ж�ĳ��ɫ�Ƿ���Ȩ�޽��뵱ǰģ��
   * @param uinfo �û���Ϣ
   * @return �Ƿ���Ȩ��
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
   * �ж��û��Ƿ�ƥ�䵱ǰҳ����Ҫ���״̬
   * @param uinfo �û���Ϣ
   * @return �Ƿ�ƥ��
   */
  public boolean isMatchStatus(UserInfo uinfo){
	  	
		if((uinfo.getUserStatus()==SysUtils.USER_STATUS_NORMAL)||(STATUS_APPLICATION.equals(status))){
			return true;
		}
	  		
		return false;
  }

  protected void Auth_internal(HttpServletRequest request,HttpServletResponse response) throws Exception {
  	
  	ErrorInformation err=new RegErrInfo();
    
    int authret=AUTH_FAIL; //��ʼ����֤���
    
    /** �鿴�û��Ƿ�ͨ������֤ */
    UserInfo uinfo=UserInfo.getLoginUser(request);
    if(uinfo==null){
    	if(!StringUtils.isNull(url)){
				response.sendRedirect(url);
				return;
    	}else
    		throw new Exception(err.getInformation(RegErrInfo.NEED_LOGIN));
    }
    
		/** ���û�����Ȩ�޼�� */
		if(!isAccess(uinfo)){
			throw new Exception(err.getInformation(RegErrInfo.CANT_ACCESS));
		}
    
		/** ����û�״̬��Ϣ */
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