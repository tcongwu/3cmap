package com.zjiao.auth;

/**
 * @author Li Bin
 * 大学生用户详细信息
 */

import java.io.Serializable;
import org.apache.struts.action.ActionForm;

/** 
 * 大学生用户个性信息对象，用于保存大学生用户的个性信息
 * @author Lee Bin
 */
public class UserUniv extends ActionForm implements Serializable{
	
	/** 用户ID */
	private int id;

	/** 母校名称 */
	private String muSchool;
	
	/** 所在年级 */
	private String muGrade;

	/** 班主任姓名 */
	private String muTeacher;
	
	/** 班主任电话 */
	private String muPhone;

	/** 班主任email */
	private String muEmail;
	
	/** 班主任手机 */
	private String muMobile;

	/** 班主任地址 */
	private String muAddress;
	
	/** 其他联系人 */
	private String otherContact;
	
	/** 理想工作 */
	private String idealWork;

	/** 关注信息分类 */
	private String infoCare;

	/** 体育偏好 */
	private String hobbySport;

	/** 书籍偏好 */
	private String hobbyBook;

	/** 电影偏好 */
	private String hobbyFilm;

	/** 音乐偏好 */
	private String hobbyMusic;

	/** 个人座右铭 */
	private String motto;

	/** 所获奖励 */
	private String award;

	/** 所获证书 */
	private String certificate;

	/** 目前电话 */
	private String phone;

	/** 目前手机 */
	private String mobile;

	/** 目前qq */
	private String qq;

	/** 用户的msn*/
	private String  msn;

	/** 目前所属省份 */
	private String province;

	/** 目前所在学校 */
	private String school;

	/** 目前所在年级 */
	private String grade;

	/** 个人详细地址 */
	private String address;

	/** 个人地址邮编 */
	private String code;

	public int getId() {
		return id;
	}

	public String getMuSchool() {
		return muSchool;
	}

	public String getMuGrade() {
		return muGrade;
	}

	public String getMuTeacher() {
		return muTeacher;
	}

	public String getMuPhone() {
		return muPhone;
	}

	public String getMuEmail() {
		return muEmail;
	}

	public String getMuMobile() {
		return muMobile;
	}

	public String getMuAddress() {
		return muAddress;
	}

	public String getOtherContact() {
		return otherContact;
	}

	public String getIdealWork() {
		return idealWork;
	}

	public String getInfoCare() {
		return infoCare;
	}

	public String getHobbyBook() {
		return hobbyBook;
	}

	public String getHobbyFilm() {
		return hobbyFilm;
	}

	public String getHobbyMusic() {
		return hobbyMusic;
	}

	public String getHobbySport() {
		return hobbySport;
	}

	public String getMotto() {
		return motto;
	}

	public String getAward() {
		return award;
	}

	public String getCertificate() {
		return certificate;
	}

	public String getPhone() {
		return phone;
	}

	public String getMobile() {
		return mobile;
	}

	public String getQq() {
		return qq;
	}

	public String getMsn() {
		return msn;
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
	
	public String getAddress() {
		return address;
	}

	public String getCode() {
		return code;
	}

	//--------------------------------------------------

	public void setId(int i) {
		id = i;
	}

	public void setMuGrade(String string) {
		muGrade = string;
	}

	public void setMuSchool(String string) {
		muSchool = string;
	}

	public void setMuTeacher(String string) {
		muTeacher = string;
	}

	public void setMuPhone(String string) {
		muPhone = string;
	}

	public void setMuEmail(String string) {
		muEmail = string;
	}

	public void setMuMobile(String string) {
		muMobile = string;
	}

	public void setMuAddress(String string) {
		muAddress = string;
	}

	public void setOtherContact(String string) {
		otherContact = string;
	}

	public void setIdealWork(String string) {
		idealWork = string;
	}

	public void setInfoCare(String string) {
		infoCare = string;
	}

	public void setHobbyBook(String string) {
		hobbyBook = string;
	}

	public void setHobbyFilm(String string) {
		hobbyFilm = string;
	}

	public void setHobbyMusic(String string) {
		hobbyMusic = string;
	}

	public void setHobbySport(String string) {
		hobbySport = string;
	}

	public void setMotto(String string) {
		motto = string;
	}

	public void setAward(String string) {
		award = string;
	}

	public void setCertificate(String string) {
		certificate = string;
	}

	public void setPhone(String string) {
		phone = string;
	}

	public void setMobile(String string) {
		mobile = string;
	}

	public void setQq(String string) {
		qq = string;
	}

	public void setMsn(String string) {
		msn = string;
	}

	public void setGrade(String string) {
		grade = string;
	}

	public void setProvince(String string) {
		province = string;
	}

	public void setSchool(String string) {
		school = string;
	}

	public void setAddress(String string) {
		address = string;
	}

	public void setCode(String string) {
		code = string;
	}
}