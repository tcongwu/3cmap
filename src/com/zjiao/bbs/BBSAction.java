package com.zjiao.bbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zjiao.apply.ClassDAO;
import com.zjiao.apply.ClassInfo;
import com.zjiao.auth.Role;
import com.zjiao.auth.UserInfo;
import com.zjiao.struts.ActionExtend;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.util.RequestUtils;
import com.zjiao.util.StringUtils;

/** 
 * ������ز���Action��
 */
public class BBSAction extends ActionExtend {
	
	/**
	 * ��������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doJoinDiscuss(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			BBSInfo bbs = (BBSInfo)form;
			ErrorInformation errInfo=new BBSErrInfo();
		
			//��ȡ��¼�û�����ɫ��Ϣ
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginRole==null))//����û��Ƿ��¼
				errInfo.saveError(BBSErrInfo.NEED_LOGIN);
			else//�����������ݣ����ǲ���Ϊ��
			if(StringUtils.isNull(bbs.getTitle())||StringUtils.isNull(bbs.getContent()))
				errInfo.saveError(BBSErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//����bbs����
				bbs.setAuthorId(loginRole.getId());
				bbs.setAuthorType(loginRole.getType());
				bbs.setAuthorName(loginRole.getString("realname"));
				
				try{
					
					if(loginRole.getId()>0){
						//���û�г����򱣴���������ݿ�
						int ret=BBSDAO.insertDiscuss(bbs);
						if(ret<1){
							errInfo.saveError(BBSErrInfo.ERR_UNKNOWN);
						}
					
						if(bbs.getParentId()>0){
							BBSDAO.increaseReplyCount(bbs.getParentId(), bbs.getClassId());
						}
					}
				
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(BBSErrInfo.DATABASE_ERR);
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
	
	/**
	 * �ö�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doTopDiscuss(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ʼ��
		ErrorInformation errInfo=new BBSErrInfo();
		String url = request.getParameter("url");
		ActionForward forward = null;
		if(StringUtils.isEmpty(url)){
			forward = mapping.findForward("home");
		}else{
			forward = new ActionForward(url);
		}
		//��ȡ��������
		RequestUtils requ=RequestUtils.getInstance(request);
		int cnt=requ.getInt("rowcnt");
		if(cnt<=0){
			return forward;
		}
		
		//��ȡ��¼�û�����ɫ��Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		Role loginRole = Role.getUserRole(request);
		
		if(loginUser==null)//����û��Ƿ��¼
			errInfo.saveError(BBSErrInfo.NEED_LOGIN);
		else//����û��Ƿ��н�ʦȨ��
		if((loginRole==null)||!loginRole.isRole(Role.ROLE_TEACHER))
			errInfo.saveError(BBSErrInfo.CANT_ACCESS);
		else{
			//��ȡ�༶ID��ѧ��ID
			int classid=requ.getInt("cid");
			int va=1;
			if(requ.getInt("va")>0){
				va=0;
			}
			
			//��ȡ��ǰ�༶�Ļ�����Ϣ����
			ClassInfo cls=ClassDAO.getBaseClassInfo(classid);
			//ֻ�а༶�Ĵ�����ʦ������Ȩ���ö��༶������������
			if((cls==null)||(cls.getTeacherId()!=loginRole.getId())){
				errInfo.saveError(BBSErrInfo.CANT_ACCESS);
			}
			else{
				try{
					//��һ��ȡҪ�ö������£����������ݿ�
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							BBSDAO.setTopValue(requ.getInt("id"+i), va, classid);
						}
					}
					
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(BBSErrInfo.DATABASE_ERR);
				}
			}
			
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}

		return forward;
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
	public ActionForward doDeleteDiscuss(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ʼ��
		ErrorInformation errInfo=new BBSErrInfo();
		String url = request.getParameter("url");
		ActionForward forward = null;
		if(StringUtils.isEmpty(url)){
			forward = mapping.findForward("home");
		}else{
			forward = new ActionForward(url);
		}
		//��ȡ��������
		RequestUtils requ=RequestUtils.getInstance(request);
		int cnt=requ.getInt("rowcnt");
		if(cnt<=0){
			return forward;
		}
		
		//��ȡ��¼�û�����ɫ��Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		Role loginRole = Role.getUserRole(request);
		
		if(loginUser==null)//����û��Ƿ��¼
			errInfo.saveError(BBSErrInfo.NEED_LOGIN);
		else//����û��Ƿ��н�ʦȨ��
		if((loginRole==null)||!loginRole.isRole(Role.ROLE_TEACHER))
			errInfo.saveError(BBSErrInfo.CANT_ACCESS);
		else{
			//��ȡ�༶ID��ѧ��ID
			int classid=requ.getInt("cid");
			boolean cas=false;
			if(requ.getInt("cas")==1){
				cas=true;
			}
			
			//��ȡ��ǰ�༶�Ļ�����Ϣ����
			ClassInfo cls=ClassDAO.getBaseClassInfo(classid);
			//ֻ�а༶�Ĵ�����ʦ������Ȩ��ɾ���༶������������
			if((cls==null)||(cls.getTeacherId()!=loginRole.getId())){
				errInfo.saveError(BBSErrInfo.CANT_ACCESS);
			}
			else{
				try{
					//��һ��ȡҪɾ�������£����޸����ݿ�
					List dels=new ArrayList();
					dels.clear();
					for(int i=0;i<cnt;i++){
						if(!StringUtils.isNull(request.getParameter("id"+i))){
							dels.add(new Integer(requ.getInt("id"+i)));
						}
					}
					
					BBSDAO.deleteDiscuss(dels, classid, cas);
					
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(BBSErrInfo.DATABASE_ERR);
				}
			}
			
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}

		return forward;
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
