package com.c3map.friend;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zjiao.auth.AuthUserDAO;
import com.zjiao.auth.UserInfo;
import com.zjiao.struts.ActionExtend;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.SysUtils;
import com.zjiao.util.RequestUtils;
import com.zjiao.util.StringUtils;

/** 
 * 好友相关操作Action类
 */
public class FriendAction extends ActionExtend {
	
	/**
	 * 发送好友申请
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSendApply(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FriendErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		
	  if(loginUser==null)
			errInfo.saveError(FriendErrInfo.NEED_LOGIN);
		else
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				//检查所申请的好友是否存在
				if(!AuthUserDAO.isExistUser(id))
					errInfo.saveError(FriendErrInfo.OPERATION_NOT_ALLOW);
				else//检查是否已经是好友关系
				if(FriendDAO.isExistFriend(id, loginUser.getId()))
					return new ActionForward("/homepage.do?op=2&id="+id, true);
				else//检查是否存在好友申请
				if(FriendDAO.isExistApply(id, loginUser.getId()))
					return new ActionForward("/homepage.do?op=1&id="+id, true);
				else{
					//把申请写入好友申请信息表中
					int ret=FriendDAO.applyFriend(id, loginUser.getId());
					if(ret!=1)
						errInfo.saveError(FriendErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(FriendErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/homepage.do?op=1&id="+id, true);
	}
	
	/**
	 * 接受好友申请
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doAcceptFriend(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FriendErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		
	  if(loginUser==null)
			errInfo.saveError(FriendErrInfo.NEED_LOGIN);
		else
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				//检查提交申请的好友是否存在
				if(!AuthUserDAO.isExistUser(id)){
					errInfo.saveError(FriendErrInfo.OPERATION_NOT_ALLOW);
				}else{
					//把申请好友写入好友信息表中
					int ret=FriendDAO.addFriend(loginUser.getId(), id);
					if(ret!=1)
						errInfo.saveError(FriendErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(FriendErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/homepage.do?op=2&id="+id, true);
	}
	
	/**
	 * 拒绝好友申请
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRejectFriend(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FriendErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		
	  if(loginUser==null)
			errInfo.saveError(FriendErrInfo.NEED_LOGIN);
		else
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				//删除好友申请信息表中记录
				int ret=FriendDAO.deleteApply(loginUser.getId(), id);
				if(ret!=1)
					errInfo.saveError(FriendErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(FriendErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/friend/apply.jsp", true);
	}
	
	/**
	 * 删除好友
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteFriend(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FriendErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		
	  if(loginUser==null)
			errInfo.saveError(FriendErrInfo.NEED_LOGIN);
		else
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				//删除好友信息表中记录
				int ret=FriendDAO.deleteFriend(loginUser.getId(), id);
				if(ret<1)
					errInfo.saveError(FriendErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(FriendErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/friend/index.jsp"+StringUtils.getQueryString(request,"id"), true);
	}
	
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
}
