package com.c3map.guild;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zjiao.SysUtils;

import com.zjiao.db.DBSource;

/**
 * 学校信息数据操作接口，任何与学校有关的数据操作都通过此类完成。
 * 用于封装对SchoolInfo类的访问，
 * 也就是SchoolInfo表的访问
 * @author Lee Bin
 */
public class GuildDAO {

	/**
	 * 默认的构造函数
	 */
  public GuildDAO() {
	}
	
	/**
	 * 修改行业分类
	 * @param guild 行业分类信息对象
	 * @return 操作结果
	 * @throws SQLException 数据库异常
	 */
	public static int modGuild(GuildInfo guild) throws SQLException {
		if(guild==null)
			return -1;

		DBSource dbsrc = new DBSource();
		try {
			/** 修改行业分类信息表中记录 */
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
	 * 获取行业信息对象
	 * @param name 分类名称
	 * @return 行业信息对象
	 * @throws SQLException 数据库异常
	 */
	public static GuildInfo getGuildInfo(String name) throws SQLException {
		
		DBSource dbsrc = new DBSource();
		try {
		  /** 从数据库中检索问题信息 */
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
	 * 获取初中行业分类列表
	 * @return List 初中行业分类列表
	 * @throws SQLException 数据库异常
	 */
	public static List getJunior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList guilds=new ArrayList();
		try {
		  /** 从数据库中检索初中行业列表 */
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
	 * 获取高中行业分类列表
	 * @return List 高中行业分类列表
	 * @throws SQLException 数据库异常
	 */
	public static List getSenior() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList guilds=new ArrayList();
		try {
		  /** 从数据库中检索高中行业列表 */
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
	 * 获取大学行业分类列表
	 * @return List 大学行业分类列表
	 * @throws SQLException 数据库异常
	 */
	public static List getUniv() throws SQLException {
		
		DBSource dbsrc = new DBSource();
		ArrayList guilds=new ArrayList();
		try {
		  /** 从数据库中检索大学行业列表 */
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
	 * 获取行业分类列表的guild.js
	 * @return String js内容
	 * @throws SQLException 数据库异常
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