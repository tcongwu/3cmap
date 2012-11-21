package com.c3map.school;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;

/**
 * 学校信息数据操作接口，任何与学校有关的数据操作都通过此类完成。
 * 用于封装对SchoolInfo类的访问，
 * 也就是SchoolInfo表的访问
 * @author Lee Bin
 */
public class SchoolDAO {

	/**
	 * 默认的构造函数
	 */
  public SchoolDAO() {
	}
	
	/**
	 * 添加新学校
	 * @param sch 学校信息对象
	 * @return int 执行结果
	 * @throws SQLException 数据库异常
	 */
	public static int newSchool(SchoolInfo sch) throws SQLException {
		if(sch==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
		  /** 在学校信息表中插入一条记录 */
		  String sql = "insert into schoolinfo (name,type,province,city,description) values(?,?,?,?,?)";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, sch.getName());
		  dbsrc.setInt(2, sch.getType());
		  dbsrc.setString(3, sch.getProvince());
		  dbsrc.setString(4, sch.getCity());
		  dbsrc.setString(5, sch.getDescription());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 判断是否存在某学校
	 * @param name 学校名称
	 * @return boolean 是否存在
	 */
	public static boolean isExistSchool(String name) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索学校信息 */
		  String sql = "select name from schoolinfo where name=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, name);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				return true;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return false;
	}
	
	/**
	 * 判断是否存在某学校
	 * @param name 学校名称
	 * @param tp 学校类型
	 * @return boolean 是否存在
	 */
	public static boolean isExistSchool(String name, int tp) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索学校信息 */
		  String sql = "select name from schoolinfo where name=? and type=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, name);
		  dbsrc.setInt(2, tp);
		  dbsrc.executeQuery();
		  
		  if (dbsrc.next()) {
				return true;
		  }
		  
		} finally {
		  if (dbsrc!=null)
			dbsrc.close();
		}
		
		return false;
	}
	
	/**
	 * 设置学校的类型
	 * @param survey 问题信息对象
	 * @return 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int setSchoolType(SchoolInfo sch) throws SQLException {
		if(sch==null)
			return -1;

		DBSource dbsrc = new DBSource();
		String sql="update schoolinfo set type=? where name=?";
		try {
		  /** 修改学校信息表中记录 */
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, sch.getType());
		  dbsrc.setString(2, sch.getName());
		  
		  return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 修改学校
	 * @param sch 试题信息对象
	 * @return 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int modSchool(SchoolInfo sch) throws SQLException {
		if(sch==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** 修改学校信息表中记录 */
			String sql = "update schoolinfo set type=?,province=?,city=?,description=? where name=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setInt(1, sch.getType());
			dbsrc.setString(2, sch.getProvince());
			dbsrc.setString(3, sch.getCity());
			dbsrc.setString(4, sch.getDescription());
			dbsrc.setString(5, sch.getName());
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * 获取学校信息对象
	 * @param name 学校名称
	 * @return 学校信息对象
	 * @throws SQLException 数据库异常
	 */
	public static SchoolInfo getSchoolInfo(String name) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索问题信息 */
		  String sql = "select * from schoolinfo where name=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, name);
		  dbsrc.executeQuery();
		  
		  if(dbsrc.next()){
				SchoolInfo sch=new SchoolInfo();
				sch.setName(name);
				sch.setType(dbsrc.getInt("type"));
				sch.setProvince(dbsrc.getString("province"));
				sch.setCity(dbsrc.getString("city"));
				sch.setDescription(dbsrc.getString("description"));
			  	
				return sch;
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * 获取初中学校列表
	 * @return 初中学校列表
	 * @throws SQLException 数据库异常
	 */
	public static List getJunior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList schools=new ArrayList();
		try {
		  /** 从数据库中检索初中学校列表 */
		  String sql = "select name,province from schoolinfo where type=? or type=? order by province";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, SysUtils.SCHOOL_JUNIOR);
		  dbsrc.setInt(2, SysUtils.SCHOOL_MIDDLE);
		  dbsrc.executeQuery();
		  
		  SchoolInfo sch;
		  while(dbsrc.next()){
				sch=new SchoolInfo();
				sch.setName(dbsrc.getString("name"));
				sch.setProvince(dbsrc.getString("province"));
			  	
				schools.add(sch);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return schools;
	}
	
	/**
	 * 获取高中学校列表
	 * @return 高中学校列表
	 * @throws SQLException 数据库异常
	 */
	public static List getSenior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList schools=new ArrayList();
		try {
		  /** 从数据库中检索高中学校列表 */
		  String sql = "select name,province from schoolinfo where type=? or type=? order by province";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, SysUtils.SCHOOL_SENIOR);
		  dbsrc.setInt(2, SysUtils.SCHOOL_MIDDLE);
		  dbsrc.executeQuery();
		  
		  SchoolInfo sch;
		  while(dbsrc.next()){
				sch=new SchoolInfo();
				sch.setName(dbsrc.getString("name"));
				sch.setProvince(dbsrc.getString("province"));
			  	
				schools.add(sch);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return schools;
	}
	
	/**
	 * 获取大学学校列表
	 * @return 大学学校列表
	 * @throws SQLException 数据库异常
	 */
	public static List getUniv() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList schools=new ArrayList();
		try {
		  /** 从数据库中检索大学学校列表 */
		  String sql = "select name,province from schoolinfo where type=? order by province";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, SysUtils.SCHOOL_UNIV);
		  dbsrc.executeQuery();
		  
		  SchoolInfo sch;
		  while(dbsrc.next()){
				sch=new SchoolInfo();
				sch.setName(dbsrc.getString("name"));
				sch.setProvince(dbsrc.getString("province"));
			  	
				schools.add(sch);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return schools;
	}
	
	/**
	 * 获取省份列表
	 * @return 省份列表
	 * @throws SQLException 数据库异常
	 */
	public static List getProvince() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList provinces=new ArrayList();
		try {
		  /** 从数据库中检索省份列表 */
		  String sql = "select name from provinceinfo order by name";
		  dbsrc.prepareStatement(sql);
		  dbsrc.executeQuery();
		  
		  while(dbsrc.next()){
				provinces.add(dbsrc.getString("name"));
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return provinces;
	}
	
	/**
	 * 获取学校列表
	 * @param tp 学校类型
	 * @param curPage 当前页面
	 * @param pageSize	分页大小
	 * @return 页面对象（含分页信息和当前页面的数据记录）
	 * @throws SQLException 数据库异常
	 */
	public static Page getPageSchool(int tp, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.MPAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		SchoolInfo sch=null;
		try {
			/** 从数据库中检索学校记录 */
			String sql;
			if(tp==0){
				sql = "select * from schoolinfo";
			}else{
				sql = "select * from schoolinfo where type=?";
			}
			dbsrc.prepareStatement(sql);
			if(tp!=0){
				dbsrc.setInt(1, tp);
			}
			dbsrc.executeQuery();
		  
		  dbsrc.setPageSize(pageSize);
		  dbsrc.setPageNo(curPage);
		  page=new Page(dbsrc.getRowNum(), dbsrc.getPageSize(), dbsrc.getPageNo());
		  
		  while(dbsrc.pageNext()){
				sch=new SchoolInfo();
				sch.setName(dbsrc.getString("name"));
				sch.setType(dbsrc.getInt("type"));
				sch.setProvince(dbsrc.getString("province"));
				sch.setCity(dbsrc.getString("city"));
				sch.setDescription(dbsrc.getString("description"));
			  
				page.addData(sch);
			}
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return page;
	}
	
	/**
	 * 获取生成学校列表的school.js
	 * @return String js内容
	 * @throws SQLException 数据库异常
	 */
	public static String getSchoolJS()  throws  Exception {
		
		SchoolInfo sch;
		List junior=getJunior();
		List senior=getSenior();
		List univ=getUniv();
		List province=getProvince();
		
		StringBuffer js=new StringBuffer();
		js.append("var junior1=new Array();");
		js.append('\n');
		js.append("var senior1=new Array();");
		js.append('\n');
		js.append("var univ1=new Array();");
		js.append('\n');
		js.append("var junior2=new Array();");
		js.append('\n');
		js.append("var senior2=new Array();");
		js.append('\n');
		js.append("var univ2=new Array();");
		js.append('\n');
		js.append("var province=new Array();");
		js.append('\n');
		js.append('\n');
		
		for(int i=0; i<junior.size(); i++){
			sch=(SchoolInfo)junior.get(i);
			js.append("junior1[");
			js.append(i);
			js.append("]=\"");
			js.append(sch.getProvince());
			js.append("\";");
			
			js.append('\n');
			
			js.append("junior2[");
			js.append(i);
			js.append("]=\"");
			js.append(sch.getName());
			js.append("\";");
			
			js.append('\n');
		}
		
		for(int i=0; i<senior.size(); i++){
			sch=(SchoolInfo)senior.get(i);
			js.append("senior1[");
			js.append(i);
			js.append("]=\"");
			js.append(sch.getProvince());
			js.append("\";");
			
			js.append('\n');
			
			js.append("senior2[");
			js.append(i);
			js.append("]=\"");
			js.append(sch.getName());
			js.append("\";");
			
			js.append('\n');
		}
		
		for(int i=0; i<univ.size(); i++){
			sch=(SchoolInfo)univ.get(i);
			js.append("univ1[");
			js.append(i);
			js.append("]=\"");
			js.append(sch.getProvince());
			js.append("\";");
			
			js.append('\n');
			
			js.append("univ2[");
			js.append(i);
			js.append("]=\"");
			js.append(sch.getName());
			js.append("\";");
			
			js.append('\n');
		}
		
		for(int i=0; i<province.size(); i++){
			js.append("province[");
			js.append(i);
			js.append("]=\"");
			js.append(province.get(i));
			js.append("\";");
			
			js.append('\n');
		}
		
		js.append('\n');

		js.append("function showProvince(fm, num){");
		js.append('\n');
		js.append("	var temp=\"\", fobj, len=0, arr;");
		js.append('\n');
		js.append("	eval(\"fobj=\"+fm);//var num=reg.userType.value");
		js.append('\n');
		js.append("	if((num<1)||(num>3)){");
		js.append('\n');
		js.append("		return;");
		js.append('\n');
		js.append("	}	");
		js.append('\n');
		js.append("	if(num==1){");
		js.append('\n');
		js.append("		len=junior1.length;");
		js.append('\n');
		js.append("		arr=\"junior\";");
		js.append('\n');
		js.append("	}else if(num==2){");
		js.append('\n');
		js.append("		len=senior1.length;");
		js.append('\n');
		js.append("		arr=\"senior\";");
		js.append('\n');
		js.append("	}else{");
		js.append('\n');
		js.append("		len=univ1.length;");
		js.append('\n');
		js.append("		arr=\"univ\";");
		js.append('\n');
		js.append("	}");
		js.append('\n');
	
		js.append("	while(fobj.province.length>1){");
		js.append('\n');
		js.append("		fobj.province.options.remove(1);");
		js.append('\n');
		js.append("	}");
		js.append('\n');
		js.append("	for (var i=0;i<len;i++){");
		js.append('\n');
		js.append("		if (eval(\"temp!=\"+arr+\"1[i]\")){");
		js.append('\n');
		js.append("			  eval(\"temp=\"+arr+\"1[i]\");");
		js.append('\n');
		js.append("			  var newitem=document.createElement(\"OPTION\");");
		js.append('\n');
		js.append("			  newitem.text=temp;");
		js.append('\n');
		js.append("			  newitem.value=temp;");
		js.append('\n');
		js.append("			  fobj.province.options.add(newitem);");
		js.append('\n');
		js.append("		}");
		js.append('\n');
		js.append("	}");
		js.append('\n');
		js.append("  }");
		js.append('\n');

		js.append("function showSchool(fm, num, province)");
		js.append('\n');
		js.append("  {");
		js.append('\n');
		js.append("	var temp, fobj, len=0, arr;");
		js.append('\n');
		js.append("	eval(\"fobj=\"+fm);//var num=reg.userType.value");
		js.append('\n');
		js.append("	if((num<1)||(num>3)){");
		js.append('\n');
		js.append("		return;");
		js.append('\n');
		js.append("	}	");
		js.append('\n');
		js.append("	if(num==1){");
		js.append('\n');
		js.append("		len=junior1.length;");
		js.append('\n');
		js.append("		arr=\"junior\";");
		js.append('\n');
		js.append("	}else if(num==2){");
		js.append('\n');
		js.append("		len=senior1.length;");
		js.append('\n');
		js.append("		arr=\"senior\";");
		js.append('\n');
		js.append("	}else{");
		js.append('\n');
		js.append("		len=univ1.length;");
		js.append('\n');
		js.append("		arr=\"univ\";");
		js.append('\n');
		js.append("	}");
		js.append('\n');

		js.append("	while(fobj.school.length>1){");
		js.append('\n');
		js.append("		fobj.school.options.remove(1);");
		js.append('\n');
		js.append("	}");
		js.append('\n');
		js.append("	for (var i=0;i<len;i++)");
		js.append('\n');
		js.append("	{");
		js.append('\n');
		js.append("	  if (eval(arr+\"1[i]==province\")){");
		js.append('\n');
		js.append("		eval(\"temp=\"+arr+\"2[i]\");");
		js.append('\n');
		js.append("		var newitem=document.createElement(\"OPTION\");");
		js.append('\n');
		js.append("		newitem.text=temp;");
		js.append('\n');
		js.append("		newitem.value=temp;");
		js.append('\n');
		js.append("		fobj.school.options.add(newitem);");
		js.append('\n');
		js.append("	  }");
		js.append('\n');
		js.append("	}");
		js.append('\n');
		js.append("  }");
		js.append('\n');

		js.append("function listAllProvince(lst)");
		js.append('\n');
		js.append("  {");
		js.append('\n');
		js.append("	var len=0;");
		js.append('\n');
		js.append("	len=province.length;");
		js.append('\n');
		js.append("	for (var i=0;i<len;i++)");
		js.append('\n');
		js.append("	{");
		js.append('\n');
		js.append("		var newitem=document.createElement(\"OPTION\");");
		js.append('\n');
		js.append("		newitem.text=province[i];");
		js.append('\n');
		js.append("		newitem.value=province[i];");
		js.append('\n');
		js.append("		eval(lst+\".options.add(newitem)\");");
		js.append('\n');
		js.append("	}");
		js.append('\n');
		js.append("  }");
		js.append('\n');
		
		return js.toString();
	}
}