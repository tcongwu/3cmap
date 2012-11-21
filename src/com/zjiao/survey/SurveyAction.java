package com.zjiao.survey;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zjiao.apply.TeacherDAO;
import com.zjiao.auth.Role;
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
public class SurveyAction extends ActionExtend {
	
	/**
	 * ��������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception �쳣
	 */
	public ActionForward doNewSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			SurveyInfo survey = (SurveyInfo)form;
			ErrorInformation errInfo=new SurveyErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginRole==null))//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(survey.getTitle())||StringUtils.isNull(survey.getType())||(survey.getCnt()<2)||StringUtils.isNull(survey.getContent())||StringUtils.isNull(survey.getA())||StringUtils.isNull(survey.getB()))
				errInfo.saveError(SurveyErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				
				try{
					//����������Ϣ����
					survey.setStatus(SysUtils.STATUS_NORMAL);
					survey.setAuthor(loginUser.getUserId());

					//�������������ݿ�
					int ret=SurveyDAO.newQues(survey, loginRole);
					if(ret<1){
						errInfo.saveError(SurveyErrInfo.ERR_UNKNOWN);
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
			return mapping.getInputForward();
	}
	
	/**
	 * �޸�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception �쳣
	 */
	public ActionForward doModSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			SurveyInfo survey = (SurveyInfo)form;
			ErrorInformation errInfo=new SurveyErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginRole==null))//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(survey.getTitle())||StringUtils.isNull(survey.getType())||(survey.getCnt()<2)||StringUtils.isNull(survey.getContent())||StringUtils.isNull(survey.getA())||StringUtils.isNull(survey.getB()))
				errInfo.saveError(SurveyErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				
				try{
					//����������Ϣ����
					survey.setAuthor(loginUser.getUserId());

					//�������������ݿ�
					int ret=SurveyDAO.modQues(survey, loginRole);
					if(ret<1){
						errInfo.saveError(SurveyErrInfo.ERR_UNKNOWN);
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
			
			return mapping.getInputForward();
	}
	
	/**
	 * ����ӵ�����������վ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRemoveSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��������Ϣ
			ErrorInformation errInfo=new SurveyErrInfo();
			//��ȡ��¼����
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//��һ��ȡҪ�������վ�����⣬���޸����ݿ�
					SurveyInfo survey=new SurveyInfo();
					survey.setStatus(SysUtils.STATUS_INVALID);
					survey.setAuthor(loginUser.getUserId());
					
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							survey.setId(requ.getInt("id"+i));
							SurveyDAO.setQuesStatus(survey, loginRole);
						}
					}
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
		
			return mapping.getInputForward();
	}
	
	/**
	 * ����ӵ�����ӻ���վ�лָ�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRecycleSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��������Ϣ
			ErrorInformation errInfo=new SurveyErrInfo();
			//��ȡ��¼����
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//��һ��ȡ�ӻ���վ�лָ������⣬���޸����ݿ�
					SurveyInfo survey=new SurveyInfo();
					survey.setStatus(SysUtils.STATUS_NORMAL);
					survey.setAuthor(loginUser.getUserId());
					
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							survey.setId(requ.getInt("id"+i));
							SurveyDAO.setQuesStatus(survey, loginRole);
						}
					}
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
		
			return mapping.getInputForward();
	}
	
	/**
	 * ��ʼ�µ���
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doBeginSurvey(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��������Ϣ
			ErrorInformation errInfo=new SurveyErrInfo();
			//��ȡ��¼����
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
			//��ȡ�༶ID
			int cid=requ.getInt("cid");
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if((cid>0)&&(!TeacherDAO.isExistClass(loginRole.getId(), cid)))
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//��һ��ȡ��Ҫ�����ĵ��飬���޸����ݿ�
					SurveyInfo survey=new SurveyInfo();
					survey.setClassId(cid);
					
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							survey.setQuesId(requ.getInt("id"+i));
							SurveyDAO.beginSurvey(survey, loginRole);
						}
					}
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
		
			return mapping.getInputForward();
	}
	
	/**
	 * ����ѡ�е���
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doCloseSurvey(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��������Ϣ
			ErrorInformation errInfo=new SurveyErrInfo();
			//��ȡ��¼����
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
			//��ȡ�༶ID
			int cid=requ.getInt("cid");
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if((cid>0)&&(!TeacherDAO.isExistClass(loginRole.getId(), cid)))
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//��һ��ȡ��Ҫ�����ĵ��飬���޸����ݿ�
					SurveyInfo survey=new SurveyInfo();
					survey.setClassId(cid);
					
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							survey.setId(requ.getInt("id"+i));
							SurveyDAO.closeSurvey(survey, loginRole);
						}
					}
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
		
			return mapping.getInputForward();
	}
	
	/**
	 * ���õ���״̬���Ƿ񹫲������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSetSurvey(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��������Ϣ
			ErrorInformation errInfo=new SurveyErrInfo();
			//��ȡ��¼����
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
			//��ȡ�༶ID
			int cid=requ.getInt("cid");
			System.out.println(cid);
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//����û��Ƿ��¼
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else//����û��Ƿ�ӵ����Ӧ��Ȩ��
			if((cid>0)&&(!TeacherDAO.isExistClass(loginRole.getId(), cid)))
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//��һ��ȡ��Ҫ����״̬�ĵ��飬���޸����ݿ�
					SurveyInfo survey=new SurveyInfo();
					survey.setClassId(cid);
					
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							survey.setId(requ.getInt("id"+i));
							survey.setIsShowResult(requ.getInt("isShowResult"+i));
							System.out.println(requ.getInt("id"+i)+":"+requ.getInt("isShowResult"+i));
							SurveyDAO.setSurvey(survey, loginRole);
						}
					}
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
				}
			}
			
			if(!errInfo.isEmpty()){
				errInfo.saveToRequest("err",request);
				return mapping.findForward("error");
			}
		
			return mapping.getInputForward();
	}
	
	/**
	 * ��С����ͶƱ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doPoll(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ʼ��
		ErrorInformation errInfo=new SurveyErrInfo();
		//��ȡ���������Լ���������
		RequestUtils requ=RequestUtils.getInstance(request);
		int cnt=requ.getInt("cnt");
		int isCheck=requ.getInt("isCheck");
		if(cnt<=0){
			return mapping.getInputForward();
		}
		//��ȡ����ID�Լ����������ͺ�ͶƱ���
		int ids[]=new int[cnt];
		int type[]=new int[cnt];
		String results[]=new String[cnt];
		String temps[];
		int i, j;
		for(i=0;i<cnt;i++){
			if(StringUtils.isNull(request.getParameter("id"+i))||StringUtils.isNull(request.getParameter("t"+i))){
				return mapping.getInputForward();
			}
			ids[i]=requ.getInt("id"+i);
			type[i]=requ.getInt("t"+i);
			if(isCheck!=1){
				if(StringUtils.isNull(request.getParameter("a"+i))){
					return mapping.getInputForward();
				}
				if(type[i]==1){
					results[i]=request.getParameter("a"+i);
				}
				else if(requ.getInt("t"+i)==2){
					temps=request.getParameterValues("a"+i);
					if((temps==null)||(temps.length==0)){
						results[i]=null;
					}else{
						for(j=0;j<temps.length;j++){
							if(j==0){
								results[i]=temps[j];
							}else{
								results[i]=results[i]+temps[j];
							}
						}
					}
				}
			}
		}
		
		//��ȡ��¼�û�����ɫ��Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		Role loginRole = Role.getUserRole(request);
		ArrayList forShow=new ArrayList();
		
		if(loginUser==null)//����û��Ƿ��¼
			errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
		else//����û��Ƿ��н�ʦȨ��
		if(loginRole==null)
			errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
		else{
			try{
				//��ȡ���������������ͶƱ��Ϣ
				Hashtable surveys=SurveyDAO.getCurrrentSurvey(loginRole);
			
				//ѭ�����ѻ�ȡ���������µļ�Ʊ���
				SurveyInfo survey=null;
				for(i=0;i<cnt;i++){
					survey=(SurveyInfo)surveys.get(new Integer(ids[i]));
					if(survey==null){
						return mapping.getInputForward();
					}//������ǡ��鿴�������Ҳ���ģ���ɫ����ͶƱ
					else if((isCheck!=1)&&(loginRole.getId()>0)){
						survey.poll(results[i], loginRole);
					}
					//�����ǰ�������ʾ״̬Ϊ1���������ʾ�б��У��Ա��ڽ����ҳ����ʾ������
					if(survey.getIsShowResult()==1){
						forShow.add(survey);
					}
				}
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(SurveyErrInfo.DATABASE_ERR);
			}
			
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		request.setAttribute("results", forShow);
		return mapping.getInputForward();
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
