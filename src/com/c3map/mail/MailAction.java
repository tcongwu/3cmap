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
 * ������ز���Action��
 */
public class MailAction extends ActionExtend {
	
	/**
	 * ���˷�վ����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSendMail(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//��ȡ��ʼ����Ϣ
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
		else//��鵱ǰ�û��Ƿ��������û�
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else//�ʼ�����������ݲ���Ϊ��
		if(StringUtils.isNull(sub)||StringUtils.isNull(content))
			errInfo.saveError(MailErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			try{
				//����ռ����Ƿ����
				rinfo=AuthUserDAO.getUserInfo(id);
				if(rinfo==null){
					errInfo.saveError(MailErrInfo.OPERATION_NOT_ALLOW);
				}else{
					//����������Ϣ����
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
					
					//��Ϊ�ظ����ѻظ����ʼ�����Ϊ�ѻظ�
					if(mid>0){
						mail.setId(mid);
						mail.setReceiverId(loginUser.getId());
						mail.setStatus(SysUtils.MAIL_STATUS_REPLIED);
						//�������ݿ⣬�����ʼ�״̬
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
	 * ɾ���ռ������ż�
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteInbox(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new MailErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		String mlist=requ.getArrayString("mid");
		
	  if(loginUser==null)
			errInfo.saveError(MailErrInfo.NEED_LOGIN);
		else//��鵱ǰ�û��Ƿ��������û�
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
	 * ɾ�����������ż�
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteOutbox(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new MailErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		String mlist=requ.getArrayString("mid");
		
	  if(loginUser==null)
			errInfo.saveError(MailErrInfo.NEED_LOGIN);
		else//��鵱ǰ�û��Ƿ��������û�
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
