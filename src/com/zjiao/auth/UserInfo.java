package com.zjiao.auth;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * 用户信息对象，用于保存用户的基本信息
 * 用户认证后的用户信息也存于此对象中
 * @author Lee Bin
 */
public class UserInfo extends ActionForm implements Serializable{

	public final static String KEY = "sys.3cmap.loginUser";
	
	/** 用户ID */
	private int id;

	/** 用户email，即登录ID */
	private String email;

	/** 用户密码 */
	private String password;

	/** 用户的真实姓名 */
	private String realName;

	/** 用户的性别 */
	private String gender;

	/** 用户的生日 */
	private Date birthday;

	/** 籍贯 */
	private String bornPlace;

	/** 用户的相片路径 */
	private String photo;

	/** 所在省市 */
	private String province;

	/** 所在学校 */
	private String school;

	/** 入学年份 */
	private String grade;

	/** 用户积分 */
	private int score;

	/** 用户类别 */
	private int userType;

	/** 用户角色 */
	private int userRole;

	/** 用户状态 */
	private int userStatus;

	/** 隐私设置之个人资料 */
	private int optyInfo;

	/** 隐私设置之联系方式 */
	private int optyContact;

	/** 隐私设置之照片查看 */
	private int optyPhoto;

	/** 隐私设置之日志查看 */
	private int optyBlog;

	/** 隐私设置之好友查看 */
	private int optyFriend;

	/** 是否接收提醒邮件 */
	private int optyMail;

	/** 用户的注册时间 */
	private Timestamp regDate;

	/** 登录次数 */
	private int logCount;

	/** 被人浏览次数 */
	private int readCount;

	/** 最近登录时间 */
	private Timestamp recentLogin;
	
	/**
	 * 从session中获取用户的登录信息
	 * @param request 请求对象
	 * @return 登录用户的信息
	 */
	public static UserInfo getLoginUser(HttpServletRequest request){
		Object obj=request.getSession().getAttribute(KEY);
		if(obj!=null)
			return (UserInfo)obj;
		return null;
	}

	/**
	 * 把用户的的验证存入session中
	 * @param request 请求对象
	 */
	public void saveLoginUser(HttpServletRequest request){
		request.getSession().setAttribute(KEY, this);
	}
	
	/**
	 * 把用户登录信息清除
	 * @param request 请求对象
	 */
	public static void removeFromSession(HttpServletRequest request){
		request.getSession().removeAttribute(KEY);
	}
	
	/**
	 * 判断当前用户是否为管理员
	 * @return boolean 是否为
	 */
	public boolean isAdmin() {
		if(userRole==SysUtils.USER_ROLE_ADMIN)
			return true;
		
		return false;
	}
	
	/**
	 * 判断当前用户是否有效
	 * @return boolean 是否有效
	 */
	public boolean isValid() {
		if(userStatus==SysUtils.USER_STATUS_NORMAL)
			return true;
		
		return false;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getRealName() {
		return realName;
	}

	public String getGender() {
		return gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getBornPlace() {
		return bornPlace;
	}

	public String getPhoto() {
		return photo;
	}

	public String getProvince() {
		return province;
	}
	
	public String getSchool() {
		return school;
	}

	public String getGrade() {
		return grade;
	}

	public int getScore() {
		return score;
	}
	
	public int getUserType() {
		return userType;
	}
	
	public int getUserRole() {
		return userRole;
	}
	
	public int getUserStatus() {
		return userStatus;
	}
	
	public int getOptyInfo() {
		return optyInfo;
	}
	
	public int getOptyContact() {
		return optyContact;
	}
	
	public int getOptyPhoto() {
		return optyPhoto;
	}
	
	public int getOptyBlog() {
		return optyBlog;
	}
	
	public int getOptyFriend() {
		return optyFriend;
	}
	
	public int getOptyMail() {
		return optyMail;
	}

	public Timestamp getRegDate() {
		return regDate;
	}
	
	public int getLogCount() {
		return logCount;
	}
	
	public int getReadCount() {
		return readCount;
	}

	public Timestamp getRecentLogin() {
		return recentLogin;
	}

	//----------------------------------------
	//----------------------------------------
	public void setId(int it) {
		id = it;
	}
	
	public void setEmail(String string) {
		email = string;
	}
	
	public void setPassword(String string) {
		password = string;
	}
	
	public void setRealName(String string) {
		realName = string;
	}
	
	public void setGender(String string) {
		gender = string;
	}
	
	public void setBirthday(Date dt) {
		birthday = dt;
	}
	
	public void setBornPlace(String string) {
		bornPlace = string;
	}
	
	public void setPhoto(String string) {
		photo = string;
	}

	public void setProvince(String string) {
		province = string;
	}

	public void setSchool(String string) {
		school = string;
	}
	
	public void setGrade(String string) {
		grade = string;
	}

	public void setScore(int it) {
		score = it;
	}
	
	public void setUserType(int it) {
		userType = it;
	}
	
	public void setUserRole(int it) {
		userRole = it;
	}

	public void setUserStatus(int it) {
		userStatus = it;
	}

	public void setOptyInfo(int it) {
		optyInfo = it;
	}

	public void setOptyContact(int it) {
		optyContact = it;
	}

	public void setOptyPhoto(int it) {
		optyPhoto = it;
	}

	public void setOptyBlog(int it) {
		optyBlog = it;
	}

	public void setOptyFriend(int it) {
		optyFriend = it;
	}

	public void setOptyMail(int it) {
		optyMail = it;
	}

	public void setRegDate(Timestamp ts) {
		regDate = ts;
	}

	public void setLogCount(int it) {
		logCount = it;
	}

	public void setReadCount(int it) {
		readCount = it;
	}

	public void setRecentLogin(Timestamp ts) {
		recentLogin = ts;
	}

}
