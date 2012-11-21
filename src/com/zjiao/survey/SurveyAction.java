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
 * 调查相关操作Action类
 */
public class SurveyAction extends ActionExtend {
	
	/**
	 * 新添问题
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception 异常
	 */
	public ActionForward doNewSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			SurveyInfo survey = (SurveyInfo)form;
			ErrorInformation errInfo=new SurveyErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginRole==null))//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(survey.getTitle())||StringUtils.isNull(survey.getType())||(survey.getCnt()<2)||StringUtils.isNull(survey.getContent())||StringUtils.isNull(survey.getA())||StringUtils.isNull(survey.getB()))
				errInfo.saveError(SurveyErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				
				try{
					//完善问题信息对象
					survey.setStatus(SysUtils.STATUS_NORMAL);
					survey.setAuthor(loginUser.getUserId());

					//保存问题入数据库
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
	 * 修改问题
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception 异常
	 */
	public ActionForward doModSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			SurveyInfo survey = (SurveyInfo)form;
			ErrorInformation errInfo=new SurveyErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginRole==null))//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(survey.getTitle())||StringUtils.isNull(survey.getType())||(survey.getCnt()<2)||StringUtils.isNull(survey.getContent())||StringUtils.isNull(survey.getA())||StringUtils.isNull(survey.getB()))
				errInfo.saveError(SurveyErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				
				try{
					//完善问题信息对象
					survey.setAuthor(loginUser.getUserId());

					//保存问题入数据库
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
	 * 把添加的问题放入回收站
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRemoveSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化错误信息
			ErrorInformation errInfo=new SurveyErrInfo();
			//获取记录总数
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查用户是否拥有相应的权限
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//逐一获取要放入回收站的问题，并修改数据库
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
	 * 把添加的问题从回收站中恢复过来
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doRecycleSurveyQues(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化错误信息
			ErrorInformation errInfo=new SurveyErrInfo();
			//获取记录总数
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查用户是否拥有相应的权限
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//逐一获取从回收站中恢复的问题，并修改数据库
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
	 * 开始新调查
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doBeginSurvey(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化错误信息
			ErrorInformation errInfo=new SurveyErrInfo();
			//获取记录总数
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
			//获取班级ID
			int cid=requ.getInt("cid");
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查用户是否拥有相应的权限
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else//检查用户是否拥有相应的权限
			if((cid>0)&&(!TeacherDAO.isExistClass(loginRole.getId(), cid)))
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//逐一获取需要结束的调查，并修改数据库
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
	 * 结束选中调查
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doCloseSurvey(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化错误信息
			ErrorInformation errInfo=new SurveyErrInfo();
			//获取记录总数
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
			//获取班级ID
			int cid=requ.getInt("cid");
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查用户是否拥有相应的权限
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else//检查用户是否拥有相应的权限
			if((cid>0)&&(!TeacherDAO.isExistClass(loginRole.getId(), cid)))
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//逐一获取需要结束的调查，并修改数据库
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
	 * 设置调查状态（是否公布结果）
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSetSurvey(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化错误信息
			ErrorInformation errInfo=new SurveyErrInfo();
			//获取记录总数
			RequestUtils requ=RequestUtils.getInstance(request);
			int cnt=requ.getInt("rowcnt");
			if(cnt<=0){
				return mapping.getInputForward();
			}
			//获取班级ID
			int cid=requ.getInt("cid");
			System.out.println(cid);
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if(loginUser==null)//检查用户是否登录
				errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
			else//检查用户是否拥有相应的权限
			if(loginRole==null)
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else//检查用户是否拥有相应的权限
			if((cid>0)&&(!TeacherDAO.isExistClass(loginRole.getId(), cid)))
				errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
			else{
				try{
					//逐一获取需要设置状态的调查，并修改数据库
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
	 * 给小调查投票
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doPoll(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//初始化
		ErrorInformation errInfo=new SurveyErrInfo();
		//获取问题总数以及动作类型
		RequestUtils requ=RequestUtils.getInstance(request);
		int cnt=requ.getInt("cnt");
		int isCheck=requ.getInt("isCheck");
		if(cnt<=0){
			return mapping.getInputForward();
		}
		//获取调查ID以及各问题类型和投票结果
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
		
		//获取登录用户及角色信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		Role loginRole = Role.getUserRole(request);
		ArrayList forShow=new ArrayList();
		
		if(loginUser==null)//检查用户是否登录
			errInfo.saveError(SurveyErrInfo.NEED_LOGIN);
		else//检查用户是否有教师权限
		if(loginRole==null)
			errInfo.saveError(SurveyErrInfo.CANT_ACCESS);
		else{
			try{
				//获取调查中所有问题的投票信息
				Hashtable surveys=SurveyDAO.getCurrrentSurvey(loginRole);
			
				//循环，已获取整个调查新的记票结果
				SurveyInfo survey=null;
				for(i=0;i<cnt;i++){
					survey=(SurveyInfo)surveys.get(new Integer(ids[i]));
					if(survey==null){
						return mapping.getInputForward();
					}//如果不是【查看】，而且不是模拟角色，则投票
					else if((isCheck!=1)&&(loginRole.getId()>0)){
						survey.poll(results[i], loginRole);
					}
					//如果当前调查的显示状态为1，则放入显示列表中，以便在结果网页中显示调查结果
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
