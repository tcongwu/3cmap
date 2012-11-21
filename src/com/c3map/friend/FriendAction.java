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
 * ������ز���Action��
 */
public class FriendAction extends ActionExtend {
	
	/**
	 * ���ͺ�������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSendApply(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
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
				//���������ĺ����Ƿ����
				if(!AuthUserDAO.isExistUser(id))
					errInfo.saveError(FriendErrInfo.OPERATION_NOT_ALLOW);
				else//����Ƿ��Ѿ��Ǻ��ѹ�ϵ
				if(FriendDAO.isExistFriend(id, loginUser.getId()))
					return new ActionForward("/homepage.do?op=2&id="+id, true);
				else//����Ƿ���ں�������
				if(FriendDAO.isExistApply(id, loginUser.getId()))
					return new ActionForward("/homepage.do?op=1&id="+id, true);
				else{
					//������д�����������Ϣ����
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
	 * ���ܺ�������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doAcceptFriend(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//��ȡ��ʼ����Ϣ
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
				//����ύ����ĺ����Ƿ����
				if(!AuthUserDAO.isExistUser(id)){
					errInfo.saveError(FriendErrInfo.OPERATION_NOT_ALLOW);
				}else{
					//���������д�������Ϣ����
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
	 * �ܾ���������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRejectFriend(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
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
				//ɾ������������Ϣ���м�¼
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
	 * ɾ������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteFriend(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
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
				//ɾ��������Ϣ���м�¼
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
	 * �÷������õ�¼һ��ʧ�ܺ����µ�½�ɹ�ҳ����ʾ�հ׵�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
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
