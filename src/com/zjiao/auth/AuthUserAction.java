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
 * 用户相关操作Action类
 */
public class AuthUserAction extends ActionExtend {
	
	/**
	 * 用户注册
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doAddUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo user = (UserInfo) form;
		ErrorInformation errInfo=new RegErrInfo();
		
		//检查用户名
		if(StringUtils.isNull(user.getRealName())||StringUtils.isRange(user.getRealName()," abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,./<>?[]\\\"{}|`~!@#$%^&*()-=+0123456789"))
			errInfo.saveError(RegErrInfo.REALNAME_ERROR);
		else//检查性别
		if(!"男".equals(user.getGender()) && !"女".equals(user.getGender()))
			errInfo.saveError(RegErrInfo.SEX_NOT_NULL);
		else//检查电子邮件
		if(StringUtils.isNull(user.getEmail()) || !StringUtils.isEmail(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else//检查密码
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
		//检查用户名是否已存在
		else{
			//判断是否存在要新注册的用户
			if(AuthUserDAO.isExistUser(user.getEmail()))
				errInfo.saveError(RegErrInfo.EMAIL_EXIST);
			else{
				//设置用户的默认信息
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
				
				//注册验证码检查
				String verifyCode = request.getParameter("verifyCode");
				if(!StringUtils.equals(verifyCode,RandomImageServlet.getRandomLoginKey(request)))
					errInfo.saveError(RegErrInfo.VERIFYCODE_ERROR);
				else {
					//写入数据库，完成用户注册
					try{
						String acode=AuthUserDAO.addUser(user);
						if((acode==null)||acode.length()!=SysUtils.ACTIVE_CODE_LENGTH)
							errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
						else{
							user.setPassword(null);

							//发送用户帐号激活邮件
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
		
		//完成注册
		request.setAttribute("uinfo", user);
		return mapping.findForward("regok");
	}
	
	/**
	 * 修改用户姓名性别
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModName(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
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
			
			//检查用户名
			if(StringUtils.isNull(loginUser.getRealName())||StringUtils.isRange(loginUser.getRealName()," abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,./<>?[]\\\"{}|`~!@#$%^&*()-=+0123456789"))
				errInfo.saveError(RegErrInfo.REALNAME_ERROR);
			else//检查性别
			if(!"男".equals(loginUser.getGender()) && !"女".equals(loginUser.getGender()))
				errInfo.saveError(RegErrInfo.SEX_NOT_NULL);
			else{
				//
				//写入数据库，更新用户姓名性别
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
			//恢复登陆用户的姓名和性别
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
	 * 修改用户所在学校
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModSchool(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
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
			
			//写入数据库，更新用户所在学校和年级
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
			//恢复登陆用户的所在学校和年级
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
	 * 修改用户基本信息
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModInfo(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
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
			
			//写入数据库，更新用户基本信息
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
			//恢复登陆用户的籍贯和生日
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
	 * 修改用户联系方式
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModContact(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
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
		
			//写入数据库，更新用户联系方式信息
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
	 * 修改用户母校的联系方式（大学生）
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModMuContact(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//写入数据库，更新用户联系方式信息
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
	 * 修改用户兴趣关注
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModInterest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String care=request.getParameter("infoCare").replaceAll("，",",");
		String sport=request.getParameter("hobbySport").replaceAll("，",",");
		String book=request.getParameter("hobbyBook").replaceAll("，",",");
		String film=request.getParameter("hobbyFilm").replaceAll("，",",");
		String music=request.getParameter("hobbyMusic").replaceAll("，",",");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//写入数据库，更新用户兴趣关注信息
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
	 * 修改用户志愿学校
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModIdeal(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		RequestUtils requ=RequestUtils.getInstance(request);
		
		String school=requ.getString("idealSchool").replaceAll("，",",");
		String strong=requ.getArrayString("strongSub");
		String weak=requ.getArrayString("weakSub");
		String wanted=requ.getString("wantedAbility").replaceAll("，",",");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//写入数据库，更新用户志愿学校信息
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
	 * 修改用户获奖情况
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModAward(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new RegErrInfo();
		
		String award=request.getParameter("award").replaceAll("，",",");
		String certificate=request.getParameter("certificate").replaceAll("，",",");
		
	  if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else{
		
			//写入数据库，更新用户获奖情况信息
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
	 * 修改用户的隐私设置
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doModPrivacy(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
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
			
			//写入数据库，更新隐私设置信息
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
			//恢复登陆用户的姓名和性别
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
	 * 上传照片
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doUploadPhoto(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
			//初始化
			UserInfo loginUser = UserInfo.getLoginUser(request);
			ErrorInformation errInfo=new RegErrInfo();
			String photo=loginUser.getPhoto();
		
			//定义变量
			int count=0;
			SmartUpload mySmartUpload = new SmartUpload();

			if(loginUser==null){
				errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
			  errInfo.saveToRequest("err", request);
			  return mapping.findForward("error");
			}
			else{
				try {
					//初始化
					mySmartUpload.initialize(servlet.getServletConfig(),request,response);
					//设置照片文件大小为600K
					mySmartUpload.setMaxFileSize(600000);
					//设置照片文件类型
					mySmartUpload.setAllowedFilesList("jpg,jpeg,gif,png");
					//完成上载
					mySmartUpload.upload();

				} catch (Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
					errInfo.saveToRequest("err", request);
					return mapping.findForward("error");
				}
		
				DateFormat sf = new SimpleDateFormat("/yyyy/MM/dd");
				String uploadDirName = "/upload/photo";//上传目录名
				String spath=uploadDirName + sf.format(new java.util.Date());
				String uploadPath = servlet.getServletContext().getRealPath(spath);//上传磁盘路径
				if(!FileUtils.newFolder(uploadPath)){
					errInfo.saveError(RegErrInfo.ERR_UNKNOWN);
					errInfo.saveToRequest("err", request);
					return mapping.findForward("error");
				}

				String fn=null;
				StringBuffer filePath=new StringBuffer(uploadPath);
				try{
					//保存图片文件到服务器磁盘
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
			
				//更新用户数据，指向新上传的照片
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
	 * 根据用户类型，选择跳转到相应的信息修改页面
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doSelectType(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
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
	 * 用户资料修改
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
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
		else//检查性别
		if(!"1".equals(user.getGender()) && !"0".equals(user.getGender()))
			errInfo.saveError(RegErrInfo.SEX_NOT_NULL);
		else//检查电子邮件
		if(StringUtils.isNull(user.getEmail()) || !StringUtils.isEmail(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else {
			//写入数据库，完成用户详细资料修改
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
	 * 修改密码
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doChgPwd(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		ErrorInformation errInfo=new RegErrInfo();
		UserInfo loginUser = UserInfo.getLoginUser(request);

		//获取新密码
		String oldPass=request.getParameter("oldPass");
		String newPass1=request.getParameter("newPass1");
		String newPass2=request.getParameter("newPass2");
		
		if(loginUser==null)
			errInfo.saveError(RegErrInfo.OPERATION_NOT_ALLOW);
		else//检查密码是否为空
		if(StringUtils.isEmpty(oldPass)||StringUtils.isEmpty(newPass1)||StringUtils.isEmpty(newPass2))
			errInfo.saveError(RegErrInfo.PASSWORD_NOT_NULL);
		else//检查新密码是否合法
		if((newPass1.length()<6)||(newPass1.length()>20))
			errInfo.saveError(RegErrInfo.PASSWORD_LENGTH_NOT_SUITABLE);
		else//检查确认密码是否正确
		if(!newPass1.equals(newPass2))
			errInfo.saveError(RegErrInfo.CONFIRM_PASSWORD_ERROR);
		else{
			//写入数据库，完成当前用户密码修改
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
	 * 取回密码
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doGetPass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		//获取初始化信息
		ErrorInformation errInfo=new RegErrInfo();
		
		//获取想取回密码的用户email
		String email=request.getParameter("email");
		
		//检查用户email是否为空
		if(StringUtils.isEmpty(email))
			errInfo.saveError(RegErrInfo.EMAIL_NOT_NULL);
		else//检查电子邮件是否合法
		if(!StringUtils.isEmail(email))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else{
			//从数据库中获取用户的密码
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
	 * 用户登录
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
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
		
		//用户名和密码都不能为空
		if(StringUtils.isNull(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_NOT_NULL);
		else//检查电子邮件是否合法
		if(!StringUtils.isEmail(user.getEmail()))
			errInfo.saveError(RegErrInfo.EMAIL_ERROR);
		else//检查密码是否为空
		if(StringUtils.isNull(user.getPassword()))
			errInfo.saveError(RegErrInfo.PASSWORD_NOT_NULL);
		else{
			//从数据库中验证用户信息
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
	 * 用户注销
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doLogout(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		//发送信息给认证服务器，使认证服务器上的session也失效
		UserInfo user=UserInfo.getLoginUser(request);
			
		if(user!=null) {
	    	UserInfo.removeFromSession(request);
	  }
	    
		return mapping.findForward("logout");
	}
	
	/**
	 * 放置用户激活动作，如果存在激活码，则调用激活动作
	 * @param mapping 路径映射对象
	 * @param form 表单对象
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward doDefault(ActionMapping mapping,
	  ActionForm form, HttpServletRequest request,
	  HttpServletResponse response) throws Exception {
		
		//获取初始化信息
		ErrorInformation errInfo=new RegErrInfo();
		
		//获取激活码
		String acode=request.getParameter("code");
		
		//如果不存在激活码，则回到主页
		if((acode==null)||(acode.length()!=SysUtils.ACTIVE_CODE_LENGTH)){
			return mapping.findForward("home");
		}
		
		//使用激活码激活用户
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
		
		request.setAttribute("ins","帐号激活成功，请登录！");
		return mapping.findForward("login");
	}
}
