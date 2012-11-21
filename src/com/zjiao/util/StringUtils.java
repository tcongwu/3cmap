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
 * �ַ������߼���
 * @author Lee Bin
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{

	/**
	 * ���ϵͳ�д��ھɰ汾�����ݣ����ֵ�����޸ģ������ڽ������������ʱ�����
	 */
	public final static int MAX_WIDTH = 440; 
	public final static int MAX_HEIGHT = 450;
  private static final String PASSWORD_CRYPT_KEY = "__zJiAo_DeS_kEy__";
	private final static String DES = "DES";
	
	/**
	 * �ж��ַ����ĸ�����ĸ�Ƿ����޶��ķ�Χ
	 * @param sV ��Ҫ�жϵ��ַ���
	 * @param sR ��Χ�ַ���
	 * @return boolean �Ƿ����Ҫ��
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
	 * ����û����Ƿ�Ϸ�
	 * @param uid �û�ID
	 * @return boolean �Ƿ�Ϸ�
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
	 * �ж��ǲ���һ���Ϸ��ĵ����ʼ���ַ
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
	 * ����
	 * @param src ����Դ
	 * @param key ��Կ�����ȱ�����8�ı���
	 * @return	  ���ؼ��ܺ������
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		
		/** DES�㷨Ҫ����һ�������ε������Դ */
		SecureRandom sr = new SecureRandom();
		
		/** ��ԭʼ�ܳ����ݴ���DESKeySpec���� */
		DESKeySpec dks = new DESKeySpec(key);
		
		/** ����һ���ܳ׹�����Ȼ��������DESKeySpecת���� */
		/** һ��SecretKey���� */
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		
		/** Cipher����ʵ����ɼ��ܲ��� */
		Cipher cipher = Cipher.getInstance(DES);
		
		/** ���ܳ׳�ʼ��Cipher���� */
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		
		/** ���ڣ���ȡ���ݲ����� */
		/** ��ʽִ�м��ܲ��� */
		return cipher.doFinal(src);
	}
	
	/**
	 * ����
	 * @param src	����Դ
	 * @param key	��Կ�����ȱ�����8�ı���
	 * @return	���ؽ��ܺ��ԭʼ����
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		
		/** DES�㷨Ҫ����һ�������ε������Դ */
		SecureRandom sr = new SecureRandom();
		
		/** ��ԭʼ�ܳ����ݴ���һ��DESKeySpec���� */
		DESKeySpec dks = new DESKeySpec(key);
		
		/** ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת���� */
		/** һ��SecretKey���� */
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		
		/** Cipher����ʵ����ɽ��ܲ��� */
		Cipher cipher = Cipher.getInstance(DES);
		
		/** ���ܳ׳�ʼ��Cipher���� */
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		
		/** ���ڣ���ȡ���ݲ����� */
		/** ��ʽִ�н��ܲ��� */
		return cipher.doFinal(src);
	}
	
    /**
     * �������
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
     * �������
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
            throw new IllegalArgumentException("���Ȳ���ż��");
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }
    
    /**
     * ��Сд�޹ص��ַ����滻����
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
	 * �ж��ַ����Ƿ�Ϊ��
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
	 * ����ַ���������null���Ϊ""
	 * @param str ԭ���ַ���
	 * @return ������ַ���
	 */
	public static String exportString(String str){
		if(str==null)
			return "";
		
		return str;
	}
	
	/**
	 * ����ַ����������null��""�����Ĭ��ֵ
	 * @param str ԭ�ַ���
	 * @param def Ĭ��ֵ
	 * @return ������ַ���
	 */
	public static String exportString(String str, String def){
		if(isNull(str))
			return exportString(def);
		else
			return str;
	}
	
	/**
	 * ����ַ�����HTML��ʽ�����ո����Ϊ&nbsp���س�Ϊ<br>������null���Ϊ""
	 * @param str ԭ���ַ���
	 * @return ������ַ���
	 */
	public static String exportHTMLString(String str){
		if(str==null)
			return "";
			
		str=str.replaceAll(" ", "&nbsp");
		str=str.replaceAll("\n", "<br>");
		
		return str;
	}
	
	/**
	 * ����ַ������޶���󳤶�
	 * @param str ԭ�ַ���
	 * @param len ����ַ�����󳤶�
	 * @return ������ַ���
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
	 * ����ʼ�״̬
	 * @param status �ʼ�״̬
	 * @return ������ַ���
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
	 * ת���ַ���
	 * @param sc Դ�ַ���
	 * @param match ƥ����ַ���
	 * @param out Ҫ������ַ���
	 * @return Ҫ������ַ���
	 */
	public static String convertString(String sc, String match, String out){
		if((sc==null)&&(match==null))
			return exportString(out);
		
		if((sc!=null)&&sc.equals(match))
			return exportString(out);
		
		return "";
	}
	
	/**
	 * ת���ַ���
	 * @param sc Դ����
	 * @param match ƥ������
	 * @param out Ҫ������ַ���
	 * @return Ҫ������ַ���
	 */
	public static String convertString(int sc, int match, String out){

		if(sc==match)
			return exportString(out);
		
		return "";
	}
	
	/**
	 * ת���ַ���
	 * @param sc Դ�ַ���
	 * @param map ת����Ϣ��
	 * @return ת����ɵ��ַ���
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
	 * ת���ַ���
	 * @param sc Դ�ַ���
	 * @param map ת����Ϣ��
	 * @param def Ĭ��ֵ
	 * @return ת����ɵ��ַ���
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
	 * ����ָ����ʽ�������
	 * @param dt ���ڶ���
	 * @param fmt ��ʽ��y���ꣻM���£�d���գ�H��Сʱ��m�����ӣ�s����
	 * @return �����ַ���
	 */
	public static String exportDate(Date dt, String fmt){
		if((dt==null)||(fmt==null))
			return "";
		
		DateFormat df = new SimpleDateFormat(fmt);
		return exportString(df.format(dt));
	}
	
	/**
	 * ����ָ����ʽ�������
	 * @param dt ���ڶ���
	 * @param fmt ��ʽ��y���ꣻM���£�d���գ�H��Сʱ��m�����ӣ�s����
	 * @return �����ַ���
	 */
	public static String exportDate(Date dt, String fmt, String def){
		if((dt==null)||(fmt==null))
			return def;
		
		DateFormat df = new SimpleDateFormat(fmt);
		return exportString(df.format(dt));
	}
	
	/**
	 * �������ӵ�HTML��ǩ
	 * @param path
	 * @param uri
	 * @return HTML��ǩ
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
	 * ����ͼ���HTML��ǩ
	 * @param path
	 * @param uri
	 * @return
	 */
	public static String genImageTag(File path, String uri){
		//����ͼ��Ĵ�С
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
	 * �õ�һ��Ψһ���ļ���
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
	 * ��ȡ�ļ�����չ��
	 * @param file
	 * @return
	 */
	public static String getFileExtendName(String file) {
		int idx = file.lastIndexOf('.');
		return (idx == -1 || idx == (file.length() - 1)) ? "" : file.substring(idx + 1).toLowerCase();
	}
	
	/**
	 * �����ַ����е�"�ַ���ʹ��javascript��������������ַ���
	 * @param src Դ�ַ���
	 * @return �������ַ���
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
	 * �����������
	 * @param src Դ�ַ���
	 * @return �������ַ���
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
	 * ��ȡ��������еĲ�ѯ�ֶ�
	 * @param req �������
	 * @return ��ѯ�ַ���
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
	 * ��ȡ��������еĲ�ѯ�ֶ�
	 * @param req �������
	 * @param clear Ҫ����Ĳ������������ʹ��&����
	 * @return ��ѯ�ַ���
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
	 * ��ȡ��ʾ��Ƭ��HTML����
	 * @param wid �޶���ʾ�Ŀ��ֵ
	 * @param img ������Ƭ��Ϣ���ַ���
	 * @return ��ʾ��Ƭ��HTML����
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
				//��ȡ��Ƭʵ�ʳߴ�
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
		
		//���ɲ����HTML��ǩ
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
	 * ��ȡ��ʾ��Ƭ��HTML����
	 * @param wid �޶���ʾ�Ŀ��ֵ
	 * @param hei �޶���ʾ�ĸ߶�ֵ
	 * @param img ������Ƭ��Ϣ���ַ���
	 * @return ��ʾ��Ƭ��HTML����
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
				//��ȡ��Ƭʵ�ʳߴ�
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
		
		//���ɲ����HTML��ǩ
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
	 * �����ż��ظ�����
	 * @param minfo �ż���Ϣ����
	 * @return �����Ļظ�����
	 */
	public static String getReplyMail(MailInfo minfo){
		
		StringBuffer out=new StringBuffer("\n\n-----------------------------------------------------\n��");
		out.append(minfo.getSenderName());
		out.append("���������ᵽ��\n");
		
		minfo.setContent(minfo.getContent().replaceAll("&nbsp", " "));
		minfo.setContent(minfo.getContent().replaceAll("<br>", "\n>"));
		
		out.append(minfo.getContent());
		
		return out.toString();
	}
	


}