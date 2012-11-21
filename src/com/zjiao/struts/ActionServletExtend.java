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
 * ��չStruts������Դ���֣�������վʹ�õ����ݿ����ӳ�
 * ���Ѹ�����Դ������ServletContext�У��Ա���Ҫ���ݿ����ʱʹ��
 * @author Lee Bin
 */
public class ActionServletExtend extends ActionServlet {

	public static DataSource ds = null;

	public static final String WEBAPP_PATH_KEY = "${webapp}";
	public static final String ROOT_PATH = "/";
	public static final String ENCODING_KEY = "encoding";
	public static final String TRUE = "true";

	/**
	 * ��ʼ��ѧ�ƹ�ϣ��
	 */
	public void initZjiao(){
		/** ��Ŀ¼ */
		FileUtils.ROOT=getServletContext().getRealPath("/");
		
		try{
			/** ����school.js����ѧУ��������ʡ���б� */
			Logger.Log(Logger.LOG_NOTICE,"��������school.js...");
			String content=SchoolDAO.getSchoolJS();
			String path=FileUtils.ROOT+"js"+File.separator;
			FileUtils.writeFile(content, path, "school.js");

			/** ����guild.js������ҵ�����б� */
			Logger.Log(Logger.LOG_NOTICE,"��������guild.js...");
			content=GuildDAO.getGuildJS();
			FileUtils.writeFile(content, path, "guild.js");
		}
		catch(Exception e){
			Logger.Log(Logger.LOG_ERROR, e);
		}
	}
	
	/**
	 * ʹ��Ĭ�ϲ�����������Դ
	 * @return DataSource �����õ�Ĭ������Դ
	 */
	public static DataSource setDefaultDataSource() {
	  BasicDataSource ds = new BasicDataSource();
	  ds.setDriverClassName("org.gjt.mm.mysql.Driver");
	  ds.setUsername("root");
	  ds.setPassword("djwillbay"); //����ˣ�localhost��¼����ʹ�����룡��
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
		//�����������Դ������
		if(ds!=null)
			return;
		
		//��struts�������ļ��л�ȡ����Դ��������Ϣ
		DataSourceConfig dscs[] = config.findDataSourceConfigs();
		//���û���ҵ��������Ĭ������
		if((dscs==null)||(dscs.length==0)){
			System.out.println("init default datasource....");
			ds=setDefaultDataSource();
		}else{
			//�������ò�����ʼ������Դ
			System.out.println("init struts datasource....");
			try{
				//����url���Ӷ����Ᵽ������ʱ������
				dscs[0].getProperties().put("url",dscs[0].getProperties().get("url")+"?useUnicode=true&characterEncoding=gbk");
				ds = (DataSource) RequestUtils.applicationInstance(dscs[0].getType());
				BeanUtils.populate(ds, rebuildProperties(dscs[0].getProperties()));
			}catch (Exception e) {
				System.out.println("init datasource fail....");
			}
			
		}

		getServletContext().setAttribute(ManageConn.DBN,ds);
		ManageConn.init(getServletContext());
		
		/** ��ʼ����ϣ�� */
		initZjiao();
	}

	/**
	 * ���������Զ����봦����
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
	 * ʵ�ֲ�����ͳһ���봦��
	 */
	protected void process(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
	  request.setCharacterEncoding("GBK");
	  super.process(request, response);
	}

	/**
	 * �ͷ���Դ
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