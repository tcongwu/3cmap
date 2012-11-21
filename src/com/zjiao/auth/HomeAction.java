package com.zjiao.auth;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zjiao.struts.ActionExtend;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.util.RequestUtils;

/** 
 * 我的主页相关操作Action类
 */
public class HomeAction extends ActionExtend {
	
	/**
	 * 放置用户激活动作，如果存在激活码，则调用激活动作
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDefault(ActionMapping mapping,
	  ActionForm form, HttpServletRequest request,
	  HttpServletResponse response) throws Exception {
		
		//获取初始化信息
		ErrorInformation errInfo=new RegErrInfo();
		UserInfo loginUser = UserInfo.getLoginUser(request);

		//变量定义
	  RequestUtils requ=RequestUtils.getInstance(request);
	  UserInfo ubasic=null;
	  int uid=requ.getInt("id"); //要浏览的用户ID
		
		//判断用户是否登陆
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
			try{
				//获得用户基本资料
			  if(uid==0){
				  ubasic=loginUser;
			  }else{
				  ubasic=AuthUserDAO.getUserInfo(uid);
			  }

			  //判断是否正确获取到用户资料
				if(ubasic==null)
					errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
				else
					request.setAttribute("ubasic", ubasic);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
		
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/homepage"+ubasic.getUserType()+".jsp");
	}
}
