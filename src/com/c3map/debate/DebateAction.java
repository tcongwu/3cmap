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
	
	/**
	 * ��������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doJoinDebate(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			DebateInfo debate = (DebateInfo)form;
			ErrorInformation errInfo=new DebateErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
		//	Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null))//����û��Ƿ��¼
				errInfo.saveError(DebateErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(debate.getDebateName())||StringUtils.isNull(debate.getDebateBody()))
				errInfo.saveError(DebateErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//����debate����
			//	debate.setAuthorId(loginRole.getId());
				
				
				try{
					
					if(loginUser.getUserRole()>0){
						//���û�г����򱣴���������ݿ�
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
