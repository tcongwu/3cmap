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
 * �ҵ���ҳ��ز���Action��
 */
public class HomeAction extends ActionExtend {
	
	/**
	 * �����û��������������ڼ����룬����ü����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDefault(ActionMapping mapping,
	  ActionForm form, HttpServletRequest request,
	  HttpServletResponse response) throws Exception {
		
		//��ȡ��ʼ����Ϣ
		ErrorInformation errInfo=new RegErrInfo();
		UserInfo loginUser = UserInfo.getLoginUser(request);

		//��������
	  RequestUtils requ=RequestUtils.getInstance(request);
	  UserInfo ubasic=null;
	  int uid=requ.getInt("id"); //Ҫ������û�ID
		
		//�ж��û��Ƿ��½
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
			try{
				//����û���������
			  if(uid==0){
				  ubasic=loginUser;
			  }else{
				  ubasic=AuthUserDAO.getUserInfo(uid);
			  }

			  //�ж��Ƿ���ȷ��ȡ���û�����
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
