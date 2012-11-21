/*
 * �������� 2006-8-2
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.mailbox;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
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
	public ActionForward doJoinMail(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			MailInfo mail = (MailInfo)form;
			ErrorInformation errInfo=new MailErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
		
		
			if((loginUser==null))//����û��Ƿ��¼
				errInfo.saveError(MailErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(mail.getPosterName())||StringUtils.isNull(mail.getLetterSubject()))
				errInfo.saveError(MailErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
							
				try{
					
					if(loginUser.getUserRole()>0){
						//���û�г����򱣴���������ݿ�
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

