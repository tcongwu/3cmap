package com.zjiao.file;

import javax.servlet.*;
import javax.servlet.http.*;

import com.zjiao.Logger;

import java.io.*;

public class HtmlServlet extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html; charset=GBK";

  //��ʼ��ȫ�ֱ���
  public void init() throws ServletException {
  }

  //���� HTTP Post ����
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletOutputStream out = response.getOutputStream ();
    
    //��ȡҪ�����HTML�ļ�
    File htmlFile=FileUtils.getHtmlFile(request.getRequestURI());

    //���������MIME����
    response.setContentType("CONTENT_TYPE");
    if(htmlFile==null){
    	response.sendError(HttpServletResponse.SC_NOT_FOUND);
    	return;
    }

    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
		FileInputStream in = null;

    try {
      //ʹ�� Buffered Stream �������
      in=new FileInputStream(htmlFile);
      bis = new BufferedInputStream(in);
      bos = new BufferedOutputStream(out);

      byte[] buff = new byte[4096];
      int bytesRead;

      //�򵥵Ķ�дѭ��
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

  //�ͷ���Դ
  public void destroy() {
  }
}