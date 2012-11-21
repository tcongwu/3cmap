package com.c3map.mail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zjiao.auth.AuthUserDAO;
import com.zjiao.auth.UserInfo;
import com.zjiao.key.KeyException;
import com.zjiao.struts.ActionExtend;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.SysUtils;
import com.zjiao.util.RequestUtils;
import com.zjiao.util.StringUtils;

/** 
 * 邮箱相关操作Action类
 */
public class MailAction extends ActionExtend {
	
	/**
	 * 给人发站内信
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSendMail(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new MailErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		UserInfo rinfo=null;
		
		int id=requ.getInt("receiverId");
		int save=requ.getInt("save");
		int mid=requ.getInt("mid");
		String sub=requ.getString("sub");
		String content=requ.getString("content");
		
	  if(loginUser==null)
			errInfo.saveError(MailErrInfo.NEED_LOGIN);
		else//检查当前用户是否是正常用户
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else//邮件的主题和内容不能为空
		if(StringUtils.isNull(sub)||StringUtils.isNull(content))
			errInfo.saveError(MailErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			try{
				//检查收件人是否存在
				rinfo=AuthUserDAO.getUserInfo(id);
				if(rinfo==null){
					errInfo.saveError(MailErrInfo.OPERATION_NOT_ALLOW);
				}else{
					//生成留言信息对象
					content=content.replaceAll(" ", "&nbsp");
					content=content.replaceAll("\n", "<br>");
					
					MailInfo mail=new MailInfo();
					mail.setReceiverId(id);
					mail.setReceiverName(rinfo.getRealName());
					mail.setSenderId(loginUser.getId());
					mail.setSenderName(loginUser.getRealName());
					mail.setSub(sub);
					mail.setContent(content);
					mail.setStatus(SysUtils.MAIL_STATUS_NEW);
					
					int ret=-1;
					if(save==1){
						ret=MailDAO.ssMail(mail);
					}else{
						ret=MailDAO.sendMail(mail);
					}
					if(ret<1)
						errInfo.saveError(MailErrInfo.ERR_UNKNOWN);
					
					//此为回复，把回复的邮件设置为已回复
					if(mid>0){
						mail.setId(mid);
						mail.setReceiverId(loginUser.getId());
						mail.setStatus(SysUtils.MAIL_STATUS_REPLIED);
						//更新数据库，设置邮件状态
						ret=MailDAO.setMailStatus(mail);
						if(ret<1)
							errInfo.saveError(MailErrInfo.ERR_UNKNOWN);
					}
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(MailErrInfo.DATABASE_ERR);
			}catch(KeyException ex){
				Logger.Log(Logger.LOG_ERROR,ex);
				errInfo.saveError(MailErrInfo.GENERATE_KEY_FAILED);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		request.setAttribute("receiver", rinfo);
		return new ActionForward("/mail/sendok.jsp");
	}
	
	/**
	 * 删除收件箱中信件
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteInbox(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new MailErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		String mlist=requ.getArrayString("mid");
		
	  if(loginUser==null)
			errInfo.saveError(MailErrInfo.NEED_LOGIN);
		else//检查当前用户是否是正常用户
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				
				int ret=MailDAO.deleteMail(loginUser, mlist);
				if(ret<1)
					errInfo.saveError(MailErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(MailErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/mail/inbox.jsp");
	}
	
	/**
	 * 删除发件箱中信件
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteOutbox(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new MailErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		String mlist=requ.getArrayString("mid");
		
	  if(loginUser==null)
			errInfo.saveError(MailErrInfo.NEED_LOGIN);
		else//检查当前用户是否是正常用户
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				
				int ret=MailDAO.delSendMail(loginUser, mlist);
				if(ret<1)
					errInfo.saveError(MailErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(MailErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/mail/outbox.jsp");
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
