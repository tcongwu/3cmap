package com.zjiao;

import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.*;

public class SysUtils {
  public static Hashtable SUBJECT = null;
  public static Hashtable CLASSIFY = null;
  public static Hashtable PROVINCE = null;

  public static int PAGE_SIZE = 10; //ÿСҳ����������
  public static int MPAGE_SIZE = 20; //ÿ��ҳ����������
  public static int LPAGE_SIZE = 30; //ÿ��ҳ����������

  public final static String SEX_MALE = "��";	//��
  public final static String SEX_FEMALE = "Ů";	//Ů

  public final static int USER_TYPE_JUNIOR = 1;	//����
  public final static int USER_TYPE_SENIOR = 2;	//����
  public final static int USER_TYPE_UNIV = 3;	//��ѧ

  public final static int SCHOOL_JUNIOR = 1;	//����
  public final static int SCHOOL_SENIOR = 2;	//����
  public final static int SCHOOL_MIDDLE = 3;	//���к͸���
  public final static int SCHOOL_UNIV = 4;	//��ѧ

  public final static int GUILD_TYPE_JUNIOR = 1;	//����
  public final static int GUILD_TYPE_SENIOR = 2;	//����
  public final static int GUILD_TYPE_UNIV = 3;	//��ѧ
  public final static int GUILD_TYPE_MIDDLE = 4;	//��ѧ
  public final static int GUILD_TYPE_ALL = 5;	//����

  public final static int USER_ROLE_NORMAL = 1;	//��ͨע����
  public final static int USER_ROLE_ANGEL = 2;	//У԰��ʹ
  public final static int USER_ROLE_ADMIN = 3;	//����Ա

  public final static int USER_STATUS_INACTIVE = 1;	//δ����
  public final static int USER_STATUS_APPLICATION = 2;	//���������
  public final static int USER_STATUS_REJECT = 3;	//����δͨ��
  public final static int USER_STATUS_INVALID = 4;	//��Ч����ֹ
  public final static int USER_STATUS_NORMAL =5;	//����

  public final static int OPEN_TYPE_OWN = 1;	//ֻ���Լ�
  public final static int OPEN_TYPE_FRIEND = 2;	//����
  public final static int OPEN_TYPE_2FRIEND = 3;	//2�Ⱥ���
  public final static int OPEN_TYPE_ALL = 4;	//������

  public final static int BOOLEAN_YES = 1;	//��
  public final static int BOOLEAN_NO = 2;	//��

  public final static int MAIL_STATUS_NEW = 1;	//���ʼ�
  public final static int MAIL_STATUS_READ = 2;	//�Ѷ�
  public final static int MAIL_STATUS_REPLIED = 3;	//�ѻظ�

  public final static int SCORE_BROWSED = 2;	//���˵ĵ���鿴��2��
  public final static int SCORE_WRITE_BLOG = 5;	//ÿһ���˵���־��5��
  public final static int SCORE_REFINED_BLOG = 20;	//��־�����뾫������20��
  public final static int SCORE_BOARD = 2;	//ÿһ���˵����Լ�2�֣�˫��ӷ֣�
  public final static int SCORE_CREATE_BBS = 500;	//����������һ�ο�500��
  public final static int SCORE_HOT_BBS = 100;	//����������������Ϊ�ȵ�������һ�μ�100��
  public final static int SCORE_JION_DISCUSS = 1;	//�μ�һ�λ������ۼ�1�֣�˫��ӷ֣�
  public final static int SCORE_BEGIN_ACTIVITY = 100;	//����һ�λ��100��
  public final static int SCORE_JOIN_ACTIVITY = 10;	//�μ�һ�λ��10�֣�˫��ӷ֣�
  public final static int SCORE_RECOMMEND = 2;	//�Ƽ�������һ�μ�2��
  public final static int SCORE_ACTIVITY_MES = 2;	//��һ������Լ�2��
  
  
 
  
  public final static int ACTIVE_CODE_LENGTH = 28;	//��������ַ���

  public final static String ERR_CANT_ACCESS = "û��Ȩ��";

  public static final String DEFAULT_CHARSET = "GBK";
  public static final String DEFAULT_LANGUAGE = "gb";
  
  public SysUtils() {
  }

  /**
   * ���ɴ�Language��sessionid��url
   * @param request ��ǰ��request����
   * @param url ��Ҫ�ض����URL
   * @param sessionid ��ǰsessionid
   * @return ������ȷ��URL
   */
  static public String generateLink(HttpServletRequest request, String url,
                                    String sessionid) {
    java.net.URL urlobj;
    String newurl, path, queryurl;
    String app_lang = request.getParameter("lang");
    String app_cid = request.getParameter("cid");
    String sep;
    boolean absolute = false;
    if (url.length()<3) return url;
    if (url.charAt(0) == '/') {
      absolute = true;
    }
    if ( (app_lang == null) || (app_lang.equals(""))) {
      queryurl = "";
    }
    else {
      queryurl = "lang=" + app_lang;
    }
    if ( !(app_cid == null) && !(app_cid.equals(""))) {
      if (queryurl.equals(""))
        queryurl = "cid=" + app_cid;
      else
        queryurl = queryurl + "&cid=" + app_cid;
    }
    if (! (sessionid == null) && ! (sessionid.equals(""))) {
      if (queryurl.equals("")) {
        queryurl = "sid=" + sessionid;
      }
      else {
        queryurl = queryurl + "&sid=" + sessionid;
      }
    }
    if ( (url.charAt(2) == '/') || ( (url.startsWith("/servlet")))) {

      //������URL����ǰ�����/en/֮���
      app_lang = null;
    }
    try {
      urlobj = new java.net.URL(url);
      String query=urlobj.getQuery();
      if (query==null)
        query="";
      if (query.equals("")) {
        sep = "?";
      }
      else {
        sep = "&";
      }
      newurl = "";
      String protocol=urlobj.getProtocol();
      if (protocol==null)
        protocol="";
      if (!protocol.equals("")) {
        newurl = urlobj.getProtocol() + "://";
      }
      String host=urlobj.getHost();
      if (host==null)
        host="";
      if (!host.equals("")) {
        newurl = newurl + urlobj.getHost();
      }
      if (urlobj.getPort() != -1) {
        newurl = newurl + ":" + urlobj.getPort();
      }
      path=urlobj.getPath();
      if (path==null) path="";
      if (!query.equals(""))
        path=path+"?"+query;
    }
    catch (java.net.MalformedURLException ex) {
      if (url.lastIndexOf("?") == -1) {
        sep = "?";
      }
      else {
        sep = "&";
      }
      newurl = "";
      path = url;
    }
    if ( (absolute) && (app_lang != null) && !app_lang.equals("")) {
      newurl = newurl + "/" + app_lang;
    }
    if (queryurl.equals("")) {
      return newurl + path;
    }
    return newurl + path + sep + queryurl;
  }

  /**
   * ���ɴ�Language��sessionid��url,sessionid��request�л��
   * @param request ��ǰ��request����
   * @param url ��Ҫ�ض����URL
   * @return ������ȷ��URL
   */
  static public String generateLink(HttpServletRequest request, String url) {
    String sid = request.getParameter("sid");
    if ( sid == null) {
      sid = (String)request.getAttribute("sid");
      if (sid == null)
        sid = "";
    }
    return generateLink(request, url, sid);
  }

  /**
   * ���ɴ�Language���������
   * @param request ��ǰ��request����
   * @param url ��Ҫ�ض����URL
   * @param tomodule ���򵽵�Ӧ��
   * @return ������ȷ��URL
   */
  static public String generateCruise(HttpServletRequest request, String url, String tomodule) {
    String from=(String)request.getAttribute("etmodule");
    if (from==null)
      from="et";
    return generateLink(request, "/servlet/cruise?from="+from.toUpperCase()+"&desturl=" +
                        java.net.URLEncoder.encode(url)+"&module="+tomodule);
/*    try {
      return generateLink(request, "/servlet/cruise?desturl=" +
                          java.net.URLEncoder.encode(url, "UTF-8"));
    } catch (java.io.UnsupportedEncodingException ex) {
      return generateLink(request, "/servlet/cruise?desturl=" +url);
    }
 */
  }

  /***
   * ��õ�ǰ��jsp����
   * @param request ��ǰ��request����
   * @return ������ĸ������id
   */
  static public String getjspLang(HttpServletRequest request) {
    String path = request.getServletPath();
    if (path == null) {
      return "";
    }
    if (path.length()>=3&&(path.charAt(0) == '/')&&(path.charAt(3) == '/')) {
      return path.substring(1, 3);
    }
    else {
      String lang=request.getParameter("lang");
      if (lang!=null)
        return lang;
      return "";
    }
  }

  /***
   * ��õ�ǰ��servlet����
   * @param request ��ǰ��request����
   * @return ������ĸ������id
   */
  static public String getServletLang(HttpServletRequest request) {
    String lang=request.getParameter("lang");
    if (lang!=null)
      return lang;
    return "";
  }

  /**
   * ������Զ�Ӧ��Charset
   * @param lang ���Դ���
   * @return ��Ӧ��Charset
   */
  static public String getCharset(String lang) {
    if (lang == null) {
      return DEFAULT_CHARSET;
    }
    /**
     * @todo make a code <--> charset table
     */
    if (lang.equals("en")) {
      return "US-ASCII";
    }
    return DEFAULT_CHARSET;
  }

  /***
   * ��õ�ǰjsp��encoding
   * @param request ��ǰ��request����
   * @return charset string
   */
  static public String getjspEncoding(HttpServletRequest request) {
    String path = request.getServletPath();
    if (path == null) {
      return DEFAULT_CHARSET;
    }
    String code = getjspLang(request);
    return getCharset(code);
  }

  /***
   * ��õ�ǰServlet��encoding
   * @param request ��ǰ��request����
   * @return charset string
   */
  static public String getServletEncoding(HttpServletRequest request) {
    return getCharset(getServletLang(request));
  }

  /**�ж��ַ����Ƿ�Ϊ��
   * ע�⣬"null"Ҳ�ᱻ������
   * @param buf ��Ҫ�жϵ��ַ���
   * @return if the buf is null return true
   */
  public static boolean isNull(String buf) {
    if (buf == null) {
      return true;
    }
    if ("".equals(buf)) {
      return true;
    }
    if ("NULL".equals(buf.toUpperCase().trim())) {
      return true;
    }

    return false;
  }

  /** �ж�buf�Ƿ����buf1(�����ִ�Сд��
   * @param buf ��ѯ���ַ���
   * @param buf1 �������ַ���
   * @return �������������true
   */
  public static boolean isInclude(String buf,String buf1)
  {
    if(isNull(buf) || isNull(buf1))
      return false;

    String temp = buf.toUpperCase();
    String temp1 = buf1.toUpperCase();
    if(temp.indexOf(temp1.trim())<0)
      return false;
    else
      return true;
  }

  /**
   * Server���¶���errorҳ��
   * @param servlet servlet reference
   * @param request http request
   * @param response http response
   * @param exception ��Ҫ�׳����쳣
   * @throws java.io.IOException
   * @throws ServletException
   */
  public static void setServletException(HttpServlet servlet,HttpServletRequest request, ServletResponse response,Exception exception)
  throws java.io.IOException,ServletException
  {
    request.setAttribute("javax.servlet.jsp.jspException",exception);
    servlet.getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
  }

  public static String URLEncoder(String str){
    return java.net.URLEncoder.encode(str);
  }
}