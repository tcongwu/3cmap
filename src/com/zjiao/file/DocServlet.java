package com.zjiao.file;

import javax.servlet.*;
import javax.servlet.http.*;

import com.zjiao.Logger;

import java.io.*;

public class DocServlet extends HttpServlet {

  //��ʼ��ȫ�ֱ���
  public void init() throws ServletException {
  }

  //���� HTTP Post ����
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletOutputStream out = response.getOutputStream ();
    
    //��ȡҪ�����HTML�ļ�
    File docFile=FileUtils.getDocFile(request.getRequestURI());

		//���������MIME����
		//response.setContentType("application/doc");
		// Content-disposition header - don't open in browser and
		// set the "Save As..." filename.
		// *There is reportedly a bug in IE4.0 which ignores this...
		//response.setHeader("Content-disposition","attachment; filename=" + gKey + ".rls");

    //���������MIME����
    response.setContentType("application/doc");
    if(docFile==null){
    	response.sendError(HttpServletResponse.SC_NOT_FOUND);
    	return;
    }

    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
		FileInputStream in = null;

    try {
      //ʹ�� Buffered Stream �������
      in=new FileInputStream(docFile);
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