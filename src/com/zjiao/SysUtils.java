package com.zjiao;

import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.*;

public class SysUtils {
  public static Hashtable SUBJECT = null;
  public static Hashtable CLASSIFY = null;
  public static Hashtable PROVINCE = null;

  public static int PAGE_SIZE = 10; //每小页的数据行数
  public static int MPAGE_SIZE = 20; //每中页的数据行数
  public static int LPAGE_SIZE = 30; //每大页的数据行数

  public final static String SEX_MALE = "男";	//男
  public final static String SEX_FEMALE = "女";	//女

  public final static int USER_TYPE_JUNIOR = 1;	//初中
  public final static int USER_TYPE_SENIOR = 2;	//高中
  public final static int USER_TYPE_UNIV = 3;	//大学

  public final static int SCHOOL_JUNIOR = 1;	//初中
  public final static int SCHOOL_SENIOR = 2;	//高中
  public final static int SCHOOL_MIDDLE = 3;	//初中和高中
  public final static int SCHOOL_UNIV = 4;	//大学

  public final static int GUILD_TYPE_JUNIOR = 1;	//初中
  public final static int GUILD_TYPE_SENIOR = 2;	//高中
  public final static int GUILD_TYPE_UNIV = 3;	//大学
  public final static int GUILD_TYPE_MIDDLE = 4;	//中学
  public final static int GUILD_TYPE_ALL = 5;	//所有

  public final static int USER_ROLE_NORMAL = 1;	//普通注册者
  public final static int USER_ROLE_ANGEL = 2;	//校园大使
  public final static int USER_ROLE_ADMIN = 3;	//管理员

  public final static int USER_STATUS_INACTIVE = 1;	//未激活
  public final static int USER_STATUS_APPLICATION = 2;	//申请待审批
  public final static int USER_STATUS_REJECT = 3;	//审批未通过
  public final static int USER_STATUS_INVALID = 4;	//无效，禁止
  public final static int USER_STATUS_NORMAL =5;	//正常

  public final static int OPEN_TYPE_OWN = 1;	//只有自己
  public final static int OPEN_TYPE_FRIEND = 2;	//好友
  public final static int OPEN_TYPE_2FRIEND = 3;	//2度好友
  public final static int OPEN_TYPE_ALL = 4;	//所有人

  public final static int BOOLEAN_YES = 1;	//是
  public final static int BOOLEAN_NO = 2;	//否

  public final static int MAIL_STATUS_NEW = 1;	//新邮件
  public final static int MAIL_STATUS_READ = 2;	//已读
  public final static int MAIL_STATUS_REPLIED = 3;	//已回复

  public final static int SCORE_BROWSED = 2;	//别人的点击查看加2分
  public final static int SCORE_WRITE_BLOG = 5;	//每一个人的日志加5分
  public final static int SCORE_REFINED_BLOG = 20;	//日志被收入精华区加20分
  public final static int SCORE_BOARD = 2;	//每一个人的留言加2分（双向加分）
  public final static int SCORE_CREATE_BBS = 500;	//创建讨论区一次扣500分
  public final static int SCORE_HOT_BBS = 100;	//当创建的讨论区成为热点讨论区一次加100分
  public final static int SCORE_JION_DISCUSS = 1;	//参加一次话题讨论加1分（双向加分）
  public final static int SCORE_BEGIN_ACTIVITY = 100;	//发起一次活动扣100分
  public final static int SCORE_JOIN_ACTIVITY = 10;	//参加一次活动加10分（双向加分）
  public final static int SCORE_RECOMMEND = 2;	//推荐给朋友一次加2分
  public final static int SCORE_ACTIVITY_MES = 2;	//给一个活动留言加2分
  
  
 
  
  public final static int ACTIVE_CODE_LENGTH = 28;	//激活码的字符数

  public final static String ERR_CANT_ACCESS = "没有权限";

  public static final String DEFAULT_CHARSET = "GBK";
  public static final String DEFAULT_LANGUAGE = "gb";
  
  public SysUtils() {
  }

  /**
   * 生成带Language和sessionid的url
   * @param request 当前的request请求
   * @param url 需要重定向的URL
   * @param sessionid 当前sessionid
   * @return 返回正确的URL
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

      //这样的URL不在前面加上/en/之类的
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
   * 生成带Language和sessionid的url,sessionid从request中获得
   * @param request 当前的request请求
   * @param url 需要重定向的URL
   * @return 返回正确的URL
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
   * 生成带Language的漫游入口
   * @param request 当前的request请求
   * @param url 需要重定向的URL
   * @param tomodule 定向到的应用
   * @return 返回正确的URL
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
   * 获得当前的jsp语言
   * @param request 当前的request请求
   * @return 两个字母的语言id
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
   * 获得当前的servlet语言
   * @param request 当前的request请求
   * @return 两个字母的语言id
   */
  static public String getServletLang(HttpServletRequest request) {
    String lang=request.getParameter("lang");
    if (lang!=null)
      return lang;
    return "";
  }

  /**
   * 获得语言对应的Charset
   * @param lang 语言代码
   * @return 对应的Charset
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
   * 获得当前jsp的encoding
   * @param request 当前的request请求
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
   * 获得当前Servlet的encoding
   * @param request 当前的request请求
   * @return charset string
   */
  static public String getServletEncoding(HttpServletRequest request) {
    return getCharset(getServletLang(request));
  }

  /**判断字符串是否为空
   * 注意，"null"也会被当作空
   * @param buf 需要判断的字符串
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

  /** 判断buf是否包含buf1(不区分大小写）
   * @param buf 查询的字符串
   * @param buf1 搜索的字符串
   * @return 如果包含，返回true
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
   * Server重新定向到error页面
   * @param servlet servlet reference
   * @param request http request
   * @param response http response
   * @param exception 需要抛出的异常
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