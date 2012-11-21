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
 * 讨论相关操作Action类
 */
public class BBSAction extends ActionExtend {
	
	/**
	 * 参与讨论
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doJoinDiscuss(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			BBSInfo bbs = (BBSInfo)form;
			ErrorInformation errInfo=new BBSErrInfo();
		
			//获取登录用户及角色信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			Role loginRole = Role.getUserRole(request);
		
			if((loginUser==null)||(loginRole==null))//检查用户是否登录
				errInfo.saveError(BBSErrInfo.NEED_LOGIN);
			else//检查主题和内容，它们不能为空
			if(StringUtils.isNull(bbs.getTitle())||StringUtils.isNull(bbs.getContent()))
				errInfo.saveError(BBSErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
				//完善bbs对象
				bbs.setAuthorId(loginRole.getId());
				bbs.setAuthorType(loginRole.getType());
				bbs.setAuthorName(loginRole.getString("realname"));
				
				try{
					
					if(loginRole.getId()>0){
						//如果没有出错，则保存测验入数据库
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
	 * 置顶讨论
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doTopDiscuss(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//初始化
		ErrorInformation errInfo=new BBSErrInfo();
		String url = request.getParameter("url");
		ActionForward forward = null;
		if(StringUtils.isEmpty(url)){
			forward = mapping.findForward("home");
		}else{
			forward = new ActionForward(url);
		}
		//获取文章总数
		RequestUtils requ=RequestUtils.getInstance(request);
		int cnt=requ.getInt("rowcnt");
		if(cnt<=0){
			return forward;
		}
		
		//获取登录用户及角色信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		Role loginRole = Role.getUserRole(request);
		
		if(loginUser==null)//检查用户是否登录
			errInfo.saveError(BBSErrInfo.NEED_LOGIN);
		else//检查用户是否有教师权限
		if((loginRole==null)||!loginRole.isRole(Role.ROLE_TEACHER))
			errInfo.saveError(BBSErrInfo.CANT_ACCESS);
		else{
			//获取班级ID和学期ID
			int classid=requ.getInt("cid");
			int va=1;
			if(requ.getInt("va")>0){
				va=0;
			}
			
			//获取当前班级的基本信息对象
			ClassInfo cls=ClassDAO.getBaseClassInfo(classid);
			//只有班级的创建教师才能有权限置顶班级讨论区的文章
			if((cls==null)||(cls.getTeacherId()!=loginRole.getId())){
				errInfo.saveError(BBSErrInfo.CANT_ACCESS);
			}
			else{
				try{
					//逐一获取要置顶的文章，并更新数据库
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
	 * 删除讨论
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDeleteDiscuss(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//初始化
		ErrorInformation errInfo=new BBSErrInfo();
		String url = request.getParameter("url");
		ActionForward forward = null;
		if(StringUtils.isEmpty(url)){
			forward = mapping.findForward("home");
		}else{
			forward = new ActionForward(url);
		}
		//获取文章总数
		RequestUtils requ=RequestUtils.getInstance(request);
		int cnt=requ.getInt("rowcnt");
		if(cnt<=0){
			return forward;
		}
		
		//获取登录用户及角色信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		Role loginRole = Role.getUserRole(request);
		
		if(loginUser==null)//检查用户是否登录
			errInfo.saveError(BBSErrInfo.NEED_LOGIN);
		else//检查用户是否有教师权限
		if((loginRole==null)||!loginRole.isRole(Role.ROLE_TEACHER))
			errInfo.saveError(BBSErrInfo.CANT_ACCESS);
		else{
			//获取班级ID和学期ID
			int classid=requ.getInt("cid");
			boolean cas=false;
			if(requ.getInt("cas")==1){
				cas=true;
			}
			
			//获取当前班级的基本信息对象
			ClassInfo cls=ClassDAO.getBaseClassInfo(classid);
			//只有班级的创建教师才能有权限删除班级讨论区的文章
			if((cls==null)||(cls.getTeacherId()!=loginRole.getId())){
				errInfo.saveError(BBSErrInfo.CANT_ACCESS);
			}
			else{
				try{
					//逐一获取要删除的文章，并修改数据库
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
