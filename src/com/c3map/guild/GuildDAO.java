package com.c3map.guild;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;

/**
 * ѧУ��Ϣ���ݲ����ӿڣ��κ���ѧУ�йص����ݲ�����ͨ��������ɡ�
 * ���ڷ�װ��SchoolInfo��ķ��ʣ�
 * Ҳ����SchoolInfo��ķ���
 * @author Lee Bin
 */
public class GuildDAO {

	/**
	 * Ĭ�ϵĹ��캯��
	 */
  public GuildDAO() {
	}
	
	/**
	 * �޸���ҵ����
	 * @param guild ��ҵ������Ϣ����
	 * @return �������
	 * @throws SQLException ���ݿ��쳣
	 */
	public static int modGuild(GuildInfo guild) throws SQLException {
		if(guild==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** �޸���ҵ������Ϣ���м�¼ */
			String sql = "update guildinfo set name=?,classify=?,type=? where id=?";
			dbsrc.prepareStatement(sql);
			dbsrc.setString(1, guild.getName());
			dbsrc.setString(2, guild.getClassify());
			dbsrc.setInt(3, guild.getType());
			dbsrc.setInt(4, guild.getId());
		  
			return dbsrc.executeUpdate();
		}
		finally {
			if(dbsrc!=null)
				dbsrc.close();
		}
	}
	
	/**
	 * ��ȡ��ҵ��Ϣ����
	 * @param name ��������
	 * @return ��ҵ��Ϣ����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static GuildInfo getGuildInfo(String name) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** �����ݿ��м���������Ϣ */
		  String sql = "select * from guildinfo where name=?";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setString(1, name);
		  dbsrc.executeQuery();
		  
		  if(dbsrc.next()){
				GuildInfo guild=new GuildInfo();
				guild.setId(dbsrc.getInt("id"));
				guild.setName(dbsrc.getString("name"));
				guild.setClassify(dbsrc.getString("classify"));
				guild.setType(dbsrc.getInt("type"));
			  	
				return guild;
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return null;
	}
	
	/**
	 * ��ȡ������ҵ�����б�
	 * @return List ������ҵ�����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getJunior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList guilds=new ArrayList();
		try {
		  /** �����ݿ��м���������ҵ�б� */
		  String sql = "select id,name,classify from guildinfo where type=? or type=? or type=? order by classify";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, SysUtils.GUILD_TYPE_ALL);
		  dbsrc.setInt(2, SysUtils.GUILD_TYPE_MIDDLE);
		  dbsrc.setInt(3, SysUtils.GUILD_TYPE_JUNIOR);
		  dbsrc.executeQuery();
		  
		  GuildInfo guild;
		  while(dbsrc.next()){
				guild=new GuildInfo();
				guild.setId(dbsrc.getInt("id"));
				guild.setName(dbsrc.getString("name"));
				guild.setClassify(dbsrc.getString("classify"));
			  	
				guilds.add(guild);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return guilds;
	}
	
	/**
	 * ��ȡ������ҵ�����б�
	 * @return List ������ҵ�����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getSenior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList guilds=new ArrayList();
		try {
		  /** �����ݿ��м���������ҵ�б� */
		  String sql = "select id,name,classify from guildinfo where type=? or type=? or type=? order by classify";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, SysUtils.GUILD_TYPE_ALL);
		  dbsrc.setInt(2, SysUtils.GUILD_TYPE_MIDDLE);
		  dbsrc.setInt(3, SysUtils.GUILD_TYPE_SENIOR);
		  dbsrc.executeQuery();
		  
		  GuildInfo guild;
		  while(dbsrc.next()){
				guild=new GuildInfo();
				guild.setId(dbsrc.getInt("id"));
				guild.setName(dbsrc.getString("name"));
				guild.setClassify(dbsrc.getString("classify"));
			  	
				guilds.add(guild);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return guilds;
	}
	
	/**
	 * ��ȡ��ѧ��ҵ�����б�
	 * @return List ��ѧ��ҵ�����б�
	 * @throws SQLException ���ݿ��쳣
	 */
	public static List getUniv() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList guilds=new ArrayList();
		try {
		  /** �����ݿ��м�����ѧ��ҵ�б� */
		  String sql = "select id,name,classify from guildinfo where type=? or type=? order by classify";
		  dbsrc.prepareStatement(sql);
		  dbsrc.setInt(1, SysUtils.GUILD_TYPE_ALL);
		  dbsrc.setInt(2, SysUtils.GUILD_TYPE_UNIV);
		  dbsrc.executeQuery();
		  
		  GuildInfo guild;
		  while(dbsrc.next()){
				guild=new GuildInfo();
				guild.setId(dbsrc.getInt("id"));
				guild.setName(dbsrc.getString("name"));
				guild.setClassify(dbsrc.getString("classify"));
			  	
				guilds.add(guild);
		  }
		  
		} finally {
		  if (dbsrc!=null)
				dbsrc.close();
		}
		
		return guilds;
	}
	
	/**
	 * ��ȡ��ҵ�����б��guild.js
	 * @return String js����
	 * @throws SQLException ���ݿ��쳣
	 */
	public static String getGuildJS() throws Exception {
		
		GuildInfo guild;
		List junior=getJunior();
		List senior=getSenior();
		List univ=getUniv();
		
		StringBuffer js=new StringBuffer();
		js.append("var gjunior1=new Array();");
		js.append('\n');
		js.append("var gsenior1=new Array();");
		js.append('\n');
		js.append("var guniv1=new Array();");
		js.append('\n');
		js.append("var gjunior2=new Array();");
		js.append('\n');
		js.append("var gsenior2=new Array();");
		js.append('\n');
		js.append("var guniv2=new Array();");
		js.append('\n');
		js.append('\n');
		
		for(int i=0; i<junior.size(); i++){
			guild=(GuildInfo)junior.get(i);
			js.append("gjunior1[");
			js.append(i);
			js.append("]=\"");
			js.append(guild.getClassify());
			js.append("\";");
			
			js.append('\n');
			
			js.append("gjunior2[");
			js.append(i);
			js.append("]=\"");
			js.append(guild.getName());
			js.append("\";");
			
			js.append('\n');
		}
		
		for(int i=0; i<senior.size(); i++){
			guild=(GuildInfo)senior.get(i);
			js.append("gsenior1[");
			js.append(i);
			js.append("]=\"");
			js.append(guild.getClassify());
			js.append("\";");
			
			js.append('\n');
			
			js.append("gsenior2[");
			js.append(i);
			js.append("]=\"");
			js.append(guild.getName());
			js.append("\";");
			
			js.append('\n');
		}
		
		for(int i=0; i<univ.size(); i++){
			guild=(GuildInfo)univ.get(i);
			js.append("guniv1[");
			js.append(i);
			js.append("]=\"");
			js.append(guild.getClassify());
			js.append("\";");
			
			js.append('\n');
			
			js.append("guniv2[");
			js.append(i);
			js.append("]=\"");
			js.append(guild.getName());
			js.append("\";");
			
			js.append('\n');
		}
		
		return js.toString();
	}
}