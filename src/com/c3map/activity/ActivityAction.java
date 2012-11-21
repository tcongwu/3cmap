
package com.c3map.activity;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.auth.UserInfo;
import com.c3map.activity.ActivityDAO;
import com.c3map.activity.ActivityInfo;
import com.c3map.activity.ActivityErrInfo;
import com.zjiao.struts.ActionExtend;
import com.zjiao.util.StringUtils;
import com.zjiao.SysUtils;
import java.util.Calendar;
import java.sql.Timestamp;
import com.zjiao.util.RequestUtils;
import com.zjiao.auth.*;

public class ActivityAction extends ActionExtend {

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
	* ��ӻ
	* @param mapping ·��ӳ�����
	* @param form ������
	* @param request �������
	* @param response ��Ӧ����
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doCreateact(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		int actid=0;
		Timestamp now=new Timestamp(System.currentTimeMillis());
		
		//��ʼ��
		ActivityInfo act = (ActivityInfo)form;
		 
		ErrorInformation errInfo=new ActivityErrInfo();
	    
		//ת��ʱ���ʽ
		Calendar cal = Calendar.getInstance(); 
		
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String bin_hour=request.getParameter("begin_hour");
		String bin_min=request.getParameter("begin_min");
		String end_hour=request.getParameter("end_hour");
		String end_min=request.getParameter("end_min");
		
		Timestamp start=Utils.StingToTimestamp(starttime,bin_hour,bin_min);
		Timestamp end=null;
		
		if(!StringUtils.isNull(endtime)){
				end=Utils.StingToTimestamp(endtime,end_hour,end_min);
		}else{
				end=Utils.StingToTimestampEnd(starttime,bin_hour,bin_min);
		}
 
		//��ȡ��¼�û�
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)
			//����û��Ƿ��¼
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		//�������,��ص�ͺͻ���ݣ����ǲ���Ϊ��
		else if(StringUtils.isNull(act.getActname())||StringUtils.isNull(act.getActplace())||StringUtils.isNull(act.getActcontent()))
			errInfo.saveError(ActivityErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			//����act����
			act.setAuthorId(loginUser.getId());
			act.setAuthorname(loginUser.getRealName());
			act.setStart(start);
		    act.setEnd(end);
			act.setStatus(Utils.ACTIVITY_NORMAL);//1Ϊ�����,2��ʾ�ȵ�;
			
			try{
	  
				//���û�г����򱣴浽���ݿ�
				int ret=ActivityDAO.createActivity(act);
				if(ret<1){
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				}
				
				//��ûid,�Ա���ת��readact.jsp;
				actid=ActivityDAO.getActIdByActname(act.getActname(),act.getAuthorname(),now);
				if(actid<0){
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				}
				
				//�򴴽�һ������û����ּ�100
				if(errInfo.isEmpty()){
					int rt=AuthUserDAO.addScore(loginUser, -Utils.SCORE_BEGIN_ACTIVITY);
					if(rt<1)
						errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				}		

			}
			catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
			}
			}
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		return new ActionForward("/activity/readact.jsp?actid="+actid, true);
	 }


	/**
	 * �޸�һ���
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doEditact(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		//��ȡ��ʼ����Ϣ
		RequestUtils requ=RequestUtils.getInstance(request);
		ErrorInformation errInfo=new ActivityErrInfo();
  
		int actid=requ.getInt("actid");
  
		String actname=request.getParameter("actname");
		String actplace=request.getParameter("actplace");
		String actcontent=request.getParameter("actcontent");
		String phone=request.getParameter("phone");
		String email=request.getParameter("email");
  
   
 
		//ת��ʱ���ʽ
		Calendar cal = Calendar.getInstance(); 
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String bin_hour=request.getParameter("begin_hour");
		String bin_min=request.getParameter("begin_min");
		String end_hour=request.getParameter("end_hour");
		String end_min=request.getParameter("end_min");
		
		Timestamp start=Utils.StingToTimestamp(starttime,bin_hour,bin_min);
	
		Timestamp end=null;
		if(!StringUtils.isNull(endtime)){
			end=Utils.StingToTimestamp(endtime,end_hour,end_min);
		}else{
		  end=Utils.StingToTimestampEnd(starttime,bin_hour,bin_min);
		}
	
		//��ȡ��¼�û���Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		
		if(loginUser==null)//����û��Ƿ��¼
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		//�������,��ص�ͺͻ���ݣ����ǲ���Ϊ��
		else if(StringUtils.isNull(actname)||StringUtils.isNull(actplace)||StringUtils.isNull(actcontent))
				errInfo.saveError(ActivityErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			//����act����
			ActivityInfo act=new ActivityInfo();
			act.setStart(start);
			act.setEnd(end);
			act.setId(actid);
			act.setActname(actname);
			act.setActplace(actplace);
			act.setActcontent(actcontent);
			act.setPhone(phone);
			act.setEmail(email);
			try{
				int ret=ActivityDAO.editActivity(act);
				if(ret<1){
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				}
			}
			catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
			}
		}

		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}

		return new ActionForward("/activity/readact.jsp?actid="+actid, true);
		}




	/**
	 * ���²���/��ע�״̬
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doUpdateActivityStatus(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		//��ʼ����Ϣ
		ErrorInformation errInfo=new ActivityErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		int joiner_id=requ.getInt("joiner_id");
		int status=requ.getInt("status");
		
		//��ȡ��¼�û���Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)//����û��Ƿ��¼
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		else{
			try{
				//���û�г��������
				int ret=JoinActivityDAO.UpdateActivityStatus(id,joiner_id,status);
				if(ret<1)
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
			}
		}
		
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
	
		return mapping.findForward("activity");
	}


	/**
	*  ����/��עĳһ�
	* @param mapping ·��ӳ�����
	* @param form ������
	* @param request �������
	* @param response ��Ӧ����
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doJoinOrCareActivity(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
	
		//��ʼ��
		ErrorInformation errInfo=new ActivityErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		int joiner_id=requ.getInt("joiner_id");
		int status=requ.getInt("status");
	
		//��ȡ��¼�û���Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)//����û��Ƿ��¼
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		else{
			try{
					//���û�г�����ɾ��ԭ����Ϣ���ٲ�������Ϣ
					int det=JoinActivityDAO.deleteJoinAndCareAct(id,joiner_id);		
					int ret=JoinActivityDAO.joinActivity(id,joiner_id,status);
					if(ret<1)
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				
					//��μ�ĳһ����û����ּ�10
					if(errInfo.isEmpty()){
						int rt=AuthUserDAO.addScore(loginUser, Utils.SCORE_JOIN_ACTIVITY);
							if(rt<1)
							errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
						}
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
			}
		}
		
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/activity/readact.jsp?actid="+id, true);
	}



	/**
	*  ����/��עĳһ�,�ڲ鿴���ϸҳ��
	* @param mapping ·��ӳ�����
	* @param form ������
	* @param request �������
	* @param response ��Ӧ����
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doJoinOrCareActivityRead(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {
	
				//��ʼ��
				ErrorInformation errInfo=new ActivityErrInfo();
				RequestUtils requ=RequestUtils.getInstance(request);
		
				int id=requ.getInt("id");
				int joiner_id=requ.getInt("joiner_id");
				int status=requ.getInt("status");
	
				UserInfo loginUser = UserInfo.getLoginUser(request);
				if(loginUser==null)//����û��Ƿ��¼
					errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
				else{
						try{
						//���û�г�����ɾ��ԭ����Ϣ���ٲ�������Ϣ
						int det=JoinActivityDAO.deleteJoinAndCareAct(id,joiner_id);		
						int ret=JoinActivityDAO.joinActivity(id,joiner_id,status);
						if(ret<1)
							errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
							
             //��μ�ĳһ����û����ּ�10
						if(errInfo.isEmpty()){
									int rt=AuthUserDAO.addScore(loginUser, Utils.SCORE_JOIN_ACTIVITY);
									if(rt<1)
											errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
							}
	
				
						}catch(SQLException e){
							Logger.Log(Logger.LOG_ERROR,e);
							errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
						}
					}
		
				if(!errInfo.isEmpty()){
						errInfo.saveToRequest("err",request);
						return mapping.findForward("error");
				}
	
				return new ActionForward("/activity/readact.jsp?actid="+id, true);
			}


	/**
	* ɾ������/��ע���Ϣ���Ӳ����б�͹�ע��б���ɾ����
	* @param mapping ·��ӳ�����
	* @param form ������
	* @param request �������
	* @param response ��Ӧ����
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doDeleteJoinAct(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {

		//��ʼ��
		ErrorInformation errInfo=new ActivityErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
	
		int id=requ.getInt("id");
		int joiner_id=requ.getInt("joiner_id");
		int status=requ.getInt("status");
		
		//��ȡ��¼�û�����ɫ��Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)//����û��Ƿ��¼
				errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		else{
				try{
						//���û�г�����ɾ��
						int ret=JoinActivityDAO.deleteJoinAct(id,joiner_id,status);
						if(ret<1)
						errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
					
						//����б���ɾ��ĳһ����û����ּ�10
						if(errInfo.isEmpty()){
								int rt=AuthUserDAO.addScore(loginUser, -Utils.SCORE_JOIN_ACTIVITY);
								if(rt<1)
										errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
							}
					
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
			}
		}
	
		if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
		}

	
		return new ActionForward("/activity/index.jsp", true);
	}



	/**
		 * ɾ������/��ע���Ϣ�������ݿ���ɾ����
		 * @param mapping ·��ӳ�����
		 * @param form ������
		 * @param request �������
		 * @param response ��Ӧ����
		 * @return ActionForward
		 * @throws Exception
	 */
		public ActionForward doDeleteAct(
			      ActionMapping mapping,
			      ActionForm form,
			      HttpServletRequest request,
			      HttpServletResponse response)
			      throws Exception {

			//��ʼ��
			ErrorInformation errInfo=new ActivityErrInfo();
			RequestUtils requ=RequestUtils.getInstance(request);
	
			int id=requ.getInt("actid");
		
		  //��ȡ��¼�û���Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			if(loginUser==null)//����û��Ƿ��¼
				 errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
			else{
				 try{
					 //���û�г�����ɾ��
				   int ret=ActivityDAO.deleteActivity(id);
				   if(ret<1)
					 errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
			
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
				}
			}
	
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}

		
			return new ActionForward("/activity/index.jsp", true);
		}




	/**
	* ɾ�������
	* @param mapping ·��ӳ�����
	* @param form ������
	* @param request �������
	* @param response ��Ӧ����
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doDeleteJoinMes(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {

			//��ʼ��
			ErrorInformation errInfo=new ActivityErrInfo();
			RequestUtils requ=RequestUtils.getInstance(request);
	
			int id=requ.getInt("actid");
			int joiner_id=requ.getInt("joiner_id");
			int status=requ.getInt("status");
		  String  mes=requ.getString("mestime");
      Timestamp mestime=Timestamp.valueOf(mes);
		
			//��ȡ��¼�û���Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			if(loginUser==null)//����û��Ƿ��¼
					errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
			else{
					try{
					//���û�г�����ɾ��
				  int ret=JoinActivityDAO.deleteJoinMes(id,joiner_id,status,mestime);
				  if(ret<1)
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
					
					//��ɾ���������£��û����ּ�2
					if(errInfo.isEmpty()){
							int rt=AuthUserDAO.addScore(loginUser, -Utils.SCORE_MESSAGE_ACTIVITY);
							 if(rt<1)
									errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
						}
	
			
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
				}
			}
	   if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}

			return mapping.findForward("actread");
		}



	/**
	* ��ĳ�����
	* @param mapping ·��ӳ�����
	* @param form ������
	* @param request �������
	* @param response ��Ӧ����
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doAdd(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {
			
			//��ȡ��ʼ����Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			
			ErrorInformation errInfo=new ActivityErrInfo();
			RequestUtils requ=RequestUtils.getInstance(request);
			Timestamp now=new Timestamp(System.currentTimeMillis());
		
			int id=requ.getInt("id");
			int actid=requ.getInt("actid");
			String content=requ.getString("message");
		  String authorname=requ.getString("authorname");
		  String joinerschool=requ.getString("joinerschool");
		  
		  if(loginUser==null)
					errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
			//��鵱ǰ�û��Ƿ��������û�
			else	if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
					return mapping.findForward("profile");
			//�������ݲ���Ϊ��
			else	if(StringUtils.isNull(content))
					errInfo.saveError(ActivityErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
					try{
					//��鱻���Ե��û��Ƿ����
					if(!AuthUserDAO.isExistUser(id)){
							errInfo.saveError(ActivityErrInfo.OPERATION_NOT_ALLOW);
						}else{
						//����������Ϣ����
						content=content.replaceAll(" ", "&nbsp");
						content=content.replaceAll("\n", "<br>");
					
						JoinActivityInfo board=new JoinActivityInfo();
						board.setActId(actid);
						board.setJoinerId(id);
						board.setJoinername(authorname);
						board.setJointime(now);
						board.setMessage(content);
						board.setMestime(now);
						board.setStatus(Utils.ACTIVITY_MESSAGE);
						board.setJoinerschool(joinerschool);
						
						int ret=JoinActivityDAO.insertMessage(board);
						if(ret!=1)
							errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
					}
					
					//���������£��û����ּ�2
					if(errInfo.isEmpty()){
					int rt=AuthUserDAO.addScore(loginUser, Utils.SCORE_MESSAGE_ACTIVITY);
							if(rt<1)
										errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
					}
									
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(ActivityErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
		
			return mapping.findForward("actread");
		}
	


	
}
