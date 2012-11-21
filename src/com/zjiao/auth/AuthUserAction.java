package com.zjiao.auth;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jspsmart.upload.SmartUpload;
import com.zjiao.file.FileUtils;
import com.zjiao.key.KeyException;
import com.zjiao.struts.ActionExtend;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.RandomImageServlet;
import com.zjiao.SysUtils;
import com.zjiao.util.ActiveMailSender;
import com.zjiao.util.PassMailSender;
import com.zjiao.util.RequestUtils;
import com.zjiao.util.StringUtils;

/** 
 * �û���ز���Action��
 */
public class AuthUserAction extends ActionExtend {
	
	/**
	 * �û�ע��
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doAddUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo user = (UserInfo) form;
		ErrorInformation errInfo=new RegErrInfo();
		
		//����û���
		if(StringUtils.isNull(user.getRealName())||StringUtils.isRange(user.getRealName()," abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,./<>?[]\\\"{}|`~!@#$%^&*()-=+0123456789"))
			errInfo.saveError(RegErrInfo.REALNAME_ERROR);
		else//����Ա�
		if(!"��".equals(user.getGender()) && !"Ů".equals(user.getGender()))
			errInfo.saveError(RegErrInfo.SEX_NOT_NULL);
		else//�������ʼ�
		if(StringUtils.isNull(user.getEmail()) || !StringUtils.isEmail(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else//�������
		if(StringUtils.isNull(user.getPassword()))
			errInfo.saveError(RegErrInfo.PASSWORD_NOT_NULL);
		else
		if((user.getPassword().length()<6)||(user.getPassword().length()>20))
			errInfo.saveError(RegErrInfo.PASSWORD_LENGTH_NOT_SUITABLE);
		else
		if(user.getUserType()<1)
			errInfo.saveError(RegErrInfo.ROLE_TYPE_NOT_NULL);
		else
		if(StringUtils.isNull(user.getProvince()))
			errInfo.saveError(RegErrInfo.PROVINCE_NOT_NULL);
		else
		if(StringUtils.isNull(user.getSchool()))
			errInfo.saveError(RegErrInfo.SCHOOL_NOT_NULL);
		//����û����Ƿ��Ѵ���
		else{
			//�ж��Ƿ����Ҫ��ע����û�
			if(AuthUserDAO.isExistUser(user.getEmail()))
				errInfo.saveError(RegErrInfo.EMAIL_EXIST);
			else{
				//�����û���Ĭ����Ϣ
				if(user.getUserRole()<1){
					user.setUserRole(SysUtils.USER_ROLE_NORMAL);
				}
				user.setUserStatus(SysUtils.USER_STATUS_INACTIVE);
				user.setOptyInfo(SysUtils.OPEN_TYPE_ALL);
				user.setOptyContact(SysUtils.OPEN_TYPE_FRIEND);
				user.setOptyPhoto(SysUtils.OPEN_TYPE_ALL);
				user.setOptyBlog(SysUtils.OPEN_TYPE_ALL);
				user.setOptyFriend(SysUtils.OPEN_TYPE_ALL);
				user.setOptyMail(SysUtils.BOOLEAN_YES);
				
				//ע����֤����
				String verifyCode = request.getParameter("verifyCode");
				if(!StringUtils.equals(verifyCode,RandomImageServlet.getRandomLoginKey(request)))
					errInfo.saveError(RegErrInfo.VERIFYCODE_ERROR);
				else {
					//д�����ݿ⣬����û�ע��
					try{
						String acode=AuthUserDAO.addUser(user);
						if((acode==null)||acode.length()!=SysUtils.ACTIVE_CODE_LENGTH)
							errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
						else{
							user.setPassword(null);

							//�����û��ʺż����ʼ�
							if(!ActiveMailSender.sendMail(user, acode)){
									errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
							}
						}
					}catch(SQLException e){
						Logger.Log(Logger.LOG_ERROR,e);
						errInfo.saveError(RegErrInfo.DATABASE_ERR);
					}catch(KeyException ex){
						Logger.Log(Logger.LOG_ERROR,ex);
						errInfo.saveError(RegErrInfo.GENERATE_KEY_FAILED);
					}
					
				}
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			request.setAttribute("uinfo",user);
			return mapping.getInputForward();
		}
		
		//���ע��
		request.setAttribute("uinfo", user);
		return mapping.findForward("regok");
	}
	
	/**
	 * �޸��û������Ա�
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModName(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String name=null;
		String sex=null;
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			name=loginUser.getRealName();
			sex=loginUser.getGender();
		
			loginUser.setRealName(request.getParameter("realName"));
			loginUser.setGender(request.getParameter("gender"));
			
			//����û���
			if(StringUtils.isNull(loginUser.getRealName())||StringUtils.isRange(loginUser.getRealName()," abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,./<>?[]\\\"{}|`~!@#$%^&*()-=+0123456789"))
				errInfo.saveError(RegErrInfo.REALNAME_ERROR);
			else//����Ա�
			if(!"��".equals(loginUser.getGender()) && !"Ů".equals(loginUser.getGender()))
				errInfo.saveError(RegErrInfo.SEX_NOT_NULL);
			else{
				//
				//д�����ݿ⣬�����û������Ա�
				try{
					int ret=AuthUserDAO.updateUserF(loginUser);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}catch(SQLException e){
					Logger.Log(Logger.LOG_ERROR,e);
					errInfo.saveError(RegErrInfo.DATABASE_ERR);
				}
			}
		}
			
		if(!errInfo.isEmpty()){
			//�ָ���½�û����������Ա�
			if(loginUser!=null){
				loginUser.setRealName(name);
				loginUser.setGender(sex);
			}
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/basic"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û�����ѧУ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModSchool(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String province=null;
		String school=null;
		String grade=null;
		
		String province1=request.getParameter("province");
		String school1=request.getParameter("school");
		String grade1=request.getParameter("grade");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			province=loginUser.getProvince();
			school=loginUser.getSchool();
			grade=loginUser.getGrade();
		
			loginUser.setGrade(grade1);
			if(!StringUtils.isNull(school1)&&!StringUtils.isNull(province1)){
				loginUser.setProvince(province1);
				loginUser.setSchool(school1);
			}
			
			//д�����ݿ⣬�����û�����ѧУ���꼶
			try{
				int ret=AuthUserDAO.updateUserF(loginUser);
				if(ret!=1)
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			//�ָ���½�û�������ѧУ���꼶
			if(loginUser!=null){
				loginUser.setGrade(grade);
				if(!StringUtils.isNull(school1)&&!StringUtils.isNull(province1)){
					loginUser.setProvince(province);
					loginUser.setSchool(school);
				}
			}
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/basic"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û�������Ϣ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModInfo(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		Date birthday1=requ.getDate("birthday");
		String bornplace1=requ.getString("bornPlace");
		String muschool1=requ.getString("muSchool");
		String mugrade1=requ.getString("muGrade");
		String motto1=requ.getString("motto");
		
		Date birthday=null;
		String bornplace=null;
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			birthday=loginUser.getBirthday();
			bornplace=loginUser.getBornPlace();
		
			loginUser.setBirthday(birthday1);
			loginUser.setBornPlace(bornplace1);
			
			//д�����ݿ⣬�����û�������Ϣ
			int ret;
			try{
				if(loginUser.getUserType()==SysUtils.USER_TYPE_JUNIOR){
					UserJunior uj=new UserJunior();
					uj.setId(loginUser.getId());
					uj.setMuSchool(muschool1);
					uj.setMuGrade(mugrade1);
					uj.setMotto(motto1);
					ret=AuthUserDAO.updateSpecial(uj);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_SENIOR){
					UserSenior us=new UserSenior();
					us.setId(loginUser.getId());
					us.setMuSchool(muschool1);
					us.setMuGrade(mugrade1);
					us.setMotto(motto1);
					ret=AuthUserDAO.updateSpecial(us);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV){
					UserUniv uv=new UserUniv();
					uv.setId(loginUser.getId());
					uv.setMuSchool(muschool1);
					uv.setMuGrade(mugrade1);
					uv.setMotto(motto1);
					ret=AuthUserDAO.updateSpecial(uv);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
				if(errInfo.isEmpty()){
					ret=AuthUserDAO.updateUser(loginUser);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			//�ָ���½�û��ļ��������
			if(loginUser!=null){
				loginUser.setBirthday(birthday);
				loginUser.setBornPlace(bornplace);
			}
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/basic"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û���ϵ��ʽ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModContact(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String qq=request.getParameter("qq");
		String msn=request.getParameter("msn");
		String mobile=request.getParameter("mobile");
		String phone=request.getParameter("phone");
		String address=request.getParameter("address");
		String code=request.getParameter("code");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//д�����ݿ⣬�����û���ϵ��ʽ��Ϣ
			int ret;
			try{
				if(loginUser.getUserType()==SysUtils.USER_TYPE_JUNIOR){
					UserJunior uj=new UserJunior();
					uj.setId(loginUser.getId());
					uj.setQq(qq);
					uj.setMsn(msn);
					uj.setMobile(mobile);
					uj.setPhone(phone);
					uj.setAddress(address);
					uj.setCode(code);
					ret=AuthUserDAO.updateSpecial(uj);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_SENIOR){
					UserSenior us=new UserSenior();
					us.setId(loginUser.getId());
					us.setQq(qq);
					us.setMsn(msn);
					us.setMobile(mobile);
					us.setPhone(phone);
					us.setAddress(address);
					us.setCode(code);
					ret=AuthUserDAO.updateSpecial(us);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV){
					UserUniv uv=new UserUniv();
					uv.setId(loginUser.getId());
					uv.setQq(qq);
					uv.setMsn(msn);
					uv.setMobile(mobile);
					uv.setPhone(phone);
					uv.setAddress(address);
					uv.setCode(code);
					
					uv.setMuTeacher(request.getParameter("muTeacher"));
					uv.setMuPhone(request.getParameter("muPhone"));
					uv.setMuEmail(request.getParameter("muEmail"));
					uv.setMuMobile(request.getParameter("muMobile"));
					uv.setMuAddress(request.getParameter("muAddress"));
					uv.setOtherContact(request.getParameter("otherContact"));
					ret=AuthUserDAO.updateSpecial(uv);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/contact"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û�ĸУ����ϵ��ʽ����ѧ����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModMuContact(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//д�����ݿ⣬�����û���ϵ��ʽ��Ϣ
			int ret;
			try{
				if(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV){
					UserUniv uv=new UserUniv();
					uv.setId(loginUser.getId());
					uv.setMuTeacher(request.getParameter("muTeacher"));
					uv.setMuPhone(request.getParameter("muPhone"));
					uv.setMuEmail(request.getParameter("muEmail"));
					uv.setMuMobile(request.getParameter("muMobile"));
					uv.setMuAddress(request.getParameter("muAddress"));
					uv.setOtherContact(request.getParameter("otherContact"));
					ret=AuthUserDAO.updateSpecial(uv);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return mapping.findForward("profile");
	}
	
	/**
	 * �޸��û���Ȥ��ע
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModInterest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String care=request.getParameter("infoCare").replaceAll("��",",");
		String sport=request.getParameter("hobbySport").replaceAll("��",",");
		String book=request.getParameter("hobbyBook").replaceAll("��",",");
		String film=request.getParameter("hobbyFilm").replaceAll("��",",");
		String music=request.getParameter("hobbyMusic").replaceAll("��",",");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//д�����ݿ⣬�����û���Ȥ��ע��Ϣ
			int ret;
			try{
				if(loginUser.getUserType()==SysUtils.USER_TYPE_JUNIOR){
					UserJunior uj=new UserJunior();
					uj.setId(loginUser.getId());
					uj.setInfoCare(care);
					uj.setHobbySport(sport);
					uj.setHobbyBook(book);
					uj.setHobbyFilm(film);
					uj.setHobbyMusic(music);
					ret=AuthUserDAO.updateSpecial(uj);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_SENIOR){
					UserSenior us=new UserSenior();
					us.setId(loginUser.getId());
					us.setInfoCare(care);
					us.setHobbySport(sport);
					us.setHobbyBook(book);
					us.setHobbyFilm(film);
					us.setHobbyMusic(music);
					ret=AuthUserDAO.updateSpecial(us);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV){
					UserUniv uv=new UserUniv();
					uv.setId(loginUser.getId());
					uv.setInfoCare(care);
					uv.setHobbySport(sport);
					uv.setHobbyBook(book);
					uv.setHobbyFilm(film);
					uv.setHobbyMusic(music);
					ret=AuthUserDAO.updateSpecial(uv);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/interest"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û�־ԸѧУ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModIdeal(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		String school=requ.getString("idealSchool").replaceAll("��",",");
		String strong=requ.getArrayString("strongSub");
		String weak=requ.getArrayString("weakSub");
		String wanted=requ.getString("wantedAbility").replaceAll("��",",");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//д�����ݿ⣬�����û�־ԸѧУ��Ϣ
			int ret;
			try{
				if(loginUser.getUserType()==SysUtils.USER_TYPE_JUNIOR){
					UserJunior uj=new UserJunior();
					uj.setId(loginUser.getId());
					uj.setIdealSchool(school);
					uj.setStrongSub(strong);
					uj.setWeakSub(weak);
					uj.setWantedAbility(wanted);
					ret=AuthUserDAO.updateSpecial(uj);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_SENIOR){
					UserSenior us=new UserSenior();
					us.setId(loginUser.getId());
					us.setIdealSchool(school);
					us.setStrongSub(strong);
					us.setWeakSub(weak);
					us.setWantedAbility(wanted);
					ret=AuthUserDAO.updateSpecial(us);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV){
					UserUniv uv=new UserUniv();
					uv.setId(loginUser.getId());
					uv.setIdealWork(requ.getString("idealWork"));
					ret=AuthUserDAO.updateSpecial(uv);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/ideal"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModAward(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String award=request.getParameter("award").replaceAll("��",",");
		String certificate=request.getParameter("certificate").replaceAll("��",",");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//д�����ݿ⣬�����û��������Ϣ
			int ret;
			try{
				if(loginUser.getUserType()==SysUtils.USER_TYPE_JUNIOR){
					UserJunior uj=new UserJunior();
					uj.setId(loginUser.getId());
					uj.setAward(award);
					uj.setCertificate(certificate);
					ret=AuthUserDAO.updateSpecial(uj);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_SENIOR){
					UserSenior us=new UserSenior();
					us.setId(loginUser.getId());
					us.setAward(award);
					us.setCertificate(certificate);
					ret=AuthUserDAO.updateSpecial(us);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}else
				if(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV){
					UserUniv uv=new UserUniv();
					uv.setId(loginUser.getId());
					uv.setAward(award);
					uv.setCertificate(certificate);
					ret=AuthUserDAO.updateSpecial(uv);
					if(ret!=1)
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/award"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �޸��û�����˽����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModPrivacy(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		int info1=0,contact1=0,photo1=0,blog1=0,friend1=0,email1=0;
		
		int info=requ.getInt("optyInfo");
		int contact=requ.getInt("optyContact");
		int photo=requ.getInt("optyPhoto");
		int blog=requ.getInt("optyBlog");
		int friend=requ.getInt("optyFriend");
		int email=requ.getInt("optyMail");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.NEED_LOGIN);
		else
		if((info==0)||(contact==0)||(photo==0)||(blog==0)||(friend==0)||(email==0))
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
			info1=loginUser.getOptyInfo();
			contact1=loginUser.getOptyContact();
			photo1=loginUser.getOptyPhoto();
			blog1=loginUser.getOptyBlog();
			friend1=loginUser.getOptyFriend();
			email1=loginUser.getOptyMail();
			
			loginUser.setOptyInfo(info);
			loginUser.setOptyContact(contact);
			loginUser.setOptyPhoto(photo);
			loginUser.setOptyBlog(blog);
			loginUser.setOptyFriend(friend);
			loginUser.setOptyMail(email);
			
			//д�����ݿ⣬������˽������Ϣ
			try{
				int ret=AuthUserDAO.updateUser(loginUser);
				if(ret!=1)
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			//�ָ���½�û����������Ա�
			if((loginUser!=null)&&(info1!=0)){
				loginUser.setOptyInfo(info1);
				loginUser.setOptyContact(contact1);
				loginUser.setOptyPhoto(photo1);
				loginUser.setOptyBlog(blog1);
				loginUser.setOptyFriend(friend1);
				loginUser.setOptyMail(email1);
			}
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/privacy"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �ϴ���Ƭ
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doUploadPhoto(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//��ʼ��
			UserInfo loginUser = UserInfo.getLoginUser(request);
			ErrorInformation errInfo=new RegErrInfo();
			String photo=loginUser.getPhoto();
		
			//�������
			int count=0;
			SmartUpload mySmartUpload = new SmartUpload();

			if(loginUser==null){
				errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
			  errInfo.saveToRequest("err", request);
			  return mapping.findForward("error");
			}
			else{
				try {
					//��ʼ��
					mySmartUpload.initialize(servlet.getServletConfig(),request,response);
					//������Ƭ�ļ���СΪ600K
					mySmartUpload.setMaxFileSize(600000);
					//������Ƭ�ļ�����
					mySmartUpload.setAllowedFilesList("jpg,jpeg,gif,png");
					//�������
					mySmartUpload.upload();

				} catch (Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
					errInfo.saveToRequest("err", request);
					return mapping.findForward("error");
				}
		
				DateFormat sf = new SimpleDateFormat("/yyyy/MM/dd");
				String uploadDirName = "/upload/photo";//�ϴ�Ŀ¼��
				String spath=uploadDirName + sf.format(new java.util.Date());
				String uploadPath = servlet.getServletContext().getRealPath(spath);//�ϴ�����·��
				if(!FileUtils.newFolder(uploadPath)){
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
					errInfo.saveToRequest("err", request);
					return mapping.findForward("error");
				}

				String fn=null;
				StringBuffer filePath=new StringBuffer(uploadPath);
				try{
					//����ͼƬ�ļ�������������
					com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(0);
					String filename = myFile.getFileName();
					String extendName = StringUtils.getFileExtendName(filename);
					fn = loginUser.getId()+"."+extendName;
					filePath.append(File.separator);
					filePath.append(fn);

					myFile.saveAs(filePath.toString(), com.jspsmart.upload.SmartUpload.SAVE_PHYSICAL);
	
				}catch(Exception fex){
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
					errInfo.saveToRequest("err", request);
					return mapping.findForward("error");
				}
			
				//�����û����ݣ�ָ�����ϴ�����Ƭ
				if(errInfo.isEmpty()){
					loginUser.setPhoto(filePath.toString());
					try{
						int ret=AuthUserDAO.updateUser(loginUser);
						if(ret!=1)
							errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
					}catch(Exception e){
						Logger.Log(Logger.LOG_ERROR, e);
						errInfo.saveError(RegErrInfo.DATABASE_ERR);
					}
				}
		}
		
		if(!errInfo.isEmpty()){
			loginUser.setPhoto(photo);
			errInfo.saveToRequest("err",request);
			return mapping.getInputForward();
		}
		
		return new ActionForward("/profile/photo"+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �����û����ͣ�ѡ����ת����Ӧ����Ϣ�޸�ҳ��
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSelectType(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		UserInfo loginUser = UserInfo.getLoginUser(request);
		
		if(loginUser==null)
			return mapping.findForward("login");
		
		String sec=request.getParameter("sec");
		
		if(sec==null)
			return new ActionForward("/profile/basic"+loginUser.getUserType()+".jsp", true);
		else
			return new ActionForward("/profile/"+sec+loginUser.getUserType()+".jsp", true);
	}
	
	/**
	 * �û������޸�
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doEditUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		UserInfo user = (UserInfo) form;
		ErrorInformation errInfo=new RegErrInfo();
		if(StringUtils.exportDate(user.getBirthday(),"yyyy-MM-dd","0002-11-30").equals("0002-11-30")){
			user.setBirthday(null);
		}

		UserInfo loginUser = UserInfo.getLoginUser(request);
	  if(loginUser==null || ((user.getId()!=loginUser.getId()) && !loginUser.isAdmin()))
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else//����Ա�
		if(!"1".equals(user.getGender()) && !"0".equals(user.getGender()))
			errInfo.saveError(RegErrInfo.SEX_NOT_NULL);
		else//�������ʼ�
		if(StringUtils.isNull(user.getEmail()) || !StringUtils.isEmail(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else {
			//д�����ݿ⣬����û���ϸ�����޸�
			try{
				int rt=AuthUserDAO.updateUser(user);
				if(rt!=1)
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
			
		}

		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
		}
		
		return mapping.findForward("editok");
	}
	
	/**
	 * �޸�����
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doChgPwd(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		ErrorInformation errInfo=new RegErrInfo();
		UserInfo loginUser = UserInfo.getLoginUser(request);

		//��ȡ������
		String oldPass=request.getParameter("oldPass");
		String newPass1=request.getParameter("newPass1");
		String newPass2=request.getParameter("newPass2");
		
		if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else//��������Ƿ�Ϊ��
		if(StringUtils.isEmpty(oldPass)||StringUtils.isEmpty(newPass1)||StringUtils.isEmpty(newPass2))
			errInfo.saveError(RegErrInfo.PASSWORD_NOT_NULL);
		else//����������Ƿ�Ϸ�
		if((newPass1.length()<6)||(newPass1.length()>20))
			errInfo.saveError(RegErrInfo.PASSWORD_LENGTH_NOT_SUITABLE);
		else//���ȷ�������Ƿ���ȷ
		if(!newPass1.equals(newPass2))
			errInfo.saveError(RegErrInfo.CONFIRM_PASSWORD_ERROR);
		else{
			//д�����ݿ⣬��ɵ�ǰ�û������޸�
			try{
				int rt=AuthUserDAO.changePass(loginUser.getId(), oldPass, newPass1);
				if(rt==101)
					errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
				else
				if(rt==102)
					errInfo.saveError(RegErrInfo.OLD_PASSWORD_ERROR);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		return new ActionForward("/profile/safe"+loginUser.getUserType()+".jsp?op=1", true);
	}
	
	/**
	 * ȡ������
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doGetPass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//��ȡ��ʼ����Ϣ
		ErrorInformation errInfo=new RegErrInfo();
		
		//��ȡ��ȡ��������û�email
		String email=request.getParameter("email");
		
		//����û�email�Ƿ�Ϊ��
		if(StringUtils.isEmpty(email))
			errInfo.saveError(RegErrInfo.EMAIL_NOT_NULL);
		else//�������ʼ��Ƿ�Ϸ�
		if(!StringUtils.isEmail(email))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else{
			//�����ݿ��л�ȡ�û�������
			try{
				UserInfo uinfo=AuthUserDAO.getUserInfo(email);
				if(uinfo==null)
					errInfo.saveError(RegErrInfo.USER_INVALID);
				else
				if(!PassMailSender.sendMail(uinfo)){
						errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				}
				request.setAttribute("uinfo", uinfo);
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
			

		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return new ActionForward("/register/forgetpass.jsp");
		}
		
		return mapping.findForward("sendpwdok");
	}
	
	/**
	 * �û���¼
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doLogin(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		UserInfo user = (UserInfo) form;
		ErrorInformation errInfo=new RegErrInfo();
		UserInfo loginUser=null;
		
		//�û��������붼����Ϊ��
		if(StringUtils.isNull(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_NOT_NULL);
		else//�������ʼ��Ƿ�Ϸ�
		if(!StringUtils.isEmail(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else//��������Ƿ�Ϊ��
		if(StringUtils.isNull(user.getPassword()))
			errInfo.saveError(RegErrInfo.PASSWORD_NOT_NULL);
		else{
			//�����ݿ�����֤�û���Ϣ
			try{
				loginUser=AuthUserDAO.userLogin(user.getEmail(), user.getPassword());
				if(loginUser==null)
					errInfo.saveError(RegErrInfo.EMAIL_OR_PASSWORD_ERROR);
				else
				if(loginUser.getUserStatus()==SysUtils.USER_STATUS_INVALID)
					errInfo.saveError(RegErrInfo.USER_INVALID);
				else
				if(loginUser.getUserStatus()==SysUtils.USER_STATUS_INACTIVE)
					errInfo.saveError(RegErrInfo.USER_INACTIVE);
				else{
					loginUser.saveLoginUser(request);
				}
				
			}catch(SQLException e){
				Logger.Log(Logger.LOG_ERROR,e);
				errInfo.saveError(RegErrInfo.DATABASE_ERR);
			}
		}
		
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		if(loginUser.isValid()){
			String url = request.getParameter("url");
			ActionForward forward = null;
			if(StringUtils.isEmpty(url))
				forward = mapping.findForward("main");
			else
				forward = new ActionForward(url,true);
		
			return forward;
			
		}else if((loginUser.getLogCount()==1)||(loginUser.getUserStatus()==SysUtils.USER_STATUS_REJECT)){
			if((loginUser.getLogCount()==1)&&(loginUser.getUserType()==SysUtils.USER_TYPE_UNIV)){
				return mapping.findForward("addmucontact");
			}
			return mapping.findForward("profile");
		}else{
			return mapping.findForward("main");
		}
	}
	
	/**
	 * �û�ע��
	 * @param mapping ·��ӳ�����
	 * @param form ������
	 * @param request �������
	 * @param response ��Ӧ����
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doLogout(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//������Ϣ����֤��������ʹ��֤�������ϵ�sessionҲʧЧ
		UserInfo user=UserInfo.getLoginUser(request);
			
		if(user!=null) {
	    	UserInfo.removeFromSession(request);
	  }
	    
		return mapping.findForward("logout");
	}
	
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
		
		//��ȡ������
		String acode=request.getParameter("code");
		
		//��������ڼ����룬��ص���ҳ
		if((acode==null)||(acode.length()!=SysUtils.ACTIVE_CODE_LENGTH)){
			return mapping.findForward("home");
		}
		
		//ʹ�ü����뼤���û�
		int ret;
		try{
			ret=AuthUserDAO.activateUser(acode);
			
			if(ret==-1)
				errInfo.saveError(RegErrInfo.ACTIVE_CODE_EXPIRE);
			else
			if(ret==0)
				errInfo.saveError(RegErrInfo.ACTIVE_CODE_ERR);
			else
			if(ret==1)
				errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
				
		}catch(SQLException e){
			Logger.Log(Logger.LOG_ERROR,e);
			errInfo.saveError(RegErrInfo.DATABASE_ERR);
		}
		
		if(!errInfo.isEmpty()){
			errInfo.saveToRequest("err",request);
			return mapping.findForward("error");
		}
		
		request.setAttribute("ins","�ʺż���ɹ������¼��");
		return mapping.findForward("login");
	}
}
