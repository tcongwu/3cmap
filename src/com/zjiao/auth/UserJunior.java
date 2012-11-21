package com.zjiao.auth;

/**
 * @author Li Bin
 * �������û���ϸ��Ϣ
 */

import java.io.Serializable;
import org.apache.struts.action.ActionForm;

/** 
 * �������û�������Ϣ�������ڱ���������û��ĸ�����Ϣ
 * @author Lee Bin
 */
public class UserJunior extends ActionForm implements Serializable{
	
	/** �û�ID */
	private int id;

	/** ĸУ���� */
	private String muSchool;
	
	/** �����꼶 */
	private String muGrade;
	
	/** �����ѧ */
	private String idealSchool;
	
	/** ���ƿ�Ŀ */
	private String strongSub;

	/** ���ƿ�Ŀ */
	private String weakSub;

	/** ϣ����ߵ����� */
	private String wantedAbility;

	/** ��ע��Ϣ���� */
	private String infoCare;

	/** ����ƫ�� */
	private String hobbySport;

	/** �鼮ƫ�� */
	private String hobbyBook;

	/** ��Ӱƫ�� */
	private String hobbyFilm;

	/** ����ƫ�� */
	private String hobbyMusic;

	/** ���������� */
	private String motto;

	/** ������ */
	private String award;

	/** ����֤�� */
	private String certificate;

	/** Ŀǰ�绰 */
	private String phone;

	/** Ŀǰ�ֻ� */
	private String mobile;

	/** Ŀǰqq */
	private String qq;

	/** �û���msn*/
	private String  msn;

	/** Ŀǰ����ʡ�� */
	private String province;

	/** Ŀǰ����ѧУ */
	private String school;

	/** Ŀǰ�����꼶 */
	private String grade;

	/** ������ϸ��ַ */
	private String address;

	/** ���˵�ַ�ʱ� */
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

	public String getIdealSchool() {
		return idealSchool;
	}

	public String getStrongSub() {
		return strongSub;
	}

	public String getWeakSub() {
		return weakSub;
	}

	public String getWantedAbility() {
		return wantedAbility;
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

	public void setIdealSchool(String string) {
		idealSchool = string;
	}

	public void setStrongSub(String string) {
		strongSub = string;
	}

	public void setWeakSub(String string) {
		weakSub = string;
	}

	public void setWantedAbility(String string) {
		wantedAbility = string;
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