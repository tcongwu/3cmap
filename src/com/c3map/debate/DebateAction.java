package com.c3map.debate;

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
import com.c3map.debate.DebateDAO;
import com.zjiao.*;
import com.c3map.debate.DebateInfo;
import com.zjiao.struts.ActionExtend;

import com.zjiao.ErrorInformation;

import com.zjiao.Logger;

import com.zjiao.util.StringUtils;

public class DebateAction extends ActionExtend  {
	
	
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
	public ActionForward doJoinDebate(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			DebateInfo debate = (DebateInfo)form;
			ErrorInformation errInfo=new DebateErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
		//	Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null))//检查用户是否登录
				errInfo.saveError(DebateErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(debate.getDebateName())||StringUtils.isNull(debate.getDebateBody()))
				errInfo.saveError(DebateErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//完善debate对象
			//	debate.setAuthorId(loginRole.getId());
				
				
				try{
					
					if(loginUser.getUserRole()>0){
						//如果没有出错，则保存测验入数据库
						int ret=DebateDAO.createDebate(debate);
						if(ret<1){
							errInfo.saveError(DebateErrInfo.ERR_UNKNOWN);
						}
					
						
							
						
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(DebateErrInfo.DATABASE_ERR);
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
