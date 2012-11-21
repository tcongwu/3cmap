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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.zjiao.auth.*;
import com.zjiao.auth.UserInfo;
import com.c3map.mailbox.MailDAO;
import com.c3map.mailbox.MailErrInfo;
import com.c3map.mailbox.MailInfo;
import com.zjiao.struts.ActionExtend;

import com.zjiao.ErrorInformation;

import com.zjiao.Logger;
import com.zjiao.util.RequestUtils;
import com.zjiao.util.StringUtils;

public class MailAction extends ActionExtend  {
	
	
	/**
	 * 该方法放置登录一次失败后重新登陆成功页面显示空白的问题
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDefault(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		return mapping.findForward("home");
	}
	
	
	
	/**
	 * 参与讨论
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doJoinMail(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			MailInfo mail = (MailInfo)form;
			ErrorInformation errInfo=new MailErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
		
		
			if((loginUser==null))//检查用户是否登录
				errInfo.saveError(MailErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(mail.getPosterName())||StringUtils.isNull(mail.getLetterSubject()))
				errInfo.saveError(MailErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
							
				try{
					
					if(loginUser.getUserRole()>0){
						//如果没有出错，则保存测验入数据库
						int ret=MailDAO.sendMail(mail);
						if(ret<1){
							errInfo.saveError(MailErrInfo.ERR_UNKNOWN);
						}
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(MailErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
			String url = request.getParameter("url");
			ActionForward forward = null;
			if(StringUtils.isEmpty(url)){
				forward = mapping.findForward("home");
			}else
				forward = new ActionForward(url);
			
			return forward;
	}
	

}

