package com.zjiao;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.zjiao.util.StringUtils;

/**
 * @author Lee Bin
 *
 * ���ݵķ�ҳ��װ��
 */
public class Page {
	
	private int curPage; //��ǰҳ
	private int pageCount; //ҳ����
	private int rowCount; //��¼��
	private int pageSize; //ÿҳ��¼��

	private ArrayList dataList; //��ǰҳ�����ݼ�
	private Iterator it; //���ݵ��������

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	public Page() {
	}
	
	/**
	 * ����ʼ�������Ĺ��캯��
	 * @param rows ��¼��
	 * @param psize ҳ���¼��С
	 * @param cpage ��ǰҳ
	 */
	public Page(int rows, int psize, int cpage){
		if((rows<0)||(psize<=0)||(cpage<0)){
			Logger.Log(Logger.LOG_WARNING,"��ʼ��Page����Ĳ������Ϸ�����ʼ��ʧ�ܣ�");
			return;
		}
		
		this.curPage=cpage;
		this.rowCount=rows;
		this.pageSize=psize;
		if(this.rowCount%this.pageSize==0){
			this.pageCount=this.rowCount/this.pageSize;
		}else{
			this.pageCount=this.rowCount/this.pageSize+1;
		}
	}
	
	/**
	 * ���ö���ķ�ҳ����
	 * @param rows ��¼��
	 * @param psize ҳ���¼��С
	 * @param cpage ��ǰҳ
	 */
	public void setParam(int rows, int psize, int cpage){
		if((rows<0)||(psize<=0)||(cpage<0)){
			Logger.Log(Logger.LOG_WARNING,"��ʼ��Page����Ĳ������Ϸ�����ʼ��ʧ�ܣ�");
			return;
		}

		this.curPage=cpage;
		this.rowCount=rows;
		this.pageSize=psize;
		if(this.rowCount%this.pageSize==0){
			this.pageCount=this.rowCount/this.pageSize;
		}else{
			this.pageCount=this.rowCount/this.pageSize+1;
		}
	}
	
	/**
	 * ��ȡ��ǰҳ���¼������
	 * @return ����
	 */
	public int getCurPageRow(){
		if(dataList!=null)
			return dataList.size();
		else
			return 0;
	}
	
	/**
	 * ��ȡ��ǰҳ��
	 * @return ��ǰҳ��
	 */
	public int getCurPage(){
		return curPage;
	}
	
	/**
	 * ��ȡҳ����
	 * @return ҳ����
	 */
	public int getPageCount(){
		return pageCount;
	}
	
	/**
	 * ��ȡ��¼��
	 * @return ��¼��
	 */
	public int getRowCount(){
		return rowCount;
	}
	
	/**
	 * ��ȡÿҳ��¼��
	 * @return ÿҳ��¼��
	 */
	public int getPageSize(){
		return pageSize;
	}
	
	/**
	 * ��ȡ���ݱ�������
	 * @return Iterator ���ݱ�������
	 */
	public Iterator getDataIterator(){
		if(it!=null)
			return it;
		else if(dataList!=null)
			return dataList.iterator();
		else
			return null;
	}
	
	/**
	 * ������ݼ�¼
	 * @param obj ���ݼ�¼����
	 */
	public void addData(Object obj){
		if(obj==null)
			return;
		
		if(dataList==null){
			dataList=new ArrayList();
			dataList.clear();
		}
		
		dataList.add(obj);
	}

	/**
	 * �ж��Ƿ�������ݼ�¼��û�б���
	 * @return �Ƿ����
	 */
	public boolean hasNext(){
		if(dataList==null)
			return false;
			
		if(it==null)
			it=dataList.iterator();
			
		return it.hasNext();
	}
	
	/**
	 * ��ȡ��¼���е���һ�����ݼ�¼
	 * @return ���ݼ�¼����
	 */
	public Object next(){
		if(dataList==null)
			return null;
			
		if(it==null)
			it=dataList.iterator();
			
		return it.next();
	}
	
	/**
	 * ��ȡҳ��Ŀ��Ƶ����˵�
	 * @param url Ҫ��ת����ҳ��
	 * @return �˵���HTML����
	 */
	public String getControlPane(String url) throws Exception{
		if(StringUtils.isNull(url))
			return "";
		
		//���δ��ҳ��������г�ʼ��������û���κ����ݣ�������κ�HTML����
		if((this.curPage==0)&&(this.pageCount==0)&&(this.pageSize==0)&&(this.rowCount==0))
			return "";
			
		if(this.pageCount==0)
			return "";
		
		//��������
		if(this.curPage>this.pageCount)
			this.curPage=this.pageCount;
		else if(this.curPage<=0)
			this.curPage=1;
			
		//��ȡ�������ַ�
		char cnt;
		if(url.indexOf('?')==-1)
			cnt='?';
		else
			cnt='&';
			
		StringBuffer html=new StringBuffer();
		html.append("��");
		html.append(curPage);
		html.append("ҳ/��");
		html.append(pageCount);
		html.append("ҳ(��");
		html.append(rowCount);
		html.append("����¼)&nbsp;");
		
		if(this.curPage!=1){
			html.append("<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=1'>&lt;&lt;��ǰҳ</a>&nbsp<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=");
			html.append(this.curPage-1);
			html.append("'>&lt;��ҳ</a>&nbsp");
		}
		
		if(this.curPage!=this.pageCount){
			html.append("<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=");
			html.append(this.curPage+1);
			html.append("'>��ҳ&gt;</a>&nbsp<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=");
			html.append(this.pageCount);
			html.append("'>���ҳ&gt;&gt</a>");
		}
		
		return html.toString();		
	}
	
	/**
	 * ��ȡҳ��Ŀ��Ƶ����˵�
	 * @param req ����ѯ�������������
	 * @param url Ҫ��ת����ҳ��
	 * @return �˵���HTML����
	 */
	public String getControlPane(HttpServletRequest req, String url)throws Exception{
		
		url=url+getQueryString(req);
		
		return getControlPane(url);
	}
	
	/**
	 * ��ȡ����ʽ��ҳ�������ת���
	 * @param url Ҫ��ת����ҳ��
	 * @return ���ֿ�������HTML����
	 */
	public String getDigitalPane(String url) throws Exception{
		if(StringUtils.isNull(url))
			return "";
		
		//���δ��ҳ��������г�ʼ��������û���κ����ݣ�������κ�HTML����
		if((this.curPage==0)&&(this.pageCount==0)&&(this.pageSize==0)&&(this.rowCount==0))
			return "";
			
		if(this.pageCount==0)
			return "";
		
		//��������
		if(this.curPage>this.pageCount)
			this.curPage=this.pageCount;
		else if(this.curPage<=0)
			this.curPage=1;
			
		//��ȡ�������ַ�
		char cnt;
		if(url.indexOf('?')==-1)
			cnt='?';
		else
			cnt='&';
			
		StringBuffer html=new StringBuffer();
		for(int i=1;i<=this.pageCount;i++){
			if(this.curPage==i){
				html.append("<span>[");
				html.append(i);
				html.append("]</span>&nbsp;");
			}else{
				html.append("<span>[<a href='");
				html.append(url);
				html.append(cnt);
				html.append("page=");
				html.append(i);
				html.append("'>");
				html.append(i);
				html.append("</a>]</span>&nbsp;");
			}
		}
		
		return html.toString();
	}
	
	/**
	 * ��ȡ����ʽ��ҳ�������ת���
	 * @param req ����ѯ�������������
	 * @param url Ҫ��ת����ҳ��
	 * @return ���ֿ�������HTML����
	 */
	public String getDigitalPane(HttpServletRequest req, String url) throws Exception{
		
		url=url+getQueryString(req);
		
		return getDigitalPane(url);
	}
	
	/**
	 * ��ȡ��������еĲ�ѯ�ֶ�
	 * @param req �������
	 * @return 
	 */
	public String getQueryString(HttpServletRequest req) throws Exception{
		Enumeration pnames=req.getParameterNames();
		StringBuffer query=new StringBuffer();
		String tmpname;
		String tmpvalue;
		int i=0;
		while(pnames.hasMoreElements()){
			tmpname=(String)pnames.nextElement();
			if((tmpname!=null)&&(!tmpname.toLowerCase().equals("page"))){
				tmpvalue=req.getParameter(tmpname);
				if(tmpvalue!=null){
					if(i==0){
						query.append('?');
					}else{
						query.append('&');
					}
					query.append(tmpname);
					query.append('=');
					query.append(tmpvalue);
					i++;
				}
			}
		}
		
		return query.toString();
	}
}
