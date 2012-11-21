package com.zjiao.util;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.c3map.mail.MailInfo;
import com.zjiao.Logger;
import com.zjiao.SysUtils;
import java.sql.Timestamp;

/**
 * 字符串工具集合
 * @author Lee Bin
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{

	/**
	 * 如果系统中存在旧版本的数据，则此值不能修改，否则在进行密码解析的时候出错
	 */
	public final static int MAX_WIDTH = 440; 
	public final static int MAX_HEIGHT = 450;
  private static final String PASSWORD_CRYPT_KEY = "__zJiAo_DeS_kEy__";
	private final static String DES = "DES";
	
	/**
	 * 判断字符串的各个字母是否在限定的范围
	 * @param sV 需要判断的字符串
	 * @param sR 范围字符串
	 * @return boolean 是否符合要求
	 */
	public static boolean isRange(String sV,String sR){
		String sTmp;
		if(sV.length()==0){ return (false);}
		for (int i=0; i < sV.length(); i++){
			sTmp= sV.substring (i, i+1);
			if (sR.indexOf(sTmp, 0)==-1) {return (false);}
		}
		return (true);
	}

	/**
	 * 检查用户名是否合法
	 * @param uid 用户ID
	 * @return boolean 是否合法
	 */
	public static boolean checkUserName(String uid){
		if (uid==null){
			return false;
		}
		if (uid.length() == 0) {
			return false;
		}
		if(uid.length()<4||uid.length()>16) {
			return false;
		}
		if (isRange(uid.substring(0,1),"0123456789") ){
			return false;
		}
		if (isRange(uid," ,./<>?[]\\{}|`~!@#$%^&*()-=+") ){
			return false;		
		}
	
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * @param email
	 * @return boolean
	 */
	public static boolean isEmail(String email){
		if(email==null)
			return false;
		email = email.trim();
		if(email.indexOf(' ')!=-1)
			return false;
		
		int idx = email.indexOf('@');
		if(idx==0 || (idx+1)==email.length())
			return false;
		if(email.indexOf('@', idx+1)!=-1)
			return false;
		return true;
		/*
		Pattern emailer;
		if(emailer==null){
			String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			emailer = Pattern.compile(check);
		}
	    Matcher matcher = emailer.matcher(email);
	    return matcher.matches();
		*/
	}
	
	/**
	 * 加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return	  返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		
		/** DES算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		
		/** 从原始密匙数据创建DESKeySpec对象 */
		DESKeySpec dks = new DESKeySpec(key);
		
		/** 创建一个密匙工厂，然后用它把DESKeySpec转换成 */
		/** 一个SecretKey对象 */
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		
		/** Cipher对象实际完成加密操作 */
		Cipher cipher = Cipher.getInstance(DES);
		
		/** 用密匙初始化Cipher对象 */
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		
		/** 现在，获取数据并加密 */
		/** 正式执行加密操作 */
		return cipher.doFinal(src);
	}
	
	/**
	 * 解密
	 * @param src	数据源
	 * @param key	密钥，长度必须是8的倍数
	 * @return	返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		
		/** DES算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		
		/** 从原始密匙数据创建一个DESKeySpec对象 */
		DESKeySpec dks = new DESKeySpec(key);
		
		/** 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 */
		/** 一个SecretKey对象 */
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		
		/** Cipher对象实际完成解密操作 */
		Cipher cipher = Cipher.getInstance(DES);
		
		/** 用密匙初始化Cipher对象 */
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		
		/** 现在，获取数据并解密 */
		/** 正式执行解密操作 */
		return cipher.doFinal(src);
	}
	
    /**
     * 密码解密
     * @param data
     * @return String
     * @throws Exception
     */
    public final static String decrypt(String data){
        try {
            return new String(decrypt(hex2byte(data.getBytes()),PASSWORD_CRYPT_KEY.getBytes()));
        }catch(Exception e) {
        }
        return null;
    }
    
    /**
     * 密码加密
     * @param password
     * @return String
     * @throws Exception
     */
	public final static String encrypt(String password){
        try {
            return byte2hex(encrypt(password.getBytes(),PASSWORD_CRYPT_KEY.getBytes()));
        }catch(Exception e) {
        }
        return null;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
    
    public static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }
    
    /**
     * 大小写无关的字符串替换策略
     * @param str
     * @param src
     * @param obj
     * @return String
     */
    public static String replaceIgnoreCase(String str, String src, String obj){
    	String l_str = str.toLowerCase();
    	String l_src = src.toLowerCase();
    	int fromIdx = 0;
    	StringBuffer result = new StringBuffer();
    	do{
    		int idx = l_str.indexOf(l_src, fromIdx);
    		if(idx==-1)
    			break;
    		result.append(str.substring(fromIdx, idx));
    		result.append(obj);
    		fromIdx = idx + src.length();
    	}while(true);
    	result.append(str.substring(fromIdx));
    	return result.toString();
    }
    
	/**
	 * 判断字符串是否为空
	 * @param str 
	 * @return boolean
	 */
	public static boolean isNull(String str){
		if((str==null)||(str.trim().equals("")))
			return true;
		else
			return false;
	}
	
	/**
	 * 输出字符串，即将null输出为""
	 * @param str 原有字符串
	 * @return 输出的字符串
	 */
	public static String exportString(String str){
		if(str==null)
			return "";
		
		return str;
	}
	
	/**
	 * 输出字符串，如果是null或""，输出默认值
	 * @param str 原字符串
	 * @param def 默认值
	 * @return 输出的字符串
	 */
	public static String exportString(String str, String def){
		if(isNull(str))
			return exportString(def);
		else
			return str;
	}
	
	/**
	 * 输出字符串的HTML格式，即空格输出为&nbsp，回车为<br>，并将null输出为""
	 * @param str 原有字符串
	 * @return 输出的字符串
	 */
	public static String exportHTMLString(String str){
		if(str==null)
			return "";
			
		str=str.replaceAll(" ", "&nbsp");
		str=str.replaceAll("\n", "<br>");
		
		return str;
	}
	
	/**
	 * 输出字符串，限定最大长度
	 * @param str 原字符串
	 * @param len 输出字符串最大长度
	 * @return 输出的字符串
	 */
	public static String exportString(String str, int len){
		
		if(isNull(str)||(len<2))
			return "";
		else
		if(str.length()>len){
			return str.substring(0,18)+"...";
		}
		
		return str;
	}
	
	/**
	 * 输出邮件状态
	 * @param status 邮件状态
	 * @return 输出的字符串
	 */
	public static String exportMailStatus(int status){
		
		if(status==SysUtils.MAIL_STATUS_NEW){
			return "<img src=\"/images/mailnew.gif\" width=\"15\" height=\"10\">";
		}else if(status==SysUtils.MAIL_STATUS_REPLIED){
			return "<img src=\"/images/mailreplied.gif\" width=\"11\" height=\"11\">";
		}
		
		return "&nbsp";
	}
	
	/**
	 * 转换字符串
	 * @param sc 源字符串
	 * @param match 匹配的字符串
	 * @param out 要输出的字符串
	 * @return 要输出的字符串
	 */
	public static String convertString(String sc, String match, String out){
		if((sc==null)&&(match==null))
			return exportString(out);
		
		if((sc!=null)&&sc.equals(match))
			return exportString(out);
		
		return "";
	}
	
	/**
	 * 转换字符串
	 * @param sc 源数字
	 * @param match 匹配数字
	 * @param out 要输出的字符串
	 * @return 要输出的字符串
	 */
	public static String convertString(int sc, int match, String out){

		if(sc==match)
			return exportString(out);
		
		return "";
	}
	
	/**
	 * 转换字符串
	 * @param sc 源字符串
	 * @param map 转换信息表
	 * @return 转换完成的字符串
	 */
	public static String convertString(String sc, Hashtable map){
		if(isNull(sc)||(map==null)){
			return "";
		}
		
		Object tmp=map.get(sc);
		if(tmp==null)
			return "";
		else
			return (String)tmp;
	}
	
	/**
	 * 转换字符串
	 * @param sc 源字符串
	 * @param map 转换信息表
	 * @param def 默认值
	 * @return 转换完成的字符串
	 */
	public static String convertString(String sc, Hashtable map, String def){
		if(isNull(sc)||(map==null)){
			return exportString(def);
		}
		
		Object tmp=map.get(sc);
		if(tmp==null)
			return exportString(def);
		else
			return (String)tmp;
	}
	
	/**
	 * 依照指定格式输出日期
	 * @param dt 日期对象
	 * @param fmt 格式，y－年；M－月；d－日；H－小时；m－分钟；s－秒
	 * @return 日期字符串
	 */
	public static String exportDate(Date dt, String fmt){
		if((dt==null)||(fmt==null))
			return "";
		
		DateFormat df = new SimpleDateFormat(fmt);
		return exportString(df.format(dt));
	}
	
	/**
	 * 依照指定格式输出日期
	 * @param dt 日期对象
	 * @param fmt 格式，y－年；M－月；d－日；H－小时；m－分钟；s－秒
	 * @return 日期字符串
	 */
	public static String exportDate(Date dt, String fmt, String def){
		if((dt==null)||(fmt==null))
			return def;
		
		DateFormat df = new SimpleDateFormat(fmt);
		return exportString(df.format(dt));
	}
	
	/**
	 * 生成链接的HTML标签
	 * @param path
	 * @param uri
	 * @return HTML标签
	 */
	public static String genLinkTag(File path, String fileName, String context, String uri){
		StringBuffer html = new StringBuffer();
		html.append("<a href='");
		html.append(uri);
		html.append("' target='_blank'>");
		String fileIcon = getFileIcon(uri);
		if(fileIcon!=null){
			html.append("<img border='0' src='");
			html.append(context);
			html.append(fileIcon);
			html.append("' align='absmiddle'/>&nbsp;");
		}
		html.append(fileName);
		html.append("</a>");
		
		return html.toString();
	}
	
	public static String getFileIcon(String uri){
		String ext = getFileExtendName(uri);
		String icon_uri = "/editor/filemanager/browser/default/images/icons/" + ext + ".gif";
		String icon_path = "";
		File f = new File(icon_path);
		return f.exists()?icon_uri.substring(1):null;
	}
	
	/**
	 * 生成图像的HTML标签
	 * @param path
	 * @param uri
	 * @return
	 */
	public static String genImageTag(File path, String uri){
		//计算图像的大小
		int width = 0, height=0;
		try{
			BufferedImage bi = ImageIO.read(path);
			width = bi.getWidth();
			height = bi.getHeight();
		}catch(Exception e){
		}
		StringBuffer imgstr = new StringBuffer(128);
		imgstr.append("<img src='");
		imgstr.append(uri);
		imgstr.append("' border='0'");
		boolean sizeok = false;
		if(width > MAX_WIDTH ) {
			imgstr.append(" width='");
			imgstr.append(MAX_WIDTH);
			imgstr.append("'");
			int nHeight = (MAX_WIDTH * height)/ width;
			imgstr.append(" height='");
			imgstr.append(nHeight);
			imgstr.append("'");
			sizeok = true;
		}
		if(!sizeok && (height > MAX_HEIGHT)) {
			imgstr.append(" height='");
			imgstr.append(MAX_HEIGHT);
			imgstr.append("'");
			int nWidth = (MAX_HEIGHT * width) / height;
			imgstr.append(" width='");
			imgstr.append(nWidth);
			imgstr.append("'");
		}
		imgstr.append("/>");
		return imgstr.toString();
	}
	
	/**
	 * 得到一个唯一的文件名
	 * @param extName
	 * @return
	 */
	public static String getUniqueFileName(String uploadPath, String extName) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSSS.");
		String fn = null;
		do {
			fn = sdf.format(new Date()) + extName;
			if (new File(uploadPath + File.separator + fn).exists())
				continue;
			break;
		} while (true);
		return fn;
	}
	
	/**
	 * 获取文件的扩展名
	 * @param file
	 * @return
	 */
	public static String getFileExtendName(String file) {
		int idx = file.lastIndexOf('.');
		return (idx == -1 || idx == (file.length() - 1)) ? "" : file.substring(idx + 1).toLowerCase();
	}
	
	/**
	 * 处理字符串中的"字符，使在javascript中能正常处理该字符串
	 * @param src 源字符串
	 * @return 处理后的字符串
	 */
	public static String replaceDouble(String src) throws Exception{
		if(src==null){
			return null;
		}
		char temp;
		String rp="\\\"";
		StringBuffer out=new StringBuffer();
		for(int i=0;i<src.length();i++){
			temp=src.charAt(i);
			if(temp=='"'){
				out.append(rp);
			}else{
				out.append(temp);
			}
		}
		return out.toString();
	}
	
	/**
	 * 编码请求参数
	 * @param src 源字符串
	 * @return 处理后的字符串
	 */
	public static String URLEncode(String src) {
		if(src==null){
			return "";
		}
		try{
			return URLEncoder.encode(src, SysUtils.DEFAULT_CHARSET);
		}catch(Exception e){
			Logger.Log(Logger.LOG_ERROR, e);
			return "";
		}
	}
	
	/**
	 * 获取请求对象中的查询字段
	 * @param req 请求对象
	 * @return 查询字符串
	 */
	public static String getQueryString(HttpServletRequest req) throws Exception{
		Enumeration pnames=req.getParameterNames();
		StringBuffer query=new StringBuffer("");
		String tmpname;
		String tmpvalue;
		int i=0;
		while(pnames.hasMoreElements()){
			tmpname=(String)pnames.nextElement();
			if(tmpname!=null){
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
	
	/**
	 * 获取请求对象中的查询字段
	 * @param req 请求对象
	 * @param clear 要清除的参数，多个参数使用&隔开
	 * @return 查询字符串
	 */
	public static String getQueryString(HttpServletRequest req, String clear)throws Exception{
		Enumeration pnames=req.getParameterNames();
		StringBuffer query=new StringBuffer("");
		String tmpname;
		String tmpvalue;
		int i=0;
		while(pnames.hasMoreElements()){
			tmpname=(String)pnames.nextElement();
			if((tmpname!=null)&&(clear.indexOf(tmpname)==-1)){
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
	
	/**
	 * 获取显示照片的HTML代码
	 * @param wid 限定显示的宽度值
	 * @param img 含有照片信息的字符串
	 * @return 显示照片的HTML代码
	 */
	public static String getPhotoTag(int wid, String img){
		if(wid<1)
			return "";
		
		String src=null;
		int width=wid;
		int height=0;
		
		if(isNull(img)){
			src="/images/nophoto.jpg";
			height=wid;
		}else{
			String[] params=img.split(",");
			if(params.length==3){
				int rwid=0, rhei=0;
				//获取照片实际尺寸
				try{
					rwid=Integer.parseInt(params[1]);
					rhei=Integer.parseInt(params[2]);
				}catch(Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
				}
				
				if((rhei>0)&&(rwid>0)){
					height=rhei*width/rwid;
				}else{
					height=width;
				}
			}else{
				height=width;
			}
			
			src=params[0];
		}
		
		//生成并输出HTML标签
		StringBuffer photo=new StringBuffer("<img src=\"");
		photo.append(src);
		photo.append("\" width=\"");
		photo.append(width);
		photo.append("\" height=\"");
		photo.append(height);
		photo.append("\" border=\"0\">");
		
		return photo.toString();
	}
	
	/**
	 * 获取显示照片的HTML代码
	 * @param wid 限定显示的宽度值
	 * @param hei 限定显示的高度值
	 * @param img 含有照片信息的字符串
	 * @return 显示照片的HTML代码
	 */
	public static String getPhotoTag(int wid, int hei, String img){
		if(hei<1)
			return getPhotoTag(wid, img);
			
		if(wid<1)
			return "";
		
		String src=null;
		int width=0;
		int height=0;
		int refer=0;
		
		if(wid>hei){
			refer=hei;
		}else{
			refer=wid;
		}
		
		if(isNull(img)){
			src="/images/nophoto.jpg";
			width=refer;
			height=refer;
		}else{
			String[] params=img.split(",");
			if(params.length==3){
				int rwid=0, rhei=0;
				//获取照片实际尺寸
				try{
					rwid=Integer.parseInt(params[1]);
					rhei=Integer.parseInt(params[2]);
				}catch(Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
				}
				
				if((rhei>0)&&(rwid>0)){
					width=wid;
					height=rhei*width/rwid;
					if(height>hei){
						height=hei;
						width=rwid*height/rhei;
					}
				}else{
					width=refer;
					height=refer;
				}
			}else{
				width=refer;
				height=refer;
			}
			
			src=params[0];
		}
		
		//生成并输出HTML标签
		StringBuffer photo=new StringBuffer("<img src=\"");
		photo.append(src);
		photo.append("\" width=\"");
		photo.append(width);
		photo.append("\" height=\"");
		photo.append(height);
		photo.append("\" border=\"0\">");
		
		return photo.toString();
	}
	
	/**
	 * 处理信件回复内容
	 * @param minfo 信件信息对象
	 * @return 处理后的回复内容
	 */
	public static String getReplyMail(MailInfo minfo){
		
		StringBuffer out=new StringBuffer("\n\n-----------------------------------------------------\n【");
		out.append(minfo.getSenderName());
		out.append("在来信中提到】\n");
		
		minfo.setContent(minfo.getContent().replaceAll("&nbsp", " "));
		minfo.setContent(minfo.getContent().replaceAll("<br>", "\n>"));
		
		out.append(minfo.getContent());
		
		return out.toString();
	}
	


}