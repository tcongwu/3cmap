package com.c3map.blog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.zjiao.db.DBSource;
public class MonarticleDAO {
public MonarticleDAO(){}
	
	  
		/**
		 * 获取某用户各月份的blog文章统计
		 * @param uid 用户ID
		 * @return 按月份blog文章列表
		 * @throws SQLException 数据库异常
		 */
		public static List getArtnumbyId(int uid) throws SQLException {
			
			    DBSource dbsrc = new DBSource();
		    	ArrayList art=new ArrayList();
			    Monarticle monart=null;
			try {
			  /** 从数据库中检索出各月份文章数目统计信息 */
			    String sql = "select mon_time,count(*) cnt from monarticle where author_id=? group by mon_time";
			    dbsrc.prepareStatement(sql);
			    dbsrc.setInt(1, uid);
			    dbsrc.executeQuery();
			 while(dbsrc.next()){
			  	monart=new Monarticle();
			  	monart.setMontime(dbsrc.getString("mon_time"));
			  	monart.setMonnum(dbsrc.getInt("cnt"));
				  art.add(monart);
			  }
			  
			} finally {
			  if (dbsrc!=null)
					dbsrc.close();
			}
			return art;
		}
	  
	
	
}
