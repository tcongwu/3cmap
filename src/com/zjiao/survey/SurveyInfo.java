package com.zjiao.survey;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

import com.zjiao.SysUtils;
import com.zjiao.auth.Role;

/** 
 * 小调查信息对象，用于保存班级的调查投票数据，及其问题信息
 * @author Lee Bin
 */
public class SurveyInfo extends ActionForm implements Serializable{

	private int id;
	private String title;
	private String type;
	private int cnt;
	private String status;
	private String content;
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String f;
	private String g;
	private String h;
	private int author;
	private Timestamp createTime;

	private int quesId;
	private int classId;
	private int isGoing;
	private int isShowResult;
	private int total;
	private int va;
	private int vb;
	private int vc;
	private int vd;
	private int ve;
	private int vf;
	private int vg;
	private int vh;
	private String voteDetail;
	private Timestamp beginTime;
	private Timestamp endTime;
	
	/**
	 * 判断当前讨论是否有效
	 * @return boolean 是否有效
	 */
	public boolean isValid() {
		return SysUtils.STATUS_NORMAL.equals(status);
	}
	
	/**
	 * 投票
	 * @param va 投票结果
	 * @param rl 投票角色
	 * @return 操作结果
	 */
	public int poll(String val, Role rl) throws SQLException{
		if(voteDetail==null){
			voteDetail="";
		}
		
		//只有在当前角色没有投票，而且班级相符的情况下，才进行计数
		if((val!=null)&&(rl!=null)&&(classId==rl.getInt("classid"))&&(voteDetail.indexOf(rl.getType()+rl.getId(), 0)==-1)){
			if(val.indexOf("A",0)!=-1){
				va++;
			}
			if(val.indexOf("B", 0)!=-1){
				vb++;
			}
			if(val.indexOf("C", 0)!=-1){
				vc++;
			}
			if(val.indexOf("D", 0)!=-1){
				vd++;
			}
			if(val.indexOf("E", 0)!=-1){
				ve++;
			}
			if(val.indexOf("F", 0)!=-1){
				vf++;
			}
			if(val.indexOf("G", 0)!=-1){
				vg++;
			}
			if(val.indexOf("H", 0)!=-1){
				vh++;
			}
			total++;
			voteDetail=voteDetail+";"+rl.getType()+rl.getId()+"-"+val;
			
			return SurveyDAO.updateSurvey(this, rl);
		}
		
		return 1;
	}
	
	/**
	 * 获取问题类型名称
	 * @return 类型名称
	 */
	public String getTypeName(){
		if("1".equals(type)){
			return "单选";
		}else if("2".equals(type)){
			return "多选";
		}else{
			return "";
		}
	}
	
	/**
	 * 获取选项内容
	 * @param i 选项序号
	 * @return 选项内容
	 */
	public String getOptions(int i){
		switch (i) {
			case 0:	return a;
			case 1:	return b;
			case 2:	return c;
			case 3:	return d;
			case 4:	return e;
			case 5:	return f;
			case 6:	return g;
			case 7:	return h;
		}
		return "";
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public int getCnt() {
		return cnt;
	}

	public String getStatus() {
		return status;
	}

	public String getContent() {
		return content;
	}

	public String getA() {
		return a;
	}

	public String getB() {
		return b;
	}

	public String getC() {
		return c;
	}

	public String getD() {
		return d;
	}

	public String getE() {
		return e;
	}

	public String getF() {
		return f;
	}

	public String getG() {
		return g;
	}

	public String getH() {
		return h;
	}

	public int getAuthor() {
		return author;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public int getQuesId() {
		return quesId;
	}

	public int getClassId() {
		return classId;
	}

	public int getIsGoing() {
		return isGoing;
	}

	public int getIsShowResult() {
		return isShowResult;
	}

	public int getTotal() {
		return total;
	}

	public int getVa() {
		return va;
	}

	public int getVb() {
		return vb;
	}

	public int getVc() {
		return vc;
	}

	public int getVd() {
		return vd;
	}

	public int getVe() {
		return ve;
	}

	public int getVf() {
		return vf;
	}

	public int getVg() {
		return vg;
	}

	public int getVh() {
		return vh;
	}

	public String getVoteDetail() {
		return voteDetail;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	//----------------------------------------
	//----------------------------------------
	public void setId(int it) {
		id = it;
	}
	
	public void setTitle(String string) {
		title = string;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	public void setCnt(int it) {
		cnt = it;
	}
	
	public void setStatus(String string) {
		status = string;
	}
	
	public void setContent(String string) {
		content = string;
	}
	
	public void setA(String string) {
		a = string;
	}
	
	public void setB(String string) {
		b = string;
	}
	
	public void setC(String string) {
		c = string;
	}
	
	public void setD(String string) {
		d = string;
	}
	
	public void setE(String string) {
		e = string;
	}
	
	public void setF(String string) {
		f = string;
	}
	
	public void setG(String string) {
		g = string;
	}
	
	public void setH(String string) {
		h = string;
	}
	
	public void setAuthor(int it) {
		author = it;
	}
	
	public void setCreateTime(Timestamp ts) {
		createTime = ts;
	}
	
	public void setQuesId(int it) {
		quesId = it;
	}
	
	public void setClassId(int it) {
		classId = it;
	}
	
	public void setIsGoing(int it) {
		isGoing = it;
	}
	
	public void setIsShowResult(int it) {
		isShowResult = it;
	}
	
	public void setTotal(int it) {
		total = it;
	}
	
	public void setVa(int it) {
		va = it;
	}
	
	public void setVb(int it) {
		vb = it;
	}
	
	public void setVc(int it) {
		vc = it;
	}
	
	public void setVd(int it) {
		vd = it;
	}
	
	public void setVe(int it) {
		ve = it;
	}
	
	public void setVf(int it) {
		vf = it;
	}
	
	public void setVg(int it) {
		vg = it;
	}
	
	public void setVh(int it) {
		vh = it;
	}
	
	public void setVoteDetail(String string) {
		voteDetail = string;
	}

	public void setBeginTime(Timestamp ts) {
		beginTime = ts;
	}
	
	public void setEndTime(Timestamp ts) {
		endTime = ts;
	}
	
}
