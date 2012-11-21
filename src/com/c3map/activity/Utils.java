/*
 * 创建日期 2006-8-27
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.c3map.activity;
import java.sql.Timestamp;
import java.util.*;


public class Utils {


	public final static int ACTIVITY_NORMAL = 1;	//正常活动
	public final static int ACTIVITY_HOT = 2;		//热点活动
	
	public final static int ACTIVITY_JOIN = 1;	//参与活动
	public final static int ACTIVITY_SEE = 2;		//关注活动
	public final static int ACTIVITY_MESSAGE = 3;	//给活动留言
	
	public final static int SCORE_BEGIN_ACTIVITY = 100;	//发起一次活动扣100分
	public final static int SCORE_JOIN_ACTIVITY = 10;	//参加一次活动加10分（双向加分）
	public final static int SCORE_MESSAGE_ACTIVITY = 2;	//给活动留言加2分（双向加分）

	public Utils() {}

	/**
	* 处理某一时间距当前时间还有多少天
	* @param distanceday
	* return  处理后的天数;
	*/
	public static long getDistanceDay(Timestamp mytime){
		Timestamp now=new Timestamp(System.currentTimeMillis());
		long  mm=(now.getTime()-mytime.getTime())/(60*60*1000*24);
		return Math.abs(mm); 
	}

	/**
		* 处理某一字符格式时间（例如2006-08-01）(仅仅针对创建和修改活动的开始时间)
		* @param 字符串时间 
		* return  Timestamp格式的时间 
		*/
		public static Timestamp StingToTimestamp(String mytime,String bin_hour,String bin_min){
			
			Calendar cal = Calendar.getInstance(); 
			
			String y1=mytime.substring(0,4);
			String m1=mytime.substring(5,7);
			String d1=mytime.substring(8,10);
					
			if(mytime.substring(5,6).equals("0")){
					m1=mytime.substring(6,7);
				}
			if(mytime.substring(8,9).equals("0")){
					d1=mytime.substring(9,10);
				}
					
			cal.set( Integer.parseInt(y1), Integer.parseInt(m1)-1, Integer.parseInt(d1),Integer.parseInt(bin_hour),Integer.parseInt(bin_min),0 );
			
			Timestamp start=new Timestamp(cal.getTimeInMillis());	
		
			return start;
			
		}
		
	/**
	* 处理某一字符格式时间（例如2006-08-01）(仅仅针对创建和修改活动的结束时间,当结束时间为空时，其值为开始时间加一个月)
	* @param 字符串时间 
	* return  Timestamp格式的时间 
	*/
	public static Timestamp StingToTimestampEnd(String mytime,String bin_hour,String bin_min){
			
		Calendar cal = Calendar.getInstance(); 
			
		String y1=mytime.substring(0,4);
		String m1=mytime.substring(5,7);
		String d1=mytime.substring(8,10);
			
		if(mytime.substring(5,6).equals("0")){
				m1=mytime.substring(6,7);
			}
		if(mytime.substring(8,9).equals("0")){
				d1=mytime.substring(9,10);
			}
			
		cal.set( Integer.parseInt(y1), Integer.parseInt(m1), Integer.parseInt(d1),Integer.parseInt(bin_hour),Integer.parseInt(bin_min),0 );
	
		Timestamp end=new Timestamp(cal.getTimeInMillis());	

		return end;
			
			}
		
	}
