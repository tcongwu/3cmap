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
 * ���԰���ز���Action��
 */
public class BoardAction extends ActionExtend {
	
	/**
	 * ��������
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
		ErrorInformation errInfo=new BoardErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		String content=requ.getString("message");
		
	  if(loginUser==null)
			errInfo.saveError(BoardErrInfo.NEED_LOGIN);
		else//��鵱ǰ�û��Ƿ��������û�
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else//�������ݲ���Ϊ��
		if(StringUtils.isNull(content))
			errInfo.saveError(BoardErrInfo.NEEDED_FIELD_NOT_NULL);
		else{
			try{
				//��鱻���Ե��û��Ƿ����
				if(!AuthUserDAO.isExistUser(id)){
					errInfo.saveError(BoardErrInfo.OPERATION_NOT_ALLOW);
				}else{
					//����������Ϣ����
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
	 * ɾ������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDelete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new BoardErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		int id=requ.getInt("id");
		
	  if(loginUser==null)
			errInfo.saveError(BoardErrInfo.NEED_LOGIN);
		else//��鵱ǰ�û��Ƿ��������û�
		if(loginUser.getUserStatus()!=SysUtils.USER_STATUS_NORMAL)
			return mapping.findForward("profile");
		else{
			try{
				//����������Ϣ����
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
