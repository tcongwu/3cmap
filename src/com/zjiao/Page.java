package com.zjiao;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.zjiao.util.StringUtils;

/**
 * @author Lee Bin
 *
 * 数据的分页包装类
 */
public class Page {
	
	private int curPage; //当前页
	private int pageCount; //页面数
	private int rowCount; //记录数
	private int pageSize; //每页记录数

	private ArrayList dataList; //当前页面数据集
	private Iterator it; //数据的输出集合

	/**
	 * 默认的构造函数
	 */
	public Page() {
	}
	
	/**
	 * 带初始化参数的构造函数
	 * @param rows 记录数
	 * @param psize 页面记录大小
	 * @param cpage 当前页
	 */
	public Page(int rows, int psize, int cpage){
		if((rows<0)||(psize<=0)||(cpage<0)){
			Logger.Log(Logger.LOG_WARNING,"初始化Page对象的参数不合法，初始化失败！");
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
	 * 设置对象的分页参数
	 * @param rows 记录数
	 * @param psize 页面记录大小
	 * @param cpage 当前页
	 */
	public void setParam(int rows, int psize, int cpage){
		if((rows<0)||(psize<=0)||(cpage<0)){
			Logger.Log(Logger.LOG_WARNING,"初始化Page对象的参数不合法，初始化失败！");
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
	 * 获取当前页面记录的行数
	 * @return 行数
	 */
	public int getCurPageRow(){
		if(dataList!=null)
			return dataList.size();
		else
			return 0;
	}
	
	/**
	 * 获取当前页面
	 * @return 当前页面
	 */
	public int getCurPage(){
		return curPage;
	}
	
	/**
	 * 获取页面数
	 * @return 页面数
	 */
	public int getPageCount(){
		return pageCount;
	}
	
	/**
	 * 获取记录数
	 * @return 记录数
	 */
	public int getRowCount(){
		return rowCount;
	}
	
	/**
	 * 获取每页记录数
	 * @return 每页记录数
	 */
	public int getPageSize(){
		return pageSize;
	}
	
	/**
	 * 获取数据遍历集合
	 * @return Iterator 数据遍历集合
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
	 * 添加数据记录
	 * @param obj 数据记录对象
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
	 * 判断是否存在数据记录还没有遍历
	 * @return 是否存在
	 */
	public boolean hasNext(){
		if(dataList==null)
			return false;
			
		if(it==null)
			it=dataList.iterator();
			
		return it.hasNext();
	}
	
	/**
	 * 获取记录集中的下一个数据记录
	 * @return 数据记录对象
	 */
	public Object next(){
		if(dataList==null)
			return null;
			
		if(it==null)
			it=dataList.iterator();
			
		return it.next();
	}
	
	/**
	 * 获取页面的控制调整菜单
	 * @param url 要跳转到的页面
	 * @return 菜单的HTML代码
	 */
	public String getControlPane(String url) throws Exception{
		if(StringUtils.isNull(url))
			return "";
		
		//如果未对页面参数进行初始化，或者没有任何数据，则不输出任何HTML代码
		if((this.curPage==0)&&(this.pageCount==0)&&(this.pageSize==0)&&(this.rowCount==0))
			return "";
			
		if(this.pageCount==0)
			return "";
		
		//合理化参数
		if(this.curPage>this.pageCount)
			this.curPage=this.pageCount;
		else if(this.curPage<=0)
			this.curPage=1;
			
		//获取参数连字符
		char cnt;
		if(url.indexOf('?')==-1)
			cnt='?';
		else
			cnt='&';
			
		StringBuffer html=new StringBuffer();
		html.append("第");
		html.append(curPage);
		html.append("页/共");
		html.append(pageCount);
		html.append("页(共");
		html.append(rowCount);
		html.append("条记录)&nbsp;");
		
		if(this.curPage!=1){
			html.append("<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=1'>&lt;&lt;最前页</a>&nbsp<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=");
			html.append(this.curPage-1);
			html.append("'>&lt;上页</a>&nbsp");
		}
		
		if(this.curPage!=this.pageCount){
			html.append("<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=");
			html.append(this.curPage+1);
			html.append("'>下页&gt;</a>&nbsp<a href='");
			html.append(url);
			html.append(cnt);
			html.append("page=");
			html.append(this.pageCount);
			html.append("'>最后页&gt;&gt</a>");
		}
		
		return html.toString();		
	}
	
	/**
	 * 获取页面的控制调整菜单
	 * @param req 含查询参数的请求对象
	 * @param url 要跳转到的页面
	 * @return 菜单的HTML代码
	 */
	public String getControlPane(HttpServletRequest req, String url)throws Exception{
		
		url=url+getQueryString(req);
		
		return getControlPane(url);
	}
	
	/**
	 * 获取数字式的页面控制跳转面板
	 * @param url 要跳转到的页面
	 * @return 数字控制面板的HTML代码
	 */
	public String getDigitalPane(String url) throws Exception{
		if(StringUtils.isNull(url))
			return "";
		
		//如果未对页面参数进行初始化，或者没有任何数据，则不输出任何HTML代码
		if((this.curPage==0)&&(this.pageCount==0)&&(this.pageSize==0)&&(this.rowCount==0))
			return "";
			
		if(this.pageCount==0)
			return "";
		
		//合理化参数
		if(this.curPage>this.pageCount)
			this.curPage=this.pageCount;
		else if(this.curPage<=0)
			this.curPage=1;
			
		//获取参数连字符
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
	 * 获取数字式的页面控制跳转面板
	 * @param req 含查询参数的请求对象
	 * @param url 要跳转到的页面
	 * @return 数字控制面板的HTML代码
	 */
	public String getDigitalPane(HttpServletRequest req, String url) throws Exception{
		
		url=url+getQueryString(req);
		
		return getDigitalPane(url);
	}
	
	/**
	 * 获取请求对象中的查询字段
	 * @param req 请求对象
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
