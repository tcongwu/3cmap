package com.zjiao.struts;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * 请求对象的封装
 * @author Lee Bin
 */
public class RequestProxy extends HttpServletRequestWrapper{
	
	final static String ENC_8859_1 = "8859_1";
	final static String ENC_UTF_8 = "GBK";
	
	protected String encode;

	public RequestProxy(HttpServletRequest request){
		this(request, ENC_UTF_8);
	}
	
	public RequestProxy(HttpServletRequest request, String encoding){
		super(request);
		this.encode = (encoding==null)?ENC_UTF_8:encoding;
	}
	
	public String getParameter(String arg0) {
		String value = super.getParameter(arg0);
		if(value!=null)
			try {
				return new String(value.getBytes(ENC_8859_1),encode);
			}catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return null;
	}
	public Map getParameterMap() {
		Map params = super.getParameterMap();
		HashMap new_params = new HashMap();
		Iterator iter = params.keySet().iterator();
		while(iter.hasNext()){
			String key = (String)iter.next();
			Object oValue = params.get(key);
			if(oValue.getClass().isArray()){
				String[] values = (String[])params.get(key);
				for(int i=0;i<values.length;i++)
					try {
						values[i] = new String(values[i].getBytes(ENC_8859_1),encode);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						break;
					}
				new_params.put(key, values);
			}
			else{
				String value = (String)params.get(key);
				String new_value = null;;
				try {
					new_value = (value!=null)?
							new String(value.getBytes(ENC_8859_1),encode):null;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					break;
				}
				if(new_value!=null)
					new_params.put(key,new_value);
			}
		}
		return new_params;
	}
	public Enumeration getParameterNames() {
		return super.getParameterNames();
	}
	public String[] getParameterValues(String arg0) {
		String[] values = super.getParameterValues(arg0);
		for(int i=0;values!=null&&i<values.length;i++){
			try {
				values[i] = new String(values[i].getBytes(ENC_8859_1),encode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				break;
			}
		}
		return values;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
}