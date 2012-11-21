package com.zjiao.file;

import java.io.*;
import java.text.SimpleDateFormat;

import javax.servlet.*;
import javax.servlet.http.*;
import com.jspsmart.upload.*;
import com.zjiao.ErrorInformation;
import com.zjiao.Logger;
import com.zjiao.auth.AuthUserDAO;
import com.zjiao.auth.UserInfo;
import com.zjiao.util.StringUtils;

public class UploadPhoto extends HttpServlet {
	
	private static final String CONTENT_TYPE = "text/html; charset=GBK";
	private ServletConfig config;
	
	/**
	 * ��ʼ��
	 */
	final public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	
	final public ServletConfig getServletConfig() {
		return config;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType(CONTENT_TYPE);
		RequestDispatcher dispatcher=config.getServletContext().getRequestDispatcher("/error.jsp");
		
		//��ʼ��
		UserInfo loginUser = UserInfo.getLoginUser(request);
		ErrorInformation errInfo=new FileErrInfo();
		String photo=null;
		
		//�������
		int count=0;
		SmartUpload mySmartUpload = new SmartUpload();

		if(loginUser==null){
			errInfo.saveError(FileErrInfo.NEED_LOGIN);
		  errInfo.saveToRequest("err", request);
		  dispatcher.forward(request, response);
		}
		else{
			photo=loginUser.getPhoto();
			
			try {
				//��ʼ��
				mySmartUpload.initialize(config,request,response);
				//������Ƭ�ļ���СΪ600K
				mySmartUpload.setMaxFileSize(320000);
				//������Ƭ�ļ�����
				mySmartUpload.setAllowedFilesList("JPG,GIF,PNG,jpg,jpeg,gif,png");
				//�������
				mySmartUpload.upload();

			} catch (Exception e){
				Logger.Log(Logger.LOG_ERROR, e);
				errInfo.saveError(FileErrInfo.FILESIZE_ERR);
				errInfo.saveToRequest("err", request);
				dispatcher.forward(request, response);
			}
		
			SimpleDateFormat sf = new SimpleDateFormat("/yyyy/MM/dd");
			String uploadDirName = "/upload/photo";//�ϴ�Ŀ¼��
			String spath=uploadDirName + sf.format(new java.util.Date());
			String uploadPath = config.getServletContext().getRealPath(spath);//�ϴ�����·��
			if(!FileUtils.newFolder(uploadPath)){
				errInfo.saveError(FileErrInfo.FILE_ERR);
				errInfo.saveToRequest("err", request);
				dispatcher.forward(request, response);
			}

			String fn=null;
			StringBuffer filePath=new StringBuffer(uploadPath);
			try{
				//����ͼƬ�ļ�������������
				com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					String filename = myFile.getFileName();
					String extendName = StringUtils.getFileExtendName(filename);
					fn = loginUser.getId()+"."+extendName;
					filePath.append(java.io.File.separator);
					filePath.append(fn);
	
					myFile.saveAs(filePath.toString(), com.jspsmart.upload.SmartUpload.SAVE_PHYSICAL);
				}
	
			}catch(Exception fex){
				errInfo.saveError(FileErrInfo.FILE_ERR);
				errInfo.saveToRequest("err", request);
				dispatcher.forward(request, response);
			}
			
			//�����û����ݣ�ָ�����ϴ�����Ƭ
			if(errInfo.isEmpty()&&(fn!=null)){
				StringBuffer pho=new StringBuffer(spath);
				pho.append('/');
				pho.append(fn);
				pho.append(',');
				pho.append(FileUtils.getImageSize(filePath.toString()));
				
				loginUser.setPhoto(pho.toString());
				try{
					int ret=AuthUserDAO.updateUserF(loginUser);
					if(ret!=1)
						errInfo.saveError(FileErrInfo.ERR_UNKNOWN);
				}catch(Exception e){
					Logger.Log(Logger.LOG_ERROR, e);
					errInfo.saveError(FileErrInfo.DATABASE_ERR);
				}
			}
		}
			
		if(!errInfo.isEmpty()){
			loginUser.setPhoto(photo);
			errInfo.saveToRequest("err",request);
			dispatcher.forward(request, response);
		}
		
		response.sendRedirect("/profile/photo"+loginUser.getUserType()+".jsp");
	}
	
	/**
	 * ����servlet
	 */
	public void  destroy () {
	}

}