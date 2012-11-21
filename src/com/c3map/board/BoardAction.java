package com.c3map.board;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.zjiao.auth.AuthUserDAO;
import com.zjiao.auth.UserInfo;
import com.zjiao.struts.ActionExtend;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.SysUtils;
import com.zjiao.util.RequestUtils;
import com.zjiao.util.StringUtils;

/** 
 * 留言板相关操作Action类
 */
public class BoardAction extends ActionExtend {
	
	/**
	 * 给人留言
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
		ErrorInformation errInfo=new BoardErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		String content=requ.getString("message");
		
	  if(loginUser==null)
			errInfo.saveError(BoardErrInfo.NEED_LOGIN);
		else//检查当前用户是否是正常用户
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else//留言内容不能为空
		if(StringUtils.isNull(content))
			errInfo.saveError(BoardErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			try{
				//检查被留言的用户是否存在
				if(!AuthUserDAO.isExistUser(id)){
					errInfo.saveError(BoardErrInfo.OPERATION_NOT_ALLOW);
				}else{
					//生成留言信息对象
					content=content.replaceAll(" ", "&nbsp");
					content=content.replaceAll("\n", "<br>");
					
					BoardInfo board=new BoardInfo();
					board.setUserId(id);
					board.setLeaveId(loginUser.getId());
					board.setRealName(loginUser.getRealName());
					board.setPhoto(loginUser.getPhoto());
					board.setSchool(loginUser.getSchool());
					board.setContent(content);
					
					int ret=BoardDAO.leaveWord(board);
					if(ret!=1)
						errInfo.saveError(BoardErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(BoardErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/homepage.do?id="+id, true);
	}
	
	/**
	 * 删除留言
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDelete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new BoardErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		
	  if(loginUser==null)
			errInfo.saveError(BoardErrInfo.NEED_LOGIN);
		else//检查当前用户是否是正常用户
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				//生成留言信息对象
				BoardInfo board=new BoardInfo();
				board.setId(id);
				board.setUserId(loginUser.getId());
				
				int ret=BoardDAO.deleteNote(board);
				if(ret!=1)
					errInfo.saveError(BoardErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(BoardErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		if(requ.getInt("sec")==1){
			return new ActionForward("/board/list.jsp");
		}
		return new ActionForward("/homepage.do", true);
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
