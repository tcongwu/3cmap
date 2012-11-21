package com.zjiao.auth;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;

/** 
 * �û���Ϣ�������ڱ����û��Ļ�����Ϣ
 * �û���֤����û���ϢҲ���ڴ˶�����
 * @author Lee Bin
 */
public class UserInfo extends ActionForm implements Serializable{

	public final static String KEY = "sys.3cmap.loginUser";
	
	/** �û�ID */
	private int id;

	/** �û�email������¼ID */
	private String email;

	/** �û����� */
	private String password;

	/** �û�����ʵ���� */
	private String realName;

	/** �û����Ա� */
	private String gender;

	/** �û������� */
	private Date birthday;

	/** ���� */
	private String bornPlace;

	/** �û�����Ƭ·�� */
	private String photo;

	/** ����ʡ�� */
	private String province;

	/** ����ѧУ */
	private String school;

	/** ��ѧ��� */
	private String grade;

	/** �û����� */
	private int score;

	/** �û���� */
	private int userType;

	/** �û���ɫ */
	private int userRole;

	/** �û�״̬ */
	private int userStatus;

	/** ��˽����֮�������� */
	private int optyInfo;

	/** ��˽����֮��ϵ��ʽ */
	private int optyContact;

	/** ��˽����֮��Ƭ�鿴 */
	private int optyPhoto;

	/** ��˽����֮��־�鿴 */
	private int optyBlog;

	/** ��˽����֮���Ѳ鿴 */
	private int optyFriend;

	/** �Ƿ���������ʼ� */
	private int optyMail;

	/** �û���ע��ʱ�� */
	private Timestamp regDate;

	/** ��¼���� */
	private int logCount;

	/** ����������� */
	private int readCount;

	/** �����¼ʱ�� */
	private Timestamp recentLogin;
	
	/**
	 * ��session�л�ȡ�û��ĵ�¼��Ϣ
	 * @param request �������
	 * @return ��¼�û�����Ϣ
	 */
	public static UserInfo getLoginUser(HttpServletRequest request){
		Object obj=request.getSession().getAttribute(KEY);
		if(obj!=null)
			return (UserInfo)obj;
		return null;
	}

	/**
	 * ���û��ĵ���֤����session��
	 * @param request �������
	 */
	public void saveLoginUser(HttpServletRequest request){
		request.getSession().setAttribute(KEY, this);
	}
	
	/**
	 * ���û���¼��Ϣ���
	 * @param request �������
	 */
	public static void removeFromSession(HttpServletRequest request){
		request.getSession().removeAttribute(KEY);
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�Ϊ����Ա
	 * @return boolean �Ƿ�Ϊ
	 */
	public boolean isAdmin() {
		if(userRole==SysUtils.USER_ROLE_ADMIN)
			return true;
		
		return false;
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ���Ч
	 * @return boolean �Ƿ���Ч
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
