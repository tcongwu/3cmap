package com.c3map.blog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.zjiao.db.DBSource;
public class MonarticleDAO {
public MonarticleDAO(){}
	
	  
		/**
		 * ��ȡĳ�û����·ݵ�blog����ͳ��
		 * @param uid �û�ID
		 * @return ���·�blog�����б�
		 * @throws SQLException ���ݿ��쳣
		 */
		public static List getArtnumbyId(int uid) throws SQLException {
			
			    DBSource dbsrc = new DBSource();
		    	ArrayList art=new ArrayList();
			    Monarticle monart=null;
			try {
			  /** �����ݿ��м��������·�������Ŀͳ����Ϣ */
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
