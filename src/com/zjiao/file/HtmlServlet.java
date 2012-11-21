package com.zjiao.file;

import javax.servlet.*;
import javax.servlet.http.*;

import com.zjiao.Logger;

import java.io.*;

public class HtmlServlet extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html; charset=GBK";

  //初始化全局变量
  public void init() throws ServletException {
  }

  //处理 HTTP Post 请求
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletOutputStream out = response.getOutputStream ();
    
    //获取要输出的HTML文件
    File htmlFile=FileUtils.getHtmlFile(request.getRequestURI());

    //设置输出的MIME类型
    response.setContentType("CONTENT_TYPE");
    if(htmlFile==null){
    	response.sendError(HttpServletResponse.SC_NOT_FOUND);
    	return;
    }

    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
		FileInputStream in = null;

    try {
      //使用 Buffered Stream 输入输出
      in=new FileInputStream(htmlFile);
      bis = new BufferedInputStream(in);
      bos = new BufferedOutputStream(out);

      byte[] buff = new byte[4096];
      int bytesRead;

      //简单的读写循环
      while ( -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
        bos.write(buff, 0, bytesRead);
      }

    } catch(Exception e) {
      Logger.Log(Logger.LOG_ERROR, e);
    } finally {
      if (bis != null)
        bis.close();
      if (bos != null)
        bos.close();
      if(in != null)
      	in.close();
    }
}

  //释放资源
  public void destroy() {
  }
}