package com.zjiao.struts;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.DataSourceConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

import org.apache.commons.dbcp.BasicDataSource;

import com.c3map.guild.GuildDAO;
import com.c3map.school.SchoolDAO;
import com.zjiao.db.ManageConn;
import com.zjiao.Logger;
import com.zjiao.file.FileUtils;

/**
 * 扩展Struts的数据源部分，整个网站使用的数据库连接池
 * 并把该数据源保存于ServletContext中，以备需要数据库操作时使用
 * @author Lee Bin
 */
public class ActionServletExtend extends ActionServlet {

	public static DataSource ds = null;

	public static final String WEBAPP_PATH_KEY = "${webapp}";
	public static final String ROOT_PATH = "/";
	public static final String ENCODING_KEY = "encoding";
	public static final String TRUE = "true";

	/**
	 * 初始化学科哈希表
	 */
	public void initZjiao(){
		/** 根目录 */
		FileUtils.ROOT=getServletContext().getRealPath("/");
		
		try{
			/** 生成school.js，即学校及其所在省市列表 */
			Logger.Log(Logger.LOG_NOTICE,"正在生成school.js...");
			String content=SchoolDAO.getSchoolJS();
			String path=FileUtils.ROOT+"js"+File.separator;
			FileUtils.writeFile(content, path, "school.js");

			/** 生成guild.js，即行业分类列表 */
			Logger.Log(Logger.LOG_NOTICE,"正在生成guild.js...");
			content=GuildDAO.getGuildJS();
			FileUtils.writeFile(content, path, "guild.js");
		}
		catch(Exception e){
			Logger.Log(Logger.LOG_ERROR, e);
		}
	}
	
	/**
	 * 使用默认参数设置数据源
	 * @return DataSource 创建好的默认数据源
	 */
	public static DataSource setDefaultDataSource() {
	  BasicDataSource ds = new BasicDataSource();
	  ds.setDriverClassName("org.gjt.mm.mysql.Driver");
	  ds.setUsername("root");
	  ds.setPassword("djwillbay"); //奇怪了，localhost登录不需使用密码！？
	  ds.setUrl("jdbc:mysql://166.111.118.155:3306/3cmap?useUnicode=true&characterEncoding=gbk");
	  ds.setMaxActive(20);
	  ds.setDefaultAutoCommit(false);
	  ds.setDefaultReadOnly(false);
	  ds.setMaxIdle(20);
	  ds.setMinIdle(10);
	  ds.setMaxWait(2000);

	  return ds;
	}


	protected void initModuleDataSources(ModuleConfig config) throws ServletException 
	{
		//如果存在数据源，返回
		if(ds!=null)
			return;
		
		//从struts的设置文件中获取数据源的设置信息
		DataSourceConfig dscs[] = config.findDataSourceConfigs();
		//如果没有找到，则进行默认设置
		if((dscs==null)||(dscs.length==0)){
			System.out.println("init default datasource....");
			ds=setDefaultDataSource();
		}else{
			//根据设置参数初始化数据源
			System.out.println("init struts datasource....");
			try{
				//处理url，从而避免保存中文时出乱码
				dscs[0].getProperties().put("url",dscs[0].getProperties().get("url")+"?useUnicode=true&characterEncoding=gbk");
				ds = (DataSource) RequestUtils.applicationInstance(dscs[0].getType());
				BeanUtils.populate(ds, rebuildProperties(dscs[0].getProperties()));
			}catch (Exception e) {
				System.out.println("init datasource fail....");
			}
			
		}

		getServletContext().setAttribute(ManageConn.DBN,ds);
		ManageConn.init(getServletContext());
		
		/** 初始化哈希表 */
		initZjiao();
	}

	/**
	 * 返回启用自动编码处理功能
	 * @param props
	 * @return
	 */
	private boolean isEncodingEnabled(Map props) {
		String value = (String) props.get(ENCODING_KEY);
		return TRUE.equalsIgnoreCase(value);
	}

	private Map rebuildProperties(Map props) {
		String webapp_path = getServletContext().getRealPath(ROOT_PATH);
		if (webapp_path.endsWith(File.separator))
			webapp_path = webapp_path.substring(0, webapp_path.length() - 1);
		Properties p = new Properties();
		String key;
		String value;
		for (Iterator keys = props.keySet().iterator(); 
			 keys.hasNext(); 
			 p.setProperty(key, StringUtils.replace(value, WEBAPP_PATH_KEY,webapp_path))) 
		{
			key = (String) keys.next();
			value = (String) props.get(key);
		}

		return p;
	}
	
	/**
	 * 实现参数的统一编码处理
	 */
	protected void process(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
	  request.setCharacterEncoding("GBK");
	  super.process(request, response);
	}

	/**
	 * 释放资源
	 */
	public void destroy() {
		BasicDataSource bds = (BasicDataSource)getServletContext().getAttribute(ManageConn.DBN);
		try{
			if(bds!=null){
				bds.close();
			}
		}catch(Exception e){
			Logger.Log(Logger.LOG_ERROR,e);
		}
		super.destroy();
		System.out.println("zjiao.com ended..............");
	}
}