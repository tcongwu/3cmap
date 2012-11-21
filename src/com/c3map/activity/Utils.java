/*
 * �������� 2006-8-27
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package com.c3map.activity;
import java.sql.Timestamp;
import java.util.*;


public class Utils {


	public final static int ACTIVITY_NORMAL = 1;	//�����
	public final static int ACTIVITY_HOT = 2;		//�ȵ�
	
	public final static int ACTIVITY_JOIN = 1;	//����
	public final static int ACTIVITY_SEE = 2;		//��ע�
	public final static int ACTIVITY_MESSAGE = 3;	//�������
	
	public final static int SCORE_BEGIN_ACTIVITY = 100;	//����һ�λ��100��
	public final static int SCORE_JOIN_ACTIVITY = 10;	//�μ�һ�λ��10�֣�˫��ӷ֣�
	public final static int SCORE_MESSAGE_ACTIVITY = 2;	//������Լ�2�֣�˫��ӷ֣�

	public Utils() {}

	/**
	* ����ĳһʱ��൱ǰʱ�仹�ж�����
	* @param distanceday
	* return  ����������;
	*/
	public static long getDistanceDay(Timestamp mytime){
		Timestamp now=new Timestamp(System.currentTimeMillis());
		long  mm=(now.getTime()-mytime.getTime())/(60*60*1000*24);
		return Math.abs(mm); 
	}

	/**
		* ����ĳһ�ַ���ʽʱ�䣨����2006-08-01��(������Դ������޸Ļ�Ŀ�ʼʱ��)
		* @param �ַ���ʱ�� 
		* return  Timestamp��ʽ��ʱ�� 
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
	* ����ĳһ�ַ���ʽʱ�䣨����2006-08-01��(������Դ������޸Ļ�Ľ���ʱ��,������ʱ��Ϊ��ʱ����ֵΪ��ʼʱ���һ����)
	* @param �ַ���ʱ�� 
	* return  Timestamp��ʽ��ʱ�� 
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
