package com.c3map.school;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.Page;
import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;

/**
 * ѧУ��Ϣ���ݲ����ӿڣ��κ���ѧУ�йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��SchoolInfo��ķ��ʣ�
 * Ҳ����SchoolInfo��ķ���
 * @author Lee Bin
 */
public class SchoolDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public SchoolDAO() {
	}
	
	/**
	 * �����ѧУ
	 * @param sch ѧУ��Ϣ����
	 * @return int ִ�н��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int newSchool(SchoolInfo sch) throws SQLException {
		if(sch==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
		  /** ��ѧУ��Ϣ���в���һ����¼ */
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
	 * �ж��Ƿ����ĳѧУ
	 * @param name ѧУ����
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistSchool(String name) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���ѧУ��Ϣ */
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
	 * �ж��Ƿ����ĳѧУ
	 * @param name ѧУ����
	 * @param tp ѧУ����
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistSchool(String name, int tp) throws SQLException {
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���ѧУ��Ϣ */
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
	 * ����ѧУ������
	 * @param survey ������Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int setSchoolType(SchoolInfo sch) throws SQLException {
		if(sch==null)
			return -1;

		DBSource dbsrc = new DBSource();
		String sql="update schoolinfo set type=? where name=?";
		try {
		  /** �޸�ѧУ��Ϣ���м�¼ */
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
	 * �޸�ѧУ
	 * @param sch ������Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int modSchool(SchoolInfo sch) throws SQLException {
		if(sch==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �޸�ѧУ��Ϣ���м�¼ */
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
	 * ��ȡѧУ��Ϣ����
	 * @param name ѧУ����
	 * @return ѧУ��Ϣ����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static SchoolInfo getSchoolInfo(String name) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���������Ϣ */
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
	 * ��ȡ����ѧУ�б�
	 * @return ����ѧУ�б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getJunior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList schools=new ArrayList();
		try {
		  /** �����ݿ��м�������ѧУ�б� */
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
	 * ��ȡ����ѧУ�б�
	 * @return ����ѧУ�б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getSenior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList schools=new ArrayList();
		try {
		  /** �����ݿ��м�������ѧУ�б� */
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
	 * ��ȡ��ѧѧУ�б�
	 * @return ��ѧѧУ�б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getUniv() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList schools=new ArrayList();
		try {
		  /** �����ݿ��м�����ѧѧУ�б� */
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
	 * ��ȡʡ���б�
	 * @return ʡ���б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getProvince() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList provinces=new ArrayList();
		try {
		  /** �����ݿ��м���ʡ���б� */
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
	 * ��ȡѧУ�б�
	 * @param tp ѧУ����
	 * @param curPage ��ǰҳ��
	 * @param pageSize	��ҳ��С
	 * @return ҳ����󣨺���ҳ��Ϣ�͵�ǰҳ������ݼ�¼��
	 * @throws SQLException ���ݿ��쳣
	 */
	public static Page getPageSchool(int tp, int curPage, int pageSize) throws SQLException {
		if (pageSize<=0){
			pageSize=SysUtils.MPAGE_SIZE;
		}
		
		DBSource dbsrc = new DBSource();
		Page page=null;
		SchoolInfo sch=null;
		try {
			/** �����ݿ��м���ѧУ��¼ */
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
	 * ��ȡ����ѧУ�б��school.js
	 * @return String js����
	 * @throws SQLException ���ݿ��쳣
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