
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


	/**
	* 添加活动
	* @param mapping 路径映射对象
	* @param form 表单对象
	* @param request 请求对象
	* @param response 响应对象
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
		
		//初始化
		ActivityInfo act = (ActivityInfo)form;
		 
		ErrorInformation errInfo=new ActivityErrInfo();
	    
		//转换时间格式
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
 
		//获取登录用户
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)
			//检查用户是否登录
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		//检查活动名称,活动地点和和活动内容，它们不能为空
		else if(StringUtils.isNull(act.getActname())||StringUtils.isNull(act.getActplace())||StringUtils.isNull(act.getActcontent()))
			errInfo.saveError(ActivityErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			//完善act对象
			act.setAuthorId(loginUser.getId());
			act.setAuthorname(loginUser.getRealName());
			act.setStart(start);
		    act.setEnd(end);
			act.setStatus(Utils.ACTIVITY_NORMAL);//1为正常活动,2表示热点活动;
			
			try{
	  
				//如果没有出错，则保存到数据库
				int ret=ActivityDAO.createActivity(act);
				if(ret<1){
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				}
				
				//获得活动id,以便跳转到readact.jsp;
				actid=ActivityDAO.getActIdByActname(act.getActname(),act.getAuthorname(),now);
				if(actid<0){
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				}
				
				//因创建一个活动，用户积分减100
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
	 * 修改一个活动
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doEditact(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		//获取初始化信息
		RequestUtils requ=RequestUtils.getInstance(request);
		ErrorInformation errInfo=new ActivityErrInfo();
  
		int actid=requ.getInt("actid");
  
		String actname=request.getParameter("actname");
		String actplace=request.getParameter("actplace");
		String actcontent=request.getParameter("actcontent");
		String phone=request.getParameter("phone");
		String email=request.getParameter("email");
  
   
 
		//转换时间格式
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
	
		//获取登录用户信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		
		if(loginUser==null)//检查用户是否登录
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		//检查活动名称,活动地点和和活动内容，它们不能为空
		else if(StringUtils.isNull(actname)||StringUtils.isNull(actplace)||StringUtils.isNull(actcontent))
				errInfo.saveError(ActivityErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			//完善act对象
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
	 * 更新参与/关注活动状态
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doUpdateActivityStatus(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		//初始化信息
		ErrorInformation errInfo=new ActivityErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		int joiner_id=requ.getInt("joiner_id");
		int status=requ.getInt("status");
		
		//获取登录用户信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)//检查用户是否登录
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		else{
			try{
				//如果没有出错，则更改
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
	*  参与/关注某一活动
	* @param mapping 路径映射对象
	* @param form 表单对象
	* @param request 请求对象
	* @param response 响应对象
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doJoinOrCareActivity(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
	
		//初始化
		ErrorInformation errInfo=new ActivityErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		int joiner_id=requ.getInt("joiner_id");
		int status=requ.getInt("status");
	
		//获取登录用户信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)//检查用户是否登录
			errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		else{
			try{
					//如果没有出错，先删除原有信息，再插入新信息
					int det=JoinActivityDAO.deleteJoinAndCareAct(id,joiner_id);		
					int ret=JoinActivityDAO.joinActivity(id,joiner_id,status);
					if(ret<1)
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
				
					//因参加某一活动，用户积分加10
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
	*  参与/关注某一活动,在查看活动详细页面
	* @param mapping 路径映射对象
	* @param form 表单对象
	* @param request 请求对象
	* @param response 响应对象
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doJoinOrCareActivityRead(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {
	
				//初始化
				ErrorInformation errInfo=new ActivityErrInfo();
				RequestUtils requ=RequestUtils.getInstance(request);
		
				int id=requ.getInt("id");
				int joiner_id=requ.getInt("joiner_id");
				int status=requ.getInt("status");
	
				UserInfo loginUser = UserInfo.getLoginUser(request);
				if(loginUser==null)//检查用户是否登录
					errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
				else{
						try{
						//如果没有出错，先删除原有信息，再插入新信息
						int det=JoinActivityDAO.deleteJoinAndCareAct(id,joiner_id);		
						int ret=JoinActivityDAO.joinActivity(id,joiner_id,status);
						if(ret<1)
							errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
							
             //因参加某一活动，用户积分加10
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
	* 删除参与/关注活动信息（从参与活动列表和关注活动列表中删除）
	* @param mapping 路径映射对象
	* @param form 表单对象
	* @param request 请求对象
	* @param response 响应对象
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doDeleteJoinAct(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {

		//初始化
		ErrorInformation errInfo=new ActivityErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
	
		int id=requ.getInt("id");
		int joiner_id=requ.getInt("joiner_id");
		int status=requ.getInt("status");
		
		//获取登录用户及角色信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		if(loginUser==null)//检查用户是否登录
				errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
		else{
				try{
						//如果没有出错，则删除
						int ret=JoinActivityDAO.deleteJoinAct(id,joiner_id,status);
						if(ret<1)
						errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
					
						//因从列表中删除某一活动，用户积分减10
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
		 * 删除参与/关注活动信息（从数据库中删除）
		 * @param mapping 路径映射对象
		 * @param form 表单对象
		 * @param request 请求对象
		 * @param response 响应对象
		 * @return ActionForward
		 * @throws Exception
	 */
		public ActionForward doDeleteAct(
			      ActionMapping mapping,
			      ActionForm form,
			      HttpServletRequest request,
			      HttpServletResponse response)
			      throws Exception {

			//初始化
			ErrorInformation errInfo=new ActivityErrInfo();
			RequestUtils requ=RequestUtils.getInstance(request);
	
			int id=requ.getInt("actid");
		
		  //获取登录用户信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			if(loginUser==null)//检查用户是否登录
				 errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
			else{
				 try{
					 //如果没有出错，则删除
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
	* 删除活动留言
	* @param mapping 路径映射对象
	* @param form 表单对象
	* @param request 请求对象
	* @param response 响应对象
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doDeleteJoinMes(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {

			//初始化
			ErrorInformation errInfo=new ActivityErrInfo();
			RequestUtils requ=RequestUtils.getInstance(request);
	
			int id=requ.getInt("actid");
			int joiner_id=requ.getInt("joiner_id");
			int status=requ.getInt("status");
		  String  mes=requ.getString("mestime");
      Timestamp mestime=Timestamp.valueOf(mes);
		
			//获取登录用户信息
			UserInfo loginUser = UserInfo.getLoginUser(request);
			if(loginUser==null)//检查用户是否登录
					errInfo.saveError(ActivityErrInfo.NEED_LOGIN);
			else{
					try{
					//如果没有出错，则删除
				  int ret=JoinActivityDAO.deleteJoinMes(id,joiner_id,status,mestime);
				  if(ret<1)
					errInfo.saveError(ActivityErrInfo.ERR_UNKNOWN);
					
					//因删除留言文章，用户积分减2
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
	* 给某活动留言
	* @param mapping 路径映射对象
	* @param form 表单对象
	* @param request 请求对象
	* @param response 响应对象
	* @return ActionForward
	* @throws Exception
	*/
	public ActionForward doAdd(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {
			
			//获取初始化信息
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
			//检查当前用户是否是正常用户
			else	if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
					return mapping.findForward("profile");
			//留言内容不能为空
			else	if(StringUtils.isNull(content))
					errInfo.saveError(ActivityErrInfo.NEEDED_FIELD_NOT_NULL);
			else{
					try{
					//检查被留言的用户是否存在
					if(!AuthUserDAO.isExistUser(id)){
							errInfo.saveError(ActivityErrInfo.OPERATION_NOT_ALLOW);
						}else{
						//生成留言信息对象
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
					
					//因留言文章，用户积分加2
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
